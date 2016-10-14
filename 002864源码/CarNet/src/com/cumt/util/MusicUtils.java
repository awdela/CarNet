package com.cumt.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import com.cumt.carnet.entity.MusicInfo;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;

public class MusicUtils {
	
	public static final int START_PLAY = 1;
	public static final int STOP_PLAY = 2;
	public static final int PAUSE_PLAY = 3;
	
	public static final int FILTER_SIZE = 1 * 1024 * 1024;// 1MB
	public static final int FILTER_DURATION = 1 * 60 * 1000;// 1分钟
	
	public static String[] proj_music = new String[] {
		MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE,
		MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.ALBUM_ID,
		MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.ARTIST_ID,
		MediaStore.Audio.Media.DURATION ,MediaStore.Audio.Media.ALBUM,
		MediaStore.Audio.Media.YEAR,MediaStore.Audio.Media.COMPOSER,
		MediaStore.Audio.Media.SIZE};
	
	private static final HashMap<Long, Bitmap> sArtCache = new HashMap<Long, Bitmap>();
	private static final BitmapFactory.Options sBitmapOptionsCache = new BitmapFactory.Options();
	private static final BitmapFactory.Options sBitmapOptions = new BitmapFactory.Options();
	private static final Uri sArtworkUri = Uri
			.parse("content://media/external/audio/albumart");
	
	static {
		// for the cache,
		// 565 is faster to decode and display
		// and we don't want to dither here because the image will be scaled
		// down later
		sBitmapOptionsCache.inPreferredConfig = Bitmap.Config.RGB_565;
		sBitmapOptionsCache.inDither = false;

		sBitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565;
		sBitmapOptions.inDither = false;
	}
	
	// 音乐检索方法
	public static ArrayList<MusicInfo> getMusicList(Cursor cursor) {
		if (cursor == null) {
			return null;
		}
		ArrayList<MusicInfo> musicList = new ArrayList<MusicInfo>();
		while (cursor.moveToNext()) {
			MusicInfo music = new MusicInfo();
			music.songId = cursor.getInt(cursor
					.getColumnIndex(MediaStore.Audio.Media._ID));
			music.albumId = cursor.getInt(cursor
					.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
			music.duration = cursor.getInt(cursor
					.getColumnIndex(MediaStore.Audio.Media.DURATION));
			music.album = cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Media.ALBUM));
			music.year = cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Media.YEAR));
			music.composer = cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Media.COMPOSER));
			Log.i("Name",""+music.duration);//输出时长
			music.musicName = cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Media.TITLE));
			Log.i("Name",music.musicName);//输出名称
			music.artist = cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Media.ARTIST));

			String filePath = cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Media.DATA));
			music.size = cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Media.SIZE));
			music.data = filePath;
			Log.i("Name",music.data);//输出路径
			String folderPath = filePath.substring(0,
					filePath.lastIndexOf(File.separator));
			music.folder = folderPath;
			music.musicNameKey = StringHelper.getPingYin(music.musicName);
			music.artistKey = StringHelper.getPingYin(music.artist);
			
			musicList.add(music);
		}
		cursor.close();
		return musicList;
	}
	/**
	 * 将音乐的时长 (s)转换为时分秒的字符串
	 * @param milliSecs
	 * @return
	 */
	public static String makeTimeString(long milliSecs) {
		StringBuffer sb = new StringBuffer();
		long m = milliSecs / (60 * 1000);
		sb.append(m < 10 ? "0" + m : m);
		sb.append(":");
		long s = (milliSecs % (60 * 1000)) / 1000;
		sb.append(s < 10 ? "0" + s : s);
		return sb.toString();
	}
	/** 
     * byte(字节)根据长度转成kb(千字节)和mb(兆字节) 
     *  
     * @param bytes 
     * @return 
     */  
    public static String bytes2kb(long bytes) {  
        BigDecimal filesize = new BigDecimal(bytes);  
        BigDecimal megabyte = new BigDecimal(1024 * 1024);  
        float returnValue = filesize.divide(megabyte, 2, BigDecimal.ROUND_UP)  
                .floatValue();  
        if (returnValue > 1)  
            return (returnValue + "MB");  
        BigDecimal kilobyte = new BigDecimal(1024);  
        returnValue = filesize.divide(kilobyte, 2, BigDecimal.ROUND_UP)  
                .floatValue();  
        return (returnValue + "KB");  
    }  
    /**
     * 获取音乐的后缀格式
     * @param data
     * @return
     */
    public static String getSuffOfMusicData(String data){
    	int index = data.lastIndexOf(".");
    	return data.substring(index+1);
    }
    /**
     * 获取封面
     * @param context
     * @param artIndex
     * @param defaultArtwork
     * @return
     */
    public static Bitmap getCachedArtwork(Context context, long artIndex,
			Bitmap defaultArtwork) {
		Bitmap bitmap = null;
		synchronized (sArtCache) {
			bitmap = sArtCache.get(artIndex);
		}
		if(context == null) {
			return null;
		}
		if (bitmap == null) {
			bitmap = defaultArtwork;
			int w = bitmap.getWidth();
			int h = bitmap.getHeight();
			Bitmap b = MusicUtils.getArtworkQuick(context, artIndex, w, h);
			if (b != null) {
				bitmap = b;
				synchronized (sArtCache) {
					// the cache may have changed since we checked
					Bitmap value = sArtCache.get(artIndex);
					if (value == null) {
						sArtCache.put(artIndex, bitmap);
					} else {
						bitmap = value;
					}
				}
			}
		}
		return bitmap;
	}
    
    public static Bitmap getArtworkQuick(Context context, long album_id, int w,
			int h) {
		// NOTE: There is in fact a 1 pixel border on the right side in the
		// ImageView
		// used to display this drawable. Take it into account now, so we don't
		// have to
		// scale later.
		w -= 1;
		ContentResolver res = context.getContentResolver();
		Uri uri = ContentUris.withAppendedId(sArtworkUri, album_id);
		if (uri != null) {
			ParcelFileDescriptor fd = null;
			try {
				fd = res.openFileDescriptor(uri, "r");
				int sampleSize = 1;

				// Compute the closest power-of-two scale factor
				// and pass that to sBitmapOptionsCache.inSampleSize, which will
				// result in faster decoding and better quality
				sBitmapOptionsCache.inJustDecodeBounds = true;
				BitmapFactory.decodeFileDescriptor(fd.getFileDescriptor(),
						null, sBitmapOptionsCache);
				int nextWidth = sBitmapOptionsCache.outWidth >> 1;
				int nextHeight = sBitmapOptionsCache.outHeight >> 1;
				while (nextWidth > w && nextHeight > h) {
					sampleSize <<= 1;
					nextWidth >>= 1;
					nextHeight >>= 1;
				}

				sBitmapOptionsCache.inSampleSize = sampleSize;
				sBitmapOptionsCache.inJustDecodeBounds = false;
				Bitmap b = BitmapFactory.decodeFileDescriptor(
						fd.getFileDescriptor(), null, sBitmapOptionsCache);

				if (b != null) {
					// finally rescale to exactly the size we need
					if (sBitmapOptionsCache.outWidth != w
							|| sBitmapOptionsCache.outHeight != h) {
						Bitmap tmp = Bitmap.createScaledBitmap(b, w, h, true);
						// Bitmap.createScaledBitmap() can return the same
						// bitmap
						if (tmp != b)
							b.recycle();
						b = tmp;
					}
				}

				return b;
			} catch (FileNotFoundException e) {
			} finally {
				try {
					if (fd != null)
						fd.close();
				} catch (IOException e) {
				}
			}
		}
		return null;
	}
    
    public static void clearCache() {
		sArtCache.clear();
	}
}
