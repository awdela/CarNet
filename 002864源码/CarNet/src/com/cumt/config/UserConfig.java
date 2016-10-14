package com.cumt.config;


/**
 * ����:UserConfig
 * ����:�û���������Ϣ
 * @author wangcan
 *
 */
public class UserConfig {
	
	private boolean isAutoShowGasstation;//�Ƿ��Զ���ʾ��Χ�ļ���վ
	/**
	 * �û����õĿ�ʼʱ��ʵ�ĵ�ͼ����:
	 * BaiduMap.MAP_TYPE_NORMAL ͨ���ĵ�ͼ����
	 * BaiduMap.MAP_TYPE_SATELLITE ���ǵ�ͼ
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
