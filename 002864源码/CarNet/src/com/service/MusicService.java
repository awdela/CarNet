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
	private boolean ifPausing = false;// 是否有音乐暂停
	private String musicUrl = null;
	public static final String MUSIC_FINISH_BROADCAST = "com.cumt.carnet.MUSIC_FINISH";

	@Override
	public IBinder onBind(Intent intent) {
		Log.i("qqqqq", "onBind");
		return myMusicBinder;
	}

	// 服务创建时调用
	@Override
	public void onCreate() {
		mp = new MediaPlayer();// 用于播放音乐
		mp.setOnCompletionListener(new OnCompletionListener() {
			
			public void onCompletion(MediaPlayer mp) {
				if(myMusicBinder.music_play_mode == MusicBinder.LIST_RECYLE){//列表循环
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

	// 每次服务启东时调用
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i("qqqqq", "onStartCommand");
		return super.onStartCommand(intent, flags, startId);
	}

	// 服务销毁时调用
	@Override
	public void onDestroy() {
		Log.i("qqqqq", "onDestroy");
		super.onDestroy();
	}

	// Binder,用于与Activity通信
	public class MusicBinder extends Binder {
		
		public static final int SINGLE_RECYLE = 0;
		public static final int LIST_RECYLE = 1;
		public static final int RANDOM_PLAY = 2;
		//音乐播放
		protected int music_play_mode = LIST_RECYLE; //默认是列表循环
		
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

		// 开始播放
		protected void startPlay(String url,boolean ifResume) {// 音乐的路径 是否处理resume
			Log.i("qqqqq", "binder-startplay");
			if(ifPausing && ifResume){
				resumePlay();
				return;
			}
			try {
				musicUrl = url;
				mp.reset();// 把各项参数恢复到初始状态
				mp.setDataSource(url);
				mp.prepare();
				mp.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// 暂停
		private void pausePlay() {
			Log.i("qqqqq", "binder-stopplay");
			if (mp != null && mp.isPlaying()) {
				mp.pause();
			}
			ifPausing = true;// 有音乐暂停
		}

		// 继续开始播放
		private void resumePlay() {
			mp.start();
			ifPausing = false;
		}
	}
}
