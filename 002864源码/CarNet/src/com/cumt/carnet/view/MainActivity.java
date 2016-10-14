package com.cumt.carnet.view;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.cumt.book.entity.BookBean;
import com.cumt.carnet.R;
import com.cumt.carnet.adapter.DrawerAdapter;
import com.cumt.carnet.entity.DrawerItemBean;
import com.cumt.carnet.module.DrawerListListener;
import com.cumt.carnet.presenter.DrawerPresenter;
import com.cumt.carnet.presenter.IDrawerPresenter;
import com.cumt.carnet.presenter.IFragmentPresenter;
import com.cumt.util.HttpUtils;
import com.cumt.util.SPUtils;
import com.cumt.view.MyPopupWindow;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 类名：MainActivity 作用：Android运行时APP的主界面，用于与用户交互，包括百度地图sdk的初始化，底部菜单栏的显示，侧滑菜单的显示
 * 
 * @author wangle
 * 
 */
public class MainActivity extends Activity implements IFragmentPresenter,
		IDrawerPresenter, OnClickListener {

	private ImageView mainImageView, guideImageView, postImageView,
			musicImageView, headImageView;// 主界面imageview,导航imageview
	private TextView mainTextView, guideTextView, postTextView, musicTextView;// 主界面textview,导航textview
	private ImageView nowImageView = null;// 当前的imageview
	private TextView nowTextView = null;// 当前的textview
	// Fragment
	private BaiduMapFragment baiduMapFragment = null;// 百度地图fragment
	private GuideFragment guideFragment = null;// 导航界面fragment
	private BookFragment bookFragment = null;// 预定界面fragment
	private MusicFragment musicFragment = null;// 音乐界面fragment

	private Fragment mContent;// 当前的fragment
	FragmentManager mFragmentMan = getFragmentManager();// *********************************

	private String gas_message_result;// post返回的加油站信息的结果
	private MyHandler mHandler;// 获取后通知主线程更新百度地图UI

	// 侧滑菜单
	private DrawerLayout drawerLayout;
	private ListView mDrawerList;
	private DrawerPresenter drawerPresenter;
	// 侧滑菜单listView点击事件
	DrawerListListener drawerListListener;
	// 弹出导航界面
	@SuppressWarnings("unused")
	private MyPopupWindow myPopupWindow;

	// 定时器
	private Timer timer = new Timer();
	private static final int TIME = 6000;// 每6s执行一次
	private TimerTask task = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题栏
		setContentView(R.layout.activity_main);
		setDefaultFragment();
		drawerPresenter = new DrawerPresenter(MainActivity.this,
				MainActivity.this);// DrawerPresenter.getDrawerInstance(MainActivity.this,
									// getApplicationContext());//生成单例
		initView();// 初始化控件,这里要用到DrawerPresenter对象，注意先获取DrawerPresenter对象
		mHandler = new MyHandler(this);
		// startGetGasMessageThread();//
		// 开启测试线程--------------------------------------------------
		initTimerTask();
		timer.schedule(task, TIME, TIME);

	}

	// 初始化view控件
	private void initView() {
		// 侧滑菜单控件引用
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		drawerPresenter.initDrawLayout();// 初始化侧滑菜单
		// listview点击事件
		drawerListListener = new DrawerListListener(MainActivity.this,
				MainActivity.this, drawerLayout);
		mDrawerList.setOnItemClickListener(drawerListListener);
		// 主界面imageview初始化
		mainImageView = (ImageView) findViewById(R.id.main_view);
		mainImageView.setOnClickListener(this);
		// 导航imageview按钮初始化
		guideImageView = (ImageView) findViewById(R.id.imageview_guide);
		guideImageView.setOnClickListener(this);
		// 订单imageview初始化
		postImageView = (ImageView) findViewById(R.id.main_post);
		postImageView.setOnClickListener(this);
		// 音乐imageview初始化
		musicImageView = (ImageView) findViewById(R.id.main_music);
		musicImageView.setOnClickListener(this);
		// 主界面textview初始化
		mainTextView = (TextView) findViewById(R.id.main_text);
		guideTextView = (TextView) findViewById(R.id.guide_text);
		postTextView = (TextView) findViewById(R.id.post_text);
		musicTextView = (TextView) findViewById(R.id.music_text);
		// 侧滑菜单头像imageview初始化
		headImageView = (ImageView) findViewById(R.id.touxiang);
		headImageView.setOnClickListener(this);
		loadCacheHeadImage();// 加载缓存的图片
		// fragment初始化
		guideFragment = new GuideFragment();
		bookFragment = new BookFragment();
		baiduMapFragment = new BaiduMapFragment();
		
		// // popupwindow初始化
		// myPopupWindow = new MyPopupWindow(MainActivity.this, guideImageView,
		// MainActivity.this);
		setInitActionbarColor();// 设置初始时对应的fragment底部栏控件颜色状态
	}

	// 设置默认的fragment
	public void setDefaultFragment() {
		// FragmentManager fm = getFragmentManager();
		FragmentTransaction transaction = mFragmentMan.beginTransaction();
		musicFragment = new MusicFragment();
		mContent = musicFragment;// *********************************
		transaction.replace(R.id.content, musicFragment);
		transaction.commit();
	}

	// IDrawerPresenter中方法实现，用于加载侧滑菜单
	public void initDrawerData(List<DrawerItemBean> itemList) {
		DrawerAdapter adapter = new DrawerAdapter(MainActivity.this,
				R.layout.list_item, itemList);
		mDrawerList.setAdapter(adapter);
	}

	// 底部菜单栏imageview点击事件以及侧滑菜单栏headImageview点击事件
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.main_view:
			switchContent(mContent, baiduMapFragment);// ***************
			mContent = baiduMapFragment;// ***************
			changeActionBarColor(mainImageView, mainTextView);// 改变actionbar颜色
			break;
		case R.id.imageview_guide:
			switchContent(mContent, guideFragment);// ***************
			mContent = guideFragment;// ***************
			changeActionBarColor(guideImageView, guideTextView);// 改变actionbar颜色
			break;
		case R.id.main_post:
			switchContent(mContent, bookFragment);// ***************
			mContent = bookFragment;// ***************
			changeActionBarColor(postImageView, postTextView);// 改变actionbar颜色
			break;
		case R.id.main_music:
			switchContent(mContent, musicFragment);// ***************
			mContent = musicFragment;// ***************
			changeActionBarColor(musicImageView, musicTextView);// 改变actionbar颜色
			break;
		case R.id.touxiang:
			setHeadImage();
			break;
		}
	}

	// Fragment之间的跳转
	public void switchContent(Fragment from, Fragment to) {
		if (from == musicFragment) {
			// 若用户在打开音乐列表后，没有关闭就点击其他fragment则进行调用，用于关闭popupwindow和floatbutton
			musicFragment.closePopupAndhideFloat();
		}
		if (mContent != to) {
			mContent = to;
			FragmentTransaction transaction = mFragmentMan.beginTransaction();
			if (!to.isAdded()) { // 先判断是否被add过
				transaction.hide(from).add(R.id.content, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
			} else {
				transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
			}
		}
	}

	// 开启获取加油站信息的线程
	public void startGetGasMessageThread() {
		new Thread(new Runnable() {
			public void run() {
				try {
					// Thread.sleep(5000);
					if (SPUtils.contains(MainActivity.this, "Lontitude")) {
						// Post参数
						gas_message_result = HttpUtils.requestByHttpPost(
								HttpUtils.paramsOfOil(MainActivity.this),
								HttpUtils.GAS_STATION_URL);
						Log.i("wwwwww", gas_message_result);
						// 构造消息通知主线程
						Message message = mHandler.obtainMessage();
						message.what = 2;
						mHandler.sendMessage(message);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	// 更新百度地图UI，添加加油站信息
	public void refreshBaiMapFragmentWithGas() {
		if (mContent != baiduMapFragment) {
			return;
		}
		Log.i("当前的content", "baiduMapFragment");
		baiduMapFragment.addGasToMap(gas_message_result);
	}

	// Timer定时器
	private void initTimerTask() {
		task = new TimerTask() {
			public void run() {
				Message message = mHandler.obtainMessage();
				message.what = 1;
				mHandler.sendMessage(message);
			}
		};
	}

	// Handler静态内部类，为了防止内存泄漏
	static class MyHandler extends Handler {

		private WeakReference<MainActivity> mOuter;

		public MyHandler(MainActivity activity) {
			mOuter = new WeakReference<MainActivity>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			MainActivity outer = mOuter.get();
			if (outer != null) {
				switch (msg.what) {
				case 1:
					outer.startGetGasMessageThread();// 开启线程
					break;
				case 2: {
					Log.d("wwwwwwwwcccccc", "wangcan");
					outer.refreshBaiMapFragmentWithGas();// 更新加油站信息
					break;
				}
				default:
					break;
				}
			}
		}
	}

	// 改变对应的fragment底部栏控件颜色状态
	public void changeActionBarColor(ImageView imageView, TextView textView) {
		nowImageView.setSelected(false);
		nowTextView.setTextColor(this.getResources().getColor(R.color.grey));
		imageView.setSelected(true);
		if(textView == mainTextView){
			textView.setTextColor(this.getResources().getColor(R.color.red));
		}else if(textView == guideTextView){
			textView.setTextColor(this.getResources().getColor(R.color.deep_blue));
		}else if(textView == postTextView){
			textView.setTextColor(this.getResources().getColor(R.color.orange));
		}else{
			textView.setTextColor(this.getResources().getColor(R.color.green));
		}
		nowImageView = imageView;
		nowTextView = textView;
	}

	// 设置初始时对应的fragment底部栏控件颜色状态
	public void setInitActionbarColor() {
		musicImageView.setSelected(true);
		musicTextView.setTextColor(this.getResources().getColor(R.color.green));
		// 记录当前的imageview和textview
		nowImageView = musicImageView;
		nowTextView = musicTextView;
	}

	// 展示bookFragment中的当前订单信息
	public void showBookOfBookFragment(BookBean bookBean) {
		switchContent(mContent, bookFragment);// ***************
		mContent = bookFragment;// ***************
		changeActionBarColor(postImageView, postTextView);// 改变actionbar颜色
		bookFragment.showZxingMessgae(bookBean);
	}

	@Override
	protected void onPause() {
		if (timer != null) {
			timer.cancel();
			timer = null;
			Log.i("timer", "Pause成功");
		}
		super.onPause();
	}

	@Override
	protected void onStop() {
		if (timer != null) {
			timer.cancel();
			timer.purge();
			timer = null;
			Log.i("timer", "stop成功");
		}
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		if (timer != null) {
			timer.cancel();
			timer = null;
			Log.i("timer", "stop成功");
		}
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (timer == null) {
			initTimerTask();
			timer = new Timer();
			timer.schedule(task, TIME, TIME);

		}
		Log.i("timer", "resume成功");
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		// 改变fragment为订单fragment
		switchContent(mContent, bookFragment);// ***************
		mContent = bookFragment;// ***************
		changeActionBarColor(postImageView, postTextView);// 改变actionbar颜色
		// 加载订单二维码
		BookBean bookBean = (BookBean) this.getIntent().getSerializableExtra(
				"BOOK_BEAN");
		if (bookBean != null) {
			showBookOfBookFragment(bookBean);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == GALLERY_REQUEST_CODE) {// 返回是相册
			if (data == null)// 如果data为null则直接返回
				return;
			Uri uri = data.getData();// 从data中获取android的content类型的url
			try {
				String res = null;
				String[] proj = { MediaStore.Images.Media.DATA };// 使用媒体库进行查询操作
				Cursor cursor = getContentResolver().query(uri, proj, null,
						null, null);// 定义查询游标
				if (cursor.moveToFirst()) {
					int column_index = cursor
							.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					res = cursor.getString(column_index);
				}
				cursor.close();// 关闭游标
				Uri fileUri = Uri.fromFile(new File(res));
				drawerPresenter.cropHeadImage(fileUri);
			} catch (Exception e) {
			}
		} else if (requestCode == CROP_REQUEST_CODE) {// 返回剪裁后的结果
			if (data == null)// 如果data为null则直接返回
				return;
			Bundle extras = data.getExtras();
			if (extras == null)
				return;
			Bitmap bm = extras.getParcelable("data");// 获取bitmap对象
			headImageView.setImageBitmap(bm);
			cahceHeadImage(bm);
		}
	}

	// 设置头像
	public void setHeadImage() {
		drawerPresenter.setHeadImage();
	}

	// 加载缓存头像
	public void loadCacheHeadImage() {
		drawerPresenter.loadCacheHeadImage(headImageView);
	}

	// 缓存图像
	public void cahceHeadImage(Bitmap bit) {
		drawerPresenter.cahceHeadImage(bit);
	}

	// 获取popupwindow的相对控件
	public ImageView getParentView() {
		return guideImageView;
	}
	
	//打开侧滑菜单
	public void openDrawerLayout(){
		drawerLayout.openDrawer(Gravity.LEFT);
	}

}