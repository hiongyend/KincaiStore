package com.kincai.store.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kincai.store.Constants;
import com.kincai.store.R;
import com.kincai.store.bean.ProInfo;
import com.kincai.store.bean.StoreInfo;
import com.kincai.store.bean.CartInfo.CartData;
import com.kincai.store.bean.StoreInfo.StoreData;
import com.kincai.store.bean.UserInfo;
import com.kincai.store.model.IReflashListener;
import com.kincai.store.thread.CachedThreadPoolUtils;
import com.kincai.store.thread.HttpGetThread;
import com.kincai.store.ui.activity.MyOrdersActivity.OrderPagerListener;
import com.kincai.store.ui.activity.base.BaseActivity;
import com.kincai.store.ui.viewpager.base.BasePager;
import com.kincai.store.ui.viewpager.base.StoreBasePager;
import com.kincai.store.ui.viewpager.page.OrderAllOrderPage;
import com.kincai.store.ui.viewpager.page.OrderNoEvaluationPage;
import com.kincai.store.ui.viewpager.page.OrderNoPaymentPage;
import com.kincai.store.ui.viewpager.page.OrderNoReceivePage;
import com.kincai.store.ui.viewpager.page.OrderNoShipmentPage;
import com.kincai.store.ui.viewpager.page.StoreAllProPage;
import com.kincai.store.ui.viewpager.page.StoreJianjiePage;
import com.kincai.store.ui.viewpager.page.StoreDongtaiPage;
import com.kincai.store.ui.viewpager.page.StoreNewProPage;
import com.kincai.store.utils.AnimationUtil;
import com.kincai.store.utils.GsonTools;
import com.kincai.store.utils.JsonUtil;
import com.kincai.store.utils.LoadingDialog;
import com.kincai.store.utils.LogTest;
import com.kincai.store.utils.NetWorkUtil;
import com.kincai.store.utils.Utils;
import com.kincai.store.view.adapter.CartListAdapter;
import com.kincai.store.view.adapter.HomeProListAdapter;
import com.kincai.store.view.adapter.OrderViewPagerAdapter;
import com.kincai.store.view.adapter.StoreViewPagerAdapter;
import com.kincai.store.view.custom.HomeListview;

public class StoreActivity extends BaseActivity {

	private MyHandler mMyHandler;
	public LoadingDialog mLoadingDialog;
	private EditText mSearchEt;
	
	private RelativeLayout mStoreTopRl;

	private ImageView mStoreBgIv, mStoreLogoIv, mStoreXinyongIv;
	private TextView mStoreNameTv, mStoreIdTv, mStoreAttentNumTv,
			mStoreCollectTv, mStroeAllProTv, mStoreNewProTv, mStoreDongtaiTv,
			mStoreJianjieTv;
	
	private String mStoreId;
	private StoreInfo mStoreInfo;
	
		
	
	/** tab下面的线条*/
	private ImageView mTabLineIv;
	
	private ViewPager mStoreViewPager;
	private List<StoreBasePager> mPagers;
	private StoreViewPagerAdapter mStoreViewPagerAdapter;
	
	private int mTabLineWidth;
	
	/** 选中时的字体颜色*/
	private int mSelectColor;
	/** 没选中时的字体颜色*/
	private int mNoSelectColor;
	
	/** tab当前需要显示的哪页*/
	private int mTabCurrentNum;
	
	public static final int ALL_PRO = 0;
	public static final int NEW_PRO = 1;
	public static final int JIAN_JIE = 2;
	public static final int DONGTAI = 3;

	@Override
	public int initContentView() {
		return R.layout.activity_store_layout;
	}

	@Override
	public void initDatas() {
		init();
		initView();
		initData();
		setListener();
	}
	
	private void init() {
		mStoreId = "-1";
		mMyHandler = new MyHandler();
		Intent intent = getIntent();
		if(intent != null) {
			mStoreId = intent.getStringExtra("storeId");
			
			System.out.println("&&&^&*&*&*&((* "+mStoreId);
		}
		mSelectColor = getResources().getColor(R.color.approach_red);
		mNoSelectColor = getResources().getColor(R.color.approach_black);
	}

	@Override
	public void initView() {
		initTabLine();
		super.initView();
		mSearchEt = (EditText) findViewById(R.id.store_search_et);
		mStoreBgIv = (ImageView)findViewById(R.id.store_bg_img_iv);
		mStoreLogoIv = (ImageView)findViewById(R.id.store_logo_iv);
		mStoreXinyongIv = (ImageView)findViewById(R.id.store_xinyong_iv);
		mStoreNameTv = (TextView)findViewById(R.id.store_name_tv);
		mStoreIdTv = (TextView)findViewById(R.id.store_id_tv);
		mStoreAttentNumTv = (TextView)findViewById(R.id.store_attent_num_tv);
		mStoreCollectTv = (TextView)findViewById(R.id.store_collect_tv);
		mStroeAllProTv = (TextView)findViewById(R.id.store_all_pro_tv);
		mStoreNewProTv = (TextView)findViewById(R.id.store_new_pro_tv);
		mStoreDongtaiTv = (TextView)findViewById(R.id.store_dongtai_pro_tv);
		mStoreJianjieTv = (TextView)findViewById(R.id.store_jianjie_tv);
		mStoreTopRl = (RelativeLayout)findViewById(R.id.store_top_rl);
		mStoreViewPager = (ViewPager) findViewById(R.id.store_viewpager);
		
		RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params1.height = (int) Math.ceil(mScreenWidth / 3);
		mStoreTopRl.setLayoutParams(params1);
	}
	
	
	public void setTopRlVisibility(boolean isVisibility) {
		mStoreTopRl.setVisibility(isVisibility ? View.VISIBLE : View.GONE);
	}
	
	private void initPageData() {

		mPagers = new ArrayList<StoreBasePager>();
		mPagers.add(new StoreAllProPage(this, 0, mStoreId));
		mPagers.add(new StoreNewProPage(this, 1, mStoreId));
		mPagers.add(new StoreJianjiePage(this, 2, mStoreId, mStoreInfo));
		mPagers.add(new StoreDongtaiPage(this, 3, mStoreId));
		
		mStoreViewPagerAdapter = new StoreViewPagerAdapter(mPagers);
		mStoreViewPager.setAdapter(mStoreViewPagerAdapter);
		
		//根据从上个页面选中的是哪个 来设置当前类viewpager选中哪页
		mStoreViewPager.setCurrentItem(ALL_PRO);
		setTabTextViewSelectColor(ALL_PRO);
		tabTextviewClickSetCurrentItem(ALL_PRO);
		
		//初始化选中页的数据
		mPagers.get(ALL_PRO).initData();
	}
	
	/**
	 * 初始化tab线条
	 */
	private void initTabLine() {
		mTabLineIv = (ImageView) findViewById(R.id.store_tabline_iv);
		mTabLineWidth = mScreenWidth / 4;
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabLineIv.getLayoutParams();
		lp.width = mTabLineWidth;
		mTabLineIv.setLayoutParams(lp);
	}

	@Override
	public void setListener() {
		super.setListener();
		mStoreViewPager.setOnPageChangeListener(new StorePagerListener());
		
		mStroeAllProTv.setOnClickListener(this);
		mStoreNewProTv.setOnClickListener(this);
		mStoreDongtaiTv.setOnClickListener(this);
		mStoreJianjieTv.setOnClickListener(this);
		
		mStoreCollectTv.setOnClickListener(this);
		
	}
	
	private void initData() {
		if(NetWorkUtil.isNetworkAvailable(this)) {
			
			if(mLoadingDialog == null) {
				mLoadingDialog = new LoadingDialog(this, false);
			}
			mLoadingDialog.dialogShow();
 			CachedThreadPoolUtils.execute(new HttpGetThread(mMyHandler, 
					new StringBuffer().append(Constants.API_URL)
					.append("appGetStoreDataApi.php?storeId=").append(mStoreId).toString()
					, "getStoredata"));
		} else {
			Utils.showToast(this, "网络不给力", 0);
		}
	}
	
	@SuppressLint("HandlerLeak")
	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 2121:
				getResponseStoreData(msg);
				break;
			case 2128:
				getResponseIsCollect(msg);
				break;
			case 2129:
				getResponseAddCollect(msg);
				break;
			case 2130:
				getResponseAllAndNewNum(msg);
				break;
			}

		}
	}
	private void getResponseAllAndNewNum(Message msg) {
		String response = (String) msg.obj;
		if(response != null && JsonUtil.msgJson("code", response).equals("200")) {
			String message = JsonUtil.msgJson("message", response);
			String[] split = message.split(",");
			mStroeAllProTv.setText(new StringBuilder().append(split[0]).append("\n全部商品"));
			mStoreNewProTv.setText(new StringBuilder().append(split[1]).append("\n上新"));
		} else {
			mStroeAllProTv.setText("全部商品");
			mStoreNewProTv.setText("上新");
			
		}
		mStoreJianjieTv.setText("简介"); 
		mStoreDongtaiTv.setText("店铺动态");
			
		
	}
	
	private void getResponseIsCollect(Message msg) {
		String response = (String) msg.obj;
		if(response != null && JsonUtil.msgJson("code", response).equals("402")) {
			//已收藏
			mStoreCollectTv.setBackgroundResource(R.color.hei1);
			mStoreCollectTv.setText("已收藏");
		} else {
			mStoreCollectTv.setBackgroundResource(R.color.hong2);
			mStoreCollectTv.setText("收藏");
		}
			
		
	}
	
	private void getResponseAddCollect(Message msg) {
		String response = (String) msg.obj;
		if(response != null && JsonUtil.msgJson("code", response).equals("402")) {
			//已取消
			mStoreCollectTv.setBackgroundResource(R.color.hong2);
			mStoreCollectTv.setText("收藏");
		} else if(response != null && JsonUtil.msgJson("code", response).equals("200")) {//添加
			mStoreCollectTv.setBackgroundResource(R.color.hei1);
			mStoreCollectTv.setText("已收藏");
		} else {
			
		}
			
		
	}
	
	private void getResponseStoreData(Message msg) {
		if(mLoadingDialog != null && mLoadingDialog.isShow()) {
			mLoadingDialog.dismiss();
		}
		String response = (String) msg.obj;
		if(response != null) {
			System.out.println("-----"+response);
			StoreInfo storeInfo = GsonTools.changeGsonToBean(response, StoreInfo.class);
			
			
			if(storeInfo != null) {
				
				if(200 == storeInfo.getCode()) {
					mStoreInfo = storeInfo;
					System.out.println("mStoreInfo--"+(mStoreInfo == null));
					showStoreData();
					initPageData();
					loadALLAndNewProNum();
					loadStoreIsCollect();
				} else if (400 == storeInfo.getCode()) {
					Utils.showToast(this, "店铺数据加载失败！", 0);
				}
				
			} else {
				Utils.showToast(this, "店铺数据加载失败！", 0);
			}
			
		} else {
			Utils.showToast(this, "店铺数据加载失败！", 0);
		}
		

	}
	
	private void loadALLAndNewProNum() {

		if(NetWorkUtil.isNetworkAvailable(this)) {
			CachedThreadPoolUtils.execute(new HttpGetThread(mMyHandler, new StringBuffer().append(Constants.API_URL)
					.append("appGetStoreAllProAndNewProNumApi.php?storeId=").append(mStoreId).toString(), "getStoreAllAndNew"));
			
		}
	}
	
	
	private void loadStoreIsCollect() {
		if (mSp.getUserIsLogin()) {
			List<UserInfo> mUserInfos = mBaseUserDao.getUserInfoOne(mSp.getUsername());
			if(mUserInfos != null && NetWorkUtil.isNetworkAvailable(this)) {
				CachedThreadPoolUtils.execute(new HttpGetThread(mMyHandler, 
						new StringBuffer().append(Constants.API_URL).append("appGetStoreIsCollectApi.php?userId=")
						.append(mUserInfos.get(0).getUserId()).append("&storeId=")
						.append(mStoreId).toString(), "isStoreCollect"));
			}
		}
	}
	
	private void addOrCancelCollect() {
		if (mSp.getUserIsLogin()) {
			List<UserInfo> mUserInfos = mBaseUserDao.getUserInfoOne(mSp.getUsername());
			if(mUserInfos != null && NetWorkUtil.isNetworkAvailable(this)) {
				CachedThreadPoolUtils.execute(new HttpGetThread(mMyHandler, 
						new StringBuffer().append(Constants.API_URL).append("appStoreCollectApi.php?userId=")
						.append(mUserInfos.get(0).getUserId()).append("&storeId=")
						.append(mStoreId).toString(), "addStoreCollect"));
			}
		}
	}
	
	private void showStoreData() {
		
		
		StoreData storeData = mStoreInfo.getStoreData();
		mImageLoader.loadImage(new StringBuilder()
		.append(Constants.SERVER_URL)
		.append("image_store/logo/")
		.append(storeData.getLogo_url()).toString(), mStoreLogoIv);
		
		mImageLoader.loadImage(new StringBuilder()
		.append(Constants.SERVER_URL)
		.append("image_store/store_bg/")
		.append(storeData.getShop_img_url()).toString(), mStoreBgIv);
		
		
		mStoreNameTv.setText(storeData.getStore_name());
		mStoreIdTv.setText(new StringBuilder().append("店铺号:").append(storeData.getStore_id()));
		mStoreAttentNumTv.setText(new StringBuilder().append(storeData.getAttent_num()).append("\n粉丝数"));
	}

	/**
	 * tab 点击的时候切换page
	 * @param position
	 */
	private void tabTextviewClickSetCurrentItem(int position) {
		if (mStoreInfo == null) {
			return;
		}
		mStoreViewPager.setCurrentItem(position, false);
		
		setTabLineLocation(position, 0);
	}

	
	/**
	 * 设置tab下面的线条位置
	 */
	private void setTabLineLocation(int position, float positionOffset) {
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabLineIv
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
		case ALL_PRO:
			mStroeAllProTv.setTextColor(mSelectColor);
			break;
		case NEW_PRO:
			mStoreNewProTv.setTextColor(mSelectColor);
			break;
		case JIAN_JIE:
			mStoreJianjieTv.setTextColor(mSelectColor);
			break;
		case DONGTAI:
			mStoreDongtaiTv.setTextColor(mSelectColor);
			break;

		}
	}
	
	/**
	 * 初始化全部tab textview字体颜色
	 */
	private void resetTextViewColor() {
		mStroeAllProTv.setTextColor(mNoSelectColor);
		mStoreNewProTv.setTextColor(mNoSelectColor);
		mStoreJianjieTv.setTextColor(mNoSelectColor);
		mStoreDongtaiTv.setTextColor(mNoSelectColor);
	}
	
	/**
	 * 订单viewpager滑动监听
	 */
	class StorePagerListener implements OnPageChangeListener {


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
		case R.id.store_collect_tv:
			
			addOrCancelCollect();
			break;
		case R.id.store_all_pro_tv:
			tabTextviewClickSetCurrentItem(ALL_PRO);
			break;
		case R.id.store_new_pro_tv:
			tabTextviewClickSetCurrentItem(NEW_PRO);
			break;
		case R.id.store_jianjie_tv:
			tabTextviewClickSetCurrentItem(JIAN_JIE);
			break;
		case R.id.store_dongtai_pro_tv:
			tabTextviewClickSetCurrentItem(DONGTAI);
			break;
		}
	}

	@Override
	public void netWork() {

	}

	@Override
	public void noNetWork() {

	}

	@Override
	protected void onPause() {
		super.onPause();
		
		if(mLoadingDialog != null && mLoadingDialog.isShow()) {
			mLoadingDialog.dismiss();
			mLoadingDialog = null;
		}
	}

}
