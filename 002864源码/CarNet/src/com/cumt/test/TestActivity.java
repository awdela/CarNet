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
	
	PoiSearch mPoiSearch = null;//Poi����
	// ��������������ʵ��  
    GeoCoder geoCoder = GeoCoder.newInstance();  
    
	// private MyPopupWindow myPopupWindow;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_xml);
		initPoi();
		
		// ���õ���������������  
        geoCoder.setOnGetGeoCodeResultListener(listener);  
		// parameters = camera.getParameters();

		btnTest = (Button) findViewById(R.id.buttontest);
		btnTest.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				// myPopupWindow.showPopupWindow();
				if (isFirst) {
					camera = Camera.open();
					parameters = camera.getParameters();
					// ֱ�ӿ���
					parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);// ����
					camera.setParameters(parameters);
					isFirst = false;
				} else {
					parameters = camera.getParameters();
					parameters.setFlashMode(Parameters.FLASH_MODE_OFF);// �ر�
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
					    .city("����")  
					    .keyword("����")  
					    .pageNum(20));
			}
		});
		
		btnCode = (Button) findViewById(R.id.buttonGeo);
		btnCode.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				LatLng ll = new LatLng((Float) SPUtils.get(TestActivity.this, "Latitude", 12.3f)
						, (Float)SPUtils.get(TestActivity.this, "Lontitude", 12.3f));
		        geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(ll));  
		        // �ͷŵ���������ʵ��  Latitude
				
			}
		});
		
		btnGeo = (Button) findViewById(R.id.button1);
		btnGeo.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				//�������
				geoCoder.geocode(new GeoCodeOption().city("������").address("���ݷ���"));
			}
		});
	}
	//Poi��ʼ��
	private void initPoi(){
		mPoiSearch = PoiSearch.newInstance();  
		mPoiSearch.setOnGetPoiSearchResultListener(poiListener);
	}
	//Poi������
	OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener(){

		//��ȡPlace����ҳ������� 
		public void onGetPoiDetailResult(PoiDetailResult arg0) {
			
		}
		//��ȡPOI�������  
		public void onGetPoiResult(PoiResult poiResult) {
			if (poiResult == null  
                    || poiResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {// û���ҵ��������  
                Toast.makeText(TestActivity.this, "δ�ҵ����",  
                        Toast.LENGTH_LONG).show();  
                return;  
            }  
			if (poiResult.error == SearchResult.ERRORNO.NO_ERROR) {// ���������������  
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
	
	//����������
	OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {  
		
        // ����������ѯ����ص�����  
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {  
            if (result == null  
                    || result.error != SearchResult.ERRORNO.NO_ERROR) {  
                // û�м�⵽���  
                Toast.makeText(TestActivity.this, "��Ǹ��δ���ҵ����",  
                        Toast.LENGTH_LONG).show();  
            }  
            Log.i("result_code:",result.getAddress());
            Toast.makeText(TestActivity.this,  
                    "λ�ã�" + result.getAddress(), Toast.LENGTH_LONG)  
                    .show();  
        }  

        // ��������ѯ����ص�����  
        public void onGetGeoCodeResult(GeoCodeResult result) {  
            if (result == null  
                    || result.error != SearchResult.ERRORNO.NO_ERROR) {  
            	// û�м�⵽���  
                Toast.makeText(TestActivity.this, "��Ǹ��δ���ҵ����",  
                        Toast.LENGTH_LONG).show();  
                return;
            } 
            Log.i("result_code:",result.getAddress());
            Toast.makeText(TestActivity.this,  
                    "λ�ã�" + result.getAddress(), Toast.LENGTH_LONG)  
                    .show();  
        }  
    };  
    
    protected void onDestroy() {
    	geoCoder.destroy();
    };
}
