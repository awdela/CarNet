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
 * 类名： DrawerAdapter
 * 作用：自定义适配器，用于将侧滑菜单的listview中的要显示的数据进行匹配
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
		DrawerItemBean listItem = getItem(position); // 获取当前项的ListItem实例
		View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
		ImageView itemImage = (ImageView) view.findViewById(R.id.itemt_image);
		TextView itemName = (TextView) view.findViewById(R.id.item_name);
		itemImage.setImageResource(listItem.getImageId());
		itemName.setText(listItem.getItemName());
		return view;
	}
}