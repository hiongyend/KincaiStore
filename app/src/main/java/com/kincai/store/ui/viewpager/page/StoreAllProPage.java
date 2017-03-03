package com.kincai.store.ui.viewpager.page;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.kincai.store.Constants;
import com.kincai.store.R;
import com.kincai.store.bean.ProInfo;
import com.kincai.store.model.IReflashListener;
import com.kincai.store.thread.CachedThreadPoolUtils;
import com.kincai.store.thread.HttpGetThread;
import com.kincai.store.ui.activity.StoreActivity;
import com.kincai.store.ui.viewpager.base.StoreBasePager;
import com.kincai.store.utils.JsonUtil;
import com.kincai.store.utils.NetWorkUtil;
import com.kincai.store.utils.Utils;
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
 * @description TODO
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.ui.viewpager.page
 *
 * @time 2015-8-24 下午9:27:36
 *
 */
public class StoreAllProPage extends StoreBasePager implements IReflashListener{
	private HomeListview mListview;
	private HomeProListAdapter mProListAdapter;
	private MyHandler mMyHandler;
	
	//全部
	private List<ProInfo> mAllProInfos;
	private List<ProInfo> mAllNewProInfos;
	/** 当前加载页 */
	private static int currentPage = 1;
	private boolean isHavaNextPage;
	private String flag;
	private boolean isFirst = true;
	private boolean isHaveData;

//	private LoadingDialog mLoadingDialog;
	public StoreAllProPage(Context context, int type, String storeId) {
		super(context, type,storeId);
	}

	@Override
	public View initView() {
		return View.inflate(mContext, R.layout.pager_store_all_layout, null);
	}

	@Override
	public void initData() {
		if (mContext instanceof StoreActivity) {
			((StoreActivity) mContext).setTopRlVisibility(true);
		}
//		mLoadingDialog = ((StoreActivity)mContext).mLoadingDialog;
		if(isFirst) {
			mMyHandler = new MyHandler();
			mListview = (HomeListview) mStoreBaseView.findViewById(R.id.store_lv);
			mListview.setOnRefreshListener(this);
		} 
		isFirst = false;
		
		if(!isHaveData)
		loadProData(1,1);
	}
	
	private void loadProData(int page,int type) {
		// 加载全部商品
		if(NetWorkUtil.isNetworkAvailable(mContext)) {
			CachedThreadPoolUtils.execute(new HttpGetThread(mMyHandler, 
					new StringBuffer().append(Constants.API_URL)
					.append("appGetStoreAllProApi.php?storeId=").append(mStoreId)
					.append("&page=").append(page).append("&pageSize=")
					.append(Constants.GET_PAGESIZE).append("&type=")
					.append(type).toString()
					, "getStoreProdata"));
		}

	}
	
	public void setSearchData(List<ProInfo> datas) {

		if (datas != null && datas.size() > 0) {

			refreshList(datas);
		} else if ("400".equals(flag)) {

			Utils.showToast(mContext, "加载失败", 0);
		}

	}

/**
 * 刷新界面
 * 
 * @param datas
 * @param Imagedatas
 */
private void refreshList(List<ProInfo> datas) {
	if (mProListAdapter == null) {

		mProListAdapter = new HomeProListAdapter(mContext, datas);
		mListview.setAdapter(mProListAdapter);
	} else {
		mProListAdapter.onDateChange(datas);
	}
	
}
	
	@SuppressLint("HandlerLeak")
	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 2127:
				mListview.onRefreshComplete();
				disposePageLoadData(msg);
				break;
			}
			
		}
	}
	
	/**
	 * 处理判断分页加载数据
	 * 
	 * @param msg
	 */
	private void disposePageLoadData(Message msg) {
		if (null == mAllProInfos) {
			mAllProInfos = getResponseLoadStoreProData(msg);
			
			if(null != mAllProInfos) {
			}
			
		} else {
			mAllNewProInfos = getResponseLoadStoreProData(msg);
			if (null != mAllNewProInfos && mAllNewProInfos.size() > 0) {
				mAllProInfos.addAll(mAllNewProInfos);
				currentPage++;
				
			}
			
		}
		
		setSearchData(mAllProInfos);

		
	}
	
	private List<ProInfo> getResponseLoadStoreProData(Message msg) {
		/*if(mLoadingDialog != null && mLoadingDialog.isShow()) {
			mLoadingDialog.dismiss();
		}
		*/
		String response = (String) msg.obj;
		if(response != null) {
			
			String code = JsonUtil.msgJson("code", response);
			String message = JsonUtil.msgJson("message", response);
			if("next".equals(message)) {
				isHavaNextPage = true;
			} else if("nonext".equals(message)){
				isHavaNextPage = false;
			}
			if("200".equals(code)) {
				isHaveData = true;
				return JsonUtil.getPro("data", response);
			} else {
				flag = "400";
			}
		} else {
			flag = "400";
		}
		
		return null;
	}

	@Override
	public void onReflash() {

		if(NetWorkUtil.isNetworkAvailable(mContext)) {
			mAllProInfos = null;
			currentPage = 1;
			
			loadProData(1, 1);
		} else {
			mListview.onRefreshComplete();
		}
		
	}

	@Override
	public void onLoadMore() {
		if(NetWorkUtil.isNetworkAvailable(mContext)
				&&isHavaNextPage) {
			loadProData(currentPage+1, 1);
		} else {
			mListview.onRefreshComplete();
		}
	}
	
}
