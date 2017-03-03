package com.kincai.store.ui.activity;

import com.kincai.store.Constants;
import com.kincai.store.R;
import com.kincai.store.ui.activity.base.BaseSlideActivity;
import com.kincai.store.utils.AnimationUtil;
import com.kincai.store.utils.LogTest;
import com.kincai.store.view.custom.HorizontalProgressBar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 *
 * @author kincai
 * 
 * @description WebView加载网页
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.ui
 *
 * @time 2015-8-18 下午3:38:02
 *
 */
public class WebviewActivity extends BaseSlideActivity {
	
	private static final String TAG = "WebviewActivity";
	public ImageView mChoseIv;
	public ImageView mRefreshIv;
	public WebView mWebView;
	public TextView mTitleTv;
	private HorizontalProgressBar mProgressBar;
	public MyWebChromeClient mMyWebChromeClient;
	
	public String mUrl;
	
	
	@Override
	public int initContentView() {
		return R.layout.webview_layout;
	}
	@Override
	public void initDatas() {
		init();
		initView();
		loadWebData();
		setListener();
	}
	

	/**
	 * 初始化
	 */
	public void init() {
		Intent intent = getIntent();
		if (null != intent) {
			mUrl = intent.getStringExtra(Constants.WEBVIEW_URL);
		}
		mMyWebChromeClient = new MyWebChromeClient();
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	/**
	 * 初始化空间
	 */
	public void initView() {

		super.initView();
		mProgressBar = (HorizontalProgressBar) findViewById(R.id.webview_loading_progress);
		mChoseIv = (ImageView) findViewById(R.id.titlebar_chose_iv);
		mRefreshIv = (ImageView) findViewById(R.id.titlebar_refresh_iv);
		mTitleTv = (TextView) findViewById(R.id.title_tv);
		mWebView = (WebView) findViewById(R.id.web_view);

		WebSettings settings = mWebView.getSettings();
		settings.setJavaScriptEnabled(true);
		// WebView加载页面优先使用缓存加载
		settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		settings.setBlockNetworkImage(false);//图
//		settings.setBuiltInZoomControls(true);

	}
	
	/**
	 * 加载Web页面
	 */
	public void loadWebData() {

		if (null != mUrl) {
			mWebView.loadUrl(mUrl);
			mWebView.setWebViewClient(new WebViewClient() {
				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					view.loadUrl(url);
					return true;
				}
			});
		}

	}
	
	/**
	 * 设置事件监听
	 */
	public void setListener() {

		super.setListener();
		mChoseIv.setOnClickListener(this);
		mRefreshIv.setOnClickListener(this);

		mWebView.setWebChromeClient(mMyWebChromeClient);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mWebView.canGoBack()) {//判断能不能返回
				mWebView.goBack();// 返回上一页面
				return true;
			} else {
				this.finish();
				AnimationUtil.finishHaveFloatActivityAnimation(this);
				return true;
			}
		}
		
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.titlebar_back_iv:

			if (mWebView.canGoBack()) {// 判断能不能返回
				mWebView.goBack();// 返回上一页面
			} else {
				finish();
				AnimationUtil.finishHaveFloatActivityAnimation(this);
			}

			break;
		case R.id.titlebar_chose_iv:
			finish();
			AnimationUtil.finishHaveFloatActivityAnimation(this);
			break;
		case R.id.titlebar_refresh_iv:
			mProgressBar.setVisibility(View.VISIBLE);
			mWebView.reload();
			break;

		}
	}
	
	class MyWebChromeClient extends WebChromeClient {

		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			super.onProgressChanged(view, newProgress);
			mProgressBar.setProgress(newProgress);
			if(100 == newProgress) {
				mProgressBar.setVisibility(View.GONE);
			}
			
		}
		

		@Override
		public void onReceivedTitle(WebView view, String title) {
			super.onReceivedTitle(view, title);
			mTitleTv.setText(title);

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
		LogTest.LogMsg(TAG, "WebviewActivity-onRestart");
		super.onRestart();
	}

	@Override
	protected void onStop() {
		LogTest.LogMsg(TAG, "WebviewActivity-onStop");
		super.onStop();
	}

	@Override
	protected void onStart() {
		LogTest.LogMsg(TAG, "WebviewActivity-onStart");
		super.onStart();
		
	}

	@Override
	protected void onResume() {
		LogTest.LogMsg(TAG, "WebviewActivity-onResume");
		super.onResume();
	}

	@Override
	protected void onPause() {
		LogTest.LogMsg(TAG, "WebviewActivity-onPause");
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		LogTest.LogMsg(TAG, "WebviewActivity-onDestroy");
		super.onDestroy();
		

	}
}
