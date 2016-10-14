package com.cumt.carnet.presenter;

import android.content.Context;

import com.baidu.mapapi.map.BaiduMap;
import com.cumt.util.SPUtils;

/**
 * ������MapinitPresenter
 * ����:�ٶȵ�ͼ���ע������߼�
 * @author wangcan
 *
 */
public class MapinitPresenter {
	
	private Context context;
	
	public MapinitPresenter(Context context){
		this.context = context;
	}
	
	public void setInitMapType(BaiduMap mBaiduMap){
		
		if(SPUtils.contains(context, "Maptype")){
			int type = (Integer) SPUtils.get(context, "Maptype", 0);
			switch(type){
			case 0://��ͨ��ͼ  
				mBaiduMap.setBaiduHeatMapEnabled(false);
        		mBaiduMap.setBaiduHeatMapEnabled(false);
        		mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL); 
				break;
			case 1://���ǵ�ͼ  
				mBaiduMap.setBaiduHeatMapEnabled(false);
        		mBaiduMap.setBaiduHeatMapEnabled(false);
        		mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
				break;
			case 2://��������ͼ
				mBaiduMap.setBaiduHeatMapEnabled(false);
        		mBaiduMap.setBaiduHeatMapEnabled(true);
				break;
			}
		}
	}
}
