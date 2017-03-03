package com.kincai.store.ui.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.kincai.store.Constants;
import com.kincai.store.KincaiApplication;
import com.kincai.store.R;
import com.kincai.store.bean.CartInfo;
import com.kincai.store.bean.CartInfo.CartData;
import com.kincai.store.bean.CartInfo.CartData.GoodsData;
import com.kincai.store.bean.ProDetailInfo;
import com.kincai.store.bean.ProDetailInfo.ProDetailData;
import com.kincai.store.bean.ProDetailInfo.ProDetailData.ProDatas;
import com.kincai.store.bean.ProImagePathInfo;
import com.kincai.store.bean.ProInfo;
import com.kincai.store.bean.UserInfo;
import com.kincai.store.db.UserDao;
import com.kincai.store.thread.CachedThreadPoolUtils;
import com.kincai.store.thread.HttpGetThread;
import com.kincai.store.thread.HttpPostThread;
import com.kincai.store.ui.activity.base.BaseActivity;
import com.kincai.store.utils.AnimationUtil;
import com.kincai.store.utils.GsonTools;
import com.kincai.store.utils.JsonUtil;
import com.kincai.store.utils.LoadingDialog;
import com.kincai.store.utils.LogTest;
import com.kincai.store.utils.NetWorkUtil;
import com.kincai.store.utils.Utils;
import com.kincai.store.utils.bitmap_cache.MyImageLoader;
import com.kincai.store.view.custom.ProShowView;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 * 
 * @author kincai
 * 
 * @description 商品详情页
 * 
 * @project Kincai_Store
 * 
 * @package com.kincai.store.ui
 * 
 * @time 2015-6-23 下午3:42:59
 * 
 */
@SuppressLint("SimpleDateFormat")
@SuppressWarnings("unused")
public class ProDetailsActivity extends BaseActivity implements
		OnClickListener {
	/** 百度定位*/
	private LocationClient mLocationClient;
	/** 定位模式*/
	private LocationMode tempMode = LocationMode.Hight_Accuracy;
	private String tempcoor="gcj02";
	private static final String TAG = "ProDetailsActivity";
	private ProShowView mShowView;
	private MyHnadler mMyHnadler;
	
	private ProDetailData mProDetailData;
	
	private TextView mTitleTv;
	private TextView mProNameTv;
	private TextView mProCollectTv;
	private TextView mProPriceTv;
	private TextView mProYuanPriceTv;
	private TextView mProKuaidiAddressTv;
	private TextView mProSalesTv;
	private TextView mProEvaluateTv;
	private TextView mProEvaluateUserTv;
	private TextView mProEvaluateContentTv;
	private TextView mProEvaluateTimeTv;
	private TextView mProEvaluateMoreTv;
	
	
	//商店
	private RelativeLayout mStoreRl;
	private ImageView mStoreLogoIv, mStoreXingyongIv;
	private TextView mStoreNameTv, mStoreAllProNumTv,mStoreAttenNumTv, mStoreDescValueTv,mStoreSellerValueTv,mStoreWuliuValueTv;
	private Button mStoreJIndianBtn;
	
	
	
	//底部
	private Button mProAddCartBtn;
	private Button mProBuyItNowBtn;
	private TextView mProContactSellerTv;
	
	
	
	private TextView[] mTabs;
	
	private Fragment[] fragments;
	private TextView mProGraphicDetailsTv;
	private TextView mProRecommendTv;
	private TextView mProSaleHistoryTv;
	private ImageView mImage1;
	private ImageView mImage2;
	private ImageView mImage3;

	private ImageView mProCartIv;
	private ImageView mProMoreIv;
	/** 标题栏显示的购物车数量 */
	private TextView mProCartNumTv;

	private int index = 0;
	private int currentTabIndex = 0;

	private RelativeLayout mEvaluateRl;

	private List<ProImagePathInfo> mProImagePathInfos;

	/** pro popupwindow 组件 */
	private PopupWindow mAddCartPopupWindow;
	private View mAddCartPopupWindowView;
	private ImageView mPwProImageIv;
	private TextView mPwPropriceTv;
	private TextView mPwProKucunTv;
	private Button mPwJiaCartNumberBtn;
	private Button mPwJianCartNumberBtn;
	private TextView mPwAddCartNumberTv;
	private Button mPwConfimBtn;
	private int mAddCartNum = 1;
	
	/** pro top menu popupwindow 组件 */
	private PopupWindow mTopMenuPopupWindow;
	private View mTopMenuPopupWindowView;
	private TextView mRefreshTv, mSearchTv, mHomeTv, mShareTv;
	
	private String mLocation;
	
	private LoadingDialog mLoadingDialog;
	private int popupwindowWay;//0为加入购物车 1为立即购买

	View vvvView;
	private int pId, StoreId;
	private List<UserInfo> mUserInfos;
	
	

	@Override
	public int initContentView() {
		return R.layout.activity_pro_details_layout;
	}
	@Override
	public void initDatas() {

		init();
		initView();
		initProData();
		setListener();
	}

	/**
	 * 初始化
	 */
	private void init() {
		pId = -1;
		StoreId = -1;
		Intent intent = getIntent();
		if (null != intent) {
			
			pId = intent.getIntExtra("pId", -1);
			StoreId = intent.getIntExtra("storeId", -1);
		}
		mLocationClient = ((KincaiApplication)getApplication()).mLocationClient;
		
		mMyHnadler = new MyHnadler();
		
	}
	
	/**
	 * 设置定位模式
	 */
	private void InitLocation(){
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(tempMode);//设置定位模式
		option.setCoorType(tempcoor);//返回的定位结果是百度经纬度，默认值gcj02
		int span=60 * 1000;
		option.setScanSpan(span);//设置发起定位请求的间隔时间
		option.setIsNeedAddress(true);//反地理编码
		mLocationClient.setLocOption(option);
	}

	/**
	 * 初始化控件
	 */
	public void initView() {
		super.initView();
		vvvView = findViewById(R.id.vvv);
		mTitleTv = (TextView) findViewById(R.id.title_tv);
		mShowView = (ProShowView) findViewById(R.id.details_pro_showview);
		
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.height = mScreenWidth;
		mShowView.setLayoutParams(params);
		

		mProCartIv = (ImageView) findViewById(R.id.pro_cart_iv);
		mProMoreIv = (ImageView) findViewById(R.id.pro_more_iv);
		mProCartNumTv = (TextView) findViewById(R.id.pro_cart_num_tv);

		mTitleTv.setText(getResources().getString(R.string.pro_details));
		mProNameTv = (TextView) findViewById(R.id.pro_name);
		mProCollectTv = (TextView) findViewById(R.id.pro_collect_tv);
		mProPriceTv = (TextView) findViewById(R.id.pro_price_tv);
		mProYuanPriceTv = (TextView) findViewById(R.id.pro_yuan_price_tv);
		mProKuaidiAddressTv = (TextView) findViewById(R.id.pro_kuaidi_address_tv);
		((KincaiApplication)getApplication()).mLocationResult = mProKuaidiAddressTv;
		
		
		
		mProSalesTv = (TextView) findViewById(R.id.pro_sales_tv);
		mProEvaluateTv = (TextView) findViewById(R.id.pro_evaluate_tv);
		mProEvaluateUserTv = (TextView) findViewById(R.id.pro_evaluate_one_user_tv);
		mProEvaluateContentTv = (TextView) findViewById(R.id.pro_evaluate_one_content_tv);
		mProEvaluateTimeTv = (TextView) findViewById(R.id.pro_evaluate_one_time_tv);
		mProEvaluateMoreTv = (TextView) findViewById(R.id.pro_check_more_evaluate_tv);
		mEvaluateRl = (RelativeLayout) findViewById(R.id.evaluate_rl);
		
		
		mStoreRl = (RelativeLayout) findViewById(R.id.store_rl);
		mStoreLogoIv = (ImageView) findViewById(R.id.store_logo_iv);
		mStoreXingyongIv = (ImageView) findViewById(R.id.store_xinyong_iv);;
		mStoreNameTv = (TextView) findViewById(R.id.store_name_tv);
		mStoreAllProNumTv = (TextView) findViewById(R.id.store_all_pro_num_tv);
		mStoreAttenNumTv = (TextView) findViewById(R.id.store_atten_num_tv);
		mStoreDescValueTv = (TextView) findViewById(R.id.store_desc_value_tv);
		mStoreSellerValueTv = (TextView) findViewById(R.id.store_seller_service_value_tv);
		mStoreWuliuValueTv = (TextView) findViewById(R.id.store_wuliu_service_value_tv);
		mStoreJIndianBtn = (Button) findViewById(R.id.store_jindian_btn);
		
		
		mProAddCartBtn = (Button) findViewById(R.id.pro_add_cart_btn);
		mProBuyItNowBtn = (Button) findViewById(R.id.pro_buy_it_now_btn);
		mProContactSellerTv = (TextView) findViewById(R.id.pro_contact_tv);
		mTabs = new TextView[3];
		mTabs[0] = (TextView) findViewById(R.id.pro_graphic_details_tv);
		mTabs[1] = (TextView) findViewById(R.id.pro_recommend_tv);
		mTabs[2] = (TextView) findViewById(R.id.pro_buy_history);

		mTabs[0].setSelected(true);

		mImage1 = (ImageView) findViewById(R.id.pro_iv1);
		mImage2 = (ImageView) findViewById(R.id.pro_iv2);
		mImage3 = (ImageView) findViewById(R.id.pro_iv3);

		mImage1.setBackgroundResource(R.color.approach_red);
		mImage2.setBackgroundResource(R.color.white);
		mImage3.setBackgroundResource(R.color.white);
		
		
		mProKuaidiAddressTv.setText("配送至 广东>广州市>天河区  快递 0.0元");

	}
	
	private void initProData() {
		if (NetWorkUtil.isNetworkAvailable(ProDetailsActivity.this)) {
			if(mLoadingDialog == null) {
				mLoadingDialog = new LoadingDialog(ProDetailsActivity.this, false);
			}
			
			mLoadingDialog.dialogShow();
			
			CachedThreadPoolUtils.execute(new HttpGetThread(mMyHnadler,
					new StringBuffer().append(Constants.API_URL)
							.append("appGetProDetailApi.php?pId=")
							.append(pId).append("&storeId=").append(StoreId).toString()
					, "ProDetail"));
		}
	}

	/**
	 * 初始化数据 1商品图片 2商品属性 3是否收藏 4添加到浏览记录5店铺信息
	 */
	private void initData() {

		if (NetWorkUtil.isNetworkAvailable(ProDetailsActivity.this)
				) {
			
			//获取商品图片
			CachedThreadPoolUtils.execute(new HttpGetThread(mMyHnadler,
					new StringBuffer().append(Constants.API_URL)
							.append("appGetProOneImagePathApi.php?pId=")
							.append(mProDetailData.getProDatas().getId()).toString()
					, "ProDetailsImage"));
			

			// 判断是否登录才能做收藏判断和添加浏览记录操作
			
			if(null != mLocationClient) {
				InitLocation();
				mLocationClient.start();
				
			}
			
			
			if (mSp.getUserIsLogin()) {
				mUserInfos = mBaseUserDao.getUserInfoOne(mSp.getUsername());
				if (null != mUserInfos) {
					String userId = String.valueOf(mUserInfos.get(0)
							.getUserId());
					String pId = String.valueOf(mProDetailData.getProDatas().getId());

					//先判断是否收藏了这个商品
					List<NameValuePair> parameters = new ArrayList<NameValuePair>();
					parameters.add(new BasicNameValuePair("userId", userId));
					parameters.add(new BasicNameValuePair("pId", pId));
					parameters.add(new BasicNameValuePair("type",
							Constants.KINCAI_TYPE_GET));
					CachedThreadPoolUtils.execute(new HttpPostThread(
							mMyHnadler, Constants.API_URL
									+ "appGetIsCollectApi.php",
							parameters, "isCollect"));

					//获取购物车数量
					loadCartNum(userId);

					
					 //把这个商品添加到浏览历史
					 
					List<NameValuePair> parameters2 = new ArrayList<NameValuePair>();
					parameters2.add(new BasicNameValuePair("userId", userId));
					parameters2.add(new BasicNameValuePair("pId", pId));
					parameters2.add(new BasicNameValuePair("type",
							Constants.KINCAI_TYPE_ADD));

					CachedThreadPoolUtils.execute(new HttpPostThread(
							mMyHnadler, Constants.API_URL
									+ "appBrowseHistoryApi.php",
							parameters2, "addBrowseHistory"));

				}

			}


		}
		
		ProDatas proDatas = mProDetailData.getProDatas();
		
		mProNameTv.setText(proDatas.getPName());
		mProPriceTv.setText(new StringBuilder().append("¥ ").append(proDatas.getIPrice()));
		mProYuanPriceTv.setText(new StringBuilder().append("原价 ").append(proDatas.getMPrice()));
		mProSalesTv.setText(new StringBuilder().append("销量 ").append(proDatas.getSale_num()));
		if (proDatas.getEvaluate_num() .equals("0")) {
			mProEvaluateTv.setText(getResources().getString(
					R.string.no_evaluate));
			mEvaluateRl.setVisibility(View.GONE);
		} else {
			mEvaluateRl.setVisibility(View.VISIBLE);
			mProEvaluateTv.setText(new StringBuilder().append("宝贝评价 (").append(proDatas.getEvaluate_num()).append(")"));

			mProEvaluateUserTv.setText("kincai");
			mProEvaluateContentTv
					.setText("还不错的衣服哦！真心觉得衣服挺好的，老板人也好 下次还会再来的");
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date(System.currentTimeMillis());

			mProEvaluateTimeTv.setText(format.format(date));
		}

		mImageLoader.loadImage(
				new StringBuilder()
				.append(Constants.SERVER_URL)
				.append("image_store/logo/")
				.append(mProDetailData.getLogo_url()).toString(),
				mStoreLogoIv);
		mStoreNameTv.setText(mProDetailData.getStore_name());
		mStoreAllProNumTv.setText(new StringBuilder().append(mProDetailData.getPro_num()).append("\n全部宝贝"));
		mStoreAttenNumTv.setText(new StringBuilder().append(mProDetailData.getAttent_num()).append("\n关注人数"));
		mStoreDescValueTv.setText(new StringBuilder().append("宝贝描述 ").append(mProDetailData.getPro_desc_value()));
		mStoreSellerValueTv.setText(new StringBuilder().append("卖家服务 ").append(mProDetailData.getSeller_service_value()));
		mStoreWuliuValueTv.setText(new StringBuilder().append("物流服务 ").append(mProDetailData.getLogistics_service_value()));
	}

	/**
	 * 得到购物车数量
	 * 
	 * @param userId
	 */
	private void loadCartNum(String userId) {
		if (NetWorkUtil.isNetworkAvailable(ProDetailsActivity.this)) {
			CachedThreadPoolUtils.execute(new HttpGetThread(mMyHnadler,
					Constants.API_URL + "appGetIsCartNumApi.php?userId="
							+ userId, "proCartNum"));
		}

	}

	/**
	 * 设置监听
	 */
	public void setListener() {
		super.setListener();
		mProCartIv.setOnClickListener(this);
		mProMoreIv.setOnClickListener(this);
		mProCollectTv.setOnClickListener(this);
		mProEvaluateTv.setOnClickListener(this);
		mProEvaluateContentTv.setOnClickListener(this);
		mProEvaluateTimeTv.setOnClickListener(this);
		mProEvaluateMoreTv.setOnClickListener(this);
		mProEvaluateUserTv.setOnClickListener(this);
		
		mStoreRl.setOnClickListener(this);
		mStoreJIndianBtn.setOnClickListener(this);
		

		mProAddCartBtn.setOnClickListener(this);
		mProBuyItNowBtn.setOnClickListener(this);
		mProCollectTv.setOnClickListener(this);
		mTabs[0].setOnClickListener(this);
		mTabs[1].setOnClickListener(this);
		mTabs[2].setOnClickListener(this);
		mProContactSellerTv.setOnClickListener(this);
	}

	@SuppressLint("HandlerLeak")
	class MyHnadler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
			case 2108:
				getResponseImagePath(msg);
				break;
			case 2109:
				getResponseCartNum(msg);
				break;
			case 2209:
				getResponseIsCollect(msg);
				break;
			case 2210:
				getResponseAddCollect(msg);
				break;
			case 2211:
				getResponseBrowseHistory(msg);
				break;
			case 2212:
				getResponseAddCart(msg);
				break;
			case 2126:
				getResponseProDetailData(msg);
				break;
			}
		}
	}
	
	
	private void getResponseProDetailData(Message msg) {
		String response = (String) msg.obj;
		if (null != response) {
			String code = JsonUtil.msgJson("code", response);
			if("200".equals(code)) {
				parseProDetailData(response);
				
			} else {
				Utils.showToast(this, "加载失败", 0);
			}
		} else {
			Utils.showToast(this, "加载失败", 0);
		}
		
		if(mLoadingDialog != null && mLoadingDialog.isShow()) {
			mLoadingDialog.dismiss();
		}
	}
	
	private void parseProDetailData(String response) {

		ProDetailInfo proDetailInfo = GsonTools.changeGsonToBean(response, ProDetailInfo.class);
		mProDetailData = proDetailInfo.getProDetailData();
		if(mProDetailData != null){
			StoreId = Integer.parseInt(mProDetailData.getStore_id());
			pId = Integer.parseInt(mProDetailData.getProDatas().getId());
			initData();
		}
			
			
		
	}
	

	/**
	 * 获取添加购物车返回数据
	 * 
	 * @param msg
	 */
	private void getResponseAddCart(Message msg) {
		String response = (String) msg.obj;
		if (null != response) {
			String code = JsonUtil.msgJson("code", response);
			if (null != code && "200".equals(code)) {
				LogTest.LogMsg(TAG, "添加购物车成功");

				String userId = String.valueOf(mUserInfos.get(0).getId());
				// 重新加载购物车数量
				loadCartNum(userId);

				// 发送广播更新购物车
				Intent intent = new Intent();
				intent.setAction(Constants.CART_CHANGE_ACTION);
				sendBroadcast(intent);

			}

		}

		mPwConfimBtn.setText(getResources().getString(R.string.confirm));

		mAddCartPopupWindow.dismiss();

	}

	/**
	 * 获得购物车数量
	 * 
	 * @param msg
	 */
	private void getResponseCartNum(Message msg) {
		String response = (String) msg.obj;
		if (null != response) {
			String code = JsonUtil.msgJson("code", response);
			if (null != code && "200".equals(code)) {
				String num = JsonUtil.msgJson("message", response);
				if (null != num) {
					mProCartNumTv.setVisibility(View.VISIBLE);
					mProCartNumTv.setText(num);
					return;
				}
			}
		}

		mProCartNumTv.setVisibility(View.GONE);

	}

	/**
	 * 获取添加浏览历史返回信息
	 * 
	 * @param msg
	 */
	private void getResponseBrowseHistory(Message msg) {
		String response = (String) msg.obj;
		if (null != response) {
			String code = JsonUtil.msgJson("code", response);
			if (null != code && "200".equals(code)) {
				Intent intent = new Intent();
				intent.setAction(Constants.BROWSE_HISTORY_CHANGE_ACTION);
				sendBroadcast(intent);
			}

		}

	}

	/**
	 * 获取添加收藏返回信息
	 * 
	 * @param msg
	 */
	private void getResponseAddCollect(Message msg) {
		String response = (String) msg.obj;
		if (null != response) {
			String code = JsonUtil.msgJson("code", response);
			if (null != code) {
				if ("200".equals(code)) {
					Utils.showToast(ProDetailsActivity.this, "收藏成功",
							Toast.LENGTH_SHORT);
					Drawable drawable = getResources().getDrawable(
							R.drawable.ic_web_favor_select_old);
					// / 这一步必须要做,否则不会显示.
					drawable.setBounds(0, 0, drawable.getMinimumWidth(),
							drawable.getMinimumHeight());
					mProCollectTv.setCompoundDrawables(null, drawable, null,
							null);
				} else if ("402".equals(code)) {
					Utils.showToast(ProDetailsActivity.this, "取消收藏",
							Toast.LENGTH_SHORT);
					Drawable drawable = getResources().getDrawable(
							R.drawable.ic_web_favor_normal);
					// / 这一步必须要做,否则不会显示.
					drawable.setBounds(0, 0, drawable.getMinimumWidth(),
							drawable.getMinimumHeight());
					mProCollectTv.setCompoundDrawables(null, drawable, null,
							null);
				}
			}
		}

	}

	/**
	 * 初始化时获取是否收藏返回信息
	 * 
	 * @param msg
	 */
	private void getResponseIsCollect(Message msg) {
		String response = (String) msg.obj;
		if (null != response) {
			String code = JsonUtil.msgJson("code", response);
			if (null != code && "200".equals(code)) {
				Drawable drawable = getResources().getDrawable(
						R.drawable.ic_web_favor_select_old);
				// / 这一步必须要做,否则不会显示.
				drawable.setBounds(0, 0, drawable.getMinimumWidth(),
						drawable.getMinimumHeight());
				mProCollectTv.setCompoundDrawables(null, drawable, null, null);
			}
		}
	}

	/**
	 * 获取图片返回信息
	 * 
	 * @param msg
	 */
	private void getResponseImagePath(Message msg) {

		String response = (String) msg.obj;
		if (null != response) {
			String code = JsonUtil.msgJson("code", response);
			if (null != code && "200".equals(code)) {
				List<ProImagePathInfo> list = JsonUtil.getImgPath("data",
						response);
				if (null != list) {
					mProImagePathInfos = list;
					mShowView.init(mProImagePathInfos, mMyHnadler);
				}
			}
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
		case R.id.pro_cart_iv://顶部购物车按钮
			
//			startActivity(new Intent(ProDetailsActivity.this, OrderDetailsActivity.class));
			JumpToMainActivityOfCart();
			
			break;
		case R.id.pro_more_iv:
			showTopMenuPopupwindow();
			break;
		case R.id.pro_collect_tv:

			addCollect();
			break;
		case R.id.pro_add_cart_btn:

			popupwindowWay = 0;
			if(mProDetailData != null)
			showAddCartPopupwindow();
			else Utils.showToast(ProDetailsActivity.this, "加载失败！请刷新", 0);
			break;
		case R.id.pro_buy_it_now_btn:
			
			popupwindowWay = 1;
			if(mProDetailData != null)
			showAddCartPopupwindow();
			else Utils.showToast(ProDetailsActivity.this, "加载失败！请刷新", 0);
			
			break;
		case R.id.pro_evaluate_tv:
			break;
		case R.id.pro_evaluate_one_user_tv:
			break;
		case R.id.pro_evaluate_one_content_tv:
			break;
		case R.id.pro_evaluate_one_time_tv:
			break;
		case R.id.pro_graphic_details_tv:
			index = 0;
			mImage1.setBackgroundResource(R.color.approach_red);
			mImage2.setBackgroundResource(R.color.white);
			mImage3.setBackgroundResource(R.color.white);
			topTab();
			break;
		case R.id.pro_recommend_tv:

			index = 1;
			mImage1.setBackgroundResource(R.color.white);
			mImage2.setBackgroundResource(R.color.approach_red);
			mImage3.setBackgroundResource(R.color.white);
			topTab();
			break;
		case R.id.pro_buy_history:
			index = 2;
			mImage1.setBackgroundResource(R.color.white);
			mImage2.setBackgroundResource(R.color.white);
			mImage3.setBackgroundResource(R.color.approach_red);
			topTab();
			break;

		case R.id.pro_pw_confim_btn://popupwindow-加入购物车确认

			if (mAddCartPopupWindow != null && mAddCartPopupWindow.isShowing()) {
				mAddCartPopupWindow.dismiss();
			}
			
			if(popupwindowWay == 0) {
				addCart();
			} else if (popupwindowWay == 1) {
				confirmOrder();
			}
			
			
			break;
		case R.id.select_pro_number_jia_btn://popupwindow-加
			if (mAddCartNum < 100) {
				mAddCartNum++;
				mPwAddCartNumberTv.setText(String.valueOf(mAddCartNum));
			}

			break;
		case R.id.select_pro_number_jian_btn://popupwindow-减

			if (mAddCartNum > 1) {
				mAddCartNum--;
				mPwAddCartNumberTv.setText(String.valueOf(mAddCartNum));
			}
			break;
		case R.id.pro_contact_tv:
			contactSeller();
			
			break;
		case R.id.top_menu_refresh_tv://top menu popupwindow-刷新
			initProData();
			dismissTopMenuPopupwindow();
			break;
		case R.id.top_menu_search_tv://top menu popupwindow-搜索
			
			startActivity(new Intent(this, SearchGoodsOnActivity.class));
			AnimationUtil.startHaveSinkActivityAnimation(this);
			dismissTopMenuPopupwindow();
			break;
		case R.id.top_menu_home_tv://top menu popupwindow-首页
			MainActivity.instance.SetCurrentTab(0);
			mKincaiApplication.exitInAdditionToTheMainAtivityOfOtherActivities();
			AnimationUtil.finishHaveFloatActivityAnimation(this);
			dismissTopMenuPopupwindow();
			break;
		case R.id.top_menu_share_tv://top menu popupwindow-分享
			
			dismissTopMenuPopupwindow();
			break;
		case R.id.store_rl:
			if("-1".equals(StoreId) || mProDetailData == null) {
				break;
			}
			startActivity(new Intent(this, StoreActivity.class)
			.putExtra("storeId", String.valueOf(StoreId)));
			AnimationUtil.startHaveSinkActivityAnimation(this);
			break;
		case R.id.store_jindian_btn:
			if("-1".equals(StoreId) || mProDetailData == null) {
				break;
			}
			startActivity(new Intent(this, StoreActivity.class)
			.putExtra("storeId", String.valueOf(StoreId)));
			AnimationUtil.startHaveSinkActivityAnimation(this);
			break;
		}

	}
	
	private void confirmOrder() {
		if(mSp.getUserIsLogin() && mProImagePathInfos != null && mProDetailData != null) {
//			startActivityForResult(new Intent(ProDetailsActivity.this, SelectPayWayActivity.class), 10);
//			AnimationUtil.startHaveSinkActivityAnimation(this);
			
			final CartInfo cartInfo = new CartInfo();
			List<CartData> cartDatas = new ArrayList<CartData>();
			CartData cartData = new CartData();
			
			List<GoodsData> goodsDatas = new ArrayList<GoodsData>();
			GoodsData goodsData = new GoodsData();
			
			
			ProDatas proDatas = mProDetailData.getProDatas();
			goodsData.setId(proDatas.getId());
			goodsData.setCId(proDatas.getCId());
			goodsData.setSupport_num(proDatas.getSupport_num());
			goodsData.setStore_id(proDatas.getStore_id());
			goodsData.setSale_num(proDatas.getSale_num());
			goodsData.setPubTime(proDatas.getPubTime());
			goodsData.setPSn(proDatas.getPSn());
			goodsData.setPNum(proDatas.getPNum());
			goodsData.setPName(proDatas.getPName());
			goodsData.setPDesc(proDatas.getPDesc());
			goodsData.setPay_num(proDatas.getPay_num());
			goodsData.setMPrice(proDatas.getMPrice());
			goodsData.setIsShow(proDatas.getIsShow());
			goodsData.setIsHot(proDatas.getIsHot());
			goodsData.setIPrice(proDatas.getIPrice());
			goodsData.setIntegral(proDatas.getIntegral());
			goodsData.setGoods_num(String.valueOf(mAddCartNum));
			goodsData.setGoods_img_url( mProImagePathInfos.get(0).getAlbumPath());
			goodsData.setGoods_attr("颜色分类：粉红；尺码：170/L");
			goodsData.setEvaluate_num(proDatas.getEvaluate_num());
			
			
			cartData.setStore_name(mProDetailData.getStore_name());
			cartData.setStore_id(mProDetailData.getStore_id());
			goodsDatas.add(goodsData);
			cartData.setGoods_data(goodsDatas);
			cartDatas.add(cartData);
			cartInfo.setCartData(cartDatas);
			
			if(mLoadingDialog == null) {
				mLoadingDialog = new LoadingDialog(ProDetailsActivity.this, false);
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
							mUserInfos = mBaseUserDao.getUserInfoOne(mSp.getUsername());
							if(mUserInfos != null ) {
								String userId = String.valueOf(mUserInfos.get(0).getUserId());
								startActivity(new Intent(ProDetailsActivity.this,ConfirmOrderActivity.class)
								.putExtra("cartinfo", cartInfo)
								.putExtra("userId", userId));
								AnimationUtil.startHaveSinkActivityAnimation(ProDetailsActivity.this);
							} else {
								Utils.showToast(ProDetailsActivity.this, "加载失败 请重新选择", Toast.LENGTH_SHORT);
							}
							
							
						}
					}, 500);
					
				}
			}, 1000);
			
			
			
		} else {
			isNoLogin();
		}
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
		
		if(resultCode == 0 || resultCode == 1) {
			if(mLoadingDialog == null) {
				mLoadingDialog = new LoadingDialog(ProDetailsActivity.this, false);
			}
			
//			mLoadingDialog.dialogShow();
			
			/*new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					
					mLoadingDialog.dismiss();
					mLoadingDialog = null;
					
					new Handler().postDelayed(new Runnable() {
						
						@Override
						public void run() {
							mUserInfos = mUserDao.getUserInfoOne(mSp.getUsername());
							if(mUserInfos != null && mProImagePathInfos != null && mProInfos != null) {
								String userId = String.valueOf(mUserInfos.get(0).getUserId());
								startActivity(new Intent(ProDetailsActivity.this,ConfirmOrderActivity.class)
								.putExtra("payway", resultCode)
								.putExtra("pro", mProInfos)
								.putExtra("num", mAddCartNum)
								.putExtra("userId", userId)
								.putExtra("proImgUrl",  mProImagePathInfos.get(0).getAlbumPath()));
								AnimationUtil.startHaveSinkActivityAnimation(ProDetailsActivity.this);
							} else {
								Utils.showToast(ProDetailsActivity.this, "加载失败 请重新选择", Toast.LENGTH_SHORT);
							}
							
							
						}
					}, 500);
					
				}
			}, 1000);*/
			
			
		}
		
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	/**
	 * 关闭top menu popupwindow
	 */
	private void dismissTopMenuPopupwindow() {
		if(mTopMenuPopupWindow != null && mTopMenuPopupWindow.isShowing()) {
			mTopMenuPopupWindow.dismiss();
		}
	}
	
	/**
	 * 跳到购物车
	 */
	private void JumpToMainActivityOfCart() {
		startActivity(new Intent(ProDetailsActivity.this, MainActivity.class).putExtra("again", true));
		AnimationUtil.startHaveSinkActivityAnimation(this);
	}
	
	/**
	 * 联系卖家
	 */
	private void contactSeller() {
		Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+"13560577498"));  
		startActivity(intent);
		AnimationUtil.startHaveSinkActivityAnimation(this);

	}

	/**
	 * tab切换
	 */
	private void topTab() {

		mTabs[currentTabIndex].setSelected(false);
		// 把当前tab设为选中状态
		mTabs[index].setSelected(true);
		currentTabIndex = index;
	}

	/**
	 * 添加到购物车
	 */
	private void addCart() {
		if (mSp.getUserIsLogin()
				&& NetWorkUtil.isNetworkAvailable(ProDetailsActivity.this)) {
			if (null != mUserInfos) {
				mPwConfimBtn.setText(getResources().getString(
						R.string.add_cart_load_str));

				List<NameValuePair> parameters = new ArrayList<NameValuePair>();
				parameters.add(new BasicNameValuePair("userId", String
						.valueOf(mUserInfos.get(0).getUserId())));
				parameters.add(new BasicNameValuePair("store_id", String
						.valueOf(mProDetailData.getStore_id())));
				parameters.add(new BasicNameValuePair("pId", String
						.valueOf(mProDetailData.getProDatas().getId())));
				parameters.add(new BasicNameValuePair("cartNum", String
						.valueOf(mAddCartNum)));
				String mProAttr = "颜色分类：粉红；尺码：170/L";
				parameters.add(new BasicNameValuePair("goods_attr", mProAttr ));

				parameters.add(new BasicNameValuePair("name",
						Constants.KINCAI_CART));
				parameters.add(new BasicNameValuePair("type",
						Constants.KINCAI_TYPE_ADD));

				CachedThreadPoolUtils.execute(new HttpPostThread(mMyHnadler,
						Constants.API_URL + "appCollectAndCartApi.php",
						parameters, "addCart"));
			}

		} else {
			isNoLogin();
		}

	}
	
	private void isNoLogin() {
		startActivity(new Intent(ProDetailsActivity.this, UserLoginActivity.class));
		AnimationUtil.startHaveSinkActivityAnimation(ProDetailsActivity.this);
	}

	/**
	 * 添加收藏夹
	 */
	private void addCollect() {
		// 有登录的情况
		if (mSp.getUserIsLogin()
				&& NetWorkUtil.isNetworkAvailable(ProDetailsActivity.this)) {
			if (null != mUserInfos && mProDetailData != null) {
				List<NameValuePair> parameters = new ArrayList<NameValuePair>();
				parameters.add(new BasicNameValuePair("userId", String
						.valueOf(mUserInfos.get(0).getUserId())));
				parameters.add(new BasicNameValuePair("pId", String
						.valueOf(mProDetailData.getProDatas().getId())));
				parameters.add(new BasicNameValuePair("type",
						Constants.KINCAI_TYPE_ADD));

				CachedThreadPoolUtils.execute(new HttpPostThread(mMyHnadler,
						Constants.API_URL + "appGetIsCollectApi.php",
						parameters, "addCollect"));
			}

		}

	}
	
	private void showTopMenuPopupwindow() {
		if(mTopMenuPopupWindow != null && mTopMenuPopupWindow.isShowing()) {
			mTopMenuPopupWindow.dismiss();
			return;
		} else {
			topMenuPopupWindowMenu();
		}
	}

	private void showAddCartPopupwindow() {
		if (mAddCartPopupWindow != null && mAddCartPopupWindow.isShowing()) {
			mAddCartPopupWindow.dismiss();

			// mCollectDefaultBtn.
			return;
		} else {
			addCartpopupWindowMenu();

		}
	}
	
	/**
	 * 右键菜单
	 */
	public void addCartpopupWindowMenu() {

		LogTest.LogMsg(TAG, "popupWindow为空？" + (mAddCartPopupWindow == null));

		if (mAddCartPopupWindow == null) {

			mAddCartPopupWindowView = View.inflate(this,
					R.layout.popupwindow_pro_add_cart_layout, null);
			mAddCartPopupWindow = new PopupWindow(mAddCartPopupWindowView,
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
			mAddCartPopupWindow.setAnimationStyle(R.style.AnimBottom);
			// 设置焦点和背景 使得popupwindow获得焦点 可以响应事件 如按返回键
			mAddCartPopupWindow.setFocusable(true);//
			ColorDrawable drawable = new ColorDrawable(getResources().getColor(
					R.color.transparent));
			mAddCartPopupWindow.setBackgroundDrawable(drawable);

			mAddCartPopupWindowView.setOnTouchListener(new OnTouchListener() {

				@SuppressLint("ClickableViewAccessibility")
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					if (mAddCartPopupWindow != null && mAddCartPopupWindow.isShowing()) {
						mAddCartPopupWindow.dismiss();
						// popupWindow = null;
					}

					return ProDetailsActivity.this.onTouchEvent(event);
				}

			});
			
			mAddCartPopupWindow.setOnDismissListener(new OnDismissListener() {
				
				@Override
				public void onDismiss() {
					setAlpha(1.0f, 0.0f, 500, false);
				}
			});

			mPwProImageIv = (ImageView) mAddCartPopupWindowView
					.findViewById(R.id.pro_pw_proimage_iv);
			mPwPropriceTv = (TextView) mAddCartPopupWindowView
					.findViewById(R.id.pro_pw_price_tv);
			mPwProKucunTv = (TextView) mAddCartPopupWindowView
					.findViewById(R.id.pro_pw_kucun_tv);
			mPwJiaCartNumberBtn = (Button) mAddCartPopupWindowView
					.findViewById(R.id.select_pro_number_jia_btn);
			mPwJianCartNumberBtn = (Button) mAddCartPopupWindowView
					.findViewById(R.id.select_pro_number_jian_btn);
			mPwAddCartNumberTv = (TextView) mAddCartPopupWindowView
					.findViewById(R.id.select_pro_number_tv);
			mPwConfimBtn = (Button) mAddCartPopupWindowView
					.findViewById(R.id.pro_pw_confim_btn);

			mPwAddCartNumberTv.setText(String.valueOf(mAddCartNum));
			mPwConfimBtn.setOnClickListener(this);
			mPwJianCartNumberBtn.setOnClickListener(this);
			mPwJiaCartNumberBtn.setOnClickListener(this);
		}

		/**
		 * 显示在父窗体的哪个位置
		 */
		mAddCartPopupWindow.showAtLocation(mProAddCartBtn,
				Gravity.BOTTOM, 0, 0);
		if (null != mProDetailData) {
			mPwPropriceTv.setText(new StringBuilder().append("¥ ").append(mProDetailData.getProDatas().getIPrice()));
			mPwProKucunTv.setText(new StringBuilder().append("库存").append(mProDetailData.getProDatas().getPNum()).append("件"));
			Drawable image = mShowView.getDrawableOne();
			if (null != image) {
				mPwProImageIv.setImageDrawable(image);
			} else {
				mPwProImageIv.setImageDrawable(getResources().getDrawable(
						R.drawable.ic_launcher));
			}
			
		}
		setAlpha(0.0f, 1.0f, 500, true);

	}
	
	private void setAlpha(float fromAlpha, float toAlpha, int durationMillis, final boolean visibility) {
		AlphaAnimation alphaAnimation = new AlphaAnimation(fromAlpha, toAlpha);
		alphaAnimation.setFillAfter(true);
		alphaAnimation.setDuration(durationMillis);
		vvvView.setVisibility(View.VISIBLE);
		alphaAnimation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				if(!visibility) {
					vvvView.setVisibility(View.GONE);
				}
			}
		});
		
		vvvView.startAnimation(alphaAnimation);
		
	}
	
	/**
	 * 顶部更多菜单
	 */
	@SuppressLint("ClickableViewAccessibility")
	private void topMenuPopupWindowMenu() {
		if(mTopMenuPopupWindow == null) {
			mTopMenuPopupWindowView = View.inflate(this, R.layout.popupwindow_top_menu_layout, null);
			mTopMenuPopupWindow = new PopupWindow(mTopMenuPopupWindowView, mScreenWidth / 2, LayoutParams.WRAP_CONTENT, true);
			
			ColorDrawable drawable = new ColorDrawable(getResources().getColor(
					R.color.transparent));
			mTopMenuPopupWindow.setBackgroundDrawable(drawable);
			mTopMenuPopupWindowView.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					if (mTopMenuPopupWindow != null && mTopMenuPopupWindow.isShowing()) {
						mTopMenuPopupWindow.dismiss();
						// popupWindow = null;
					}

					return ProDetailsActivity.this.onTouchEvent(event);
				}

			});
			
			
			mRefreshTv = (TextView) mTopMenuPopupWindowView.findViewById(R.id.top_menu_refresh_tv);
			mSearchTv = (TextView) mTopMenuPopupWindowView.findViewById(R.id.top_menu_search_tv);
			mHomeTv = (TextView) mTopMenuPopupWindowView.findViewById(R.id.top_menu_home_tv);
			mShareTv = (TextView) mTopMenuPopupWindowView.findViewById(R.id.top_menu_share_tv);
			
			mRefreshTv.setOnClickListener(this);
			mSearchTv.setOnClickListener(this);
			mHomeTv.setOnClickListener(this);
			mShareTv.setOnClickListener(this);
		}
		
		mTopMenuPopupWindow.showAsDropDown(mProMoreIv, 0, 0);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (event.getAction() == KeyEvent.ACTION_DOWN
				&& keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			AnimationUtil.finishHaveFloatActivityAnimation(this);
			return true;
		}
		
		return super.onKeyDown(keyCode, event);
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
		super.onRestart();
		LogTest.LogMsg(TAG, "ProDetailsActivity-onRestart");
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		// initData();
		LogTest.LogMsg(TAG, "ProDetailsActivity-onStart");
	}

	@Override
	protected void onResume() {
		super.onResume();

		// initData();
		LogTest.LogMsg(TAG, "ProDetailsActivity-onResume");

	}

	@Override
	protected void onPause() {
		super.onPause();
		if(mLoadingDialog != null && mLoadingDialog.isShow()) {
			mLoadingDialog.dismiss();
			mLoadingDialog = null;
		}
		LogTest.LogMsg(TAG, "ProDetailsActivity-onPause");
	}
	
	@Override
	protected void onStop() {
		if(null != mLocationClient) {
			mLocationClient.stop();
		}
		
		super.onStop();
		LogTest.LogMsg(TAG, "ProDetailsActivity-onStop");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		LogTest.LogMsg(TAG, "ProDetailsActivity-onDestroy");

	}

	

}
