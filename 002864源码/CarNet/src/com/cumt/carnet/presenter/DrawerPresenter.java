package com.cumt.carnet.presenter;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.app.CarnetApplication;
import com.cumt.carnet.module.DrawerStatus;
import com.cumt.carnet.view.MainActivity;
import com.image.DiskCache;
import com.image.ImageCache;
import com.image.ImageLoader;

/**
 * ������DrawerPresenter ���ã����ڴ�����໬�˵���ص��߼�
 * 
 * @author wangcan
 * 
 */
public class DrawerPresenter {

	private DrawerStatus drawerStatus;
	private Context context;
	private IDrawerPresenter iDrawerPresenter;

	public DrawerPresenter(MainActivity activity, Context context) {// ����ģʽ���췽��
		this.context = context;
		drawerStatus = new DrawerStatus();
		this.iDrawerPresenter = activity;
	}

	// ���ڳ�ʼ���໬�˵�
	public void initDrawLayout() {
		iDrawerPresenter.initDrawerData(drawerStatus.getDrawerItemList());
	}

	// ���ֻ�����ȡͷ��
	public void setHeadImage() {
		Intent intent = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				"image/*");
		((Activity) context).startActivityForResult(intent,
				IDrawerPresenter.GALLERY_REQUEST_CODE);
	}

	// ��ͷ����м��ô���
	public void cropHeadImage(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 100);// ���ü��ú�ߴ�100*100
		intent.putExtra("outputY", 100);
		intent.putExtra("return-data", true);
		((Activity) context).startActivityForResult(intent,
				IDrawerPresenter.CROP_REQUEST_CODE);
	}

	// ����ͼ��
	public void cahceHeadImage(Bitmap bitmap) {
		File tmpDir = new File(Environment.getExternalStorageDirectory() + "/"
				+ "CarNet");
		if (!tmpDir.exists())
			tmpDir.mkdir();
		CarnetApplication carApp = CarnetApplication.getInstance();
		String user = carApp.getUsername();
		String imageUrl = tmpDir.getAbsolutePath() + "/" + user + "head.png";
		ImageCache diskcache = new DiskCache();
		diskcache.putImage(imageUrl, bitmap);
	}

	// ���ػ���ͷ��
	public void loadCacheHeadImage(ImageView imageView) {
		File tmpDir = new File(Environment.getExternalStorageDirectory() + "/" +"CarNet");
        if(!tmpDir.exists())
            tmpDir.mkdir();
        CarnetApplication carApp = CarnetApplication.getInstance();
		String user = carApp.getUsername();
		String imageUrl = tmpDir.getAbsolutePath()
        		+ "/" + user
        		+ "head.png";
		ImageLoader imageLoader = new ImageLoader(context);
		imageLoader.showImage(imageUrl, imageView);
	}
}
