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
 * ����:DrawerListListener
 * ����:DrawerListListener�ǲ໬�˵���listview����¼��Ļص������ڴ���listview�еĵ���¼�
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
			//��ת����ά��ʶ�����
			NormalMethodsUtils.goNextActivity(context, UserPersonalActivity.class, activity);
			//�ȹرղ໬�˵�
			drawerLayout.closeDrawers();
			break;
		case 1:
			//��ת����ά��ʶ�����
			NormalMethodsUtils.goNextActivity(context, ZcodeActivity.class, activity);
			//�ȹرղ໬�˵�
			drawerLayout.closeDrawers();
			break;
		case 2:
			break;
		case 3:
			//��ת����ά��ʶ�����
			NormalMethodsUtils.goNextActivity(context, CarInfoActivity.class, activity);
			//�رղ໬�˵�
			drawerLayout.closeDrawers();
			break;
		case 4:
			//��ת����ά��ʶ�����
			NormalMethodsUtils.goNextActivity(context, BookManageActivity.class, activity);
			//�رղ໬�˵�
			drawerLayout.closeDrawers();
			break;
		case 5:
			NormalMethodsUtils.goNextActivity(context, MuiscManageActivity.class, activity);
			//�رղ໬�˵�
			drawerLayout.closeDrawers();
			break;
		case 6:
			//��ת����ά��ʶ�����
			NormalMethodsUtils.goNextActivity(context, SettingActivity.class, activity);
			//�رղ໬�˵�
			drawerLayout.closeDrawers();
			break;
		default:
			break;
		}

	}

}
