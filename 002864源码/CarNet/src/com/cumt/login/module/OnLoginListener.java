package com.cumt.login.module;

import com.cumt.login.entity.LoginUserBean;

import android.content.Context;

public interface OnLoginListener {

	public void loginSuccess(LoginUserBean loginUserBean,int code);//��¼�ɹ� code:������
	public void loginFailed(Context context);//��¼ʧ��
}
