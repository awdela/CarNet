package com.cumt.carnet.module;

import java.util.HashMap;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.Marker;
import com.cumt.book.view.BookActivity;
import com.cumt.carnet.entity.GasItemBean;
import com.cumt.view.Dialog;

public class MarkerClickListener implements OnMarkerClickListener{
	
	private HashMap<Marker, GasItemBean> hashMap;//哈希表
	private Context context;
	
	public MarkerClickListener(HashMap<Marker, GasItemBean> hashMap,
			Context context) {
		this.hashMap = hashMap;
		this.context = context;
	}

	//百度地图Marker点击事件
	public boolean onMarkerClick(Marker marker) {
		
		//如果marker不再哈希表中，则直接退出，不显示对话框，
		//这是为了避免用户当前位置marker的影响，因为此处marker点击事件
		//也会检测到用户当前位置marker的点击事件
		if(!hashMap.containsKey(marker))
			return false;
		final GasItemBean gasItemBean = hashMap.get(marker);
		String name = gasItemBean.getName();
		String address = gasItemBean.getAddress();
		String distance = gasItemBean.getDistance();
		final Dialog dialog = new Dialog(
				context,name,
				address+",距离您的距离为:"+distance+"米","详情");
		dialog.addCancelButton("取消");
		
		dialog.setOnAcceptButtonClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(context, BookActivity.class);
				//传递一个序列化的GasItemBean对象，包含加油站的相关信息
				intent.putExtra("MarkerGasItem",gasItemBean);
				dialog.dismiss();
				context.startActivity(intent);
//				((Activity) context).finish();
				
			}
		});
		dialog.setOnCancelButtonClickListener(new OnClickListener() {

			public void onClick(View v) {
				
			}
		});
		dialog.show();
		return false;
	}
}
