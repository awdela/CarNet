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
 * ������MainActivity ���ã�Android����ʱAPP�������棬�������û������������ٶȵ�ͼsdk�ĳ�ʼ�����ײ��˵�������ʾ���໬�˵�����ʾ
 * 
 * @author wangle
 * 
 */
public class MainActivity extends Activity implements IFragmentPresenter,
		IDrawerPresenter, OnClickListener {

	private ImageView mainImageView, guideImageView, postImageView,
			musicImageView, headImageView;// ������imageview,����imageview
	private TextView mainTextView, guideTextView, postTextView, musicTextView;// ������textview,����textview
	private ImageView nowImageView = null;// ��ǰ��imageview
	private TextView nowTextView = null;// ��ǰ��textview
	// Fragment
	private BaiduMapFragment baiduMapFragment = null;// �ٶȵ�ͼfragment
	private GuideFragment guideFragment = null;// ��������fragment
	private BookFragment bookFragment = null;// Ԥ������fragment
	private MusicFragment musicFragment = null;// ���ֽ���fragment

	private Fragment mContent;// ��ǰ��fragment
	FragmentManager mFragmentMan = getFragmentManager();// *********************************

	private String gas_message_result;// post���صļ���վ��Ϣ�Ľ��
	private MyHandler mHandler;// ��ȡ��֪ͨ���̸߳��°ٶȵ�ͼUI

	// �໬�˵�
	private DrawerLayout drawerLayout;
	private ListView mDrawerList;
	private DrawerPresenter drawerPresenter;
	// �໬�˵�listView����¼�
	DrawerListListener drawerListListener;
	// ������������
	@SuppressWarnings("unused")
	private MyPopupWindow myPopupWindow;

	// ��ʱ��
	private Timer timer = new Timer();
	private static final int TIME = 6000;// ÿ6sִ��һ��
	private TimerTask task = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// ���ر�����
		setContentView(R.layout.activity_main);
		setDefaultFragment();
		drawerPresenter = new DrawerPresenter(MainActivity.this,
				MainActivity.this);// DrawerPresenter.getDrawerInstance(MainActivity.this,
									// getApplicationContext());//���ɵ���
		initView();// ��ʼ���ؼ�,����Ҫ�õ�DrawerPresenter����ע���Ȼ�ȡDrawerPresenter����
		mHandler = new MyHandler(this);
		// startGetGasMessageThread();//
		// ���������߳�--------------------------------------------------
		initTimerTask();
		timer.schedule(task, TIME, TIME);

	}

	// ��ʼ��view�ؼ�
	private void initView() {
		// �໬�˵��ؼ�����
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		drawerPresenter.initDrawLayout();// ��ʼ���໬�˵�
		// listview����¼�
		drawerListListener = new DrawerListListener(MainActivity.this,
				MainActivity.this, drawerLayout);
		mDrawerList.setOnItemClickListener(drawerListListener);
		// ������imageview��ʼ��
		mainImageView = (ImageView) findViewById(R.id.main_view);
		mainImageView.setOnClickListener(this);
		// ����imageview��ť��ʼ��
		guideImageView = (ImageView) findViewById(R.id.imageview_guide);
		guideImageView.setOnClickListener(this);
		// ����imageview��ʼ��
		postImageView = (ImageView) findViewById(R.id.main_post);
		postImageView.setOnClickListener(this);
		// ����imageview��ʼ��
		musicImageView = (ImageView) findViewById(R.id.main_music);
		musicImageView.setOnClickListener(this);
		// ������textview��ʼ��
		mainTextView = (TextView) findViewById(R.id.main_text);
		guideTextView = (TextView) findViewById(R.id.guide_text);
		postTextView = (TextView) findViewById(R.id.post_text);
		musicTextView = (TextView) findViewById(R.id.music_text);
		// �໬�˵�ͷ��imageview��ʼ��
		headImageView = (ImageView) findViewById(R.id.touxiang);
		headImageView.setOnClickListener(this);
		loadCacheHeadImage();// ���ػ����ͼƬ
		// fragment��ʼ��
		guideFragment = new GuideFragment();
		bookFragment = new BookFragment();
		baiduMapFragment = new BaiduMapFragment();
		
		// // popupwindow��ʼ��
		// myPopupWindow = new MyPopupWindow(MainActivity.this, guideImageView,
		// MainActivity.this);
		setInitActionbarColor();// ���ó�ʼʱ��Ӧ��fragment�ײ����ؼ���ɫ״̬
	}

	// ����Ĭ�ϵ�fragment
	public void setDefaultFragment() {
		// FragmentManager fm = getFragmentManager();
		FragmentTransaction transaction = mFragmentMan.beginTransaction();
		musicFragment = new MusicFragment();
		mContent = musicFragment;// *********************************
		transaction.replace(R.id.content, musicFragment);
		transaction.commit();
	}

	// IDrawerPresenter�з���ʵ�֣����ڼ��ز໬�˵�
	public void initDrawerData(List<DrawerItemBean> itemList) {
		DrawerAdapter adapter = new DrawerAdapter(MainActivity.this,
				R.layout.list_item, itemList);
		mDrawerList.setAdapter(adapter);
	}

	// �ײ��˵���imageview����¼��Լ��໬�˵���headImageview����¼�
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.main_view:
			switchContent(mContent, baiduMapFragment);// ***************
			mContent = baiduMapFragment;// ***************
			changeActionBarColor(mainImageView, mainTextView);// �ı�actionbar��ɫ
			break;
		case R.id.imageview_guide:
			switchContent(mContent, guideFragment);// ***************
			mContent = guideFragment;// ***************
			changeActionBarColor(guideImageView, guideTextView);// �ı�actionbar��ɫ
			break;
		case R.id.main_post:
			switchContent(mContent, bookFragment);// ***************
			mContent = bookFragment;// ***************
			changeActionBarColor(postImageView, postTextView);// �ı�actionbar��ɫ
			break;
		case R.id.main_music:
			switchContent(mContent, musicFragment);// ***************
			mContent = musicFragment;// ***************
			changeActionBarColor(musicImageView, musicTextView);// �ı�actionbar��ɫ
			break;
		case R.id.touxiang:
			setHeadImage();
			break;
		}
	}

	// Fragment֮�����ת
	public void switchContent(Fragment from, Fragment to) {
		if (from == musicFragment) {
			// ���û��ڴ������б��û�йرվ͵������fragment����е��ã����ڹر�popupwindow��floatbutton
			musicFragment.closePopupAndhideFloat();
		}
		if (mContent != to) {
			mContent = to;
			FragmentTransaction transaction = mFragmentMan.beginTransaction();
			if (!to.isAdded()) { // ���ж��Ƿ�add��
				transaction.hide(from).add(R.id.content, to).commit(); // ���ص�ǰ��fragment��add��һ����Activity��
			} else {
				transaction.hide(from).show(to).commit(); // ���ص�ǰ��fragment����ʾ��һ��
			}
		}
	}

	// ������ȡ����վ��Ϣ���߳�
	public void startGetGasMessageThread() {
		new Thread(new Runnable() {
			public void run() {
				try {
					// Thread.sleep(5000);
					if (SPUtils.contains(MainActivity.this, "Lontitude")) {
						// Post����
						gas_message_result = HttpUtils.requestByHttpPost(
								HttpUtils.paramsOfOil(MainActivity.this),
								HttpUtils.GAS_STATION_URL);
						Log.i("wwwwww", gas_message_result);
						// ������Ϣ֪ͨ���߳�
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

	// ���°ٶȵ�ͼUI����Ӽ���վ��Ϣ
	public void refreshBaiMapFragmentWithGas() {
		if (mContent != baiduMapFragment) {
			return;
		}
		Log.i("��ǰ��content", "baiduMapFragment");
		baiduMapFragment.addGasToMap(gas_message_result);
	}

	// Timer��ʱ��
	private void initTimerTask() {
		task = new TimerTask() {
			public void run() {
				Message message = mHandler.obtainMessage();
				message.what = 1;
				mHandler.sendMessage(message);
			}
		};
	}

	// Handler��̬�ڲ��࣬Ϊ�˷�ֹ�ڴ�й©
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
					outer.startGetGasMessageThread();// �����߳�
					break;
				case 2: {
					Log.d("wwwwwwwwcccccc", "wangcan");
					outer.refreshBaiMapFragmentWithGas();// ���¼���վ��Ϣ
					break;
				}
				default:
					break;
				}
			}
		}
	}

	// �ı��Ӧ��fragment�ײ����ؼ���ɫ״̬
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

	// ���ó�ʼʱ��Ӧ��fragment�ײ����ؼ���ɫ״̬
	public void setInitActionbarColor() {
		musicImageView.setSelected(true);
		musicTextView.setTextColor(this.getResources().getColor(R.color.green));
		// ��¼��ǰ��imageview��textview
		nowImageView = musicImageView;
		nowTextView = musicTextView;
	}

	// չʾbookFragment�еĵ�ǰ������Ϣ
	public void showBookOfBookFragment(BookBean bookBean) {
		switchContent(mContent, bookFragment);// ***************
		mContent = bookFragment;// ***************
		changeActionBarColor(postImageView, postTextView);// �ı�actionbar��ɫ
		bookFragment.showZxingMessgae(bookBean);
	}

	@Override
	protected void onPause() {
		if (timer != null) {
			timer.cancel();
			timer = null;
			Log.i("timer", "Pause�ɹ�");
		}
		super.onPause();
	}

	@Override
	protected void onStop() {
		if (timer != null) {
			timer.cancel();
			timer.purge();
			timer = null;
			Log.i("timer", "stop�ɹ�");
		}
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		if (timer != null) {
			timer.cancel();
			timer = null;
			Log.i("timer", "stop�ɹ�");
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
		Log.i("timer", "resume�ɹ�");
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		// �ı�fragmentΪ����fragment
		switchContent(mContent, bookFragment);// ***************
		mContent = bookFragment;// ***************
		changeActionBarColor(postImageView, postTextView);// �ı�actionbar��ɫ
		// ���ض�����ά��
		BookBean bookBean = (BookBean) this.getIntent().getSerializableExtra(
				"BOOK_BEAN");
		if (bookBean != null) {
			showBookOfBookFragment(bookBean);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == GALLERY_REQUEST_CODE) {// ���������
			if (data == null)// ���dataΪnull��ֱ�ӷ���
				return;
			Uri uri = data.getData();// ��data�л�ȡandroid��content���͵�url
			try {
				String res = null;
				String[] proj = { MediaStore.Images.Media.DATA };// ʹ��ý�����в�ѯ����
				Cursor cursor = getContentResolver().query(uri, proj, null,
						null, null);// �����ѯ�α�
				if (cursor.moveToFirst()) {
					int column_index = cursor
							.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					res = cursor.getString(column_index);
				}
				cursor.close();// �ر��α�
				Uri fileUri = Uri.fromFile(new File(res));
				drawerPresenter.cropHeadImage(fileUri);
			} catch (Exception e) {
			}
		} else if (requestCode == CROP_REQUEST_CODE) {// ���ؼ��ú�Ľ��
			if (data == null)// ���dataΪnull��ֱ�ӷ���
				return;
			Bundle extras = data.getExtras();
			if (extras == null)
				return;
			Bitmap bm = extras.getParcelable("data");// ��ȡbitmap����
			headImageView.setImageBitmap(bm);
			cahceHeadImage(bm);
		}
	}

	// ����ͷ��
	public void setHeadImage() {
		drawerPresenter.setHeadImage();
	}

	// ���ػ���ͷ��
	public void loadCacheHeadImage() {
		drawerPresenter.loadCacheHeadImage(headImageView);
	}

	// ����ͼ��
	public void cahceHeadImage(Bitmap bit) {
		drawerPresenter.cahceHeadImage(bit);
	}

	// ��ȡpopupwindow����Կؼ�
	public ImageView getParentView() {
		return guideImageView;
	}
	
	//�򿪲໬�˵�
	public void openDrawerLayout(){
		drawerLayout.openDrawer(Gravity.LEFT);
	}

}