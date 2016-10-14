package com.cumt.book.view;

import java.util.ArrayList;

import com.cumt.book.entity.BookBean;
import com.cumt.book.entity.GasPriceBean;
import com.cumt.book.module.BookStatus;
import com.cumt.book.presenter.BookPresenter;
import com.cumt.book.presenter.IPresenter;
import com.cumt.carnet.R;
import com.cumt.carnet.entity.GasItemBean;
import com.cumt.carnet.view.MainActivity;
import com.cumt.util.REUtils;
import com.cumt.view.ButtonRectangle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 用户预定界面，用于接收用户的预定信息
 * @author wangcan
 *
 */
public class BookActivity extends Activity implements IPresenter{
	
	private TextView gasTitleTextView,gasContentTextView,gasDisTextView;
	private EditText moneyEditText;
	private Spinner spinner;
	private ArrayAdapter<String> spinnerAdapter;
	private ButtonRectangle postButtonRectangle;
	private ButtonRectangle returnButtonRectangle;
	private GasItemBean gasItemBean;
	ArrayList<GasPriceBean> gasPriceBeanList;
	private BookPresenter bookPresenter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_book);
		gasItemBean = (GasItemBean) getIntent().getSerializableExtra("MarkerGasItem");
		Toast.makeText(BookActivity.this, gasItemBean.getPrice(), Toast.LENGTH_SHORT).show();
//		Log.i("wwww",gasItemBean.getPrice());
		//"E90":"5.19","E93":"5.54","E97":"5.9","E0":"5.11"
		gasPriceBeanList = REUtils.getGasPriceList(gasItemBean.getPrice());
		bookPresenter = new BookPresenter(this);
		initView();
		addData();
	}
	
	private void initView(){
		gasTitleTextView = (TextView) findViewById(R.id.gas_title);
		gasContentTextView = (TextView) findViewById(R.id.gas_content);
		gasDisTextView = (TextView) findViewById(R.id.gas_distance);
		spinner = (Spinner) findViewById(R.id.gas_spinner);
		moneyEditText = (EditText) findViewById(R.id.gas_money);
		postButtonRectangle = (ButtonRectangle) findViewById(R.id.gas_post);
		postButtonRectangle.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				BookStatus bookStatus = new BookStatus();
				BookBean bookBean = null;
				try{
					bookBean = bookStatus.getBookBean(gasItemBean,getSpinnerText()
						,getMoneyText());
				}catch(Exception e){
					Toast.makeText(BookActivity.this, "预约失败，请重试!", Toast.LENGTH_SHORT).show();
					return;
				}
				//预约成功后，将订单信息写入数据库并上传到服务端
				bookPresenter.saveBoogInfoAndUpload(bookBean);
				//跳转并传递BookBean对象，用于主界面bookfragment的当前订单查新以及二维码的生成显示
				//MainActivity使用的加载模式是singleTask,用户点击返回之后不会再回到该界面，该activyt的
				//实例已经出栈，因为该activity的实例在栈中位于MainActivity的上面
				//在MainActivyt中可以直接判断获得的BOOK_BEAN对应是不是null,根据这个判断进行bookfragment的跳转
				//然后在bookFragment中显示订单的信息以及订单的二维码
				Intent intent = new Intent(BookActivity.this, MainActivity.class);
				intent.putExtra("BOOK_BEAN", bookBean);
				startActivity(intent);
				BookActivity.this.finish();
				
			}
		});
		returnButtonRectangle = (ButtonRectangle) findViewById(R.id.gas_return);
		returnButtonRectangle.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(BookActivity.this, MainActivity.class);
				startActivity(intent);
				BookActivity.this.finish();
			}
		});
	}
	
	private void addData(){
		gasTitleTextView.setText(gasItemBean.getName());
		gasContentTextView.setText(gasItemBean.getAddress());
		gasDisTextView.setText("此加油站距离您"+gasItemBean.getDistance()+"米");
		spinnerAdapter = new ArrayAdapter<String>(BookActivity.this, 
				android.R.layout.simple_spinner_dropdown_item
				,makeSpinnerListData(gasPriceBeanList));
		spinner.setAdapter(spinnerAdapter);
	}
	
	private ArrayList<String> makeSpinnerListData(ArrayList<GasPriceBean> arrayList){
		ArrayList<String> list = new ArrayList<String>();
		for(GasPriceBean item:arrayList){
			list.add(item.getGasType()+":"+item.getGasPrice());
		}
		return list;
	}
	//获取用户选择的油的类型 E90:5.19
	public String getSpinnerText() {
		return spinner.getSelectedItem().toString();
	}
	//获取用户填写的金钱
	public String getMoneyText() {
		return moneyEditText.getText().toString();
	}
	//返回用户当前点击的加油站的信息实体对象
	public GasItemBean getGasItemBean() {
		return gasItemBean;
	}
}