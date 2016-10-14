package com.cumt.zxing.presenter;

import com.cumt.util.NormalLoadPictrue;
import com.cumt.zxing.entity.ZxingBean;
import com.cumt.zxing.module.CarInfoStatus;
import com.db.CarDataBaseHelper;
import com.db.CarInfoDao;

import android.content.Context;
import android.widget.ImageView;

public class CarInfoSavePresenter {
	
	@SuppressWarnings("unused")
	private Context context;
	private CarDataBaseHelper carDBHelper;
	private CarInfoDao carInfoDao;
	
	public CarInfoSavePresenter(Context context) {
		super();
		this.context = context;
		carDBHelper = CarDataBaseHelper.getHelper(context);
		carInfoDao = new CarInfoDao(context);
		carDBHelper.getWritableDatabase();//该语句会调用carDBHelper的onCrate建立数据库，若数据库已经存在则不会建立
	}
	
	//如果数据库中无该车信息 则将汽车信息写入数据库 上传到服务器
	public void saveCarInfoDB(ZxingBean zxingBean){
		
		if(carInfoDao.hasSomeData(zxingBean)){//若果已经存储过了则不保存
			
		}else{
			CarInfoStatus carInfoStatus = new CarInfoStatus();
			carInfoStatus.saveCarInfoNet(zxingBean);
			carInfoDao.saveCarInfoBean(zxingBean);//存储数据
		}
	}
	
	//加载汽车头像
	public void loadCarPhoto(String symbol,ImageView imageView){
		String uri = "http://115.159.205.135/carnet/images/" + symbol +".jpg";
		new NormalLoadPictrue().getPicture(uri, imageView);
	}
}
