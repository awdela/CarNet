package com.cumt.carnet.view;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.baidu.navisdk.adapter.BNRoutePlanNode.CoordinateType;
import com.baidu.navisdk.adapter.BaiduNaviManager.RoutePlanListener;
import com.cumt.book.entity.BookBean;
import com.cumt.book.module.BookStatus;
import com.cumt.carnet.R;
import com.cumt.test.BNDemoGuideActivity;
import com.cumt.util.SPUtils;
import com.cumt.view.ButtonRectangle;
import com.cumt.view.ImageDialog;
import com.cumt.view.ItemDialog;
import com.db.BookInfoDao;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 类名：BookFragment
 * 作用：预约界面fragment,用于显示用户当前预约的信息
 * @author wangcan
 *
 */
@SuppressLint("InflateParams")
public class BookFragment extends Fragment implements OnClickListener {
	
	public static List<Activity> activityList = new LinkedList<Activity>();
	public static final String ROUTE_PLAN_NODE = "routePlanNode";
	private TextView timeText,numberText,stationText;
	private TextView phoneText,oilTypeText,costText;
	private BookBean bookBean = null;
	private ImageView qrcodeImage;
	private RelativeLayout layout;
	//button
	private ImageButton titleImage = null;
	private ButtonRectangle btnGuide = null;
	private ButtonRectangle btnCancel = null;
	private View rootView = null;//缓存Fragment view
	private float lontitude,latitude;//用户的当前位置
	//qcode
	private Bitmap bmp = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activityList.add(getActivity());
		lontitude = (Float) SPUtils.get(getActivity(), "Lontitude", 117.147f);
		latitude = (Float) SPUtils.get(getActivity(), "Latitude", 34.3570f);
		initDBBook();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(rootView==null){
            rootView=inflater.inflate(R.layout.fragment_bookview,null);
        }
		ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        } 
		TextView titleText = (TextView) rootView.findViewById(R.id.title_text);
		titleText.setText("当前订单");
		if(bookBean != null){
			BookStatus bookStatus = new BookStatus();
			bmp = bookStatus.makeZxingOfBook(bookBean);
		}
		if(btnGuide == null){
			titleImage = (ImageButton) rootView.findViewById(R.id.title_back);
			titleImage.setImageResource(R.drawable.playlist);
			titleImage.setOnClickListener(this);
			btnGuide = (ButtonRectangle) rootView.findViewById(R.id.start_guide);
			btnGuide.setOnClickListener(this);
			btnCancel = (ButtonRectangle) rootView.findViewById(R.id.cancel_reserve);
			btnCancel.setOnClickListener(this);
			initVew();
		}
		if(bookBean == null){//如果没有生成订单，则导航按钮设为不可见,取消订单按钮也设为不见
			btnGuide.setVisibility(View.GONE);
			btnCancel.setVisibility(View.GONE);
		}else{
			showBookMessage();
			btnGuide.setVisibility(View.VISIBLE);
			btnCancel.setVisibility(View.VISIBLE);
		}
		return rootView;
	}
	//初始化textView imageview和 layout
	private void initVew(){
		timeText = (TextView) rootView.findViewById(R.id.reserve_time);
		numberText = (TextView) rootView.findViewById(R.id.car_number);
		stationText = (TextView) rootView.findViewById(R.id.reserve_station);
		phoneText = (TextView) rootView.findViewById(R.id.phone_number);
		oilTypeText = (TextView) rootView.findViewById(R.id.oil_style);
		costText = (TextView) rootView.findViewById(R.id.reserve_cost);
		qrcodeImage = (ImageView) rootView.findViewById(R.id.reserve_qrcode);
		layout = (RelativeLayout) rootView.findViewById(R.id.gas_detail_msg);
		qrcodeImage.setOnClickListener(this);
		layout.setOnClickListener(this);
	}
	
	//显示订单信息
	public void showBookMessage(){
		timeText.setText(bookBean.getOil_time());
		numberText.setText(bookBean.getCar_number());
		stationText.setText(bookBean.getGasstation_name());
		phoneText.setText(bookBean.getPhone_number());
		oilTypeText.setText(bookBean.getOilStyle());
		costText.setText(""+bookBean.getTotal_price());
	}
	//显示二维码
	@SuppressLint("InflateParams")
	public void showZxingMessgae(BookBean bookBean){
		this.bookBean = bookBean;
		if(btnGuide != null){
			btnGuide.setVisibility(View.VISIBLE);
			btnCancel.setVisibility(View.VISIBLE);
			showBookMessage();
		}
		if(bmp == null){
			BookStatus bookStatus = new BookStatus();
			bmp = bookStatus.makeZxingOfBook(bookBean);
		}
	}

	public void onClick(View v) {
		switch(v.getId()){
		case R.id.start_guide:
			if (BaiduNaviManager.isNaviInited()) {
				routeplanToNavi(CoordinateType.BD09LL);
			}
			break;
		case R.id.reserve_qrcode:
			if(bmp != null){
				showQcodeDialog();
			}
			break;
		case R.id.gas_detail_msg:
			if(bookBean != null){
				showGasDetailMsg();
			}
			break;
		case R.id.title_back:
			((MainActivity)getActivity()).openDrawerLayout();
			break;
		case R.id.cancel_reserve://取消订单
			cancelBook();
			break;
		}
	}
	
	private void showQcodeDialog(){
		ImageDialog dialog = new com.cumt.view.ImageDialog(getActivity(), bmp);
		dialog.show();
	}
	
	private void showGasDetailMsg(){
		List<String> msg = new ArrayList<String>();
		msg.add(bookBean.getGas_address());
		msg.add("纬度: " + bookBean.getGas_lat());
		msg.add("经度: " + bookBean.getGas_lon());
		ItemDialog dialog = new ItemDialog(getActivity(),bookBean.getGasstation_name(), msg);
		dialog.show();
	}
	
	private void routeplanToNavi(CoordinateType coType) {
		BNRoutePlanNode sNode = null;
		BNRoutePlanNode eNode = null;
		switch (coType) {
			case BD09LL: {
				sNode = new BNRoutePlanNode(lontitude, latitude, "我的位置", null, coType);
				eNode = new BNRoutePlanNode(Double.valueOf(bookBean.getGas_lon())
						,Double.valueOf(bookBean.getGas_lat()), "目的地", null, coType);
				break;
			}
			default:
				;
			}
			if (sNode != null && eNode != null) {
				List<BNRoutePlanNode> list = new ArrayList<BNRoutePlanNode>();
				list.add(sNode);
				list.add(eNode);
				BaiduNaviManager.getInstance().launchNavigator(getActivity(), list, 1, true, new DemoRoutePlanListener(sNode));
			}
	}
	
	public class DemoRoutePlanListener implements RoutePlanListener {

		private BNRoutePlanNode mBNRoutePlanNode = null;

		public DemoRoutePlanListener(BNRoutePlanNode node) {
			mBNRoutePlanNode = node;
		}

		
		public void onJumpToNavigator() {
			/*
			 * 设置途径点以及resetEndNode会回调该接口
			 */
		 
			for (Activity ac : activityList) {
			   
				if (ac.getClass().getName().endsWith("BNDemoGuideActivity")) {
				 
					return;
				}
			}
			Intent intent = new Intent(getActivity(), BNDemoGuideActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable(ROUTE_PLAN_NODE, (BNRoutePlanNode) mBNRoutePlanNode);
			intent.putExtras(bundle);
			startActivity(intent);
		}
		
		public void onRoutePlanFailed() {
			Toast.makeText(getActivity(), "算路失败", Toast.LENGTH_SHORT).show();
		}
	}
	/**
	 * 初始化订单信息
	 */
	private void initDBBook(){
		BookInfoDao bookInfoDao = new BookInfoDao(getActivity());
		List<BookBean> bookList = bookInfoDao.getNowNotFinishBookInfo();
		if(!bookList.isEmpty()){
			bookBean = bookList.get(bookList.size()-1);
		}
	}
	/**
	 * 取消订单
	 */
	private void cancelBook(){
		timeText.setText("您当前没有预约哦");
		numberText.setText("");
		stationText.setText("");
		phoneText.setText("");
		oilTypeText.setText("");
		costText.setText("");
		BookInfoDao bookInfoDao = new BookInfoDao(getActivity());
		bookInfoDao.updateBookState(2, bookBean.getOil_time());
		bookBean = null;
		bmp = null;
		btnGuide.setVisibility(View.GONE);
		btnCancel.setVisibility(View.GONE);
	}
}
