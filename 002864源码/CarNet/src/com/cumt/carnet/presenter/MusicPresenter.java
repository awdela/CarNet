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
		// startSearch(this.context);//MusicPresenter对象构造时就进行线程开启检索音乐
	}

	public ArrayAdapter<String> getMusicNameListAdapter() {
		musicNameList = musicStatus.getMusicNameList(musicInfoList);
		// 获取音乐列表
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
				android.R.layout.simple_list_item_1,
				// musicStatus.getMusicNameList(musicInfoList));
				musicNameList);
		return adapter;
	}

	// buttonfloat旋转动画
	public void showFloatRotate(ButtonFloat btn) {
		ObjectAnimator animator = ObjectAnimator.ofFloat(btn, "rotation", 0F,
				90F);// 360度旋转
		animator.setDuration(1000);
		animator.start();
	}

	// buttonfloat旋转动画
	public void hideFloatRotate(ButtonFloat btn) {
		ObjectAnimator animator = ObjectAnimator.ofFloat(btn, "rotation", 90F,
				0F);// 360度旋转
		animator.setDuration(1000);
		animator.start();
	}
	
	//用户选择音乐列表后进行播放界面的设置
	public void setMusicPlayView(TextView textView,TextView textViewAuthor){
		if(musicInfoList == null){
			// 获取音乐列表
			CarnetApplication carApp = (CarnetApplication) context
					.getApplicationContext();
			musicInfoList = carApp.getMusicInfoList();
		}
		textView.setText(musicInfoList.get(musicId).musicName);
		textViewAuthor.setText(musicInfoList.get(musicId).artist);
	}
	//播放音乐 判断是否需要恢复暂停的音乐
	public void playMusic(MusicService.MusicBinder controlMusicBinder,int musicId,boolean ifResume){
		if(musicId >= 0){
			this.musicId = musicId;//设置正在播放的ID
		}
		controlMusicBinder.orderMusic(MusicUtils.START_PLAY,musicInfoList.get(this.musicId),ifResume);
	}
	//刚开始时播放音乐
	public void playMusicWhenStart(MusicService.MusicBinder controlMusicBinder,String data){
		int id = 0;
		for(MusicInfo item:musicInfoList){//获取初始播放音乐的id
			if(item.data == data || item.data.equals(data)){
				break;
			}
			id++;
		}
		this.musicId = id;//更新当前播放的音乐id
		playMusic(controlMusicBinder,id,false);
	}
	//暂停正在播放的音乐
	public void pauseMusic(MusicService.MusicBinder controlMusicBinder){
		controlMusicBinder.orderMusic(MusicUtils.PAUSE_PLAY,musicInfoList.get(musicId),false);
	}
	
	//播放下一首音乐
	public void playNextMusic(MusicService.MusicBinder controlMusicBinder){
		if(musicId == musicInfoList.size() - 1)
			musicId = 0;
		else
			musicId += 1;
		controlMusicBinder.orderMusic(MusicUtils.START_PLAY,musicInfoList.get(musicId),false);
	}
	
	//播放上一首音乐
	public void playLastMusic(MusicService.MusicBinder controlMusicBinder){
		if(musicId == 0)
			musicId = musicInfoList.size() - 1;
		else
			musicId -= 1;
		controlMusicBinder.orderMusic(MusicUtils.START_PLAY,musicInfoList.get(musicId),false);
	}
	//随机播放
	public void randomPlay(MusicService.MusicBinder controlMusicBinder){
		Random ra =new Random();
		musicId = ra.nextInt(musicInfoList.size()-1);//随机生成id
		controlMusicBinder.orderMusic(MusicUtils.START_PLAY,musicInfoList.get(musicId),false);
	}
	
	
	//------------------------------------------------------------------------------
	//------------------------------------------------------------------------------
	//若音乐正在播放被删除会   崩掉  ！！！！！！！！！！！！！！！！！！！！！！！！！！
	//更新媒体库
	public void refreshMediaStore(ArrayAdapter<String> adapter,int position){
		musicInfoList.remove(position);
		musicNameList.remove(position);
		Log.i("refreshMediaStore","refreshMediaStore");
		adapter.notifyDataSetChanged();
	}
	/**
	 * 设置专辑封面
	 * @param image
	 */
	public void setFengmian(ImageView image){
		Bitmap defaultBmp = BitmapFactory.decodeResource(((MainActivity)context).getResources(), R.drawable.zhuanji);
		Bitmap bmp = MusicUtils.getCachedArtwork(context, 
				musicInfoList.get(musicId).albumId, defaultBmp);
		image.setImageBitmap(bmp);
	}
}
