package com.cumt.carnet.module;


import com.cumt.drawerlayout.carinfo.CarInfoActivity;
import com.cumt.drawerlayout.manage.activity.BookManageActivity;
import com.cumt.drawerlayout.music.MuiscManageActivity;
import com.cumt.drawerlayout.personal.UserPersonalActivity;
import com.cumt.drawerlayout.setting.SettingActivity;
import com.cumt.util.NormalMethodsUtils;
import com.cumt.zxing.view.ZcodeActivity;

import android.app.Activity;
import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
/**
 * 类名:DrawerListListener
 * 作用:DrawerListListener是侧滑菜单中listview点击事件的回调，用于处理listview中的点击事件
 * @author wangcan
 *
 */
public class DrawerListListener implements OnItemClickListener {
	
	private Context context;
	private Activity activity;
	private DrawerLayout drawerLayout;
	
	public DrawerListListener(Context context, Activity activity,DrawerLayout drawerLayout) {
		super();
		this.context = context;
		this.activity = activity;
		this.drawerLayout = drawerLayout;
	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		switch(position){
		case 0:
			//跳转到二维码识别界面
			NormalMethodsUtils.goNextActivity(context, UserPersonalActivity.class, activity);
			//先关闭侧滑菜单
			drawerLayout.closeDrawers();
			break;
		case 1:
			//跳转到二维码识别界面
			NormalMethodsUtils.goNextActivity(context, ZcodeActivity.class, activity);
			//先关闭侧滑菜单
			drawerLayout.closeDrawers();
			break;
		case 2:
			break;
		case 3:
			//跳转到二维码识别界面
			NormalMethodsUtils.goNextActivity(context, CarInfoActivity.class, activity);
			//关闭侧滑菜单
			drawerLayout.closeDrawers();
			break;
		case 4:
			//跳转到二维码识别界面
			NormalMethodsUtils.goNextActivity(context, BookManageActivity.class, activity);
			//关闭侧滑菜单
			drawerLayout.closeDrawers();
			break;
		case 5:
			NormalMethodsUtils.goNextActivity(context, MuiscManageActivity.class, activity);
			//关闭侧滑菜单
			drawerLayout.closeDrawers();
			break;
		case 6:
			//跳转到二维码识别界面
			NormalMethodsUtils.goNextActivity(context, SettingActivity.class, activity);
			//关闭侧滑菜单
			drawerLayout.closeDrawers();
			break;
		default:
			break;
		}

	}

}
