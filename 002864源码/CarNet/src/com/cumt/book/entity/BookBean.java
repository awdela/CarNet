package com.cumt.book.entity;

import java.io.Serializable;

import com.app.CarnetApplication;
import com.cumt.carnet.entity.GasItemBean;
import com.cumt.util.Utils;

/**
 * 订单实体类，包含订单的信息
 * @author wangcan
 *
 */
public class BookBean implements Serializable{
	
	private static final long serialVersionUID = 2L;
	
	private String phone_number;//账号
	private String username;//用户姓名
	private String gasstation_name;//加油站姓名
	private String gas_address;//加油站详细地址
	private String oilStyle;//油类型
	private double oil_price;//油单价
	private double total_price;//订单金额
	private double count;//油数量
	private String oil_time;//订单产生时间
	private String make_date;//订单完成时间
	private int gas_state;//订单状态
	private String gas_lon;//加油站的经度
	private String gas_lat;//加油站的纬度
	private String car_number;//加油的车牌号
	
	
	public BookBean(GasItemBean gasItemBean,String oilType,double oil_price,double total_price){
		CarnetApplication carApp = CarnetApplication.getInstance();
		this.phone_number = carApp.getUsername();
		this.username = this.phone_number;
		this.gasstation_name = gasItemBean.getName();
		this.gas_address = gasItemBean.getAddress();
		this.oilStyle = oilType;
		this.oil_price = oil_price;
		this.total_price = total_price;
		this.count = this.total_price/this.oil_price;
		this.oil_time = Utils.getTimeString(Utils.getTimeDate());
		this.gas_state = 0;
		this.gas_lon = gasItemBean.getLon();
		this.gas_lat = gasItemBean.getLat();
		String number = carApp.getCar_number();
		if(number != null){
			this.car_number = number;
		}else{
			this.car_number = "未绑定车辆";
		}
	}

	
	public BookBean() {
	}


	@Override
	public String toString() {
		return "phone_number:" + phone_number +"," 
				+ "username:" + username +","
				+ "gasstation_name:" + gasstation_name + ","
				+ "gas_address:" + gas_address + ","
				+ "oilStyle:" + oilStyle + ","
				+ "oil_price:" + oil_price + ","
				+ "total_price" + total_price + ","
				+ "oil_time:" + oil_time + ","
				+ "gas_state:" + gas_state + "," 
				+ "car_number" + car_number;
	}


	public String getPhone_number() {
		return phone_number;
	}


	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getGasstation_name() {
		return gasstation_name;
	}


	public void setGasstation_name(String gasstation_name) {
		this.gasstation_name = gasstation_name;
	}


	public String getGas_address() {
		return gas_address;
	}


	public void setGas_address(String gas_address) {
		this.gas_address = gas_address;
	}


	public String getOilStyle() {
		return oilStyle;
	}


	public void setOilStyle(String oilStyle) {
		this.oilStyle = oilStyle;
	}


	public double getCount() {
		return count;
	}


	public void setCount(double count) {
		this.count = count;
	}


	public String getOil_time() {
		return oil_time;
	}


	public void setOil_time(String oil_time) {
		this.oil_time = oil_time;
	}


	public String getMake_date() {
		return make_date;
	}


	public void setMake_date(String make_date) {
		this.make_date = make_date;
	}


	public int getGas_state() {
		return gas_state;
	}


	public void setGas_state(int gas_state) {
		this.gas_state = gas_state;
	}




	public double getOil_price() {
		return oil_price;
	}

	public void setOil_price(double oil_price) {
		this.oil_price = oil_price;
	}


	public double getTotal_price() {
		return total_price;
	}


	public void setTotal_price(double total_price) {
		this.total_price = total_price;
	}


	public String getGas_lon() {
		return gas_lon;
	}


	public void setGas_lon(String gas_lon) {
		this.gas_lon = gas_lon;
	}


	public String getGas_lat() {
		return gas_lat;
	}


	public void setGas_lat(String gas_lat) {
		this.gas_lat = gas_lat;
	}


	public String getCar_number() {
		return car_number;
	}


	public void setCar_number(String car_number) {
		this.car_number = car_number;
	}
	
}
