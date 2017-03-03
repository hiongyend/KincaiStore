package com.kincai.store.ui.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.kincai.store.Constants;
import com.kincai.store.R;
import com.kincai.store.thread.CachedThreadPoolUtils;
import com.kincai.store.thread.HttpPostThread;
import com.kincai.store.ui.activity.base.BaseSlideActivity;
import com.kincai.store.utils.AnimationUtil;
import com.kincai.store.utils.JsonUtil;
import com.kincai.store.utils.LogTest;
import com.kincai.store.utils.NetWorkUtil;
import com.kincai.store.utils.Utils;

import android.annotation.SuppressLint;
//import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 * 
 * @author kincai
 * 
 * @description 添加地址
 * 
 * @project Kincai_Store
 * 
 * @package com.kincai.store.ui
 * 
 * @time 2015-7-8 上午8:28:18
 * 
 */
public class AddConsigneeAddressActivity extends BaseSlideActivity {

	private static final String TAG = "AddConsigneeAddressActivity";
	private Myhandler mMyhandler;

	private TextView mTitleTv;
	
	private TextView mAreaTv;
	private EditText mConsigneeEt;
	private EditText mPhoneNumEt;
	private EditText mPostalCodeEt;
	private EditText mDetailedAddressEt;

	private Button mSaveAddressBtn;

	@Override
	public int initContentView() {
		return R.layout.activity_add_consignee_address_layout;
	}
	@Override
	public void initDatas() {
		init();
		initView();
		setListener();
	}

	/**
	 * 初始化
	 */
	private void init() {
		mMyhandler = new Myhandler();
	}

	/**
	 * 初始化控件
	 */
	@Override
	public void initView() {
		super.initView();
		mTitleTv = (TextView) findViewById(R.id.title_tv);
		mAreaTv = (TextView) findViewById(R.id.add_address_area_tv);
		mConsigneeEt = (EditText) findViewById(R.id.add_address_consignee_et);
		mPhoneNumEt = (EditText) findViewById(R.id.add_address_phoneNum_et);
		mPostalCodeEt = (EditText) findViewById(R.id.add_address_postalCode_et);
		mDetailedAddressEt = (EditText) findViewById(R.id.add_address_detailedAddress_et);
		mSaveAddressBtn = (Button) findViewById(R.id.add_address_save_btn);

		mTitleTv.setText(getResources().getString(
				R.string.consignee_address_str));
		
	}

	/**
	 * 设置事件监听
	 */
	public void setListener() {
		super.setListener();
		mAreaTv.setOnClickListener(this);
		mSaveAddressBtn.setOnClickListener(this);
		
	}

	/**
	 * 
	 * @param userId
	 *            用户id
	 * @param consignee
	 *            收货人
	 * @param phoneNum
	 *            联系号码
	 * @param postalCode
	 *            邮政编码
	 * @param area
	 *            地区
	 * @param detailedAddress
	 *            详细地址
	 */
	private void addaddress(int userId, String consignee, String phoneNum,
			String postalCode, String area, String detailedAddress) {
		if (NetWorkUtil.isNetworkAvailable(AddConsigneeAddressActivity.this)) {
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
	
			parameters.add(new BasicNameValuePair("userId", String.valueOf(userId)));
			parameters.add(new BasicNameValuePair("consignee", consignee));
			parameters.add(new BasicNameValuePair("phoneNum", phoneNum));
			parameters.add(new BasicNameValuePair("postalCode", postalCode));
			parameters.add(new BasicNameValuePair("area", area));
			parameters.add(new BasicNameValuePair("detailedAddress",
					detailedAddress));
			parameters.add(new BasicNameValuePair("type", Constants.KINCAI_TYPE_ADD));

			// ((ConsigneeAddressActivity)ConsigneeAddressActivity.this).refreshData();

			CachedThreadPoolUtils.execute(new HttpPostThread(mMyhandler,
					Constants.API_URL + "appAddressApi.php", parameters,
					"addaddress"));
		}

	}

	@SuppressLint("HandlerLeak")
	class Myhandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);

			switch (msg.what) {
			case 2208:
				getResponseAddress(msg);

				break;
			}
		}
	}

	/**
	 * 返回数据处理
	 * 
	 * @param msg
	 */
	private void getResponseAddress(Message msg) {
		String response = (String) msg.obj;
		if (null != response) {
			String code = JsonUtil.msgJson("code", response);
			if (null != code && "200".equals(code)) {
				Utils.showToast(AddConsigneeAddressActivity.this, "添加地址成功",
						Toast.LENGTH_SHORT);
				Intent intent = new Intent();
				intent.putExtra("isTrue", true);
				setResult(1, intent);
				AddConsigneeAddressActivity.this.finish();
				AnimationUtil.finishHaveFloatActivityAnimation(this);
			} else {
				Utils.showToast(AddConsigneeAddressActivity.this, "添加地址失败",
						Toast.LENGTH_SHORT);
			}
		} else {
			Utils.showToast(AddConsigneeAddressActivity.this, "添加地址失败",
					Toast.LENGTH_SHORT);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.network_abnormal_top_layout:
			startActivity(new Intent(Settings.ACTION_SETTINGS));
			AnimationUtil.startHaveSinkActivityAnimation(this);
			break;
		case R.id.titlebar_back_iv:
			finish();
			AnimationUtil.finishHaveFloatActivityAnimation(this);
			break;
		case R.id.add_address_area_tv:
			startActivityForResult(new Intent(AddConsigneeAddressActivity.this,
					AreaSelectActivity.class), 66);
			AnimationUtil.startHaveSinkActivityAnimation(this);
			break;

		case R.id.add_address_save_btn:
			
			saveAddress();
			break;
		}
	}

	/**
	 * 保存收货地址
	 */
	private void saveAddress() {
		int userId = mBaseUserDao.getUserInfoOne(mSp.getUsername()).get(0).getUserId();
		String consignee = mConsigneeEt.getText().toString();
		String phoneNum = mPhoneNumEt.getText().toString();
		String postalCode = mPostalCodeEt.getText().toString();
		String area = mAreaTv.getText().toString();
		String detailedAddress = mDetailedAddressEt.getText().toString();
		if (null != mBaseUserDao.getUserInfoOne(mSp.getUsername())
				&& !"".equals(consignee) && !"".equals(phoneNum)
				&& !"".equals(postalCode) && !"".equals(area)
				&& !"".equals(detailedAddress)) {
			addaddress(userId, consignee, phoneNum, postalCode, area,
					detailedAddress);
		}
		

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (null != data) {
			if (requestCode == 66 && resultCode == 0) {
				mAreaTv.setText(data.getStringExtra("area"));
			}
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void netWork() {
		mNetWorkLayout.setVisibility(View.GONE);
	}
	
	@Override
	public void noNetWork() {
		mNetWorkLayout.setVisibility(View.VISIBLE);
	}

	@Override
	protected void onStart() {
		super.onStart();
		LogTest.LogMsg(TAG, "AddConsigneeAddressActivity-onStart");
	}

	@Override
	protected void onResume() {
		super.onResume();
		LogTest.LogMsg(TAG, "AddConsigneeAddressActivity-onResume");
	}

	@Override
	protected void onPause() {
		super.onPause();
		LogTest.LogMsg(TAG, "AddConsigneeAddressActivity-onPause");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		LogTest.LogMsg(TAG, "AddConsigneeAddressActivity-onDestroy");
	}

}
