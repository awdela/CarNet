package com.cumt.carnet.module;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapLongClickListener;
import com.baidu.mapapi.model.LatLng;
import com.cumt.carnet.R;
/**
 * 长时间按地图界面事件处理
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
		final String items[]={"普通地图","卫星地图","热量地图","实时交通图"};  
        AlertDialog.Builder builder=new AlertDialog.Builder(context);  //先得到构造器  
        builder.setTitle("设置地图类型"); //设置标题  
        builder.setIcon(R.drawable.ic_launcher);//设置图标，图片id即可  
        builder.setSingleChoiceItems(items,0,new DialogInterface.OnClickListener() {  
        	
            public void onClick(DialogInterface dialog, int which) {  
            	MapChoiseIndex = which;
            }  
        });  
        builder.setPositiveButton("确定",new DialogInterface.OnClickListener() {  

        	public void onClick(DialogInterface dialog, int which) { 
            	if(MapChoiseIndex==0){//普通地图  
            		mBaiduMap.setBaiduHeatMapEnabled(false);
            		mBaiduMap.setBaiduHeatMapEnabled(false);
            		mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL); 
            	}
            	else if(MapChoiseIndex==1){//卫星地图  
            		mBaiduMap.setBaiduHeatMapEnabled(false);
            		mBaiduMap.setBaiduHeatMapEnabled(false);
            		mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
            	}
            	else if(MapChoiseIndex==2){
            		mBaiduMap.setBaiduHeatMapEnabled(false);
            		mBaiduMap.setBaiduHeatMapEnabled(true);
            	}
            	else if(MapChoiseIndex==3){//开启交通图   
            		mBaiduMap.setBaiduHeatMapEnabled(false);
            		mBaiduMap.setTrafficEnabled(true);
            	}
                dialog.dismiss(); 
            }  
        });  
        builder.create().show(); 
	}

}
