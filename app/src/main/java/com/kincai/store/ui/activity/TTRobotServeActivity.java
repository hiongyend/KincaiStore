package com.kincai.store.ui.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kincai.store.R;
import com.kincai.store.bean.RobotChatMessageInfo;
import com.kincai.store.bean.UserInfo;
import com.kincai.store.bean.RobotChatMessageInfo.Type;
import com.kincai.store.bean.RobotChatResultInfo;
import com.kincai.store.common.LoadUserFace;
import com.kincai.store.thread.CachedThreadPoolUtils;
import com.kincai.store.thread.HttpGetThread;
import com.kincai.store.ui.activity.base.BaseSlideActivity;
import com.kincai.store.utils.AnimationUtil;
import com.kincai.store.utils.GsonTools;
import com.kincai.store.utils.LogTest;
import com.kincai.store.utils.NetWorkUtil;
import com.kincai.store.utils.Utils;
import com.kincai.store.view.adapter.ChatMessageAdapter;
import com.kincai.store.view.adapter.ChatMessageAdapter.IChatIvLinstener;
import com.kincai.store.view.custom.CircleImageView;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 *
 * @author kincai
 * 
 * @description tt机器人
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.ui.activity
 *
 * @time 2015-9-29 上午9:31:26
 *
 */
public class TTRobotServeActivity extends BaseSlideActivity implements IChatIvLinstener {
	private static final String TAG = "TTRobotServeActivity";
	private TextView mTitleTv;
	private ListView mRobotLv;
	private Button mSendBtn;
	private EditText mChatInputEt;
	private MyHandler mMyHandler;
	private List<RobotChatMessageInfo> mDatasList;
	private ChatMessageAdapter mMessageAdapter;
	private MyTextWatcher mMyTextWatcher;
	// Api 地址
	private static final String URL = "http://www.tuling123.com/openapi/api";
	// key
	private static final String API_KEY = "004a784d73a51047790ac793ec0f6199";
	private String userPath;


	@Override
	public int initContentView() {
		return R.layout.activity_robot_layout;
	}
	@Override
	public void initDatas() {
		init();
		initView();
		initData();
		setListener();
	}
	
	
	private void init() {
		mMyHandler = new MyHandler();
		mMyTextWatcher = new MyTextWatcher();
	}
	
	@Override
	public void initView() {
		super.initView();
		mTitleTv = (TextView) findViewById(R.id.title_tv);
		mRobotLv = (ListView) findViewById(R.id.robot_listview);
		mSendBtn = (Button) findViewById(R.id.robot_send_btn);
		mChatInputEt = (EditText) findViewById(R.id.robot_et);
		
		mTitleTv.setText(getString(R.string.tt_robot_str));
		mSendBtn.setEnabled(false);
		mSendBtn.setBackgroundResource(R.drawable.dialog_btn_left_bg_down);
	}
	
	@Override
	public void setListener() {
		super.setListener();
		mSendBtn.setOnClickListener(this);
		mChatInputEt.addTextChangedListener(mMyTextWatcher);
		mMessageAdapter.setChatToMsgBtmListener(this);
	}
	
	private void initData() {
		List<UserInfo> mUserInfos = mBaseUserDao.getUserInfoOne(mSp.getUsername());
		if(mUserInfos != null) {
			userPath = mUserInfos.get(0).getFace();
		}
		mDatasList = new ArrayList<RobotChatMessageInfo>();
		mDatasList.add(new RobotChatMessageInfo("欢迎回来", Type.INCOMING));
		
		setData(mDatasList);
	}
	
	private void setData(List<RobotChatMessageInfo> datas) {
		if(mMessageAdapter == null) {
			mMessageAdapter = new ChatMessageAdapter(this, datas);
			mRobotLv.setAdapter(mMessageAdapter);
		} else {
			mMessageAdapter.setChangeData(datas);
		}
	}
	
	@SuppressLint("HandlerLeak")
	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
			case 2114:
				String response = (String) msg.obj;
				if(response != null) {
//					Utils.showToast(TTRobotServeActivity.this, response, Toast.LENGTH_LONG);
					RobotChatResultInfo resultInfo = GsonTools.changeGsonToBean(response, RobotChatResultInfo.class);
					// 等待接收，子线程完成数据的返回
					RobotChatMessageInfo messageInfo = new RobotChatMessageInfo();
					messageInfo.setMsg(resultInfo.getText());
					messageInfo.setType(Type.INCOMING);
					mDatasList.add(messageInfo);
					mMessageAdapter.notifyDataSetChanged();
					mRobotLv.setSelection(mDatasList.size() - 1);
				}
				
				break;
			}
		}
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.titlebar_back_iv:
			finish();
			AnimationUtil.finishHaveFloatActivityAnimation(this);
			break;
		case R.id.robot_send_btn:
			String msg = mChatInputEt.getText().toString();
			if(TextUtils.isEmpty(msg)) {
				Utils.showToast(this, "不能发送空消息", Toast.LENGTH_LONG);
				return;
			}
			
			RobotChatMessageInfo toMessage = new RobotChatMessageInfo();
			toMessage.setMsg(msg);
			toMessage.setType(Type.OUTCOMING);
			mDatasList.add(toMessage);
			mMessageAdapter.notifyDataSetChanged();
			mRobotLv.setSelection(mDatasList.size() - 1);// 添加到listview的左后

			mChatInputEt.setText("");
			
			String url = null;
			try {
				url = URL + "?key=" + API_KEY + "&info="
						+ URLEncoder.encode(msg, "UTF-8");
				if(NetWorkUtil.isNetworkAvailable(this)) {
					CachedThreadPoolUtils.execute(new HttpGetThread(mMyHandler, url, "robot_chat"));
				}
				
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			
			break;

		}
	}
	
	class MyTextWatcher implements TextWatcher {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			
		}

		@Override
		public void afterTextChanged(Editable s) {
			String content = mChatInputEt.getText().toString();
			if(content.length() > 0) {
				mSendBtn.setEnabled(true);
				mSendBtn.setBackgroundResource(R.drawable.dialog_btn_right_bg);
			} else {
				mSendBtn.setEnabled(false);
				mSendBtn.setBackgroundResource(R.drawable.dialog_btn_left_bg_down);
			}
					
		}
		
	}
	
	@Override
	public void setChatToMsgBtm(CircleImageView iv) {
		if(iv != null) {
			LoadUserFace.userFace(this, mMyHandler, userPath, iv);
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
	protected void onRestart() {
		LogTest.LogMsg(TAG, "TTRobotServeActivity-onRestart");
		super.onRestart();
	}

	@Override
	protected void onStart() {
		LogTest.LogMsg(TAG, "TTRobotServeActivity-onStart");
		super.onStart();
	}

	@Override
	protected void onResume() {
		LogTest.LogMsg(TAG, "TTRobotServeActivity-onResume");
		super.onResume();
	}

	@Override
	protected void onPause() {
		LogTest.LogMsg(TAG, "TTRobotServeActivity-onPause");
		super.onPause();
	}

	@Override
	protected void onStop() {
		LogTest.LogMsg(TAG, "TTRobotServeActivity-onStop");
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		LogTest.LogMsg(TAG, "TTRobotServeActivity-onDestroy");
		super.onDestroy();
	}

}
