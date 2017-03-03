package com.kincai.store.ui.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.kincai.store.Constants;
import com.kincai.store.R;
import com.kincai.store.bean.AddressInfo;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 * 
 * @author kincai
 * 
 * @description 编辑地址
 * 
 * @project Kincai_Store
 * 
 * @package com.kincai.store.ui
 * 
 * @time 2015-7-11 下午8:14:30
 * 
 */
public class EditConsigneeAddressActivity extends BaseSlideActivity {
	private static final String TAG = "EditConsigneeAddressActivity";

	
	private TextView mTitleTv;
	private TextView mAreaTv;
	private EditText mConsigneeEt;
	private EditText mPhoneNumEt;
	private EditText mPostalCodeEt;
	private EditText mDetailedAddressEt;
	private CheckBox mMorenCb;
	private TextView mMorenTv;
	private TextView mDeleteTv;

	private Button mSaveAddressBtn;
	

	private AddressInfo mAddressInfo;
	private String mUserId;

	private DefaultChenkBox mDefaultChenkBox;
	private MyHandler mMyHandler;
	private boolean isSelectDefault;


	@Override
	public int initContentView() {
		return R.layout.activity_edit_consignee_address_layout;
	}
	@Override
	public void initDatas() {
		init();
		initView();
		initData();
		setListener();
	}
	
	/**
	 * 初始化
	 */
	private void init() {
		Intent intent = getIntent();
		if (null != intent) {
			mAddressInfo = (AddressInfo) intent
					.getSerializableExtra("addressInfo");
			mUserId = intent.getStringExtra("userId");
		}

	}

	/**
	 * 初始化空间
	 */
	public void initView() {
		super.initView();
		mTitleTv = (TextView) findViewById(R.id.title_tv);
		mAreaTv = (TextView) findViewById(R.id.edit_address_area_tv);
		mConsigneeEt = (EditText) findViewById(R.id.edit_address_consignee_et);
		mPhoneNumEt = (EditText) findViewById(R.id.edit_address_phoneNum_et);
		mPostalCodeEt = (EditText) findViewById(R.id.edit_address_postalCode_et);
		mDetailedAddressEt = (EditText) findViewById(R.id.edit_address_detailedAddress_et);
		mMorenCb = (CheckBox) findViewById(R.id.edit_address_shezhi_moren_cb);
		mMorenTv = (TextView) findViewById(R.id.edit_address_shezhi_moren_tv);
		mDeleteTv = (TextView) findViewById(R.id.edit_address_delete_tv);
		mSaveAddressBtn = (Button) findViewById(R.id.edit_address_save_btn);

	}

	/**
	 * 初始化地址收货地址信息
	 */
	private void initData() {
		mTitleTv.setText(getResources().getString(R.string.edit_address_str));
		if (null != mAddressInfo) {
			mConsigneeEt.setText(mAddressInfo.getConsignee());
			mPhoneNumEt.setText(mAddressInfo.getPhoneNum());
			mPostalCodeEt.setText(mAddressInfo.getPostalCode());
			mAreaTv.setText(mAddressInfo.getArea());
			mDetailedAddressEt.setText(mAddressInfo.getDetailedAddress());
			// 当这个地址信息不是默认的收货地址的时候 才可以有选择默认
			if (mAddressInfo.getIsDefault() == 0) {
				mMorenCb.setVisibility(View.VISIBLE);
				mMorenTv.setVisibility(View.VISIBLE);

				mDefaultChenkBox = new DefaultChenkBox();
			}

		}
	}

	/**
	 * 设置事件监听
	 */
	public void setListener() {
		super.setListener();
		mSaveAddressBtn.setOnClickListener(this);
		mDeleteTv.setOnClickListener(this);
		mAreaTv.setOnClickListener(this);
		if (null != mDefaultChenkBox) {
			mMorenCb.setOnCheckedChangeListener(mDefaultChenkBox);
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
		case R.id.edit_address_delete_tv:
			deleteAddressInfo();
			
			break;
		case R.id.edit_address_save_btn:
			saveEditAddressInfo();

			break;
		case R.id.edit_address_area_tv:
			startActivityForResult(
					new Intent(EditConsigneeAddressActivity.this,
							AreaSelectActivity.class), 66);
			AnimationUtil.startHaveSinkActivityAnimation(this);
			break;
		}
	}
	
	/**
	 * 删除地址
	 */
	private void deleteAddressInfo() {
		if(NetWorkUtil.isNetworkAvailable(EditConsigneeAddressActivity.this)) {
			if(null == mMyHandler) {
				mMyHandler = new MyHandler();
			}
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();

			parameters.add(new BasicNameValuePair("id", String
					.valueOf(mAddressInfo.getId())));
			parameters.add(new BasicNameValuePair("userId", mUserId));
			parameters.add(new BasicNameValuePair("type",
					Constants.KINCAI_TYPE_DELETE));

			CachedThreadPoolUtils.execute(new HttpPostThread(mMyHandler,
					Constants.API_URL + "appAddressApi.php", parameters,
					"deleteAddress"));
		}
	}

	/**
	 * 保存编辑信息
	 */
	private void saveEditAddressInfo() {
		if (NetWorkUtil.isNetworkAvailable(EditConsigneeAddressActivity.this)) {

			String consignee = mConsigneeEt.getText().toString();
			String phoneNum = mPhoneNumEt.getText().toString();
			String postalCode = mPostalCodeEt.getText().toString();
			String area = mAreaTv.getText().toString();
			String detailedAddress = mDetailedAddressEt.getText().toString();
			String defaults = null;
			/** 如果全部都不为空 才能请求编辑*/
			if (null != mUserId && !"".equals(consignee)
					&& !"".equals(phoneNum) && !"".equals(postalCode)
					&& !"".equals(area) && !"".equals(detailedAddress)) {
				
//				if(mAddressInfo.getIsDefault() == 1) {//本身是为默认的话 那就不用判断checkbox 因为checkbox已经不可见
//					defaults = "1";
//				} else {
//					defaults = isSelectDefault ? "1" : "0";
//				}
			
				//把上面的简写  //本身是为默认的话 那就不用判断checkbox 因为checkbox已经不可见
				defaults = (mAddressInfo.getIsDefault() == 1) ? "1" : (isSelectDefault ? "1" : "0");

				if (null == mMyHandler) {
					mMyHandler = new MyHandler();
				}
				List<NameValuePair> parameters = new ArrayList<NameValuePair>();

				parameters.add(new BasicNameValuePair("id", String
						.valueOf(mAddressInfo.getId())));
				parameters.add(new BasicNameValuePair("userId", mUserId));
				parameters.add(new BasicNameValuePair("consignee", consignee));
				parameters.add(new BasicNameValuePair("phoneNum", phoneNum));
				parameters
						.add(new BasicNameValuePair("postalCode", postalCode));
				parameters.add(new BasicNameValuePair("area", area));
				parameters.add(new BasicNameValuePair("isDefault", defaults));
				parameters.add(new BasicNameValuePair("detailedAddress",
						detailedAddress));
				parameters.add(new BasicNameValuePair("type",
						Constants.KINCAI_TYPE_EDIT));

				CachedThreadPoolUtils.execute(new HttpPostThread(mMyHandler,
						Constants.API_URL + "appAddressApi.php", parameters,
						"editAddress"));

			}
		}
	}

	@SuppressLint("HandlerLeak")
	class MyHandler extends Handler {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 2216:
				getResponseEdit(msg);
				break;
			case 2217:
				getResponseDelete(msg);
				
				break;
			}
		};
	}

	/**
	 * 获取返回的编辑信息
	 * 
	 * @param msg
	 */
	private void getResponseEdit(Message msg) {
		String response = (String) msg.obj;
		if (null != response) {
			String code = JsonUtil.msgJson("code", response);
			if (null != code && "200".equals(code)) {
				Utils.showToast(EditConsigneeAddressActivity.this, "编辑地址成功",
						Toast.LENGTH_SHORT);
				setResult(1, new Intent().putExtra("isTrue", true));
				EditConsigneeAddressActivity.this.finish();
				AnimationUtil.finishHaveFloatActivityAnimation(this);
			}
		}
	}
	
	/**
	 * 获取删除返回数据
	 * @param msg
	 */
	private void getResponseDelete(Message msg) {
		String response = (String) msg.obj;
		if (null != response) {
			String code = JsonUtil.msgJson("code", response);
			if (null != code && "200".equals(code)) {
				Utils.showToast(EditConsigneeAddressActivity.this, "删除地址成功",
						Toast.LENGTH_SHORT);
				setResult(1, new Intent().putExtra("isTrue", true));
				EditConsigneeAddressActivity.this.finish();
				AnimationUtil.finishHaveFloatActivityAnimation(this);
			}
		}
//		Utils.showToast(EditConsigneeAddressActivity.this, "删除地址失败",
//				Toast.LENGTH_SHORT);
		
		
	}

	/**
	 * 设置默认地址checkbox监听
	 */
	class DefaultChenkBox implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {

			if (buttonView == mMorenCb && isChecked) {
				isSelectDefault = true;
			} else if (buttonView == mMorenCb && !isChecked) {
				isSelectDefault = false;
			}
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
//		mTitleBarRl.setVisibility(View.VISIBLE);
		mNetWorkLayout.setVisibility(View.GONE);
	}
	
	@Override
	public void noNetWork() {
		mNetWorkLayout.setVisibility(View.VISIBLE);
//		mTitleBarRl.setVisibility(View.GONE);
	}
	@Override
	protected void onStart() {
		super.onStart();
		LogTest.LogMsg(TAG, "EditConsigneeAddressActivity-onStart");
	}

	@Override
	protected void onResume() {
		super.onResume();
		LogTest.LogMsg(TAG, "EditConsigneeAddressActivity-onResume");
	}

	@Override
	protected void onPause() {
		super.onPause();
		LogTest.LogMsg(TAG, "EditConsigneeAddressActivity-onPause");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		LogTest.LogMsg(TAG, "EditConsigneeAddressActivity-onDestroy");
	}

}
