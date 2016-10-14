package com.image;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;

/**
 * ʹ��SD�����л���ͼƬ
 * @author wangcan
 *
 */
public class DiskCache implements ImageCache{
	
	private CompressFormat compressFormat = CompressFormat.PNG;//Ĭ��ͼƬ��ʽpng
	
	public void putImage(String imageUrl, Bitmap bitmap) {//��ͼƬ������SD����
		checkImageType(imageUrl);//���ͼƬ����
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(imageUrl);//�ļ����������
			bitmap.compress(compressFormat, 85, fileOutputStream);//��bitmap����ΪpngͼƬ�ļ�
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}finally{//��finally�йر������
			if(fileOutputStream != null){
				try{
					fileOutputStream.close();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}
	}

	public Bitmap getImage(String imageUrl) {//��SD���л�ȡͼƬ
		return BitmapFactory.decodeFile(imageUrl);
	}
	/**
	 * ���ͼƬ�����Ծ��������ͼƬ�ĸ�ʽ
	 * @param imageUrl
	 */
	private void checkImageType(String imageUrl){
		if(imageUrl.endsWith(".png"))//png����ͼƬ
			compressFormat = CompressFormat.PNG;
		else if(imageUrl.endsWith(".jpg"))//jpg����ͼƬ
			compressFormat = CompressFormat.JPEG;
	}
}