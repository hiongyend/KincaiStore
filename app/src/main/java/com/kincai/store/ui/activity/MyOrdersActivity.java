package com.kincai.store.ui.activity;

import java.util.ArrayList;
import java.util.List;

import c.b.BP;

import com.kincai.store.Constants;
import com.kincai.store.R;
import com.kincai.store.ui.activity.base.BaseActivity;
import com.kincai.store.ui.viewpager.base.BasePager;
import com.kincai.store.ui.viewpager.page.OrderAllOrderPage;
import com.kincai.store.ui.viewpager.page.OrderNoEvaluationPage;
import com.kincai.store.ui.viewpager.page.OrderNoPaymentPage;
import com.kincai.store.ui.viewpager.page.OrderNoReceivePage;
import com.kincai.store.ui.viewpager.page.OrderNoShipmentPage;
import com.kincai.store.utils.AnimationUtil;
import com.kincai.store.utils.LogTest;
import com.kincai.store.view.adapter.OrderViewPagerAdapter;

import android.content.Intent;
import android.provider.Settings;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 *
 * @author kincai
 * 
 * @description 我的订单
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.ui
 *
 * @time 2015-8-24 上午11:35:29
 *
 */
public class MyOrdersActivity extends BaseActivity {
	
	private TextView mTitleTv;
	/** 全部订单*/
	private TextView mAllOrderTv;
	/** 待支付*/
	private TextView mNoPaymentTv;
	/** 待发货*/
	private TextView mNoShipmentsTv;
	/** 待收货*/
	private TextView mNoReceiveTv;
	/** 待评价*/
	private TextView mNoEvaluationTv;
	/** tab下面的线条*/
	private ImageView mTabLineIv;
	
	private ViewPager mOrderViewPager;
	private List<BasePager> mPagers;
	private OrderViewPagerAdapter mOrderViewPagerAdapter;
	
	private int mTabLineWidth;
	
	/** 选中时的字体颜色*/
	private int mSelectColor;
	/** 没选中时的字体颜色*/
	private int mNoSelectColor;
	/** tab当前需要显示的哪页*/
	private int mTabCurrentNum;
	
	/** 全部订单*/
	public static final int ALL_ORDER = 0;
	/** 待支付*/
	public static final int NO_PAYMENT = 1;
	/** 待发货*/
	public static final int NO_SHIPMENT = 2;
	/** 待收货*/
	public static final int NO_RECEIVE = 3;
	/** 待评价*/
	public static final int NO_EVALUATION = 4;
	/** 订单页获取intent数据的标记*/
	public static final String ORDER_INTENT_FLAG = "orderTabCurrentNum";
	private String mUserId;
	
	
	@Override
	public int initContentView() {
		return R.layout.activity_order_layout;
	}
	@Override
	public void initDatas() {
		init();
		getIntentData();
		initView();
		initData();
		setListener();
	}
	
	/**
	 * 初始化
	 */
	private void init() {
		BP.init(this, Constants.BMOB_PAY_ID);
		mSelectColor = getResources().getColor(R.color.approach_red);
		mNoSelectColor = getResources().getColor(R.color.approach_black);
	}
	
	/**
	 * 获取intent传过来的数据
	 */
	private void getIntentData() {
		Intent intent = getIntent();
		mTabCurrentNum = (intent == null) ? ALL_ORDER : intent.getIntExtra(ORDER_INTENT_FLAG, ALL_ORDER);
		mUserId = (intent == null) ? "" : intent.getStringExtra("userId");
	}
	
	@Override
	public void initView() {
		initTabLine();
		super.initView();
		mTitleTv = (TextView) findViewById(R.id.title_tv);
		mAllOrderTv = (TextView) findViewById(R.id.order_tab_all_tv);
		mNoPaymentTv = (TextView) findViewById(R.id.order_tab_no_payment_tv);
		mNoShipmentsTv = (TextView) findViewById(R.id.order_tab_no_shipments_tv);
		mNoReceiveTv = (TextView) findViewById(R.id.order_tab_no_receive_tv);
		mNoEvaluationTv = (TextView) findViewById(R.id.order_tab_no_evaluation_tv);
		mOrderViewPager = (ViewPager) findViewById(R.id.order_viewpager);
		
		mTitleTv.setText(getResources().getString(R.string.my_orders));
	}
	
	/**
	 * 初始化数据
	 */
	private void initData() {
		mPagers = new ArrayList<BasePager>();
		mPagers.add(new OrderAllOrderPage(this, mUserId, "all"));
		mPagers.add(new OrderNoPaymentPage(this, mUserId, "daifukuan"));
		mPagers.add(new OrderNoShipmentPage(this, mUserId, "daifahuo"));
		mPagers.add(new OrderNoReceivePage(this, mUserId, "daishouhuo"));
		mPagers.add(new OrderNoEvaluationPage(this, mUserId, "daipingjia"));
		
		mOrderViewPagerAdapter = new OrderViewPagerAdapter(mPagers);
		mOrderViewPager.setAdapter(mOrderViewPagerAdapter);
		
		//根据从上个页面选中的是哪个 来设置当前类viewpager选中哪页
		mOrderViewPager.setCurrentItem(mTabCurrentNum);
		setTabTextViewSelectColor(mTabCurrentNum);
		tabTextviewClickSetCurrentItem(mTabCurrentNum);
		
		//初始化选中页的数据
		mPagers.get(mTabCurrentNum).initData();
		
	}
	
	/**
	 * 初始化tab线条
	 */
	private void initTabLine() {
		mTabLineIv = (ImageView) findViewById(R.id.order_tabline_iv);
		mTabLineWidth = mScreenWidth / 5;
		RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mTabLineIv.getLayoutParams();
		lp.width = mTabLineWidth;
		mTabLineIv.setLayoutParams(lp);
	}
	
	@Override
	public void setListener() {
		super.setListener();		
		mOrderViewPager.setOnPageChangeListener(new OrderPagerListener());
		
		mAllOrderTv.setOnClickListener(this);
		mNoPaymentTv.setOnClickListener(this);
		mNoShipmentsTv.setOnClickListener(this);
		mNoReceiveTv.setOnClickListener(this);
		mNoEvaluationTv.setOnClickListener(this);
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
		case R.id.order_tab_all_tv:
			tabTextviewClickSetCurrentItem(ALL_ORDER);
			break;
		case R.id.order_tab_no_payment_tv:
			tabTextviewClickSetCurrentItem(NO_PAYMENT);
			break;
		case R.id.order_tab_no_shipments_tv:
			tabTextviewClickSetCurrentItem(NO_SHIPMENT);
			break;
		case R.id.order_tab_no_receive_tv:
			tabTextviewClickSetCurrentItem(NO_RECEIVE);
			break;
		case R.id.order_tab_no_evaluation_tv:
			tabTextviewClickSetCurrentItem(NO_EVALUATION);
			break;
		}
	}
	
	/**
	 * 订单viewpager滑动监听
	 */
	class OrderPagerListener implements OnPageChangeListener {


		@Override
		public void onPageScrollStateChanged(int state) {
			
		}

		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			LogTest.LogMsg("TAG", "onPageScrolled-->position: "+position 
					+ " , positionOffset: " + positionOffset 
					+ " , positionOffsetPixels: "
					+ positionOffsetPixels);

			setTabLineLocation(position, positionOffset);
		}

		@Override
		public void onPageSelected(int position) {
			resetTextViewColor();
			setTabTextViewSelectColor(position);
			mPagers.get(position).initData();
		}
		
	}
	
	/**
	 * tab 点击的时候切换page
	 * @param position
	 */
	private void tabTextviewClickSetCurrentItem(int position) {
		mOrderViewPager.setCurrentItem(position, false);
		
		setTabLineLocation(position, 0);
	}

	
	/**
	 * 设置tab下面的线条位置
	 */
	private void setTabLineLocation(int position, float positionOffset) {
		RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mTabLineIv
				.getLayoutParams();
		
		
		lp.leftMargin=(int) ((position+positionOffset)*mTabLineWidth);
		mTabLineIv.setLayoutParams(lp);
	}
	
	/**
	 * 设置tab选中哪个的字体颜色位选中状态时所对应的颜色
	 * @param position
	 */
	private void setTabTextViewSelectColor(int position) {
		switch (position) {
		case ALL_ORDER:
			mAllOrderTv.setTextColor(mSelectColor);
			break;
		case NO_PAYMENT:
			mNoPaymentTv.setTextColor(mSelectColor);
			break;
		case NO_SHIPMENT:
			mNoShipmentsTv.setTextColor(mSelectColor);
			break;
		case NO_RECEIVE:
			mNoReceiveTv.setTextColor(mSelectColor);
			break;
		case NO_EVALUATION:
			mNoEvaluationTv.setTextColor(mSelectColor);
			break;

		}
	}
	
	/**
	 * 初始化全部tab textview字体颜色
	 */
	private void resetTextViewColor() {
		mAllOrderTv.setTextColor(mNoSelectColor);
		mNoPaymentTv.setTextColor(mNoSelectColor);
		mNoShipmentsTv.setTextColor(mNoSelectColor);
		mNoReceiveTv.setTextColor(mNoSelectColor);
		mNoEvaluationTv.setTextColor(mNoSelectColor);
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
