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
		
		//Ҫע���spinner�л�����ݸ�ʽ��oilType:price,��������
		String[] oilTypeAndPrice = oilAndPrice.split(":");
		BookBean bookBean = new BookBean(gasItemBean, oilTypeAndPrice[0],
				Double.valueOf(oilTypeAndPrice[1]), Double.valueOf(total_price));
		
		return bookBean;
	}

	public Bitmap makeZxingOfBook(BookBean bookBean) {
		//�������ɵĶ�ά���ʽ
		String content = bookBean.toString();
		try {
			saveBookInfoNet(bookBean);//�ϴ������
			Bitmap qrcodeBitmap = EncodingHandler.createQRCode(content, 300);
			return qrcodeBitmap;
		} catch (WriterException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * ��������Ϣ���浽���������ݿ�
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
