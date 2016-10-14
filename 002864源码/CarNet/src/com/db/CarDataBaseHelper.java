package com.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

public class CarDataBaseHelper extends SQLiteOpenHelper {
	
	/*
	brand : 宝马, symble:  baoma  ,
	style:宝马SUV ,car_number:鲁A6071，
	engine:325 , level : 4|5,  miles : 123.4 ,
	oil : 40.5 ,engine_feature：, good 
	transmision：good ,light：good   
	另外加一item : 所属用户(用户账号电话号码)
	*/
	private static SQLiteDatabase mDb;
	private static CarDataBaseHelper mHelper;
	private static final int DB_VERSION = 3;
	private static final String DB_NAME = "carstore_new";
	private static final String TABLE_CAR = "car_info";

	
	public static SQLiteDatabase getInstance(Context context) {
		if (mDb == null) {
			mDb = getHelper(context).getWritableDatabase();
		}
		return mDb;
	}
	
	public static CarDataBaseHelper getHelper(Context context) {
		if(mHelper == null) {
			mHelper = new CarDataBaseHelper(context);
		}
		return mHelper;
	}
	
	
	public CarDataBaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}
	
	public CarDataBaseHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	} 

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i("cumt","创建被执行了呢！");
		db.execSQL("create table "
				+ TABLE_CAR
				+ " (brand varchar(10), symble varchar(15), style varchar(10), car_number varchar(10), "
				+ "engine varchar(15), level varchar(5), miles varchar(10), oil varchar(5), engine_feature varchar(5), "
				+ "transmation varchar(5), light varchar(5), user varchar(12), vernum varchar(17), enginenum varchar(8))");
		Log.i("cumt","创建成功！");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (newVersion > oldVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_CAR);
			onCreate(db);
		}
	}
	
	public void deleteTables(Context context) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_CAR, null, null);
	}
}
