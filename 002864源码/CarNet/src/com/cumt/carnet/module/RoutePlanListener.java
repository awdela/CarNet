package com.cumt.carnet.module;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.cumt.test.BNDemoGuideActivity;

public class RoutePlanListener implements com.baidu.navisdk.adapter.BaiduNaviManager.RoutePlanListener {

	private BNRoutePlanNode mBNRoutePlanNode = null;
	public static List<Activity> activityList = new LinkedList<Activity>();
	public static final String ROUTE_PLAN_NODE = "routePlanNode";
	private Context context;
	
	public RoutePlanListener(BNRoutePlanNode node,Activity activity,Context context) {
		mBNRoutePlanNode = node;
		activityList.add(activity);
		this.context = context;
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
		Intent intent = new Intent(context, BNDemoGuideActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable(ROUTE_PLAN_NODE, (BNRoutePlanNode) mBNRoutePlanNode);
		intent.putExtras(bundle);
		context.startActivity(intent);
	}
	
	public void onRoutePlanFailed() {
		Toast.makeText(context, "算路失败", Toast.LENGTH_SHORT).show();
	}
}