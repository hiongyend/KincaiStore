package com.kincai.store.ui.viewpager.page;

import android.content.Context;
import android.view.View;

import com.kincai.store.R;
import com.kincai.store.ui.viewpager.base.BasePager;
import com.kincai.store.utils.LogTest;

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
public class OrderAllOrderPage extends BasePager {
	private static final String TAG = "OrderAllOrderPage";
	
	public OrderAllOrderPage(Context context, String userId, String type) {
		super(context, userId, type);
		LogTest.LogMsg(TAG, TAG+"-被调用");
	}

	@Override
	public View initView() {
		LogTest.LogMsg(TAG, "initView-被调用");
		View view = View.inflate(mContext, R.layout.pager_order_all_layout, null);
		
		return view;
	}
	
	
}
