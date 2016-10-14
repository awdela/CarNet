package com.cumt.carnet.presenter;

import java.util.List;

import android.graphics.Bitmap;

import com.cumt.carnet.entity.DrawerItemBean;
/**
 * �ӿڣ�IDrawerPresenter
 * ���ã�����໬�˵�����Ҫ�����߼�����MainActivity��ʵ�֣���DrawerPresenter�������
 * @author wangcan
 *
 */
public interface IDrawerPresenter {
	
	public final int GALLERY_REQUEST_CODE = 1;//����activity�ص��ж��õı��
	public final int CROP_REQUEST_CODE = 2;//����ϵͳ����activity�Ļص��жϱ��
	public void initDrawerData(List<DrawerItemBean> itemList);//��ʼ��DrawerLayout��listview
	public void setHeadImage();//����ͷ��
	public void cahceHeadImage(Bitmap bit);//����ͼ��
	public void loadCacheHeadImage();//���ػ����ͷ�� 
	
}