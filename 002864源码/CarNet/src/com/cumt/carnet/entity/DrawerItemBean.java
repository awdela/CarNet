package com.cumt.carnet.entity;
/**
 * ������DrawerItemBean
 * ���ã��໬�˵���listview��ÿһ���ʵ�壬��һ��ͼƬ���ı���Ϣ���
 * @author wangcan
 *
 */
public class DrawerItemBean {
	
	private String itemName;//drawer��item������
	private int imageId;//item�е�ͼƬ��id
	
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
