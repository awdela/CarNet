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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class Fragment1 extends Fragment implements OnItemClickListener, OnItemLongClickListener{

	private List<BookBean> bookList = null;// ���ж����б�
	private ListView listView;
	private BookItemAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("fragment1", "onCreate");
		BookInfoDao bookInfoDao = new BookInfoDao(getActivity());
		// initData();
		if (bookInfoDao.hasData()) {
			bookList = bookInfoDao.getBookInfo();
		} else {
			Toast.makeText(getActivity(), "����û�ж�����Ϣ", Toast.LENGTH_SHORT)
					.show();
		}
		Log.i("fragment","onCreate");
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		Log.i("fragment","onPause");
		super.onPause();
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		Log.i("fragment","onResume");
		super.onResume();
	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_one, null);
		listView = (ListView) view.findViewById(R.id.all_book);
		listView.setOnItemClickListener(this);
		listView.setOnItemLongClickListener(this);
		Log.i("fragment1", "onCreateView");
		if(bookList != null){
			initData();
		}
		Log.i("fragment","onCreateView");
		return view;
	}
	// ��ʾ����
	private void initData() {
		adapter = new BookItemAdapter(getActivity(), R.layout.order_item, bookList);
		listView.setAdapter(adapter);
	}
	//�����¼�
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(getActivity(), BookInfoActivity.class);
		intent.putExtra("BOOK_INFO", bookList.get(position));
		startActivity(intent);
		getActivity().finish();
	}
	//�����¼�
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
//		int state = bookList.get(position).getGas_state();//��ȡ����״̬
//		showDeleteDialog(state,position);
		return true;
	}
	
//	private void showDeleteDialog(final int state,final int position){
//		Dialog dialog = new Dialog(
//				getActivity(),"��ʾ",
//				"���Ƿ�Ҫɾ���ö���?","ȷ��");
//		dialog.addCancelButton("ȡ��");
//		dialog.setOnAcceptButtonClickListener(new OnClickListener() {
//			
//			public void onClick(View v) {
//				if(state == 0){//��ǰ�����޷�ɾ��
//					Toast.makeText(getActivity(), "��ǰԤԼ��������ɾ��", Toast.LENGTH_SHORT).show();
//					return;
//				}
//				//�����ݿ���ɾ��
//				deleteFromDB(bookList.get(position).getOil_time());
//				bookList.remove(position);
//				adapter.notifyDataSetChanged();//����listview
//				//�����ݿ�ɾ���ü�¼
//			}
//		});
//		dialog.setOnCancelButtonClickListener(new OnClickListener() {
//
//			public void onClick(View v) {
//				
//			}
//		});
//		dialog.show();
//	}
	
}
