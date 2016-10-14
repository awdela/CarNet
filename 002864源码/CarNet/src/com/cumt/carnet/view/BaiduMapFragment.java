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
 * ������BaiduMapFragment ���ã��ٶȵ�ͼfragment�����ذٶȵ�ͼview,ʵ�ְٶȵ�ͼ�����ڹ���
 * 
 * @author wangcan
 * 
 */
public class BaiduMapFragment extends Fragment implements IMapinitPresenter {
	
	private TextureMapView mMapView = null; // �ٶȵ�ͼview
	private BaiduMap mBaiduMap = null;// �ٶȵ�ͼ����,���ʰٶȵ�ͼAPI
	private LocationClient mLocClient = null;// ��λ��ط������
	private HashMap<Marker, GasItemBean> hashMap = null;//��ϣ��
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
		// ��ȡ�ٶȵ�ͼ�ؼ�����
		mMapView = (TextureMapView) view.findViewById(R.id.bmapView);
		if (mBaiduMap == null) {
			mBaiduMap = mMapView.getMap();// ��ȡ�ٶȵ�ͼAPI����
			mBaiduMap.setOnMapLongClickListener(new MapLongClickListener(getActivity(), mBaiduMap));
			initMapConfig();
		}
		initMyPositon();
		return view;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// ��activityִ��onDestroyʱִ��mMapView.onDestroy()��ʵ�ֵ�ͼ�������ڹ���
		if(mMapView != null){
			//mMapView.onDestroy();
			mBaiduMap.setMyLocationEnabled(false);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		// ��activityִ��onResumeʱִ��mMapView. onResume ()��ʵ�ֵ�ͼ�������ڹ���
		if(mMapView != null){
			mMapView.onResume();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		// ��activityִ��onPauseʱִ��mMapView. onPause ()��ʵ�ֵ�ͼ�������ڹ���
		if(mMapView != null){
			mMapView.onPause();
		}
	}

	// �򿪶�λ���ҵ�λ�÷���
	public void initMyPositon() {
		// ��λ���
		// ������λͼ��
		mBaiduMap.setMyLocationEnabled(true);
		// ��λ��ʼ��
		mLocClient = new LocationClient(getActivity().getApplicationContext());
		// ����listener
		MyLocationListener myListener = new MyLocationListener(mMapView,
				getActivity().getApplicationContext(), mBaiduMap, mLocClient);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// ��gps
		option.setCoorType("bd09ll"); // ������������
		option.setScanSpan(4000);
		option.setIsNeedAddress(true);
		mLocClient.setLocOption(option);
		mLocClient.start();// ������λ����
		
	}
	
	@Override
	public void onStart() {
		super.onStart();
		if(mLocClient.isStarted() == false){
			mLocClient.start();//������λ����
		}
	}
	
	@Override
	public void onStop() {
		super.onStop();
		mBaiduMap.setMyLocationEnabled(false);
		mLocClient.stop();//ֹͣ��λ����
	}
	// ��ȡ�ҵĵ�ǰλ��
	public void getMyPosition() {

	}

	// ��ʼ���ٶȵ�ͼ��Ϣ
	public void initMapConfig() {
		//���ñ����ߴ�С
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(14).build()));
		mBaiduMap.setOnMarkerClickListener(new MarkerClickListener(hashMap, this.getActivity()));
		mapinitPresenter = new MapinitPresenter(getActivity());
		mapinitPresenter.setInitMapType(mBaiduMap);
	}
	
	// ����BaiduMap�м���վ��ϢUI
	public void addGasToMap(String gas_message_result){
		if(hashMap != null){
			//����Iterator����HashMap  ���ԭ����ʾ�ļ���վMarker
	        Iterator<Marker> it = hashMap.keySet().iterator();  
	        while(it.hasNext()) {  
	        	Marker key = (Marker) it.next();
	        	key.remove();
	        }
			hashMap.clear();
		}
		ArrayList<GasItemBean> gasItemList = REUtils.getGasItemBeanList(gas_message_result);
//		Log.i("�������,price",gasItemList.get(0).getPrice());
		//Toast.makeText(getActivity(),gasItemList.get(0).getPrice(), Toast.LENGTH_SHORT).show();
		for(GasItemBean item:gasItemList){
			double lon = Double.valueOf(item.getLon());
			double lat = Double.valueOf(item.getLat());
			LatLng myPosition = new LatLng(lat,lon);
			BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory  
				    .fromResource(R.drawable.oil_far);  
			//mBaiduMap.clear();//��ս���
			Marker marker = NormalMethodsUtils.addNoteMarkerMap(myPosition, mBaiduMap, bitmapDescriptor);
			//��marker�����Ϣ
			hashMap.put(marker, item);//ʹ�ù�ϣ����
		}
		//����ļ���վ
		BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory  
			    .fromResource(R.drawable.oil_near); 
		try{
			NormalMethodsUtils.getLatestDistanceOfGas(hashMap).setIcon(bitmapDescriptor);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}