package com.cumt.drawerlayout.personal;

import java.io.File;

import android.content.Context;
import android.os.Environment;
import android.widget.ImageView;

import com.app.CarnetApplication;
import com.image.ImageLoader;

public class UserMsgPresenter {
	
	private Context context;
	
	public UserMsgPresenter(Context context) {
		super();
		this.context = context;
	}

	// ���ػ���ͷ��
	public void loadCacheHeadImage(ImageView imageView) {
		File tmpDir = new File(Environment.getExternalStorageDirectory() + "/"
				+ "CarNet");
		if (!tmpDir.exists())
			tmpDir.mkdir();
		CarnetApplication carApp = CarnetApplication.getInstance();
		String user = carApp.getUsername();
		String imageUrl = tmpDir.getAbsolutePath() + "/" + user + "head.png";
		ImageLoader imageLoader = new ImageLoader(context);
		imageLoader.showImage(imageUrl, imageView);
	}

}
