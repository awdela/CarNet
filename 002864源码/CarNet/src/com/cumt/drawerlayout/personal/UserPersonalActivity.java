package com.cumt.drawerlayout.personal;

import com.app.CarnetApplication;
import com.cumt.carnet.R;
import com.cumt.util.NormalMethodsUtils;
import com.cumt.util.SPUtils;
import com.db.CarInfoDao;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class UserPersonalActivity extends Activity implements IUserPresenter{
	
	private TextView phoneText,countText;
	private UserMsgPresenter msgPresenter;
	private ImageView headImage,sexImage;
	private ImageButton editBtn;
	private TextView nickText,birthText,homeText,companyText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_userinfo);
		msgPresenter = new UserMsgPresenter(this);
		initView();
		initData();
		initTextData();
	}
	
	@Override
	protected void onResume() {
		Log.e("onResume","onResume");
		super.onResume();
		initTextData();
	}
	
	private void initView(){
		phoneText = (TextView) findViewById(R.id.phone_number);
		countText = (TextView) findViewById(R.id.my_name);
		headImage = (ImageView) findViewById(R.id.my_portrait);
		sexImage = (ImageView) findViewById(R.id.sex);
		editBtn = (ImageButton) findViewById(R.id.edit_myinfo);
		editBtn.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				NormalMethodsUtils.goNextActivity(UserPersonalActivity.this,
						UserInfoSettingActivity.class, UserPersonalActivity.this);
			}
		});
		nickText = (TextView) findViewById(R.id.user_name);
		homeText = (TextView) findViewById(R.id.home);
		companyText = (TextView) findViewById(R.id.work);
		birthText = (TextView) findViewById(R.id.birthday);
	}
	
	
	private void initData(){
		CarnetApplication carApp = CarnetApplication.getInstance();
		String phone = carApp.getUsername();
		phoneText.setText(phone);
		CarInfoDao carInfoDao = new CarInfoDao(UserPersonalActivity.this) ;
		int count = carInfoDao.getDataCount();
		countText.setText(""+count);
		loadHeadImage();
	}

	//加载缓存的头像
	public void loadHeadImage() {
		msgPresenter.loadCacheHeadImage(headImage);
	}
	
	//初始化textview的数据
	private void initTextData(){
		if(SPUtils.contains(UserPersonalActivity.this, "Nickname")){
			nickText.setText((String)SPUtils.get(UserPersonalActivity.this
					, "Nickname", "好昵称都被别人占了"));
		}
		if(SPUtils.contains(UserPersonalActivity.this, "Birthday")){
			birthText.setText((String)SPUtils.get(UserPersonalActivity.this
					, "Birthday", "1994-6-16"));
		}
		if(SPUtils.contains(UserPersonalActivity.this, "HomeAddr")){
			homeText.setText((String)SPUtils.get(UserPersonalActivity.this
					, "HomeAddr", "CUMT"));
		}
		if(SPUtils.contains(UserPersonalActivity.this, "Company")){
			companyText.setText((String)SPUtils.get(UserPersonalActivity.this
					, "Company", "CUMT"));
		}
		if(SPUtils.contains(UserPersonalActivity.this, "Sex")){
			String sex = (String) SPUtils.get(UserPersonalActivity.this, "Sex", "男");
			if("男".equals(sex)||"男"==sex){
				sexImage.setImageDrawable(getResources().getDrawable(R.drawable.male));
			}else{
				sexImage.setImageDrawable(getResources().getDrawable(R.drawable.female));
			}
		}
	}
}
