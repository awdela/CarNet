package com.cumt.login.entity;
/**
 * �û�ʵ��
 * @author wangcan
 *
 */
public class LoginUserBean {
	
	private String userName;//�û���
	private String userPass;//�û�����
	
	public LoginUserBean() {
	}
	public LoginUserBean(String userName, String userPass) {
		super();
		this.userName = userName;
		this.userPass = userPass;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPass() {
		return userPass;
	}
	public void setUserPass(String userPass) {
		this.userPass = userPass;
	}
}
