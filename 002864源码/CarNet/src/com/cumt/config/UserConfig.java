package com.cumt.config;


/**
 * 类名:UserConfig
 * 作用:用户的设置信息
 * @author wangcan
 *
 */
public class UserConfig {
	
	private boolean isAutoShowGasstation;//是否自动显示周围的加油站
	/**
	 * 用户设置的开始时现实的地图类型:
	 * BaiduMap.MAP_TYPE_NORMAL 通常的地图类型
	 * BaiduMap.MAP_TYPE_SATELLITE 卫星地图
	 */
	private int showMapType;
	
	
	public UserConfig(boolean isAutoShowGasstation, int showMapType) {
		this.isAutoShowGasstation = isAutoShowGasstation;
		this.showMapType = showMapType;
	}


	public boolean isAutoShowGasstation() {
		return isAutoShowGasstation;
	}


	public void setAutoShowGasstation(boolean isAutoShowGasstation) {
		this.isAutoShowGasstation = isAutoShowGasstation;
	}


	public int getShowMapType() {
		return showMapType;
	}


	public void setShowMapType(int showMapType) {
		this.showMapType = showMapType;
	}
	
}
