package com.cumt.carnet.presenter;

/**
 * 类名：IMapinitPresenter
 * 作用：百度地图配置信息初始化
 * @author wangcan
 *
 */
public interface IMapinitPresenter {
	
	public void initMapConfig();//初始化地图设置信息
	public void initMyPositon();//初始化地定位功能
	public void getMyPosition();//获取我的当前位置
	
	
}