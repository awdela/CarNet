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
	
	private HashMap<Marker, GasItemBean> hashMap;//��ϣ��
	private Context context;
	
	public MarkerClickListener(HashMap<Marker, GasItemBean> hashMap,
			Context context) {
		this.hashMap = hashMap;
		this.context = context;
	}

	//�ٶȵ�ͼMarker����¼�
	public boolean onMarkerClick(Marker marker) {
		
		//���marker���ٹ�ϣ���У���ֱ���˳�������ʾ�Ի���
		//����Ϊ�˱����û���ǰλ��marker��Ӱ�죬��Ϊ�˴�marker����¼�
		//Ҳ���⵽�û���ǰλ��marker�ĵ���¼�
		if(!hashMap.containsKey(marker))
			return false;
		final GasItemBean gasItemBean = hashMap.get(marker);
		String name = gasItemBean.getName();
		String address = gasItemBean.getAddress();
		String distance = gasItemBean.getDistance();
		final Dialog dialog = new Dialog(
				context,name,
				address+",�������ľ���Ϊ:"+distance+"��","����");
		dialog.addCancelButton("ȡ��");
		
		dialog.setOnAcceptButtonClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(context, BookActivity.class);
				//����һ�����л���GasItemBean���󣬰�������վ�������Ϣ
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
