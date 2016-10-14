package com.cumt.drawerlayout.manage.adapter;

import java.util.List;

import com.cumt.book.entity.BookBean;
import com.cumt.carnet.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class BookItemAdapter extends ArrayAdapter<BookBean> {
	
	private int resourceId;
	private String[] state = new String[]{"当前预约订单","已完成","用户取消","超时失效"};

	public BookItemAdapter(Context context, int textViewResourceId,
			List<BookBean> objects) {
		super(context, textViewResourceId, objects);
		this.resourceId = textViewResourceId;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		BookBean bookBean = getItem(position);
		View view;
		if(convertView == null){
			view = LayoutInflater.from(getContext()).inflate(resourceId, null);
		}else{
			view = convertView;
		}
		TextView timeText = (TextView) view.findViewById(R.id.order_date);
		TextView stateText = (TextView) view.findViewById(R.id.order_state);
		TextView stationText = (TextView) view.findViewById(R.id.oil_station);
		TextView costText = (TextView) view.findViewById(R.id.cost);
		timeText.setText(bookBean.getOil_time());
		stateText.setText(state[bookBean.getGas_state()]);
		stationText.setText(bookBean.getGasstation_name());
		costText.setText(""+bookBean.getTotal_price());
		return view;
	}
}
