package com.cumt.carnet.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;

import com.cumt.carnet.R;
import com.cumt.carnet.entity.GasItemBean;
import com.cumt.carnet.module.MapLongClickListener;
import com.cumt.carnet.module.MarkerClickListener;
import com.cumt.carnet.module.MyLocationListener;
import com.cumt.carnet.presenter.IMapinitPresenter;
import com.cumt.carnet.presenter.MapinitPresenter;
import com.cumt.util.NormalMethodsUtils;
import com.cumt.util.REUtils;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 类名：BaiduMapFragment 作用：百度地图fragment，加载百度地图view,实现百度地图的周期管理
 * 
 * @author wangcan
 * 
 */
public class BaiduMapFragment extends Fragment implements IMapinitPresenter {
	
	private TextureMapView mMapView = null; // 百度地图view
	private BaiduMap mBaiduMap = null;// 百度地图对象,访问百度地图API
	private LocationClient mLocClient = null;// 定位相关服务对象
	private HashMap<Marker, GasItemBean> hashMap = null;//哈希表
	private MapinitPresenter mapinitPresenter = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		hashMap = new HashMap<Marker,GasItemBean>();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_mapview, container,
				false);
		// 获取百度地图控件引用
		mMapView = (TextureMapView) view.findViewById(R.id.bmapView);
		if (mBaiduMap == null) {
			mBaiduMap = mMapView.getMap();// 获取百度地图API对象
			mBaiduMap.setOnMapLongClickListener(new MapLongClickListener(getActivity(), mBaiduMap));
			initMapConfig();
		}
		initMyPositon();
		return view;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
		if(mMapView != null){
			//mMapView.onDestroy();
			mBaiduMap.setMyLocationEnabled(false);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		// 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
		if(mMapView != null){
			mMapView.onResume();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		// 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
		if(mMapView != null){
			mMapView.onPause();
		}
	}

	// 打开定位到我的位置服务
	public void initMyPositon() {
		// 定位相关
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		// 定位初始化
		mLocClient = new LocationClient(getActivity().getApplicationContext());
		// 创建listener
		MyLocationListener myListener = new MyLocationListener(mMapView,
				getActivity().getApplicationContext(), mBaiduMap, mLocClient);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(4000);
		option.setIsNeedAddress(true);
		mLocClient.setLocOption(option);
		mLocClient.start();// 开启定位服务
		
	}
	
	@Override
	public void onStart() {
		super.onStart();
		if(mLocClient.isStarted() == false){
			mLocClient.start();//开启定位服务
		}
	}
	
	@Override
	public void onStop() {
		super.onStop();
		mBaiduMap.setMyLocationEnabled(false);
		mLocClient.stop();//停止定位服务
	}
	// 获取我的当前位置
	public void getMyPosition() {

	}

	// 初始化百度地图信息
	public void initMapConfig() {
		//设置比例尺大小
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(14).build()));
		mBaiduMap.setOnMarkerClickListener(new MarkerClickListener(hashMap, this.getActivity()));
		mapinitPresenter = new MapinitPresenter(getActivity());
		mapinitPresenter.setInitMapType(mBaiduMap);
	}
	
	// 更新BaiduMap中加油站信息UI
	public void addGasToMap(String gas_message_result){
		if(hashMap != null){
			//采用Iterator遍历HashMap  清空原来显示的加油站Marker
	        Iterator<Marker> it = hashMap.keySet().iterator();  
	        while(it.hasNext()) {  
	        	Marker key = (Marker) it.next();
	        	key.remove();
	        }
			hashMap.clear();
		}
		ArrayList<GasItemBean> gasItemList = REUtils.getGasItemBeanList(gas_message_result);
//		Log.i("解析结果,price",gasItemList.get(0).getPrice());
		//Toast.makeText(getActivity(),gasItemList.get(0).getPrice(), Toast.LENGTH_SHORT).show();
		for(GasItemBean item:gasItemList){
			double lon = Double.valueOf(item.getLon());
			double lat = Double.valueOf(item.getLat());
			LatLng myPosition = new LatLng(lat,lon);
			BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory  
				    .fromResource(R.drawable.oil_far);  
			//mBaiduMap.clear();//清空界面
			Marker marker = NormalMethodsUtils.addNoteMarkerMap(myPosition, mBaiduMap, bitmapDescriptor);
			//给marker添加信息
			hashMap.put(marker, item);//使用哈希表保存
		}
		//最近的加油站
		BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory  
			    .fromResource(R.drawable.oil_near); 
		try{
			NormalMethodsUtils.getLatestDistanceOfGas(hashMap).setIcon(bitmapDescriptor);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}