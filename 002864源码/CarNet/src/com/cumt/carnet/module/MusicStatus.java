package com.cumt.carnet.module;

import java.util.ArrayList;
import java.util.List;

import com.cumt.carnet.entity.MusicInfo;

public class MusicStatus implements IMusicStatus{
	

	public List<String> getMusicNameList(ArrayList<MusicInfo> musicInfoList) {
		
		List<String> list = new ArrayList<String>();
//		list.add("�й�������");
//		list.add("������˵Ĵ�");
//		list.add("�󺣵ĸ�");
//		list.add("û�й�������û�����й�");
//		list.add("�й�������");
//		list.add("������˵Ĵ�");
//		list.add("�󺣵ĸ�");
//		list.add("û�й�������û�����й�");
		for(MusicInfo musicInfo:musicInfoList){
			list.add(musicInfo.musicName);
		}
		
		return list;
	}
	
	
	
}
