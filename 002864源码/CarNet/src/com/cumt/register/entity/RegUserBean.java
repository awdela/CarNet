package com.cumt.register.entity;
/**
 * 用户实体
 * @author wangcan
 *
 */
public class RegUserBean {
	private String userName;//用户名
	private String userPass;//用户密码
	
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
