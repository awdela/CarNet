package com.cumt.register.view;

import org.json.JSONObject;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import com.cumt.carnet.R;
import com.cumt.carnet.view.MainActivity;
import com.cumt.register.presenter.IRegisPresenter;
import com.cumt.register.presenter.RegisPresenter;
import com.cumt.util.NormalMethodsUtils;
import com.cumt.view.ButtonRectangle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 类名：RegisterActivity 作用：用于用户注册见面的显示，用户注册信息的获取，包括手机号，密码，显示验证码
 * 
 * @author wangcan
 * 
 */
public class RegisterActivity extends Activity implements IRegisPresenter,
		OnClickListener {

	private EditText phoneText, passText, codeText;
	private ButtonRectangle getCodeBtn, regBtn;

	private RegisPresenter regisPresenter;

	// 填写从短信SDK应用后台注册得到的APPKEY
	private static String APPKEY = "69d6705af33d";// "69d6705af33d";0d786a4efe92bfab3d5717b9bc30a10d
	// 填写从短信SDK应用后台注册得到的APPSECRET
	private static String APPSECRET = "0d786a4efe92bfab3d5717b9bc30a10d";
	public String phString;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题栏
		setContentView(R.layout.activity_register);
		initView();
		regisPresenter = new RegisPresenter(RegisterActivity.this,
				RegisterActivity.this);
		
		SMSSDK.initSDK(this,APPKEY,APPSECRET,true);
		EventHandler eh=new EventHandler(){

			@Override
			public void afterEvent(int event, int result, Object data) {
				Message msg = new Message();
				msg.arg1 = event;
				msg.arg2 = result;
				msg.obj = data;
				mHandler.sendMessage(msg);
			}
			
		};
		SMSSDK.registerEventHandler(eh);
	}

	private void initView() {
		phoneText = (EditText) findViewById(R.id.editText1);
		passText = (EditText) findViewById(R.id.editText3);
		codeText = (EditText) findViewById(R.id.editText2);
		getCodeBtn = (ButtonRectangle) findViewById(R.id.button1);
		regBtn = (ButtonRectangle) findViewById(R.id.button2);
		getCodeBtn.setOnClickListener(this);
		regBtn.setOnClickListener(this);
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.button1://获取验证码短信
			if(!TextUtils.isEmpty(phoneText.getText().toString())){
				SMSSDK.getVerificationCode("86",phoneText.getText().toString());   
				phString=phoneText.getText().toString();
				
			}else {
				Toast.makeText(RegisterActivity.this,"电话不能为空", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.button2:
			SMSSDK.submitVerificationCode("86", phString, codeText.getText().toString());
			break;
		}
	}

	public void gotoNextActivity() {
		// TODO Auto-generated method stub
		// 注册成功则进行跳转到主界面
		NormalMethodsUtils.goNextActivity(RegisterActivity.this,
				MainActivity.class, RegisterActivity.this);

	}

	public String getUserName() {
		// TODO Auto-generated method stub
		return phoneText.getText().toString();
	}

	public String getUserPass() {
		// TODO Auto-generated method stub
		return passText.getText().toString();
	}

	public String getCode() {
		// TODO Auto-generated method stub
		return codeText.getText().toString();
	}

	public String setCode() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@SuppressLint("HandlerLeak")
	Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg) {

			// TODO Auto-generated method stub
			super.handleMessage(msg);
			int event = msg.arg1;
			int result = msg.arg2;
			Object data = msg.obj;
			Log.e("event", "event="+event);
			if (result == SMSSDK.RESULT_COMPLETE) {
				System.out.println("--------result"+event);
				//短信注册成功后，返回MainActivity,然后提示新好友
				if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码成功
					Toast.makeText(getApplicationContext(), "提交验证码成功", Toast.LENGTH_SHORT).show();
					regisPresenter.register();// 注册
					
				} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
						//已经验证
						Toast.makeText(getApplicationContext(), "验证码已经发送", Toast.LENGTH_SHORT).show();
				}else if(event == SMSSDK.RESULT_ERROR){
					Toast.makeText(getApplicationContext(), "验证码出错", Toast.LENGTH_SHORT).show();
				}
				
			} else {
				((Throwable) data).printStackTrace();
				Toast.makeText(RegisterActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
				@SuppressWarnings("unused")
				int status = 0;	
					try {
						((Throwable) data).printStackTrace();
						Throwable throwable = (Throwable) data;

						JSONObject object = new JSONObject(throwable.getMessage());
						String des = object.optString("detail");
						status = object.optInt("status");
						if (!TextUtils.isEmpty(des)) {
							Toast.makeText(RegisterActivity.this, des, Toast.LENGTH_SHORT).show();
							return;
						}
					} catch (Exception e) {
					}
			}
		
		};
	};
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		SMSSDK.unregisterAllEventHandler();
	};
}
