package com.cumt.carnet.presenter;

import java.util.List;

import android.graphics.Bitmap;

import com.cumt.carnet.entity.DrawerItemBean;
/**
 * 接口：IDrawerPresenter
 * 作用：定义侧滑菜单的主要功能逻辑，在MainActivity中实现，由DrawerPresenter对象调用
 * @author wangcan
 *
 */
public interface IDrawerPresenter {
	
	public final int GALLERY_REQUEST_CODE = 1;//设置activity回调判断用的标记
	public final int CROP_REQUEST_CODE = 2;//设置系统剪裁activity的回调判断标记
	public void initDrawerData(List<DrawerItemBean> itemList);//初始化DrawerLayout中listview
	public void setHeadImage();//设置头像
	public void cahceHeadImage(Bitmap bit);//缓存图像
	public void loadCacheHeadImage();//加载缓存的头像 
	
}