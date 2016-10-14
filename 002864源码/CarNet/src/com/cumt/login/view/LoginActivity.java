package com.cumt.login.view;

import java.io.File;
import java.lang.ref.WeakReference;

import com.app.CarnetApplication;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.navisdk.adapter.BNOuterTTSPlayerCallback;
import com.baidu.navisdk.adapter.BNaviSettingManager;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.baidu.navisdk.adapter.BaiduNaviManager.NaviInitListener;
import com.cumt.carnet.R;
import com.cumt.carnet.view.MainActivity;
import com.cumt.login.presenter.ILoginPresenter;
import com.cumt.login.presenter.LoginPresenter;
import com.cumt.register.view.RegisterActivity;
import com.cumt.util.NormalMethodsUtils;
import com.cumt.view.ButtonRectangle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

/**
 * ��¼���� �����û���¼�������ʾ�����û����н��� �����û�������˺��������Ϣ
 * 
 * @author wangle
 */
public class LoginActivity extends Activity implements ILoginPresenter {

	private ButtonRectangle btnLogin;// ��¼��ť
	private ButtonRectangle regButton;// ע�ᰴť
	private EditText userNameText;// �û���EditText
	private EditText userPassText;// �û�����Edittext
	private LoginPresenter loginPresenter;

	private String mSDCardPath = null;

	private String authinfo = null;

	private MyHandler mHandler = null;

	private static final String APP_FOLDER_NAME = "CarNet";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// ���ر�����
		setContentView(R.layout.activity_login);
		// �ٶȵ�ͼsdk��ʼ��
		SDKInitializer.initialize(getApplicationContext());
		mHandler = new MyHandler(this);
		if (initDirs()) {
			initNavi();
		}

		initView();// ��ʼ���ؼ�
		loginPresenter = new LoginPresenter(LoginActivity.this,
				LoginActivity.this);
	}

	// ��ʼ���ؼ�
	private void initView() {
		btnLogin = (ButtonRectangle) findViewById(R.id.bnLogin);
		btnLogin.setOnClickListener(new OnClickListener() {// ��¼��ť����¼�

			public void onClick(View v) {

				// ��ʱ��д���û����Ĳ�����������-------------------���޸�-----------------------------------
				// ---------------��¼�ɹ���Ĳ���
     			CarnetApplication app = (CarnetApplication) getApplication();
				app.setUsername(getUserName());
//				// --------------------------------------------------------------------------
//				loginPresenter.login(1);// ʹ���ֻ��ŵ�¼ ��ʱע�͵� ����ɵ�¼ ������
				//gotoNextActivity();
				Intent intent=new Intent(LoginActivity.this, MainActivity.class);
				startActivity(intent);
				
			}
		});
		regButton = (ButtonRectangle) findViewById(R.id.bnRegister);
		regButton.setOnClickListener(new OnClickListener() {// ע�ᰴť����¼�

					public void onClick(View v) {
						// gotoRegisterActivity();
						NormalMethodsUtils.goNextActivity(
								getApplicationContext(), RegisterActivity.class,
								LoginActivity.this);
					}
				});

		userNameText = (EditText) findViewById(R.id.userNameText);
		userPassText = (EditText) findViewById(R.id.userPasswdText);

	}

	// ��ת����һ��activty
	public void gotoNextActivity() {
		NormalMethodsUtils.goNextActivity(getApplicationContext(),MainActivity.class,
				LoginActivity.this);
	}

	// ��ת��ע�����
	public void gotoRegisterActivity() {
		NormalMethodsUtils.goNextActivity(getApplicationContext(),
				RegisterActivity.class, LoginActivity.this);
	}

	// ��ȡ�û���
	public String getUserName() {
		return userNameText.getText().toString();

	}

	// ��ȡ�û�����
	public String getUserPass() {
		return userPassText.getText().toString();
	}

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

	private void initNavi() {

		@SuppressWarnings("unused")
		BNOuterTTSPlayerCallback ttsCallback = null;

		BaiduNaviManager.getInstance().init(this, mSDCardPath, APP_FOLDER_NAME,
				new NaviInitListener() {
					public void onAuthResult(int status, String msg) {
						if (0 == status) {
							authinfo = "keyУ��ɹ�!";
						} else {
							authinfo = "keyУ��ʧ��, " + msg;
						}
						LoginActivity.this.runOnUiThread(new Runnable() {

							public void run() {
								Toast.makeText(LoginActivity.this, authinfo,
										Toast.LENGTH_LONG).show();
							}
						});
					}

					public void initSuccess() {
						Toast.makeText(LoginActivity.this, "�ٶȵ��������ʼ���ɹ�",
								Toast.LENGTH_SHORT).show();
						initSetting();
					}

					public void initStart() {
						Toast.makeText(LoginActivity.this, "�ٶȵ��������ʼ����ʼ",
								Toast.LENGTH_SHORT).show();
					}

					public void initFailed() {
						Toast.makeText(LoginActivity.this, "�ٶȵ��������ʼ��ʧ��",
								Toast.LENGTH_SHORT).show();
					}

				}, null, mHandler, null);
	}

	// Handler��̬�ڲ��࣬Ϊ�˷�ֹ�ڴ�й©
	static class MyHandler extends Handler {

		private WeakReference<LoginActivity> mOuter;

		public MyHandler(LoginActivity activity) {
			mOuter = new WeakReference<LoginActivity>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			LoginActivity outer = mOuter.get();
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
		LoginActivity.this.runOnUiThread(new Runnable() {

			public void run() {
				Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT)
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
	protected void onDestroy() {
		super.onDestroy();
		BaiduNaviManager.getInstance().uninit();
	}
}
