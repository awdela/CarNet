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
 * 登录工鞥的主要逻辑控制类
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
				Log.e("login_return","用户不存在");
				Toast.makeText(context, "用户不存在",Toast.LENGTH_SHORT).show();
			}else if(code == 1){
				Log.e("login_return","密码错误");
				Toast.makeText(context, "密码错误",Toast.LENGTH_SHORT).show();
			}else if(code == 2){
				iLoginPresenter.gotoNextActivity();
			}else{
				Log.e("login_return","未知错误");
				Toast.makeText(context, "未知错误",Toast.LENGTH_SHORT).show();
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
	 * @param loginType loginType==1 普通登录
	 */
	public void login(final int loginType){
		
		loginUserBean.setUserName(iLoginPresenter.getUserName());
		loginUserBean.setUserPass(iLoginPresenter.getUserPass());
		Log.i("username",iLoginPresenter.getUserName());
		
		userLoginStatus.Login(context, loginUserBean, new OnLoginListener() {
			
			public void loginSuccess(LoginUserBean loginUserBean, int code) {
				Log.i("loginsucess","我被调用了!"+code);
				Message msg = myHandler.obtainMessage();
				msg.what = code;
				myHandler.sendMessage(msg);
//				if(code == 0){
//					Log.e("login_return","用户不存在");
//					//Toast.makeText(context, "用户不存在",Toast.LENGTH_SHORT).show();
//				}else if(code == 1){
//					Log.e("login_return","密码错误");
//					//Toast.makeText(context, "密码错误",Toast.LENGTH_SHORT).show();
//				}else if(code == 2){
//					iLoginPresenter.gotoNextActivity();
//				}else{
//					Log.e("login_return","未知错误");
//					//Toast.makeText(context, "未知错误",Toast.LENGTH_SHORT).show();
//				}
			}
			
			public void loginFailed(Context context) {
				Log.e("login_return","登录失败，请重试!");
				//Toast.makeText(context, "登录失败，请重试!", Toast.LENGTH_SHORT).show();
			}
		});
		
	}
}