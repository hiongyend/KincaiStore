package com.kincai.store.ui.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.kincai.store.Constants;
import com.kincai.store.R;
import com.kincai.store.bean.UserInfo;
import com.kincai.store.common.AppUpgrade;
import com.kincai.store.model.IDialog;
import com.kincai.store.thread.CachedThreadPoolUtils;
import com.kincai.store.thread.HttpGetThread;
import com.kincai.store.thread.HttpPostThread;
import com.kincai.store.ui.activity.base.BaseSlideActivity;
import com.kincai.store.utils.AnimationUtil;
import com.kincai.store.utils.JsonUtil;
import com.kincai.store.utils.LogTest;
import com.kincai.store.utils.NetWorkUtil;
import com.kincai.store.utils.Utils;
import com.kincai.store.utils.bitmap_cache.BitmapLocaFileCacheUtil;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 * 
 * @author kincai
 * 
 * @description 设置
 * 
 * @project Kincai_Store
 * 
 * @package com.kincai.store.ui
 * 
 * @time 2015-6-06 上午8:23:25
 * 
 */

public class SettingActivity extends BaseSlideActivity {

	private TextView mTitleTv;
	private CheckedTextView mMessageWarnCtv;
	private CheckedTextView mWifiAutomativUpgradeCtv;
	private CheckedTextView mPictureModeCtv;
	private CheckedTextView mPictureModeDefaultCtv;
	private CheckedTextView mPictureModeHighQualityCtv;
	private CheckedTextView mPictureModeOrdinaryCtv;
	private ViewStub mPictureStub;
	private LinearLayout mPictureModeLl;

	private RelativeLayout mHelperRl;
	private RelativeLayout mClearCacheRl;
	private RelativeLayout mCheckUpgradeRl;
	private RelativeLayout mAboutRl;
	private TextView mCheckUpgradeTv;
	private TextView mPresentVersionTv;
	private Button mExitLoginBtn;
	private TextView mCacheTv;

	private static final String TAG = "SettingActivity";

	private MyHandler mMyHandler;
	private AppUpgrade mAppUpgradeUtil;
	private BitmapLocaFileCacheUtil mFileUtils;
	

	@Override
	public int initContentView() {
		return R.layout.activity_setting_layout;
	}
	@Override
	public void initDatas() {
		init();
		initView();
		initIsCheckedCtv();
		setListener();
	}
	
	/**
	 * 初始化
	 */
	private void init() {

		mFileUtils = new BitmapLocaFileCacheUtil(SettingActivity.this);
		mMyHandler = new MyHandler();

	}

	/**
	 * 初始化控件
	 */
	public void initView() {
		super.initView();
		mMessageWarnCtv = (CheckedTextView) findViewById(R.id.message_warn_ctv);
		mWifiAutomativUpgradeCtv = (CheckedTextView) findViewById(R.id.wifi_automatic_upgrade_ctv);
		mPictureModeCtv = (CheckedTextView) findViewById(R.id.picture_mode_ctv);

		mHelperRl = (RelativeLayout) findViewById(R.id.helper_rl);
		mClearCacheRl = (RelativeLayout) findViewById(R.id.clear_cache_rl);
		mCacheTv = (TextView) findViewById(R.id.cache_number_tv);
		mCheckUpgradeRl = (RelativeLayout) findViewById(R.id.check_upgrade_rl);
		mCheckUpgradeTv = (TextView) findViewById(R.id.check_upgrade_tv);
		mPresentVersionTv = (TextView) findViewById(R.id.check_upgrade_present_version_tv);
		mAboutRl = (RelativeLayout) findViewById(R.id.about_rl);


		mTitleTv = (TextView) findViewById(R.id.title_tv);
		mExitLoginBtn = (Button) findViewById(R.id.exit_login);
		

		mTitleTv.setText(getResources().getString(R.string.my_setting_str));
		String versionName = Utils.getVersionName(SettingActivity.this);
		mPresentVersionTv.setText((versionName == null) ? "" : "V"+versionName);
		//显示缓存大小
		mCacheTv.setText(mFileUtils.getFileDirectorySizeStr());
		//判断登录状态显示隐藏退出按钮
		mExitLoginBtn.setVisibility(mSp.getUserIsLogin() ? View.VISIBLE
				: View.GONE);

	}

	/**
	 * 设置监听
	 */
	public void setListener() {
		super.setListener();
		mMessageWarnCtv.setOnClickListener(this);
		mWifiAutomativUpgradeCtv.setOnClickListener(this);
		mPictureModeCtv.setOnClickListener(this);

		mHelperRl.setOnClickListener(this);
		mClearCacheRl.setOnClickListener(this);
		mCheckUpgradeRl.setOnClickListener(this);
		mAboutRl.setOnClickListener(this);


		mExitLoginBtn.setOnClickListener(this);

	}

	/**
	 * 初始ctv化选中状态
	 */
	public void initIsCheckedCtv() {
		mMessageWarnCtv.setChecked(mSp.getIsMessageWarnChecked());
		mWifiAutomativUpgradeCtv
				.setChecked(mSp.getWifiAutomateUpgradeChecked());
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
		case R.id.message_warn_ctv:
			mMessageWarnCtv.toggle();
			LogTest.LogMsg("SettingActivity", mMessageWarnCtv.isChecked() + "");
			mSp.saveIsMessageWarnChecked(mMessageWarnCtv.isChecked());
			break;
		case R.id.wifi_automatic_upgrade_ctv:
			mWifiAutomativUpgradeCtv.toggle();
			mSp.saveWifiAutomateUpgradeChecked(mWifiAutomativUpgradeCtv
					.isChecked());

			break;
			
		case R.id.picture_mode_ctv://图片显示模式设置
			
			mPictureStub = (ViewStub) findViewById(R.id.ViewStub_picture_mode);
			LogTest.LogMsg(TAG, "mViewStub为空？ "+(mPictureStub==null));
			if (mPictureStub != null) {
				View inflatedView = mPictureStub.inflate();
				mPictureModeDefaultCtv = (CheckedTextView) inflatedView.findViewById(R.id.picture_mode_default_ctv);
				mPictureModeHighQualityCtv = (CheckedTextView) inflatedView.findViewById(R.id.picture_mode_high_quality_ctv);
				mPictureModeOrdinaryCtv = (CheckedTextView) inflatedView.findViewById(R.id.picture_mode_ordinary_ctv);
				mPictureModeLl = (LinearLayout) inflatedView.findViewById(R.id.picture_mode_ll);
				
				mPictureModeDefaultCtv.setOnClickListener(this);
				mPictureModeHighQualityCtv.setOnClickListener(this);
				mPictureModeOrdinaryCtv.setOnClickListener(this);
				
				setPictureCtvSelect(mSp.getPictureMode());
			}

			mPictureModeCtv.toggle();
			if (mPictureModeCtv.isChecked()) {

				AnimationUtil.setShowAnimationAa(mPictureModeLl, 500);
				mPictureModeLl.setVisibility(View.VISIBLE);

			} else {
				AnimationUtil.setHideAnimationAa(mPictureModeLl, 500);
				mPictureModeLl.setVisibility(View.GONE);
			}
			
			break;
		case R.id.picture_mode_default_ctv://默认智能模式
			
			setPictureCtvSelect(Constants.PICTURE_MODE_DEFAULT);
			
			
			break;
		case R.id.picture_mode_high_quality_ctv://高清模式
			setPictureCtvSelect(Constants.PICTURE_MODE_HIGH_QUALITY);
			
			break;
		case R.id.picture_mode_ordinary_ctv://普通模式
			setPictureCtvSelect(Constants.PICTURE_MODE_ORDINARY);
			
			break;
			
		case R.id.helper_rl:
			if(mSp.getUserIsLogin()) {
				startActivity(new Intent(this, HelpAndServeActivity.class));
				AnimationUtil.startHaveSinkActivityAnimation(this);
			} else {
				startActivity(new Intent(this, UserLoginActivity.class));
				AnimationUtil.startHaveSinkActivityAnimation(this);
			}
			
			break;
		case R.id.clear_cache_rl:
			clearCache();
			break;
		case R.id.check_upgrade_rl:
			checkUpgrade();
			break;
		case R.id.about_rl:
			
			startActivity(new Intent(this, AboutActivity.class));
			AnimationUtil.startHaveSinkActivityAnimation(this);

			break;
		case R.id.exit_login:

			showTip();

			break;
		}
	}

	/**
	 * 设置图片模式Ctv选中状态
	 * @param mode
	 */
	private void setPictureCtvSelect(int mode) {

		switch (mode) {
		case Constants.PICTURE_MODE_DEFAULT:
			mPictureModeDefaultCtv.setChecked(true);
			mPictureModeHighQualityCtv.setChecked(false);
			mPictureModeOrdinaryCtv.setChecked(false);

			break;
		case Constants.PICTURE_MODE_HIGH_QUALITY:

			mPictureModeDefaultCtv.setChecked(false);
			mPictureModeHighQualityCtv.setChecked(true);
			mPictureModeOrdinaryCtv.setChecked(false);

			break;
		case Constants.PICTURE_MODE_ORDINARY:
			mPictureModeDefaultCtv.setChecked(false);
			mPictureModeHighQualityCtv.setChecked(false);
			mPictureModeOrdinaryCtv.setChecked(true);

			break;
		}
		
		mSp.savePictureMode(mode);
	}

	/**
	 * 清除缓存
	 */
	private void clearCache() {
		Utils.showDialog(SettingActivity.this, false, "", mScreenWidth, "清除缓存",
				"确定清除本地缓存？", "取消", "确定", new IDialog() {

					@Override
					public void onDialogClick() {

						mFileUtils.deleteFile();
						mCacheTv.setText(mFileUtils.getFileDirectorySizeStr());
					}
				});
	}


	/**
	 * 检查更新
	 */
	private void checkUpgrade() {

		if (NetWorkUtil.isNetworkAvailable(SettingActivity.this)) {
//			mLoadingDialog.dialogShow();
			mCheckUpgradeTv.setText(getResources().getString(R.string.upgrading));

			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			parameters.add(new BasicNameValuePair("app_id", Constants.APP_ID));
			parameters.add(new BasicNameValuePair("version_code", Integer
					.toString(Utils.getVersionCode(SettingActivity.this))));

			CachedThreadPoolUtils.execute(new HttpPostThread(mMyHandler,
					Constants.API_URL + "appVersionUpGradeApi.php", parameters,
					"SettingActivityV"));
		}

	}

	/**
	 * 退出登录时 按确定按钮时
	 */
	private IDialog exitLogin = new IDialog() {

		@Override
		public void onDialogClick() {
			
			if(NetWorkUtil.isNetworkAvailable(SettingActivity.this)) {
				
				setFocusableAndEnable("注销中...", false);
				
				CachedThreadPoolUtils.execute(new HttpGetThread(mMyHandler, new StringBuffer().append(Constants.API_URL)
						.append("appUserLogoutApi.php?username=").append(mSp.getUsername()).toString(), "logout"));
			} else {
				setFocusableAndEnable("退出登录", true);
				Utils.showToast(SettingActivity.this, "网络状态有问题哦！请检查网络连接设置", 0);
			}
			
		}
	};
	private void setFocusableAndEnable(String btnText, boolean isTrue) {
		mExitLoginBtn.setBackgroundResource(isTrue ? R.drawable.dialog_btn_right_bg : R.drawable.dialog_btn_left_bg_down);
		mExitLoginBtn.setEnabled(isTrue);
		mExitLoginBtn.setText(btnText);
	}
	
	/**
	 * 环信退出登录
	 */
	private void loginOut() {
		EMChatManager.getInstance().logout(new EMCallBack() {
			 
			@Override
			public void onSuccess() {
				runOnUiThread(new Runnable() {
					public void run() {
						Utils.showToast(SettingActivity.this, "环信退出登录 成功", 0);
					}
				});
					
			}
		 
			@Override
			public void onProgress(int progress, String status) {
			    // TODO Auto-generated method stub
		 
			}
		 
			@Override
			public void onError(int code, String message) {
				runOnUiThread(new Runnable() {
					public void run() {
				Utils.showToast(SettingActivity.this, "环信退出登录 失败", 0);	}});	 
			}
		});
	}
	/**
	 * 显示退出提示对话框
	 */
	private void showTip() {
		Utils.showDialog(SettingActivity.this, false, "", mScreenWidth, "退出登录",
				"确定退出当前登录？", "取消", "确定", exitLogin);

	}

	@SuppressLint("HandlerLeak")
	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
			case 2050:
				mCheckUpgradeTv.setText(getResources().getString(R.string.check_grade_str));
				mAppUpgradeUtil = new AppUpgrade(msg, SettingActivity.this,
						mScreenWidth, mMyHandler, mSp);
				mAppUpgradeUtil.versionJudge(false);
				break;

			case 230:
				mCheckUpgradeTv.setText(getResources().getString(R.string.check_grade_str));
				mAppUpgradeUtil.updateApp(msg);
				break;
			case 2132:
				setFocusableAndEnable("退出登录", true);
				getResponseLogoutData(msg);
				break;
			}
		}
	}
	private void getResponseLogoutData(Message msg) {
		String response =  (String) msg.obj;
		if(response != null && "200".equals(JsonUtil.msgJson("code", response))) {
			//注销成功
			//TODO 退出登录 设置登录状态为false 用户数据库清除当前信息 
			List<UserInfo> mUserInfos = mBaseUserDao.getUserInfoOne(mSp.getUsername());
			if(mUserInfos != null && mUserInfos.size() > 0)
			mBaseUserDao.deleteUser(mUserInfos.get(0).getUserId());
			mSp.saveUserIsLogin(false);
			mSp.saveUsername(null);
			
			loginOut();
			Intent intent = new Intent();
			intent.setAction(Constants.USERINFO_CHANGE_ACTION);
			sendBroadcast(intent);
			finish();
			AnimationUtil.finishHaveFloatActivityAnimation(SettingActivity.this);
			
		} else {
			//失败
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
	protected void onStart() {
		super.onStart();
		LogTest.LogMsg(TAG, "SettingActivity-onStart");
	}

	@Override
	protected void onResume() {
		super.onResume();
		LogTest.LogMsg(TAG, "SettingActivity-onResume");
	}

	@Override
	protected void onPause() {
		super.onPause();
		LogTest.LogMsg(TAG, "SettingActivity-onPause");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		LogTest.LogMsg(TAG, "SettingActivity-onDestroy");
		
	}
}
