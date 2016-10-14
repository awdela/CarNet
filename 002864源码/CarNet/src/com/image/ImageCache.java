package com.image;

import android.graphics.Bitmap;
/**
 * 图片缓存接口 各种缓存方式实现这个接口
 * @author wangcan
 *
 */
public interface ImageCache {
	public void putImage(String imageUrl,Bitmap bitmap);//用于缓存图片
	public Bitmap getImage(String imageUrl);//用于从缓存中获取图片
}