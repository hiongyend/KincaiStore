package com.kincai.store.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kincai.store.Constants;
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
 * @description 服务与帮助
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.ui.activity
 *
 * @time 2015-9-29 上午9:28:41
 *
 */
public class HelpAndServeActivity extends BaseSlideActivity {
	private static final String TAG = "HelpAndServeActivity";
	private TextView titleTv;
	private RelativeLayout mRobotRl, mKefuRl, mHelpRl;

	
	@Override
	public int initContentView() {
		return R.layout.activity_help_and_serve_layout;
	}
	@Override
	public void initDatas() {
		initView();
		setListener();
	}

	@Override
	public void initView() {
		super.initView();
		titleTv = (TextView) findViewById(R.id.title_tv);
		mRobotRl = (RelativeLayout) findViewById(R.id.tt_robot_rl);
		mKefuRl = (RelativeLayout) findViewById(R.id.tt_kefu_rl);
		mHelpRl = (RelativeLayout) findViewById(R.id.help_rl);
		
		titleTv.setText(getString(R.string.help_serve_str));
	}

	@Override
	public void setListener() {
		super.setListener();
		mRobotRl.setOnClickListener(this);
		mKefuRl.setOnClickListener(this);
		mHelpRl.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.titlebar_back_iv:
			finish();
			AnimationUtil.finishHaveFloatActivityAnimation(this);
			break;
		case R.id.tt_robot_rl:
			startActivity(new Intent(this, TTRobotServeActivity.class));
			AnimationUtil.startHaveSinkActivityAnimation(this);
			break;
		case R.id.tt_kefu_rl:

			break;
		case R.id.help_rl:
			startActivity(new Intent(this, WebviewActivity.class)
				.putExtra(Constants.WEBVIEW_URL, Constants.SERVER_URL+"app/help_center/appHelp.html"));
			AnimationUtil.startHaveSinkActivityAnimation(this);
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
		LogTest.LogMsg(TAG, "HelpAndServeActivity-onRestart");
		super.onRestart();
	}

	@Override
	protected void onStart() {
		LogTest.LogMsg(TAG, "HelpAndServeActivity-onStart");
		super.onStart();
	}

	@Override
	protected void onResume() {
		LogTest.LogMsg(TAG, "HelpAndServeActivity-onResume");
		super.onResume();
	}

	@Override
	protected void onPause() {
		LogTest.LogMsg(TAG, "HelpAndServeActivity-onPause");
		super.onPause();
	}

	@Override
	protected void onStop() {
		LogTest.LogMsg(TAG, "HelpAndServeActivity-onStop");
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		LogTest.LogMsg(TAG, "HelpAndServeActivity-onDestroy");
		super.onDestroy();
	}
	
	

}
