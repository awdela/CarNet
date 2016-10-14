package com.cumt.carnet.presenter;

import com.cumt.book.entity.BookBean;

import android.app.Fragment;
import android.widget.ImageView;
import android.widget.TextView;

public interface IFragmentPresenter {
	public void setDefaultFragment();//����Ĭ�ϵ�frament
	public void switchContent(Fragment from, Fragment to);//Fragment��ת
	public void startGetGasMessageThread();//������ȡ����վ��Ϣ���߳�
	public void refreshBaiMapFragmentWithGas();//���°ٶȵ�ͼUI����Ӽ���վ��Ϣ
	public void changeActionBarColor(ImageView imageView,TextView textView);//�ı��Ӧ��fragment�ײ����ؼ���ɫ״̬
	public void setInitActionbarColor();//���ó�ʼʱ��Ӧ��fragment�ײ����ؼ���ɫ״̬
	public void showBookOfBookFragment(BookBean bookBean);//չʾbookFragment�еĵ�ǰ������Ϣ
	public ImageView getParentView();
}
