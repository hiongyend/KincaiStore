package com.kincai.store.ui.activity;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.kincai.store.Constants;
import com.kincai.store.KincaiApplication;
import com.kincai.store.R;

import com.kincai.store.bean.AreaCityInfo;
import com.kincai.store.bean.AreaProvinceInfo;
import com.kincai.store.bean.AreaZoneInfo;
import com.kincai.store.bean.ProImagePathInfo;
import com.kincai.store.bean.SearchItem;
import com.kincai.store.bean.UserInfo;
import com.kincai.store.common.ProCommon;
import com.kincai.store.db.AreaDao;
import com.kincai.store.db.ImageDao;
import com.kincai.store.db.SearchItemDao;
import com.kincai.store.model.IDialog;
import com.kincai.store.thread.CachedThreadPoolUtils;
import com.kincai.store.thread.HttpGetThread;
import com.kincai.store.ui.activity.base.BaseActivity;
import com.kincai.store.ui.fragment.CartFragment;
import com.kincai.store.ui.fragment.CateFragment;
import com.kincai.store.ui.fragment.HomeFragment;
import com.kincai.store.ui.fragment.PersionFragment;
import com.kincai.store.utils.AnimationUtil;
import com.kincai.store.utils.GetAssetsFiles;
import com.kincai.store.utils.JsonUtil;
import com.kincai.store.utils.LoadingDialog;
import com.kincai.store.utils.LogTest;
import com.kincai.store.utils.NetWorkUtil;
import com.kincai.store.utils.SPStorageUtil;
import com.kincai.store.utils.Utils;
import com.kincai.store.utils.bitmap_cache.MyImageLoader;
/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 * 
 * @author kincai
 * 
 * @description MainActivity
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.ui
 *
 * @time 2015-7-13 上午8:22:11
 *
 */
@SuppressWarnings("unused")
public class MainActivity extends BaseActivity {
	private static final String TAG = "MainActivity";
	private Button[] mTabs;
	public HomeFragment mHomeFragment;
	private CateFragment mCateFragment;
	private CartFragment mCartFragment;
	private PersionFragment mPersionFragment;
	private Fragment[] fragments;
	private ImageView mIvCartTip;

	private ImageView mIvPersionTip;

	private String message;
	private MyHandler mMyHandler;
	private int index = 0;
	private int currentTabIndex = 0;
	int mScreenWidth;
	int mScreenHeight;

	private SearchItemDao mItemDb;
	private List<ProImagePathInfo> mAllProImagePath;
	private AreaDao mAddressDao;

	private List<AreaProvinceInfo> mProvinceInfos;
	private List<AreaCityInfo> mCityInfos;
	private List<AreaZoneInfo> mZoneInfos;
	
	/** 等待时间*/
	private long waitTime = 2000;  
	private long touchTime = 0;
	
	public static MainActivity instance = null;
//	static int key;
	boolean isAgain;

	@Override
	public int initContentView() {
		return R.layout.activity_main_layout;
	}
	@Override
	public void initDatas() {
//		key ++;
//		KincaiApplication.getInstance().addMinaActivity(MainActivity.this, key);
		instance = this;
		mMyHandler = new MyHandler();


		init();
		initView();
		initData();
		initTab();
	}
	
	
	/**
	 * 初始化
	 */
	private void init() {
		mItemDb = new SearchItemDao(this);

		mAddressDao = new AreaDao(this);
		if (!mSp.getIsAddProvinceCityZoneToDb()) {
			// Utils.showToast(MainActivity.this,
			// "已经添加在数据库  城市数量："+mAddressDao.getProvinceInfo().size()+" 市数量："+mAddressDao.getCityInfo().size()+" 县数量："+mAddressDao.getZoneInfo().size(),
			// 10000);
			initProvinceCityZone();
		}
		
//		getIntentData();

	}
	
//	private void getIntentData() {
//		Intent intent = getIntent();
//		if(intent != null) {
//			isAgain = intent.getBooleanExtra("again", false);
//		}
//	}

	/**
	 * 初始化控件
	 */
	public void initView() {
		mTabs = new Button[4];
		mTabs[0] = (Button) findViewById(R.id.btn_home);
		mTabs[1] = (Button) findViewById(R.id.btn_cate);
		mTabs[2] = (Button) findViewById(R.id.btn_cart);
		mTabs[3] = (Button) findViewById(R.id.btn_persion);
		mIvCartTip = (ImageView) findViewById(R.id.iv_cart_tips);
		mIvPersionTip = (ImageView) findViewById(R.id.iv_persion_tips);
		// 把第一个tab设为选中状态
		mTabs[0].setSelected(true);
	}

	/**
	 * 初始化底部菜单
	 */
	private void initTab() {
		mHomeFragment = new HomeFragment();
		mCateFragment = new CateFragment();
		mCartFragment = new CartFragment();
		mPersionFragment = new PersionFragment();
		fragments = new Fragment[] { mHomeFragment, mCateFragment,
				mCartFragment, mPersionFragment };
		// 添加显示第一个fragment
		getSupportFragmentManager().beginTransaction()
				.add(R.id.fragment_container, mHomeFragment)
				.show(mHomeFragment).commit();
//		if(isAgain)SetCurrentTab(2);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		SetCurrentTab(2);
		
	}
	/**
	 * 初始化加载数据
	 */
	private void initData() {
		
//		if(mSp.getUserIsLogin()) {
//			Set<String> set = new HashSet<String>();
//			set.add(mSp.getUsername());
//			JPushInterface.setTags(this, set, new TagAliasCallback() {
//				
//				@Override
//				public void gotResult(int arg0, String arg1, Set<String> arg2) {
//					Iterator<String> iterator = arg2.iterator();
//					StringBuilder stringBuilder = new StringBuilder();
//					while (iterator.hasNext()) {
//						stringBuilder.append(iterator.next()).append(",");
//						
//					}
//					Utils.showToast(MainActivity.this, arg0+" "+arg1+" arg2: "+stringBuilder.toString(), 1);
//				}
//			});
//			
//		}
		
		if (NetWorkUtil.isNetworkAvailable(this)) {

			if(mSp.getUserIsLogin()) {
				CachedThreadPoolUtils.execute(new HttpGetThread(mMyHandler, 
						new StringBuffer().append(Constants.API_URL).append("appUserDeviceIsLoginApi.php?username=")
						.append(mSp.getUsername()).append("&device_id=0&unique_id=").append(Utils.getUniqueId(MainActivity.this)).toString(), "isdevicelogin"));
			}
			CachedThreadPoolUtils.execute(new HttpGetThread(mMyHandler,
					Constants.API_URL + "appGetSearchItem.php",
					"SearchItem"));
			
			CachedThreadPoolUtils
					.execute(new HttpGetThread(mMyHandler, Constants.API_URL
							+ "appGetIsSplashAdvApi.php", "haveSplashAdv"));

		}

	}

	/**
	 * 程序第一次运行到这个activity的时候才会被调用 从assets获取省市县/区存到数据库
	 */
	private void initProvinceCityZone() {

		new Thread(new Runnable() {

			@Override
			public void run() {
				List<Map<String, Object>> list = GetAssetsFiles
						.getAddressStrForDom(MainActivity.this);
//						.getAddressStrForPull(MainActivity.this);
				if (null != list) {
					addAssetsDataToDb(list);
				}
			}
		}).start();
	}

	/**
	 * 保存省市县/区到数据库
	 */
	private void addAssetsDataToDb(List<Map<String, Object>> datas) {
		mProvinceInfos = JsonUtil.getProvince(datas.get(0).get("province")
				.toString());
		mCityInfos = JsonUtil.getCity(datas.get(0).get("city").toString());
		mZoneInfos = JsonUtil.getZone(datas.get(0).get("zone").toString());
		if (null != mProvinceInfos && null != mCityInfos && null != mZoneInfos) {
			long row = mAddressDao.saveProviceInfo(mProvinceInfos);
			long row2 = mAddressDao.saveCityInfo(mCityInfos);
			long row3 = mAddressDao.saveZoneInfo(mZoneInfos);
			if (0 != row && 0 != row2 && 0 != row3) {
				// 保存成功
				mSp.saveIsAddProvinceCityZoneToDb(true);
				Message message = Message.obtain();
				message.what = 3;
				mMyHandler.sendMessage(message);

			}
		}
	}

	/**
	 * button点击事件
	 * 
	 * @param view
	 */
	public void onTabSelect(View view) {
		switch (view.getId()) {
		case R.id.btn_home:
			index = 0;
			break;
		case R.id.btn_cate:
			index = 1;
			break;
		case R.id.btn_cart:
			index = 2;
			break;
		case R.id.btn_persion:
			index = 3;
			break;
		}
		if (currentTabIndex != index) {
			FragmentTransaction trx = getSupportFragmentManager()
					.beginTransaction();
			trx.hide(fragments[currentTabIndex]);
			if (!fragments[index].isAdded()) {
				trx.add(R.id.fragment_container, fragments[index]);
			}
			trx.show(fragments[index]).commit();
		}
		mTabs[currentTabIndex].setSelected(false);
		// 把当前tab设为选中状态
		mTabs[index].setSelected(true);
		currentTabIndex = index;
		
//		if(index != 2 && isAgain) {
//			isAgain = false;
//			KincaiApplication.getInstance().exitOtherMainActivity();
//			KincaiApplication.getInstance().exitInAdditionToTheMainAtivityOfOtherActivities();
//			
//		}
	}

	@SuppressLint("HandlerLeak")
	class MyHandler extends Handler {
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case 210:
				addSearchItem(msg);
				break;
			case 2113:
				getResponseSplashAdvData(msg);
				break;
			case 2133:
				getResponseIsDeviceLoginData(msg);
				break;
			}

		}

	}
	
	private void getResponseIsDeviceLoginData(Message msg) {
		String response = (String) msg.obj;

		if (null != response && "200".equals(JsonUtil.msgJson("code", response))) { 
			//修改登录状态为false
			//TODO 退出登录 设置登录状态为false 用户数据库清除当前信息 
			List<UserInfo> mUserInfos = mBaseUserDao.getUserInfoOne(mSp.getUsername());
			if(mUserInfos != null && mUserInfos.size() > 0)
			mBaseUserDao.deleteUser(mUserInfos.get(0).getUserId());
			mSp.saveUserIsLogin(false);
			mSp.saveUsername(null);
		} else {
			
		}
		
	}

	/**
	 * 获取返回的是否有广告页数据
	 * @param msg
	 */
	private void getResponseSplashAdvData(Message msg) {
		String response = (String) msg.obj;

		if (null != response) {
			String code = JsonUtil.msgJson("code", response);
			if(null != code && "200".equals(code)) {
				loadSplashAdvImg();
				mSp.saveIsSplashAdv(true);
			} else {
				mSp.saveIsSplashAdv(false);
			}
				
		}
	}
	
	/**
	 * 加载广告页图片
	 */
	private void loadSplashAdvImg() {
		MyImageLoader imageLoader = new MyImageLoader(this);
		imageLoader.loadImage(Constants.SERVER_URL+"appImg/splash_adv.png", null);
		imageLoader = null;
	}

	/**
	 * 从服务器获取的搜索数据
	 * 
	 * @param msg
	 */
	private void addSearchItem(Message msg) {
		// 不要忘记判断数据库有没有重复色数据 重复的不再添加
		// jsonItems从网络获取的
		// dbList数据库获取的
		// list新的数据
		List<SearchItem> jsonItems;
		String response = (String) msg.obj;
		// System.out.println("dddsdsdsd");
		if (response != null) {
			String code = JsonUtil.msgJson("code", response);
			if ("200".equals(code)) {
				jsonItems = JsonUtil.getSearchItemList("data", response);
				if (jsonItems != null) {
					List<SearchItem> dbList = mItemDb.getSearchItemInfo();
					List<SearchItem> list = new ArrayList<SearchItem>();
					if (dbList != null) {

						int dbListSize = dbList.size();
						int jsonItemsSize = jsonItems.size();
						boolean flag = true;
						SearchItem searchItem = new SearchItem();
						for (int i = 0; i < jsonItemsSize; i++) {
							for (int j = 0; j < dbListSize; j++) {
								flag = true;
								if (jsonItems.get(i).getItem()
										.equals(dbList.get(j).getItem())) {
									flag = false;
									break;
								}

							}

							if (flag) {
								searchItem.setItem(jsonItems.get(i).getItem());
								list.add(searchItem);
							}

						}
						mItemDb.saveSearchInfo(list);
					} else {
						mItemDb.saveSearchInfo(jsonItems);
					}

					// LogTest.LogMsg(TAG, "获取搜索条目信息 " +
					// list.get(0).getItem());

				}
			} else {
				LogTest.LogMsg(TAG, "获取搜索条目信息失败");
			}

		}
	}

	/**
	 * 改变屏幕亮度
	 * 
	 * @param num
	 */
	/*private void setScreenBrightness(float num) {
		WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
		layoutParams.screenBrightness = num;// 设置屏幕的亮度
		getWindow().setAttributes(layoutParams);
	}*/

	/**
	 * 回调tab 底部提示图标
	 * 
	 * @param isNotNull
	 */
	public void tip(boolean isNotNull) {
		if (isNotNull) {
			mIvCartTip.setVisibility(View.VISIBLE);
		} else {
			mIvCartTip.setVisibility(View.GONE);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (event.getAction() == KeyEvent.ACTION_DOWN
				&& KeyEvent.KEYCODE_BACK == keyCode) {

//			if(!isAgain) {
				if (currentTabIndex != 0) {
					FragmentTransaction trx = getSupportFragmentManager()
							.beginTransaction();
					trx.hide(fragments[currentTabIndex]);
					trx.show(mHomeFragment).commit();
					mTabs[0].setSelected(true);
					mTabs[currentTabIndex].setSelected(false);
					index = 0;
					currentTabIndex = 0;

					return false;

				} else {

//					moveTaskToBack(false);
					long currentTime = System.currentTimeMillis();  
			        if((currentTime-touchTime)>=waitTime) {  
			            Utils.showToast(MainActivity.this, "再按一次退出", 0); 
			            touchTime = currentTime;  
			            
			        } else {
			        	Utils.dismissToast();
			        	
//			        	mKincaiApplication.remove();
			        	finish();
			        	
					}
			        return true; 

				}
			/*} else {
				KincaiApplication.getInstance().exitLastMainActivity();
				AnimationUtil.finishHaveFloatActivityAnimation(this);
//				finish();
			}*/
			
		}
		return super.onKeyDown(keyCode, event);

	}
	
	/**
	 * 设置选中购物车页面
	 * @param position
	 */
	public void SetCurrentTab(int position) {
		FragmentTransaction trx = getSupportFragmentManager()
				.beginTransaction();
		trx.hide(fragments[currentTabIndex]);
		if (!fragments[position].isAdded()) {
			trx.add(R.id.fragment_container, fragments[position]);
		} 
		
		if(position == 0) {
			trx.show(mHomeFragment).commitAllowingStateLoss();
		} else if (position == 1) {
			trx.show(mCateFragment).commitAllowingStateLoss();
		} else if (position == 2) {
			trx.show(mCartFragment).commitAllowingStateLoss();
		} else if (position == 3) {
			trx.show(mPersionFragment).commitAllowingStateLoss();
		}
		
		mTabs[position].setSelected(true);
		mTabs[currentTabIndex].setSelected(position == currentTabIndex);
		index = position;
		currentTabIndex = position;
	}
	
	/*public void setIsAgain(boolean isAgain) {
		this.isAgain = isAgain;
		
	}*/
	
	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		
		LogTest.LogMsg(TAG, "menu");
		//屏蔽系统的菜单
		return false;
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.add("menu");
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		LogTest.LogMsg(TAG, "MainActivity-onRestart");
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		LogTest.LogMsg(TAG, "MainActivity-onStart");
	}

	@Override
	protected void onResume() {
		super.onResume();
		LogTest.LogMsg(TAG, "MainActivity-onResume");

		/*if (mSp.getIsChangeLightChecked()) {
			setScreenBrightness((float) mSp.getChangeLightPrice());
			LogTest.LogMsg(TAG, (float) mSp.getChangeLightPrice() + " main");
		}*/
		isNerwoking();

	}

	@Override
	protected void onPause() {
		super.onPause();
		LogTest.LogMsg(TAG, "MainActivity-onPause");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		LogTest.LogMsg(TAG, "MainActivity-onDestroy");
		System.exit(0);
	}

	public void isNerwoking() {
		if (!NetWorkUtil.isNetworkAvailable(MainActivity.this)) {
			Utils.showToast(MainActivity.this, "网络连接失败", 0);
		}
	}


	@Override
	public void onClick(View v) {
		
	}


	@Override
	public void netWork() {
		
	}


	@Override
	public void noNetWork() {
		
	}

}
