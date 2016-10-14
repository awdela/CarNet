package com.cumt.drawerlayout.personal;

import java.util.List;

import org.apache.http.NameValuePair;

import com.cumt.carnet.R;
import com.cumt.util.HttpUtils;
import com.cumt.util.SPUtils;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

public class UserInfoSettingActivity extends Activity implements OnClickListener{
	
	private EditText nickText,birthText,homeText,companyText;
	
	private Button btnSet,btnCancle;
	
	private RadioButton radioButton;
	
	private static final String PERSONALMSG_URL = "http://115.159.205.135/carnet/changePersoninfor";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_userinfosetting);
		initView();
	}
	
	
	private void initView(){
		nickText = (EditText) findViewById(R.id.nickname);
		birthText = (EditText) findViewById(R.id.birthday);
		homeText = (EditText) findViewById(R.id.home_address);
		companyText = (EditText) findViewById(R.id.work_address);
		
		btnSet = (Button) findViewById(R.id.save_and_return);
		btnCancle = (Button) findViewById(R.id.not_save);
		btnSet.setOnClickListener(this);
		btnCancle.setOnClickListener(this);
		
		radioButton = (RadioButton) findViewById(R.id.male);
	}


	public void onClick(View v) {
		switch(v.getId()){
		case R.id.save_and_return:
			savePersonMsg();
			UserInfoSettingActivity.this.finish();
			break;
		case R.id.not_save:
			UserInfoSettingActivity.this.finish();
			break;
		}
	}
	
	private void savePersonMsg(){
		String nickname = nickText.getText().toString();
		String birthday = birthText.getText().toString();
		String home = homeText.getText().toString();
		String company = companyText.getText().toString();
		boolean isMale = true;
		if(radioButton.isChecked()){
			isMale = true;
		}else{
			isMale = false;
		}
		if(nickname != null){
			SPUtils.put(UserInfoSettingActivity.this, "Nickname", nickname);
		}
		if(birthday != null){
			SPUtils.put(UserInfoSettingActivity.this, "Birthday", birthday);
		}
		if(home != null){
			SPUtils.put(UserInfoSettingActivity.this, "HomeAddr", home);
		}
		if(company != null){
			SPUtils.put(UserInfoSettingActivity.this, "Company", company);
		}
		String sex = isMale?"男":"女";
		SPUtils.put(UserInfoSettingActivity.this, "Sex", sex);
		//服务端存储
		savePersonMsgNet(nickname,home,birthday,sex,company);
	}
	//服务器数据库存储
	private void savePersonMsgNet(final String nickname,final String address,
			final String birthday,final String sex,final String company){
		new Thread(){
			public void run() {
				
				List<NameValuePair> params = HttpUtils.paramsOfPersonalMsg(nickname
						, address, birthday, sex, company);
				try {
					HttpUtils.requestByHttpPost(params, PERSONALMSG_URL);
				} catch (Exception e) {
					e.printStackTrace();
				}		
				
			}
		}.start();
	}
}
