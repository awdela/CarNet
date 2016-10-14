package com.cumt.drawerlayout.music;

import java.util.ArrayList;
import java.util.List;
import com.app.CarnetApplication;
import com.cumt.carnet.R;
import com.cumt.carnet.entity.MusicInfo;
import com.cumt.drawerlayout.setting.MusicAdapter;
import com.cumt.drawerlayout.setting.MusicListItem;
import com.cumt.util.MusicUtils;
import com.cumt.util.NormalMethodsUtils;
import com.cumt.view.Dialog;
import com.cumt.view.ProgressWheel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.Media;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class MuiscManageActivity extends Activity implements
		OnItemClickListener, OnItemLongClickListener {

	private ListView musicList;
	private ArrayList<MusicInfo> musicInfoList;
	private List<MusicListItem> list;
	private MusicAdapter adapter;
	private ImageView searchView;
	private ImageButton btnReturn;
	private ProgressWheel progress;
	
	public static final String MUSIC_FINISH_BROADCAST = "com.cumt.carnet.MUSIC_FINISH";

	@SuppressLint("HandlerLeak")
	private Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				list = getMusicItemList(musicInfoList);
				adapter.notifyDataSetChanged();
				progress.setVisibility(View.GONE);
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_musicmanage);
		initView();
		initData();
	}

	private void initView() {
		LinearLayout title = (LinearLayout) findViewById(R.id.music_title);
		title.setBackgroundColor(getResources().getColor(R.color.pink));
		progress = (ProgressWheel) findViewById(R.id.progress_wheel);
		progress.setVisibility(View.GONE);
		musicList = (ListView) findViewById(R.id.list_music);
		musicList.setOnItemClickListener(this);
		musicList.setOnItemLongClickListener(this);
		searchView = (ImageView) findViewById(R.id.title_search);
		btnReturn = (ImageButton) findViewById(R.id.title_back);
		btnReturn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				MuiscManageActivity.this.finish();
			}
		});
		searchView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				progress.setVisibility(View.VISIBLE);
				startSearch(MuiscManageActivity.this);//开启搜索
			}
		});
	}

	private void initData() {
		CarnetApplication carApp = CarnetApplication.getInstance();
		musicInfoList = carApp.getMusicInfoList();
		list = getMusicItemList(musicInfoList);
		adapter = new MusicAdapter(this, R.layout.music_item, list);
		musicList.setAdapter(adapter);
	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(MuiscManageActivity.this, MusicInfoActivity.class);
		Bundle bundle = new Bundle();
		bundle.putParcelable("MUSIC_INFO", musicInfoList.get(position));
		intent.putExtras(bundle);
		startActivity(intent);
	}
	
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		String filePath = musicInfoList.get(position).data;
		showDeleteDialog(filePath,position);
		return true;
	}
	

	// 检索音乐
	private void startSearch(final Context context) {

		new Thread(new Runnable() {

			public void run() {
				Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;// -------------
				ContentResolver cr = context.getContentResolver();
				StringBuffer select = new StringBuffer(" 1=1 ");
				// 查询语句：检索出时长大于1分钟，文件大小大于1MB的媒体文件
				select.append(" and " + Media.SIZE + " > "
						+ MusicUtils.FILTER_SIZE);
				select.append(" and " + Media.DURATION + " > "
						+ MusicUtils.FILTER_DURATION);

				musicInfoList = MusicUtils.getMusicList(cr.query(uri,
						MusicUtils.proj_music, select.toString(), null,
						MediaStore.Audio.Media.ARTIST_KEY));
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				// 检索玩成发送信息
				Message msg = myHandler.obtainMessage();
				msg.what = 1;
				myHandler.sendMessage(msg);
			}
		}).start();
	}
	
	private void showDeleteDialog(final String filePath,final int position){
		final Dialog dialog = new Dialog(
				MuiscManageActivity.this,"提示",
				"您是否要删除该音乐?","确定");
		dialog.addCancelButton("取消");
		
		dialog.setOnAcceptButtonClickListener(new OnClickListener() {

			public void onClick(View v) {
				boolean d = NormalMethodsUtils.deleteFile(filePath);
				String msg = "删除成功!";
				if(!d){
					msg = "删除失败!";
				}
				//更新媒体库
				list.remove(position);
				adapter.notifyDataSetChanged();
				CarnetApplication carApp = CarnetApplication.getInstance();
				carApp.refreshMedia();
				Toast.makeText(MuiscManageActivity.this, msg, Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(MUSIC_FINISH_BROADCAST);
				intent.putExtra("REFRESH_MODE", position);
				sendBroadcast(intent);
			}
		});
		dialog.setOnCancelButtonClickListener(new OnClickListener() {

			public void onClick(View v) {
				
			}
		});
		dialog.show();
	}
	//构造适配器所需的list
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

}
