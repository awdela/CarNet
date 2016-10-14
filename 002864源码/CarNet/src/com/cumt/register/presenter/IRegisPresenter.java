package com.cumt.register.presenter;

public interface IRegisPresenter {
	
	public void gotoNextActivity();//跳转到下一个界面
	public String getUserName();//获取用户名
	public String getUserPass();//获取用户密码
	public String getCode();//获取验证码
	public String setCode();//设置验证码

}
