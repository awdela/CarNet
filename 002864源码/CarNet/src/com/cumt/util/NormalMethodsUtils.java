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
 * ������NormalMethodsUtils ���ã�ͨ�ù����࣬������һЩ���õķ���
 * 
 * @author wangcan
 * 
 */
public class NormalMethodsUtils {

	/**
	 * activity��ת
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
	 * ������ͼ�����ʽ��ӵ��������ͼ��
	 * 
	 * @param LatLng
	 *            point ��γ�� ��ǵ�λ��
	 * @param BaiduMap
	 *            �ٶȵ�ͼ����
	 * @param BitmapDescriptor
	 *            Ҫ��ʾ�ı��ͼƬ
	 * @return Marker ���ڹ�����
	 */
	public static Marker addNoteMarkerMap(LatLng point, BaiduMap mBaiduMap,
			BitmapDescriptor bitmap) {
		MarkerOptions option = new MarkerOptions().position(point).icon(bitmap);
		Marker marker = (Marker) mBaiduMap.addOverlay(option);
		return marker;
	}

	/**
	 * ������û�����ļ���վ
	 * 
	 * @param hashMap
	 * @return
	 */
	public static Marker getLatestDistanceOfGas(
			HashMap<Marker, GasItemBean> hashMap) {
		Marker mark = null;
		double distance = 10000.0d;
		// ����Iterator����HashMap
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
	 * ��ȡ�ֻ�SD��·��
	 * 
	 * @return true:��̨
	 */
	public static String getSdcardDir() {
		if (Environment.getExternalStorageState().equalsIgnoreCase(
				Environment.MEDIA_MOUNTED)) {
			return Environment.getExternalStorageDirectory().toString();
		}
		return null;
	}

	/**
	 * ��ȡ��ǰ�û���
	 * 
	 * @return �û���
	 */
	public static String getUserName(Context context) {
		return (String) SPUtils.get(context, "Phonenumber", "15152113686");
	}

	/**
	 * Android �ж�app�Ƿ���ǰ̨�����ں�̨����
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
				Log.i(context.getPackageName(), "��appimportace ="
						+ appProcess.importance
						+ ",context.getClass().getName()="
						+ context.getClass().getName());
				if (appProcess.importance != RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
					Log.i(context.getPackageName(), "���ں�̨"
							+ appProcess.processName);
					return true;
				} else {
					Log.i(context.getPackageName(), "����ǰ̨"
							+ appProcess.processName);
					return false;
				}
			}
		}
		return false;
	}

	/**
	 * ת��������Ķ�ά������ݵĸ�ʽ���洢��Zxingbena������
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
	 * ɾ�������ļ�
	 * 
	 * @param filePath
	 *            ��ɾ���ļ����ļ���
	 * @return �ļ�ɾ���ɹ�����true�����򷵻�false
	 */
	public static boolean deleteFile(String filePath) {
		File file = new File(filePath);
		if (file.isFile() && file.exists()) {
			return file.delete();
		}
		return false;
	}
	
	
}