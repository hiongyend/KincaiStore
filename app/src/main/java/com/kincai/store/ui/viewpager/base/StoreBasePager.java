package com.kincai.store.ui.viewpager.base;

import com.kincai.store.utils.LogTest;

import android.content.Context;
import android.view.View;

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
 * @package com.kincai.store.ui.viewpager.base
 *
 * @time 2015-8-24 下午9:27:29
 *
 */
public abstract class StoreBasePager {
	private static final String TAG = "StoreBasePager";
	
	public Context mContext;
	public View mStoreBaseView;
	int type;
	public String mStoreId;
	public StoreBasePager(Context context, int type, String storeId) {
		mContext = context;
		this.type = type;
		mStoreId = storeId;
		mStoreBaseView = initView();
		LogTest.LogMsg(TAG, TAG+"-被调用");
	}
	
	public abstract View initView();
	
	/**
	 * 初始化数据
	 */
	public abstract void initData();
	
	
	/**
	 * 设置view的可见性
	 * @param visibility
	 * @param views
	 */
	public void setViewVisibility(boolean visibility, View...views ){
		for(View view : views) {
			view.setVisibility(visibility ? View.VISIBLE : View.GONE);
		}
	}
	
	
	
	
}
