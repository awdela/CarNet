package com.image;

import android.graphics.Bitmap;
/**
 * ͼƬ����ӿ� ���ֻ��淽ʽʵ������ӿ�
 * @author wangcan
 *
 */
public interface ImageCache {
	public void putImage(String imageUrl,Bitmap bitmap);//���ڻ���ͼƬ
	public Bitmap getImage(String imageUrl);//���ڴӻ����л�ȡͼƬ
}