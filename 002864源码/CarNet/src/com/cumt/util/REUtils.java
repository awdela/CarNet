package com.cumt.util;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cumt.carnet.entity.GasItemBean;
import com.cumt.book.entity.GasPriceBean;

public class REUtils {

	// 获取所有加油站item的正则表达式
	private static final String GAS_ITEM_RE = "\\{\"id\":\".*?[^:][0-9]\\}";
	// 获取一个加油站item的加油站id的正则表达式
	private static final String GAS_ID_RE = "\"id\":\"([0-9]+?)\"";
	// 获取一个加油站item的加油站名称的正则表达式
	private static final String GAS_NAME_RE = "\"name\":\"(.*?)\"";
	// 获取一个加油站item的加油站地址的正则表达式
	private static final String GAS_ADDRESS_RE = "\"address\":\"(.*?)\",\"brandname\"";
	// 获取一个加油站item的加油站是否折扣的正则表达式
	private static final String GAS_DISCOUNT_RE = "\"discount\":\"(.{0,6})\"";
	// 获取一个加油站item的加油站油价信息的正则表达式
	private static final String GAS_PRICE_RE = "\"price\":\\{(.*?)\\}";
	// 获取一个加油站item的加油站距离的正则表达式
	private static final String GAS_DISTANCE_RE = "\"distance\":([0-9]{0,7})\\}";
	// 获取一个加油站item的加油站的经度的正则表达式
	private static final String GAS_LONTITUDE_RE = "\"lon\":\"([0-9]{3}\\.[0-9]{0,15})";
	// 获取一个加油站item的加油站纬度的正则表达式
	private static final String GAS_LATITUDE_RE = "\"lat\":\"([0-9]{2}\\.[0-9]{0,15})";
	
	
	/**
	 * 用于将从聚合数据API获得的加油站Json转换为一个个的加油站item的bean列表对象，返回使用
	 * @param json 需要解析的获得的聚合数据的json信息
	 * @return 返回含有GasItemBean的列表
	 */
	public static ArrayList<GasItemBean> getGasItemBeanList(String json){
		ArrayList<GasItemBean> gasItemList = new ArrayList<GasItemBean>();
		ArrayList<String> list = new ArrayList<String>();
		list = REUtils.getGasItem(json);
		for(String item:list){
			String id = REUtils.getGasId(item);
			String name = REUtils.getGasName(item);
			String address = REUtils.getGasAddress(item);
			String price = REUtils.getGasPrice(item);
			String lon = REUtils.getGasLantitude(item);
			String lat = REUtils.getGasLatitude(item);
			String discount = REUtils.getGasDiscount(item);
			String distance = REUtils.getGasDistance(item);
			GasItemBean gasItemBean = new GasItemBean(id, name, address, lon, lat, price, distance, discount);
			gasItemList.add(gasItemBean);
		}
		return gasItemList;
	}
	/**
	 * 获取加油站所有的item
	 * @param gas_message
	 * @return
	 */
	public static ArrayList<String> getGasItem(String gas_message){
		ArrayList<String> list = new ArrayList<String>();
		Pattern p = Pattern.compile(GAS_ITEM_RE);  //获取每一个加油站信息
        Matcher m = p.matcher(gas_message); 
        while(m.find()){  
        	list.add(m.group());
        }
		return list;
	}
	
	//获取加油站id
	public static String getGasId(String gas_item){
		return analyze_re(gas_item,GAS_ID_RE);
	}
	
	//获取加油站name
	public static String getGasName(String gas_item){
		return analyze_re(gas_item,GAS_NAME_RE);
	}
	
	//获取加油站address
	public static String getGasAddress(String gas_item){
		return analyze_re(gas_item,GAS_ADDRESS_RE);
	}
	
	//获取加油站是否折扣discount
	public static String getGasDiscount(String gas_item){
		return analyze_re(gas_item,GAS_DISCOUNT_RE);
	}
	
	//获取加油站距离distance
	public static String getGasDistance(String gas_item){
		return analyze_re(gas_item,GAS_DISTANCE_RE);
	}
	
	//获取加油站纬度latitude
	public static String getGasLatitude(String gas_item){
		return analyze_re(gas_item,GAS_LATITUDE_RE);
	}
	
	//获取加油站经度lantitude
	public static String getGasLantitude(String gas_item){
		return analyze_re(gas_item,GAS_LONTITUDE_RE);
	}
	
	//获取加油站油价price 返回是:"E90":"5.16","E93":"5.53","E97":"5.88","E0":"5.11"
	public static String getGasPrice(String gas_item){
		return analyze_re(gas_item,GAS_PRICE_RE);
	}
	
	/**
	 * 获取加油站油种及其对应单价
	 * @param price 传入油类型及对应价格 如："E90":"5.16","E93":"5.53","E97":"5.88","E0":"5.11"
	 * @return 返回列表，每个列表是一个油价及油种的item
	 */
	public static ArrayList<GasPriceBean> getGasPriceList(String price){
		ArrayList<GasPriceBean> arrayList = new ArrayList<GasPriceBean>();
		String[] items = price.split(",");
		for(String str:items){
			String[] item = str.split(":");
			GasPriceBean gasBean = new GasPriceBean(item[0].replace("\"", ""),
					item[1].replace("\"", ""));
			arrayList.add(gasBean);
		}
		return arrayList;
	}
	/**
	 * 正则表达式解析
	 * @param gas_item 每一个加油站json项目
	 * @param re	匹配用的正则表达式
	 * @return "NOT_FOUNT" 未找到的返回值
	 */
	private static String analyze_re(String gas_item,String re){
		Pattern p = Pattern.compile(re);  //获取每一个加油站信息
        Matcher m = p.matcher(gas_item); 
        while(m.find()){  
        	return m.group(1);
        }
		return "NOT_FOUNT";
	}
	/**
	 * 返回值
	 * 	0-------用户不存在，未注册
	 *	1-------密码错误
	 *	2-------登录成功
	 * @param result 输入的服务端返回的结果Json {"resultcode":0}
	 * @return 返回-1表示未知错误
	 */
	public static int getReturnCode(String result){
		Pattern p = Pattern.compile("\\{\"returncode\":([0-9])\\}");
		Matcher m = p.matcher(result);
		while(m.find()){
			return Integer.valueOf(m.group(1));
		}
		return -1;//返回-1表示未知错误
	}
	

}