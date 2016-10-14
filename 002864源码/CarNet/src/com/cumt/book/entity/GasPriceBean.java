package com.cumt.book.entity;
/**
 * 加油站油类型价格实体类
 * @author wangcan
 *
 */
public class GasPriceBean {
	
	private String gasType;
	private String gasPrice;
	
	public GasPriceBean(String gasType, String gasPrice) {
		super();
		this.gasType = gasType;
		this.gasPrice = gasPrice;
	}

	public String getGasType() {
		return gasType;
	}

	public void setGasType(String gasType) {
		this.gasType = gasType;
	}

	public String getGasPrice() {
		return gasPrice;
	}

	public void setGasPrice(String gasPrice) {
		this.gasPrice = gasPrice;
	}
	
}
