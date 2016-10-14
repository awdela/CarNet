package com.cumt.drawerlayout.manage.activity;

import java.util.ArrayList;
import java.util.List;

import com.cumt.carnet.R;
import com.cumt.view.PagerSlidingTabStrip;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

public class BookManageActivity extends FragmentActivity {
	
	private ViewPager viewpager;
	private PagerSlidingTabStrip tabs;

	private Fragment f1, f2, f3;
	private List<Fragment> pages = new ArrayList<Fragment>();
	
	private ImageButton btnReturn;
	private TextView titleText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_bookmanage);
		initView();
		// ��ʼ��
		initPagers();
	}
	private void initView(){
		btnReturn = (ImageButton) findViewById(R.id.title_back);
		btnReturn.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				BookManageActivity.this.finish();//����
			}
		});
		titleText = (TextView) findViewById(R.id.title_text);
		titleText.setText("��������");
	}
	private void initPagers() {

		viewpager = (ViewPager) findViewById(R.id.viewpager);

		if (f1 == null) {
			f1 = new Fragment1();
		}
		if (f2 == null) {
			f2 = new Fragment2();
		}
		if (f3 == null) {
			f3 = new Fragment3();
		}
		pages.add(f1);
		pages.add(f2);
		pages.add(f3);
		viewpager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(),
				pages));
		// ��ʼ�� Ĭ����ʾ�ĸ�
		viewpager.setCurrentItem(0);
		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		tabs.setViewPager(viewpager);
	}

	// MyPagerAdapterҪ������ʵ�ֵ�����Fragment��Ӧ����
	class MyPagerAdapter extends FragmentPagerAdapter {

		private final String[] TITLES = { "���ж���", "����ɶ���", "δ��ɶ���" };
		private List<Fragment> fragmentlist;

		public MyPagerAdapter(FragmentManager fm, List<Fragment> list) {
			super(fm);
			fragmentlist = list;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return TITLES[position];
		}

		@Override
		public int getCount() {
			return fragmentlist.size();
		}

		public Fragment getItem(int position) {
			return fragmentlist.get(position);
		}
	}
}
