package com.cumt.login.presenter;
/**
 * 接口名:ILoginPresenter
 * @author wangcan
 *
 */
public interface ILoginPresenter {
	public void gotoNextActivity();//跳转到下一个界面
	public void gotoRegisterActivity();//跳转到注册界面
	public String getUserName();//获取用户名
	public String getUserPass();//获取用户密码
}
