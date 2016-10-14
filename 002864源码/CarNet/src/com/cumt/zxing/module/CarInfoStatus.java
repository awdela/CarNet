package com.cumt.zxing.module;

import java.util.List;
import org.apache.http.NameValuePair;
import com.cumt.util.HttpUtils;
import com.cumt.zxing.entity.ZxingBean;

public class CarInfoStatus {
	
	private static final String CARINFO_URL = "";
	
	//在服务端存储车辆信息
	public void saveCarInfoNet(final ZxingBean zxingBean){
		new Thread(){
			public void run() {
				List<NameValuePair> params = HttpUtils.paramsOfSaveCarInfo(zxingBean);
				try {
					HttpUtils.requestByHttpPost(params, CARINFO_URL);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			};
		}.start();
	}
}
