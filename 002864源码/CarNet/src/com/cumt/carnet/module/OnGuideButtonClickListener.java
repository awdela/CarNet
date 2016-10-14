package com.cumt.carnet.module;

import com.cumt.carnet.R;
import com.cumt.carnet.presenter.IGuidePresenter;
import com.cumt.carnet.view.MainActivity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
/**
 * GuideFragment按钮点击事件
 * @author wangcan
 *
 */
public class OnGuideButtonClickListener implements OnClickListener{
	
	private IGuidePresenter iGuidePresenter = null;
	private Context context;
	
	public OnGuideButtonClickListener(IGuidePresenter iGuidePresenter,Context context) {
		super();
		this.iGuidePresenter = iGuidePresenter;
		this.context = context;
	}

	public void onClick(View v) {
		switch(v.getId()){
		case R.id.title_back:
			((MainActivity)context).openDrawerLayout();
			break;
		case R.id.btnGuide:
			iGuidePresenter.startGuide();
			break;
		case R.id.btnChange:
			iGuidePresenter.exchangeTextData();
			break;
		case R.id.btnCancel:
			iGuidePresenter.clearData();
			break;
		default:
			break;
		}
	}
}
