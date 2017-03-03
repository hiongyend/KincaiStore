package com.kincai.store.ui.viewpager.page;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.kincai.store.Constants;
import com.kincai.store.R;
import com.kincai.store.bean.StoreNewProInfo;
import com.kincai.store.bean.StoreNewProInfo.NewProData;
import com.kincai.store.model.IReflashListener;
import com.kincai.store.thread.CachedThreadPoolUtils;
import com.kincai.store.thread.HttpGetThread;
import com.kincai.store.ui.activity.StoreActivity;
import com.kincai.store.ui.viewpager.base.StoreBasePager;
import com.kincai.store.utils.GsonTools;
import com.kincai.store.utils.NetWorkUtil;
import com.kincai.store.utils.Utils;
import com.kincai.store.view.adapter.StoreNewProAdapter;
import com.kincai.store.view.custom.RefreshExpandableListView;

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
public class StoreNewProPage extends StoreBasePager implements IReflashListener{

	private RefreshExpandableListView mListview;
	private StoreNewProAdapter mProListAdapter;
	private MyHandler mMyHandler;
	
	//全部
	private List<NewProData> mAllProInfos;
	private List<NewProData> mAllNewProInfos;
	/** 当前加载页 */
	private static int currentPage = 1;
	private boolean isHavaNextPage;
	private String flag;
	private boolean isFirst = true;
	private boolean isHaveData;

//	private LoadingDialog mLoadingDialog;
	public StoreNewProPage(Context context, int type, String storeId) {
		super(context, type,storeId);
	}

	@Override
	public View initView() {
		return View.inflate(mContext, R.layout.pager_store_new_layout, null);
	}

	@Override
	public void initData() {
//		mLoadingDialog = ((StoreActivity)mContext).mLoadingDialog;
		if (mContext instanceof StoreActivity) {
			((StoreActivity) mContext).setTopRlVisibility(true);
		}
		if(isFirst) {
			mMyHandler = new MyHandler();
			mListview = (RefreshExpandableListView) mStoreBaseView.findViewById(R.id.store_lv);
			mListview.setOnRefreshListener(this);
		} 
		isFirst = false;
		
		if(!isHaveData)
		loadProData(1,1);
	}
	
	private void loadProData(int page,int type) {
		// TODO 加载全部商品
		if(NetWorkUtil.isNetworkAvailable(mContext)) {
			CachedThreadPoolUtils.execute(new HttpGetThread(mMyHandler, 
					new StringBuffer().append(Constants.API_URL)
					.append("appGetStoreNewProApi.php?storeId=").append(mStoreId)
					.append("&page=").append(page).append("&pageSize=")
					.append(Constants.GET_PAGESIZE).append("&type=")
					.append(type).toString()
					, "getStoreNewProdata"));
		}

	}
	
	public void setSearchData(List<NewProData> datas) {

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
	private void refreshList(List<NewProData> datas) {
		if (mProListAdapter == null) {

			mProListAdapter = new StoreNewProAdapter(mContext, datas);
			mListview.setAdapter(mProListAdapter);
		} else {
			mProListAdapter.onDateChange(datas);
		}

		expandGroup();
	}

	private void expandGroup() {

		int size = mProListAdapter.getGroupCount();
		for (int i = 0; i < size; i++) {
			mListview.expandGroup(i);
		}
	}
	
	@SuppressLint("HandlerLeak")
	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 2131:
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
	
	private List<NewProData> getResponseLoadStoreProData(Message msg) {
		/*if(mLoadingDialog != null && mLoadingDialog.isShow()) {
			mLoadingDialog.dismiss();
		}
		*/
		String response = (String) msg.obj;
		if(response != null) {
			StoreNewProInfo storeNewProInfo = GsonTools.changeGsonToBean(response, StoreNewProInfo.class);
			if("next".equals(storeNewProInfo.getMessage())) {
				isHavaNextPage = true;
			} else if("nonext".equals(storeNewProInfo.getMessage())){
				isHavaNextPage = false;
			}
			if(200 == storeNewProInfo.getCode()) {
				isHaveData = true;
				return storeNewProInfo.getNewProData();
				
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

