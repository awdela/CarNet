package com.cumt.carnet.view;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.cumt.carnet.R;
import com.cumt.carnet.presenter.IMusicPresenter;
import com.cumt.carnet.presenter.MusicPresenter;
import com.cumt.util.SPUtils;
import com.cumt.view.ButtonFloat;
import com.cumt.view.MaterialPlayPauseButton;
import com.cumt.view.MyPopupWindow;
import com.service.MusicService;

public class MusicFragment extends Fragment implements OnClickListener,
		OnItemClickListener, IMusicPresenter {

	private MyPopupWindow musicPopup = null;
	private View rootView = null;// 缓存Fragment view
	private ButtonFloat btnFloat = null;
	private ArrayAdapter<String> adapter = null;
	private MusicPresenter musicPresenter = null;
	private boolean isRotate = false;// buttonfloat是否旋转
	// 音乐服务
	private MusicService.MusicBinder controlMusicBinder = null;
	private Intent serviceIntent = null;
	private ServiceConnection connection = null;
	// 音乐相关控件
	private MaterialPlayPauseButton btnPlay = null;
	private ImageButton nextButton = null, previousButton = null,
			randomButton = null, recyleButton = null;
	// 音乐歌名和演唱者文本
	private TextView musicName = null, musicSinger = null;
	private boolean isHasMusicPlay = false, isPlaying = false;// 是否有音乐播放，是否正在播放
	private boolean isSingleRecyle = false, isRandomPlay = false;// 是否单曲循环// 是否随机播放
	//音乐专辑封面
	private ImageView fengmianView = null;
	//broadcast
	MusicBroadcastReceiver broadcastReceiver;
	IntentFilter filter;
																	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 注册广播
		filter = new IntentFilter(
				MusicBroadcastReceiver.MUSIC_FINISH_BROADCAST);
		broadcastReceiver = new MusicBroadcastReceiver();
		getActivity().registerReceiver(broadcastReceiver, filter);
		if (musicPresenter == null) {
			musicPresenter = new MusicPresenter(getActivity());
			serviceIntent = new Intent(getActivity(), MusicService.class);
			connection = new ServiceConnection() {
				// 音乐服务
				public void onServiceDisconnected(ComponentName name) {
				}

				public void onServiceConnected(ComponentName name,
						IBinder service) {
					controlMusicBinder = (MusicService.MusicBinder) service;
					playMusicWhenStart();// 初始时设置播放音乐
				}
			};
			getActivity().startService(serviceIntent);// 开启音乐服务
			getActivity().bindService(serviceIntent, connection, 1);// 绑定服务
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		getActivity().unregisterReceiver(broadcastReceiver);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		getActivity().registerReceiver(broadcastReceiver, filter);
	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.fragment_musicview, null);
		}
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		if (btnFloat == null) {
			initButton();
			fengmianView = (ImageView) rootView.findViewById(R.id.fengmian);
		}
		return rootView;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// getActivity().unbindService(connection);// 解除绑定服务
		// getActivity().stopService(serviceIntent);// 关闭服务
		Log.i("33333", "调用了！！！");
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.buttonFloat:// 右上角悬浮的按钮
			setMusicNameListAdapter();
			if (musicPopup == null) {
				musicPopup = new MyPopupWindow(getActivity(),
						((MainActivity) getActivity()).getParentView(),
						R.layout.dialog_musicpopup, adapter);
				musicPopup.getListView().setOnItemClickListener(this);
			}
			if (isRotate) {
				hideFloatRotate();
			} else {
				showFloatRotate();
			}
			musicPopup.showPopupWindow();
			break;
		case R.id.material_play:
			if (isHasMusicPlay && isPlaying) {// 若果有音乐播放且正在播放
				musicPresenter.pauseMusic(controlMusicBinder);
				btnPlay.setToPause();// 暂停
				isPlaying = false;
			} else if (!isHasMusicPlay) {// 若果没有音乐播放
				btnPlay.setToPlay();// 播放
				musicPresenter.playMusic(controlMusicBinder, -1, true);// 播放第一首
				musicPresenter.setMusicPlayView(musicName, musicSinger);
				isHasMusicPlay = isPlaying = true;
			} else if (isHasMusicPlay && !isPlaying) {// 如果有音乐播放且处于停止播放状态
				btnPlay.setToPlay();// 播放
				musicPresenter.playMusic(controlMusicBinder, -1, true);
				isPlaying = true;
			}
			break;
		case R.id.last_song:// 播放上一首音乐
			btnPlay.setToPlay();// 播放
			musicPresenter.playLastMusic(controlMusicBinder);// 下一行必须在这行下面，因为这行代码要改变id
			musicPresenter.setMusicPlayView(musicName, musicSinger);// 改行利用id设置
			musicPresenter.setFengmian(fengmianView);
			btnPlay.setToPlay();// 播放
			isPlaying = true;
			break;
		case R.id.next_song:// 播放下一首音乐
			btnPlay.setToPlay();// 播放
			musicPresenter.playNextMusic(controlMusicBinder);
			musicPresenter.setMusicPlayView(musicName, musicSinger);
			musicPresenter.setFengmian(fengmianView);
			btnPlay.setToPlay();// 播放
			isPlaying = true;
			break;
		case R.id.recycle:// 单曲循环
			if (isSingleRecyle) {// 如果设置了单曲循环
				Toast.makeText(getActivity(), "列表循环", Toast.LENGTH_SHORT)
						.show();
				recyleButton.setBackgroundResource(R.drawable.recycle);// 设置列表循环图片背景
				controlMusicBinder
						.setMusicPlayMode(MusicService.MusicBinder.LIST_RECYLE);
				isSingleRecyle = false;
			} else {// 如果没有设置单曲循环
				Toast.makeText(getActivity(), "单曲循环", Toast.LENGTH_SHORT)
						.show();
				recyleButton.setBackgroundResource(R.drawable.single_play);// 设置单曲循环图片背景
				isSingleRecyle = true;
				controlMusicBinder
						.setMusicPlayMode(MusicService.MusicBinder.SINGLE_RECYLE);
			}
			break;
		case R.id.random:// 随机播放
			if (isRandomPlay) {// 如果设置了随机播放
				randomButton.setBackgroundResource(R.drawable.random);
				controlMusicBinder
						.setMusicPlayMode(MusicService.MusicBinder.LIST_RECYLE);
				isRandomPlay = false;
			} else {
				randomButton.setBackgroundResource(R.drawable.random_mode);
				Toast.makeText(getActivity(), "随机播放", Toast.LENGTH_SHORT)
						.show();
				btnPlay.setToPlay();// 播放
				musicPresenter.randomPlay(controlMusicBinder);
				musicPresenter.setMusicPlayView(musicName, musicSinger);
				musicPresenter.setFengmian(fengmianView);
				controlMusicBinder
						.setMusicPlayMode(MusicService.MusicBinder.RANDOM_PLAY);
				isRandomPlay = true;
			}

			break;
		}
	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		closePopupAndhideFloat();
		if (btnPlay.getState() == MaterialPlayPauseButton.PAUSE) {
			btnPlay.setToPlay();
		}
		// 播放音乐
		musicPresenter.playMusic(controlMusicBinder, position, false);
		musicPresenter.setMusicPlayView(musicName, musicSinger);
		musicPresenter.setFengmian(fengmianView);
	}

	// listview设置adapter
	public void setMusicNameListAdapter() {
		if (adapter == null) {
			adapter = musicPresenter.getMusicNameListAdapter();
		}
	}

	// buttonfloat旋转动画
	public void showFloatRotate() {
		musicPresenter.showFloatRotate(btnFloat);
		isRotate = true;
	}

	// buttonfloat旋转动画
	public void hideFloatRotate() {
		musicPresenter.hideFloatRotate(btnFloat);
		isRotate = false;
	}

	// 若用户在打开音乐列表后，没有关闭就点击其他fragment则进行调用，用于关闭popupwindow和floatbutton
	public void closePopupAndhideFloat() {
		if (isRotate) {
			hideFloatRotate();
			musicPopup.dismiss();
		}
	}

	// 按钮初始化
	private void initButton() {
		rootView.findViewById(R.id.play).setFocusable(false);
		btnFloat = (ButtonFloat) rootView.findViewById(R.id.buttonFloat);
		btnFloat.setOnClickListener(this);
		btnPlay = (MaterialPlayPauseButton) rootView
				.findViewById(R.id.material_play);
		btnPlay.setColor(Color.RED);
		btnPlay.setAnimDuration(300);
		nextButton = (ImageButton) rootView.findViewById(R.id.next_song);
		previousButton = (ImageButton) rootView.findViewById(R.id.last_song);
		randomButton = (ImageButton) rootView.findViewById(R.id.random);
		recyleButton = (ImageButton) rootView.findViewById(R.id.recycle);
		btnPlay.setOnClickListener(this);
		nextButton.setOnClickListener(this);
		previousButton.setOnClickListener(this);
		randomButton.setOnClickListener(this);
		recyleButton.setOnClickListener(this);
		musicName = (TextView) rootView.findViewById(R.id.song);
		musicSinger = (TextView) rootView.findViewById(R.id.artist);
	}

	private class MusicBroadcastReceiver extends BroadcastReceiver {

		public static final String MUSIC_FINISH_BROADCAST = "com.cumt.carnet.MUSIC_FINISH";

		public MusicBroadcastReceiver() {

		}

		@Override
		public void onReceive(Context context, Intent intent) {
			// 列表循环
			if (intent.getIntExtra("PLAY_MODE", 0) == MusicService.MusicBinder.LIST_RECYLE) {
				btnPlay.setToPlay();// 播放
				musicPresenter.playNextMusic(controlMusicBinder);
				musicPresenter.setMusicPlayView(musicName, musicSinger);
			} else if (intent.getIntExtra("REFRESH_MODE", -1) != -1) {
				// 更新媒体库
				musicPresenter.refreshMediaStore(adapter,
						intent.getIntExtra("REFRESH_MODE", -1));
				Toast.makeText(getActivity(), "我被调用了", Toast.LENGTH_LONG)
						.show();
				Log.i("refreshadapter", "我哦哦哦");
			} else {// 随机播放
				btnPlay.setToPlay();// 播放
				musicPresenter.randomPlay(controlMusicBinder);
				musicPresenter.setMusicPlayView(musicName, musicSinger);
			}
		}
	}

	// 刚开始时根据用户设置播放音乐
	// 首先检测在SP中是否保存了Ifplaymusic关键字，若保存了则读取该boolean值，否则随机播放一首
	// 若该boolean值为false,则不播放音乐，否则判断用户是否
	public void playMusicWhenStart() {
		if (SPUtils.contains(getActivity(), "IfPlayMusic")) {
			boolean ifPlay = (Boolean) SPUtils.get(getActivity(),
					"IfPlayMusic", true);
			if (ifPlay == false) {
				return;
			} else {
				if (SPUtils.contains(getActivity(), "MusicPath")) {
					String data = (String) SPUtils.get(getActivity(),
							"MusicPath", "CDCARD");
					Log.i("MusicPath",data);
					musicPresenter.playMusicWhenStart(controlMusicBinder, data);
					musicPresenter.setMusicPlayView(musicName, musicSinger);
					musicPresenter.setFengmian(fengmianView);
					isHasMusicPlay = isPlaying = true;
				} else {
					musicPresenter.randomPlay(controlMusicBinder);
					musicPresenter.setMusicPlayView(musicName, musicSinger);
					musicPresenter.setFengmian(fengmianView);
					isHasMusicPlay = isPlaying = true;
				}
			}
		} else {
			musicPresenter.randomPlay(controlMusicBinder);
			musicPresenter.setFengmian(fengmianView);
			musicPresenter.setMusicPlayView(musicName, musicSinger);
			isHasMusicPlay = isPlaying = true;
		}
	}
}
