package com.cumt.register.module;

import com.cumt.register.entity.RegUserBean;

public interface OnRegListener {
	
	public void regisSuccess(RegUserBean regUserBean,int code);//ע����
	public void regisFalied(int code);//ע��ʧ��

}
