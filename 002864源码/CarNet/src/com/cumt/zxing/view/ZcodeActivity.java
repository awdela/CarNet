package com.cumt.zxing.view;

import com.app.CarnetApplication;
import com.cumt.carnet.R;
import com.cumt.drawerlayout.carinfo.CarInfoActivity;
import com.cumt.util.NormalMethodsUtils;
import com.cumt.view.ButtonFloat;
import com.cumt.view.Dialog;
import com.cumt.zxing.entity.ZxingBean;
import com.cumt.zxing.presenter.CarInfoSavePresenter;
import com.cumt.zxing.presenter.ICarInfoPresenter;
import com.zxing.activity.CaptureActivity;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
 * ����:ZcodeActivity ����:��ʾ��֤��ʶ���Ľ��
 * 
 * @author wangcan
 * 
 */
public class ZcodeActivity extends Activity implements OnGestureListener,ICarInfoPresenter{

	private static final int PHOTO_PIC = 1;// ɨ���ά��activity�ص��жϲ���
	private String result;// ������ά��Ľ��
	private ZxingBean zxingBean = null;// �������ʵ��
	private LinearLayout layout = null, titleLayout = null;
	private boolean isTitleShow = true;
	private ButtonFloat btnFloat;
	private CarInfoSavePresenter carInfoSavePresenter = null;

	private TextView[] textViews = new TextView[11];

	private int[] textViewIds = {

	R.id.brand_text, R.id.symble_pic, R.id.style_text, R.id.car_number,
			R.id.engine_text, R.id.lever_text, R.id.miles_text, R.id.oil_text,
			R.id.engine_feature, R.id.transmision_text, R.id.light_text };

	// �������Ƽ����ʵ��
	GestureDetector detector;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_carmessage);
		carInfoSavePresenter = new CarInfoSavePresenter(ZcodeActivity.this);
		initTextView();
		layout = (LinearLayout) findViewById(R.id.layout);
		titleLayout = (LinearLayout) findViewById(R.id.title);
		btnFloat = (ButtonFloat) findViewById(R.id.buttonFloat);
		btnFloat.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				
				showFloatRotate(btnFloat);
				showBindDialog();
			}
		});
		btnFloat.hide();
		// �������Ƽ����
		detector = new GestureDetector(this, this);
		// ��ת�����ս���ɨ���ά��
		Intent intent = new Intent(ZcodeActivity.this, CaptureActivity.class);
		startActivityForResult(intent, PHOTO_PIC);
	}

	private void initTextView() {
		for (int i = 0; i < 11; i++) {
			if (textViewIds[i] != R.id.symble_pic)
				textViews[i] = (TextView) findViewById(textViewIds[i]);
		}
	}

	// ����ϵͳactivity�ص�����
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case PHOTO_PIC:
				result = data.getExtras().getString("result");
				zxingBean = NormalMethodsUtils.getZxingBean(result);
				getZxingBean(result, zxingBean);// ��ʾ��ά����
//				Toast.makeText(ZcodeActivity.this, "���������" + result,
//						Toast.LENGTH_LONG).show();
				ImageView head = (ImageView) findViewById(R.id.symble_pic);
				carInfoSavePresenter.loadCarPhoto(zxingBean.getSymbol(), head);
				break;
			default:
				break;
			}
		} else {
			btnFloat.setVisibility(View.GONE);// ����û�н�������ά����ð�ť��Ч
		}
	}

	// ����ע��-----------------------������------��ά����-------------------------------------
	private ZxingBean getZxingBean(String result, ZxingBean zxingBean) {

		if (zxingBean == null) {
			zxingBean = new ZxingBean();
		}
		String[] result_dot = result.split(",");
		int i = 0;
		for (String item : result_dot) {
			String[] temp = item.split(":");
			if (i != 1 && i<textViewIds.length)
				textViews[i].setText(temp[1]);
			i++;
		}
		return null;
	}

	// ����activity�ϵĴ����¼�����GestureDetector����
	public boolean onTouchEvent(MotionEvent me) {
		return detector.onTouchEvent(me);
	}

	public boolean onDown(MotionEvent e) {
		return false;
	}

	public void onShowPress(MotionEvent e) {
	}

	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	public void onLongPress(MotionEvent e) {
	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		float minMove = 120; // ��С��������
		float minVelocity = 0; // ��С�����ٶ�
//		float beginX = e1.getX();
//		float endX = e2.getX();
		float beginY = e1.getY();
		float endY = e2.getY();
		// if(beginX-endX>minMove&&Math.abs(velocityX)>minVelocity){ //��
		// Toast.makeText(this,velocityX+"��",Toast.LENGTH_SHORT).show();
		// }else if(endX-beginX>minMove&&Math.abs(velocityX)>minVelocity){ //�һ�
		// Toast.makeText(this,velocityX+"�һ�",Toast.LENGTH_SHORT).show();
		if (beginY - endY > minMove && Math.abs(velocityY) > minVelocity) { // �ϻ�
			if (isTitleShow) {
				hideTitle();
				btnFloat.show2();
			}
			// Toast.makeText(this,velocityX+"�ϻ�",Toast.LENGTH_SHORT).show();
		} else if (endY - beginY > minMove && Math.abs(velocityY) > minVelocity) { // �»�
			// Toast.makeText(this,velocityX+"�»�",Toast.LENGTH_SHORT).show();
			if (!isTitleShow) {
				showTitle();
				btnFloat.hide2();
			}
		}
		return false;
	}

	public void hideTitle() {
		ObjectAnimator anim = ObjectAnimator.ofFloat(
				layout,
				"y",
				titleLayout.getY(),
				titleLayout.getY() - titleLayout.getHeight()
						+ textViews[0].getHeight() * 1.5f);
		ObjectAnimator anim2 = ObjectAnimator.ofFloat(textViews[0],
				"translationX", 0, -textViews[0].getX() * 0.9f);// X��ƽ��
		AnimatorSet set = new AnimatorSet();
		set.playTogether(anim, anim2);
		set.setDuration(1000);
		set.start();
		isTitleShow = false;
	}

	public void showTitle() {
		ObjectAnimator anim = ObjectAnimator.ofFloat(layout, "y",
				-titleLayout.getHeight() + textViews[0].getHeight() * 2,
				titleLayout.getY());
		ObjectAnimator anim2 = ObjectAnimator.ofFloat(textViews[0],
				"translationX", -titleLayout.getHeight(), 0);// X��ƽ��
		AnimatorSet set = new AnimatorSet();
		set.playTogether(anim, anim2);
		set.setDuration(1000);
		set.start();
		isTitleShow = true;
	}

	// buttonfloat��ת����
	public void showFloatRotate(ButtonFloat btn) {
		ObjectAnimator animator = ObjectAnimator.ofFloat(btn, "rotation", 0F,
				145F);// 360����ת
		animator.setDuration(500);
		animator.start();
	}

	// buttonfloat��ת����
	public void hideFloatRotate(ButtonFloat btn) {
		ObjectAnimator animator = ObjectAnimator.ofFloat(btn, "rotation", -45F,
				-180F);// 360����ת
		animator.setDuration(500);
		animator.start();
	}
	
	private void showBindDialog(){
		final Dialog dialog = new Dialog(
				ZcodeActivity.this,"��ʾ",
				"���Ƿ�Ҫ�󶨸ó�?","ȷ��");
		dialog.addCancelButton("ȡ��");
		
		dialog.setOnAcceptButtonClickListener(new OnClickListener() {

			public void onClick(View v) {
				saveCarInfoDB();
				CarnetApplication carApp = CarnetApplication.getInstance();
				carApp.setCar_number(zxingBean.getCar_number());//�����û���ǰ�󶨵ĳ���
				Intent intent = new Intent(ZcodeActivity.this, CarInfoActivity.class);
				startActivity(intent);
				ZcodeActivity.this.finish();
			}
		});
		dialog.setOnCancelButtonClickListener(new OnClickListener() {

			public void onClick(View v) {
				
			}
		});
		dialog.setOnDismissListener(new OnDismissListener() {
			
			public void onDismiss(DialogInterface dialog) {
				hideFloatRotate(btnFloat);
			}
		});
		dialog.show();
	}
	
	//������ݿ����޸ó���Ϣ ��������Ϣд�����ݿ� �ϴ���������
	public void saveCarInfoDB() {
		carInfoSavePresenter.saveCarInfoDB(zxingBean);
	}
}
