package com.cumt.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.app.CarnetApplication;
import com.cumt.book.entity.BookBean;
import com.cumt.zxing.entity.ZxingBean;

import android.content.Context;


public class HttpUtils {

	// 聚合数据加油站信息API
	public static final String GAS_STATION_URL = "http://apis.juhe.cn/oil/local?key=e542bee1a72aa678e1f92250ae10b05b";

	
	// Post参数
//	List<NameValuePair> params = new ArrayList<NameValuePair>();
//	params.add(new BasicNameValuePair("lon", lon));
//	params.add(new BasicNameValuePair("lat", lat));
//	params.add(new BasicNameValuePair("r", area));
//	params.add(new BasicNameValuePair("page", "1"));
//	params.add(new BasicNameValuePair("format", "1"));
	public static String requestByHttpPost(List<NameValuePair> params,String url)
			throws Exception {
		String result = null;
		// 新建HttpPost对象
		HttpPost httpPost = new HttpPost(url);
		// 设置字符集
		HttpEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
		// 设置参数实体
		httpPost.setEntity(entity);
		HttpClient httpClient = null;
		try {
			// 获取HttpClient对象
			httpClient = new DefaultHttpClient();
			//设置超时
			httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
			// 获取HttpResponse实例
			HttpResponse httpResp = httpClient.execute(httpPost);
			// 判断是够请求成功
			// 获取返回的数据
			result = EntityUtils.toString(httpResp.getEntity(), "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
		return result;
	}
	
	/**
	 * 加油站的params构造
	 * @return 构造完成的登录json数据包
	 */
	public static List<NameValuePair> paramsOfOil(Context context){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("lon",""  + (Float) SPUtils.get(context, "Lontitude", 117.14591f)));
		params.add(new BasicNameValuePair("lat",""  + (Float) SPUtils.get(context, "Latitude", 34.216555f)));
		params.add(new BasicNameValuePair("r", "5000"));
		params.add(new BasicNameValuePair("page", "1"));
		params.add(new BasicNameValuePair("format", "1"));
		return params;
	}
	
	/**
	 * 修改密码params构造
	 * @param oldpass
	 * @param newpass
	 * @return
	 */
	public static List<NameValuePair> paramsOfChangePass(String oldpass,String newpass){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		CarnetApplication carApp = CarnetApplication.getInstance();
		String user = carApp.getUsername();
		params.add(new BasicNameValuePair("phone_number", user));
		params.add(new BasicNameValuePair("oldpassword", oldpass));
		params.add(new BasicNameValuePair("newpassword", newpass));
		return params;
	}
	/**
	 * 维护的汽车信息的params构造
	 * @param zxingBean
	 * @return
	 */
	public static List<NameValuePair> paramsOfSaveCarInfo(ZxingBean zxingBean){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		CarnetApplication carApp = CarnetApplication.getInstance();
		String user = carApp.getUsername();
		params.add(new BasicNameValuePair("phone_number", user));
		params.add(new BasicNameValuePair("brand", zxingBean.getBrand()));
		params.add(new BasicNameValuePair("symble", zxingBean.getSymbol()));
		params.add(new BasicNameValuePair("style", zxingBean.getStyle()));
		params.add(new BasicNameValuePair("car_number", zxingBean.getCar_number()));
		params.add(new BasicNameValuePair("engine", zxingBean.getEngine()));
		params.add(new BasicNameValuePair("level", zxingBean.getLevel()));
		params.add(new BasicNameValuePair("miles", zxingBean.getMiles()));
		params.add(new BasicNameValuePair("oil", zxingBean.getOil()));
		params.add(new BasicNameValuePair("engine_feature", zxingBean.getEngine_feature()));
		params.add(new BasicNameValuePair("transmision", zxingBean.getTransmation()));
		params.add(new BasicNameValuePair("light", zxingBean.getlight()));
		params.add(new BasicNameValuePair("vernum", zxingBean.getVernum()));
		params.add(new BasicNameValuePair("enginenum", zxingBean.getEnginenum()));
		return params;
	}
	/**
	 * 用户个人信息params
	 * @param nickname
	 * @param address
	 * @param birthday
	 * @param sex
	 * @param company
	 * @return
	 */
	public static List<NameValuePair> paramsOfPersonalMsg(String nickname,String address,
			String birthday,String sex,String company){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		CarnetApplication carApp = CarnetApplication.getInstance();
		String user = carApp.getUsername();
		params.add(new BasicNameValuePair("phone_number", user));
		params.add(new BasicNameValuePair("nickname", nickname));
		params.add(new BasicNameValuePair("address", address));
		params.add(new BasicNameValuePair("birthday", birthday));
		params.add(new BasicNameValuePair("sex", sex));
		params.add(new BasicNameValuePair("company", company));
		return params;
	}
	/**
	 * 订单信息
	 * @param bookBean
	 * @return
	 */
	public static List<NameValuePair> paramsOfBookMsg(BookBean bookBean){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		params.add(new BasicNameValuePair("phone_number", bookBean.getPhone_number()));
		params.add(new BasicNameValuePair("gasstation_name", bookBean.getGasstation_name()));
		params.add(new BasicNameValuePair("gas_address", bookBean.getGas_address()));
		params.add(new BasicNameValuePair("oilStyle", bookBean.getOilStyle()));
		params.add(new BasicNameValuePair("oil_price", ""+bookBean.getOil_price()));
		params.add(new BasicNameValuePair("count", ""+bookBean.getCount()));
		params.add(new BasicNameValuePair("total_price", bookBean.getPhone_number()));
		params.add(new BasicNameValuePair("oil_time", bookBean.getOil_time()));
		params.add(new BasicNameValuePair("make_date", bookBean.getMake_date()));
		params.add(new BasicNameValuePair("gas_state", ""+bookBean.getGas_state()));
		
		return params;
	}
	/**
	 * 
	 * @param data
	 * @return
	 */
	public static List<NameValuePair> paramsOfFeedBackMsg(String data){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		CarnetApplication carApp = CarnetApplication.getInstance();
		String user = carApp.getUsername();
		String time = Utils.getTime();
		params.add(new BasicNameValuePair("backdata",data));
		params.add(new BasicNameValuePair("phone_number",user));
		params.add(new BasicNameValuePair("time",time));
		
		return params;
	}
}
