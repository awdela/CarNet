package com.cumt.register.module;

import com.cumt.register.entity.RegUserBean;

import android.content.Context;

public interface IUserRegStatus {
	
	public void Register(final Context context,RegUserBean regUserBean,OnRegListener onRegListener);

}
