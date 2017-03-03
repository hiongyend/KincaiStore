package com.kincai.store.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kincai.store.Constants;
import com.kincai.store.R;
import com.kincai.store.bean.OrderInfo;
import com.kincai.store.bean.OrderInfo.OrderData;
import com.kincai.store.common.OrderUpdateRequest;
import com.kincai.store.ui.activity.base.BaseSlideActivity;
import com.kincai.store.utils.AnimationUtil;
import com.kincai.store.utils.LogTest;
import com.kincai.store.utils.TimeUtil;
import com.kincai.store.utils.Utils;
import com.kincai.store.view.adapter.OrderDetailAdapter;

public class OrderDetailsActivity extends BaseSlideActivity {
	
	private TextView mTitleBarTv;
	
	//头部状态
	private TextView mOrderDetailTradeStateTv;
	private TextView mOrderDetailTradeMsgTv;
	private ImageView mOrderDetailTradeStateIv;
	
	//头部地址
	private TextView mOrderDetailConsigneeName;
	private TextView mOrderDetailConsigneePhone;
	private TextView mOrderDetailDetailedAddress;
	
	//底部价格运费
	private TextView mOrderDetailPrice;
	private TextView mOrderDetailFreight;
	private TextView mOrderDetailShifukuan;
	
	//底部订单信息
	private TextView mOrderDetailOrderNumTv;
	private TextView mOrderDetailTradeNumTv;
	private TextView mOrderDetailCreateTimeTv;
	private TextView mOrderDetailPayTimeTv;
	private TextView mOrderDetailShipmentsTimeTv;
	private TextView mOrderDetailSuccessfullTv;
	private TextView mOrderDetailPywayTv;
	
	
	private Button mOrderDetailBottomDzfPayBtn;
	private Button mOrderDetailBottomDzfCancelOrderBtn;
	private Button mOrderDetailBottomDfhTuiKuanBtn;
	private Button mOrderDetailBottomDfhRemindBtn;
	private Button mOrderDetailBottomDshConfirmReceiveBtn;
	private Button mOrderDetailBottomDshWuliuBtn;
	private Button mOrderDetailBottomDpjPingjiaBtn;
	private Button mOrderDetailBottomDpjWuliuBtn;
	private Button mOrderDetailBottomDpjDeleteBtn;
	private Button mOrderDetailBottomGbDeleteBtn;
	
	private RelativeLayout mOrderDetailBottomDzf;
	private RelativeLayout mOrderDetailBottomDfh;
	private RelativeLayout mOrderDetailBottomDsh;
	private RelativeLayout mOrderDetailBottomDpj;
	private RelativeLayout mOrderDetailBottomGb;
	
	private ExpandableListView mOrderDetailLv;
	private OrderDetailAdapter mOrderDetailAdapter;
	private View mHeaderView;
	private View mFooterView;
	
	private OrderData mOrderData;
	private MyHandler mMyHandler;

	private String mTradeState;

	private String create_time;

	private String pay_time;

	private String shipments_time;

	private String successful_trade_time;

	@Override
	public int initContentView() {
		return R.layout.activity_order_details_layout;
	}
	@Override
	public void initDatas() {
		init();
		initView();
		getIntentData();
		initData();
		setListener();
	}
	
	private void init() {
		mMyHandler = new MyHandler();
	}
	
	@Override
	public void initView() {
		super.initView();
		
		mTitleBarTv = (TextView) findViewById(R.id.title_tv);
		mOrderDetailLv = (ExpandableListView) findViewById(R.id.order_detail_lv);
		
		mHeaderView = View.inflate(this, R.layout.item_order_detail_list_header_layout, null);
		mFooterView = View.inflate(this, R.layout.item_order_detail_list_footer_layout, null);
		
		
		//头部
		mOrderDetailTradeStateTv = (TextView) mHeaderView.findViewById(R.id.order_detail_top_nei_trade_state_tv);
		mOrderDetailTradeMsgTv = (TextView) mHeaderView.findViewById(R.id.order_detail_top_nei_trade_msg_tv);
		mOrderDetailTradeStateIv = (ImageView) mHeaderView.findViewById(R.id.order_detail_top_state_iv);
		
		mOrderDetailConsigneeName = (TextView)mHeaderView.findViewById(R.id.address_name);
		mOrderDetailConsigneePhone = (TextView) mHeaderView.findViewById(R.id.address_phone);
		mOrderDetailDetailedAddress = (TextView) mHeaderView.findViewById(R.id.address_detailedAddress);
		
		
		//底部
		mOrderDetailPrice = (TextView) mFooterView.findViewById(R.id.order_detail_total_money_tv);
		mOrderDetailFreight = (TextView) mFooterView.findViewById(R.id.order_detail_yunfei_tv);
		mOrderDetailShifukuan = (TextView) mFooterView.findViewById(R.id.order_detail_shifukuan_tv);
		
		mOrderDetailOrderNumTv = (TextView) mFooterView.findViewById(R.id.order_detail_order_info_order_num);
		mOrderDetailTradeNumTv = (TextView) mFooterView.findViewById(R.id.order_detail_order_info_transaction_num);
		mOrderDetailCreateTimeTv = (TextView) mFooterView.findViewById(R.id.order_detail_order_info_create_time);
		mOrderDetailPayTimeTv = (TextView) mFooterView.findViewById(R.id.order_detail_order_info_pay_time);
		mOrderDetailShipmentsTimeTv = (TextView) mFooterView.findViewById(R.id.order_detail_order_info_shipments_time);
		mOrderDetailSuccessfullTv = (TextView) mFooterView.findViewById(R.id.order_detail_order_info_successful_trade);
		mOrderDetailPywayTv = (TextView) mFooterView.findViewById(R.id.order_detail_payway_tv);
		
		
		
		mOrderDetailBottomDzfPayBtn = (Button) findViewById(R.id.daizhifu_pay_btn);
		mOrderDetailBottomDzfCancelOrderBtn = (Button) findViewById(R.id.daizhifu_cencel_order_btn);
		mOrderDetailBottomDfhTuiKuanBtn = (Button) findViewById(R.id.daifahuo_tuikuan_btn);
		mOrderDetailBottomDfhRemindBtn = (Button) findViewById(R.id.daifahuo_remind_fahuo_btn);
		mOrderDetailBottomDshConfirmReceiveBtn = (Button) findViewById(R.id.daishouhuo_confirm_receive_btn);
		mOrderDetailBottomDshWuliuBtn = (Button) findViewById(R.id.daishouhuo_wuliu_btn);
		mOrderDetailBottomDpjPingjiaBtn = (Button) findViewById(R.id.daipingjia_pingjia_btn);
		mOrderDetailBottomDpjWuliuBtn = (Button) findViewById(R.id.daipingjia_wuliu_btn);
		mOrderDetailBottomDpjDeleteBtn = (Button) findViewById(R.id.daipingjia_delete_order_btn);
		mOrderDetailBottomGbDeleteBtn = (Button) findViewById(R.id.guanbi_delete_order_btn);
		
		mOrderDetailBottomDzf = (RelativeLayout) findViewById(R.id.order_detail_bottom_daizhifu);
		mOrderDetailBottomDfh = (RelativeLayout) findViewById(R.id.order_detail_bottom_daifahuo);
		mOrderDetailBottomDsh = (RelativeLayout) findViewById(R.id.order_detail_bottom_daishouhuo);
		mOrderDetailBottomDpj = (RelativeLayout) findViewById(R.id.order_detail_bottom_daipingjia);
		mOrderDetailBottomGb = (RelativeLayout) findViewById(R.id.order_detail_bottom_guanbi);
		
		mTitleBarTv.setText(getString(R.string.order_detail_str));
		
		mOrderDetailLv.addHeaderView(mHeaderView);
		mOrderDetailLv.addFooterView(mFooterView);
		//屏蔽ExpandableListView的父item点击	事件
		mHeaderView.setOnClickListener(this);
		mFooterView.setOnClickListener(this);
		
		
		
		
	}
	
	private void getIntentData() {
		Intent intent = getIntent();
		if(intent != null) {
			mOrderData = (OrderData) intent.getSerializableExtra("orderData");
		}
	}

	@Override
	public void setListener() {
		super.setListener();
		
		mOrderDetailBottomDzfPayBtn.setOnClickListener(this);
		mOrderDetailBottomDzfCancelOrderBtn.setOnClickListener(this);
		mOrderDetailBottomDfhTuiKuanBtn.setOnClickListener(this); 
		mOrderDetailBottomDfhRemindBtn.setOnClickListener(this);
		mOrderDetailBottomDshConfirmReceiveBtn.setOnClickListener(this);
		mOrderDetailBottomDshWuliuBtn.setOnClickListener(this);
		mOrderDetailBottomDpjPingjiaBtn.setOnClickListener(this);
		mOrderDetailBottomDpjWuliuBtn.setOnClickListener(this);
		mOrderDetailBottomDpjDeleteBtn.setOnClickListener(this);
		mOrderDetailBottomGbDeleteBtn.setOnClickListener(this);
	}
	
	private void initData() {
		if(mOrderData != null) {
			List<OrderData> orderDatas = new ArrayList<OrderData>();
			orderDatas.add(mOrderData);
			setData(orderDatas);
			
			String yufei = mOrderData.getYunfei();
			String total_fee = mOrderData.getTotal_fee();
			String mTotalPrice = new StringBuilder().append((Double.parseDouble(total_fee)-Double.parseDouble(yufei))).toString();
			String mShifukuan = new StringBuilder().append("¥").append(total_fee).toString();
			
			
			mOrderDetailConsigneeName.setText(mOrderData.getConsignee_name());
			mOrderDetailConsigneePhone.setText(mOrderData.getConsignee_phone());
			mOrderDetailDetailedAddress.setText(mOrderData.getConsignee_address());
			
			
			mOrderDetailPywayTv.setText(("0".equals(mOrderData.getPay_type())) ? "微信支付" : "支付宝支付");
			mOrderDetailPrice.setText(new StringBuilder("¥").append(mTotalPrice));
			mOrderDetailFreight.setText(new StringBuilder().append("+ ¥").append(yufei).toString());
			mOrderDetailShifukuan.setText(mShifukuan);
			
			mTradeState = mOrderData.getTrade_state();
			create_time = mOrderData.getCreate_time();
			pay_time = mOrderData.getPay_time();
			shipments_time = mOrderData.getShipments_time();
			successful_trade_time = mOrderData.getSuccessful_trade_time();
			
			setBootomOrderInfoData();
		}
		

	}
	
	private void setData(List<OrderData> datas) {
		if(mOrderDetailAdapter == null) {
			mOrderDetailAdapter = new OrderDetailAdapter(this, datas);
		}
		
		mOrderDetailLv.setAdapter(mOrderDetailAdapter);
		
		expandGroup();
	}
	
	private void expandGroup() {

		int size = mOrderDetailAdapter.getGroupCount();
		for (int i = 0; i < size; i++) {
			mOrderDetailLv.expandGroup(i);
		}
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
		case R.id.daizhifu_pay_btn://待支付 去支付
			LogTest.LogMsg("待支付 去支付");
			orderClickHandler("daizhifu_pay_btn");
			break;
		case R.id.daizhifu_cencel_order_btn://待支付 取消订单
			LogTest.LogMsg("待支付 取消订单");
			orderClickHandler("daizhifu_cencel_order_btn");
			break;
		case R.id.daifahuo_tuikuan_btn://待发货 申请退款
			LogTest.LogMsg("待发货 申请退款");
			orderClickHandler("daifahuo_tuikuan_btn");
			break;
		case R.id.daifahuo_remind_fahuo_btn://待发货 提醒发货
			LogTest.LogMsg("待发货 提醒发货");
			orderClickHandler("daifahuo_remind_fahuo_btn");
			break;
		case R.id.daishouhuo_confirm_receive_btn://待收货 确认收货
			LogTest.LogMsg("待收货 确认收货");
			orderClickHandler("daishouhuo_confirm_receive_btn");
			break;
		case R.id.daishouhuo_wuliu_btn://待收货 物流
			LogTest.LogMsg("待收货 物流");
			orderClickHandler("wuliu_btn");
			break;
		case R.id.daipingjia_pingjia_btn://待评价  评价
			LogTest.LogMsg("待评价  评价");
			orderClickHandler("daipingjia_pingjia_btn");
			break;
		case R.id.daipingjia_wuliu_btn://待评价 物流
			LogTest.LogMsg("待评价 物流");
			orderClickHandler("wuliu_btn");
			break;
		case R.id.daipingjia_delete_order_btn://待评价 删除订单
			LogTest.LogMsg("待评价 删除订单");
			orderClickHandler("delete_order_btn");
			break;
		case R.id.guanbi_delete_order_btn://关闭 删除订单
			LogTest.LogMsg("关闭 删除订单");
			orderClickHandler("delete_order_btn");
			break;
			
		}
	}
	
	private void orderClickHandler(String type) {
		switch (type) {
		case "daizhifu_pay_btn":
			//TODO
			break;
		case "daizhifu_cencel_order_btn"://需要提醒是否操作
			//取消订单 交易状态 4 关闭原因 2  成交时间?
			OrderUpdateRequest.sendRequest(OrderDetailsActivity.this, mMyHandler, 
					new StringBuilder().append(Constants.API_URL).
					append("appUpdateOrderDataApi.php?userId=").append(mOrderData.getUserId())
					.append("&orderId=").append(mOrderData.getOrder_id())
					.append("&type=cencel_order").toString(), "取消订单", "确定要取消订单？");
			break;
		case "daifahuo_tuikuan_btn":
			//退款 交易状态 4 关闭原因 3  成交时间?
			break;
		case "daifahuo_remind_fahuo_btn":
		    //提醒发货
			Utils.showToast(OrderDetailsActivity.this, "提醒成功 商品会尽快安排发货", Toast.LENGTH_SHORT);
			break;
		case "daishouhuo_confirm_receive_btn":
			//确认收货    交易状态3 评价状态未评价1   成交时间
			
			break;
		case "wuliu_btn": 
			//TODO
			break;
		case "delete_order_btn"://需要提醒是否操作
			//删除订单 交易状态 5
			OrderUpdateRequest.sendRequest(OrderDetailsActivity.this, mMyHandler, 
					new StringBuilder().append(Constants.API_URL).
					append("appUpdateOrderDataApi.php?userId=").append(mOrderData.getUserId())
					.append("&orderId=").append(mOrderData.getOrder_id())
					.append("&type=delete_order").toString(), "删除订单", "删除后可在PC端回收站回复订单");
			
			break;
		case "daipingjia_pingjia_btn":
			//评价 评价状态已评价2
			break;
	
		}
	}
	
	@SuppressLint("HandlerLeak")
	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 2118:
				//修改商品
				break;
				
			case 2119:
				//取消订单和删除订单
				getCancelResponse(msg);
				break;
			}
			
		}
	}
	
	//交易状态 0待付款 1代发货 2待收货 3交易成功 4交易关闭',
	private void setBootomOrderInfoData() {
		
		//把不一定有数据的控件隐藏
		setViewVisibility(false,mOrderDetailTradeNumTv,mOrderDetailSuccessfullTv, mOrderDetailShipmentsTimeTv,mOrderDetailPayTimeTv);
		
		try {
			long currentTimeMillis = System.currentTimeMillis();
			if("0".equals(mTradeState)) {
				mOrderDetailTradeStateTv.setText("等待买家付款");
				
				//3天毫秒数 -
				long h = Long.parseLong(TimeUtil.getMillis(3)) - (currentTimeMillis - Long.parseLong(create_time));
				
				mOrderDetailTradeMsgTv.setText(new StringBuilder().append("剩").append(TimeUtil.getTimeDifference(h)).append("自动关闭"));
				setViewVisibility(true, mOrderDetailTradeNumTv);
				setViewVisibility(true, mOrderDetailBottomDzf);
				
				
				
			} else if("1".equals(mTradeState)) {
				mOrderDetailTradeStateTv.setText("等待商家发货");
				mOrderDetailTradeStateIv.setImageResource(R.drawable.nomessage);
				setViewVisibility(false, mOrderDetailTradeMsgTv);
				setViewVisibility(true, mOrderDetailTradeNumTv, mOrderDetailPayTimeTv, mOrderDetailBottomDfh);
				
				
			} else if("2".equals(mTradeState)) {
				mOrderDetailTradeStateTv.setText("等待确认收货");
				mOrderDetailTradeStateIv.setImageResource(R.drawable.nomessage);
				long h = Long.parseLong(TimeUtil.getMillis(16)) - (currentTimeMillis - Long.parseLong(shipments_time));
				mOrderDetailTradeMsgTv.setText(new StringBuilder().append("剩").append(TimeUtil.getTimeDifference(h)).append("自动确认收货"));
				setViewVisibility(true, mOrderDetailBottomDsh, mOrderDetailShipmentsTimeTv);
				
			} else if("3".equals(mTradeState)) {
				mOrderDetailTradeStateTv.setText("交易成功货");
				setViewVisibility(false, mOrderDetailTradeMsgTv);
				String evaluateState = mOrderData.getEvaluateState();
				
				if("1".equals(evaluateState)) {//待评价
					setViewVisibility(true, mOrderDetailBottomDpj);
					 
				} else if ("2".equals(evaluateState)) {//已评价
					setViewVisibility(true, mOrderDetailBottomDpj);
					setViewVisibility(false, mOrderDetailBottomDpjPingjiaBtn);
				}
				setViewVisibility(true, mOrderDetailSuccessfullTv);
				
				
			} else if("4".equals(mTradeState)) {
				mOrderDetailTradeStateTv.setText("交易关闭");
				
				mOrderDetailTradeStateIv.setImageResource(R.drawable.ic_search_no_data);
				String reason = mOrderData.getCancel_order_reason();
				mOrderDetailTradeMsgTv.setText(reason);
				if("申请退款".equals(reason)) {
					setViewVisibility(true, mOrderDetailPayTimeTv);
					
				}

				setViewVisibility(true, mOrderDetailBottomGb, mOrderDetailSuccessfullTv);
				
				
				
			}
			mOrderDetailOrderNumTv.setText(new StringBuilder().append("订单编号：").append(mOrderData.getOrder_num()));
			mOrderDetailTradeNumTv.setText(new StringBuilder().append("支付订单编号：").append(mOrderData.getTransaction_num()));
			mOrderDetailCreateTimeTv.setText(TextUtils.isEmpty(create_time) ? "" :new StringBuilder().append("创建时间：")
					.append(TimeUtil.StringLongToString(create_time, "yyyy-MM-dd HH:mm:ss")));
			
			mOrderDetailPayTimeTv.setText(TextUtils.isEmpty(pay_time) ? "" :new  StringBuilder().append("支付时间：")
					.append(TimeUtil.StringLongToString(pay_time, "yyyy-MM-dd HH:mm:ss")));
			
			mOrderDetailShipmentsTimeTv.setText(TextUtils.isEmpty(shipments_time) ? "" : new StringBuilder().append("发货时间：")
					.append(TimeUtil.StringLongToString(shipments_time, "yyyy-MM-dd HH:mm:ss")));
			
			mOrderDetailSuccessfullTv.setText(TextUtils.isEmpty(successful_trade_time) ? "" : new StringBuilder().append("成交时间：")
					.append(TimeUtil.StringLongToString(successful_trade_time, "yyyy-MM-dd HH:mm:ss")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void getCancelResponse(Message msg) {
		String response = (String) msg.obj;
		if(response != null && "200".equals(response)) {
			//删除或取消订单成功 重新刷新
			finish();
		} else {
			
		}
		
	}

	/**
	 * 设置view的可见性
	 * @param visibility
	 * @param views
	 */
	private void setViewVisibility(boolean visibility, View...views ){
		for(View view : views) {
			view.setVisibility(visibility ? View.VISIBLE : View.GONE);
		}
	}
	
	
	

	@Override
	public void netWork() {
		
	}

	@Override
	public void noNetWork() {
		
	}


}
