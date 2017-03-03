package com.kincai.store.ui.activity;

import android.content.Intent;
import android.provider.Settings;
import android.view.View;

import com.kincai.store.R;
import com.kincai.store.ui.activity.base.BaseSlideActivity;
import com.kincai.store.utils.AnimationUtil;

public class BalanceActivity extends BaseSlideActivity {
	
	
	@Override
	public int initContentView() {
		return R.layout.activity_balance_layout;
	}
	
	
	@Override
	public void initDatas() {

	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.titlebar_back_iv:
			finish();
			AnimationUtil.finishHaveFloatActivityAnimation(this);
			break;
		case R.id.network_abnormal_top_layout:
			startActivity(new Intent(Settings.ACTION_SETTINGS));
			AnimationUtil.startHaveSinkActivityAnimation(this);
			break;
			
		}
		
	}
	

	@Override
	public void netWork() {

	}

	@Override
	public void noNetWork() {

	}

}
