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
import com.kincai.store.utils.EncryptionUtil;
import com.kincai.store.utils.JsonUtil;
import com.kincai.store.utils.LoadingDialog;
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
 * @description 修改登录密码
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.ui
 *
 * @time 2015-7-24 下午7:36:54
 *
 */
public class ChangeLoginPwdActivity extends BaseSlideActivity {

	private static final String TAG = "ChangeLoginPwdActivity";
	private TextView mConfirmTv;
	private CleanableAndVoiceEditTextView mPasswordEt;
	private TextView mTitleTv;

	private MyHandler mMyHandler;

	private LoadingDialog mLoadingDialog;

	@Override
	public int initContentView() {
		return R.layout.activity_change_loginpwd_layout;
	}
	@Override
	public void initDatas() {
		mMyHandler = new MyHandler();
		mLoadingDialog = new LoadingDialog(ChangeLoginPwdActivity.this, false);
		initView();
		setListener();
	}
	
	/**
	 * 初始化控件
	 */
	public void initView() {
		super.initView();
		mConfirmTv = (TextView) findViewById(R.id.titlebar_confirm_change_iv);
		mPasswordEt = (CleanableAndVoiceEditTextView) findViewById(R.id.loginpwd_et);
		mTitleTv = (TextView) findViewById(R.id.title_tv);

		mTitleTv.setText(getResources().getString(R.string.loginPwd_str));

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
		// TODO Auto-generated method stub
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
			confirmChangePaw();

			break;
		}
	}

	/**
	 * 确认修改密码
	 */
	private void confirmChangePaw() {
		// 用户账号密码格判断
		boolean usernamebl = mPasswordEt.getText().toString()
				.matches(Constants.PASSWORD_RESTRICTION);
		if (usernamebl) {
			if (NetWorkUtil.isNetworkAvailable(ChangeLoginPwdActivity.this)) {
				mLoadingDialog.dialogShow();

				List<NameValuePair> parameters = new ArrayList<NameValuePair>();
				parameters.add(new BasicNameValuePair("userId", String
						.valueOf(mBaseUserDao.getUserInfoOne(mSp.getUsername())
								.get(0).getUserId())));
				parameters.add(new BasicNameValuePair("userInfo",
						EncryptionUtil.md5Encryption(mPasswordEt.getText()
								.toString())));
				parameters.add(new BasicNameValuePair("flag", "password"));

				CachedThreadPoolUtils.execute(new HttpPostThread(mMyHandler,
						Constants.API_URL + "appChangeUserInfoApi.php",
						parameters, "changeUserInfo"));
				LogTest.LogMsg(TAG, "密码格式正确  有网络");
			} else {
				LogTest.LogMsg(TAG, "密码格式正确  没网络");
			}

		} else {
			LogTest.LogMsg(TAG, "密码格式不正确");
		}
	}

	@SuppressLint("HandlerLeak")
	class MyHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 208:

				getResponseChangePawData(msg);
				break;
			}
		}
	}
	
	/**
	 * 获取返回的修改登录密码数据
	 * 
	 * @param msg
	 */
	private void getResponseChangePawData(Message msg) {
		mLoadingDialog.dismiss();
		String response = (String) msg.obj;
		if (response != null) {
			String code = JsonUtil.msgJson("code", response);
			if ("200".equals(code)) {
				List<Map<String, Object>> userInfo = new ArrayList<Map<String, Object>>();
				userInfo = JsonUtil.listJson("data", response);
				if (null != userInfo) {

					mBaseUserDao.upDateUser(userInfo.get(0).get("id") + "",
							"password", userInfo.get(0).get("password")
									+ "");
//					mSp.saveUsername(userInfo.get(0).get("username")
//							+ "");
				}

				LogTest.LogMsg(TAG, "修改成功");
				mSp.saveUserIsLogin(false);
				UserInfoActivity.instance.finish();
				Intent intent = new Intent();
				intent.setAction(Constants.USERINFO_CHANGE_ACTION);
				sendBroadcast(intent);
				finish();
				AnimationUtil.finishHaveFloatActivityAnimation(ChangeLoginPwdActivity.this);

			} else {
				LogTest.LogMsg(TAG, "密码修改失败");
				Utils.showToast(ChangeLoginPwdActivity.this, "密码修改失败",
						Toast.LENGTH_SHORT);
				finish();
				AnimationUtil.finishHaveFloatActivityAnimation(ChangeLoginPwdActivity.this);
			}

		} else {
			Utils.showToast(ChangeLoginPwdActivity.this, "连接服务器超时 ",
					Toast.LENGTH_SHORT);
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
		LogTest.LogMsg(TAG, "ChangeLoginPwdActivity-onStart");
	}

	@Override
	protected void onResume() {
		super.onResume();
		LogTest.LogMsg(TAG, "ChangeLoginPwdActivity-onResume");

	}

	@Override
	protected void onPause() {
		super.onPause();
		LogTest.LogMsg(TAG, "ChangeLoginPwdActivity-onPause");
	}

	@Override
	protected void onDestroy() {
		mLoadingDialog.dismiss();
		super.onDestroy();
		LogTest.LogMsg(TAG, "ChangeLoginPwdActivity-onDestroy");
	}
}
