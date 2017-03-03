package com.kincai.store.ui.activity;

import android.content.Intent;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;

import com.kincai.store.R;
import com.kincai.store.ui.activity.base.BaseSlideActivity;
import com.kincai.store.utils.AnimationUtil;
import com.kincai.store.utils.LogTest;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 *
 * @author kincai
 * 
 * @description 联系我们
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.ui.activity
 *
 * @time 2015-9-22 下午10:00:35
 *
 */
public class ContactUsActivity extends BaseSlideActivity {
	
	private static final String TAG = "ContactUsActivity";
	private TextView mTitleTv;

	
	@Override
	public int initContentView() {
		return R.layout.activity_contact_us_layout;
	}
	@Override
	public void initDatas() {
		initView();
		setListener();
	}
	
	@Override
	public void initView() {
		super.initView();
		mTitleTv = (TextView) findViewById(R.id.title_tv);
		mTitleTv.setText(getString(R.string.contact_us_str));
	}
	@Override
	public void setListener() {
		super.setListener();
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
	public void netWork() {
		mNetWorkLayout.setVisibility(View.GONE);
	}

	@Override
	public void noNetWork() {
		mNetWorkLayout.setVisibility(View.VISIBLE);
	}

	
	@Override
	protected void onRestart() {
		LogTest.LogMsg(TAG, "ContactUsActivity-onRestart");
		super.onRestart();
	}

	@Override
	protected void onStart() {
		LogTest.LogMsg(TAG, "ContactUsActivity-onStart");
		super.onStart();
	}

	@Override
	protected void onResume() {
		LogTest.LogMsg(TAG, "ContactUsActivity-onResume");
		super.onResume();
	}

	@Override
	protected void onPause() {
		LogTest.LogMsg(TAG, "ContactUsActivity-onPause");
		super.onPause();
	}

	@Override
	protected void onStop() {
		LogTest.LogMsg(TAG, "ContactUsActivity-onStop");
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		LogTest.LogMsg(TAG, "ContactUsActivity-onDestroy");
		super.onDestroy();
	}
	
	
}
