package com.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * 用于加载图片的类
 * 用户直接使用该类进行缓存图片的获取 
 * 以及进行图片的缓存
 * @author wangcan
 */
public class ImageLoader{
	
	ImageCache mImageCache  = new DiskCache();//图片缓存
	@SuppressWarnings("unused")
	private Context context;//上下文
	
	public ImageLoader(Context context){
		this.context = context;
	}
	/**
	 * 用于加载图片的方法，供用户使用
	 * @param imageUrl
	 * @param imageView
	 */
	public void showImage(String imageUrl,ImageView imageView){
		Bitmap bitmap = mImageCache.getImage(imageUrl);//从缓存中获取图片
		if(bitmap != null){//若缓存中已缓存该图片则加载到view上
			imageView.setImageBitmap(bitmap);
			return ;
		}
		//如果图片没有在缓存中，则从bmob后台数据库进行加载
		initialHead(imageView);
	}
	/*
	 * 若用户本地没有缓存头像则调用该方法从服务器端初始化头像
	 */
	private void initialHead(final ImageView imageView){
		
	}
}
