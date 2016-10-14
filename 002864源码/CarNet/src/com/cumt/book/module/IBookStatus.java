package com.cumt.book.module;

import android.graphics.Bitmap;

import com.cumt.book.entity.BookBean;
import com.cumt.carnet.entity.GasItemBean;

public interface IBookStatus {
	
	public BookBean getBookBean(GasItemBean gasItemBean, String oilAndPrice,String total_price);//构造并获取订单bean对象实体
	public Bitmap makeZxingOfBook(BookBean bookBean);//生成订单的二维码
	
}
