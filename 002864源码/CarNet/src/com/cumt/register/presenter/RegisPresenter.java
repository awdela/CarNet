package com.cumt.register.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;
import com.cumt.register.entity.RegUserBean;
import com.cumt.register.module.OnRegListener;
import com.cumt.register.module.UserRegStatus;

@SuppressLint("HandlerLeak")
public class RegisPresenter {
	
	private IRegisPresenter iRegisPresenter;
	private RegUserBean regUserBean;
	private UserRegStatus regStatus;
	private Context context;
	
	private Handler myHandler = new Handler(){
		
		public void handleMessage(Message msg) {
			int code = msg.what;
			if(code == 0){
				Toast.makeText(context, "�û��Ѿ�ע�����",Toast.LENGTH_SHORT).show();
			}else if(code == 1){
				Toast.makeText(context, "ע��ɹ�",Toast.LENGTH_SHORT).show();
				iRegisPresenter.gotoNextActivity();//��ת��������
			}else{
				Toast.makeText(context, "δ֪����,������",Toast.LENGTH_SHORT).show();
			}
		};
	};
	
	
	public RegisPresenter(IRegisPresenter iRegisPresenter,Context context){
		this.iRegisPresenter = iRegisPresenter;
		this.context = context;
		regUserBean = new RegUserBean();
		regStatus = new UserRegStatus();
	}
	
	public void register(){
		regUserBean.setUserName(iRegisPresenter.getUserName());
		regUserBean.setUserPass(iRegisPresenter.getUserPass());
		
		regStatus.Register(context, regUserBean, new OnRegListener() {
			
			public void regisSuccess(RegUserBean regUserBean, int code) {
				// TODO Auto-generated method stub
				Message msg = myHandler.obtainMessage();
				msg.what = code;
				myHandler.sendMessage(msg);
			}
			
			public void regisFalied(int code) {
				// TODO Auto-generated method stub
				Message msg = myHandler.obtainMessage();
				msg.what = code;
				myHandler.sendMessage(msg);
			}
		});
	}
}
