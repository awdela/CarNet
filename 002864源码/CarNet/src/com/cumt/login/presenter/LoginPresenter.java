package com.cumt.login.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.cumt.login.entity.LoginUserBean;
import com.cumt.login.module.OnLoginListener;
import com.cumt.login.module.UserLoginStatus;

/**
 * ��¼���E����Ҫ�߼�������
 * @author wangcan
 *
 */
@SuppressLint("HandlerLeak")
public class LoginPresenter {
	
	
	private ILoginPresenter iLoginPresenter;
	private UserLoginStatus userLoginStatus;
	private LoginUserBean loginUserBean;
	private Context context;
	
	private Handler myHandler = new Handler(){
		
		public void handleMessage(Message msg) {
			int code = msg.what;
			if(code == 0){
				Log.e("login_return","�û�������");
				Toast.makeText(context, "�û�������",Toast.LENGTH_SHORT).show();
			}else if(code == 1){
				Log.e("login_return","�������");
				Toast.makeText(context, "�������",Toast.LENGTH_SHORT).show();
			}else if(code == 2){
				iLoginPresenter.gotoNextActivity();
			}else{
				Log.e("login_return","δ֪����");
				Toast.makeText(context, "δ֪����",Toast.LENGTH_SHORT).show();
			}
		};
	};
	
	
	public LoginPresenter(ILoginPresenter iLoginPresenter,Context context) {
		this.iLoginPresenter = iLoginPresenter;
		this.context = context;
		loginUserBean = new LoginUserBean();
		userLoginStatus = new UserLoginStatus();
	}

	/**
	 * 
	 * @param loginType loginType==1 ��ͨ��¼
	 */
	public void login(final int loginType){
		
		loginUserBean.setUserName(iLoginPresenter.getUserName());
		loginUserBean.setUserPass(iLoginPresenter.getUserPass());
		Log.i("username",iLoginPresenter.getUserName());
		
		userLoginStatus.Login(context, loginUserBean, new OnLoginListener() {
			
			public void loginSuccess(LoginUserBean loginUserBean, int code) {
				Log.i("loginsucess","�ұ�������!"+code);
				Message msg = myHandler.obtainMessage();
				msg.what = code;
				myHandler.sendMessage(msg);
//				if(code == 0){
//					Log.e("login_return","�û�������");
//					//Toast.makeText(context, "�û�������",Toast.LENGTH_SHORT).show();
//				}else if(code == 1){
//					Log.e("login_return","�������");
//					//Toast.makeText(context, "�������",Toast.LENGTH_SHORT).show();
//				}else if(code == 2){
//					iLoginPresenter.gotoNextActivity();
//				}else{
//					Log.e("login_return","δ֪����");
//					//Toast.makeText(context, "δ֪����",Toast.LENGTH_SHORT).show();
//				}
			}
			
			public void loginFailed(Context context) {
				Log.e("login_return","��¼ʧ�ܣ�������!");
				//Toast.makeText(context, "��¼ʧ�ܣ�������!", Toast.LENGTH_SHORT).show();
			}
		});
		
	}
}