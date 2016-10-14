package com.cumt.drawerlayout.manage.activity;

import com.cumt.book.entity.BookBean;
import com.cumt.carnet.R;
import com.cumt.util.NormalMethodsUtils;
import com.cumt.view.ButtonRectangle;
import com.cumt.view.Dialog;
import com.db.BookInfoDao;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class BookInfoActivity extends Activity {

	private BookBean bookBean;

	private ImageView imageView;
	private TextView gasText, timeText, stateText, carText, userText,
			allmoneyText, typeText, priceText, countText, addressText;

	private ButtonRectangle btnCancle;
	
	@SuppressLint("HandlerLeak")
	Handler myHandler = new Handler() {  
        public void handleMessage(Message msg) {   
        	Toast.makeText(BookInfoActivity.this, "删除成功!",
					Toast.LENGTH_SHORT).show();
        	NormalMethodsUtils.goNextActivity(BookInfoActivity.this
        			, BookManageActivity.class, BookInfoActivity.this);
			BookInfoActivity.this.finish();
            super.handleMessage(msg);   
        }   
   };  

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_orderinfo);

		bookBean = (BookBean) this.getIntent()
				.getSerializableExtra("BOOK_INFO");

		initView();
		initData();

	}

	private void initView() {
		imageView = (ImageView) findViewById(R.id.order_bg);
		timeText = (TextView) findViewById(R.id.order_time);
		gasText = (TextView) findViewById(R.id.oil_station);
		stateText = (TextView) findViewById(R.id.order_state);
		carText = (TextView) findViewById(R.id.car_number);
		userText = (TextView) findViewById(R.id.user_name);
		allmoneyText = (TextView) findViewById(R.id.order_cost);
		typeText = (TextView) findViewById(R.id.oil_style);
		priceText = (TextView) findViewById(R.id.oil_price);
		countText = (TextView) findViewById(R.id.oil_volume);
		addressText = (TextView) findViewById(R.id.oil_station_position);
		btnCancle = (ButtonRectangle) findViewById(R.id.cancel_order);
		btnCancle.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				int state = bookBean.getGas_state();
				deleteDialog(state);
			}
		});
	}

	private void initData() {
		int state = bookBean.getGas_state();
		if (state != 1) {// 若订单未完成
			imageView.setImageDrawable(getResources().getDrawable(
					R.drawable.sad_face));
			if (state == 0) {
				stateText.setText("当前预约订单");
			} else if (state == 2) {
				stateText.setText("用户取消订单");
			} else if (state == 3) {
				stateText.setText("超时失效订单");
			}
		} else {
			stateText.setText("订单完成啦~");
		}
		timeText.setText(bookBean.getOil_time());
		gasText.setText(bookBean.getGasstation_name());// 加油站名称
		carText.setText(bookBean.getCar_number());
		userText.setText(bookBean.getUsername());
		allmoneyText.setText("" + bookBean.getTotal_price());
		typeText.setText(bookBean.getOilStyle());
		priceText.setText("" + bookBean.getOil_price());
		countText.setText("" + bookBean.getCount());
		addressText.setText(bookBean.getGas_address());
	}

	private void deleteDialog(final int state) {
		Dialog dialog = new Dialog(BookInfoActivity.this, "提示", "您是否要删除该订单?",
				"确定");
		dialog.addCancelButton("取消");
		dialog.setOnAcceptButtonClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (state == 0) {// 当前订单无法删除
					Toast.makeText(BookInfoActivity.this, "当前预约订单不能删除",
							Toast.LENGTH_SHORT).show();
					return;
				}
				// 从数据库中删除
				deleteFromDB(bookBean.getOil_time());
			}
		});
		dialog.setOnCancelButtonClickListener(new OnClickListener() {

			public void onClick(View v) {

			}
		});
		dialog.show();
	}

	// 根据时间删除一条记录
	private void deleteFromDB(final String time) {
		new Thread(new Runnable() {

			public void run() {
				BookInfoDao bookInfoDao = new BookInfoDao(BookInfoActivity.this);
				bookInfoDao.deleteBookItem(time);
				Message msg = myHandler.obtainMessage();
				myHandler.sendMessage(msg);
			}
		}).start();
	}
}
