package com.cumt.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.cumt.carnet.entity.GasItemBean;
import com.cumt.zxing.entity.ZxingBean;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

/**
 * 类名：NormalMethodsUtils 作用：通用工具类，包含了一些常用的方法
 * 
 * @author wangcan
 * 
 */
public class NormalMethodsUtils {

	/**
	 * activity跳转
	 * 
	 * @param packageContext
	 * @param cls
	 * @param activity
	 */
	public static void goNextActivity(Context packageContext, Class<?> cls,
			Activity activity) {
		Intent intent = new Intent(packageContext, cls);
		activity.startActivity(intent);
	}

	/**
	 * 用于以图标的形式添加到主界面地图上
	 * 
	 * @param LatLng
	 *            point 经纬度 标记的位置
	 * @param BaiduMap
	 *            百度地图对象
	 * @param BitmapDescriptor
	 *            要显示的标记图片
	 * @return Marker 用于管理标记
	 */
	public static Marker addNoteMarkerMap(LatLng point, BaiduMap mBaiduMap,
			BitmapDescriptor bitmap) {
		MarkerOptions option = new MarkerOptions().position(point).icon(bitmap);
		Marker marker = (Marker) mBaiduMap.addOverlay(option);
		return marker;
	}

	/**
	 * 求距离用户最近的加油站
	 * 
	 * @param hashMap
	 * @return
	 */
	public static Marker getLatestDistanceOfGas(
			HashMap<Marker, GasItemBean> hashMap) {
		Marker mark = null;
		double distance = 10000.0d;
		// 采用Iterator遍历HashMap
		Iterator<Marker> it = hashMap.keySet().iterator();
		while (it.hasNext()) {
			Marker key = (Marker) it.next();
			double dis = Double.valueOf(hashMap.get(key).getDistance());
			if (dis < distance) {
				distance = dis;
				mark = key;
			}
		}
		return mark;
	}

	/**
	 * 获取手机SD卡路径
	 * 
	 * @return true:后台
	 */
	public static String getSdcardDir() {
		if (Environment.getExternalStorageState().equalsIgnoreCase(
				Environment.MEDIA_MOUNTED)) {
			return Environment.getExternalStorageDirectory().toString();
		}
		return null;
	}

	/**
	 * 获取当前用户名
	 * 
	 * @return 用户名
	 */
	public static String getUserName(Context context) {
		return (String) SPUtils.get(context, "Phonenumber", "15152113686");
	}

	/**
	 * Android 判断app是否在前台还是在后台运行
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isBackground(Context context) {
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		for (RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(context.getPackageName())) {
				/*
				 * BACKGROUND=400 EMPTY=500 FOREGROUND=100 GONE=1000
				 * PERCEPTIBLE=130 SERVICE=300 ISIBLE=200
				 */
				Log.i(context.getPackageName(), "此appimportace ="
						+ appProcess.importance
						+ ",context.getClass().getName()="
						+ context.getClass().getName());
				if (appProcess.importance != RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
					Log.i(context.getPackageName(), "处于后台"
							+ appProcess.processName);
					return true;
				} else {
					Log.i(context.getPackageName(), "处于前台"
							+ appProcess.processName);
					return false;
				}
			}
		}
		return false;
	}

	/**
	 * 转换解析后的二维码的数据的格式，存储在Zxingbena对象中
	 * 
	 * @param zxingData
	 * @return
	 */
	public static ZxingBean getZxingBean(String zxingData) {
		List<String> list = new ArrayList<String>();
		String[] result_dot = zxingData.split(",");
		for (String item : result_dot) {
			String[] temp = item.split(":");
			list.add(temp[1]);
		}
		ZxingBean zxingBean = new ZxingBean();
		zxingBean.setBrand(list.get(0));
		zxingBean.setSymbol(list.get(1));
		zxingBean.setStyle(list.get(2));
		zxingBean.setCar_number(list.get(3));
		zxingBean.setEngine(list.get(4));
		zxingBean.setLevel(list.get(5));
		zxingBean.setMiles(list.get(6));
		zxingBean.setOil(list.get(7));
		zxingBean.setEngine_feature(list.get(8));
		zxingBean.setTransmation(list.get(9));
		zxingBean.setlight(list.get(10));
		zxingBean.setVernum(list.get(11));
		zxingBean.setEnginenum(list.get(12));
		return zxingBean;
	}

	/**
	 * 删除单个文件
	 * 
	 * @param filePath
	 *            被删除文件的文件名
	 * @return 文件删除成功返回true，否则返回false
	 */
	public static boolean deleteFile(String filePath) {
		File file = new File(filePath);
		if (file.isFile() && file.exists()) {
			return file.delete();
		}
		return false;
	}
	
	
}