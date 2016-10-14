package com.cumt.carnet.entity;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class MusicInfo implements Parcelable {
	
	public final static String KEY_MUSIC= "music";
	
	public static final String KEY_ID = "_id";
	public static final String KEY_SONG_ID = "songid";
	public static final String KEY_ALBUM_ID = "albumid";
	public static final String KEY_DURATION = "duration";
	public static final String KEY_MUSIC_NAME = "musicname";
	public static final String KEY_ARTIST = "artist";
	public static final String KEY_DATA = "data";
	public static final String KEY_FOLDER = "folder";
	public static final String KEY_MUSIC_NAME_KEY = "musicnamekey";
	public static final String KEY_ARTIST_KEY = "artistkey";
	public static final String KEY_FAVORITE = "favorite";
	//2016/4/24新增
	public static final String KEY_ALBUM = "album";
	public static final String KEY_COMPOSER = "composer";
	public static final String KEY_YEAR = "year";
	public static final String KEY_SIZE = "size";
	
	/** 数据库中的_id */
	public int _id = -1;
	public int songId = -1;
	public int albumId = -1;
	public int duration;
	public String musicName;
	public String artist;
	public String data;
	public String folder;
	public String musicNameKey;
	public String artistKey;
	
	public String album;
	public String composer;
	public String year;
	public String size;
	/** 0表示没有收藏 1表示收藏 */
	public int favorite = 0;
	
	
	public int describeContents() {
		return 0;
	}
	
	public void writeToParcel(Parcel dest, int flags) {
		Bundle bundle = new Bundle();
		bundle.putInt(KEY_ID, _id);
		bundle.putInt(KEY_SONG_ID, songId);
		bundle.putInt(KEY_ALBUM_ID, albumId);
		bundle.putInt(KEY_DURATION, duration);
		bundle.putString(KEY_MUSIC_NAME, musicName);
		bundle.putString(KEY_ARTIST, artist);
		bundle.putString(KEY_DATA, data);
		bundle.putString(KEY_FOLDER, folder);
		bundle.putString(KEY_MUSIC_NAME_KEY, musicNameKey);
		bundle.putInt(KEY_FAVORITE, favorite);
		bundle.putString(KEY_ALBUM, album);
		bundle.putString(KEY_COMPOSER, composer);
		bundle.putString(KEY_YEAR, year);
		bundle.putString(KEY_SIZE, size);
		dest.writeBundle(bundle);
	}
	
	public static final Parcelable.Creator<MusicInfo> CREATOR = new Parcelable.Creator<MusicInfo>() {

		public MusicInfo createFromParcel(Parcel source) {
			MusicInfo music = new MusicInfo();
			Bundle bundle = new Bundle();
			bundle = source.readBundle();
			music._id = bundle.getInt(KEY_ID);
			music.songId = bundle.getInt(KEY_SONG_ID);
			music.albumId = bundle.getInt(KEY_ALBUM_ID);
			music.duration = bundle.getInt(KEY_DURATION);
			music.musicName = bundle.getString(KEY_MUSIC_NAME);
			music.artist = bundle.getString(KEY_ARTIST);
			music.data = bundle.getString(KEY_DATA);
			music.folder = bundle.getString(KEY_FOLDER);
			music.musicNameKey = bundle.getString(KEY_MUSIC_NAME_KEY);
			music.favorite = bundle.getInt(KEY_FAVORITE);
			music.album = bundle.getString(KEY_ALBUM);
			music.year = bundle.getString(KEY_YEAR);
			music.composer = bundle.getString(KEY_COMPOSER);
			music.size = bundle.getString(KEY_SIZE);
			return music;
		}

		public MusicInfo[] newArray(int size) {
			return new MusicInfo[size];
		}
	};


	public int getFavorite() {
		return favorite;
	}
	public void setFavorite(int favorite) {
		this.favorite = favorite;
	}

}