package com.cumt.carnet.module;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapLongClickListener;
import com.baidu.mapapi.model.LatLng;
import com.cumt.carnet.R;
/**
 * ��ʱ�䰴��ͼ�����¼�����
 * @author wangcan
 *
 */
public class MapLongClickListener implements OnMapLongClickListener {
	
	private Context context;
	private int MapChoiseIndex;
	private BaiduMap mBaiduMap;

	public MapLongClickListener(Context context, BaiduMap mBaiduMap) {
		super();
		this.context = context;
		this.mBaiduMap = mBaiduMap;
	}

	public void onMapLongClick(LatLng arg0) {
		dialogChoiseMapType();
	}
	
	private void dialogChoiseMapType(){
		final String items[]={"��ͨ��ͼ","���ǵ�ͼ","������ͼ","ʵʱ��ͨͼ"};  
        AlertDialog.Builder builder=new AlertDialog.Builder(context);  //�ȵõ�������  
        builder.setTitle("���õ�ͼ����"); //���ñ���  
        builder.setIcon(R.drawable.ic_launcher);//����ͼ�꣬ͼƬid����  
        builder.setSingleChoiceItems(items,0,new DialogInterface.OnClickListener() {  
        	
            public void onClick(DialogInterface dialog, int which) {  
            	MapChoiseIndex = which;
            }  
        });  
        builder.setPositiveButton("ȷ��",new DialogInterface.OnClickListener() {  

        	public void onClick(DialogInterface dialog, int which) { 
            	if(MapChoiseIndex==0){//��ͨ��ͼ  
            		mBaiduMap.setBaiduHeatMapEnabled(false);
            		mBaiduMap.setBaiduHeatMapEnabled(false);
            		mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL); 
            	}
            	else if(MapChoiseIndex==1){//���ǵ�ͼ  
            		mBaiduMap.setBaiduHeatMapEnabled(false);
            		mBaiduMap.setBaiduHeatMapEnabled(false);
            		mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
            	}
            	else if(MapChoiseIndex==2){
            		mBaiduMap.setBaiduHeatMapEnabled(false);
            		mBaiduMap.setBaiduHeatMapEnabled(true);
            	}
            	else if(MapChoiseIndex==3){//������ͨͼ   
            		mBaiduMap.setBaiduHeatMapEnabled(false);
            		mBaiduMap.setTrafficEnabled(true);
            	}
                dialog.dismiss(); 
            }  
        });  
        builder.create().show(); 
	}

}
