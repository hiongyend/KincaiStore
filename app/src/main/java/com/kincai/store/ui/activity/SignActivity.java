package com.kincai.store.ui.activity;

import com.kincai.store.ui.activity.base.BaseSlideActivity;

import android.view.View;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 *
 * @author kincai
 * 
 * @description 签到
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.ui
 *
 * @time 2015-7-24 下午7:30:43
 *
 */
public class SignActivity extends BaseSlideActivity {
	

	@Override
	public int initContentView() {
		return 0;
	}
	@Override
	public void initDatas() {
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
	public void onClick(View v) {
		
	}
}
