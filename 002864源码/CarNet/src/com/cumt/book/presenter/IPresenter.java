package com.cumt.book.presenter;

import com.cumt.carnet.entity.GasItemBean;

public interface IPresenter {
	public String getSpinnerText();//获取用户选择的油的类型 E90:5.19
	public String getMoneyText();//获取用户填写的金钱
	public GasItemBean getGasItemBean();//返回用户当前点击的加油站的信息实体对象
}
