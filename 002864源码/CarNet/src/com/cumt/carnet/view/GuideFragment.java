package com.cumt.carnet.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.baidu.navisdk.adapter.BNRoutePlanNode.CoordinateType;
import com.cumt.carnet.R;
import com.cumt.carnet.module.OnGuideButtonClickListener;
import com.cumt.carnet.module.RoutePlanListener;
import com.cumt.carnet.presenter.IGuidePresenter;
import com.cumt.util.SPUtils;
import com.cumt.view.ButtonFlat;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

/**
 * 类名：GuideFragment 作用：导航界面fragment,显示与用户交互的导航界面，用于获取用户的起始地址以及目的地址，开始导航
 * 
 * @author wangcan
 * 
 */
public class GuideFragment extends Fragment implements OnItemClickListener,
		IGuidePresenter {

	private View rootView = null;// 缓存Fragment view
	// 当用户点击输入时进行title的隐藏
	private LinearLayout layoutTitle = null, layoutFrag = null;
	// 用于显示用户输入地址模糊匹配的内容
	private ListView positionList = null;
	private EditText startInputText = null, endInputText = null;
	// listview要添加的数据源
	private List<String> listData;
	PoiSearch mPoiSearch = null;// Poi检索
	// 用于存储 名称，坐标 item
	private HashMap<String, LatLng> addressAndPosHash = null;
	// ListView的适配器
	private ArrayAdapter<String> adapter = null;
	// 我的当前位置
	LatLng nowll = null, ll_start = null, ll_end = null;
	// 当前焦点所在的EditText
	private EditText nowEditText = null;
	// 交换按钮
	private ImageButton imageButton = null,imageTitle = null;
	private ButtonFlat btnCancle = null;
	private ButtonFlat btnGuide = null;
	private OnGuideButtonClickListener buttonClickListener = null;
	// 创建地理编码检索实例
	GeoCoder geoCoder = GeoCoder.newInstance();
	// 用于判断当前要进行地理编码的是其中哪一个，是第一个，第二个还是两者都要
	// --- 0 -----两者都要
	// --- 1 -----第一个需要
	// --- 2 -----第二个需要
	private int isStartPosition = 0;
	private boolean ifFirstGeo = true;// 当前进行地理编码的是否是第一个
	private boolean isTitleShow = true;// title是否显示

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initPoi();// Poi检索初始化
		listData = new ArrayList<String>();
		addressAndPosHash = new HashMap<String, LatLng>();
		nowll = new LatLng(
				(Float) SPUtils.get(getActivity(), "Latitude", 12.3f),
				(Float) SPUtils.get(getActivity(), "Lontitude", 12.3f));
		addressAndPosHash.put("我的当前位置", nowll);
		listData.add("我的当前位置");// 第一条数据
		// 设置地理编码检索监听者
		geoCoder.setOnGetGeoCodeResultListener(listener);
	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.fragment_guideview, null);
		}
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		initView(rootView);
		// Log.i("GuideFragment","createview:我被调用了");
		return rootView;
	}

	private void initView(View rootView) {
		if (positionList == null) {
			positionList = (ListView) rootView.findViewById(R.id.list_position);
			positionList.setOnItemClickListener(this);
		}
		if (startInputText == null || endInputText == null) {
			startInputText = (EditText) rootView
					.findViewById(R.id.input_position);
			endInputText = (EditText) rootView.findViewById(R.id.input_target);
			startInputText.addTextChangedListener(new TextWatcher() {

				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					initAndAddData();
					if (adapter == null) {// 如果适配器==null则进行加载适配器，这里主要是进行初次加载
						addAdapter();
					}
					// poi检索
					mPoiSearch.searchInCity((new PoiCitySearchOption())
							.city((String) SPUtils.get(getActivity(),
									"Cityname", "徐州市"))
							.keyword(startInputText.getText().toString())
							.pageNum(20));
				}

				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
					if (isTitleShow) {
						hideTitle();
					}
					nowEditText = startInputText;
				}

				public void afterTextChanged(Editable s) {
				}
			});
			endInputText.addTextChangedListener(new TextWatcher() {

				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					initAndAddData();
					if (adapter == null) {// 如果适配器==null则进行加载适配器，这里主要是进行初次加载
						addAdapter();
					}
					// poi检索
					mPoiSearch.searchInCity((new PoiCitySearchOption())
							.city((String) SPUtils.get(getActivity(),
									"Cityname", "徐州市"))
							.keyword(endInputText.getText().toString())
							.pageNum(20));
				}

				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
					if (isTitleShow) {
						hideTitle();
					}
					nowEditText = endInputText;
				}

				public void afterTextChanged(Editable s) {

				}
			});
		}
		if (layoutTitle == null) {
			layoutTitle = (LinearLayout) rootView
					.findViewById(R.id.include_guide);
			layoutFrag = (LinearLayout) rootView
					.findViewById(R.id.guidefragment_liner);
			imageButton = (ImageButton) rootView.findViewById(R.id.btnChange);
			imageTitle = (ImageButton) rootView.findViewById(R.id.title_back);
			imageTitle.setImageResource(R.drawable.playlist);
			btnCancle = (ButtonFlat) rootView.findViewById(R.id.btnCancel);
			btnGuide = (ButtonFlat) rootView.findViewById(R.id.btnGuide);
			btnGuide.setBackgroundColor(Color.BLUE);
			buttonClickListener = new OnGuideButtonClickListener(
					GuideFragment.this, getActivity());
			imageButton.setOnClickListener(buttonClickListener);
			btnCancle.setOnClickListener(buttonClickListener);
			btnGuide.setOnClickListener(buttonClickListener);
			imageTitle.setOnClickListener(buttonClickListener);
		}
	}

	@Override
	public void onStart() {
		if (!isTitleShow) {
			showTitle();
		}
		super.onStart();
	}

	// Poi初始化
	private void initPoi() {
		mPoiSearch = PoiSearch.newInstance();
		mPoiSearch.setOnGetPoiSearchResultListener(poiListener);
	}

	// Poi监听器
	OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener() {

		// 获取Place详情页检索结果
		public void onGetPoiDetailResult(PoiDetailResult arg0) {
		}

		// 获取POI检索结果
		public void onGetPoiResult(PoiResult poiResult) {
			if (poiResult == null
					|| poiResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {// 没有找到检索结果
				Toast.makeText(getActivity(), "未找到结果", Toast.LENGTH_LONG)
						.show();
				return;
			}
			if (poiResult.error == SearchResult.ERRORNO.NO_ERROR) {// 检索结果正常返回
				List<PoiInfo> poiAddress = poiResult.getAllPoi();
				for (PoiInfo info : poiAddress) {
					String name = info.name;
					LatLng ll = info.location;
					listData.add(name);// listview添加数据
					addressAndPosHash.put(name, ll);// 把对应item添加到哈希表
				}
				adapter.notifyDataSetChanged();// 更新listview数据
			}
		}
	};
	// 地理编码监听
	OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {
		// 反地理编码查询结果回调函数
		public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		}

		// 地理编码查询结果回调函数
		public synchronized void onGetGeoCodeResult(GeoCodeResult result) {
			Log.i("wwwwcccc", "C");
			if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
				String content = ifFirstGeo ? "起始地址无法识别" : "终止地址无法识别";
				// 没有检测到结果
				Toast.makeText(getActivity(), content, Toast.LENGTH_LONG)
						.show();
				return;
			}
			Message msg = myGeoHandler.obtainMessage();
			if (isStartPosition == 0 && ifFirstGeo) {
				msg.what = 3;
				ll_start = result.getLocation();
			} else if (isStartPosition == 0 && !ifFirstGeo) {
				msg.what = 4;
				ll_end = result.getLocation();
			} else if (ifFirstGeo) {
				msg.what = 1;
				ll_start = result.getLocation();
			} else if (!ifFirstGeo) {
				msg.what = 2;
				ll_end = result.getLocation();
			}
			myGeoHandler.sendMessage(msg);
		}
	};

	// Listview设置adapter
	private void addAdapter() {
		adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, listData);
		positionList.setAdapter(adapter);
	}

	// 每次POi之前Listview数据初始化
	private void initAndAddData() {
		listData.clear();
		listData.add("我的当前位置");
		addressAndPosHash.put("我的当前位置", nowll);
	}

	// listview点击事件
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// 点击之后设置用户选择的数据
		String name = listData.get(position);
		nowEditText.setText(name);
	}

	// 用于交换两个edittext的内容
	public void exchangeTextData() {
		String content = startInputText.getText().toString();
		startInputText.setText(endInputText.getText().toString());
		endInputText.setText(content);
	}

	// 在用户点击取消时调用，负责list和hash存储的数据，textview的数据以及恢复title的显示
	public void clearData() {
		addressAndPosHash.clear();
		if (!listData.isEmpty()) {
			listData.clear();
			if(adapter != null)
				adapter.notifyDataSetChanged();
		}
		if (!isTitleShow) {
			showTitle();
		}
	}

	// 用户点击导航后启动导航功能，具体而言是获得起始和终止位置的经纬度坐标，然后进行导航
	public void startGuide() {
		Log.i("wwwwcccc", "0");
		ll_start = addressAndPosHash.get(startInputText.getText().toString());
		ll_end = addressAndPosHash.get(endInputText.getText().toString());
		if (ll_start != null && ll_end != null) {
			routeplanToNavi(CoordinateType.BD09LL);
		} else if (ll_start == null && ll_end == null) {// 如果两者都等于null则必须一个编码完成再进行另一个的编码，否则会冲突，只能获得后调用的编码
			isStartPosition = 0;// 两者都需要
			geoFirst();
		} else if (ll_start == null) {
			isStartPosition = 1;// 第一个需要
			geoFirst();
		} else if (ll_end == null) {
			isStartPosition = 2;// 第二个需要
			geoSecond();
		}
	}

	// 地理编码 当一个地理编码解析完成后进行第二个地理编码的解析
	private void geoFirst() {
		ifFirstGeo = true;// 第一个进行地理编码
		Log.i("wwwwcccc", "A");
		// 地理编码
		geoCoder.geocode(new GeoCodeOption().city(
				(String) SPUtils.get(getActivity(), "Cityname", "徐州市"))
				.address(startInputText.getText().toString()));
	}

	private void geoSecond() {
		ifFirstGeo = false;// 第二个进行地理编码
		Log.i("wwwwcccc", "B");
		// 地理编码
		geoCoder.geocode(new GeoCodeOption().city(
				(String) SPUtils.get(getActivity(), "Cityname", "徐州市"))
				.address(endInputText.getText().toString()));
	}

	Handler myGeoHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				Log.i("msg.what", "1");
				routeplanToNavi(CoordinateType.BD09LL);
				break;
			case 2:
				Log.i("msg.what", "2");
				routeplanToNavi(CoordinateType.BD09LL);
				break;
			case 3:
				Log.i("msg.what", "3");
				geoSecond();
				break;
			case 4:
				Log.i("msg.what", "4");
				routeplanToNavi(CoordinateType.BD09LL);
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};

	private void routeplanToNavi(CoordinateType coType) {
		if (ll_start == null || ll_end == null) {
			Toast.makeText(getActivity(), "请重新输入尝试", 0).show();
			return;
		}
		BNRoutePlanNode sNode = null;
		BNRoutePlanNode eNode = null;
		switch (coType) {
		case BD09LL: {
			sNode = new BNRoutePlanNode(ll_start.longitude, ll_start.latitude,
					"起点", null, coType);
			eNode = new BNRoutePlanNode(ll_end.longitude, ll_end.latitude,
					"终点", null, coType);
			break;
		}
		default:
			;
		}
		if (sNode != null && eNode != null) {
			List<BNRoutePlanNode> list = new ArrayList<BNRoutePlanNode>();
			list.add(sNode);
			list.add(eNode);
			BaiduNaviManager.getInstance().launchNavigator(getActivity(), list,
					1, true,
					new RoutePlanListener(sNode, getActivity(), getActivity()));
		}
	}

	// 显示linerlayout中的title
	private void showTitle() {
		ObjectAnimator anim = ObjectAnimator.ofFloat(layoutFrag, "y",
				layoutFrag.getY(), layoutFrag.getY() + layoutTitle.getHeight());
		anim.setDuration(500);
		anim.start();
		isTitleShow = true;
	}

	// 隐藏linerlayout中的title
	private void hideTitle() {
		ObjectAnimator anim = ObjectAnimator.ofFloat(layoutFrag, "y",
				layoutFrag.getY(), layoutFrag.getY() - layoutTitle.getHeight());
		anim.setDuration(500);
		anim.start();
		isTitleShow = false;
	}

	@Override
	public void onResume() {
		Log.i("resume", "我被调用了");
		isTitleShow = true;
		super.onResume();
	}
	
}
