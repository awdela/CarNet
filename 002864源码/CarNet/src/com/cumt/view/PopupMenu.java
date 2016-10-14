package com.cumt.view;


import com.cumt.carnet.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

public class PopupMenu extends PopupWindow implements OnClickListener {

	private Activity activity;
	private View popView;

	private View v_item1;
	private View v_item2;
	private View v_item3;

	private OnItemClickListener onItemClickListener;

	/**
	 *
	 * @author
	 */
	public enum MENUITEM {
		ITEM1, ITEM2, ITEM3
	}

	private String[]  tabs;
	

	public PopupMenu(Activity activity ,String[] tabs) {
		super(activity);
		this.activity = activity;
		this.tabs = tabs;
		LayoutInflater inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		popView = inflater.inflate(R.layout.popup_menu, null);
		this.setContentView(popView);
		this.setWidth(dip2px(activity, 120));
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setFocusable(true);
		this.setTouchable(true); 
		this.setOutsideTouchable(true); 
		ColorDrawable dw = new ColorDrawable(0x00000000);
		this.setBackgroundDrawable(dw);

		v_item1 = popView.findViewById(R.id.ly_item1);
		v_item2 = popView.findViewById(R.id.ly_item2);
		v_item3 = popView.findViewById(R.id.ly_item3);
		
		v_item1.setOnClickListener(this);
		v_item2.setOnClickListener(this);
		v_item3.setOnClickListener(this);

	}


	/**
	 * 传入控件ID,相对于控件下面显示
	 * @param resourId
	 */
	public void showLocation(int resourId) {
		showAsDropDown(activity.findViewById(resourId), dip2px(activity, 0),
				dip2px(activity, -8));
	}

	public void onClick(View v) {
		MENUITEM menuitem = null;
		String str = "";
		int position = 0;
		if (v == v_item1) {
			menuitem = MENUITEM.ITEM1;
			str = tabs[0];
			position = 0;
			TextView tvAboutUs = (TextView)v_item1.findViewById(R.id.tv_about_us);
			tvAboutUs.setText(str);
		} else if (v == v_item2) {
			menuitem = MENUITEM.ITEM2;
			str = tabs[1];
			position = 1;
			TextView tvAboutUs = (TextView)v_item2.findViewById(R.id.tv_check_update);
			tvAboutUs.setText(str);
		} else if (v == v_item3) {
			menuitem = MENUITEM.ITEM3;
			str = tabs[2];
			position = 2;
			TextView tvAboutUs = (TextView)v_item3.findViewById(R.id.tv_feedback);
			tvAboutUs.setText(str);
		}
		if (onItemClickListener != null) {
			onItemClickListener.onClick(menuitem, str,position);
		}
		dismiss();
	}

	public int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	public interface OnItemClickListener {
		void onClick(MENUITEM item, String str,int position);
	}

	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}
}
