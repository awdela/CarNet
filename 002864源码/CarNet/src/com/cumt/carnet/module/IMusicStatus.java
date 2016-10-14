package com.cumt.carnet.module;

import java.util.ArrayList;
import java.util.List;

import com.cumt.carnet.entity.MusicInfo;


public interface IMusicStatus {
	
	public List<String> getMusicNameList(ArrayList<MusicInfo> musicInfoList);

}
