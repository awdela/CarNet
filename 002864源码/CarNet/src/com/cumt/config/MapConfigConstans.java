package com.cumt.config;

import com.baidu.mapapi.map.BaiduMap;
/**
 * ���ڱ������ý���͵�ͼ���ó�ʼ��ʱ����Ҫ�ĳ���
 * @author wangcan
 *
 */
public class MapConfigConstans {
	public static final double INIT_MAP_LAT = 34.21621d;//���ݿ��;//��ͼ��ʼ����λ��
	public static final double INIT_MAP_LNT = 117.14711d;
	public static final int INIT_MAP_SIZE = 18;//��ʼ��ͼ���Ŵ�С
	public static final int INIT_MAP_TYPE = BaiduMap.MAP_TYPE_NORMAL;//��ʼʱ��ͼ����
	public static final boolean INIT_ZOOM_CONTROL = false;//��ʼʱ�Ƿ�����ſؼ�
	public static final boolean INIT_SCALE_CONTROL = false;//��ʼʱ�Ƿ��������
	public static final String CONFIG_TABLE_NAME = "MAP_CONFIG";//���ڴ洢�û����õ���Ϣ�ı���
	public static final String MAP_SIZE = "MAP_SIZE";//��ͼ��С�ֶ���
	public static final String MAP_TYPE = "MAP_TYPE";//��ͼ�����ֶ���
	public static final String MAP_LAT = "MAP_LAT";//��ͼ��ʼ�����ֶ���
	public static final String MAP_LNT = "MAP_LNT";//��ͼ��ʼ�����ֶ���
	public static final String MAP_WITH_ZOOM ="MAP_ZOOM";//��ͼ�Ƿ�������ֶ���
	public static final String MAP_WITH_SCALE = "MAP_SCALE";//��ͼ�Ƿ���������ֶ���
	//��ʱ�����û�λ�õı����ֶ���
	public static final String POSITON_TABLE_NAME = "USER_POSITION";//����
	public static final String POSITION_LAT = "MAP_LAT";//γ��
	public static final String POSITION_LNT = "MAP_LNT";//����
	public static final String POSITION_NAME = "MAP_POSITION";//��ϸ�ص�
}
