package com.cumt.drawerlayout.setting;

import com.cumt.carnet.R;
import com.cumt.login.view.LoginActivity;
import com.cumt.util.NormalMethodsUtils;
import com.cumt.util.SPUtils;
import com.cumt.view.ButtonRectangle;
import com.cumt.view.PopupMenu;
import com.cumt.view.PopupMenu.MENUITEM;
import com.cumt.view.PopupMenu.OnItemClickListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SettingActivity extends Activity implements OnClickListener, OnItemClickListener{
	
	private PopupMenu popupMenu;
	
	private ButtonRectangle btnQuit,btnChangePass;
	
	private RelativeLayout layoutMapType,layoutPlayMusic,layoutChoise
							,layoutMsg,layoutCheckNew,layoutAdvice;
	
	private CheckBox playCheck,acceptCheck;
	//title
	private ImageButton btnReturn;
	private TextView textTitle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_setting);
		initView();
	}
	
	
	private void initView(){
		String[] mapType = new String[]{"基础地图","卫星地图","热力地图"};
		popupMenu = new PopupMenu(this, mapType);
		popupMenu.setOnItemClickListener(this);
		//textview
		textTitle = (TextView) findViewById(R.id.title_text);
		//button
		btnQuit = (ButtonRectangle) findViewById(R.id.quit);
		btnQuit.setOnClickListener(this);
		btnChangePass = (ButtonRectangle) findViewById(R.id.change_password);
		btnChangePass.setOnClickListener(this);
		btnReturn = (ImageButton) findViewById(R.id.title_back);
		//checkbox
		playCheck = (CheckBox) findViewById(R.id.auto_music_on);
		acceptCheck = (CheckBox) findViewById(R.id.acpt_message);
		//layout
		layoutMapType = (RelativeLayout) findViewById(R.id.choise_map_type);
		layoutPlayMusic = (RelativeLayout) findViewById(R.id.play_when_open);
		layoutChoise = (RelativeLayout) findViewById(R.id.choise_play_music);
		layoutMsg = (RelativeLayout) findViewById(R.id.accept_system_msg);
		layoutCheckNew = (RelativeLayout) findViewById(R.id.check_new_version);
		layoutAdvice = (RelativeLayout) findViewById(R.id.give_user_advice);
		//event
		btnReturn.setOnClickListener(this);
		layoutMapType.setOnClickListener(this);
		layoutPlayMusic.setOnClickListener(this);
		layoutChoise.setOnClickListener(this);
		layoutMsg.setOnClickListener(this);
		layoutCheckNew.setOnClickListener(this);
		layoutAdvice.setOnClickListener(this);
		//textview
		textTitle.setText("设置");
	}

	public void onClick(MENUITEM item, String str,int position) {
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
		//记录用户设置的地图类型
		SPUtils.put(SettingActivity.this, "Maptype", position);
	}


	public void onClick(View v) {
		switch(v.getId()){
		case R.id.choise_map_type:
			popupMenu.showLocation(R.id.more_pri_maps);
			break;
		case R.id.play_when_open:
			if(playCheck.isChecked()){
				playCheck.setChecked(false);
				SPUtils.put(SettingActivity.this, "IfPlayMusic", false);
				layoutChoise.setVisibility(View.GONE);
				
			}else{
				playCheck.setChecked(true);
				SPUtils.put(SettingActivity.this, "IfPlayMusic", true);
				layoutChoise.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.choise_play_music:
			NormalMethodsUtils.goNextActivity(SettingActivity.this, 
					ChoisemusicActivity.class, SettingActivity.this);
			break;
		case R.id.accept_system_msg:
			if(acceptCheck.isChecked()){
				acceptCheck.setChecked(false);
				SPUtils.put(SettingActivity.this, "IfAcceptMess", false);
			}else{
				acceptCheck.setChecked(true);
				SPUtils.put(SettingActivity.this, "IfAcceptMess", true);
			}
			break;
		case R.id.check_new_version:
			Toast.makeText(SettingActivity.this, "暂无更新", Toast.LENGTH_SHORT).show();
			break;
		case R.id.give_user_advice:
			Intent intent = new Intent(SettingActivity.this,FeedBackActivity.class);
			startActivity(intent);
			break;
		case R.id.change_password:
			NormalMethodsUtils.goNextActivity(SettingActivity.this, 
					ChangePassActivity.class, SettingActivity.this);
			break;
		case R.id.quit:
			NormalMethodsUtils.goNextActivity(SettingActivity.this, 
					LoginActivity.class, SettingActivity.this);
			break;
		case R.id.title_back:
			SettingActivity.this.finish();
			break;
		}
	}
}
