package com.kincai.store.ui.activity;

import cn.jpush.android.api.JPushInterface;

import com.easemob.chat.EMChat;
import com.easemob.chat.EMGroupManager;
import com.google.gson.Gson;
import com.kincai.store.R;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.kincai.store.Constants;
import com.kincai.store.KincaiApplication;

import com.kincai.store.bean.AreaCityInfo;
import com.kincai.store.bean.AreaProvinceInfo;
import com.kincai.store.bean.AreaZoneInfo;
import com.kincai.store.db.AreaDao;
import com.kincai.store.model.IDialog;
import com.kincai.store.thread.CachedThreadPoolUtils;
import com.kincai.store.thread.HttpGetImgThread;
import com.kincai.store.utils.AnimationUtil;
import com.kincai.store.utils.JsonUtil;
import com.kincai.store.utils.LogTest;
import com.kincai.store.utils.NetWorkUtil;
import com.kincai.store.utils.SPStorageUtil;
import com.kincai.store.utils.Utils;
import com.kincai.store.utils.bitmap_cache.BitmapLocaFileCacheUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.KeyEvent;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 * 
 * @author kincai
 * 
 * @description 欢迎界面
 * 
 * @project Kincai_Store
 * 
 * @package com.kincai.store.ui
 * 
 * @time 2015-6-27 上午7:49:56
 * 
 */
@SuppressWarnings("unused")
public class SplashActivity extends Activity {

	private static final String TAG = "SplashActivity";
	boolean flag = false;
	private MyHandler mMyHandler;
//	List<Map<String, Object>> mJsonList;
	private String mVersionCode;

	private String message;
	/**
	 * 保存下载的欢迎界面
	 */
	private Bitmap mStartBitmap;
	// 屏幕宽度
	int mScreenWidth;
	int mScreenHeight;

	private SPStorageUtil mSp;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		setContentView(R.layout.start_layout);
		super.onCreate(savedInstanceState);
		// 无标题
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 全屏显示
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		LogTest.LogMsg(TAG, "SplashActivity-onCreate");
		init();
		// 加载数据
		getData();

	}

	/**
	 * 初始化
	 */
	private void init() {
		mMyHandler = new MyHandler();
		mSp = new SPStorageUtil(SplashActivity.this);
		initJPush();
	}
	
	/**
	 * 初始化极光推送
	 */
	private void initJPush() {
		if(mSp.getIsMessageWarnChecked()) {//判断是否开启消息通知
			//极光推送
			JPushInterface.setDebugMode(false);// setDebugMode 设置调试模式
			JPushInterface.init(this);
		}
		
	}

	@SuppressLint("HandlerLeak")
	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:

				isFirstStart();

				break;

			default:
				break;

			}
		};
	};

	/**
	 * 获得开始图片
	 * 
	 * @param msg
	 */
	private void getStartImage(Message msg) {
		String jsonImgString = (String) msg.obj;
		if (jsonImgString != null) {
			List<Map<String, Object>> mJsonList0 = new ArrayList<Map<String, Object>>();
			mJsonList0 = JsonUtil.listJson("data", jsonImgString);
			if (mJsonList0 != null) {
				CachedThreadPoolUtils.execute(new HttpGetImgThread(
						mMyHandler, Constants.API_URL + "appImg/"
								+ mJsonList0.get(0).get("face"),
						"userface"));
			} else {
				mStartBitmap = BitmapFactory.decodeResource(getResources(),
						R.drawable.sbg);
				// mIvStartImg.setImageBitmap(mStartBitmap);
			}
		}
	}

	/**
	 * 判断是否第一次启动
	 */
	public void isFirstStart() {
		
		if(!mSp.getIsFirstStart()) {
			LogTest.LogMsg(TAG, "-->不是第一次运行");
			
			//不是第一次启动的情况下才判断有没有广告页
			if(!mSp.getIsSplashAdv()) {
				startMainActivity();
			} else {
				String newUrl = (Constants.SERVER_URL+"appImg/splash_adv.png").replaceAll("[^\\w]", "");
				BitmapLocaFileCacheUtil util = new BitmapLocaFileCacheUtil(this);
				Bitmap bitmap = util.getBitmap(newUrl);
				if(bitmap != null) {
					Utils.showSplashDialog(this, new IDialog() {
						
						@Override
						public void onDialogClick() {
							startMainActivity();
						}
					}, bitmap);
				} else {
					startMainActivity();
				}
				
			}
			
		} else {
			LogTest.LogMsg(TAG, "-->第一次运行");
			
			startActivity(new Intent(SplashActivity.this, GuideActivity.class).putExtra("isGuideFirst", true));
			finish();
			AnimationUtil.startSmoothActivityAnimation(this);
		}
		

	}
	
	/**
	 * 跳转到MainActivity
	 */
	private void startMainActivity() {
		startActivity(new Intent(SplashActivity.this, MainActivity.class));
		finish();
		AnimationUtil.startSmoothActivityAnimation(SplashActivity.this);
	}

	/**
	 * 获取数据
	 */
	public void getData() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				/*AddressDao addressDao = new AddressDao(SplashActivity.this); 

				List<AddressProvinceInfo> list = addressDao.getProvinceInfo();
				List<AddressCityInfo> list2 = addressDao.getCityInfo();
				List<AddressZoneInfo> list3 = addressDao.getZoneInfo();
				if(list != null && list2 != null && list3 != null) {
					String provinceJson = JsonUtil.createGsonString(list);
					String provinceJson2 = JsonUtil.createGsonString(list2);
					String provinceJson3 = JsonUtil.createGsonString(list3);
//					LogTest.LogMsg(TAG, json);
					
					
					SharedPreferences mSp = SplashActivity.this.getSharedPreferences("province_city_zone",
							Context.MODE_PRIVATE);
					
					Editor mEditor = mSp.edit();
					mEditor.putString("privince", provinceJson);
					mEditor.commit();
					mEditor.putString("city", provinceJson2);
					mEditor.commit();
					mEditor.putString("zone", provinceJson3);
					mEditor.commit();
				}
				*/
				
				
				
				
				

				mMyHandler.sendEmptyMessageDelayed(1, 3000);
			}
		}).start();
	}

	/**
	 * 捕获返回键 让按返回键无效
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		return (event.getAction() == KeyEvent.ACTION_DOWN && KeyEvent.KEYCODE_BACK == keyCode)
				|| super.onKeyDown(keyCode, event);

	}

	@Override
	public void onStart() {
		super.onStart();
		//判断是否登录
		if(mSp.getUserIsLogin() &&  EMChat.getInstance().isLoggedIn()) {
			EMGroupManager.getInstance().loadAllGroups();
		}
		LogTest.LogMsg(TAG, "SplashActivity-onStart");
	}

	@Override
	public void onResume() {
		JPushInterface.onResume(this);
		super.onResume();
		LogTest.LogMsg(TAG, "SplashActivity-onResume");
	}

	@Override
	public void onPause() {
		JPushInterface.onPause(this);
		super.onPause();
		LogTest.LogMsg(TAG, "SplashActivity-onPause");

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(Utils.SplashDialogIsShow()) {
			Utils.dismissSplashDialog();
		}
		LogTest.LogMsg(TAG, "SplashActivity-onDestroy");
	}
}
