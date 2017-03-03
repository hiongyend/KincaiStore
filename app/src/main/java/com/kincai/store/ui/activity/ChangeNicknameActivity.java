package com.kincai.store.ui.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.kincai.store.Constants;
import com.kincai.store.R;
import com.kincai.store.thread.CachedThreadPoolUtils;
import com.kincai.store.thread.HttpPostThread;
import com.kincai.store.ui.activity.base.BaseSlideActivity;
import com.kincai.store.utils.AnimationUtil;
import com.kincai.store.utils.JsonUtil;
//import com.kincai.store.utils.LoadingDialog;
import com.kincai.store.utils.LogTest;
import com.kincai.store.utils.NetWorkUtil;
import com.kincai.store.utils.Utils;
import com.kincai.store.view.custom.CleanableAndVoiceEditTextView;

import android.annotation.SuppressLint;
//import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 *
 * @author kincai
 * 
 * @description 修改昵称
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.ui
 *
 * @time 2015-7-24 下午7:36:54
 *
 */
public class ChangeNicknameActivity extends BaseSlideActivity {
	private static final String TAG = "ChangeUsernamActivity";
	private TextView mConfirmTv;
	private CleanableAndVoiceEditTextView mNickNameEt;
	private TextView mTitleTv;

	private MyHandler mMyHandler;

	private String mNickNameStr;
	
	@Override
	public int initContentView() {
		return R.layout.activity_change_username_layout;
	}
	@Override
	public void initDatas() {
		init();
		getIntentData();
		initView();
		setListener();
	}
	
	/**
	 * 初始化
	 */
	private void init() {
		mMyHandler = new MyHandler();
	}
	
	/**
	 * 获取intent传过来的值
	 */
	private void getIntentData() {
		Intent intent = getIntent();
		mNickNameStr = (intent == null) ? "" : intent.getStringExtra("nickname"); 
	}

	/**
	 * 初始化控件
	 */
	public void initView() {
		super.initView();
		mConfirmTv = (TextView) findViewById(R.id.titlebar_confirm_change_iv);
		mNickNameEt = (CleanableAndVoiceEditTextView) findViewById(R.id.username_et);
		mTitleTv = (TextView) findViewById(R.id.title_tv);
		mTitleTv.setText(getResources().getString(R.string.user_tv_str));
		
		mNickNameEt.setText(mNickNameStr);
	}

	/**
	 * 设置监听
	 */
	public void setListener() {
		super.setListener();
		mConfirmTv.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.titlebar_back_iv:
			finish();
			AnimationUtil.finishHaveFloatActivityAnimation(this);
			break;
		case R.id.network_abnormal_top_layout:
			startActivity(new Intent(Settings.ACTION_SETTINGS));
			AnimationUtil.startHaveSinkActivityAnimation(this);
			break;
		case R.id.titlebar_confirm_change_iv:

			checkUser();
			break;

		}
	}

	@SuppressLint("HandlerLeak")
	class MyHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 208:

				changeUserInfoState(msg);

				break;

			default:
				break;
			}
		}
	}

	/**
	 * 用户账号密码检测
	 */
	private void checkUser() {
		// 昵称格判断
		boolean usernamebl = mNickNameEt.getText().toString()
				.matches(Constants.NICKNAME_RESTRICTION);
		if (usernamebl) {
			if (NetWorkUtil.isNetworkAvailable(ChangeNicknameActivity.this)) {
//				mLoadingDialog.dialogShow();
				mTitleTv.setText(this.getResources().getString(R.string.updateing));

				List<NameValuePair> parameters = new ArrayList<NameValuePair>();
				parameters.add(new BasicNameValuePair("userId", String
						.valueOf(mBaseUserDao.getUserInfoOne(mSp.getUsername())
								.get(0).getUserId())));
				parameters.add(new BasicNameValuePair("userInfo", mNickNameEt
						.getText().toString()));
				parameters.add(new BasicNameValuePair("flag", "nickname"));

				CachedThreadPoolUtils.execute(new HttpPostThread(mMyHandler,
						Constants.API_URL + "appChangeUserInfoApi.php", parameters,
						"changeUserInfo"));
				LogTest.LogMsg(
						TAG,
						"用户名格式正确  有网络 "
								+ mBaseUserDao.getUserInfoOne(mSp.getUsername())
										.get(0).getUserId() + "");
				LogTest.LogMsg("ki", mNickNameEt.getText().toString());

				// finish();
			} else {
				LogTest.LogMsg(TAG, "用户名格式正确  没网络");
			}

		} else {
			LogTest.LogMsg(TAG, "用户名格式不正确");
		}
	}

	/**
	 * 修改用户信息状态
	 * 
	 * @param msg
	 */
	private void changeUserInfoState(Message msg) {
//		mLoadingDialog.dismiss();
		mTitleTv.setText(this.getResources().getString(R.string.user_tv_str));
		String response = (String) msg.obj;
		// LogTest.LogMsg(TAG, response);
		if (response != null) {
			String code = JsonUtil.msgJson("code", response);
			if ("200".equals(code)) {
				List<Map<String, Object>> userInfo = new ArrayList<Map<String, Object>>();
				userInfo = JsonUtil.listJson("data", response);
				if (null != userInfo) {
					mBaseUserDao.upDateUser(userInfo.get(0).get("id") + "",
							"nickname", userInfo.get(0).get("nickname") + "");

					// mSp.saveUsername(userInfo.get(0).get("username")
					// + "");
				}
				LogTest.LogMsg(TAG, "昵称修改成功");
				Intent intent = new Intent();
				intent.setAction(Constants.USERINFO_CHANGE_ACTION);
				sendBroadcast(intent);
				finish();
				AnimationUtil.finishHaveFloatActivityAnimation(this);

			} else if ("402".equals(code)) {
				Utils.showToast(ChangeNicknameActivity.this, "昵称已存在",
						Toast.LENGTH_SHORT);
				LogTest.LogMsg(TAG, "昵称已存在");
			} else {
				LogTest.LogMsg(TAG, "昵称修改失败");
				Utils.showToast(ChangeNicknameActivity.this, "昵称修改失败",
						Toast.LENGTH_SHORT);
			}

		} else {
			Utils.showToast(ChangeNicknameActivity.this, "连接服务器超时 ",
					Toast.LENGTH_SHORT);
		}
	}
	
	@Override
	public void netWork() {
//		mTitleBarRl.setVisibility(View.VISIBLE);
		mNetWorkLayout.setVisibility(View.GONE);
	}
	
	@Override
	public void noNetWork() {
		mNetWorkLayout.setVisibility(View.VISIBLE);
//		mTitleBarRl.setVisibility(View.GONE);
	}

	@Override
	protected void onStart() {
		super.onStart();
		LogTest.LogMsg(TAG, "ChangeUsernamActivity-onStart");
	}

	@Override
	protected void onResume() {
		super.onResume();
		LogTest.LogMsg(TAG, "ChangeUsernamActivity-onResume");

	}

	@Override
	protected void onPause() {
		super.onPause();
		LogTest.LogMsg(TAG, "ChangeUsernamActivity-onPause");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		LogTest.LogMsg(TAG, "ChangeUsernamActivity-onDestroy");
	}
}
