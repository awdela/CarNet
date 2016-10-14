package com.cumt.splash;

import java.io.File;
import java.lang.ref.WeakReference;
import com.baidu.navisdk.adapter.BNOuterTTSPlayerCallback;
import com.baidu.navisdk.adapter.BNaviSettingManager;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.baidu.navisdk.adapter.BaiduNaviManager.NaviInitListener;
import com.cumt.carnet.R;
import com.cumt.login.view.LoginActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.widget.Toast;

public class SplashActivity extends Activity {

	private final int SPLASH_LENGTH = 3000;
	private static final String APP_FOLDER_NAME = "CarNet";

	private String mSDCardPath = null;

	private String authinfo = null;

	private MyHandler mHandler = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
//		mHandler = new MyHandler(this);
//		if (initDirs()) {
//			initNavi();
//		}

		new Handler().postDelayed(new Runnable() {
			public void run() {
				Intent mainIntent = new Intent(SplashActivity.this,
						LoginActivity.class);
				SplashActivity.this.startActivity(mainIntent);
				SplashActivity.this.finish();
			}
		}, SPLASH_LENGTH);
	}

	@SuppressWarnings("unused")
	private boolean initDirs() {
		mSDCardPath = getSdcardDir();
		if (mSDCardPath == null) {
			return false;
		}
		File f = new File(mSDCardPath, APP_FOLDER_NAME);
		if (!f.exists()) {
			try {
				f.mkdir();
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	private String getSdcardDir() {
		if (Environment.getExternalStorageState().equalsIgnoreCase(
				Environment.MEDIA_MOUNTED)) {
			return Environment.getExternalStorageDirectory().toString();
		}
		return null;
	}

	@SuppressWarnings("unused")
	private void initNavi() {

		BNOuterTTSPlayerCallback ttsCallback = null;

		BaiduNaviManager.getInstance().init(this, mSDCardPath, APP_FOLDER_NAME,
				new NaviInitListener() {
					public void onAuthResult(int status, String msg) {
						if (0 == status) {
							authinfo = "keyУ��ɹ�!";
						} else {
							authinfo = "keyУ��ʧ��, " + msg;
						}
						SplashActivity.this.runOnUiThread(new Runnable() {

							public void run() {
								Toast.makeText(SplashActivity.this, authinfo,
										Toast.LENGTH_LONG).show();
							}
						});
					}

					public void initSuccess() {
						Toast.makeText(SplashActivity.this, "�ٶȵ��������ʼ���ɹ�",
								Toast.LENGTH_SHORT).show();
						initSetting();
					}

					public void initStart() {
						Toast.makeText(SplashActivity.this, "�ٶȵ��������ʼ����ʼ",
								Toast.LENGTH_SHORT).show();
					}

					public void initFailed() {
						Toast.makeText(SplashActivity.this, "�ٶȵ��������ʼ��ʧ��",
								Toast.LENGTH_SHORT).show();
					}

				}, null, mHandler, null);
	}

	/**
	 * �ڲ�TTS����״̬�ش�handler
	 */
	// private Handler ttsHandler = new Handler() {
	// public void handleMessage(Message msg) {
	// int type = msg.what;
	// switch (type) {
	// case BaiduNaviManager.TTSPlayMsgType.PLAY_START_MSG: {
	// // showToastMsg("Handler : TTS play start");
	// break;
	// }
	// case BaiduNaviManager.TTSPlayMsgType.PLAY_END_MSG: {
	// // showToastMsg("Handler : TTS play end");
	// break;
	// }
	// default :
	// break;
	// }
	// }
	// };

	// Handler��̬�ڲ��࣬Ϊ�˷�ֹ�ڴ�й©
	static class MyHandler extends Handler {

		private WeakReference<SplashActivity> mOuter;

		public MyHandler(SplashActivity activity) {
			mOuter = new WeakReference<SplashActivity>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			SplashActivity outer = mOuter.get();
			if (outer != null) {
				switch (msg.what) {
				case BaiduNaviManager.TTSPlayMsgType.PLAY_START_MSG: {
					// showToastMsg("Handler : TTS play start");
					break;
				}
				case BaiduNaviManager.TTSPlayMsgType.PLAY_END_MSG: {
					// showToastMsg("Handler : TTS play end");
					break;
				}
				default:
					break;
				}
			}
		}
	}

	public void showToastMsg(final String msg) {
		SplashActivity.this.runOnUiThread(new Runnable() {

			public void run() {
				Toast.makeText(SplashActivity.this, msg, Toast.LENGTH_SHORT)
						.show();
			}
		});
	}

	private void initSetting() {
		BNaviSettingManager
				.setDayNightMode(BNaviSettingManager.DayNightMode.DAY_NIGHT_MODE_DAY);
		BNaviSettingManager
				.setShowTotalRoadConditionBar(BNaviSettingManager.PreViewRoadCondition.ROAD_CONDITION_BAR_SHOW_ON);
		BNaviSettingManager.setVoiceMode(BNaviSettingManager.VoiceMode.Veteran);
		BNaviSettingManager
				.setPowerSaveMode(BNaviSettingManager.PowerSaveMode.DISABLE_MODE);
		BNaviSettingManager
				.setRealRoadCondition(BNaviSettingManager.RealRoadCondition.NAVI_ITS_ON);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}
}
