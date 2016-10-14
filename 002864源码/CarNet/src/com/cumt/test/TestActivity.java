package com.cumt.test;

import java.util.List;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.cumt.carnet.R;
import com.cumt.util.SPUtils;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TestActivity extends Activity {

	private Parameters parameters = null;
	private Camera camera = null;
	private boolean isFirst = true;
	@SuppressWarnings("unused")
	private TextView textView = null;
	private Button btnTest, btnPost,btnPoi,btnCode,btnGeo;
	
	PoiSearch mPoiSearch = null;//Poi检索
	// 创建地理编码检索实例  
    GeoCoder geoCoder = GeoCoder.newInstance();  
    
	// private MyPopupWindow myPopupWindow;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_xml);
		initPoi();
		
		// 设置地理编码检索监听者  
        geoCoder.setOnGetGeoCodeResultListener(listener);  
		// parameters = camera.getParameters();

		btnTest = (Button) findViewById(R.id.buttontest);
		btnTest.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				// myPopupWindow.showPopupWindow();
				if (isFirst) {
					camera = Camera.open();
					parameters = camera.getParameters();
					// 直接开启
					parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);// 开启
					camera.setParameters(parameters);
					isFirst = false;
				} else {
					parameters = camera.getParameters();
					parameters.setFlashMode(Parameters.FLASH_MODE_OFF);// 关闭
					camera.setParameters(parameters);
					camera.release();
					isFirst = true;
				}
			}
		});
		// myPopupWindow = new MyPopupWindow(TestActivity.this,
		// btnTest,TestActivity.this);

		btnPost = (Button) findViewById(R.id.buttonPost);
		btnPost.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				
				new Thread(new Runnable() {

					public void run() {
						try {
//							String result = HttpUtils.requestByHttpPost("121.538123"
//									, "31.677132", "5000");
//							Log.i("wwwww",result);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}).start();
			}
		});
		
		btnPoi = (Button) findViewById(R.id.buttonPoi);
		btnPoi.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				mPoiSearch.searchInCity((new PoiCitySearchOption())  
					    .city("徐州")  
					    .keyword("银行")  
					    .pageNum(20));
			}
		});
		
		btnCode = (Button) findViewById(R.id.buttonGeo);
		btnCode.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				LatLng ll = new LatLng((Float) SPUtils.get(TestActivity.this, "Latitude", 12.3f)
						, (Float)SPUtils.get(TestActivity.this, "Lontitude", 12.3f));
		        geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(ll));  
		        // 释放地理编码检索实例  Latitude
				
			}
		});
		
		btnGeo = (Button) findViewById(R.id.button1);
		btnGeo.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				//地理编码
				geoCoder.geocode(new GeoCodeOption().city("徐州市").address("徐州饭店"));
			}
		});
	}
	//Poi初始化
	private void initPoi(){
		mPoiSearch = PoiSearch.newInstance();  
		mPoiSearch.setOnGetPoiSearchResultListener(poiListener);
	}
	//Poi监听器
	OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener(){

		//获取Place详情页检索结果 
		public void onGetPoiDetailResult(PoiDetailResult arg0) {
			
		}
		//获取POI检索结果  
		public void onGetPoiResult(PoiResult poiResult) {
			if (poiResult == null  
                    || poiResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {// 没有找到检索结果  
                Toast.makeText(TestActivity.this, "未找到结果",  
                        Toast.LENGTH_LONG).show();  
                return;  
            }  
			if (poiResult.error == SearchResult.ERRORNO.NO_ERROR) {// 检索结果正常返回  
				int totalPage = poiResult.getTotalPageNum();
				Log.i("totalPage:",""+totalPage);
				List<PoiInfo> poiAddress = poiResult.getAllPoi();
				Log.i("length:",""+poiAddress.size());
				for(PoiInfo address:poiAddress ){
					Log.i("address:",address.address);
					Log.i("name:",address.name);
					Log.i("LongLat",address.location.toString());
				}
			}
		}  
	};
	
	//地理编码监听
	OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {  
		
        // 反地理编码查询结果回调函数  
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {  
            if (result == null  
                    || result.error != SearchResult.ERRORNO.NO_ERROR) {  
                // 没有检测到结果  
                Toast.makeText(TestActivity.this, "抱歉，未能找到结果",  
                        Toast.LENGTH_LONG).show();  
            }  
            Log.i("result_code:",result.getAddress());
            Toast.makeText(TestActivity.this,  
                    "位置：" + result.getAddress(), Toast.LENGTH_LONG)  
                    .show();  
        }  

        // 地理编码查询结果回调函数  
        public void onGetGeoCodeResult(GeoCodeResult result) {  
            if (result == null  
                    || result.error != SearchResult.ERRORNO.NO_ERROR) {  
            	// 没有检测到结果  
                Toast.makeText(TestActivity.this, "抱歉，未能找到结果",  
                        Toast.LENGTH_LONG).show();  
                return;
            } 
            Log.i("result_code:",result.getAddress());
            Toast.makeText(TestActivity.this,  
                    "位置：" + result.getAddress(), Toast.LENGTH_LONG)  
                    .show();  
        }  
    };  
    
    protected void onDestroy() {
    	geoCoder.destroy();
    };
}
