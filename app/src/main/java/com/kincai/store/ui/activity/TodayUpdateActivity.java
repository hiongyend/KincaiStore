package com.kincai.store.ui.activity;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.kincai.store.Constants;
import com.kincai.store.R;
import com.kincai.store.bean.ProInfo;
import com.kincai.store.model.IReflashListener;
import com.kincai.store.thread.CachedThreadPoolUtils;
import com.kincai.store.thread.HttpGetThread;
import com.kincai.store.ui.activity.base.BaseSlideActivity;
import com.kincai.store.utils.AnimationUtil;
import com.kincai.store.utils.JsonUtil;
import com.kincai.store.utils.LogTest;
import com.kincai.store.utils.NetWorkUtil;
import com.kincai.store.view.adapter.HomeProListAdapter;
import com.kincai.store.view.custom.HomeListview;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 *
 * @author kincai
 * 
 * @description 今日更新
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.ui.activity
 *
 * @time 2015-9-21 上午12:11:05
 *
 */
public class TodayUpdateActivity extends BaseSlideActivity implements
	OnItemClickListener, IReflashListener {
	
	private static final String TAG = "TodayUpdateActivity";
	private TextView mTitleTv;
	private LinearLayout mLoadingProgressLl;
	private HomeListview mListview;
	private HomeProListAdapter mProListAdapter;
	private MyHandler mMyHandler;
	
	/** 当前加载页 */
	private static int currentPage = 1;
	private boolean isHavaNextPage;
	
	private List<ProInfo> mProInfos;
	private List<ProInfo> mNewInfos;
	

	@Override
	public int initContentView() {
		return R.layout.activity_today_update_layout;
	}
	@Override
	public void initDatas() {
		init();
		initView();
		loadData(1, true);
		initData();
		setListener();
	}
	
	private void init() {
		mMyHandler = new MyHandler();
	}
	
	@Override
	public void initView() {
		super.initView();
		mTitleTv = (TextView) findViewById(R.id.title_tv);
		mLoadingProgressLl = (LinearLayout) findViewById(R.id.loading_progress_ll);
		mListview = (HomeListview) findViewById(R.id.today_lv);
		
	}
	
	private void initData() {
		
		mTitleTv.setText(getString(R.string.today_update_str));
		
	}
	
	private void loadData(int page, boolean isLoadDialog) {
		if(NetWorkUtil.isNetworkAvailable(this)) {
			mListview.setVisibility(View.GONE);
			if(isLoadDialog) {
				mLoadingProgressLl.setVisibility(View.VISIBLE);
			} else {
				mListview.setVisibility(View.VISIBLE);
			}
			
			
			CachedThreadPoolUtils.execute(new HttpGetThread(mMyHandler,
					new StringBuffer().append(
					Constants.API_URL).append("appGetTodayProApi.php?page=")
					.append(page).append("&pageSize=")
					.append(Constants.GET_PAGESIZE).toString(), "integralStore"));
			
		}
	}
	
	
	/**
	 * 显示搜索数据
	 * 
	 * @param datas
	 */
	public void setSearchData(List<ProInfo> datas) {

			mLoadingProgressLl.setVisibility(View.GONE);
		if (datas != null) {
			mListview.setVisibility(View.VISIBLE);

			if (mProListAdapter == null) {

				mProListAdapter = new HomeProListAdapter(
						this, datas);
				mListview.setAdapter(mProListAdapter);
			} else {
				LogTest.LogMsg(TAG, "eeeeeee");
				mProListAdapter.onDateChange(datas);
			}
		} else {
		}
	}
	
	@SuppressLint("HandlerLeak")
	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 2112:
				mListview.onRefreshComplete();
				disposePageLoadData(msg);
				break;
//			case 2215:
//				getResponseImagePath(msg);
//				break;

			}
		};
	}
	
	/**
	 * 获取返回的商品图片路径
	 * @param msg
	 */
//	private void getResponseImagePath(Message msg) {
//		String response = (String) msg.obj;
//
//		if (null != response) {
//			String code = JsonUtil.msgJson("code", response);
//			if(null != code && "200".equals(code)) {
//				mNewImagePathInfos = JsonUtil.getImgPath("data", response);
//					
//			}
//			
//		}
//		if(null != mImagePathInfos) {
//			if(mNewImagePathInfos != null && mNewImagePathInfos.size() > 0) {
//				mImagePathInfos.addAll(mNewImagePathInfos);
//			}
//			
//			//直接上面addAll就行了 不用一个一个添加 暂时不用这里的
////			ProCommon.addProImagePathList(mAllProImagePath, mSearchProImagePath);
//		} else {
//			mImagePathInfos = mNewImagePathInfos;
//		}
//		
//		
//		
////		mAllProImagePath = ProImage
////				.getProImagePath(mAllProImagePath, mSearchProList);
//
//		setSearchData(mProInfos, mImagePathInfos);
//		
//	}
	
	/**
	 * 处理判断分页加载数据
	 * 
	 * @param msg
	 */
	private void disposePageLoadData(Message msg) {
		if (null == mProInfos) {
			mProInfos = getResponseIntegralData(msg);
			
//			ProCommon.getProImagePath(this, mMyHandler, mProInfos);
			if(null != mProInfos) {
//				currentGetPageSize = mProInfos.size();
			}
			
		} else {
			mNewInfos = getResponseIntegralData(msg);
			if (null != mNewInfos && mNewInfos.size() > 0) {
				mProInfos.addAll(mNewInfos);
				currentPage++;
//				currentGetPageSize = mNewInfos.size();
				LogTest.LogMsg(TAG, "currentPage " + currentPage);
			}
			
			//直接上面addAll就行了 不用一个一个添加 暂时不用这里的
//			mSearchProList = ProCommon.addProList(mSearchProList, mNewProList);
//			ProCommon.getProImagePath(this, mMyHandler,mNewInfos);
		}
		
//		if(null == mProInfos) {
//			
//			mImagePathInfos = null;
			setSearchData(mProInfos);
//		}
		
//		//加载图片
//		proImageReturn = ProImage.getProImagePath(getActivity(), mMyHandler, mAllProList);
		
	}
	
	
	private List<ProInfo> getResponseIntegralData(Message msg) {
		String response = (String) msg.obj;

		if (null != response) {
			String code = JsonUtil.msgJson("code", response);
			String message = JsonUtil.msgJson("message", response);
			if("next".equals(message)) {
				isHavaNextPage = true;
			} else if("nonext".equals(message)){
				isHavaNextPage = false;
			}
			
			if (code != null && "200".equals(code)) {
				List<ProInfo> searchDataList = JsonUtil
						.getPro("data", response);
				if (null != searchDataList) {
					return searchDataList;
				}
			}

		}

		return null;
	}
	
	@Override
	public void setListener() {
		super.setListener();
		mListview.setOnRefreshListener(this);
		mListview.setOnItemClickListener(this);
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

		}
	}

	
	@Override
	public void onReflash() {

		if (NetWorkUtil.isNetworkAvailable(this)) {
			mProInfos = null;
			currentPage = 1;
			loadData(1,false);
		} else {
			mListview.onRefreshComplete();
		}
	}
	
	@Override
	public void onLoadMore() {
		if (NetWorkUtil.isNetworkAvailable(this)
				&& isHavaNextPage) {//当获取到的每页数据数等于指定的每页显示数 则 还有数据(其实这样判断不好 忽略刚刚好可以整除时候问题)
			loadData(currentPage+1,false);
		} else {
			mListview.onRefreshComplete();
		}		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if(mProInfos != null) {
			LogTest.LogMsg(TAG, "position "+position);
			startActivity(new Intent(this, ProDetailsActivity.class).putExtra("pId", mProInfos.get(position-1).getId())
					.putExtra("storeId", mProInfos.get(position-1).getStoreId()));
			AnimationUtil.startHaveSinkActivityAnimation(this);
		}
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
	protected void onRestart() {
		LogTest.LogMsg(TAG, "TodayUpdateActivity-onRestart");
		super.onRestart();
	}

	@Override
	protected void onStart() {
		LogTest.LogMsg(TAG, "TodayUpdateActivity-onStart");
		super.onStart();
	}

	@Override
	protected void onResume() {
		LogTest.LogMsg(TAG, "TodayUpdateActivity-onResume");
		super.onResume();
	}

	@Override
	protected void onPause() {
		LogTest.LogMsg(TAG, "TodayUpdateActivity-onPause");
		super.onPause();
	}

	@Override
	protected void onStop() {
		LogTest.LogMsg(TAG, "TodayUpdateActivity-onStop");
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		LogTest.LogMsg(TAG, "TodayUpdateActivity-onDestroy");
		super.onDestroy();
	}


}
