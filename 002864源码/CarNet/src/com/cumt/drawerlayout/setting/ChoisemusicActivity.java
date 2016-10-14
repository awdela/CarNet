package com.cumt.drawerlayout.setting;

import java.util.ArrayList;
import java.util.List;

import com.app.CarnetApplication;
import com.cumt.carnet.R;
import com.cumt.carnet.entity.MusicInfo;
import com.cumt.util.MusicUtils;
import com.cumt.util.SPUtils;
import com.cumt.view.ButtonRectangle;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ChoisemusicActivity extends Activity implements OnItemClickListener{
	
	private ListView listView;
	private ButtonRectangle btnReturn;
	private ArrayList<MusicInfo> musicInfoList;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_musicchoise);
		initView();
		initData();
	}
	
	private void initView(){
		listView = (ListView) findViewById(R.id.listview_choisemusic);
		listView.setOnItemClickListener(this);
		btnReturn = (ButtonRectangle) findViewById(R.id.return_button);
		btnReturn.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				ChoisemusicActivity.this.finish();
			}
		});
	}
	
	private void initData(){
		CarnetApplication carApp = CarnetApplication.getInstance();
		musicInfoList = carApp.getMusicInfoList();
		
		List<MusicListItem> list = getMusicItemList(musicInfoList);
		MusicAdapter adapter = new MusicAdapter(this, R.layout.music_item,list);
		listView.setAdapter(adapter);
	}
	
	private List<MusicListItem> getMusicItemList(ArrayList<MusicInfo> musicInfoList){
		List<MusicListItem> list = new ArrayList<MusicListItem>();
		for(MusicInfo musicInfo:musicInfoList){
			MusicListItem musiItem = new MusicListItem();
			musiItem.setMusicName(musicInfo.musicName);
			musiItem.setMusicSinger(musicInfo.artist);
			musiItem.setMusicLength(MusicUtils.makeTimeString(musicInfo.duration));
			list.add(musiItem);
		}
		return list;
	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		SPUtils.put(ChoisemusicActivity.this, "IfPlayMusic", true);
		//写入用户选择的初始播放的音乐的路径你
		SPUtils.put(ChoisemusicActivity.this, "MusicPath",musicInfoList.get(position).data);
		Log.i("MusicPath",musicInfoList.get(position).data);
		Toast.makeText(ChoisemusicActivity.this, "设置成功!", Toast.LENGTH_SHORT).show();
	}
}
