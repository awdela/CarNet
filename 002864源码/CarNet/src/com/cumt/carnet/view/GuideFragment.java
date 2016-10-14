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
 * ������GuideFragment ���ã���������fragment,��ʾ���û������ĵ������棬���ڻ�ȡ�û�����ʼ��ַ�Լ�Ŀ�ĵ�ַ����ʼ����
 * 
 * @author wangcan
 * 
 */
public class GuideFragment extends Fragment implements OnItemClickListener,
		IGuidePresenter {

	private View rootView = null;// ����Fragment view
	// ���û��������ʱ����title������
	private LinearLayout layoutTitle = null, layoutFrag = null;
	// ������ʾ�û������ַģ��ƥ�������
	private ListView positionList = null;
	private EditText startInputText = null, endInputText = null;
	// listviewҪ��ӵ�����Դ
	private List<String> listData;
	PoiSearch mPoiSearch = null;// Poi����
	// ���ڴ洢 ���ƣ����� item
	private HashMap<String, LatLng> addressAndPosHash = null;
	// ListView��������
	private ArrayAdapter<String> adapter = null;
	// �ҵĵ�ǰλ��
	LatLng nowll = null, ll_start = null, ll_end = null;
	// ��ǰ�������ڵ�EditText
	private EditText nowEditText = null;
	// ������ť
	private ImageButton imageButton = null,imageTitle = null;
	private ButtonFlat btnCancle = null;
	private ButtonFlat btnGuide = null;
	private OnGuideButtonClickListener buttonClickListener = null;
	// ��������������ʵ��
	GeoCoder geoCoder = GeoCoder.newInstance();
	// �����жϵ�ǰҪ���е���������������һ�����ǵ�һ�����ڶ����������߶�Ҫ
	// --- 0 -----���߶�Ҫ
	// --- 1 -----��һ����Ҫ
	// --- 2 -----�ڶ�����Ҫ
	private int isStartPosition = 0;
	private boolean ifFirstGeo = true;// ��ǰ���е��������Ƿ��ǵ�һ��
	private boolean isTitleShow = true;// title�Ƿ���ʾ

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initPoi();// Poi������ʼ��
		listData = new ArrayList<String>();
		addressAndPosHash = new HashMap<String, LatLng>();
		nowll = new LatLng(
				(Float) SPUtils.get(getActivity(), "Latitude", 12.3f),
				(Float) SPUtils.get(getActivity(), "Lontitude", 12.3f));
		addressAndPosHash.put("�ҵĵ�ǰλ��", nowll);
		listData.add("�ҵĵ�ǰλ��");// ��һ������
		// ���õ���������������
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
		// Log.i("GuideFragment","createview:�ұ�������");
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
					if (adapter == null) {// ���������==null����м�����������������Ҫ�ǽ��г��μ���
						addAdapter();
					}
					// poi����
					mPoiSearch.searchInCity((new PoiCitySearchOption())
							.city((String) SPUtils.get(getActivity(),
									"Cityname", "������"))
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
					if (adapter == null) {// ���������==null����м�����������������Ҫ�ǽ��г��μ���
						addAdapter();
					}
					// poi����
					mPoiSearch.searchInCity((new PoiCitySearchOption())
							.city((String) SPUtils.get(getActivity(),
									"Cityname", "������"))
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

	// Poi��ʼ��
	private void initPoi() {
		mPoiSearch = PoiSearch.newInstance();
		mPoiSearch.setOnGetPoiSearchResultListener(poiListener);
	}

	// Poi������
	OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener() {

		// ��ȡPlace����ҳ�������
		public void onGetPoiDetailResult(PoiDetailResult arg0) {
		}

		// ��ȡPOI�������
		public void onGetPoiResult(PoiResult poiResult) {
			if (poiResult == null
					|| poiResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {// û���ҵ��������
				Toast.makeText(getActivity(), "δ�ҵ����", Toast.LENGTH_LONG)
						.show();
				return;
			}
			if (poiResult.error == SearchResult.ERRORNO.NO_ERROR) {// ���������������
				List<PoiInfo> poiAddress = poiResult.getAllPoi();
				for (PoiInfo info : poiAddress) {
					String name = info.name;
					LatLng ll = info.location;
					listData.add(name);// listview�������
					addressAndPosHash.put(name, ll);// �Ѷ�Ӧitem��ӵ���ϣ��
				}
				adapter.notifyDataSetChanged();// ����listview����
			}
		}
	};
	// ����������
	OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {
		// ����������ѯ����ص�����
		public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		}

		// ��������ѯ����ص�����
		public synchronized void onGetGeoCodeResult(GeoCodeResult result) {
			Log.i("wwwwcccc", "C");
			if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
				String content = ifFirstGeo ? "��ʼ��ַ�޷�ʶ��" : "��ֹ��ַ�޷�ʶ��";
				// û�м�⵽���
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

	// Listview����adapter
	private void addAdapter() {
		adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, listData);
		positionList.setAdapter(adapter);
	}

	// ÿ��POi֮ǰListview���ݳ�ʼ��
	private void initAndAddData() {
		listData.clear();
		listData.add("�ҵĵ�ǰλ��");
		addressAndPosHash.put("�ҵĵ�ǰλ��", nowll);
	}

	// listview����¼�
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// ���֮�������û�ѡ�������
		String name = listData.get(position);
		nowEditText.setText(name);
	}

	// ���ڽ�������edittext������
	public void exchangeTextData() {
		String content = startInputText.getText().toString();
		startInputText.setText(endInputText.getText().toString());
		endInputText.setText(content);
	}

	// ���û����ȡ��ʱ���ã�����list��hash�洢�����ݣ�textview�������Լ��ָ�title����ʾ
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

	// �û���������������������ܣ���������ǻ����ʼ����ֹλ�õľ�γ�����꣬Ȼ����е���
	public void startGuide() {
		Log.i("wwwwcccc", "0");
		ll_start = addressAndPosHash.get(startInputText.getText().toString());
		ll_end = addressAndPosHash.get(endInputText.getText().toString());
		if (ll_start != null && ll_end != null) {
			routeplanToNavi(CoordinateType.BD09LL);
		} else if (ll_start == null && ll_end == null) {// ������߶�����null�����һ����������ٽ�����һ���ı��룬������ͻ��ֻ�ܻ�ú���õı���
			isStartPosition = 0;// ���߶���Ҫ
			geoFirst();
		} else if (ll_start == null) {
			isStartPosition = 1;// ��һ����Ҫ
			geoFirst();
		} else if (ll_end == null) {
			isStartPosition = 2;// �ڶ�����Ҫ
			geoSecond();
		}
	}

	// ������� ��һ��������������ɺ���еڶ����������Ľ���
	private void geoFirst() {
		ifFirstGeo = true;// ��һ�����е������
		Log.i("wwwwcccc", "A");
		// �������
		geoCoder.geocode(new GeoCodeOption().city(
				(String) SPUtils.get(getActivity(), "Cityname", "������"))
				.address(startInputText.getText().toString()));
	}

	private void geoSecond() {
		ifFirstGeo = false;// �ڶ������е������
		Log.i("wwwwcccc", "B");
		// �������
		geoCoder.geocode(new GeoCodeOption().city(
				(String) SPUtils.get(getActivity(), "Cityname", "������"))
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
			Toast.makeText(getActivity(), "���������볢��", 0).show();
			return;
		}
		BNRoutePlanNode sNode = null;
		BNRoutePlanNode eNode = null;
		switch (coType) {
		case BD09LL: {
			sNode = new BNRoutePlanNode(ll_start.longitude, ll_start.latitude,
					"���", null, coType);
			eNode = new BNRoutePlanNode(ll_end.longitude, ll_end.latitude,
					"�յ�", null, coType);
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

	// ��ʾlinerlayout�е�title
	private void showTitle() {
		ObjectAnimator anim = ObjectAnimator.ofFloat(layoutFrag, "y",
				layoutFrag.getY(), layoutFrag.getY() + layoutTitle.getHeight());
		anim.setDuration(500);
		anim.start();
		isTitleShow = true;
	}

	// ����linerlayout�е�title
	private void hideTitle() {
		ObjectAnimator anim = ObjectAnimator.ofFloat(layoutFrag, "y",
				layoutFrag.getY(), layoutFrag.getY() - layoutTitle.getHeight());
		anim.setDuration(500);
		anim.start();
		isTitleShow = false;
	}

	@Override
	public void onResume() {
		Log.i("resume", "�ұ�������");
		isTitleShow = true;
		super.onResume();
	}
	
}
