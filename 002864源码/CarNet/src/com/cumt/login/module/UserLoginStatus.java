package com.cumt.login.module;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.util.Log;
import com.cumt.login.entity.LoginUserBean;
import com.cumt.util.HttpUtils;
import com.cumt.util.REUtils;

public class UserLoginStatus implements IUserLoginStatus{
	
	private static final String LOGIN_URL = "http://115.159.205.135/carnet/login";
	
	//判断用户是否选择记住密码
	public boolean isRemember(Context context) {
		// TODO Auto-generated method stub
		return false;
	}

	//若用户选择自动登录设置用户的账号密码
	public void setNameAndPass(Context context, LoginUserBean user) {
		
	}

	//
	public LoginUserBean getUserBean(Context context) {
		// TODO Auto-generated method stub
		return null;
	}
	//普通账号密码登录
	public void Login(final Context context,final LoginUserBean loginUserBean, final OnLoginListener onLoginListener) {
		
		new Thread(){
			public void run() {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("phone_number", loginUserBean.getUserName()));
				params.add(new BasicNameValuePair("password", loginUserBean.getUserPass()));
				try {
					Log.i("wwwwww","cccccc");
					String result = HttpUtils.requestByHttpPost(params, LOGIN_URL);
					Log.i("result",result);
					int code = REUtils.getReturnCode(result);
					Log.i("code",""+code);
					if( code != -1 ){
						onLoginListener.loginSuccess(loginUserBean,code);
					}else{
						onLoginListener.loginFailed(context);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			};
		}.start();
	}

}
