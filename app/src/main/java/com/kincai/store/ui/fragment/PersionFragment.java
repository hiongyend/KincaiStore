package com.kincai.store.ui.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kincai.store.Constants;
import com.kincai.store.R;
import com.kincai.store.bean.IsHaveOrderInfo;
import com.kincai.store.bean.IsHaveOrderInfo.DataEntity;
import com.kincai.store.bean.UserInfo;
import com.kincai.store.common.LoadUserFace;
import com.kincai.store.db.UserDao;
import com.kincai.store.thread.CachedThreadPoolUtils;
import com.kincai.store.thread.HttpGetImgThread;
import com.kincai.store.thread.HttpGetThread;
import com.kincai.store.thread.HttpPostThread;
import com.kincai.store.ui.activity.BalanceActivity;
import com.kincai.store.ui.activity.BrowseHistoryActivity;
import com.kincai.store.ui.activity.FeedbackActivity;
import com.kincai.store.ui.activity.LockComeInMoneyActivity;
import com.kincai.store.ui.activity.MainActivity;
import com.kincai.store.ui.activity.MyCollectActivity;
import com.kincai.store.ui.activity.MyOrdersActivity;
import com.kincai.store.ui.activity.SettingActivity;
import com.kincai.store.ui.activity.UserInfoActivity;
import com.kincai.store.ui.activity.UserLoginActivity;
import com.kincai.store.ui.fragment.base.BaseFragment;
import com.kincai.store.utils.AnimationUtil;
import com.kincai.store.utils.GsonTools;
import com.kincai.store.utils.JsonUtil;
import com.kincai.store.utils.LoadingDialog;
import com.kincai.store.utils.LogTest;
import com.kincai.store.utils.NetWorkUtil;
import com.kincai.store.utils.SPStorageUtil;
import com.kincai.store.utils.Utils;
import com.kincai.store.utils.bitmap_cache.BitmapLocaFileCacheUtil;
//import com.lidroid.xutils.HttpUtils;
//import com.lidroid.xutils.exception.HttpException;
//import com.lidroid.xutils.http.ResponseInfo;
//import com.lidroid.xutils.http.callback.RequestCallBack;
//import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.kincai.store.view.custom.CircleImageView;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 *
 * @author kincai
 * 
 * @description 用户
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.fragment
 *
 * @time 2015-6-29 下午7:45:56
 *
 */
@SuppressWarnings("unused")
public class PersionFragment extends BaseFragment implements OnClickListener {

	private static final String TAG = "PersionFragment";
	private ChangeInfoReceiver mInfoReceiver;
	/** 网络异常提示布局*/
	private RelativeLayout mNetWorkLayout;

	/** 网络异常提示文字内容*/
	private TextView mNetworkTipTv;
	/** 设置*/
	private ImageView mSettingIv;
	/** 我的头像*/
	private CircleImageView mMyLogoIv;
	/** 我的账户名*/
	private TextView mMyNickTv;
	/** 等级*/
	private TextView mMyLevelTv;

	private ImageView mLoginRegIv;

	/** 我的积分布局*/
	private LinearLayout mMyIntegralLayout;
	/** 我的积分*/
	private TextView mMyIntegralTv;
	/** 我的足迹布局*/
	private LinearLayout mMyHistoryLayout;
	/** 我的足迹*/
	private TextView mMyHistoryTv;

	/** 代支付*/
	private Button mMyNoPaymentBtn;
	/** 代发货*/
	private Button mMyNoShipmentsBtn;
	/** 代收货*/
	private Button mMyNoReceiveBtn;
	/** 代评价*/
	private Button mMyNoEvaluationBtn;

	/** 待支付提示*/
	private TextView mMyNoPaymentTipIv;
	/** 待发货提示*/
	private TextView mMyNoShipmentsTipIv;
	/** 待收货提示*/
	private TextView mMyNoReceiveTipIv;
	/** 待评价提示*/
	private TextView mMyNoEvaluationTipIv;

	/** 我的收藏*/
	private RelativeLayout mMyCollectRl;
	/** 全部订单*/
	private RelativeLayout mMyAllOrdersRl;
	/** 我的余额*/
	private RelativeLayout mMyMoneyRl;
	/** 我的账户*/
	private RelativeLayout mMyAccountRl;
	/** 账户安全*/
	private RelativeLayout mMyAccountSafetyRl;
	/** 安全等级*/
	private TextView mMyAccountSafetyGradeTv;
	/** 售后服务*/
	private RelativeLayout mMyAfterServiceRl;
	/** 意见反馈*/
	private RelativeLayout mMyFeedbackRl;

	private List<View> mViewList;
	private MyHandler mMyHandler;
	private Bitmap mMyFaceBitmap;

	String mUsernameStr;
	String mFacePath;

	private String mUserId;
//	private LoadingDialog mLoadingDialog;

	
	@Override
	public View getContentView() {
		return Utils.getView(mMainActivityContext, R.layout.fragment_persion_layout);
	}
	
	@Override
	public void initDatas() {
		init();
		initView();
		initUserData();
		setListener();
		registerUserInfoReceiver();
	}
	
	private void init() {

		mMyHandler = new MyHandler();
//		mLoadingDialog = new LoadingDialog(mMainActivityContext, false);
	}

	/**
	 * 初始化控件
	 */
	public void initView() {
		mNetWorkLayout = (RelativeLayout) mView
				.findViewById(R.id.network_abnormal_top_layout);
		mNetworkTipTv = (TextView) mView
				.findViewById(R.id.fragment_nerwork_state_tv);
		mSettingIv = (ImageView) mView.findViewById(R.id.main_menu_setting);
		mMyLogoIv = (CircleImageView) mView.findViewById(R.id.my_logo);

		mMyNickTv = (TextView) mView.findViewById(R.id.my_nick);
		;
		mMyLevelTv = (TextView) mView.findViewById(R.id.my_Level);
		mLoginRegIv = (ImageView) mView
				.findViewById(R.id.nologin);

		mMyIntegralTv = (TextView) mView.findViewById(R.id.my_integral);
		mMyIntegralLayout = (LinearLayout) mView
				.findViewById(R.id.my_integral_linearlayout);
		mMyHistoryTv = (TextView) mView.findViewById(R.id.my_browsing_history);
		mMyHistoryLayout = (LinearLayout) mView
				.findViewById(R.id.my_browsing_history_linearlayout);

		mMyNoPaymentBtn = (Button) mView.findViewById(R.id.my_goods_no_payment);
		mMyNoShipmentsBtn = (Button) mView
				.findViewById(R.id.my_goods_no_shipments);
		mMyNoReceiveBtn = (Button) mView.findViewById(R.id.my_goods_no_receive);
		mMyNoEvaluationBtn = (Button) mView
				.findViewById(R.id.my_goods_no_evaluation);

		mMyNoPaymentTipIv = (TextView) mView
				.findViewById(R.id.my_goods_no_payment_tip);
		mMyNoShipmentsTipIv = (TextView) mView
				.findViewById(R.id.my_goods_no_shipments_tip);
		mMyNoReceiveTipIv = (TextView) mView
				.findViewById(R.id.my_goods_no_receive_tip);
		mMyNoEvaluationTipIv = (TextView) mView
				.findViewById(R.id.my_goods_no_evaluation_tip);

		mMyCollectRl = (RelativeLayout) mView
				.findViewById(R.id.my_collect_relativeLayout);
		mMyAllOrdersRl = (RelativeLayout) mView
				.findViewById(R.id.my_allorders_relativeLayout);
		mMyMoneyRl = (RelativeLayout) mView
				.findViewById(R.id.my_money_relativeLayout);
		mMyAccountRl = (RelativeLayout) mView
				.findViewById(R.id.my_account_relativeLayout);
		mMyAccountSafetyRl = (RelativeLayout) mView
				.findViewById(R.id.my_account_safety_relativeLayout);
		mMyAfterServiceRl = (RelativeLayout) mView
				.findViewById(R.id.my_after_service_relativeLayout);
		mMyFeedbackRl = (RelativeLayout) mView
				.findViewById(R.id.my_feedback_relativeLayout);
		mMyAccountSafetyGradeTv = (TextView) mView
				.findViewById(R.id.my_account_safety_grade);
		
		

	};

	/**
	 * 初始化用户数据
	 */
	public void initUserData() {
		LogTest.LogMsg(TAG, "sdsd");
		
		if (mSp.getUserIsLogin()) {
			mMyLogoIv.setVisibility(View.VISIBLE);
			List<UserInfo> mUserInfos = mUserDao.getUserInfoOne(mSp.getUsername());

			LogTest.LogMsg(TAG, "用户查询是否不为空" + (mUserInfos != null));

			if (null != mUserInfos) {
				mUserId = String.valueOf(mUserInfos.get(0).getUserId());

				mLoginRegIv.setVisibility(View.GONE);
				mMyNickTv.setVisibility(View.VISIBLE);
				mMyLevelTv.setVisibility(View.VISIBLE);
				mFacePath = mUserInfos.get(0).getFace();
				mMyNickTv.setText(mUserInfos.get(0).getNickname());
				int level = mUserInfos.get(0).getGrade();
				if(level==0) {
					mMyLevelTv.setText(getResources().getString(R.string.copper_user));
				} else if (level==1) {
					mMyLevelTv.setText(getResources().getString(R.string.silver_user));
				} else if (level==2) {
					mMyLevelTv.setText(getResources().getString(R.string.gold_user));
				} 
				
				mMyIntegralTv.setText(String.valueOf(mUserInfos.get(0).getIntegral()));

				LoadUserFace.userFace(mMainActivityContext, mMyHandler, mFacePath, mMyLogoIv);
				
				
				
				initData();

			} else {
				mSp.saveUserIsLogin(false);
				mMyNickTv.setVisibility(View.GONE);
				mMyLevelTv.setVisibility(View.GONE);
				mLoginRegIv.setVisibility(View.VISIBLE);
				mMyIntegralTv.setText("");
			}

		} else {
			mMyLogoIv.setVisibility(View.GONE);
			mUserId = "";
//			initData();
			mMyHistoryTv.setText("");
//			mMyLogoIv.setImageResource(R.drawable.ic_launcher);
			mMyNickTv.setVisibility(View.GONE);
			mMyLevelTv.setVisibility(View.GONE);
			mLoginRegIv.setVisibility(View.VISIBLE);
			mMyIntegralTv.setText("");
			
			
			mMyNoPaymentTipIv.setVisibility(View.GONE);
			mMyNoShipmentsTipIv.setVisibility(View.GONE);
			mMyNoReceiveTipIv.setVisibility(View.GONE);
			mMyNoEvaluationTipIv.setVisibility(View.GONE);
			

		}
		

	}

	/**
	 * 初始化数据
	 */
	private void initData() { 
		if (NetWorkUtil.isNetworkAvailable(mMainActivityContext)) {
			loadBrowseNumData();
			
			loadIsHaveOrderData();
			
		}
		
		
		
	}
	
	private void loadIsHaveOrderData() {
		//订单提示
		CachedThreadPoolUtils.execute(new HttpGetThread(mMyHandler, 
				new StringBuffer().append(Constants.API_URL)
				.append("appIsHaveOrderApi.php?userId=")
				.append(mUserId).toString(), "isHaveOrder"));
	}
	
	/**
	 * 加载流量历史数量
	 */
	private void loadBrowseNumData() {
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("userId", mUserId));
		CachedThreadPoolUtils.execute(new HttpPostThread(mMyHandler,
				Constants.API_URL + "appGetBrowseNumApi.php",
				parameters, "getBrowseNum"));
	}

	@SuppressLint("HandlerLeak")
	class MyHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 202:
				
				LoadUserFace.getUserFaceResponse(mMainActivityContext, mMyLogoIv, mFacePath, msg);
				
//				getFace(msg);
				break;
			case 2206:
				getBrowseNumData(msg);
				
				break;
			case 2120:
				getIsHaveOrderData(msg);
				break;
			}
		}
	}
	
	private void getIsHaveOrderData(Message msg) {
		String response = (String) msg.obj;
		if(null != response && "200".equals(JsonUtil.msgJson("code", response))) {
			IsHaveOrderInfo isHaveOrderInfo = GsonTools.changeGsonToBean(response, IsHaveOrderInfo.class);
			DataEntity data = isHaveOrderInfo.getData();
			
			System.out.println(data.toString());
			if(!TextUtils.isEmpty(data.getDaifukuan())) {
				mMyNoPaymentTipIv.setVisibility(View.VISIBLE);
				mMyNoPaymentTipIv.setText(data.getDaifukuan());
			}
			if (!TextUtils.isEmpty(data.getDaifahuo())) {
				mMyNoShipmentsTipIv.setVisibility(View.VISIBLE);
				mMyNoShipmentsTipIv.setText(data.getDaifahuo());
			} 
			if (!TextUtils.isEmpty(data.getDaishouhuo())) {
				mMyNoReceiveTipIv.setVisibility(View.VISIBLE);
				mMyNoReceiveTipIv.setText(data.getDaishouhuo());
			} 
			if (!TextUtils.isEmpty(data.getDaipingjia())) {
				mMyNoEvaluationTipIv.setVisibility(View.VISIBLE);
				mMyNoEvaluationTipIv.setText(data.getDaipingjia());
			}
			
			
		}
	}
	
	/**
	 * 我的足迹个数
	 * @param msg
	 */
	private void getBrowseNumData(Message msg) {
		String response = (String) msg.obj;
		if(null != response) {
			String code = JsonUtil.msgJson("code", response);
			if("200".equals(code)) {
				String number = JsonUtil.msgJson("message", response);
				mMyHistoryTv.setText(number);
			} else {
				mMyHistoryTv.setText("0");
			}
		} else {
			mMyHistoryTv.setText("0");
		}
		
		if(!mSp.getUserIsLogin()) {
			mMyHistoryTv.setText("");
		}
	}
	
	

	/**
	 * 设置监听
	 */
	public void setListener() {
		// TODO Auto-generated method stub
		mNetWorkLayout.setOnClickListener(this);
		mSettingIv.setOnClickListener(this);
		mMyLogoIv.setOnClickListener(this);

		mMyIntegralLayout.setOnClickListener(this);
		mMyHistoryLayout.setOnClickListener(this);

		mMyNoPaymentBtn.setOnClickListener(this);
		mMyNoShipmentsBtn.setOnClickListener(this);
		mMyNoReceiveBtn.setOnClickListener(this);
		mMyNoEvaluationBtn.setOnClickListener(this);

		mMyCollectRl.setOnClickListener(this);
		mMyAllOrdersRl.setOnClickListener(this);
		mMyMoneyRl.setOnClickListener(this);
		mMyAccountRl.setOnClickListener(this);
		mMyAccountSafetyRl.setOnClickListener(this);
		mMyAfterServiceRl.setOnClickListener(this);
		mMyFeedbackRl.setOnClickListener(this);
		mLoginRegIv.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.network_abnormal_top_layout:

			startActivity(new Intent(Settings.ACTION_SETTINGS));
			AnimationUtil.startHaveSinkActivityAnimation((MainActivity)mMainActivityContext);
			break;
		case R.id.main_menu_setting:
			startActivity(new Intent(mMainActivityContext, SettingActivity.class));
			AnimationUtil.startHaveSinkActivityAnimation((MainActivity)mMainActivityContext);
			break;
		case R.id.my_logo:
			startActivity(new Intent(mMainActivityContext, UserInfoActivity.class));
			AnimationUtil.startHaveSinkActivityAnimation((MainActivity)mMainActivityContext);
			break;
		case R.id.nologin:
			startActivity(new Intent(mMainActivityContext, UserLoginActivity.class));
			AnimationUtil.startHaveSinkActivityAnimation((MainActivity)mMainActivityContext);

			break;
		case R.id.my_integral_linearlayout:

			break;
		case R.id.my_browsing_history_linearlayout:
			if(mSp.getUserIsLogin()) {
				startActivity(new Intent(mMainActivityContext, BrowseHistoryActivity.class));
				AnimationUtil.startHaveSinkActivityAnimation((MainActivity)mMainActivityContext);
			} else {
				isNoLogin();
			}
			
			break;
		case R.id.my_goods_no_payment:

			startActivitytoMyOrderActivity(MyOrdersActivity.NO_PAYMENT);	
			
			break;
		case R.id.my_goods_no_shipments:

			startActivitytoMyOrderActivity(MyOrdersActivity.NO_SHIPMENT);	
			
			break;
		case R.id.my_goods_no_receive:

			startActivitytoMyOrderActivity(MyOrdersActivity.NO_RECEIVE);	
			
			break;
		case R.id.my_goods_no_evaluation:

			startActivitytoMyOrderActivity(MyOrdersActivity.NO_EVALUATION);	
			
			break;
		case R.id.my_collect_relativeLayout:
			if(mSp.getUserIsLogin()) {
				startActivity(new Intent(mMainActivityContext, MyCollectActivity.class));
				AnimationUtil.startHaveSinkActivityAnimation((MainActivity)mMainActivityContext);
			} else {
				isNoLogin();
			}
			
			break;
		case R.id.my_allorders_relativeLayout:
			
			startActivitytoMyOrderActivity(MyOrdersActivity.ALL_ORDER);			
			break;
		case R.id.my_money_relativeLayout:
			if(mSp.getUserIsLogin()) {
				
				if(mSp.getLockPwdState()) {
					startActivity(new Intent(mMainActivityContext, LockComeInMoneyActivity.class).putExtra("settingType", 4));
				} else {
					startActivity(new Intent(mMainActivityContext, BalanceActivity.class));
					AnimationUtil.startHaveSinkActivityAnimation((MainActivity)mMainActivityContext);
				}
			} else {
				isNoLogin();
			}
			

			break;
		case R.id.my_account_relativeLayout:
			if(mSp.getUserIsLogin()) {
				startActivity(new Intent(mMainActivityContext, UserInfoActivity.class));
				AnimationUtil.startHaveSinkActivityAnimation((MainActivity)mMainActivityContext);
			} else {
				isNoLogin();
			}
			
			break;
		case R.id.my_account_safety_relativeLayout:

			break;
		case R.id.my_feedback_relativeLayout:
			startActivity(new Intent(mMainActivityContext, FeedbackActivity.class));
			AnimationUtil.startHaveSinkActivityAnimation((MainActivity)mMainActivityContext);
			break;

		}

	}
	
	/**
	 * 跳转到MyOrderActivity
	 * @param position
	 */
	private void startActivitytoMyOrderActivity(int position) {
		if(mSp.getUserIsLogin()) {
			startActivity(new Intent(mMainActivityContext, MyOrdersActivity.class)
				.putExtra(MyOrdersActivity.ORDER_INTENT_FLAG, position)
				.putExtra("userId", mUserId));
			AnimationUtil.startHaveSinkActivityAnimation((MainActivity)mMainActivityContext);
		} else {
			isNoLogin();
		}
		
	}
	
	private void isNoLogin() {
		startActivity(new Intent(mMainActivityContext, UserLoginActivity.class));
		AnimationUtil.startHaveSinkActivityAnimation((MainActivity)mMainActivityContext);
	}

	@Override
	public void netWork() {
		mNetWorkLayout.setVisibility(View.GONE);
	}
	
	@Override
	public void noNetWork() {
		mNetWorkLayout.setVisibility(View.VISIBLE);
	}

	/**
	 * 修改信息广播接收者
	 * 
	 * @author kincai
	 * 
	 */
	class ChangeInfoReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent != null) {
				if (Constants.USERINFO_CHANGE_ACTION.equals(intent.getAction())) {
					LogTest.LogMsg(TAG, "接收到广播");
					initUserData();
					
					//历史记录改变广播
				} else if(Constants.BROWSE_HISTORY_CHANGE_ACTION.equals(intent.getAction())) {
					loadBrowseNumData();
	//				initData();
				} else if (Constants.PERSIONFRAGMENT_ORDER_DATA_UPDATE_ACTION.equals(intent.getAction())) {
					loadIsHaveOrderData();
					
				} else {
					LogTest.LogMsg(TAG, "没有收到广播");
				}
			}
		}

	}

	/**
	 * 广播注册
	 */
	private void registerUserInfoReceiver() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.USERINFO_CHANGE_ACTION);
		filter.addAction(Constants.BROWSE_HISTORY_CHANGE_ACTION);
		filter.addAction(Constants.PERSIONFRAGMENT_ORDER_DATA_UPDATE_ACTION);
		mInfoReceiver = new ChangeInfoReceiver();
		mMainActivityContext.registerReceiver(mInfoReceiver, filter);
	}

	/**
	 * 用户信息广播注销
	 */
	private void unRegisterUserInfoReceiver() {
		mMainActivityContext.unregisterReceiver(mInfoReceiver);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		// initUserData();
		LogTest.LogMsg(TAG, "PersionFragment-onStart");

	}

	@Override
	public void onResume() {
		super.onResume();

		LogTest.LogMsg(TAG, "PersionFragment-onResume");

		// initUserData();
		if (!NetWorkUtil.isNetworkAvailable(mMainActivityContext)) {
			// Utils.showToast(mMainActivityContext, "网络连接失败", Toast.LENGTH_SHORT);
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		LogTest.LogMsg(TAG, "PersionFragment-onPause");
	}

	@Override
	public void onDestroy() {
		unRegisterUserInfoReceiver();
		super.onDestroy();
		LogTest.LogMsg(TAG, "PersionFragment-onDestroy");
		
	}

	public void isNerwoking() {
		// TODO Auto-generated method stub
		if (!NetWorkUtil.isNetworkAvailable(mMainActivityContext)) {
			Utils.showToast(mMainActivityContext, "网络连接失败", Toast.LENGTH_SHORT);
			// Utils.viewFocus(mViewList, false);
		}
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);

		if (!hidden) {
			LogTest.LogMsg(TAG, "PersionFragment-onHiddenChanged");
			// onResume();
//			initUserData();
			isNerwoking();
		}

	}

}
