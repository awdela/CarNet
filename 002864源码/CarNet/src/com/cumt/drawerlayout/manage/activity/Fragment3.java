package com.cumt.drawerlayout.manage.activity;

import java.util.List;
import com.cumt.book.entity.BookBean;
import com.cumt.carnet.R;
import com.cumt.drawerlayout.manage.adapter.BookItemAdapter;
import com.db.BookInfoDao;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class Fragment3 extends Fragment implements OnItemClickListener {

	private List<BookBean> bookList = null;// 所有未完成订单列表
	private ListView listView;
	private BookItemAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		BookInfoDao bookInfoDao = new BookInfoDao(getActivity());
		// initData();
		if (bookInfoDao.hasData()) {
			bookList = bookInfoDao.getNotFinishBookInfo();
		}
	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_three, null);
		listView = (ListView) view.findViewById(R.id.notfinish_book);
		listView.setOnItemClickListener(this);
		if (bookList != null) {
			initData();
		}
		return view;
	}

	// 显示数据
	private void initData() {
		adapter = new BookItemAdapter(getActivity(), R.layout.order_item, bookList);
		listView.setAdapter(adapter);
	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(getActivity(), BookInfoActivity.class);
		intent.putExtra("BOOK_INFO", bookList.get(position));
		startActivity(intent);	
		getActivity().finish();
	}
}
