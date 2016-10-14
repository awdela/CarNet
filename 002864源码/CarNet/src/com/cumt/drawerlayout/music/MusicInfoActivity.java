package com.cumt.drawerlayout.music;

import com.cumt.carnet.R;
import com.cumt.carnet.entity.MusicInfo;
import com.cumt.util.MusicUtils;
import com.cumt.util.SPUtils;
import com.cumt.view.ButtonRectangle;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MusicInfoActivity extends Activity {
	
	private MusicInfo musicInfo;
	//title
	private LinearLayout layout;
	private ImageButton btnReturn;
	private TextView titleText;
	private Button btn;
	//music info
	private TextView textSong,textArtist,textAlbum,textYear
						,textGenre,textLength,textSize,textFormat;
	//button
	private ButtonRectangle btnStart;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_musicinfo);
		musicInfo = (MusicInfo) this.getIntent().getParcelableExtra("MUSIC_INFO");
		initView();
		initData();
	}
	
	private void initView(){
		//title
		layout = (LinearLayout) findViewById(R.id.titleMusic);
		btnReturn = (ImageButton) findViewById(R.id.title_back);
		titleText = (TextView) findViewById(R.id.title_text);
		btn = (Button) findViewById(R.id.title_post);
		//music info
		textSong = (TextView) findViewById(R.id.song);
		textArtist = (TextView) findViewById(R.id.artist);
		textAlbum = (TextView) findViewById(R.id.album);
		textYear = (TextView) findViewById(R.id.year);
		textGenre = (TextView) findViewById(R.id.genre);
		textLength = (TextView) findViewById(R.id.music_time);
		textSize = (TextView) findViewById(R.id.size);
		textFormat = (TextView) findViewById(R.id.format);
		//button
		btnStart = (ButtonRectangle) findViewById(R.id.btnStart);
		//event
		btnReturn.setOnClickListener(new OnClickListener() {//return
			
			public void onClick(View v) {
				MusicInfoActivity.this.finish();
			}
		});
		btnStart.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				//写入用户选择的初始播放的音乐的路径你
				SPUtils.put(MusicInfoActivity.this, "MusicPath",musicInfo.data);
				Toast.makeText(MusicInfoActivity.this, "设置成功!", Toast.LENGTH_SHORT).show();
			}
		});
	}
	private void initData(){
		layout.setBackgroundColor(getResources().getColor(R.color.pink));
		btnReturn.setBackgroundColor(getResources().getColor(R.color.pink));
		btn.setBackgroundColor(getResources().getColor(R.color.pink));
		titleText.setText("音乐");
		textSong.setText(musicInfo.musicName);
		textArtist.setText(musicInfo.artist);
		textAlbum.setText(musicInfo.album);
		textYear.setText(musicInfo.year);
		textGenre.setText(musicInfo.composer);
		textLength.setText(MusicUtils.makeTimeString(musicInfo.duration));
		long bytes = Long.parseLong(musicInfo.size);
		textSize.setText(MusicUtils.bytes2kb(bytes));
		textFormat.setText(MusicUtils.getSuffOfMusicData(musicInfo.data));
	}

}
