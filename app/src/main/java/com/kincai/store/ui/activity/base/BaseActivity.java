package com.kincai.store.ui.activity.base;


import com.kincai.store.KincaiApplication;
import com.kincai.store.R;
import com.kincai.store.db.UserDao;
import com.kincai.store.model.IDialog;
import com.kincai.store.ui.activity.UserLoginActivity;
import com.kincai.store.utils.AnimationUtil;
import com.kincai.store.utils.LogTest;
import com.kincai.store.utils.SPStorageUtil;
import com.kincai.store.utils.TimeUtil;
import com.kincai.store.utils.Utils;
import com.kincai.store.utils.bitmap_cache.MyImageLoader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 * 
 * @author kincai
 * 
 * @description Activity基类 不可右滑结束
 * 
 * @project Kincai_Store
 * 
 * @package com.kincai.store.ui
 * 
 * @time 2015-8-24 下午3:09:03
 * 
 */
public abstract class BaseActivity extends FragmentActivity implements OnClickListener {
	
	private static final String TAG = "BaseActivity";
	
	/** 网络监听广播接受者 */
	private ConnectionChangeReceiver mReceiver;
	
	/** 返回*/
	public ImageView mBackIv;
	/** 网络异常提示布局*/
	public RelativeLayout mNetWorkLayout;
	public SPStorageUtil mSp;
	public MyImageLoader mImageLoader;
	public KincaiApplication mKincaiApplication;
	
	/** 屏幕高度*/
	public int mScreenWidth;
	/** 屏幕宽度*/
	public int mScreenHeight;
	
	public UserDao mBaseUserDao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// 无标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		init();
		setContentView(initContentView());
		initDatas();
		registerReceiver();
		LogTest.LogMsg(TAG, "BaseActivity-onCreate");
		
	}
	
	private void init() {
		mKincaiApplication = KincaiApplication.getKincaiApplication();
		mSp = mKincaiApplication.getmSp();
		mBaseUserDao = mKincaiApplication.getmUserDao();
		getWindowManager().getDefaultDisplay().getMetrics(mKincaiApplication.getMetric());
		mScreenWidth = mKincaiApplication.getMetric().widthPixels;
		mScreenHeight =mKincaiApplication.getMetric().heightPixels;
		mKincaiApplication.setmScreenWidth(mScreenWidth);
		
		mImageLoader = mKincaiApplication.getmImageLoader();
		if(mImageLoader == null) {
			mKincaiApplication.setmImageLoader(new MyImageLoader(this));
		} else {
		}
		
		mImageLoader = mKincaiApplication.getmImageLoader();
		
		mKincaiApplication.addActivity(this);
	}
	

	/**
	 * 初始化空间
	 */
	public void initView() {
		mBackIv = (ImageView) findViewById(R.id.titlebar_back_iv);
		mNetWorkLayout = (RelativeLayout) findViewById(R.id.network_abnormal_top_layout);
	}
	
	/**
	 * 设置监听
	 */
	public void setListener() {
		mBackIv.setOnClickListener(this);
		mNetWorkLayout.setOnClickListener(this);
		
	}
	
	public abstract void initDatas();
	
	public abstract int initContentView();
	/**
	 * 广播监听有网络的时候调用
	 */
	public abstract void netWork();

	/**
	 * 广播监听没有网络的时候调用
	 */
	public abstract void noNetWork();

	/**
	 * 注册实时网络监听广播
	 */
	private void registerReceiver() {
		IntentFilter filter = new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION);
		filter.addAction("isloginout");
		mReceiver = new ConnectionChangeReceiver();
		registerReceiver(mReceiver, filter);
	}

	/**
	 * 注销实时网络监听广播
	 */
	private void unregisterReceiver() {
		unregisterReceiver(mReceiver);
	}

	/**
	 * 实时网络监听广播
	 * 
	 * @author kincai
	 * 
	 */
	public class ConnectionChangeReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			ConnectivityManager connectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mobNetInfo = connectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			NetworkInfo wifiNetInfo = connectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

			if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
				noNetWork();
			} else {
				netWork();
			}
			
			if(intent!= null && intent.getAction().equals("isloginout")) {
				String deviceType = intent.getStringExtra("deviceType");
				Utils.showMiniDialog(context, mScreenWidth, new StringBuilder().append("您的账号于").append(TimeUtil.StringLongToString(intent.getStringExtra("loginTime"),"HH:mm"))
						.append("Pc".equals(deviceType) ?"在Pc端登录" : ("Android".equals(deviceType) ? 
								"在另一台Android手机登录" : ("Ios".equals(deviceType) ? "在另一台Ios手机登录": "")))
								.append("。如非本人操作，则密码可能已泄露，建议立即修改密码或冷冻账户。").toString(),"", "确定", new IDialog() {
					
					@Override
					public void onDialogClick() {
						startActivity(new Intent(BaseActivity.this, UserLoginActivity.class));
					}
				},false);
			}
		}
	}
	

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			this.finish();
			AnimationUtil.finishHaveFloatActivityAnimation(this);
			return true;

		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onRestart() {
		LogTest.LogMsg(TAG, "BaseActivity-onRestart");
		super.onRestart();
	}

	@Override
	protected void onStart() {
		LogTest.LogMsg(TAG, "BaseActivity-onStart");
		super.onStart();

	}

	@Override
	protected void onResume() {
		LogTest.LogMsg(TAG, "BaseActivity-onResume");
		super.onResume();

	}

	@Override
	protected void onPause() {
		LogTest.LogMsg(TAG, "BaseActivity-onPause");
		super.onPause();

	}

	@Override
	protected void onStop() {
		LogTest.LogMsg(TAG, "BaseActivity-onStop");
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		LogTest.LogMsg(TAG, "BaseActivity-onDestroy");
		unregisterReceiver();
		super.onDestroy();
		mKincaiApplication.exitOneActivity(this);

	}
}
