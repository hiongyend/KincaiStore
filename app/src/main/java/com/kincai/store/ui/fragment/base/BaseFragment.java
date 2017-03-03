package com.kincai.store.ui.fragment.base;

import com.kincai.store.db.UserDao;
import com.kincai.store.ui.activity.base.BaseActivity;
import com.kincai.store.utils.LogTest;
import com.kincai.store.utils.NetWorkUtil;
import com.kincai.store.utils.SPStorageUtil;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 *
 * @author kincai
 * 
 * @description fragment 基类
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.fragment
 *
 * @time 2015-8-18 下午10:34:21
 *
 */
public abstract class BaseFragment extends Fragment {
	private static final String TAG = "BaseFragment";
	private ConnectionChangeReceiver mReceiver;
	public Context mMainActivityContext;
	public int mScreenWidth;
	public View mView;
	public int mScreenHeight;
	public SPStorageUtil mSp;
	
	public UserDao mUserDao;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		mMainActivityContext = getActivity();
		init();
		super.onCreate(savedInstanceState);
		
		LogTest.LogMsg(TAG, "BaseFragment-onCreate");
	}
	
	private void init() {
		if(mMainActivityContext instanceof BaseActivity) {
			mSp = ((BaseActivity)mMainActivityContext).mSp;
			mUserDao = ((BaseActivity)mMainActivityContext).mBaseUserDao;
			mScreenWidth = ((BaseActivity)mMainActivityContext).mScreenWidth;
			mScreenHeight = ((BaseActivity)mMainActivityContext).mScreenHeight;
		
		} else {
			mSp = new SPStorageUtil(mMainActivityContext);
			mUserDao = new UserDao(mMainActivityContext);
			DisplayMetrics displayMetrics = new DisplayMetrics();
			((Activity)mMainActivityContext).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
			mScreenWidth = displayMetrics.widthPixels;
			mScreenHeight = displayMetrics.heightPixels;
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LogTest.LogMsg(TAG, "BaseFragment-onCreateView");
		mView = getContentView();
		initDatas();
		return mView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		LogTest.LogMsg(TAG, "BaseFragment-onActivityCreated");
		registerReceiver();
		
		
	}
	
	public abstract View getContentView();
	
	public abstract void initDatas();
	
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
		mReceiver = new ConnectionChangeReceiver();
		mMainActivityContext.registerReceiver(mReceiver, filter);
	}

	/**
	 * 注销实时网络监听广播
	 */
	private void unRegisterReceiver() {
		mMainActivityContext.unregisterReceiver(mReceiver);
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
				LogTest.LogMsg(TAG, "广播监听到有网络");
				netWork();
			}
		}
	}
	
	
	@Override
	public void onStart() {
		super.onStart();
		LogTest.LogMsg(TAG, "BaseFragment-onStart");

	}

	@Override
	public void onResume() {
		super.onResume();
		LogTest.LogMsg(TAG, "BaseFragment-onResume");
		if (!NetWorkUtil.isNetworkAvailable(mMainActivityContext)) {
			// Utils.showToast(mMainFragmentContext, "网络连接失败", Toast.LENGTH_SHORT);
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		LogTest.LogMsg(TAG, "BaseFragment-onPause");
	}
	
	@Override
	public void onStop() {
		super.onStop();
		LogTest.LogMsg(TAG, "BaseFragment-onStop");
	}

	@Override
	public void onDestroy() {
		unRegisterReceiver();
		super.onDestroy();
		LogTest.LogMsg(TAG, "BaseFragment-onDestroy");

		
	}


}
