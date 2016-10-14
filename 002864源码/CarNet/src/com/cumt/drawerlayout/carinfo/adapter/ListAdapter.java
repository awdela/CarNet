package com.cumt.drawerlayout.carinfo.adapter;

import java.util.List;

import com.cumt.carnet.R;
import com.cumt.drawerlayout.carinfo.model.ListModel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListAdapter extends BaseAdapter {
	
	private  List<ListModel> mDate;
	private Context mContext;

	public ListAdapter( Context mContext,List<ListModel> mDate){
		this.mContext=mContext;
		this.mDate=mDate;		
	}
	

	
	public int getCount() {
		return mDate.size();
	}

	
	public Object getItem(int position) {
		return mDate.get(position);
	}

	
	public long getItemId(int position) {
		return position;
	}

	
	@SuppressLint("ViewHolder")
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View view = View.inflate(mContext, R.layout.csy_listitem_citys, null);
		
		//初始化
		ListModel model=mDate.get(position) ;
		TextView txt_name =(TextView) view.findViewById(R.id.txt_name);
		//ImageView image=(ImageView)view.findViewById(R.id.iv_1);
		

		//绑定数据
		txt_name.setText(model.getTextName());
		txt_name.setTag(model.getNameId());
		//返回
		return view;
	}

}
