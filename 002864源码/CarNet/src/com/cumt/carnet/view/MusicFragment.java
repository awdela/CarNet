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
	private View rootView = null;// ����Fragment view
	private ButtonFloat btnFloat = null;
	private ArrayAdapter<String> adapter = null;
	private MusicPresenter musicPresenter = null;
	private boolean isRotate = false;// buttonfloat�Ƿ���ת
	// ���ַ���
	private MusicService.MusicBinder controlMusicBinder = null;
	private Intent serviceIntent = null;
	private ServiceConnection connection = null;
	// ������ؿؼ�
	private MaterialPlayPauseButton btnPlay = null;
	private ImageButton nextButton = null, previousButton = null,
			randomButton = null, recyleButton = null;
	// ���ָ������ݳ����ı�
	private TextView musicName = null, musicSinger = null;
	private boolean isHasMusicPlay = false, isPlaying = false;// �Ƿ������ֲ��ţ��Ƿ����ڲ���
	private boolean isSingleRecyle = false, isRandomPlay = false;// �Ƿ���ѭ��// �Ƿ��������
	//����ר������
	private ImageView fengmianView = null;
	//broadcast
	MusicBroadcastReceiver broadcastReceiver;
	IntentFilter filter;
																	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ע��㲥
		filter = new IntentFilter(
				MusicBroadcastReceiver.MUSIC_FINISH_BROADCAST);
		broadcastReceiver = new MusicBroadcastReceiver();
		getActivity().registerReceiver(broadcastReceiver, filter);
		if (musicPresenter == null) {
			musicPresenter = new MusicPresenter(getActivity());
			serviceIntent = new Intent(getActivity(), MusicService.class);
			connection = new ServiceConnection() {
				// ���ַ���
				public void onServiceDisconnected(ComponentName name) {
				}

				public void onServiceConnected(ComponentName name,
						IBinder service) {
					controlMusicBinder = (MusicService.MusicBinder) service;
					playMusicWhenStart();// ��ʼʱ���ò�������
				}
			};
			getActivity().startService(serviceIntent);// �������ַ���
			getActivity().bindService(serviceIntent, connection, 1);// �󶨷���
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
		// getActivity().unbindService(connection);// ����󶨷���
		// getActivity().stopService(serviceIntent);// �رշ���
		Log.i("33333", "�����ˣ�����");
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.buttonFloat:// ���Ͻ������İ�ť
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
			if (isHasMusicPlay && isPlaying) {// ���������ֲ��������ڲ���
				musicPresenter.pauseMusic(controlMusicBinder);
				btnPlay.setToPause();// ��ͣ
				isPlaying = false;
			} else if (!isHasMusicPlay) {// ����û�����ֲ���
				btnPlay.setToPlay();// ����
				musicPresenter.playMusic(controlMusicBinder, -1, true);// ���ŵ�һ��
				musicPresenter.setMusicPlayView(musicName, musicSinger);
				isHasMusicPlay = isPlaying = true;
			} else if (isHasMusicPlay && !isPlaying) {// ��������ֲ����Ҵ���ֹͣ����״̬
				btnPlay.setToPlay();// ����
				musicPresenter.playMusic(controlMusicBinder, -1, true);
				isPlaying = true;
			}
			break;
		case R.id.last_song:// ������һ������
			btnPlay.setToPlay();// ����
			musicPresenter.playLastMusic(controlMusicBinder);// ��һ�б������������棬��Ϊ���д���Ҫ�ı�id
			musicPresenter.setMusicPlayView(musicName, musicSinger);// ��������id����
			musicPresenter.setFengmian(fengmianView);
			btnPlay.setToPlay();// ����
			isPlaying = true;
			break;
		case R.id.next_song:// ������һ������
			btnPlay.setToPlay();// ����
			musicPresenter.playNextMusic(controlMusicBinder);
			musicPresenter.setMusicPlayView(musicName, musicSinger);
			musicPresenter.setFengmian(fengmianView);
			btnPlay.setToPlay();// ����
			isPlaying = true;
			break;
		case R.id.recycle:// ����ѭ��
			if (isSingleRecyle) {// ��������˵���ѭ��
				Toast.makeText(getActivity(), "�б�ѭ��", Toast.LENGTH_SHORT)
						.show();
				recyleButton.setBackgroundResource(R.drawable.recycle);// �����б�ѭ��ͼƬ����
				controlMusicBinder
						.setMusicPlayMode(MusicService.MusicBinder.LIST_RECYLE);
				isSingleRecyle = false;
			} else {// ���û�����õ���ѭ��
				Toast.makeText(getActivity(), "����ѭ��", Toast.LENGTH_SHORT)
						.show();
				recyleButton.setBackgroundResource(R.drawable.single_play);// ���õ���ѭ��ͼƬ����
				isSingleRecyle = true;
				controlMusicBinder
						.setMusicPlayMode(MusicService.MusicBinder.SINGLE_RECYLE);
			}
			break;
		case R.id.random:// �������
			if (isRandomPlay) {// ����������������
				randomButton.setBackgroundResource(R.drawable.random);
				controlMusicBinder
						.setMusicPlayMode(MusicService.MusicBinder.LIST_RECYLE);
				isRandomPlay = false;
			} else {
				randomButton.setBackgroundResource(R.drawable.random_mode);
				Toast.makeText(getActivity(), "�������", Toast.LENGTH_SHORT)
						.show();
				btnPlay.setToPlay();// ����
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
		// ��������
		musicPresenter.playMusic(controlMusicBinder, position, false);
		musicPresenter.setMusicPlayView(musicName, musicSinger);
		musicPresenter.setFengmian(fengmianView);
	}

	// listview����adapter
	public void setMusicNameListAdapter() {
		if (adapter == null) {
			adapter = musicPresenter.getMusicNameListAdapter();
		}
	}

	// buttonfloat��ת����
	public void showFloatRotate() {
		musicPresenter.showFloatRotate(btnFloat);
		isRotate = true;
	}

	// buttonfloat��ת����
	public void hideFloatRotate() {
		musicPresenter.hideFloatRotate(btnFloat);
		isRotate = false;
	}

	// ���û��ڴ������б��û�йرվ͵������fragment����е��ã����ڹر�popupwindow��floatbutton
	public void closePopupAndhideFloat() {
		if (isRotate) {
			hideFloatRotate();
			musicPopup.dismiss();
		}
	}

	// ��ť��ʼ��
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
			// �б�ѭ��
			if (intent.getIntExtra("PLAY_MODE", 0) == MusicService.MusicBinder.LIST_RECYLE) {
				btnPlay.setToPlay();// ����
				musicPresenter.playNextMusic(controlMusicBinder);
				musicPresenter.setMusicPlayView(musicName, musicSinger);
			} else if (intent.getIntExtra("REFRESH_MODE", -1) != -1) {
				// ����ý���
				musicPresenter.refreshMediaStore(adapter,
						intent.getIntExtra("REFRESH_MODE", -1));
				Toast.makeText(getActivity(), "�ұ�������", Toast.LENGTH_LONG)
						.show();
				Log.i("refreshadapter", "��ŶŶŶ");
			} else {// �������
				btnPlay.setToPlay();// ����
				musicPresenter.randomPlay(controlMusicBinder);
				musicPresenter.setMusicPlayView(musicName, musicSinger);
			}
		}
	}

	// �տ�ʼʱ�����û����ò�������
	// ���ȼ����SP���Ƿ񱣴���Ifplaymusic�ؼ��֣������������ȡ��booleanֵ�������������һ��
	// ����booleanֵΪfalse,�򲻲������֣������ж��û��Ƿ�
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
