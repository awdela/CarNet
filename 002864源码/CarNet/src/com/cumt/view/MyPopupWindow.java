package com.cumt.view;

import com.cumt.carnet.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
/**
 * ������ MyPopupWindow
 * ����:�Զ���popupwindow,����ʵ�ִ����浯��ѡ�񵼺�����
 * @author wangcan
 *
 */
public class MyPopupWindow extends PopupWindow implements OnClickListener {
	
	private View contentView = null;
	private View parent;
	private ListView listView;
	
	@SuppressLint("InflateParams")
	public MyPopupWindow(final Activity activity,final View parent,int resourceId,ArrayAdapter<String> adapter){
		this.parent = parent;
		if(contentView == null){
			LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			contentView = layoutInflater.inflate(resourceId, null);
			listView = (ListView) contentView.findViewById(R.id.music_list);
			listView.setAdapter(adapter);
		}
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		this.setContentView(contentView);
		this.setWidth(dm.widthPixels);
		this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
		this.setAnimationStyle(R.style.PopupAnimation);//���ö���Ч��
		this.setFocusable(false);
		this.setOutsideTouchable(false);
		this.setBackgroundDrawable(new BitmapDrawable());
		this.setOnDismissListener(new OnDismissListener() {
			
			public void onDismiss() {
				
//				MyPopupWindow.this.setAnimationStyle(R.style.PopupAnimation2);
			}
			
		});
		initView();
	}
	
	//��ʾpopupwindow:��������ʾ��ر�
	public void showPopupWindow(){
		if(!this.isShowing()){
			this.showAsDropDown(parent,0,0);
		}
		else{
			this.dismiss();
		}
	}
	//ʵ��popupwindow��imageview�ĵ���¼�����
	private void initView(){
	}
	public void onClick(View v) {
		switch(v.getId()){
		}
	}
	
	public View getView(){
		return contentView;
	}
	
	public ListView getListView(){
		return listView;
	}
}