package com.kincai.store.ui.viewpager.page;

import android.content.Context;
import android.view.View;

import com.kincai.store.R;
import com.kincai.store.ui.viewpager.base.BasePager;

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
 * @time 2015-8-24 下午9:27:52
 *
 */
public class OrderNoReceivePage extends BasePager {

	public OrderNoReceivePage(Context context, String userId, String type) {
		super(context, userId, type);
	}

	@Override
	public View initView() {
		View view = View.inflate(mContext, R.layout.pager_order_no_receive_layout, null);
		return view;
	}
	
	
}
