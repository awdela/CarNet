package com.cumt.register.entity;
/**
 * �û�ʵ��
 * @author wangcan
 *
 */
public class RegUserBean {
	private String userName;//�û���
	private String userPass;//�û�����
	
	public RegUserBean() {
	}
	public RegUserBean(String userName, String userPass) {
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
