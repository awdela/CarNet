package com.cumt.drawerlayout.carinfo;

import java.util.ArrayList;
import java.util.List;

import com.app.CarnetApplication;
import com.cumt.carnet.R;
import com.cumt.drawerlayout.carinfo.activity.SearchActivity;
import com.cumt.zxing.entity.ZxingBean;
import com.db.CarInfoDao;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CarInfoActivity extends Activity implements OnItemSelectedListener, OnClickListener {
	
	private List<ZxingBean> zxingBeanList = null;
	private Button btnSearch;//查询违章信息
	private TextView engineText,leverText,milesText,oilText,enginefeatureText,
							transmisionText,lightText,nowBindText;
	private Spinner spinner;
	private ArrayAdapter<String> spinnerAdapter;
	private List<String> carNumList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_carinfo);
		initView();
		getData();
		initSpinnerData();
	}
	
	private void initView(){
		btnSearch = (Button) findViewById(R.id.carinfo_search_rules);
		spinner = (Spinner) findViewById(R.id.spinner1);
		engineText = (TextView) findViewById(R.id.engine_text);
		leverText = (TextView) findViewById(R.id.lever_text);
		milesText = (TextView) findViewById(R.id.miles_text);
		oilText = (TextView) findViewById(R.id.oil_text);
		enginefeatureText = (TextView) findViewById(R.id.engine_feature);
		transmisionText = (TextView) findViewById(R.id.transmision_text);
		lightText = (TextView) findViewById(R.id.light_text);
		nowBindText = (TextView) findViewById(R.id.now_bind_car);
		spinner.setOnItemSelectedListener(this);
		btnSearch.setOnClickListener(this);
	}
	
	private void getData(){
		CarInfoDao carInfoDao = new CarInfoDao(this);
		if(carInfoDao.hasData()){
			Log.i("database","有数据");
			zxingBeanList = carInfoDao.getCarInfo();
		}
	}
	
	private void initSpinnerData(){
		if(zxingBeanList == null){
			Toast.makeText(this, "您还没有维护的车辆", Toast.LENGTH_SHORT).show();
			return;
		}
		carNumList = new ArrayList<String>();
		for(ZxingBean item:zxingBeanList){
			carNumList.add(item.getCar_number());
		}
		spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, 
				carNumList);
		spinner.setAdapter(spinnerAdapter);
		initTextData(0);
	}
	
	private void initTextData(int position){
		ZxingBean zxBean = zxingBeanList.get(position);
		engineText.setText(zxBean.getEngine());
		leverText.setText(zxBean.getLevel());
		milesText.setText(zxBean.getMiles());
		oilText.setText(zxBean.getOil());
		enginefeatureText.setText(zxBean.getEngine_feature());
		transmisionText.setText(zxBean.getTransmation());
		lightText.setText(zxBean.getlight());
		CarnetApplication carApp = CarnetApplication.getInstance();
		String carBind = carApp.getCar_number();
		if(carBind!= null){
			nowBindText.setText("当前绑定车辆:" + carBind);
		}
	}

	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		initTextData(position);
	}

	public void onNothingSelected(AdapterView<?> parent) {
		
	}

	public void onClick(View v) {
		switch(v.getId()){
		case R.id.carinfo_search_rules:
			Intent intent = new Intent(CarInfoActivity.this, SearchActivity.class);
			startActivity(intent);
			break;
		}
	}
}
