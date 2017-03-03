package com.kincai.store.ui.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.kincai.store.Constants;
import com.kincai.store.R;
import com.kincai.store.bean.UserInfo;
import com.kincai.store.common.LoadUserFace;
import com.kincai.store.db.UserDao;
import com.kincai.store.thread.CachedThreadPoolUtils;
import com.kincai.store.thread.HttpGetImgThread;
import com.kincai.store.thread.HttpPostThread;
import com.kincai.store.ui.activity.base.BaseSlideActivity;
import com.kincai.store.utils.JsonUtil;
//import com.kincai.store.utils.LoadingDialog;
import com.kincai.store.utils.AnimationUtil;
import com.kincai.store.utils.EncryptionUtil;
import com.kincai.store.utils.LogTest;
import com.kincai.store.utils.NetWorkUtil;
import com.kincai.store.utils.SPStorageUtil;
import com.kincai.store.utils.Utils;
import com.kincai.store.utils.bitmap_cache.BitmapLocaFileCacheUtil;
import com.kincai.store.view.custom.CircleImageView;
import com.kincai.store.view.custom.CleanableAndVoiceEditTextView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.provider.Settings;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 *
 * @author kincai
 * 
 * @description 用户登录
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.ui
 *
 * @time 2015-7-24 下午7:28:53
 *
 */
@SuppressWarnings("unused")
public class UserLoginActivity extends BaseSlideActivity {
	private static final String TAG = "UserLoginActivity";
	private CleanableAndVoiceEditTextView mUsernameEt;

	private CleanableAndVoiceEditTextView mPasswordEt;
	private Button mLoginBtn;
	private TextView mRegisterTv;
	private CircleImageView mUserFaceIv;
	private ToggleButton mPwdEtToggleButton;
	private TextView mNetTipTvTv;

	private List<UserInfo> mUserList;

	private MyHandler mMyHandler;

	private MyTextWatcher myTextWatcher;
	
	private boolean ready;
	public String mPhoneNum;
	private String mFacePath;
	private Bitmap mMyFaceBitmap;
	
	private BitmapLocaFileCacheUtil mFileUtils;
	
	// 短信注册，随机产生头像
	private static final String[] AVATARS = {
			"http://tupian.qqjay.com/u/2011/0729/e755c434c91fed9f6f73152731788cb3.jpg",
			"http://99touxiang.com/public/upload/nvsheng/125/27-011820_433.jpg",
			"http://img1.touxiang.cn/uploads/allimg/111029/2330264224-36.png",
			"http://img1.2345.com/duoteimg/qqTxImg/2012/04/09/13339485237265.jpg",
			"http://diy.qqjay.com/u/files/2012/0523/f466c38e1c6c99ee2d6cd7746207a97a.jpg",
			"http://img1.touxiang.cn/uploads/20121224/24-054837_708.jpg",
			"http://img1.touxiang.cn/uploads/20121212/12-060125_658.jpg",
			"http://img1.touxiang.cn/uploads/20130608/08-054059_703.jpg",
			"http://diy.qqjay.com/u2/2013/0422/fadc08459b1ef5fc1ea6b5b8d22e44b4.jpg",
			"http://img1.2345.com/duoteimg/qqTxImg/2012/04/09/13339510584349.jpg",
			"http://img1.touxiang.cn/uploads/20130515/15-080722_514.jpg",
			"http://diy.qqjay.com/u2/2013/0401/4355c29b30d295b26da6f242a65bcaad.jpg"

	};
	private String usernameStr;
	private String passwordStr;
	

	@Override
	public int initContentView() {
		return R.layout.activity_login_layout;
	}
	@Override
	public void initDatas() {
		init();
		initView();
		setListener();;
	}
	

	/**
	 * 初始化
	 */
	private void init() {
		mFileUtils = new BitmapLocaFileCacheUtil(UserLoginActivity.this);
		mMyHandler = new MyHandler();
		myTextWatcher = new MyTextWatcher();
		
		

	}

	/**
	 * 初始化短信验证SMSSDK
	 */
	private void initSMSSDK() {
		// 初始化短信SDK
		SMSSDK.initSDK(this, Constants.SMSSDK_APPKEY, Constants.SMSSDK_APPSECRET);
//		final Handler handler = new Handler(this);
		/*EventHandler eventHandler = new EventHandler() {
			public void afterEvent(int event, int result, Object data) {
				Message msg = new Message();
				msg.what = 0x222;
				msg.arg1 = event;
				msg.arg2 = result;
				msg.obj = data;
				mMyHandler.sendMessage(msg);
			}
		};
		// 注册回调监听接口
		SMSSDK.registerEventHandler(eventHandler);*/
		ready = true;

		// 获取新好友个数
//		showDialog();
		SMSSDK.getNewFriendsCount();

	}
	
	/**
	 * 初始化控件
	 */
	public void initView() {
		super.initView();
		mUserFaceIv = (CircleImageView) findViewById(R.id.register_login_logo_iv);
		mUsernameEt = (CleanableAndVoiceEditTextView) findViewById(R.id.username_et);
		mPasswordEt = (CleanableAndVoiceEditTextView) findViewById(R.id.password_et);
		mPwdEtToggleButton = (ToggleButton) findViewById(R.id.pwd_toggle_btn);
		mLoginBtn = (Button) findViewById(R.id.login_btn);
		mRegisterTv = (TextView) findViewById(R.id.phone_register_tv);
		mNetTipTvTv = (TextView) findViewById(R.id.fragment_nerwork_state_tv);

		if ((mUsernameEt != null && mUsernameEt.getText().toString().equals(""))) {
			mLoginBtn.setEnabled(false);
			mLoginBtn.setBackgroundResource(R.drawable.dialog_btn_left_bg_down);
		}
		String username = mSp.getUsername();
		
		//设置登陆过的用户
		mUsernameEt.setText(username == null ? "" : username);
		loadUserFace(username);

	}
	
	/**
	 * 监听输入框时加载判断用户是否有头像(有登陆过就能获取到头像)
	 * @param username
	 */
	private void loadUserFace(String username) {
		List<UserInfo> mUserInfos = mBaseUserDao.getUserInfoOne(mSp.getUsername());
		LogTest.LogMsg(TAG, "用户查询是否不为空" + (mUserInfos != null));

		if (null != mUserInfos) {
			mFacePath = mUserInfos.get(0).getFace();

			LoadUserFace.userFace(this, mMyHandler, mFacePath, mUserFaceIv);
			
		} else {
			mUserFaceIv.setImageResource(R.drawable.ic_useraccount_headview);
		}
		
	}

	/**
	 * 设置监听
	 */
	public void setListener() {
		super.setListener();
		mLoginBtn.setOnClickListener(this);
		mRegisterTv.setOnClickListener(this);
		mUsernameEt.addTextChangedListener(myTextWatcher);
		mPasswordEt.addTextChangedListener(myTextWatcher);
		mPwdEtToggleButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				mPwdEtToggleButton.setChecked(isChecked);
						mPasswordEt.setInputType(isChecked 
								? InputType.TYPE_CLASS_TEXT
										: InputType.TYPE_CLASS_TEXT 
										| InputType.TYPE_TEXT_VARIATION_PASSWORD);
			}
		});
	}

	/**
	 * edittext实时监听
	 */
	class MyTextWatcher implements TextWatcher {
		private CharSequence temp;
		private int editStartName;
		private int editEndName;
		private int editStartPwd;
		private int editEndPwd;

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			temp = s;
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// mTextView.setText(s);//将输入的内容实时显示
		}

		@Override
		public void afterTextChanged(Editable s) {
			
			LogTest.LogMsg(TAG, "-->login 进入输入框监听");
			editStartName = mUsernameEt.getSelectionStart();
			editEndName = mUsernameEt.getSelectionEnd();

			editStartPwd = mPasswordEt.getSelectionStart();
			editEndPwd = mPasswordEt.getSelectionEnd();
			
			String userEtStr = mUsernameEt.getText().toString();
			if(userEtStr.trim().length() > 9 || userEtStr.trim().length() == 0) 
			loadUserFace(mUsernameEt.getText().toString());
			// 当输入框有一个为空就让按钮不能点击
			if (mUsernameEt.getText().toString().equals("")
					|| mPasswordEt.getText().toString().equals("")
					|| (mUsernameEt.getText().toString().equals("") && !mPasswordEt
							.getText().toString().equals(""))
					|| (!mUsernameEt.getText().toString().equals("") && mPasswordEt
							.getText().toString().equals(""))) {
				mLoginBtn.setEnabled(false);
				mLoginBtn.setBackgroundResource(R.drawable.dialog_btn_left_bg_down);
			} else if (mUsernameEt.getText().toString().length() > 0) {
				
				mLoginBtn.setEnabled(true);
				mLoginBtn.setBackgroundResource(R.drawable.dialog_btn_right_bg);
			}

		}
	}

	@SuppressLint("HandlerLeak")
	class MyHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			// case 202:
			//
			// break;
			case 207:
				loginState(msg);
				break;
			case 0x222://短信验证
//				phoneSmsVerify(msg);
				break;
			case 202:
				
				LoadUserFace.getUserFaceResponse(UserLoginActivity.this, mUserFaceIv, mFacePath, msg);

				
				break;

			}
		}
	}
	
	/**
	 * 短信验证返回
	 * @param msg
	 */
	private void phoneSmsVerify(Message msg) {
		int event = msg.arg1;
		int result = msg.arg2;
		Object data = msg.obj;
		//提交应用内的用户资料
		if (event == SMSSDK.EVENT_SUBMIT_USER_INFO) {
			// 短信注册成功后，返回MainActivity,然后提示新好友
			if (result == SMSSDK.RESULT_COMPLETE) {
				
//				Utils.showToast(UserLoginActivity.this, getResources().getString(R.string.smssdk_user_info_submited), 
//						Toast.LENGTH_SHORT);
				
				startActivity(new Intent(UserLoginActivity.this,
						UserRegisterActivity.class).putExtra("phoneNum",mPhoneNum ));
				AnimationUtil.startHaveSinkActivityAnimation(this);
				
			} else {
				((Throwable) data).printStackTrace();
			}
		} else if (event == SMSSDK.EVENT_GET_NEW_FRIENDS_COUNT){
			if (result == SMSSDK.RESULT_COMPLETE) {
				refreshViewCount(data);
			} else {
				((Throwable) data).printStackTrace();
			}
		}
		
	}
	
	/**
	 *  更新，新好友个数
	 * @param data
	 */
	private void refreshViewCount(Object data) {
		int newFriendsCount = 0;
		try {
			newFriendsCount = Integer.parseInt(String.valueOf(data));
		} catch (Throwable t) {
			newFriendsCount = 0;
		}
		if (newFriendsCount > 0) {
//			Utils.showToast(this, String.valueOf(newFriendsCount),
//					Toast.LENGTH_SHORT);
		} else {
			// tvNum.setVisibility(View.GONE);
		}
		// if (pd != null && pd.isShowing()) {
		// pd.dismiss();
		// }
	}

	/**
	 * 登录状态 是否成功
	 * 
	 * @param msg
	 */
	private void loginState(Message msg) {
		String response = (String) msg.obj;
		if (null != response) {
			String code = JsonUtil.msgJson("code", response);
			if ("200".equals(code)) {
				
				LogTest.LogMsg("--测试login--response:"+response);
				mUserList = new ArrayList<UserInfo>();
				mUserList = JsonUtil.getList("data", response);
				if (null != mUserList) {
					LogTest.LogMsg("--测试login--mUserList:"+mUserList.toString());
					mSp.saveUserIsLogin(true);
					mSp.saveUsername(mUserList.get(0).getUsername());
					LogTest.LogMsg(TAG, mSp.getUserIsLogin() + " 登录状态");
					LogTest.LogMsg(TAG, mUserList.toString());
					List<UserInfo> userInfos = mBaseUserDao.getUserInfoOne(mSp.getUsername());
					if(userInfos == null)
					mBaseUserDao.saveUserInfo(mUserList);
					
					//登录环信
					loginHx(usernameStr, passwordStr);
				}
				// mLoadingDialog.dismiss();
				// Utils.showToast(UserLoginActivity.this, "登录成功",
				// Toast.LENGTH_SHORT);
				Intent intent = new Intent();
				intent.setAction(Constants.USERINFO_CHANGE_ACTION);
				sendBroadcast(intent);
				finish();
				AnimationUtil.finishHaveFloatActivityAnimation(this);

			} else if ("401".equals(code)) {
				LogTest.LogMsg(TAG, "密码错误");
				setFocusableAndEnable(getString(R.string.login_str), true);
				getFocusable();
				// Utils.showToast(UserLoginActivity.this, "登录失败",
				// Toast.LENGTH_SHORT);
			} else if ("403".equals(code)) {
				setFocusableAndEnable(getString(R.string.login_str), true);
				getFocusable();
				// Utils.showToast(UserLoginActivity.this, "登录失败",
				// Toast.LENGTH_SHORT);
				LogTest.LogMsg(TAG, "用户不存在");
			} else {
				setFocusableAndEnable(getString(R.string.login_str), true);
				getFocusable();
				
				// Utils.showToast(UserLoginActivity.this, "登录失败",
				// Toast.LENGTH_SHORT);
			}

			if ("408".equals(response)) {
				// mNetWorkLayout.setVisibility(View.VISIBLE);
				// mNetTipTvTv.setText("网络连接超时");
				// mBackIv.setVisibility(View.GONE);
			}

			// LogTest.LogMsg(TAG, "登录返回数据 " + mUserList + "");
		} else {
			setFocusableAndEnable(getString(R.string.login_str), true);
			getFocusable();
			Utils.showToast(UserLoginActivity.this, "登录失败", Toast.LENGTH_SHORT);
		}

		// mLoadingDialog.dismiss();

	}
	
	private void loginHx(String userName, String password) {

		EMChatManager.getInstance().login(userName,password,new EMCallBack() {//回调
			@Override
			public void onSuccess() {
				runOnUiThread(new Runnable() {
					public void run() {
						EMGroupManager.getInstance().loadAllGroups();
						EMChatManager.getInstance().loadAllConversations();
						LogTest.LogMsg("环信登录", "登录成功");
						Utils.showToast(UserLoginActivity.this, "环信登录 登录成功", 0);
					}
				});
			}
		 
			@Override
			public void onProgress(int progress, String status) {
		 
			}
		 
			@Override
			public void onError(int code, String message) {
				LogTest.LogMsg("环信登录", "登录失败");
				runOnUiThread(new Runnable() {
					public void run() {
						Utils.showToast(UserLoginActivity.this, "环信登录 登录失败", 0);
						
					}});
				
			}
		});

	}
	
	/**
	 * 设置EditText焦点和是否可点击 登录按钮文本和是否可点击
	 * @param btnText 按钮文本
	 * @param isTrue 是否
	 */
	private void setFocusableAndEnable(String btnText, boolean isTrue) {
		mLoginBtn.setBackgroundResource(isTrue ? R.drawable.dialog_btn_right_bg : R.drawable.dialog_btn_left_bg_down);
		mLoginBtn.setText(btnText);
		mLoginBtn.setEnabled(isTrue);
		mUsernameEt.setEnabled(isTrue);
		mPasswordEt.setEnabled(isTrue);
		mUsernameEt.setFocusable(isTrue);
		mPasswordEt.setFocusable(isTrue);
		
		
	}
	
	/**
	 * EditText重新获得焦点
	 */
	private void getFocusable() {
		mUsernameEt.setFocusableInTouchMode(true);
		mPasswordEt.setFocusableInTouchMode(true);
		mUsernameEt.requestFocus();
		mPasswordEt.requestFocus();
		mUsernameEt.findFocus();
		mPasswordEt.findFocus();
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
		case R.id.login_btn:
			login();
			break;
		case R.id.phone_register_tv://手机注册
			
			initSMSSDK();
			
			openResgisterPage();
			
//			startActivity(new Intent(UserLoginActivity.this,
//					UserRegisterActivity.class));
//			AnimationUtil.activityStartAnimation(this);
			break;

		default:
			break;
		}
	}

	/**
	 * 打开注册界面
	 */
	private void openResgisterPage() {

		// 打开注册页面
		RegisterPage registerPage = new RegisterPage();
		registerPage.setRegisterCallback(new EventHandler() {
			public void afterEvent(int event, int result, Object data) {
				// 解析注册结果
				if (result == SMSSDK.RESULT_COMPLETE) {
					@SuppressWarnings("unchecked")
					HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
					String country = (String) phoneMap.get("country");
					String phone = (String) phoneMap.get("phone");
					mPhoneNum = phone;
					// 提交用户信息
					registerUser(country, phone);
					
					
					startActivity(new Intent(UserLoginActivity.this,
							UserRegisterActivity.class).putExtra("phoneNum",mPhoneNum ));
					AnimationUtil.startHaveSinkActivityAnimation(UserLoginActivity.this);
				}
			}
		});
		
		registerPage.show(this);
	}
	
	/**
	 *  提交用户信息
	 * @param country
	 * @param phone
	 */
	private void registerUser(String country, String phone) {
		Random rnd = new Random();
		int id = Math.abs(rnd.nextInt());
		String uid = String.valueOf(id);
		String nickName = "SmsSDK_User_" + uid;
		String avatar = AVATARS[id % 12];
		SMSSDK.submitUserInfo(uid, nickName, avatar, country, phone);
	}

	/**
	 * 登陆
	 */
	private void login() {
		usernameStr = mUsernameEt.getText().toString().trim();
		passwordStr = mPasswordEt.getText().toString().trim();
		boolean usernameBool = usernameStr
				.matches(Constants.USERNAME_RESTRICTION);
		boolean PasswordBool = passwordStr
				.matches(Constants.PASSWORD_RESTRICTION);
		if (usernameBool && PasswordBool) {
			if (NetWorkUtil.isNetworkAvailable(UserLoginActivity.this)) {
				// mLoadingDialog.dialogShow();

				setFocusableAndEnable(getString(R.string.In_the_login), false);
				LogTest.LogMsg(TAG, "加密前密码 :"
						+ mPasswordEt.getText().toString());
				LogTest.LogMsg(TAG,"加密后密码 :"
								+ EncryptionUtil.md5Encryption(mPasswordEt.getText().toString()));

				List<NameValuePair> parameters = new ArrayList<NameValuePair>();
				parameters.add(new BasicNameValuePair("username", mUsernameEt
						.getText().toString()));
				parameters.add(new BasicNameValuePair("password", EncryptionUtil.md5Encryption(mPasswordEt.getText()
						.toString())));
				parameters.add(new BasicNameValuePair("device_id", "0"));//android
				parameters.add(new BasicNameValuePair("unique_id", Utils.getUniqueId(UserLoginActivity.this)));
				CachedThreadPoolUtils.execute(new HttpPostThread(
						mMyHandler, Constants.API_URL
								+ "appUserLoginApi.php", parameters, "login"));
			} else {
				LogTest.LogMsg(TAG, "账号密码格式正确  没网络");
			}

		} else if (!usernameBool && PasswordBool) {
			LogTest.LogMsg(TAG, "登录账号格式不对");
			// Utils.showToast(UserLoginActivity.this, "登录失败",
			// Toast.LENGTH_SHORT);
		} else if (usernameBool && !PasswordBool) {
			LogTest.LogMsg(TAG, "登录密码格式不对");
			// Utils.showToast(UserLoginActivity.this, "登录失败",
			// Toast.LENGTH_SHORT);
		} else {
			LogTest.LogMsg(TAG, "登录账号和密码格式不对");
			
			// Utils.showToast(UserLoginActivity.this, "登录失败",
			// Toast.LENGTH_SHORT);
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
		LogTest.LogMsg(TAG, "UserLoginActivity-onStart");
	}

	@Override
	protected void onResume() {
		if (ready) {
			// 获取新好友个数
			SMSSDK.getNewFriendsCount();
		}
		super.onResume();
		LogTest.LogMsg(TAG, "UserLoginActivity-onResume");

	}

	@Override
	protected void onPause() {
		super.onPause();
		LogTest.LogMsg(TAG, "UserLoginActivity-onPause");
	}

	@Override
	protected void onDestroy() {
		if (ready) {
			// 销毁回调监听接口
			SMSSDK.unregisterAllEventHandler();
		}
		super.onDestroy();
		LogTest.LogMsg(TAG, "UserLoginActivity-onDestroy");
	}

}
