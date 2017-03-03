package com.kincai.store.ui.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.easemob.EMError;
import com.easemob.chat.EMChatManager;
import com.easemob.exceptions.EaseMobException;
import com.kincai.store.Constants;
import com.kincai.store.R;
import com.kincai.store.thread.CachedThreadPoolUtils;
import com.kincai.store.thread.HttpPostThread;
import com.kincai.store.ui.activity.base.BaseSlideActivity;
import com.kincai.store.utils.AnimationUtil;
import com.kincai.store.utils.EncryptionUtil;
import com.kincai.store.utils.JsonUtil;
import com.kincai.store.utils.LogTest;
import com.kincai.store.utils.NetWorkUtil;
import com.kincai.store.utils.Utils;
import com.kincai.store.view.custom.CleanableAndVoiceEditTextView;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 *
 * @author kincai
 * 
 * @description 用户注册
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.ui
 *
 * @time 2015-7-24 下午7:28:26
 *
 */
public class UserRegisterActivity extends BaseSlideActivity {

	private static final String TAG = "UserRegisterActivity";
	private CleanableAndVoiceEditTextView mUsernameEt;
	private CleanableAndVoiceEditTextView mPasswordEt;
	private Button mRegisterBtn;
	private ToggleButton mPwdEtToggleButton;
	private MyTextWatcher mMyTextWatcher;

	private MyHandler mMyHandler;
	public String mPhoneNum;
	private String userString;
	private String passString;

	
	@Override
	public int initContentView() {
		return R.layout.activity_register_layout;
	}
	@Override
	public void initDatas() {
		init();
		initView();
		setListener();
	}
	
	private void init() {
		mMyHandler = new MyHandler();
		mMyTextWatcher = new MyTextWatcher();
		
		getIntentData();
	}
	
	private void getIntentData() {
		Intent intent = getIntent();
		mPhoneNum = (intent == null) ? "" : intent.getStringExtra("phoneNum");
	}

	/**
	 * 初始化控件
	 */
	public void initView() {
		super.initView();
		mRegisterBtn = (Button) findViewById(R.id.register_btn);
		mUsernameEt = (CleanableAndVoiceEditTextView) findViewById(R.id.username_et);
		mPasswordEt = (CleanableAndVoiceEditTextView) findViewById(R.id.password_et);
		mPwdEtToggleButton = (ToggleButton) findViewById(R.id.pwd_toggle_btn);
		if ((mUsernameEt != null && mUsernameEt.getText().toString().equals(""))) {
			mRegisterBtn.setEnabled(false);
			mRegisterBtn.setBackgroundResource(R.drawable.dialog_btn_left_bg_down);
		}

		mUsernameEt.setText(mPhoneNum);

	}

	/**
	 * 设置监听
	 */
	public void setListener() {
		super.setListener();
		mRegisterBtn.setOnClickListener(this);
		mUsernameEt.addTextChangedListener(mMyTextWatcher);
		mPasswordEt.addTextChangedListener(mMyTextWatcher);
		mPwdEtToggleButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				mPasswordEt.setInputType(isChecked 
						? InputType.TYPE_CLASS_TEXT 
								: InputType.TYPE_CLASS_TEXT 
								| InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
			}
		});
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
		case R.id.register_btn:

			register();

			break;
		}
	}

	/**
	 * 用户祖册
	 */
	private void register() {
		userString = mUsernameEt.getText().toString().trim();
		passString = mPasswordEt.getText().toString().trim();
		// 用户账号密码格判断
		boolean usernamebl = userString
				.matches(Constants.USERNAME_RESTRICTION);
		boolean passwordbl = passString
				.matches(Constants.PASSWORD_RESTRICTION);

		if (usernamebl && passwordbl) {

			if (NetWorkUtil.isNetworkAvailable(UserRegisterActivity.this)) {
				mRegisterBtn.setText("正在注册...");
				mRegisterBtn.setEnabled(false);
				LogTest.LogMsg(TAG, "加密前密码 :"
						+ mPasswordEt.getText().toString());
				LogTest.LogMsg(TAG,"加密后密码 :"+ EncryptionUtil.md5Encryption(
						mPasswordEt.getText().toString()));

				List<NameValuePair> parameters = new ArrayList<NameValuePair>();
				parameters.add(new BasicNameValuePair("username", mUsernameEt
						.getText().toString()));
				parameters.add(new BasicNameValuePair("password",
						EncryptionUtil.md5Encryption(mPasswordEt.getText()
								.toString())));
				CachedThreadPoolUtils.execute(new HttpPostThread(mMyHandler,
						Constants.API_URL + "appUserRegisterApi.php", parameters,
						"reg"));

				LogTest.LogMsg(TAG, "账号密码格式正确  有网络");
			} else {
				LogTest.LogMsg(TAG, "账号密码格式正确  没网络");
			}

		} else if (!usernamebl && passwordbl) {
			LogTest.LogMsg(TAG, "账号格式不对");
		} else if (usernamebl && !passwordbl) {
			LogTest.LogMsg(TAG, "密码格式不对");
		} else {
			LogTest.LogMsg(TAG, "账号和密码格式不对");
		}
	}

	@SuppressLint("HandlerLeak")
	class MyHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 2070:
				registerState(msg);
				break;
			}
		}
	}

	/**
	 * 注册状态 是否注册成功
	 * 
	 * @param msg
	 */
	private void registerState(Message msg) {
		String register = (String) msg.obj;
		if (register != null) {
			String redisterRsp = JsonUtil.msgJson("code", register);
			if (redisterRsp != null) {
				if ("200".equals(redisterRsp)) {
					Utils.showToast(UserRegisterActivity.this, "注册成功",
							Toast.LENGTH_SHORT);
					
					registerHx(userString, passString);

					finish();
					AnimationUtil.finishHaveFloatActivityAnimation(this);
				} else if ("402".equals(redisterRsp)) {
					mRegisterBtn.setText("注册");
					mRegisterBtn.setEnabled(true);
					Utils.showToast(UserRegisterActivity.this, "手机号码已被注册过",
							Toast.LENGTH_SHORT);
				} else {
					mRegisterBtn.setText("注册");
					mRegisterBtn.setEnabled(true);
					Utils.showToast(UserRegisterActivity.this, "注册失败",
							Toast.LENGTH_SHORT);
				}
			}
		}

		// mLoadingDialog.dismiss();
	}
	
	private void registerHx(final String username, final String pwd) {
		new Thread(new Runnable() {
		    public void run() {
		      try {
		         // 调用sdk注册方法
		         EMChatManager.getInstance().createAccountOnServer(username, pwd);
		      } catch (final EaseMobException e) {
		      //注册失败
				int errorCode=e.getErrorCode();
				if(errorCode==EMError.NONETWORK_ERROR){
				    LogTest.LogMsg("环信注册", "网络异常，请检查网络！");
				}else if(errorCode==EMError.USER_ALREADY_EXISTS){
					LogTest.LogMsg( "用户已存在！");
				}else if(errorCode==EMError.UNAUTHORIZED){
					LogTest.LogMsg("注册失败，无权限！");
				}else{
					LogTest.LogMsg("注册失败: " + e.getMessage());
				}
		      }
		      
		    }
		}).start();

	}

	/**
	 * edittext实时监听
	 */
	class MyTextWatcher implements TextWatcher {
		@SuppressWarnings("unused")
		private CharSequence temp;
		private int editStart;
		private int editEnd;
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
			editStart = mUsernameEt.getSelectionStart();
			editEnd = mUsernameEt.getSelectionEnd();

			editStartPwd = mPasswordEt.getSelectionStart();
			editEndPwd = mPasswordEt.getSelectionEnd();

			// 当输入框有一个为空就让按钮不能点击
			if (mUsernameEt.getText().toString().equals("")
					|| mPasswordEt.getText().toString().equals("")
					|| (mUsernameEt.getText().toString().equals("") && !mPasswordEt
							.getText().toString().equals(""))
					|| (!mUsernameEt.getText().toString().equals("") && mPasswordEt
							.getText().toString().equals(""))) {
				mRegisterBtn.setEnabled(false);
				mRegisterBtn.setBackgroundResource(R.drawable.dialog_btn_left_bg_down);
			} else if (mUsernameEt.getText().toString().length() > 0) {
				mRegisterBtn.setEnabled(true);
				mRegisterBtn.setBackgroundResource(R.drawable.dialog_btn_left_bg);
			}

			if (mUsernameEt.getText().toString().length() > 11) {
				Utils.showToast(UserRegisterActivity.this, "请输入正确手机号码",
						Toast.LENGTH_SHORT);
				s.delete(editStart - 1, editEnd);
				int tempSelection = editStart;
				mUsernameEt.setText(s);
				mUsernameEt.setSelection(tempSelection);
			}

			if (mPasswordEt.getText().toString().length() > 16) {
				Utils.showToast(UserRegisterActivity.this, "密码长度不能超过16位数字或字母",
						Toast.LENGTH_SHORT);
				s.delete(editStartPwd - 1, editEndPwd);
				int tempSelection = editStartPwd;
				mPasswordEt.setText(s);
				mPasswordEt.setSelection(tempSelection);
			}

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
		LogTest.LogMsg(TAG, "UserRegisterActivity-onStart");
	}

	@Override
	protected void onResume() {
		super.onResume();

		LogTest.LogMsg(TAG, "UserRegisterActivity-onResume");

	}

	@Override
	protected void onPause() {
		super.onPause();
		LogTest.LogMsg(TAG, "UserRegisterActivity-onPause");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		LogTest.LogMsg(TAG, "UserRegisterActivity-onDestroy");

	}
}
