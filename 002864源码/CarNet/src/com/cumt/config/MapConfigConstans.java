package com.cumt.config;

import com.baidu.mapapi.map.BaiduMap;
/**
 * 用于保存设置界面和地图设置初始化时所需要的常量
 * @author wangcan
 *
 */
public class MapConfigConstans {
	public static final double INIT_MAP_LAT = 34.21621d;//徐州矿大;//地图初始中心位置
	public static final double INIT_MAP_LNT = 117.14711d;
	public static final int INIT_MAP_SIZE = 18;//开始地图缩放大小
	public static final int INIT_MAP_TYPE = BaiduMap.MAP_TYPE_NORMAL;//开始时地图类型
	public static final boolean INIT_ZOOM_CONTROL = false;//开始时是否带缩放控件
	public static final boolean INIT_SCALE_CONTROL = false;//开始时是否带比例尺
	public static final String CONFIG_TABLE_NAME = "MAP_CONFIG";//用于存储用户设置的信息的表名
	public static final String MAP_SIZE = "MAP_SIZE";//地图大小字段名
	public static final String MAP_TYPE = "MAP_TYPE";//地图类型字段名
	public static final String MAP_LAT = "MAP_LAT";//地图初始中心字段名
	public static final String MAP_LNT = "MAP_LNT";//地图初始中心字段名
	public static final String MAP_WITH_ZOOM ="MAP_ZOOM";//地图是否带缩放字段名
	public static final String MAP_WITH_SCALE = "MAP_SCALE";//地图是否带比例尺字段名
	//临时保存用户位置的表及其字段名
	public static final String POSITON_TABLE_NAME = "USER_POSITION";//表名
	public static final String POSITION_LAT = "MAP_LAT";//纬度
	public static final String POSITION_LNT = "MAP_LNT";//经度
	public static final String POSITION_NAME = "MAP_POSITION";//详细地点
}
