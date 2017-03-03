package com.kincai.store.ui.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import c.b.BP;

import cn.jpush.android.data.p;
import cn.jpush.android.data.s;

import com.kincai.store.Constants;
import com.kincai.store.R;
import com.kincai.store.bean.AddressInfo;
import com.kincai.store.bean.CartInfo;
import com.kincai.store.bean.QueryOrderInfo;
import com.kincai.store.bean.CartInfo.CartData;
import com.kincai.store.bean.CartInfo.CartData.GoodsData;
import com.kincai.store.common.OrderPay;
import com.kincai.store.thread.CachedThreadPoolUtils;
import com.kincai.store.thread.HttpGetThread;
import com.kincai.store.thread.HttpPostThread;
import com.kincai.store.ui.activity.base.BaseSlideActivity;
import com.kincai.store.utils.AnimationUtil;
import com.kincai.store.utils.GsonTools;
import com.kincai.store.utils.JsonUtil;
import com.kincai.store.utils.LoadingDialog;
import com.kincai.store.utils.LogTest;
import com.kincai.store.utils.NetWorkUtil;
import com.kincai.store.utils.TimeUtil;
import com.kincai.store.utils.Utils;
import com.kincai.store.utils.bitmap_cache.BitmapLocaFileCacheUtil;
import com.kincai.store.view.adapter.ConfirmOrderAdapter;

public class ConfirmOrderActivity extends BaseSlideActivity {
	
	public static final String TAG = "ConfirmOrderActivity";
	private TextView mTitleBarTv;
	private TextView mConfirmOrderConsigneeName;
	private TextView mConfirmOrderConsigneePhone;
	private TextView mConfirmOrderDetailedAddress;
	private TextView mConfirmOrderShifukuan;
	private Button mConfirmOrderBtn;
	private TextView mConfirmOrderAddressBottom;
	
	private MyHandler mMyHandler;
	
	private int mAnonymity = 1;//是否匿名 0实名 1匿名
	private String mUserId;
	private ConfirmOrderInfoReceiver mInfoReceiver;
	private LoadingDialog mLoadingDialog;
	private AddressInfo mAddressInfo;
	
	private ListView mListView;
	private View headerView, footView;
	
	private LinearLayout mAliPayLl, mWxPayLl;
	private ImageView mAliCb, mWxCb;
	private String mOrderNum;
	private int isWxOrAliCheck;
	private CartInfo cartInfo;
	private ConfirmOrderAdapter mConfirmOrderAdapter;
	private OrderPay orderPay;
	private List<CartData> cartDatas;
	private List<String> payOrderNumList;
	private boolean isPay;
	private String orderId;
	@Override
	public int initContentView() {
		return R.layout.activity_confirm_order_layout;
	}
	@Override
	public void initDatas() {
		init();
		initView();
		initData();
		setListener();
		
		registerOrderInfoReceiver();
	}
	
	private void init() {
		mMyHandler = new MyHandler();
		mUserId = String.valueOf(mBaseUserDao.getUserInfoOne(mSp.getUsername()).get(0).getUserId());
		BP.init(this,Constants.BMOB_PAY_ID);
		
		
	}
	
	@Override
	public void initView() {
		super.initView();
		headerView = View.inflate(this, R.layout.include_address_header_layout, null);
		footView = View.inflate(this, R.layout.item_confirm_order_foot_layout, null);
		mTitleBarTv = (TextView) findViewById(R.id.title_tv);
		mConfirmOrderConsigneeName = (TextView) headerView.findViewById(R.id.address_name);
		mConfirmOrderConsigneePhone = (TextView) headerView.findViewById(R.id.address_phone);
		mConfirmOrderDetailedAddress = (TextView) headerView.findViewById(R.id.address_detailedAddress);
		
		mAliPayLl = (LinearLayout) footView.findViewById(R.id.select_ali_ll);
		mWxPayLl = (LinearLayout) footView.findViewById(R.id.select_wx_ll);
		
		mAliCb = (ImageView) footView.findViewById(R.id.select_al_cb);
		mWxCb = (ImageView) footView.findViewById(R.id.select_wx_cb);
		
		mConfirmOrderShifukuan = (TextView) findViewById(R.id.confirm_order_shifukuang_price);
		mConfirmOrderBtn = (Button) findViewById(R.id.confirm_order_btn);
		mConfirmOrderAddressBottom = (TextView) findViewById(R.id.confirm_order_address_bottom);
		mListView = (ListView) findViewById(R.id.comfig_order_lv);
		mTitleBarTv.setText(getString(R.string.confirm_order_strss));
		 
		
		mListView.addHeaderView(headerView);
		mListView.addFooterView(footView);
		
	}
	
	private void initData() {
		isWxOrAliCheck = 1;//阿里选中
		changeSelectState();
		Intent intent = getIntent();
		if(intent != null) {
			
			cartInfo = (CartInfo) intent.getSerializableExtra("cartinfo");
			if(NetWorkUtil.isNetworkAvailable(this)) {
				CachedThreadPoolUtils.execute(new HttpGetThread(mMyHandler, Constants.API_URL+"appGetDefaultAddressApi.php?userId="+mUserId, "default_address"));
			}
			
			setData(cartInfo.getCartData());

		}
		
		
	}
	
	private void setData(List<CartData> datas) {

		if(mConfirmOrderAdapter == null) {
			mConfirmOrderAdapter = new ConfirmOrderAdapter(this, datas);
			mListView.setAdapter(mConfirmOrderAdapter);
		} else {
			mConfirmOrderAdapter.onDateChange(datas);
		}
		
		double shifukuan= 0;
		if(datas != null) {
			int size = datas.size();
			for (int i = 0; i < size; i++) {
				List<GoodsData> goods_data = datas.get(i).getGoods_data();
				int size2 = goods_data.size();
				for (int j = 0; j < size2; j++) {
					shifukuan += (Double.parseDouble(goods_data.get(j).getIPrice())* Double.parseDouble(goods_data.get(j).getGoods_num()));
				}
			}
		}
		
		mConfirmOrderShifukuan.setText(new StringBuilder().append("¥").append(shifukuan));
		
	}
	
	@Override
	public void setListener() {
		super.setListener();
		mConfirmOrderBtn.setOnClickListener(this);
		mAliPayLl.setOnClickListener(this);
		mWxPayLl.setOnClickListener(this);
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
		case R.id.confirm_order_btn:
			
			confirmBtn();
			break;
		case R.id.select_ali_ll:
			isWxOrAliCheck = 1;
			changeSelectState();
			break;
		case R.id.select_wx_ll:
			isWxOrAliCheck = 0;
			changeSelectState();
			break;
		}
	}
	
	
	private void changeSelectState() {
		if(isWxOrAliCheck == 1) {
			mAliCb.setImageResource(R.drawable.ic_selltip_checkbox_check);
			mWxCb.setImageResource(R.drawable.ic_selltip_checkbox_normal);
		} else if (isWxOrAliCheck == 0) {
			mAliCb.setImageResource(R.drawable.ic_selltip_checkbox_normal);
			mWxCb.setImageResource(R.drawable.ic_selltip_checkbox_check);
		}
	}
	
	
	/**
	 * 确认订单按钮 先支付 成功后查询 订单 在判断是否支付 还是没支付 需要注意 支付时间与支付状态字段
	 */
	private void confirmBtn() {
		if(mLoadingDialog == null) {
			mLoadingDialog = new LoadingDialog(ConfirmOrderActivity.this, false);
		}
		mLoadingDialog.dialogShow();
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				
				mLoadingDialog.dismiss();
				mLoadingDialog = null;
				
				new Handler().postDelayed(new Runnable() {
					

					@Override
					public void run() {
						if(mAddressInfo == null) {
							Utils.showToast(ConfirmOrderActivity.this, "地址不能为空，请手动选择收货地址！", 1);
							return;
						}
						orderPay = OrderPay.getInstance(ConfirmOrderActivity.this);
						
						cartDatas = mConfirmOrderAdapter.getCartDatas();
						String price = mConfirmOrderShifukuan.getText().toString();
						if(cartDatas != null && price != null && !"".equals(price) && !"0.0".equals(price) && NetWorkUtil.isNetworkAvailable(ConfirmOrderActivity.this)) {
							//订单号
							mOrderNum = createOrderNum();
							if(mOrderNum == null) return;
							
							//支付订单号
							payOrderNumList = new ArrayList<>();
							int size = cartDatas.size();
							for(int i = 0; i < size; i++ ) {
								payOrderNumList.add(createDifferentStoreOrderNum(i));
							}
							
							//进行支付 测试价格 0.02
							if(isWxOrAliCheck == 0) {
								/*orderPay.wxPay(Double.parseDouble(price), new StringBuffer().append("order_name")
										.append(mOrderNum).toString(), new StringBuffer().append("order_desc")
										.append(mOrderNum).toString());*/
								orderPay.wxPay(0.02, new StringBuffer().append("order_name")
										.append(mOrderNum).toString(), new StringBuffer().append("order_desc")
										.append(mOrderNum).toString());
								
							} else if (isWxOrAliCheck == 1) {
								/*orderPay.aliPay(Double.parseDouble(price), new StringBuffer().append("order_name")
										.append(mOrderNum).toString(), new StringBuffer().append("order_desc")
										.append(mOrderNum).toString());*/
								orderPay.aliPay(0.02, new StringBuffer().append("order_name")
										.append(mOrderNum).toString(), new StringBuffer().append("order_desc")
										.append(mOrderNum).toString());
							}
//							
						}
						
					}
				}, 300);
			}
		}, 300);
		
		
	}
	
	/**
	 * 获得一次订单 的订单号 年份后2位+时间戳13位 共15位
	 * 
	 * @return 返回15位订单号
	 */
	private String createOrderNum() {
		String year = TimeUtil.getYearStr2();
		return new StringBuilder().append(year).append(TimeUtil.getCurrentTimeMillis()).toString();
	}
	
	/**
	 * 获取一次订单 不同商家的支付订单号
	 * 
	 * @return 返回32位支付订单号
	 */
	private String createDifferentStoreOrderNum(int i) {
		String year = TimeUtil.getYearStr();
		if(mOrderNum == null || "".equals(mOrderNum)) return null;
		return new StringBuilder().append(year).append(mOrderNum).append(TimeUtil.getCurrentTimeMillis().substring(0, 12)).append(i).toString();
	}
	
	@SuppressLint("HandlerLeak")
	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 2115:
				parseQueryOrderData(msg);
				break;
			case 2116:
				parseAddressData(msg);
				break;
			case 2218:
				parseUpLoadOrderData(msg);
				break;

			}
		}

	}
	private void parseUpLoadOrderData(Message msg) {
		String response = (String)msg.obj;
		if(response != null) {
			
			sendBroadcast(new Intent().setAction(Constants.CART_CHANGE_ACTION));
			
			LogTest.LogMsg(TAG, response);
			startActivity(new Intent(ConfirmOrderActivity.this, MyOrdersActivity.class)
			.putExtra(MyOrdersActivity.ORDER_INTENT_FLAG, isPay ? 2 : 1)
			.putExtra("userId", mUserId));
			finish();
//			AnimationUtil.finishHaveFloatActivityAnimation(ConfirmOrderActivity.this);
		}
	}
	
	
	/**
	 * 解析地地址信息
	 * @param msg
	 */
	private void parseAddressData(Message msg) {
		String response = (String)msg.obj;
		if(response != null && "200".equals(JsonUtil.msgJson("code", response))) {
			List<AddressInfo> mAddressInfoLists = JsonUtil.getAddress("data", response);
			if(mAddressInfoLists != null) {
				mAddressInfo = mAddressInfoLists.get(0);
				mConfirmOrderConsigneeName.setText(mAddressInfo.getConsignee());
				mConfirmOrderConsigneePhone.setText(mAddressInfo.getPhoneNum());
				StringBuilder stringBuilder = new StringBuilder();
				
				stringBuilder.append(mAddressInfo.getArea());
				stringBuilder.append(" ");
				stringBuilder.append(mAddressInfo.getDetailedAddress());
				
				mConfirmOrderDetailedAddress.setText(stringBuilder.toString());
				
				mConfirmOrderAddressBottom.setText("送至："+stringBuilder.toString());
			}
			
		} else {
			mAddressInfo = null;
		}
	}
	
	/**
	 * 注册用户信息广播
	 */
	private void registerOrderInfoReceiver() {
		IntentFilter filter = new IntentFilter(Constants.ORDER_COMPLETE_PAY);
		mInfoReceiver = new ConfirmOrderInfoReceiver();
		registerReceiver(mInfoReceiver, filter);
	}


	private void unRegisterOrderInfoReceiver() {
		unregisterReceiver(mInfoReceiver);
	}
	
	/**
	 * 成功或失败都会 接收到广播  成功支付
	 * @author kincai
	 *
	 */
	class ConfirmOrderInfoReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent != null && Constants.ORDER_COMPLETE_PAY.equals(intent.getAction())) {
				LogTest.LogMsg(TAG, "接收到广播"+intent.getStringExtra("orderId"));
				orderId = intent.getStringExtra("orderId");
				String state_type = intent.getStringExtra("state_type");
//				Intent intent2 = new Intent(ConfirmOrderActivity.this, OrderDetailsActivity.class);
				if(Constants.BMOB_PAY_STATE_TYPE_SUCCESS.equals(state_type)) {//成功
					isPay = true;
					orderPay.query(mMyHandler, orderId);
					
				} else if(Constants.BMOB_PAY_STATE_TYPE_UNKNOW.equals(state_type)) {
					
				} else if(Constants.BMOB_PAY_STATE_TYPE_FAIL_6001.equals(state_type)) {//支付宝手动中断支付
					orderPay.query(mMyHandler, orderId);
				} else if(Constants.BMOB_PAY_STATE_TYPE_FAIL_7777.equals(state_type)) {
					
				} else if(Constants.BMOB_PAY_STATE_TYPE_FAIL_8888.equals(state_type)) {
					
				} else if(Constants.BMOB_PAY_STATE_TYPE_FAIL__2.equals(state_type)) {//微信手动中断支付
					orderPay.query(mMyHandler, orderId);
					
				} else if(Constants.BMOB_PAY_STATE_TYPE_FAIL__3.equals(state_type)) {
					
				} else if(Constants.BMOB_QUERY_STATE_TYPE_FAIL_QUERY.equals(state_type)) {//已提交支付 但查询订单的时候 失败
					String pay_state = "";
					String pay_time = "";
					if(isPay) {
						pay_state = Constants.ORDER_SUCCESS;
						pay_time = mOrderNum.substring(2, mOrderNum.length());
					} else {
						pay_state = Constants.ORDER_NOTPAY;
						pay_time = "";
					}
					
					upLoadOrder(pay_state, String.valueOf(isWxOrAliCheck), orderId, pay_time, "1");
				}
				
//				if(mAddressInfo != null) {
//					
//					finish();
//				} else {
//					Utils.showToast(ConfirmOrderActivity.this, "收货地址信息加载失败！请手动选择收货地址", Toast.LENGTH_SHORT);
//				}
				
			} else {
				LogTest.LogMsg(TAG, "no");
			}
		}

	}
	
	
	/**
	 * 解析查询订单数据
	 * @param msg
	 */
	private void parseQueryOrderData(Message msg) {
		String response = (String) msg.obj;
		if(response != null) {
			QueryOrderInfo queryOrderInfo = GsonTools.changeGsonToBean(response, QueryOrderInfo.class);
			String trade_state = queryOrderInfo.getTrade_state();
			String threeOrderNums = queryOrderInfo.getOut_trade_no();
			String pay_state = "";//还没支付
			String pay_time = "";
			if(Constants.ORDER_NOTPAY.equals(trade_state)) {
				pay_state = Constants.ORDER_NOTPAY;
				pay_time = "";
				//已支付成功
			} else if (Constants.ORDER_SUCCESS.equals(trade_state)) {
				Random random = new Random();
				int s = random.nextInt(3000)%(3000-1000+1) + 1000;
				
				pay_state = Constants.ORDER_SUCCESS;
				pay_time =  String.valueOf(Long.parseLong(mOrderNum.substring(2, mOrderNum.length()))+s);
//				pay_time =TimeUtil.stringToString(queryOrderInfo.getCreate_time(), "yyyy-MM-dd HH:mm:ss");
			}
			upLoadOrder(pay_state, String.valueOf(isWxOrAliCheck), threeOrderNums, pay_time, "1");
			
		} else {
			String pay_state = "";
			String pay_time = "";
			if(isPay) {
				Random random = new Random();
				int s = random.nextInt(1000)%(1000-500+1) + 500;
				pay_time =  String.valueOf(Long.parseLong(mOrderNum.substring(2, mOrderNum.length()))+s);
				pay_state = Constants.ORDER_SUCCESS;
//				pay_time = mOrderNum.substring(2, mOrderNum.length());
			} else {
				pay_state = Constants.ORDER_NOTPAY;
				pay_time = "";
			}
			
			upLoadOrder(pay_state, String.valueOf(isWxOrAliCheck), orderId, pay_time, "1");
		}
	}
	
	/**
	 * 上传订单信息 json字符串
	 * @param payState
	 * @param payType
	 * @param threeOrderNums
	 * @param payTime
	 * @param isanonymity
	 */
	private void upLoadOrder(String payState, String payType,String threeOrderNums,String payTime, String isanonymity) {
		
		if(orderPay == null) {
			orderPay = OrderPay.getInstance(ConfirmOrderActivity.this);
		}
		
		String orders = orderPay.createOrders(cartDatas, mUserId, mAddressInfo, mOrderNum, mOrderNum.substring(2, mOrderNum.length()), payOrderNumList, threeOrderNums, payType, payState, payTime, isanonymity);
		if(NetWorkUtil.isNetworkAvailable(ConfirmOrderActivity.this) && orders != null) {
			try {
				new BitmapLocaFileCacheUtil(ConfirmOrderActivity.this).savaFile("rrrrtrtrtrtrtrt.txt", orders);
			} catch (IOException e) {
				e.printStackTrace();
			}
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			parameters.add(new BasicNameValuePair("order", orders));
			
			CachedThreadPoolUtils.execute(new HttpPostThread(mMyHandler, 
					new StringBuffer().append(Constants.API_URL)
					.append("appUpLoadOrderDataApi.php").toString(), parameters, "uploadOrderdata"));
		}
		
	}
	
	@Override
	protected void onDestroy() {
		unRegisterOrderInfoReceiver();
		super.onDestroy();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if(mLoadingDialog != null && mLoadingDialog.isShow()) {
			mLoadingDialog.dismiss();
			mLoadingDialog = null;
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

}
