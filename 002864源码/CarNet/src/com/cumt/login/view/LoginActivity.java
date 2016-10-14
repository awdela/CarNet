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
 * 登录界面活动 用于用户登录界面的显示，与用户进行交互 接收用户输入的账号密码等信息
 * 
 * @author wangle
 */
public class LoginActivity extends Activity implements ILoginPresenter {

	private ButtonRectangle btnLogin;// 登录按钮
	private ButtonRectangle regButton;// 注册按钮
	private EditText userNameText;// 用户名EditText
	private EditText userPassText;// 用户密码Edittext
	private LoginPresenter loginPresenter;

	private String mSDCardPath = null;

	private String authinfo = null;

	private MyHandler mHandler = null;

	private static final String APP_FOLDER_NAME = "CarNet";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题栏
		setContentView(R.layout.activity_login);
		// 百度地图sdk初始化
		SDKInitializer.initialize(getApplicationContext());
		mHandler = new MyHandler(this);
		if (initDirs()) {
			initNavi();
		}

		initView();// 初始化控件
		loginPresenter = new LoginPresenter(LoginActivity.this,
				LoginActivity.this);
	}

	// 初始化控件
	private void initView() {
		btnLogin = (ButtonRectangle) findViewById(R.id.bnLogin);
		btnLogin.setOnClickListener(new OnClickListener() {// 登录按钮点击事件

			public void onClick(View v) {

				// 暂时把写入用户名的操作放在这里-------------------待修改-----------------------------------
				// ---------------登录成功后的操作
     			CarnetApplication app = (CarnetApplication) getApplication();
				app.setUsername(getUserName());
//				// --------------------------------------------------------------------------
//				loginPresenter.login(1);// 使用手机号登录 暂时注释掉 已完成登录 待测试
				//gotoNextActivity();
				Intent intent=new Intent(LoginActivity.this, MainActivity.class);
				startActivity(intent);
				
			}
		});
		regButton = (ButtonRectangle) findViewById(R.id.bnRegister);
		regButton.setOnClickListener(new OnClickListener() {// 注册按钮点击事件

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

	// 跳转到下一个activty
	public void gotoNextActivity() {
		NormalMethodsUtils.goNextActivity(getApplicationContext(),MainActivity.class,
				LoginActivity.this);
	}

	// 跳转到注册界面
	public void gotoRegisterActivity() {
		NormalMethodsUtils.goNextActivity(getApplicationContext(),
				RegisterActivity.class, LoginActivity.this);
	}

	// 获取用户名
	public String getUserName() {
		return userNameText.getText().toString();

	}

	// 获取用户密码
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
							authinfo = "key校验成功!";
						} else {
							authinfo = "key校验失败, " + msg;
						}
						LoginActivity.this.runOnUiThread(new Runnable() {

							public void run() {
								Toast.makeText(LoginActivity.this, authinfo,
										Toast.LENGTH_LONG).show();
							}
						});
					}

					public void initSuccess() {
						Toast.makeText(LoginActivity.this, "百度导航引擎初始化成功",
								Toast.LENGTH_SHORT).show();
						initSetting();
					}

					public void initStart() {
						Toast.makeText(LoginActivity.this, "百度导航引擎初始化开始",
								Toast.LENGTH_SHORT).show();
					}

					public void initFailed() {
						Toast.makeText(LoginActivity.this, "百度导航引擎初始化失败",
								Toast.LENGTH_SHORT).show();
					}

				}, null, mHandler, null);
	}

	// Handler静态内部类，为了防止内存泄漏
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
