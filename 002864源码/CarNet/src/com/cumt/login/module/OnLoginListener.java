package com.cumt.login.module;

import com.cumt.login.entity.LoginUserBean;

import android.content.Context;

public interface OnLoginListener {

	public void loginSuccess(LoginUserBean loginUserBean,int code);//µÇÂ¼³É¹¦ code:·µ»ØÂë
	public void loginFailed(Context context);//µÇÂ¼Ê§°Ü
}
