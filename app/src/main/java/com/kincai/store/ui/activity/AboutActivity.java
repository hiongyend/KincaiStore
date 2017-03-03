package com.kincai.store.ui.activity;

import android.content.Intent;
import android.provider.Settings;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kincai.store.Constants;
import com.kincai.store.R;
import com.kincai.store.ui.activity.base.BaseSlideActivity;
import com.kincai.store.utils.AnimationUtil;
import com.kincai.store.utils.LogTest;
import com.kincai.store.utils.Utils;

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
 * @package com.kincai.store.ui.activity
 *
 * @time 2015-9-22 下午3:48:42
 *
 */
public class AboutActivity extends BaseSlideActivity {

	private static final String TAG = "AboutActivity";

	private TextView mTitleTv, mVersionTv;
	private RelativeLayout mGuidePageRl, mContactUsRl, mUseAgreementRl, mAttentionToOurRl, mExplainRl;
	
	
	@Override
	public int initContentView() {
		LogTest.LogMsg(TAG, "AboutActivity-onCreate");
		return R.layout.activity_about_layout;
	}
	@Override
	public void initDatas() {
		initView();
		initData();
		setListener();
	}

	@Override
	public void initView() {
		super.initView();
		mTitleTv = (TextView) findViewById(R.id.title_tv);
		mVersionTv = (TextView) findViewById(R.id.about_version_tv);
		mGuidePageRl = (RelativeLayout) findViewById(R.id.about_guide_rl);
		mContactUsRl = (RelativeLayout) findViewById(R.id.about_contact_us_rl);
		mUseAgreementRl = (RelativeLayout) findViewById(R.id.about_agreement_rl);
		mAttentionToOurRl = (RelativeLayout) findViewById(R.id.about_attention_our_rl);
		mExplainRl = (RelativeLayout) findViewById(R.id.about_explain_rl);
	}
	
	@Override
	public void setListener() {
		super.setListener();
		mGuidePageRl.setOnClickListener(this);
		mContactUsRl.setOnClickListener(this);
		mUseAgreementRl.setOnClickListener(this);
		mAttentionToOurRl.setOnClickListener(this);
		mExplainRl.setOnClickListener(this);
	}
	
	/**
	 * 初始化数据
	 */
	private void initData() {
		mTitleTv.setText(getString(R.string.about_us_str));
		String versionName = Utils.getVersionName(this);
		mVersionTv.setText(versionName != null ? "当前版本V"+ versionName : "");
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
		case R.id.about_guide_rl:
			startActivity(new Intent(this, GuideActivity.class).putExtra("isGuideFirst", false));
			AnimationUtil.startHaveSinkActivityAnimation(this);
			break;
		case R.id.about_contact_us_rl:
			
			startActivity(new Intent(this, ContactUsActivity.class));
			AnimationUtil.startHaveSinkActivityAnimation(this);
			
			break;
		case R.id.about_agreement_rl:
			
			startActivity(new Intent(this, WebviewActivity.class).putExtra(
					Constants.WEBVIEW_URL, Constants.SERVER_URL+"app/use_agreement.html"));
			AnimationUtil.startHaveSinkActivityAnimation(this);
			break;
		case R.id.about_attention_our_rl:
			startActivity(new Intent(this, AttentionToOurWXActivity.class));
			AnimationUtil.startHaveSinkActivityAnimation(this);

			break;
		case R.id.about_explain_rl:
			
			startActivity(new Intent(this, WebviewActivity.class)
					.putExtra(Constants.WEBVIEW_URL,  Constants.SERVER_URL+"app/explain.html"));
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
		LogTest.LogMsg(TAG, "AboutActivity-onRestart");
		super.onRestart();
	}

	@Override
	protected void onStart() {
		LogTest.LogMsg(TAG, "AboutActivity-onStart");
		super.onStart();
	}

	@Override
	protected void onResume() {
		LogTest.LogMsg(TAG, "AboutActivity-onResume");
		super.onResume();
	}

	@Override
	protected void onPause() {
		LogTest.LogMsg(TAG, "AboutActivity-onPause");
		super.onPause();
	}

	@Override
	protected void onStop() {
		LogTest.LogMsg(TAG, "AboutActivity-onStop");
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		LogTest.LogMsg(TAG, "AboutActivity-onDestroy");
		super.onDestroy();
	}
	
	
	

}
