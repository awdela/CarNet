package com.app;

import java.util.ArrayList;

import com.cumt.carnet.entity.MusicInfo;
import com.cumt.util.MusicUtils;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.OnScanCompletedListener;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.Media;

/**
 * Android的Application类，这个类是app每次启动时最先运行的类 在此类中我们开辟一个线程进行音乐的检索操作，在其他类中可以利用该类的对象
 * 获取得到的音乐信息列表:ArrayList<MusicInfo>
 * 
 * @author wangcan
 * 
 */
public class CarnetApplication extends Application {

	private static CarnetApplication instance;

	private String username;// 全局变量 用户名 即手机号码
	
	private String car_number = null;//用户绑定的车
	
	protected ArrayList<MusicInfo> musicInfoList;

	public static CarnetApplication getInstance() {
		return instance;
	}

	@Override
	public void onCreate() {
		// 更新媒体库
		MediaScannerConnection.scanFile(this, new String[] { Environment
				.getExternalStorageDirectory().getAbsolutePath() }, null,
				new OnScanCompletedListener() {

					public void onScanCompleted(String path, Uri uri) {
						startSearch(getApplicationContext());
					}
				});
		super.onCreate();
		instance = this;
		startSearch(getApplicationContext());
		username = "15152113686";// 默认用户名
		//  initCarObeySearch();
	}

	private void startSearch(final Context context) {

		new Thread(new Runnable() {

			public void run() {
				Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;// -------------
				ContentResolver cr = context.getContentResolver();
				StringBuffer select = new StringBuffer(" 1=1 ");
				// 查询语句：检索出时长大于1分钟，文件大小大于1MB的媒体文件
				select.append(" and " + Media.SIZE + " > "
						+ MusicUtils.FILTER_SIZE);
				select.append(" and " + Media.DURATION + " > "
						+ MusicUtils.FILTER_DURATION);

				musicInfoList = MusicUtils.getMusicList(cr.query(uri,
						MusicUtils.proj_music, select.toString(), null,
						MediaStore.Audio.Media.ARTIST_KEY));
			}
		}).start();
	}

	public ArrayList<MusicInfo> getMusicInfoList() {
		return musicInfoList;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	// 更新媒体库
	public void refreshMedia() {
		// 更新媒体库
		MediaScannerConnection.scanFile(this, new String[] { Environment
				.getExternalStorageDirectory().getAbsolutePath() }, null,
				new OnScanCompletedListener() {

					public void onScanCompleted(String path, Uri uri) {
						startSearch(getApplicationContext());
					}
				});
	}

	public String getCar_number() {
		return car_number;
	}

	public void setCar_number(String car_number) {
		this.car_number = car_number;
	}
	
}
