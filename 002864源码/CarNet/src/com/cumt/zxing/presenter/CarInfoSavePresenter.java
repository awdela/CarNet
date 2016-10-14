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
		carDBHelper.getWritableDatabase();//���������carDBHelper��onCrate�������ݿ⣬�����ݿ��Ѿ������򲻻Ὠ��
	}
	
	//������ݿ����޸ó���Ϣ ��������Ϣд�����ݿ� �ϴ���������
	public void saveCarInfoDB(ZxingBean zxingBean){
		
		if(carInfoDao.hasSomeData(zxingBean)){//�����Ѿ��洢�����򲻱���
			
		}else{
			CarInfoStatus carInfoStatus = new CarInfoStatus();
			carInfoStatus.saveCarInfoNet(zxingBean);
			carInfoDao.saveCarInfoBean(zxingBean);//�洢����
		}
	}
	
	//��������ͷ��
	public void loadCarPhoto(String symbol,ImageView imageView){
		String uri = "http://115.159.205.135/carnet/images/" + symbol +".jpg";
		new NormalLoadPictrue().getPicture(uri, imageView);
	}
}
