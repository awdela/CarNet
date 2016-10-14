package com.app;

import java.util.ArrayList;

import com.cumt.carnet.entity.MusicInfo;
import com.cumt.util.MusicUtils;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.OnScanCompletedListener;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.Media;

/**
 * Android��Application�࣬�������appÿ������ʱ�������е��� �ڴ��������ǿ���һ���߳̽������ֵļ������������������п������ø���Ķ���
 * ��ȡ�õ���������Ϣ�б�:ArrayList<MusicInfo>
 * 
 * @author wangcan
 * 
 */
public class CarnetApplication extends Application {

	private static CarnetApplication instance;

	private String username;// ȫ�ֱ��� �û��� ���ֻ�����
	
	private String car_number = null;//�û��󶨵ĳ�
	
	protected ArrayList<MusicInfo> musicInfoList;

	public static CarnetApplication getInstance() {
		return instance;
	}

	@Override
	public void onCreate() {
		// ����ý���
		MediaScannerConnection.scanFile(this, new String[] { Environment
				.getExternalStorageDirectory().getAbsolutePath() }, null,
				new OnScanCompletedListener() {

					public void onScanCompleted(String path, Uri uri) {
						startSearch(getApplicationContext());
					}
				});
		super.onCreate();
		instance = this;
		startSearch(getApplicationContext());
		username = "15152113686";// Ĭ���û���
		//  initCarObeySearch();
	}

	private void startSearch(final Context context) {

		new Thread(new Runnable() {

			public void run() {
				Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;// -------------
				ContentResolver cr = context.getContentResolver();
				StringBuffer select = new StringBuffer(" 1=1 ");
				// ��ѯ��䣺������ʱ������1���ӣ��ļ���С����1MB��ý���ļ�
				select.append(" and " + Media.SIZE + " > "
						+ MusicUtils.FILTER_SIZE);
				select.append(" and " + Media.DURATION + " > "
						+ MusicUtils.FILTER_DURATION);

				musicInfoList = MusicUtils.getMusicList(cr.query(uri,
						MusicUtils.proj_music, select.toString(), null,
						MediaStore.Audio.Media.ARTIST_KEY));
			}
		}).start();
	}

	public ArrayList<MusicInfo> getMusicInfoList() {
		return musicInfoList;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	// ����ý���
	public void refreshMedia() {
		// ����ý���
		MediaScannerConnection.scanFile(this, new String[] { Environment
				.getExternalStorageDirectory().getAbsolutePath() }, null,
				new OnScanCompletedListener() {

					public void onScanCompleted(String path, Uri uri) {
						startSearch(getApplicationContext());
					}
				});
	}

	public String getCar_number() {
		return car_number;
	}

	public void setCar_number(String car_number) {
		this.car_number = car_number;
	}
	
}
