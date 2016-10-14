package com.cumt.zxing.entity;
/**
 * 二维码解析结果的实体类
 * @author wangcan
 *
 */
public class ZxingBean {
	
	private String brand;
	private String symbol;
	private String style;
	private String car_number;
	private String engine;//发动机型号
	private String level;
	private String miles;
	private String oil;
	private String engine_feature;
	private String transmation;
	private String light;
	private String vernum;//车架号
	private String enginenum;//发动机号
	
	public ZxingBean(String brand, String symbol, String style,
			String car_number, String engine, String level, String miles,
			String oil, String engine_feature, String transmation, String light,
			String vernum,String enginenum) {
		super();
		this.brand = brand;
		this.symbol = symbol;
		this.style = style;
		this.car_number = car_number;
		this.engine = engine;
		this.level = level;
		this.miles = miles;
		this.oil = oil;
		this.engine_feature = engine_feature;
		this.transmation = transmation;
		this.light = light;
		this.vernum = vernum;
		this.enginenum = enginenum;
	}

	public ZxingBean() {
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getCar_number() {
		return car_number;
	}

	public void setCar_number(String car_number) {
		this.car_number = car_number;
	}

	public String getEngine() {
		return engine;
	}

	public void setEngine(String engine) {
		this.engine = engine;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getMiles() {
		return miles;
	}

	public void setMiles(String miles) {
		this.miles = miles;
	}

	public String getOil() {
		return oil;
	}

	public void setOil(String oil) {
		this.oil = oil;
	}

	public String getEngine_feature() {
		return engine_feature;
	}

	public void setEngine_feature(String engine_feature) {
		this.engine_feature = engine_feature;
	}

	public String getTransmation() {
		return transmation;
	}

	public void setTransmation(String transmation) {
		this.transmation = transmation;
	}

	public String getlight() {
		return light;
	}

	public void setlight(String light) {
		this.light = light;
	}

	public String getVernum() {
		return vernum;
	}

	public void setVernum(String vernum) {
		this.vernum = vernum;
	}

	public String getEnginenum() {
		return enginenum;
	}

	public void setEnginenum(String enginenum) {
		this.enginenum = enginenum;
	}
	
}
