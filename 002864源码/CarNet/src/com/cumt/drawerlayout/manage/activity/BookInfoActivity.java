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
        	Toast.makeText(BookInfoActivity.this, "ɾ���ɹ�!",
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
		if (state != 1) {// ������δ���
			imageView.setImageDrawable(getResources().getDrawable(
					R.drawable.sad_face));
			if (state == 0) {
				stateText.setText("��ǰԤԼ����");
			} else if (state == 2) {
				stateText.setText("�û�ȡ������");
			} else if (state == 3) {
				stateText.setText("��ʱʧЧ����");
			}
		} else {
			stateText.setText("���������~");
		}
		timeText.setText(bookBean.getOil_time());
		gasText.setText(bookBean.getGasstation_name());// ����վ����
		carText.setText(bookBean.getCar_number());
		userText.setText(bookBean.getUsername());
		allmoneyText.setText("" + bookBean.getTotal_price());
		typeText.setText(bookBean.getOilStyle());
		priceText.setText("" + bookBean.getOil_price());
		countText.setText("" + bookBean.getCount());
		addressText.setText(bookBean.getGas_address());
	}

	private void deleteDialog(final int state) {
		Dialog dialog = new Dialog(BookInfoActivity.this, "��ʾ", "���Ƿ�Ҫɾ���ö���?",
				"ȷ��");
		dialog.addCancelButton("ȡ��");
		dialog.setOnAcceptButtonClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (state == 0) {// ��ǰ�����޷�ɾ��
					Toast.makeText(BookInfoActivity.this, "��ǰԤԼ��������ɾ��",
							Toast.LENGTH_SHORT).show();
					return;
				}
				// �����ݿ���ɾ��
				deleteFromDB(bookBean.getOil_time());
			}
		});
		dialog.setOnCancelButtonClickListener(new OnClickListener() {

			public void onClick(View v) {

			}
		});
		dialog.show();
	}

	// ����ʱ��ɾ��һ����¼
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
