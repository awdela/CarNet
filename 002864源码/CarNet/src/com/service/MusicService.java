package com.service;

import com.cumt.carnet.entity.MusicInfo;
import com.cumt.util.MusicUtils;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class MusicService extends Service {

	private MusicBinder myMusicBinder = new MusicBinder();
	private MediaPlayer mp;
	private boolean ifPausing = false;// �Ƿ���������ͣ
	private String musicUrl = null;
	public static final String MUSIC_FINISH_BROADCAST = "com.cumt.carnet.MUSIC_FINISH";

	@Override
	public IBinder onBind(Intent intent) {
		Log.i("qqqqq", "onBind");
		return myMusicBinder;
	}

	// ���񴴽�ʱ����
	@Override
	public void onCreate() {
		mp = new MediaPlayer();// ���ڲ�������
		mp.setOnCompletionListener(new OnCompletionListener() {
			
			public void onCompletion(MediaPlayer mp) {
				if(myMusicBinder.music_play_mode == MusicBinder.LIST_RECYLE){//�б�ѭ��
					Intent intent = new Intent(MUSIC_FINISH_BROADCAST);
					intent.putExtra("PLAY_MODE", MusicBinder.LIST_RECYLE);
					sendBroadcast(intent);
				}else if(myMusicBinder.music_play_mode == MusicBinder.SINGLE_RECYLE){
					myMusicBinder.startPlay(musicUrl, ifPausing);
				}else if(myMusicBinder.music_play_mode == MusicBinder.RANDOM_PLAY){
					Intent intent = new Intent(MUSIC_FINISH_BROADCAST);
					intent.putExtra("PLAY_MODE", MusicBinder.RANDOM_PLAY);
					sendBroadcast(intent);
				}
			}
		});
		Log.i("qqqqq", "onCreate");
		super.onCreate();
	}

	// ÿ�η�������ʱ����
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i("qqqqq", "onStartCommand");
		return super.onStartCommand(intent, flags, startId);
	}

	// ��������ʱ����
	@Override
	public void onDestroy() {
		Log.i("qqqqq", "onDestroy");
		super.onDestroy();
	}

	// Binder,������Activityͨ��
	public class MusicBinder extends Binder {
		
		public static final int SINGLE_RECYLE = 0;
		public static final int LIST_RECYLE = 1;
		public static final int RANDOM_PLAY = 2;
		//���ֲ���
		protected int music_play_mode = LIST_RECYLE; //Ĭ�����б�ѭ��
		
		public void setMusicPlayMode(int mode){
			 music_play_mode = mode;
		}
		
		public void orderMusic(int order,MusicInfo musicInfo,boolean ifResume){
			switch(order){
			case MusicUtils.START_PLAY:
				startPlay(musicInfo.data,ifResume);
				break;
			case MusicUtils.PAUSE_PLAY:
				pausePlay();
				break;
			case MusicUtils.STOP_PLAY:
				break;
			}
		}

		// ��ʼ����
		protected void startPlay(String url,boolean ifResume) {// ���ֵ�·�� �Ƿ���resume
			Log.i("qqqqq", "binder-startplay");
			if(ifPausing && ifResume){
				resumePlay();
				return;
			}
			try {
				musicUrl = url;
				mp.reset();// �Ѹ�������ָ�����ʼ״̬
				mp.setDataSource(url);
				mp.prepare();
				mp.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// ��ͣ
		private void pausePlay() {
			Log.i("qqqqq", "binder-stopplay");
			if (mp != null && mp.isPlaying()) {
				mp.pause();
			}
			ifPausing = true;// ��������ͣ
		}

		// ������ʼ����
		private void resumePlay() {
			mp.start();
			ifPausing = false;
		}
	}
}
