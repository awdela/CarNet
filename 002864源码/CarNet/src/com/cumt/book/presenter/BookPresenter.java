package com.cumt.book.presenter;

import com.cumt.book.entity.BookBean;
import com.db.BookInfoDao;

import android.content.Context;

public class BookPresenter {
	
	private Context context;
	
	public BookPresenter(Context context){
		this.context = context;
	}
	
	
	public void saveBoogInfoAndUpload(BookBean bookBean){
		BookInfoDao bookInfoDao = new BookInfoDao(context);
		bookInfoDao.saveBookInfoBean(bookBean);
		
		//------------------------------上传到服务端------------------------------
	}

}
