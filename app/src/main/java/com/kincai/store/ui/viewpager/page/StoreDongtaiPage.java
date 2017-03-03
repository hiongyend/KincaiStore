package com.kincai.store.ui.viewpager.page;

import android.content.Context;
import android.view.View;

import com.kincai.store.R;
import com.kincai.store.ui.activity.StoreActivity;
import com.kincai.store.ui.viewpager.base.StoreBasePager;

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
public class StoreDongtaiPage extends StoreBasePager {

	public StoreDongtaiPage(Context context,int type, String storeId) {
		super(context, type,storeId);
	}

	@Override
	public View initView() {
		return View.inflate(mContext, R.layout.pager_store_dongtai_layout, null);
	}

	@Override
	public void initData() {
		if (mContext instanceof StoreActivity) {
			((StoreActivity) mContext).setTopRlVisibility(true);
		}
	}
	
}
