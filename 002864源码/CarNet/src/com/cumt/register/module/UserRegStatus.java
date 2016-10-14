package com.cumt.register.module;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;

import com.cumt.register.entity.RegUserBean;
import com.cumt.util.HttpUtils;
import com.cumt.util.REUtils;

public class UserRegStatus implements IUserRegStatus{
	
	private static final String LOGIN_URL = "http://115.159.205.135/carnet/register";

	/**
	 * ÓÃ»§×¢²á
	 */
	public void Register(Context context, final RegUserBean regUserBean,
			final OnRegListener onRegListener) {
		
		new Thread(){
			
			public void run() {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("phone_number", regUserBean.getUserName()));
				params.add(new BasicNameValuePair("password", regUserBean.getUserPass()));
				try {
					String result = HttpUtils.requestByHttpPost(params, LOGIN_URL);
					int code = REUtils.getReturnCode(result);
					if( code != -1 ){
						onRegListener.regisSuccess(regUserBean, code);
					}else{
						onRegListener.regisFalied(-1);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}.start();
	}

}
