package com.cumt.carnet.entity;

import java.io.Serializable;

/**
 * 加油站item信息实体类
 * @author wangcan
 *
 */
@SuppressWarnings("serial")
public class GasItemBean implements Serializable{
	
	private String id;	//加油站id
	private String name;	//加油站名称
	private String address;	//加油站地址
	private String lon;	//加油站经度
	private String lat;//加油站纬度
	private String price;//油价
	private String distance;//加油站距离
	private String discount;//是否折扣
	public GasItemBean(String id, String name, String address, String lon,
			String lat, String price, String distance, String discount) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
		this.lon = lon;
		this.lat = lat;
		this.price = price;
		this.distance = distance;
		this.discount = discount;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getLon() {
		return lon;
	}
	public void setLon(String lon) {
		this.lon = lon;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public String getDiscount() {
		return discount;
	}
	public void setDiscount(String discount) {
		this.discount = discount;
	}
	
}
