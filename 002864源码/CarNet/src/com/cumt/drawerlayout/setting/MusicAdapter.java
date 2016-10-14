package com.cumt.drawerlayout.setting;

import java.util.List;

import com.cumt.carnet.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MusicAdapter extends ArrayAdapter<MusicListItem> {
	
	private int resourceId;

	public MusicAdapter(Context context, int textViewResourceId,
			List<MusicListItem> objects) {
		super(context, textViewResourceId, objects);
		resourceId = textViewResourceId;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MusicListItem musicListItem = getItem(position);
		View view;
		if (convertView == null) {
			view = LayoutInflater.from(getContext()).inflate(resourceId, null);
		}else{
			view = convertView;
		}
		TextView musicName = (TextView) view.findViewById(R.id.music_name);
		TextView singer = (TextView) view.findViewById(R.id.singer_name);
		TextView length = (TextView) view.findViewById(R.id.music_time);
		musicName.setText(musicListItem.getMusicName());
		singer.setText(musicListItem.getMusicSinger());
		length.setText(musicListItem.getMusicLength());
		return view;
	}

}
