package com.kincai.store.ui.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.kincai.store.Constants;
import com.kincai.store.R;
import com.kincai.store.thread.CachedThreadPoolUtils;
import com.kincai.store.thread.HttpPostThread;
import com.kincai.store.ui.activity.base.BaseSlideActivity;
import com.kincai.store.utils.AnimationUtil;
import com.kincai.store.utils.LoadingDialog;
import com.kincai.store.utils.LogTest;
import com.kincai.store.utils.NetWorkUtil;
import com.kincai.store.utils.Utils;
import com.kincai.store.view.custom.CleanableAndVoiceEditTextView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
 * @description 用户反馈信息
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.ui
 *
 * @time 2015-6-14 上午7:09:20
 *
 */
public class FeedbackActivity extends BaseSlideActivity {
	private TextView mTitleTv;
	/** 发送反馈信息 iv */
	private Button mSendSuggestionBtn;
	/** 反馈信息 et */
	private EditText mSuggestionMsgEt;
	/** 邮箱 rt */
	private CleanableAndVoiceEditTextView mEmailEt;
	/** 提示 tv */
	private TextView mTipContentTv;
	/** 提示数字 tv */
	private TextView mContentCount;
	private MyHandler mMyHandler;

	private LoadingDialog mLoadingDialog;

	private static final String TAG = "feedbackactivity";

	
	@Override
	public int initContentView() {
		return R.layout.activity_feedback_layout;
	}
	@Override
	public void initDatas() {
		mMyHandler = new MyHandler();
		mLoadingDialog = new LoadingDialog(FeedbackActivity.this, false);
		initView();
		setListener();
	}

	/**
	 * 初始化控件
	 */
	public void initView() {
		super.initView();
		mTitleTv = (TextView) findViewById(R.id.title_tv);
		mSendSuggestionBtn = (Button) findViewById(R.id.titlebar_send_suggestion_btn);
		mSuggestionMsgEt = (EditText) findViewById(R.id.feedback_message_et);
		mEmailEt = (CleanableAndVoiceEditTextView) findViewById(R.id.feedback_emails_et);
		mTipContentTv = (TextView) findViewById(R.id.tip_content);
		mContentCount = (TextView) findViewById(R.id.content_count);
		
		mTitleTv.setText(this.getResources().getString(R.string.my_setting_str));
		mContentCount.setText("0/200");
	}

	/**
	 * 设置监听
	 */
	public void setListener() {
		super.setListener();
		mSendSuggestionBtn.setOnClickListener(this);
		mSuggestionMsgEt.addTextChangedListener(mTextWatcher);
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
		case R.id.titlebar_send_suggestion_btn:
			sendFeedBack();

			break;
		default:
			break;
		}
	}

	/**
	 * 发送反馈
	 */
	private void sendFeedBack() {
		String suggestion = mSuggestionMsgEt.getText().toString();
		String email = mEmailEt.getText().toString();
		//正则约束
		boolean suggestionB = suggestion
				.matches(Constants.FEEDBACK_RESTRICTION);
		boolean emailB = email.matches(Constants.EMAIL_RESTRICTION);

		LogTest.LogMsg(TAG, suggestion);
		if (suggestionB && emailB) {

			mTipContentTv.setVisibility(View.GONE);
			LogTest.LogMsg(TAG, "tt");
			if (NetWorkUtil.isNetworkAvailable(FeedbackActivity.this)) {

				mLoadingDialog.dialogShow();
				List<NameValuePair> parameters = new ArrayList<NameValuePair>();
				parameters.add(new BasicNameValuePair("suggestion", suggestion));
				parameters.add(new BasicNameValuePair("email", email));
				CachedThreadPoolUtils.execute(new HttpPostThread(mMyHandler,
						Constants.API_URL + "appUserFeedbackApi.php",
						parameters, "FeedBackActivity"));
			} else {
				Utils.showToast(FeedbackActivity.this,
						getString(R.string.network_abnormal_tip_str2),
						Toast.LENGTH_SHORT);
			}
		} else if (suggestionB && !emailB) {
			LogTest.LogMsg(TAG, "邮箱格式不正确");
			mTipContentTv.setVisibility(View.VISIBLE);
			mTipContentTv.setText("邮箱格式不正确");
		} else if (!suggestionB && emailB) {
			LogTest.LogMsg(TAG, "反馈内容为6-200任意字符");
			mTipContentTv.setVisibility(View.VISIBLE);
			mTipContentTv.setText("反馈内容为6-200任意字符");
		} else {
			LogTest.LogMsg(TAG, "邮箱和反馈信息格式不正确");
			mTipContentTv.setVisibility(View.VISIBLE);
			mTipContentTv.setText("反馈内容为6-200任意字符  请输入正确的邮箱格式");
		}
	}

	/**
	 * edittext实时监听
	 */
	TextWatcher mTextWatcher = new TextWatcher() {
		private CharSequence temp;
		private int editStart;
		private int editEnd;

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			temp = s;
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			// mTextView.setText(s);//将输入的内容实时显示
		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			editStart = mSuggestionMsgEt.getSelectionStart();
			editEnd = mSuggestionMsgEt.getSelectionEnd();

			if (temp.length() > 0 && temp.length() < 200) {

				mTipContentTv.setVisibility(View.GONE);

			}
			// Utils.showToast(FeedbackActivity.this, temp.toString(),
			// Toast.LENGTH_SHORT);
			// mSuggestionMsgEt.setText("您输入了" + temp.length() + "个字符");
			if (temp.length() > 200) {
				// sparkTip();
				mTipContentTv.setVisibility(View.VISIBLE);
				mTipContentTv.setText("反馈内容不能大于200字");
				s.delete(editStart - 1, editEnd);
				int tempSelection = editStart;
				mSuggestionMsgEt.setText(s);
				mSuggestionMsgEt.setSelection(tempSelection);
			}
			mContentCount.setText(temp.length() + "/200");

		}
	};

	/**
	 * 定时任务
	 */
	public void sparkTip() {
		new Timer().schedule(new TimerTask() {
			boolean change = true;// 颜色改变状态
			int spardTimes = 0;

			@Override
			public void run() {
				// 对界面UI的更改一定要在UI线程
				runOnUiThread(new Runnable() {// 或者用handler
					@Override
					public void run() {
						if (++spardTimes > 2) {// 6次
							mTipContentTv.setVisibility(View.GONE);

							return;
						}

						// 执行闪烁
						for (int i = 0; i < 2; i++) {
							mTipContentTv
									.setVisibility((change ? View.VISIBLE
											: View.GONE));
						}

						change = !change;
					}
				});
			}
		}, 0, 3000);// 1 就是什么时候开始

	}

	@SuppressLint("HandlerLeak")
	class MyHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 220:
				getResponseSendFeedBack(msg);
				break;

			}
		}
	}
	
	/**
	 * 获取返回的发送反馈信息数据
	 * @param msg
	 */
	private void getResponseSendFeedBack(Message msg) {
		String response = (String) msg.obj;

		if ("200".equals(response)) {
			Utils.showToast(FeedbackActivity.this, "发送成功",
					Toast.LENGTH_SHORT);
			mSuggestionMsgEt.setText("");
			mEmailEt.setText("");
		} else if ("400".equals(response)) {
			Utils.showToast(FeedbackActivity.this, "发送失败",
					Toast.LENGTH_SHORT);
		}

		mLoadingDialog.dismiss();
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
		LogTest.LogMsg(TAG, "FeedbackActivity-onStart");
	}

	@Override
	protected void onResume() {
		super.onResume();
		LogTest.LogMsg(TAG, "FeedbackActivity-onResume");

	}

	@Override
	protected void onPause() {
		super.onPause();
		LogTest.LogMsg(TAG, "FeedbackActivity-onPause");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		LogTest.LogMsg(TAG, "FeedbackActivity-onDestroy");
	}
}
