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
 * �û�Ԥ�����棬���ڽ����û���Ԥ����Ϣ
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
					Toast.makeText(BookActivity.this, "ԤԼʧ�ܣ�������!", Toast.LENGTH_SHORT).show();
					return;
				}
				//ԤԼ�ɹ��󣬽�������Ϣд�����ݿⲢ�ϴ��������
				bookPresenter.saveBoogInfoAndUpload(bookBean);
				//��ת������BookBean��������������bookfragment�ĵ�ǰ���������Լ���ά���������ʾ
				//MainActivityʹ�õļ���ģʽ��singleTask,�û��������֮�󲻻��ٻص��ý��棬��activyt��
				//ʵ���Ѿ���ջ����Ϊ��activity��ʵ����ջ��λ��MainActivity������
				//��MainActivyt�п���ֱ���жϻ�õ�BOOK_BEAN��Ӧ�ǲ���null,��������жϽ���bookfragment����ת
				//Ȼ����bookFragment����ʾ��������Ϣ�Լ������Ķ�ά��
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
		gasDisTextView.setText("�˼���վ������"+gasItemBean.getDistance()+"��");
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
	//��ȡ�û�ѡ����͵����� E90:5.19
	public String getSpinnerText() {
		return spinner.getSelectedItem().toString();
	}
	//��ȡ�û���д�Ľ�Ǯ
	public String getMoneyText() {
		return moneyEditText.getText().toString();
	}
	//�����û���ǰ����ļ���վ����Ϣʵ�����
	public GasItemBean getGasItemBean() {
		return gasItemBean;
	}
}