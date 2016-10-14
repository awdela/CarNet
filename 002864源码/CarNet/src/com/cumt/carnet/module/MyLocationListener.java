package com.cumt.carnet.module;

import android.content.Context;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.cumt.carnet.R;
import com.cumt.util.NormalMethodsUtils;
import com.cumt.util.SPUtils;

public class MyLocationListener implements BDLocationListener {
	
	private TextureMapView mMapView;
	private Context context;
	private BaiduMap mBaiduMap;
	@SuppressWarnings("unused")
	private LocationClient mLocClient;
	private Marker marker_a = null;
	
	public MyLocationListener(TextureMapView mMapView, Context context,
			BaiduMap mBaiduMap, LocationClient mLocClient) {
		super();
		this.mMapView = mMapView;
		this.context = context;
		this.mBaiduMap = mBaiduMap;
		this.mLocClient = mLocClient;
	}
	
	public void onReceiveLocation(BDLocation location) {
		// map view 销毁后不在处理新接收的位置
		if (location == null || mMapView == null)
			return;	
		showPosition(location);
		//mLocClient.stop();//关闭监听服务
	}
	//以图标的形式显示用户的位置信息
	private void showPosition(BDLocation location){
		
		LatLng myPosition = new LatLng(location.getLatitude(),
				location.getLongitude());
//		Log.d("positionX",""+location.getLatitude());
//		Log.d("positionY",""+location.getLongitude());
		MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(myPosition);
		mBaiduMap.animateMapStatus(u);
		BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory  
			    .fromResource(R.drawable.location_now);  
		if(marker_a != null){
			marker_a.remove();
		}
//		mBaiduMap.clear();//清空界面
		marker_a = NormalMethodsUtils.addNoteMarkerMap(myPosition, mBaiduMap, bitmapDescriptor);
		marker_a.setTitle("MyPosition");
		//写入SharedPreferences经纬度
		SPUtils.put(context, "Lontitude", (float)location.getLongitude());//写入经度
		SPUtils.put(context, "Latitude", (float)location.getLatitude());//写入纬度
		//写入当前城市
		SPUtils.put(context, "Cityname",location.getCity());
	}
}