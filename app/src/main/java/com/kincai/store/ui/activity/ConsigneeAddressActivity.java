package com.kincai.store.ui.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.kincai.store.Constants;
import com.kincai.store.R;
import com.kincai.store.bean.AddressInfo;
import com.kincai.store.bean.UserInfo;
import com.kincai.store.thread.CachedThreadPoolUtils;
import com.kincai.store.thread.HttpPostThread;
import com.kincai.store.ui.activity.base.BaseSlideActivity;
import com.kincai.store.utils.AnimationUtil;
import com.kincai.store.utils.JsonUtil;
import com.kincai.store.utils.LogTest;
import com.kincai.store.utils.NetWorkUtil;
import com.kincai.store.view.adapter.AddressListAdapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 * 
 * @author kincai
 * 
 * @description 收货地址列表
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.ui
 *
 * @time 2015-7-11 下午5:02:22
 *
 */
public class ConsigneeAddressActivity extends BaseSlideActivity implements
		 OnItemClickListener {
	private static final String TAG = "ConsigneeAddressActivity";
	private TextView mTitleTv;
	private ListView mAddressListView;
	private Button mAddAddressBtn;
	
	private LinearLayout mLoadingProgressLl;
	private ProgressBar mProgressBar;

	private MyHandler mMyHandler;
	private String mUserId;
	private AddressListAdapter mAddressListAdapter;
	private List<AddressInfo> mAddressInfo;

	
	@Override
	public int initContentView() {
		return R.layout.activity_consignee_address_layout;
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
		mMyHandler = new MyHandler();
	}

	/**
	 * 初始化控件
	 */
	public void initView() {
		super.initView();
		mLoadingProgressLl = (LinearLayout) findViewById(R.id.loading_progress_ll);
		mProgressBar = (ProgressBar) findViewById(R.id.loading_progress_p);
		
		mTitleTv = (TextView) findViewById(R.id.title_tv);
		mAddressListView = (ListView) findViewById(R.id.address_lv);
		mAddAddressBtn = (Button) findViewById(R.id.address_add_btn);

		mTitleTv.setText(getResources().getString(
				R.string.consignee_address_str));
	}

	/**
	 * 设置监听
	 */
	public void setListener() {
		super.setListener();
		mAddAddressBtn.setOnClickListener(this);
		mAddressListView.setOnItemClickListener(this);
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		List<UserInfo> mUserInfos = mBaseUserDao.getUserInfoOne(mSp.getUsername());
		if (null != mUserInfos) {
			mUserId = String.valueOf(mUserInfos.get(0).getUserId());
			// getImageData();
			loadData(mUserId, Constants.KINCAI_TYPE_GET, true);
		}

	}

	/**
	 * 加载数据
	 */
	private void loadData(String userId, String type, boolean isLoad) {
		if (NetWorkUtil.isNetworkAvailable(ConsigneeAddressActivity.this)) {

			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			parameters.add(new BasicNameValuePair("userId", userId));
			parameters.add(new BasicNameValuePair("type", type));
			
			if(isLoad) {
				mLoadingProgressLl.setVisibility(View.VISIBLE);
				mProgressBar.setVisibility(View.VISIBLE);
			}
			
			CachedThreadPoolUtils.execute(new HttpPostThread(mMyHandler,
					Constants.API_URL + "appAddressApi.php", parameters,
					"getAddress"));
		}
	}

	private void setData(List<AddressInfo> datas) {
		if (null != datas) {
			if (mAddressListAdapter == null) {

				// mSearchAfterLv.setInterface(this);
				mAddressListAdapter = new AddressListAdapter(
						ConsigneeAddressActivity.this, datas);
				mAddressListView.setAdapter(mAddressListAdapter);
			} else {
				LogTest.LogMsg(TAG, "eeeeeee");
				mAddressListAdapter.onDateChange(datas);
			}
		} else {
			if (mAddressListAdapter == null) {

				// mSearchAfterLv.setInterface(this);
				mAddressListAdapter = new AddressListAdapter(
						ConsigneeAddressActivity.this, datas);
				mAddressListView.setAdapter(mAddressListAdapter);
			} else {
				LogTest.LogMsg(TAG, "eeeeeee");
				mAddressListAdapter.onDateChange(datas);
			}
		}

	}

	@SuppressLint("HandlerLeak")
	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 2203:
				mLoadingProgressLl.setVisibility(View.GONE);
				mProgressBar.setVisibility(View.GONE);
				getAddressData(msg);
				break;

			}
		}
	}

	private void getAddressData(Message msg) {
		String response = (String) msg.obj;
		if (null != response) {
			String code = JsonUtil.msgJson("code", response);
			if ("200".equals(code)) {
				List<AddressInfo> addressInfo = JsonUtil.getAddress("data",
						response);
				if (null != addressInfo) {
					setData(addressInfo);
					mAddressInfo = addressInfo;
				} else {
				}

			} else if ("402".equals(code)) {
				setData(null);
			}

		} else {

		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.network_abnormal_top_layout:
			startActivity(new Intent(Settings.ACTION_SETTINGS));
			AnimationUtil.startHaveSinkActivityAnimation(this);
			break;
		case R.id.titlebar_back_iv:
			finish();
			AnimationUtil.finishHaveFloatActivityAnimation(this);
			break;
		case R.id.address_add_btn:
			startActivityForResult(new Intent(ConsigneeAddressActivity.this,
					AddConsigneeAddressActivity.class), 0);
			AnimationUtil.startHaveSinkActivityAnimation(this);
			break;

		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (null != data) {
			boolean isTrue = data.getBooleanExtra("isTrue", false);
			if (isTrue) {
				if (0 == requestCode && 1 == resultCode) {
					loadData(mUserId, Constants.KINCAI_TYPE_GET, false);
				}
			}

		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		LogTest.LogMsg(TAG, "position " + position);
		if (mAddressInfo != null) {
			LogTest.LogMsg(TAG,
					"mAddress position "
							+ mAddressInfo.get(position).getDetailedAddress());
			Intent intent = new Intent(ConsigneeAddressActivity.this, EditConsigneeAddressActivity.class);
			intent.putExtra("addressInfo", mAddressInfo.get(position));
			intent.putExtra("userId", mUserId);
			startActivityForResult(intent, 0);
			AnimationUtil.startHaveSinkActivityAnimation(this);
		}
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
		// TODO Auto-generated method stub
		super.onStart();
		LogTest.LogMsg(TAG, "MyCollectActivity-onStart");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		LogTest.LogMsg(TAG, "MyCollectActivity-onResume");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		LogTest.LogMsg(TAG, "MyCollectActivity-onPause");
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		LogTest.LogMsg(TAG, "MyCollectActivity-onDestroy");
	}
}
