package com.kincai.store.ui.viewpager.base;

import java.util.List;

import com.kincai.store.Constants;
import com.kincai.store.R;
import com.kincai.store.bean.OrderInfo;
import com.kincai.store.bean.OrderInfo.OrderData;
import com.kincai.store.common.OrderUpdateRequest;
import com.kincai.store.model.IOrderClickListener;
import com.kincai.store.model.IOrderListClickListener;
import com.kincai.store.model.IReflashListener;
import com.kincai.store.thread.CachedThreadPoolUtils;
import com.kincai.store.thread.HttpGetThread;
import com.kincai.store.ui.activity.OrderDetailsActivity;
import com.kincai.store.ui.activity.StoreActivity;
import com.kincai.store.ui.activity.base.BaseActivity;
import com.kincai.store.utils.AnimationUtil;
import com.kincai.store.utils.GsonTools;
import com.kincai.store.utils.JsonUtil;
import com.kincai.store.utils.LogTest;
import com.kincai.store.utils.NetWorkUtil;
import com.kincai.store.utils.Utils;
import com.kincai.store.view.adapter.OrderListAdapter2;
import com.kincai.store.view.custom.HomeListview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
 * @description TODO
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.ui.viewpager.base
 *
 * @time 2015-8-24 下午9:27:29
 *
 */
public abstract class BasePager implements IReflashListener, IOrderClickListener, IOrderListClickListener {
	private static final String TAG = "BasePager";
	
	MyHandler mMyHandler;
	private HomeListview mAllOrderListview;
	private LinearLayout mProgressBarll;
	private LinearLayout mNoOrderOrNoNetwordTip;
	private ImageView tipIv;
	private TextView tipTv;
	private Button tipBtn;
	
	/** 当前加载页 */
	private static int currentPage = 1;
	private boolean isHavaNextPage;
	
	public Context mContext;
	public View mBaseView;
	public String mUserId;
	public String mType;
	private OrderListAdapter2 mOrderListAdapter;
	
	private List<OrderData> mOrderData;
	private List<OrderData> mOrderNewsDatas;
	private String mFlag = "";
	public BasePager(Context context, String userId, String type) {
		mContext = context;
		mUserId = userId;
		mType = type;
		mBaseView = initView();
		LogTest.LogMsg(TAG, TAG+"-被调用");
	}
	
	public abstract View initView();
	
	/**
	 * 初始化数据
	 */
	public void initData() {
		initOb();
		initViews();
		mMyHandler = new MyHandler();
		loadOrderData(1, Constants.GET_PAGESIZE);
	}
	
	private void initViews() {
		mAllOrderListview = (HomeListview) mBaseView.findViewById(R.id.order_lv);
		mProgressBarll = (LinearLayout) mBaseView.findViewById(R.id.loading_progress_ll);
		mNoOrderOrNoNetwordTip = (LinearLayout) mBaseView.findViewById(R.id.no_order_tip_ll);
		tipIv = (ImageView) mBaseView.findViewById(R.id.order_no_order_iv);
		tipTv = (TextView) mBaseView.findViewById(R.id.order_no_order_tv);
		tipBtn = (Button) mBaseView.findViewById(R.id.order_no_netword_btn);
		setListener();
	}
	
	/**
	 * 设置view的可见性
	 * @param visibility
	 * @param views
	 */
	public void setViewVisibility(boolean visibility, View...views ){
		for(View view : views) {
			view.setVisibility(visibility ? View.VISIBLE : View.GONE);
		}
	}
	
	private void setListener() {
		tipBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				loadOrderData(1, Constants.GET_PAGESIZE);
			}
		});
		
		mAllOrderListview.setOnRefreshListener(this);
	}
	
	private void loadOrderData(int page, int pageSize) {

		if (NetWorkUtil.isNetworkAvailable(mContext)) {
			setViewVisibility(true, mProgressBarll);

			CachedThreadPoolUtils.execute(new HttpGetThread(mMyHandler,
					new StringBuilder().append(Constants.API_URL)
							.append("appGetOrderApi.php").append("?userId=")
							.append(mUserId).append("&type=").append(mType)
							.append("&page=").append(page).append("&pageSize=")
							.append(pageSize).toString(),
					"allOrderData"));

		} else {
			setViewVisibility(false, mProgressBarll, mAllOrderListview);
			setViewVisibility(true, mNoOrderOrNoNetwordTip, tipBtn);
			tipTv.setText("加载失败，请检查您的网络环境！");
			tipIv.setImageResource(R.drawable.limit_icon_erronetwork_h);
		}
	}
	
	@SuppressLint("HandlerLeak")
	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			
			switch (msg.what) {
			case 2117:
				updatePersionFragmentOrderData();
				setViewVisibility(false, mProgressBarll);
				mAllOrderListview.onRefreshComplete();
				
				disposePageLoadData(msg);
				break;
			case 2119:
				getCancelResponse(msg);
				break;
			}
		}
	}
	
	
	/**
	 * 处理判断分页加载数据
	 * 
	 * @param msg
	 */
	private void disposePageLoadData(Message msg) {
		if (null == mOrderData) {
			mOrderData = getSearchData(msg);
			
			if(null != mOrderData) {
			}
			
		} else {
			mOrderNewsDatas = getSearchData(msg);
			if (null != mOrderNewsDatas && mOrderNewsDatas.size() > 0) {
				mOrderData.addAll(mOrderNewsDatas);
				currentPage++;
				
				LogTest.LogMsg(TAG, "currentPage " + currentPage);
			}
			
		}
		
		setSearchData(mOrderData);

	}
	
	
	private void setSearchData(List<OrderData> mOrderData) {
		if(mOrderData != null) {
			setViewVisibility(true, mAllOrderListview);
			setViewVisibility(false, mNoOrderOrNoNetwordTip);
			
			setDatas(mOrderData);
			
		} else if("400".equals(mFlag)){
			setViewVisibility(true, mNoOrderOrNoNetwordTip);
			setViewVisibility(false, mAllOrderListview, tipBtn);
			tipTv.setText("您没有相关订单");
			tipIv.setImageResource(R.drawable.usercenter_order);
		}
	}

	private List<OrderData> getSearchData(Message msg) {
		String response = (String) msg.obj;
		
		if(response != null ) {
			String code = JsonUtil.msgJson("code",response);
			if("200".equals(code)) {
				String msgs = JsonUtil.msgJson("message", response);
				if("next".equals(msgs)) {
					isHavaNextPage = true;
				} else if ("nonext".equals(msgs)) {
					isHavaNextPage = false;
				}
				//解析数据
				return parseData(response);
				
			} else if ("400".equals(code)) {//没数据
				mFlag = "400";
				
			}
			
		} 
		
		return null;
		
	}
	private void getCancelResponse(Message msg) {
		String response = (String) msg.obj;
		if(response != null && "200".equals(response)) {
			//删除或取消订单成功 重新刷新
			initOb();
			loadOrderData(1, Constants.GET_PAGESIZE);
		} else {
			
		}
		
	}
	private void initOb() {
		mOrderData = null;
		currentPage = 1;
	}
	private List<OrderData> parseData(String response) {
		OrderInfo orderInfo = GsonTools.changeGsonToBean(response, OrderInfo.class);
		List<OrderData> datas = orderInfo.getData();
		return datas;
		
	}
	
	private void setDatas(List<OrderData> datas) {
		if(mOrderListAdapter == null) {
			mOrderListAdapter = new OrderListAdapter2(mContext, datas);
			mOrderListAdapter.setOnOrderClickListener(this);
			mOrderListAdapter.setOnOrderListClickListener(this);
			mAllOrderListview.setAdapter(mOrderListAdapter);
		} else {
			mOrderListAdapter.onChange(datas);
		}
		
	}
	
	private void orderClickHandler(int position, String type, List<OrderData> datas) {
		switch (type) {
		case "daizhifu_pay_btn":
			//TODO
			break;
		case "daizhifu_cencel_order_btn":
			//取消订单 交易状态 4 关闭原因 2  成交时间?
			OrderUpdateRequest.sendRequest(mContext, mMyHandler, 
					new StringBuilder().append(Constants.API_URL).
					append("appUpdateOrderDataApi.php?userId=").append(mUserId)
					.append("&orderId=").append(datas.get(position).getOrder_id())
					.append("&type=cencel_order").toString(), "取消订单", "确定要取消订单？");

			break;
		case "daifahuo_tuikuan_btn":
			//退款 交易状态 4 关闭原因 3  成交时间?
			break;
		case "daifahuo_remind_fahuo_btn":
		    //提醒发货
			
			Utils.showToast(mContext, "提醒成功 商品会尽快安排发货", Toast.LENGTH_SHORT);
			break;
		case "daishouhuo_confirm_receive_btn":
			//确认收货    交易状态3 评价状态未评价1   成交时间?
			
			break;
		case "wuliu_btn": 
			//TODO
			break;
		case "delete_order_btn":
			//删除订单 交易状态 5
			OrderUpdateRequest.sendRequest(mContext, mMyHandler, 
					new StringBuilder().append(Constants.API_URL).
					append("appUpdateOrderDataApi.php?userId=").append(mUserId)
					.append("&orderId=").append(datas.get(position).getOrder_id())
					.append("&type=delete_order").toString(), "删除订单", "删除后可在PC端回收站回复订单");
			break;
		case "daipingjia_pingjia_btn":
			//评价 评价状态已评价2
			break;
			
		case "shangjia_order_btn":
			//TODO 进入店铺
			mContext.startActivity(new Intent(mContext, StoreActivity.class)
			.putExtra("storeId", datas.get(position).getStore_id()));
			AnimationUtil.startHaveSinkActivityAnimation((BaseActivity)mContext);
			break;
	
		}
	}
	
	protected void updatePersionFragmentOrderData() {
		Intent intent = new Intent();
		intent.setAction(Constants.PERSIONFRAGMENT_ORDER_DATA_UPDATE_ACTION);
		mContext.sendBroadcast(intent);
	}
	
	@Override
	public void onReflash() {
		mAllOrderListview.onRefreshComplete();
	}
	
	@Override
	public void onLoadMore() {
		if (NetWorkUtil.isNetworkAvailable(mContext)
				&& isHavaNextPage) {
			loadOrderData(currentPage+1, Constants.GET_PAGESIZE);
		} else {
			mAllOrderListview.onRefreshComplete();
		}	
	}
	
	@Override
	public void onOrderClick(int position, String type) {
		orderClickHandler(position, type, mOrderData);
	}
	
	@Override
	public void onOrderListClick(int position) {
		//TODO 进入订单详情
		OrderData orderData = mOrderData.get(position);
		mContext.startActivity(new Intent(mContext, OrderDetailsActivity.class).putExtra("orderData", orderData));
		AnimationUtil.startHaveSinkActivityAnimation((BaseActivity)mContext);
	}
	

}
