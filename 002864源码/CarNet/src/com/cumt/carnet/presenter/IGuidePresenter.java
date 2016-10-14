package com.cumt.carnet.presenter;
/**
 * 导航fragment的一些功能方法
 * @author wangcan
 *
 */
public interface IGuidePresenter {
	public void exchangeTextData();//用于交换两个edittext的内容
	public void clearData();//在用户点击取消时调用，负责list和hash存储的数据，textview的数据以及恢复title的显示
	public void startGuide();//用户点击导航后启动导航功能，具体而言是获得起始和终止位置的经纬度坐标，然后进行导航
}
