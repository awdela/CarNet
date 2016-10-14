package com.db;

import java.util.ArrayList;
import java.util.List;

import com.app.CarnetApplication;
import com.cumt.zxing.entity.ZxingBean;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class CarInfoDao {
	
	private static final String TABLE_CAR = "car_info";
	private Context mContext;
	
	public CarInfoDao(Context mContext) {
		super();
		this.mContext = mContext;
	}
	/**
	 * 写入一辆车的信息
	 * @param carInfoBean
	 */
	public void saveCarInfoBean(ZxingBean carInfoBean){
		SQLiteDatabase db = CarDataBaseHelper.getInstance(mContext);
		ContentValues cv = new ContentValues();
		cv.put("brand", carInfoBean.getBrand());
		cv.put("symble", carInfoBean.getSymbol());
		cv.put("style", carInfoBean.getStyle());
		cv.put("car_number", carInfoBean.getCar_number());
		cv.put("engine", carInfoBean.getEngine());//发动机型号
		cv.put("level", carInfoBean.getLevel());
		cv.put("miles", carInfoBean.getMiles());
		cv.put("oil", carInfoBean.getOil());
		cv.put("engine_feature", carInfoBean.getEngine_feature());
		cv.put("transmation", carInfoBean.getTransmation());
		cv.put("light", carInfoBean.getlight());
		CarnetApplication carApplication = CarnetApplication.getInstance();
		String user = carApplication.getUsername();
		cv.put("user", user);//所属用户名
		cv.put("vernum", carInfoBean.getVernum());//车架号
		cv.put("enginenum", carInfoBean.getEnginenum());//发动机号
		Log.i("username:database",user);
		Log.i("write:style",carInfoBean.getStyle());
		db.insert(TABLE_CAR, null, cv);
		Log.i("cumt","写入成功!");
	}
	//查询车辆所有者为当前登录用户所属的车辆信息
	public List<ZxingBean> getCarInfo() {
		SQLiteDatabase db = CarDataBaseHelper.getInstance(mContext);
		String sql = "select * from " + TABLE_CAR +" where user = ?" ;
		CarnetApplication carnetApplication = CarnetApplication.getInstance();
		String user = carnetApplication.getUsername();
		return parseCursor(db.rawQuery(sql, new String[]{user}));
	}
	
	private List<ZxingBean> parseCursor(Cursor cursor) {
		List<ZxingBean> list = new ArrayList<ZxingBean>();
		while(cursor.moveToNext()) {
			ZxingBean carInfo = new ZxingBean();
			carInfo.setBrand(cursor.getString(cursor.getColumnIndex("brand")));
			carInfo.setSymbol(cursor.getString(cursor.getColumnIndex("symble")));
			carInfo.setStyle(cursor.getString(cursor.getColumnIndex("style")));
			carInfo.setCar_number(cursor.getString(cursor.getColumnIndex("car_number")));
			carInfo.setEngine(cursor.getString(cursor.getColumnIndex("engine")));
			carInfo.setLevel(cursor.getString(cursor.getColumnIndex("level")));
			carInfo.setMiles(cursor.getString(cursor.getColumnIndex("miles")));
			carInfo.setOil(cursor.getString(cursor.getColumnIndex("oil")));
			carInfo.setEngine_feature(cursor.getString(cursor.getColumnIndex("engine_feature")));
			carInfo.setTransmation(cursor.getString(cursor.getColumnIndex("transmation")));
			carInfo.setlight(cursor.getString(cursor.getColumnIndex("light")));
			carInfo.setVernum(cursor.getString(cursor.getColumnIndex("vernum")));
			carInfo.setEnginenum(cursor.getString(cursor.getColumnIndex("enginenum")));
			list.add(carInfo);
		}
		cursor.close();
		return list;
	}
	
	/**
	 * 数据库中是否有该用户的数据
	 * @return
	 */
	public boolean hasData() {
		SQLiteDatabase db = CarDataBaseHelper.getInstance(mContext);
		String sql = "select count(*) from " + TABLE_CAR + " where user = ?";
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
	 * 数据库中是否含有某item
	 * @param carInfoBean
	 * @return
	 */
	public boolean hasSomeData(ZxingBean carInfoBean){
		SQLiteDatabase db = CarDataBaseHelper.getInstance(mContext);
		CarnetApplication carnetApplication = CarnetApplication.getInstance();
		String sql = "select * from " + TABLE_CAR + " where car_number = ? and user = ?" ;
		Cursor cursor = db.rawQuery(sql, new String[]{carInfoBean.getCar_number(),
				carnetApplication.getUsername()});
		if(cursor.moveToFirst() == false){
			cursor.close();
			return false;
		}
		cursor.close();
		return true;
	}
	/**
	 * 获取数据库中
	 * @return
	 */
	public int getDataCount() {
		SQLiteDatabase db = CarDataBaseHelper.getInstance(mContext);
		String sql = "select count(*) from " + TABLE_CAR + " where user = ?";
		CarnetApplication carnetApplication = CarnetApplication.getInstance();
		String user = carnetApplication.getUsername();
		Cursor cursor = db.rawQuery(sql, new String[]{user});
		int count = 0;
		if(cursor.moveToFirst()) {
			count = cursor.getInt(0);
		}
		return count;
	}

}
