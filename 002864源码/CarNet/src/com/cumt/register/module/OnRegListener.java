package com.cumt.register.module;

import com.cumt.register.entity.RegUserBean;

public interface OnRegListener {
	
	public void regisSuccess(RegUserBean regUserBean,int code);//×¢²á½á¹û
	public void regisFalied(int code);//×¢²áÊ§°Ü

}
