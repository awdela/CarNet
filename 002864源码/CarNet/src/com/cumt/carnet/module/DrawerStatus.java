package com.cumt.carnet.module;

import java.util.ArrayList;
import java.util.List;

import com.cumt.carnet.R;
import com.cumt.carnet.entity.DrawerItemBean;

/**
 * ������DrawerStatus
 * ���ã����ڲ໬�˵���listview�е����ݵķ�װ
 * @author wangcan
 *
 */
public class DrawerStatus implements IDrawerStatus{
	
	/**
	 * ��ȡ��໬���˵�����ʾ������
	 */
	public List<DrawerItemBean> getDrawerItemList() {
		List<DrawerItemBean> itemList = new ArrayList<DrawerItemBean>();
		DrawerItemBean personalMessage = new DrawerItemBean("�ҵ���Ϣ", R.drawable.myinfor);
		itemList.add(personalMessage);
		DrawerItemBean scancer = new DrawerItemBean("ɨ��ά��", R.drawable.erweima);
		itemList.add(scancer);
		DrawerItemBean myFriend = new DrawerItemBean("�ҵ���Ϣ", R.drawable.my_messages);
		itemList.add(myFriend);
		DrawerItemBean setting = new DrawerItemBean("������Ϣ", R.drawable.carinfor);
		itemList.add(setting);
		DrawerItemBean changePass = new DrawerItemBean("��������", R.drawable.dingdan);
		itemList.add(changePass);
		DrawerItemBean returnMain = new DrawerItemBean("���ֹ���", R.drawable.music_manage);
		itemList.add(returnMain);
		DrawerItemBean exitApp = new DrawerItemBean("ϵͳ����", R.drawable.settings);
		itemList.add(exitApp);
		return itemList;
	}
}
