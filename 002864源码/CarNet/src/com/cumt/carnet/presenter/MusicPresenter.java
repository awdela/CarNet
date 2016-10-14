package com.cumt.carnet.presenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.CarnetApplication;
import com.cumt.carnet.R;
import com.cumt.carnet.entity.MusicInfo;
import com.cumt.carnet.module.MusicStatus;
import com.cumt.carnet.view.MainActivity;
import com.cumt.util.MusicUtils;
import com.cumt.view.ButtonFloat;
import com.nineoldandroids.animation.ObjectAnimator;
import com.service.MusicService;

public class MusicPresenter{
	
	private final static int SEARCH_OVER = 1;
	private int musicId = 0;
	
	@SuppressLint("HandlerLeak")
	Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SEARCH_OVER:
				break;
			}
			super.handleMessage(msg);
		}
	};

	private MusicStatus musicStatus = null;
	private Context context;

	private ArrayList<MusicInfo> musicInfoList = null;
	private List<String> musicNameList;

	public MusicPresenter(Context context) {
		musicStatus = new MusicStatus();
		this.context = context;
		CarnetApplication carApp = CarnetApplication.getInstance();
		musicInfoList = carApp.getMusicInfoList();
		// startSearch(this.context);//MusicPresenter������ʱ�ͽ����߳̿�����������
	}

	public ArrayAdapter<String> getMusicNameListAdapter() {
		musicNameList = musicStatus.getMusicNameList(musicInfoList);
		// ��ȡ�����б�
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
				android.R.layout.simple_list_item_1,
				// musicStatus.getMusicNameList(musicInfoList));
				musicNameList);
		return adapter;
	}

	// buttonfloat��ת����
	public void showFloatRotate(ButtonFloat btn) {
		ObjectAnimator animator = ObjectAnimator.ofFloat(btn, "rotation", 0F,
				90F);// 360����ת
		animator.setDuration(1000);
		animator.start();
	}

	// buttonfloat��ת����
	public void hideFloatRotate(ButtonFloat btn) {
		ObjectAnimator animator = ObjectAnimator.ofFloat(btn, "rotation", 90F,
				0F);// 360����ת
		animator.setDuration(1000);
		animator.start();
	}
	
	//�û�ѡ�������б����в��Ž��������
	public void setMusicPlayView(TextView textView,TextView textViewAuthor){
		if(musicInfoList == null){
			// ��ȡ�����б�
			CarnetApplication carApp = (CarnetApplication) context
					.getApplicationContext();
			musicInfoList = carApp.getMusicInfoList();
		}
		textView.setText(musicInfoList.get(musicId).musicName);
		textViewAuthor.setText(musicInfoList.get(musicId).artist);
	}
	//�������� �ж��Ƿ���Ҫ�ָ���ͣ������
	public void playMusic(MusicService.MusicBinder controlMusicBinder,int musicId,boolean ifResume){
		if(musicId >= 0){
			this.musicId = musicId;//�������ڲ��ŵ�ID
		}
		controlMusicBinder.orderMusic(MusicUtils.START_PLAY,musicInfoList.get(this.musicId),ifResume);
	}
	//�տ�ʼʱ��������
	public void playMusicWhenStart(MusicService.MusicBinder controlMusicBinder,String data){
		int id = 0;
		for(MusicInfo item:musicInfoList){//��ȡ��ʼ�������ֵ�id
			if(item.data == data || item.data.equals(data)){
				break;
			}
			id++;
		}
		this.musicId = id;//���µ�ǰ���ŵ�����id
		playMusic(controlMusicBinder,id,false);
	}
	//��ͣ���ڲ��ŵ�����
	public void pauseMusic(MusicService.MusicBinder controlMusicBinder){
		controlMusicBinder.orderMusic(MusicUtils.PAUSE_PLAY,musicInfoList.get(musicId),false);
	}
	
	//������һ������
	public void playNextMusic(MusicService.MusicBinder controlMusicBinder){
		if(musicId == musicInfoList.size() - 1)
			musicId = 0;
		else
			musicId += 1;
		controlMusicBinder.orderMusic(MusicUtils.START_PLAY,musicInfoList.get(musicId),false);
	}
	
	//������һ������
	public void playLastMusic(MusicService.MusicBinder controlMusicBinder){
		if(musicId == 0)
			musicId = musicInfoList.size() - 1;
		else
			musicId -= 1;
		controlMusicBinder.orderMusic(MusicUtils.START_PLAY,musicInfoList.get(musicId),false);
	}
	//�������
	public void randomPlay(MusicService.MusicBinder controlMusicBinder){
		Random ra =new Random();
		musicId = ra.nextInt(musicInfoList.size()-1);//�������id
		controlMusicBinder.orderMusic(MusicUtils.START_PLAY,musicInfoList.get(musicId),false);
	}
	
	
	//------------------------------------------------------------------------------
	//------------------------------------------------------------------------------
	//���������ڲ��ű�ɾ����   ����  ����������������������������������������������������
	//����ý���
	public void refreshMediaStore(ArrayAdapter<String> adapter,int position){
		musicInfoList.remove(position);
		musicNameList.remove(position);
		Log.i("refreshMediaStore","refreshMediaStore");
		adapter.notifyDataSetChanged();
	}
	/**
	 * ����ר������
	 * @param image
	 */
	public void setFengmian(ImageView image){
		Bitmap defaultBmp = BitmapFactory.decodeResource(((MainActivity)context).getResources(), R.drawable.zhuanji);
		Bitmap bmp = MusicUtils.getCachedArtwork(context, 
				musicInfoList.get(musicId).albumId, defaultBmp);
		image.setImageBitmap(bmp);
	}
}
