package com.kincai.store.ui.activity;

import android.content.Intent;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kincai.store.R;
import com.kincai.store.ui.activity.base.BaseSlideActivity;
import com.kincai.store.utils.AnimationUtil;
import com.kincai.store.utils.LogTest;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 *
 * @author kincai
 * 
 * @description 关注天天购物微信
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.ui.activity
 *
 * @time 2015-9-23 上午7:41:21
 *
 */
public class AttentionToOurWXActivity extends BaseSlideActivity {
	
	private static final String TAG = "AttentionToOurWXActivity";
	private TextView mTitleTv;
	private Button mAtOnceAttentionBtn, mCencelBtn;
	
	/** APP_KEY*/
    private static final String APP_KEY = "wxf3d79dbf95573fe0";
    /** 微信API*/
    private IWXAPI mApi;
		
	@Override
	public int initContentView() {
		return R.layout.activity_attention_to_our_wx_layout;
	}
	@Override
	public void initDatas() {
		init();
		initView();
		setListener();
	}
	
	private void init() {
		mApi = WXAPIFactory.createWXAPI(this, APP_KEY);
		// 将APP_ID注册到微信中
		mApi.registerApp(APP_KEY);
	}
	
	@Override
	public void initView() {
		super.initView();
		mTitleTv = (TextView) findViewById(R.id.title_tv);
		mAtOnceAttentionBtn = (Button) findViewById(R.id.at_once_attention_btn);
		mCencelBtn = (Button) findViewById(R.id.cencel_btn);
		mTitleTv.setText(getString(R.string.attention_to_our_str));
	}
	@Override
	public void setListener() {
		super.setListener();
		mAtOnceAttentionBtn.setOnClickListener(this);
		mCencelBtn.setOnClickListener(this);
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
		case R.id.at_once_attention_btn:
			// 成功打开返回true 否则返回false
			if(mApi.openWXApp()) finish();
			break;
		case R.id.cencel_btn:
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
		LogTest.LogMsg(TAG, "AttentionToOurWXActivity-onRestart");
		super.onRestart();
	}

	@Override
	protected void onStart() {
		LogTest.LogMsg(TAG, "AttentionToOurWXActivity-onStart");
		super.onStart();
	}

	@Override
	protected void onResume() {
		LogTest.LogMsg(TAG, "AttentionToOurWXActivity-onResume");
		super.onResume();
	}

	@Override
	protected void onPause() {
		LogTest.LogMsg(TAG, "AttentionToOurWXActivity-onPause");
		super.onPause();
	}

	@Override
	protected void onStop() {
		LogTest.LogMsg(TAG, "AttentionToOurWXActivity-onStop");
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		LogTest.LogMsg(TAG, "AttentionToOurWXActivity-onDestroy");
		super.onDestroy();
	}
	
	
}
