package com.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * ���ڼ���ͼƬ����
 * �û�ֱ��ʹ�ø�����л���ͼƬ�Ļ�ȡ 
 * �Լ�����ͼƬ�Ļ���
 * @author wangcan
 */
public class ImageLoader{
	
	ImageCache mImageCache  = new DiskCache();//ͼƬ����
	@SuppressWarnings("unused")
	private Context context;//������
	
	public ImageLoader(Context context){
		this.context = context;
	}
	/**
	 * ���ڼ���ͼƬ�ķ��������û�ʹ��
	 * @param imageUrl
	 * @param imageView
	 */
	public void showImage(String imageUrl,ImageView imageView){
		Bitmap bitmap = mImageCache.getImage(imageUrl);//�ӻ����л�ȡͼƬ
		if(bitmap != null){//���������ѻ����ͼƬ����ص�view��
			imageView.setImageBitmap(bitmap);
			return ;
		}
		//���ͼƬû���ڻ����У����bmob��̨���ݿ���м���
		initialHead(imageView);
	}
	/*
	 * ���û�����û�л���ͷ������ø÷����ӷ������˳�ʼ��ͷ��
	 */
	private void initialHead(final ImageView imageView){
		
	}
}
