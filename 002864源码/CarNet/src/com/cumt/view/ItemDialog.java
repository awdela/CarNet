package com.cumt.view;

import java.util.List;

import com.cumt.carnet.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ItemDialog extends android.app.Dialog {

	Context context;
	View view;
	View backView;
	ListView listView;
	ArrayAdapter<String> adapter;

	List<String> messages;
	String title;
	TextView titleTextView;
	
	OnItemClickListener itemClickListener;

	public ItemDialog(Context context, String title, List<String> messages) {
		super(context, android.R.style.Theme_Translucent);
		this.context = context;
		this.messages = messages;
		this.title = title;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.itemdialog);

		view = (RelativeLayout) findViewById(R.id.contentDialog);
		backView = (RelativeLayout) findViewById(R.id.dialog_rootView);
		backView.setOnTouchListener(new OnTouchListener() {

			@SuppressLint("ClickableViewAccessibility")
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getX() < view.getLeft()
						|| event.getX() > view.getRight()
						|| event.getY() > view.getBottom()
						|| event.getY() < view.getTop()) {
					dismiss();
				}
				return false;
			}
		});

		this.titleTextView = (TextView) findViewById(R.id.title);
		setTitle(title);
		
		this.listView = (ListView) findViewById(R.id.list_view);
		adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, messages);
		listView.setAdapter(adapter);
		
	}
	//get title
	public String getTitle() {
		return title;
	}
	//set title
	public void setTitle(String title) {
		this.title = title;
		if(title == null)
			titleTextView.setVisibility(View.GONE);
		else{
			titleTextView.setVisibility(View.VISIBLE);
			titleTextView.setText(title);
		}
	}
	
	@Override
	public void dismiss() {
		Animation anim = AnimationUtils.loadAnimation(context,
				R.anim.dialog_main_hide_amination);
		anim.setAnimationListener(new AnimationListener() {

			public void onAnimationStart(Animation animation) {
			}

			public void onAnimationRepeat(Animation animation) {
			}

			public void onAnimationEnd(Animation animation) {
				view.post(new Runnable() {
					public void run() {
						ItemDialog.super.dismiss();
					}
				});

			}
		});
		Animation backAnim = AnimationUtils.loadAnimation(context,
				R.anim.dialog_root_hide_amin);

		view.startAnimation(anim);
		backView.startAnimation(backAnim);
	}
	
	public void setOnItemClickListener(OnItemClickListener itemClickListener){
		this.itemClickListener = itemClickListener;
		if(listView != null){
			listView.setOnItemClickListener(itemClickListener);
		}
	}
}
