package com.db;

import java.util.ArrayList;
import java.util.List;

import com.app.CarnetApplication;
import com.cumt.book.entity.BookBean;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class BookInfoDao {

	private static final String TABLE_BOOK = "book_info";
	private Context mContext;
	
	public BookInfoDao(Context context){
		this.mContext = context;
	}
	/**
	 * 写入一条订单记录
	 * @param bookBean
	 */
	public void saveBookInfoBean(BookBean bookBean){
		SQLiteDatabase db = BookDataBaseHelper.getInstance(mContext);
		ContentValues cv = new ContentValues();
		cv.put("phone_number", bookBean.getPhone_number());
		cv.put("username", bookBean.getUsername());
		cv.put("gasstation_name", bookBean.getGasstation_name());
		cv.put("gas_address", bookBean.getGas_address());
		cv.put("oilStyle", bookBean.getOilStyle());
		cv.put("oil_price", bookBean.getOil_price());
		cv.put("total_price", bookBean.getTotal_price());
		cv.put("count", bookBean.getCount());
		cv.put("oil_time", bookBean.getOil_time());
		cv.put("make_date", "未完成");
		cv.put("gas_state", bookBean.getGas_state());
		cv.put("gas_lon", bookBean.getGas_lon());
		cv.put("gas_lat", bookBean.getGas_lat());
		cv.put("car_number", bookBean.getCar_number());
		db.insert(TABLE_BOOK, null, cv);
		Log.i("booking","订单写入成功!!!!");
	}
	/**
	 * 获取当前用户的所有的订单信息
	 * @return
	 */
	public List<BookBean> getBookInfo(){
		SQLiteDatabase db = BookDataBaseHelper.getInstance(mContext);
		String sql = "select * from " + TABLE_BOOK +" where phone_number = ?" ;
		CarnetApplication carnetApplication = CarnetApplication.getInstance();
		String user = carnetApplication.getUsername();
		return parseCursor(db.rawQuery(sql, new String[]{user}));
	}
	/**
	 * 获取已完成订单信息
	 * @return
	 */
	public List<BookBean> getFinishBookInfo(){
		SQLiteDatabase db = BookDataBaseHelper.getInstance(mContext);
		String sql = "select * from " + TABLE_BOOK +" where phone_number = ? and gas_state = 1" ;
		CarnetApplication carnetApplication = CarnetApplication.getInstance();
		String user = carnetApplication.getUsername();
		return parseCursor(db.rawQuery(sql, new String[]{user}));
	}
	
	/**
	 * 获取未完成订单信息
	 * @return
	 */
	public List<BookBean> getNotFinishBookInfo(){
		SQLiteDatabase db = BookDataBaseHelper.getInstance(mContext);
		String sql = "select * from " + TABLE_BOOK +" where phone_number = ? and gas_state != 1" ;
		CarnetApplication carnetApplication = CarnetApplication.getInstance();
		String user = carnetApplication.getUsername();
		return parseCursor(db.rawQuery(sql, new String[]{user}));
	}
	
	/**
	 * 获取当前预约的订单信息
	 * @return
	 */
	public List<BookBean> getNowNotFinishBookInfo(){
		SQLiteDatabase db = BookDataBaseHelper.getInstance(mContext);
		String sql = "select * from " + TABLE_BOOK +" where phone_number = ? and gas_state = 0" ;
		CarnetApplication carnetApplication = CarnetApplication.getInstance();
		String user = carnetApplication.getUsername();
		return parseCursor(db.rawQuery(sql, new String[]{user}));
	}
	/**
	 * 更新订单的状态
	 * @param newState
	 * @param time
	 */
	public void updateBookState(int newState,String time){
		SQLiteDatabase db = BookDataBaseHelper.getInstance(mContext);
		CarnetApplication carnetApplication = CarnetApplication.getInstance();
		String user = carnetApplication.getUsername();
		ContentValues updatedValues = new ContentValues();
		updatedValues.put("gas_state", newState);
		String whereClause = "phone_number = ? and oil_time = ?";
		db.update(TABLE_BOOK, updatedValues, whereClause, new String[]{user,time});
	}
	
	
	private List<BookBean> parseCursor(Cursor cursor) {
		List<BookBean> list = new ArrayList<BookBean>();
		while(cursor.moveToNext()) {
			BookBean bookInfo = new BookBean();
			bookInfo.setPhone_number(cursor.getString(cursor.getColumnIndex("phone_number")));
			bookInfo.setUsername(cursor.getString(cursor.getColumnIndex("username")));
			bookInfo.setGasstation_name(cursor.getString(cursor.getColumnIndex("gasstation_name")));
			bookInfo.setGas_address(cursor.getString(cursor.getColumnIndex("gas_address")));
			bookInfo.setOilStyle(cursor.getString(cursor.getColumnIndex("oilStyle")));
			bookInfo.setOil_price(cursor.getDouble(cursor.getColumnIndex("oil_price")));
			bookInfo.setTotal_price(cursor.getDouble(cursor.getColumnIndex("total_price")));
			bookInfo.setCount(cursor.getDouble(cursor.getColumnIndex("count")));
			bookInfo.setOil_time(cursor.getString(cursor.getColumnIndex("oil_time")));
			bookInfo.setMake_date(cursor.getString(cursor.getColumnIndex("make_date")));
			bookInfo.setGas_state(cursor.getInt(cursor.getColumnIndex("gas_state")));
			bookInfo.setGas_lon(cursor.getString(cursor.getColumnIndex("gas_lon")));
			bookInfo.setGas_lat(cursor.getString(cursor.getColumnIndex("gas_lat")));
			bookInfo.setCar_number(cursor.getString(cursor.getColumnIndex("car_number")));
			list.add(bookInfo);
		}
		cursor.close();
		return list;
	}
	
	/**
	 * 数据库中是否有该用户的数据
	 * @return
	 */
	public boolean hasData() {
		SQLiteDatabase db = BookDataBaseHelper.getInstance(mContext);
		String sql = "select count(*) from " + TABLE_BOOK + " where phone_number = ?";
		CarnetApplication carnetApplication = CarnetApplication.getInstance();
		String user = carnetApplication.getUsername();
		Cursor cursor = db.rawQuery(sql, new String[]{user});
		boolean has = false;
		if(cursor.moveToFirst()) {
			int count = cursor.getInt(0);
			if(count > 0) {
				has = true;
			}
		}
		cursor.close();
		return has;
	}
	
	/**
	 * 从数据库根据订单时间删除一条数据
	 * @param time
	 */
	public void deleteBookItem(String time){
		CarnetApplication carnetApplication = CarnetApplication.getInstance();
		String user = carnetApplication.getUsername();
		SQLiteDatabase db = BookDataBaseHelper.getInstance(mContext);
		db.delete(TABLE_BOOK, "phone_number=? and oil_time=?", new String[]{user,time});
	}
	
}
