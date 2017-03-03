package com.kincai.store.ui.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.kincai.store.Constants;
import com.kincai.store.R;
import com.kincai.store.bean.ProInfo;
import com.kincai.store.bean.UserInfo;
import com.kincai.store.model.IDialog;
import com.kincai.store.model.IReflashListener;
import com.kincai.store.thread.CachedThreadPoolUtils;
import com.kincai.store.thread.HttpPostThread;
import com.kincai.store.ui.activity.base.BaseSlideActivity;
import com.kincai.store.utils.AnimationUtil;
import com.kincai.store.utils.JsonUtil;
import com.kincai.store.utils.LogTest;
import com.kincai.store.utils.NetWorkUtil;
import com.kincai.store.utils.Utils;
import com.kincai.store.view.adapter.HomeProListAdapter;
import com.kincai.store.view.custom.HomeListview;

import android.annotation.SuppressLint;
//import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
//import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 * 
 * @author kincai
 * 
 * @description 浏览历史
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.ui
 *
 * @time 2015-7-1 上午11:49:23
 *
 */
public class BrowseHistoryActivity extends BaseSlideActivity implements 
		IReflashListener, OnItemClickListener {
	private static final String TAG = "BrowseHistoryActivity";
	
	private TextView mTitleTv;
	private HomeListview mHistoryListView;
	private Button mClearAllBtn;
	

	private LinearLayout mLoadingProgressLl;
	private ProgressBar mProgressBar;


	private List<ProInfo> mSearchProList;
	private List<ProInfo> mNewProList;
	
	/** 当前加载页 */
	private static int currentPage = 1;
	private boolean isHavaNextPage;
	private HomeProListAdapter mBrowseListAdapter;
	
	private MyHandler mMyHandler;
	private String mUserId;
	private String flag;

	
	@Override
	public int initContentView() {
		
		return R.layout.activity_browse_history_layout;
	}
	@Override
	public void initDatas() {
		init();
		initView();
		initData();
		setListener();
	}

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
		mHistoryListView = (HomeListview) findViewById(R.id.browse_history_lv);
		mClearAllBtn = (Button) findViewById(R.id.browse_clear_all_history_btn);

		mTitleTv.setText(getResources().getString(
				R.string.my_browsing_history_str));
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		if (mSp.getUserIsLogin()) {
			List<UserInfo> mUserInfos = mBaseUserDao.getUserInfoOne(mSp.getUsername());
			if (null != mUserInfos) {
				mUserId = String.valueOf(mUserInfos.get(0).getUserId());
				// getImageData();
				loadData(mUserId, Constants.KINCAI_TYPE_GET, true, 1);
				// loadData(mUserId, Constants.KINCAI_COLLECT_OR_CART_GET);
			}
		} else {
//			mHistoryListView.setEmptyView(mEmptyView);
		}

	}

	/**
	 * 设置事件监听
	 */
	public void setListener() {
		super.setListener();
		mClearAllBtn.setOnClickListener(this);
		mHistoryListView.setOnItemClickListener(this);
		
		mHistoryListView.setOnRefreshListener(this);

	}

	/**
	 * 加载数据
	 */
	private void loadData(String userId, String type, boolean flag, int page) {
		if (NetWorkUtil.isNetworkAvailable(BrowseHistoryActivity.this)) {

			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			parameters.add(new BasicNameValuePair("userId", userId));
			parameters.add(new BasicNameValuePair("type", type));
			parameters.add(new BasicNameValuePair("page", String.valueOf(page)));
			parameters.add(new BasicNameValuePair("pageSize", String.valueOf(Constants.GET_PAGESIZE)));
			

			if (flag) {
				// mLoadingDialog.dialogShow();
				mLoadingProgressLl.setVisibility(View.VISIBLE);
				mProgressBar.setVisibility(View.VISIBLE);
			}

			CachedThreadPoolUtils.execute(new HttpPostThread(mMyHandler,
					new StringBuffer().append(Constants.API_URL).append("appBrowseHistoryApi.php").toString(),
					parameters, "getBrowse"));
		}
	}

	@SuppressLint("HandlerLeak")
	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 2103:
				break;

			case 2204:

				
				mHistoryListView.onRefreshComplete();
				disposePageLoadData(msg);

				
				break;
			case 2205:

				getClearAllData(msg);

				break;
//			case 2215:
//
//				getResponseImagePath(msg);
//				break;
			}
		}
	}
	
	/**
	 * 处理判断分页加载数据
	 * 
	 * @param msg
	 */
	private void disposePageLoadData(Message msg) {
		if (null == mSearchProList) {
			mSearchProList = getSearchData(msg);
			
			if(null != mSearchProList) {
			}
			
		} else {
			mNewProList = getSearchData(msg);
			if (null != mNewProList && mNewProList.size() > 0) {
				mSearchProList.addAll(mNewProList);
				currentPage++;
				
				LogTest.LogMsg(TAG, "currentPage " + currentPage);
			}
			
//			ProCommon.getProImagePath(mMainActivityContext, mMyHandler,mNewProList);
		}
		
		setSearchData(mSearchProList);

		
//		//加载图片
//		proImageReturn = ProImage.getProImagePath(mMainActivityContext, mMyHandler, mAllProList);
		
	}
	

//	private void getResponseImagePath(Message msg) {
//		String response = (String) msg.obj;
//
//		if (null != response) {
//			String code = JsonUtil.msgJson("code", response);
//			if (null != code) {
//				if ("200".equals(code)) {
//					mAllProImagePath = JsonUtil.getImgPath("data", response);
//
//				}
//			}
//
//		}
//
//		setSearchData(mSearchProList, mAllProImagePath);
//
//	}

	/**
	 * 删除历史返回数据
	 * 
	 * @param msg
	 */
	private void getClearAllData(Message msg) {
		String response = (String) msg.obj;
		if (null != response) {
			String code = JsonUtil.msgJson("code", response);
			if ("200".equals(code)) {
				// isEditAllList();
				// mTitleProNumTv.setText("(0)");
				initOb();
				loadData(mUserId, Constants.KINCAI_TYPE_GET, false, 1);
				// setSearchData(new ArrayList<ProInfo>(), new
				// ArrayList<ProImagePathInfo>());
			} else {
				mLoadingProgressLl.setVisibility(View.GONE);
				mProgressBar.setVisibility(View.GONE);
				// 删除失败
			}
		}

	}

	/**
	 * 显示搜索数据
	 * 
	 * @param datas
	 */
	public void setSearchData(List<ProInfo> datas) {

		mLoadingProgressLl.setVisibility(View.GONE);
		mProgressBar.setVisibility(View.GONE);

		LogTest.LogMsg(TAG, "data是否为空" + (datas == null));
		if (datas != null) {

			refreshData(datas);
		} else if ("402".equals(flag)) {
			
//			mHistoryListView.setEmptyView(mEmptyView);
			refreshData(datas);

			mHistoryListView.setVisibility(View.GONE);

		} else if ("403".equals(flag)) {
//			mHistoryListView.setEmptyView(mEmptyView);
		} else {
			
//			mHistoryListView.setEmptyView(mEmptyView);
		}
	}

//	private void setEmptyView() {
//		
//		mEmptyView = LayoutInflater.from(BrowseHistoryActivity.this).inflate(R.layout.empty_layout, null);
//	}
	
	/**
	 * 刷新数据
	 * 
	 * @param datas
	 * @param Imagedatas
	 */
	private void refreshData(List<ProInfo> datas) {
		if (mBrowseListAdapter == null) {

			mBrowseListAdapter = new HomeProListAdapter(
					BrowseHistoryActivity.this, datas);
			mHistoryListView.setAdapter(mBrowseListAdapter);

		} else {
			LogTest.LogMsg(TAG, "mBrowseListAdapter不为空");
			mBrowseListAdapter
					.onDateChange(datas);
		}
	}
	
	private void initOb() {
		mSearchProList = null;
		currentPage = 1;
	}

	/**
	 * 获取网络返回的数据
	 * 
	 * @param msg
	 * @return
	 */
	private List<ProInfo> getSearchData(Message msg) {
		String response = (String) msg.obj;

		
		if (null != response) {
			String code = JsonUtil.msgJson("code", response);
			String message = JsonUtil.msgJson("message", response);
			if("next".equals(message)) {
				isHavaNextPage = true;
			} else if("nonext".equals(message)){
				isHavaNextPage = false;
			}
			if ("200".equals(code)) {
				List<ProInfo> searchDataList = JsonUtil
						.getPro("data", response);
				
				if (null != searchDataList) {
					return searchDataList;

				}

			} else if ("402".equals(code)) {
				flag = "402";
				// return new ArrayList<ProInfo>();
			}

		}

		return null;

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

		case R.id.browse_clear_all_history_btn:
			if (mSearchProList != null && mSearchProList.size() > 0) {
				Utils.showDialog(BrowseHistoryActivity.this, false, "",
						mScreenWidth, "清空记录", "是否清空浏览记录", "取消", "确定",
						new IDialog() {

							@Override
							public void onDialogClick() {
								clearAllHistory();
							}
						});
			}
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		if (null != mSearchProList) {
			Intent intent = new Intent(BrowseHistoryActivity.this,
					ProDetailsActivity.class);
			intent.putExtra("pId", mSearchProList.get(position-1).getId());
			intent.putExtra("storeId", mSearchProList.get(position-1).getStoreId());
			startActivity(intent);
			AnimationUtil.startHaveSinkActivityAnimation(this);
		}

	}

	/**
	 * 
	 */
	private void clearAllHistory() {

		if (NetWorkUtil.isNetworkAvailable(BrowseHistoryActivity.this)) {
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			parameters.add(new BasicNameValuePair("userId", mUserId));
			parameters.add(new BasicNameValuePair("pId", "0"));

			mLoadingProgressLl.setVisibility(View.VISIBLE);
			mProgressBar.setVisibility(View.VISIBLE);
			CachedThreadPoolUtils.execute(new HttpPostThread(mMyHandler,
					Constants.API_URL + "appBrowseHistoryApi.php",
					parameters, "getBrowseClear"));
		}

	}
	
	@Override
	public void onReflash() {
		
		if(NetWorkUtil.isNetworkAvailable(BrowseHistoryActivity.this)) {
			initOb();
			loadData(mUserId, Constants.KINCAI_TYPE_GET, false, 1);
		} else {
			mHistoryListView.onRefreshComplete();
		}
		
	}
	
	@Override
	public void onLoadMore() {
		
		if (NetWorkUtil.isNetworkAvailable(BrowseHistoryActivity.this)
				&& isHavaNextPage) {
			loadData(mUserId, Constants.KINCAI_TYPE_GET, false, currentPage+1);
		} else {
			mHistoryListView.onRefreshComplete();
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
		super.onResume();
		LogTest.LogMsg(TAG, "MyCollectActivity-onResume");
	}

	@Override
	protected void onPause() {
		super.onPause();
		LogTest.LogMsg(TAG, "MyCollectActivity-onPause");
	}

	@Override
	protected void onStop() {
		super.onStop();

	}

	@Override
	protected void onDestroy() {
		Intent intent = new Intent();
		intent.setAction(Constants.BROWSE_HISTORY_CHANGE_ACTION);
		sendBroadcast(intent);
		LogTest.LogMsg(TAG, "MyCollectActivity-onDestroy");
		super.onDestroy();
		
	}

}
