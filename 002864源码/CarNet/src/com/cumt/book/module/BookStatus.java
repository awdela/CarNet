package com.cumt.book.module;

import java.util.List;
import org.apache.http.NameValuePair;
import android.graphics.Bitmap;
import com.cumt.book.entity.BookBean;
import com.cumt.carnet.entity.GasItemBean;
import com.cumt.util.HttpUtils;
import com.google.zxing.WriterException;
import com.zxing.encoding.EncodingHandler;

public class BookStatus implements IBookStatus{
	
	public static final String BOOK_URL = "";

	public BookBean getBookBean(GasItemBean gasItemBean, String oilAndPrice,String total_price) {
		
		//要注意从spinner中获得数据格式是oilType:price,否则会出错
		String[] oilTypeAndPrice = oilAndPrice.split(":");
		BookBean bookBean = new BookBean(gasItemBean, oilTypeAndPrice[0],
				Double.valueOf(oilTypeAndPrice[1]), Double.valueOf(total_price));
		
		return bookBean;
	}

	public Bitmap makeZxingOfBook(BookBean bookBean) {
		//构造生成的二维码格式
		String content = bookBean.toString();
		try {
			saveBookInfoNet(bookBean);//上传服务端
			Bitmap qrcodeBitmap = EncodingHandler.createQRCode(content, 300);
			return qrcodeBitmap;
		} catch (WriterException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 将订单信息保存到服务器数据库
	 * @param bookBean
	 */
	public void saveBookInfoNet(final BookBean bookBean){
		new Thread(){
			public void run() {
				List<NameValuePair> params = HttpUtils.paramsOfBookMsg(bookBean);
				try {
					HttpUtils.requestByHttpPost(params, BOOK_URL);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			};
		}.start();
	}
}
