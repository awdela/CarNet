package com.cumt.book.module;

import android.graphics.Bitmap;

import com.cumt.book.entity.BookBean;
import com.cumt.carnet.entity.GasItemBean;

public interface IBookStatus {
	
	public BookBean getBookBean(GasItemBean gasItemBean, String oilAndPrice,String total_price);//���첢��ȡ����bean����ʵ��
	public Bitmap makeZxingOfBook(BookBean bookBean);//���ɶ����Ķ�ά��
	
}
