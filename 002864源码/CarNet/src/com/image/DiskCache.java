package com.image;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;

/**
 * 使用SD卡进行缓存图片
 * @author wangcan
 *
 */
public class DiskCache implements ImageCache{
	
	private CompressFormat compressFormat = CompressFormat.PNG;//默认图片格式png
	
	public void putImage(String imageUrl, Bitmap bitmap) {//将图片保存在SD卡中
		checkImageType(imageUrl);//检测图片类型
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(imageUrl);//文件输出流对象
			bitmap.compress(compressFormat, 85, fileOutputStream);//把bitmap保存为png图片文件
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}finally{//在finally中关闭输出流
			if(fileOutputStream != null){
				try{
					fileOutputStream.close();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}
	}

	public Bitmap getImage(String imageUrl) {//从SD卡中获取图片
		return BitmapFactory.decodeFile(imageUrl);
	}
	/**
	 * 检测图片类型以决定保存的图片的格式
	 * @param imageUrl
	 */
	private void checkImageType(String imageUrl){
		if(imageUrl.endsWith(".png"))//png类型图片
			compressFormat = CompressFormat.PNG;
		else if(imageUrl.endsWith(".jpg"))//jpg类型图片
			compressFormat = CompressFormat.JPEG;
	}
}