package com.cumt.drawerlayout.carinfo.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cheshouye.api.client.json.WeizhangResponseHistoryJson;
import com.cumt.carnet.R;

public class WeizhangResponseAdapter extends BaseAdapter {

	private List<WeizhangResponseHistoryJson> mDate;
	private Context mContext;

	public WeizhangResponseAdapter(Context mContex,List<WeizhangResponseHistoryJson> mDate){
		this.mContext=mContex;
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
		View view = View.inflate(mContext, R.layout.csy_listitem_result, null);
		// ��ȡID
		WeizhangResponseHistoryJson model=mDate.get(position) ;

		TextView wz_time = (TextView) view.findViewById(R.id.wz_time);
		TextView wz_money = (TextView) view.findViewById(R.id.wz_money);
		TextView wz_addr = (TextView) view.findViewById(R.id.wz_addr);
		TextView wz_info = (TextView) view.findViewById(R.id.wz_info);
		// ��дֵ
		
		wz_time.setText(model.getOccur_date());
		wz_money.setText("��"+model.getFen()+"��, ��"+model.getMoney()+"Ԫ");
		wz_addr.setText(model.getOccur_area());
		wz_info.setText(model.getInfo());
		
		return view;
	}

}
