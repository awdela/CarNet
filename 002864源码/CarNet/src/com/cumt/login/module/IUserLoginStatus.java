package com.cumt.login.module;

import com.cumt.login.entity.LoginUserBean;

import android.content.Context;

public interface IUserLoginStatus {
	boolean isRemember(Context context);//�ж��û��Ƿ��ס����
	public void setNameAndPass(Context context,LoginUserBean user);//���û����ü�ס���룬���¼
	public LoginUserBean getUserBean(Context context); //��ȡuserbean���󣬼���ȡ�洢���û����˺�����
	
	public void Login(final Context context,final LoginUserBean user,final OnLoginListener onLoginListener);//��¼
}
