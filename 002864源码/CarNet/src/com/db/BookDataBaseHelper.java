package com.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class BookDataBaseHelper extends SQLiteOpenHelper {
	
	private static SQLiteDatabase mDb;
	private static BookDataBaseHelper mHelper;
	
	private static final int DB_VERSION = 3;
	private static final String DB_NAME = "bookstore_new";
	private static final String TABLE_BOOK = "book_info";
	
	public static SQLiteDatabase getInstance(Context context) {
		if (mDb == null) {
			mDb = getHelper(context).getWritableDatabase();//该句会调用onCreate,在onCreate中创建表
		}
		return mDb;
	}
	
	public static BookDataBaseHelper getHelper(Context context) {
		if(mHelper == null) {
			mHelper = new BookDataBaseHelper(context);
		}
		return mHelper;
	}
	

	public BookDataBaseHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	public BookDataBaseHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table "
				+ TABLE_BOOK
				+ " (phone_number varchar(12), username varchar(15), gasstation_name varchar(15), gas_address varchar(100), "
				+ "oilStyle varchar(10), oil_price real, total_price real, count real, oil_time varchar(30), car_number varchar(15), "
				+ "make_date varchar(30), gas_state integer, gas_lon varchar(20),gas_lat varchar(20))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (newVersion > oldVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOK);
			onCreate(db);
		}
	}

}
