package com.cumt.drawerlayout.setting;

import java.util.List;
import org.apache.http.NameValuePair;
import com.cumt.carnet.R;
import com.cumt.util.HttpUtils;
import com.cumt.util.REUtils;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangePassActivity extends Activity{
	
	private static final String CHANGEPASS_URL = "http://115.159.205.135/carnet/changePassword";
	
	private Button btnChnagePass;
	
	private EditText oldPassText,newPassText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_changepass);
		initView();
	}
	
	private void initView(){
		oldPassText = (EditText) findViewById(R.id.old_password);
		newPassText = (EditText) findViewById(R.id.new_password);
		btnChnagePass = (Button) findViewById(R.id.confirm_and_return);
		btnChnagePass.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				if(TextUtils.isEmpty(oldPassText.getText())
						|| TextUtils.isEmpty(newPassText.getText())){
					Toast.makeText(ChangePassActivity.this, "ÃÜÂë²»ÄÜÎª¿Õ", Toast.LENGTH_SHORT).show();
					return;
				}
				String oldPass = oldPassText.getText().toString();
				String newPass = newPassText.getText().toString();
				changePass(oldPass,newPass);
			}
		});
	}
	
	private void changePass(final String oldPass,final String newPass){
		new Thread(){
			public void run() {
				List<NameValuePair> params = HttpUtils.paramsOfChangePass(
						oldPass, newPass);
				try {
					Log.i("wwwwww","cccccc");
					String result = HttpUtils.requestByHttpPost(params, CHANGEPASS_URL);
					Log.i("result",result);
					int code = REUtils.getReturnCode(result);
					Log.i("code",""+code);
					if( code != -1 ){
//						onLoginListener.loginSuccess(loginUserBean,code);
					}else{
//						onLoginListener.loginFailed(context);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();
	}
}
