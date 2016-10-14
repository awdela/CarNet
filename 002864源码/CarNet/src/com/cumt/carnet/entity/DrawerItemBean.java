package com.cumt.carnet.entity;
/**
 * 类名：DrawerItemBean
 * 作用：侧滑菜单中listview的每一项的实体，由一张图片加文本信息组成
 * @author wangcan
 *
 */
public class DrawerItemBean {
	
	private String itemName;//drawer中item的名称
	private int imageId;//item中的图片的id
	
	public DrawerItemBean(String itemName,int imageId){
		this.itemName = itemName;
		this.imageId = imageId;
	}
	
	public String getItemName(){
		return itemName;
	}
	
	public int getImageId() {
		return imageId;
	}
}
