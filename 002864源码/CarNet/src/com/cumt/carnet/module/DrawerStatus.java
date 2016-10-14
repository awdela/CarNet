package com.cumt.carnet.module;

import java.util.ArrayList;
import java.util.List;

import com.cumt.carnet.R;
import com.cumt.carnet.entity.DrawerItemBean;

/**
 * 类名：DrawerStatus
 * 作用：用于侧滑菜单的listview中的数据的封装
 * @author wangcan
 *
 */
public class DrawerStatus implements IDrawerStatus{
	
	/**
	 * 获取左侧滑动菜单栏显示的内容
	 */
	public List<DrawerItemBean> getDrawerItemList() {
		List<DrawerItemBean> itemList = new ArrayList<DrawerItemBean>();
		DrawerItemBean personalMessage = new DrawerItemBean("我的信息", R.drawable.myinfor);
		itemList.add(personalMessage);
		DrawerItemBean scancer = new DrawerItemBean("扫二维码", R.drawable.erweima);
		itemList.add(scancer);
		DrawerItemBean myFriend = new DrawerItemBean("我的消息", R.drawable.my_messages);
		itemList.add(myFriend);
		DrawerItemBean setting = new DrawerItemBean("车辆信息", R.drawable.carinfor);
		itemList.add(setting);
		DrawerItemBean changePass = new DrawerItemBean("订单管理", R.drawable.dingdan);
		itemList.add(changePass);
		DrawerItemBean returnMain = new DrawerItemBean("音乐管理", R.drawable.music_manage);
		itemList.add(returnMain);
		DrawerItemBean exitApp = new DrawerItemBean("系统设置", R.drawable.settings);
		itemList.add(exitApp);
		return itemList;
	}
}
