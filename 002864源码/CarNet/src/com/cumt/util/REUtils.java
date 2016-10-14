package com.cumt.util;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cumt.carnet.entity.GasItemBean;
import com.cumt.book.entity.GasPriceBean;

public class REUtils {

	// ��ȡ���м���վitem��������ʽ
	private static final String GAS_ITEM_RE = "\\{\"id\":\".*?[^:][0-9]\\}";
	// ��ȡһ������վitem�ļ���վid��������ʽ
	private static final String GAS_ID_RE = "\"id\":\"([0-9]+?)\"";
	// ��ȡһ������վitem�ļ���վ���Ƶ�������ʽ
	private static final String GAS_NAME_RE = "\"name\":\"(.*?)\"";
	// ��ȡһ������վitem�ļ���վ��ַ��������ʽ
	private static final String GAS_ADDRESS_RE = "\"address\":\"(.*?)\",\"brandname\"";
	// ��ȡһ������վitem�ļ���վ�Ƿ��ۿ۵�������ʽ
	private static final String GAS_DISCOUNT_RE = "\"discount\":\"(.{0,6})\"";
	// ��ȡһ������վitem�ļ���վ�ͼ���Ϣ��������ʽ
	private static final String GAS_PRICE_RE = "\"price\":\\{(.*?)\\}";
	// ��ȡһ������վitem�ļ���վ�����������ʽ
	private static final String GAS_DISTANCE_RE = "\"distance\":([0-9]{0,7})\\}";
	// ��ȡһ������վitem�ļ���վ�ľ��ȵ�������ʽ
	private static final String GAS_LONTITUDE_RE = "\"lon\":\"([0-9]{3}\\.[0-9]{0,15})";
	// ��ȡһ������վitem�ļ���վγ�ȵ�������ʽ
	private static final String GAS_LATITUDE_RE = "\"lat\":\"([0-9]{2}\\.[0-9]{0,15})";
	
	
	/**
	 * ���ڽ��Ӿۺ�����API��õļ���վJsonת��Ϊһ�����ļ���վitem��bean�б���󣬷���ʹ��
	 * @param json ��Ҫ�����Ļ�õľۺ����ݵ�json��Ϣ
	 * @return ���غ���GasItemBean���б�
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
	 * ��ȡ����վ���е�item
	 * @param gas_message
	 * @return
	 */
	public static ArrayList<String> getGasItem(String gas_message){
		ArrayList<String> list = new ArrayList<String>();
		Pattern p = Pattern.compile(GAS_ITEM_RE);  //��ȡÿһ������վ��Ϣ
        Matcher m = p.matcher(gas_message); 
        while(m.find()){  
        	list.add(m.group());
        }
		return list;
	}
	
	//��ȡ����վid
	public static String getGasId(String gas_item){
		return analyze_re(gas_item,GAS_ID_RE);
	}
	
	//��ȡ����վname
	public static String getGasName(String gas_item){
		return analyze_re(gas_item,GAS_NAME_RE);
	}
	
	//��ȡ����վaddress
	public static String getGasAddress(String gas_item){
		return analyze_re(gas_item,GAS_ADDRESS_RE);
	}
	
	//��ȡ����վ�Ƿ��ۿ�discount
	public static String getGasDiscount(String gas_item){
		return analyze_re(gas_item,GAS_DISCOUNT_RE);
	}
	
	//��ȡ����վ����distance
	public static String getGasDistance(String gas_item){
		return analyze_re(gas_item,GAS_DISTANCE_RE);
	}
	
	//��ȡ����վγ��latitude
	public static String getGasLatitude(String gas_item){
		return analyze_re(gas_item,GAS_LATITUDE_RE);
	}
	
	//��ȡ����վ����lantitude
	public static String getGasLantitude(String gas_item){
		return analyze_re(gas_item,GAS_LONTITUDE_RE);
	}
	
	//��ȡ����վ�ͼ�price ������:"E90":"5.16","E93":"5.53","E97":"5.88","E0":"5.11"
	public static String getGasPrice(String gas_item){
		return analyze_re(gas_item,GAS_PRICE_RE);
	}
	
	/**
	 * ��ȡ����վ���ּ����Ӧ����
	 * @param price ���������ͼ���Ӧ�۸� �磺"E90":"5.16","E93":"5.53","E97":"5.88","E0":"5.11"
	 * @return �����б�ÿ���б���һ���ͼۼ����ֵ�item
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
	 * ������ʽ����
	 * @param gas_item ÿһ������վjson��Ŀ
	 * @param re	ƥ���õ�������ʽ
	 * @return "NOT_FOUNT" δ�ҵ��ķ���ֵ
	 */
	private static String analyze_re(String gas_item,String re){
		Pattern p = Pattern.compile(re);  //��ȡÿһ������վ��Ϣ
        Matcher m = p.matcher(gas_item); 
        while(m.find()){  
        	return m.group(1);
        }
		return "NOT_FOUNT";
	}
	/**
	 * ����ֵ
	 * 	0-------�û������ڣ�δע��
	 *	1-------�������
	 *	2-------��¼�ɹ�
	 * @param result ����ķ���˷��صĽ��Json {"resultcode":0}
	 * @return ����-1��ʾδ֪����
	 */
	public static int getReturnCode(String result){
		Pattern p = Pattern.compile("\\{\"returncode\":([0-9])\\}");
		Matcher m = p.matcher(result);
		while(m.find()){
			return Integer.valueOf(m.group(1));
		}
		return -1;//����-1��ʾδ֪����
	}
	

}