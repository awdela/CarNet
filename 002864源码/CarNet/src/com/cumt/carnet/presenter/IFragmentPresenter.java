package com.cumt.carnet.presenter;

import com.cumt.book.entity.BookBean;

import android.app.Fragment;
import android.widget.ImageView;
import android.widget.TextView;

public interface IFragmentPresenter {
	public void setDefaultFragment();//设置默认的frament
	public void switchContent(Fragment from, Fragment to);//Fragment跳转
	public void startGetGasMessageThread();//开启获取加油站信息的线程
	public void refreshBaiMapFragmentWithGas();//更新百度地图UI，添加加油站信息
	public void changeActionBarColor(ImageView imageView,TextView textView);//改变对应的fragment底部栏控件颜色状态
	public void setInitActionbarColor();//设置初始时对应的fragment底部栏控件颜色状态
	public void showBookOfBookFragment(BookBean bookBean);//展示bookFragment中的当前订单信息
	public ImageView getParentView();
}
