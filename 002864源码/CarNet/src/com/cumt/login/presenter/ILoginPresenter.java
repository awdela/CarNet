package com.cumt.login.presenter;
/**
 * �ӿ���:ILoginPresenter
 * @author wangcan
 *
 */
public interface ILoginPresenter {
	public void gotoNextActivity();//��ת����һ������
	public void gotoRegisterActivity();//��ת��ע�����
	public String getUserName();//��ȡ�û���
	public String getUserPass();//��ȡ�û�����
}
