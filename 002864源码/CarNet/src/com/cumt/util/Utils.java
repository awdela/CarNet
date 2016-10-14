package com.cumt.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;

public class Utils {
	
	
	/**
	 * Convert Dp to Pixel
	 */
	public static int dpToPx(float dp, Resources resources){
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
		return (int) px;
	}
	
	public static int getRelativeTop(View myView) {
//	    if (myView.getParent() == myView.getRootView())
	    if(myView.getId() == android.R.id.content)
	        return myView.getTop();
	    else
	        return myView.getTop() + getRelativeTop((View) myView.getParent());
	}
	
	public static int getRelativeLeft(View myView) {
//	    if (myView.getParent() == myView.getRootView())
		if(myView.getId() == android.R.id.content)
			return myView.getLeft();
		else
			return myView.getLeft() + getRelativeLeft((View) myView.getParent());
	}
	
	public static Date getTimeDate(){  
        long time=System.currentTimeMillis();//long now = android.os.SystemClock.uptimeMillis();  
//        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        Date date = new Date(time);  
        return date;
//        String t1=format.format(d1);  
//        Log.e("msg", t1);  
    }  
	//获取当前时间
	@SuppressLint("SimpleDateFormat")
	public static String getTimeString(Date date){  
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        String t = format.format(date);  
        return t;
//        Log.e("msg", t1);  
    }  
	
	/**
	 * 直接获取时间
	 * @return
	 */
	public static String getTime(){
		long time=System.currentTimeMillis();//long now = android.os.SystemClock.uptimeMillis();  
//      SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		Date date = new Date(time);  
		return getTimeString(date);
	}
}
