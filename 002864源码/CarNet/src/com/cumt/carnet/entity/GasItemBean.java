package com.cumt.carnet.entity;

import java.io.Serializable;

/**
 * ����վitem��Ϣʵ����
 * @author wangcan
 *
 */
@SuppressWarnings("serial")
public class GasItemBean implements Serializable{
	
	private String id;	//����վid
	private String name;	//����վ����
	private String address;	//����վ��ַ
	private String lon;	//����վ����
	private String lat;//����վγ��
	private String price;//�ͼ�
	private String distance;//����վ����
	private String discount;//�Ƿ��ۿ�
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
