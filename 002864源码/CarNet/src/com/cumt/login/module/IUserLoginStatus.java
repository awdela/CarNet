package com.cumt.login.module;

import com.cumt.login.entity.LoginUserBean;

import android.content.Context;

public interface IUserLoginStatus {
	boolean isRemember(Context context);//判断用户是否记住密码
	public void setNameAndPass(Context context,LoginUserBean user);//若用户设置记住密码，则记录
	public LoginUserBean getUserBean(Context context); //获取userbean对象，即获取存储的用户名账号密码
	
	public void Login(final Context context,final LoginUserBean user,final OnLoginListener onLoginListener);//登录
}
