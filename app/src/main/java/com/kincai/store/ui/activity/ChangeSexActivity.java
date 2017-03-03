package com.kincai.store.ui.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.kincai.store.Constants;
import com.kincai.store.R;
import com.kincai.store.bean.UserInfo;
import com.kincai.store.thread.CachedThreadPoolUtils;
import com.kincai.store.thread.HttpPostThread;
import com.kincai.store.ui.activity.base.BaseSlideActivity;
import com.kincai.store.utils.AnimationUtil;
import com.kincai.store.utils.JsonUtil;
import com.kincai.store.utils.LogTest;
import com.kincai.store.utils.NetWorkUtil;
import com.kincai.store.utils.Utils;

import android.annotation.SuppressLint;
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
 * @description 修改性别
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.ui
 *
 * @time 2015-7-24 下午7:39:49
 *
 */
public class ChangeSexActivity extends BaseSlideActivity {
	private static final String TAG = "ChangeSexActivity";
	private TextView mTitleTv;
	private TextView mManRl;
	private TextView mWomanRl;
	private TextView mSecrecyRl;

	private MyHandler mMyHandler;

	
	@Override
	public int initContentView() {
		return R.layout.activity_change_sex_layout;
	}
	@Override
	public void initDatas() {
		init();
		initView();
		setListener();
	}
	
	private void init() {
		mMyHandler = new MyHandler();
	}

	public void initView() {
		super.initView();
		mTitleTv = (TextView) findViewById(R.id.title_tv);
		mManRl = (TextView) findViewById(R.id.man_rl);
		mWomanRl = (TextView) findViewById(R.id.woman_rl);
		mSecrecyRl = (TextView) findViewById(R.id.secrecy_rl);

		mTitleTv.setText(getResources().getString(R.string.user_sex_str));
	}

	public void setListener() {
		super.setListener();
		mManRl.setOnClickListener(this);
		mWomanRl.setOnClickListener(this);
		mSecrecyRl.setOnClickListener(this);

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
		case R.id.man_rl:
			
			selectSex( getResources().getString(R.string.man_str));
			break;
		case R.id.woman_rl:
			
			selectSex(getResources().getString(R.string.woman_str));
			
			break;
		case R.id.secrecy_rl:
			selectSex(getResources().getString(R.string.secrecy_str));
			break;

		}

	}

	/**
	 * 选择性别提交请求
	 * @param sex
	 */
	private void selectSex(String sex) {
		List<UserInfo> mUserInfos = mBaseUserDao.getUserInfoOne(mSp.getUsername());
		if (NetWorkUtil.isNetworkAvailable(ChangeSexActivity.this) && mUserInfos != null) {
			
			LogTest.LogMsg(TAG, "选择用户id为: "+mUserInfos.get(0).getUserId()+"的用户性别: "+sex);

			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			parameters.add(new BasicNameValuePair("userId", String.valueOf(mUserInfos.get(0).getUserId())));
			parameters.add(new BasicNameValuePair("userInfo", sex));
			parameters.add(new BasicNameValuePair("flag", "sex"));

			CachedThreadPoolUtils.execute(new HttpPostThread(mMyHandler,
					Constants.API_URL + "appChangeUserInfoApi.php", parameters,
					"changeUserInfo"));
		}

	}

	@SuppressLint("HandlerLeak")
	class MyHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 208:
				String response = (String) msg.obj;
				LogTest.LogMsg(TAG, "response->" + response);
				if (response != null) {
					String code = JsonUtil.msgJson("code", response);
					LogTest.LogMsg(TAG, "code->" + code);
					if ("200".equals(code)) {
						List<Map<String, Object>> userInfo = new ArrayList<Map<String, Object>>();
						userInfo = JsonUtil.listJson("data", response);
						if (null != userInfo) {
							LogTest.LogMsg("json", userInfo.toString());

							mBaseUserDao.upDateUser(userInfo.get(0).get("id") + "",
									"sex", userInfo.get(0).get("sex") + "");

//							mSp.saveUsername(userInfo.get(0).get("username")
//									+ "");

						}
						LogTest.LogMsg(TAG, "性别修改成功");
						Intent intent = new Intent();
						intent.setAction(Constants.USERINFO_CHANGE_ACTION);
						sendBroadcast(intent);
						finish();
						AnimationUtil.finishHaveFloatActivityAnimation(ChangeSexActivity.this);
					} else {
						LogTest.LogMsg(TAG, "性别修改失败");
						Utils.showToast(ChangeSexActivity.this, "性别修改失败",
								Toast.LENGTH_SHORT);
						finish();
						AnimationUtil.finishHaveFloatActivityAnimation(ChangeSexActivity.this);
					}

				} else {
					finish();
					AnimationUtil.finishHaveFloatActivityAnimation(ChangeSexActivity.this);
				}

				break;

			default:
				break;
			}
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
		LogTest.LogMsg(TAG, "ChangeSexActivity-onStart");
	}

	@Override
	protected void onResume() {
		super.onResume();
		LogTest.LogMsg(TAG, "ChangeSexActivity-onResume");

	}

	@Override
	protected void onPause() {
		super.onPause();
		LogTest.LogMsg(TAG, "ChangeSexActivity-onPause");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		LogTest.LogMsg(TAG, "ChangeSexActivity-onDestroy");
	}
}
