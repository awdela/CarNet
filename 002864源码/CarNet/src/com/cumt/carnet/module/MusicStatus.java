package com.cumt.carnet.module;

import java.util.ArrayList;
import java.util.List;

import com.cumt.carnet.entity.MusicInfo;

public class MusicStatus implements IMusicStatus{
	

	public List<String> getMusicNameList(ArrayList<MusicInfo> musicInfoList) {
		
		List<String> list = new ArrayList<String>();
//		list.add("中国好声音");
//		list.add("飞向别人的床");
//		list.add("大海的歌");
//		list.add("没有共产党就没有新中国");
//		list.add("中国好声音");
//		list.add("飞向别人的床");
//		list.add("大海的歌");
//		list.add("没有共产党就没有新中国");
		for(MusicInfo musicInfo:musicInfoList){
			list.add(musicInfo.musicName);
		}
		
		return list;
	}
	
	
	
}
