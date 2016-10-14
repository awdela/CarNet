package com.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MusicDatabaseHelper extends SQLiteOpenHelper {

	private static SQLiteDatabase mDb;
	private static MusicDatabaseHelper mHelper;
	private static final int DB_VERSION = 3;
	private static final String DB_NAME = "musicstore_new";
	private static final String TABLE_MUSIC = "music_info";

	public static SQLiteDatabase getInstance(Context context) {
		if (mDb == null) {
			mDb = getHelper(context).getWritableDatabase();
		}
		return mDb;
	}
	
	public static MusicDatabaseHelper getHelper(Context context) {
		if(mHelper == null) {
			mHelper = new MusicDatabaseHelper(context);
		}
		return mHelper;
	}

	public MusicDatabaseHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	public MusicDatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table "
				+ TABLE_MUSIC
				+ " (_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ " songid integer, albumid integer, duration integer, musicname varchar(10), "
				+ "artist char, data char, folder char, musicnamekey char, artistkey char, favorite integer)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (newVersion > oldVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_MUSIC);
			onCreate(db);
		}
	}
	
	public void deleteTables(Context context) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_MUSIC, null, null);
	}

}
