package com.cumt.carnet.module;

import java.util.List;

import com.cumt.carnet.entity.DrawerItemBean;
/**
 * 接口：IDrawerStatus
 * 作用：用于定义封装侧滑菜单listview中的数据项的方法
 * @author wangcan
 *
 */
public interface IDrawerStatus {
	public List<DrawerItemBean> getDrawerItemList();//获取保存item的列表
}
