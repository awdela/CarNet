package com.cumt.carnet.adapter;

import java.util.List;

import com.cumt.carnet.R;
import com.cumt.carnet.entity.DrawerItemBean;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * ������ DrawerAdapter
 * ���ã��Զ��������������ڽ��໬�˵���listview�е�Ҫ��ʾ�����ݽ���ƥ��
 * @author wangcan
 *
 */
public class DrawerAdapter extends ArrayAdapter<DrawerItemBean>{
	
	private int resourceId;

	public DrawerAdapter(Context context, int textViewResourceId,
			List<DrawerItemBean> objects) {
		super(context, textViewResourceId, objects);
		resourceId = textViewResourceId;
	}

	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		DrawerItemBean listItem = getItem(position); // ��ȡ��ǰ���ListItemʵ��
		View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
		ImageView itemImage = (ImageView) view.findViewById(R.id.itemt_image);
		TextView itemName = (TextView) view.findViewById(R.id.item_name);
		itemImage.setImageResource(listItem.getImageId());
		itemName.setText(listItem.getItemName());
		return view;
	}
}