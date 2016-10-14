package com.cumt.drawerlayout.setting;

import java.util.List;

import org.apache.http.NameValuePair;

import com.cumt.carnet.R;
import com.cumt.util.HttpUtils;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class FeedBackActivity extends Activity{
	
	public static final String FEED_BACK_URL = "http://115.159.205.135/carnet/feedback";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
		initView();
	}
	
	private void initView(){
		findViewById(R.id.send).setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				EditText feedText = (EditText) findViewById(R.id.feed_back_text);
				String data = feedText.getText().toString();
				sendData(data);
				Toast.makeText(FeedBackActivity.this, "·´À¡³É¹¦", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	private void sendData(final String data){
		new Thread(){
			public void run() {
				List<NameValuePair> params = HttpUtils.paramsOfFeedBackMsg(data);
				try {
					HttpUtils.requestByHttpPost(params, FEED_BACK_URL);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			};
		}.start();
	}

}
