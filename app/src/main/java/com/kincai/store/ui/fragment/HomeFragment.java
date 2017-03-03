package com.kincai.store.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kincai.store.Constants;
import com.kincai.store.R;
import com.kincai.store.bean.AdvInfo;
import com.kincai.store.bean.HomeGridInfo;
import com.kincai.store.bean.ProInfo;
import com.kincai.store.common.AppUpgrade;
import com.kincai.store.model.IBackTopVisiableListener;
import com.kincai.store.model.IReflashListener;
import com.kincai.store.thread.CachedThreadPoolUtils;
import com.kincai.store.thread.HttpGetThread;
import com.kincai.store.thread.HttpPostThread;
import com.kincai.store.ui.activity.IntegralStoreActivity;
import com.kincai.store.ui.activity.MainActivity;
import com.kincai.store.ui.activity.ProDetailsActivity;
import com.kincai.store.ui.activity.SearchGoodsOnActivity;
import com.kincai.store.ui.activity.TodayUpdateActivity;
import com.kincai.store.ui.activity.WebviewActivity;
import com.kincai.store.ui.activity.base.BaseActivity;
import com.kincai.store.ui.fragment.base.BaseFragment;
import com.kincai.store.utils.AnimationUtil;
import com.kincai.store.utils.DensityUtils;
import com.kincai.store.utils.JsonUtil;
import com.kincai.store.utils.LogTest;
import com.kincai.store.utils.NetWorkUtil;
import com.kincai.store.utils.TimeUtil;
import com.kincai.store.utils.Utils;
import com.kincai.store.utils.bitmap_cache.MyImageLoader;
import com.kincai.store.view.adapter.HomeGridViewAdapter;
import com.kincai.store.view.adapter.HomeProListAdapter;
import com.kincai.store.view.custom.HomeListview;
import com.kincai.store.view.custom.SlideShowView;
import com.kincai.store.view.custom.HomeListview.IHomeTitleListener;
import com.xys.libzxing.zxing.activity.CaptureActivity;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 * 
 * @author kincai
 * 
 * @description 商城首页（使用自己的listview-在用）
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.fragment
 *
 * @time 2015-6-29 上午9:13:10
 *
 */
public class HomeFragment extends BaseFragment implements OnClickListener,
	OnItemClickListener, IReflashListener, IBackTopVisiableListener,IHomeTitleListener {

	private static final String TAG = "HomeFragment";
	private RelativeLayout mNetWorkLayout;
	private RelativeLayout titleRl;
	private LinearLayout mTitleScanRl,mTitleNewRl;
	private EditText mSearchGoodsEt;
	private TextView mRecommendedForYouTv;
	private LinearLayout mProListTipLl;
	private HomeProListAdapter mHomeProListAdapter;
	private HomeListview mHomeProListview;
	private View mHeadView;
	/** 轮播view*/
	private SlideShowView mShowView;
	
	/** 首页tab标签gridview*/
	private GridView mTabGridView;
	private HomeGridViewAdapter mGridViewAdapter;
	private RelativeLayout mLoadingLayout;
	private ProgressBar mLoadingPb;
	private ImageButton mBackToTopIb;
	private ImageView mHomeIntegralstoreIv, mHomeAIv, mHomeBIv;
	private MyHandler mMyHandler;
	
	/** 全部pro存储list*/
	private List<ProInfo> mAllProList;
	/** 加载pro新数据存储list*/
	private List<ProInfo> mNewProList;
	private List<HomeGridInfo> mTabGridInfos;
	/** 记录返回码*/
	private String flag;
	private List<AdvInfo> mIndexAdv;

	private AppUpgrade mAppUpgradeUtil;

	/** 当前加载页 */
	private static int currentPage = 1;
	private boolean isHavaNextPage;
	
	/** 用来判断是否第一次加载pro */
	private boolean isProFirst = true;
	private int height;

	
	@Override
	public View getContentView() {
		return View.inflate(mMainActivityContext, R.layout.fragment_home_layout, null);
	}
	
	@Override
	public void initDatas() {
		init();
		initView();
		initHeadView();
		
		if(mSp == null) return;
		initData();
		loadApkVersionData();
		setListener();
	}


	/**
	 * 初始化
	 */
	private void init() {
		mMyHandler = new MyHandler();
	}

	
	/**
	 * 初始化控件
	 */
	public void initView() {
		mLoadingLayout = (RelativeLayout) mView
				.findViewById(R.id.loading_mini_layout);
		mLoadingPb = (ProgressBar) mView.findViewById(R.id.loading_mini_iv);
		mNetWorkLayout = (RelativeLayout) mView
				.findViewById(R.id.network_abnormal_top_layout);
		mSearchGoodsEt = (EditText) mView
				.findViewById(R.id.home_search_goods_et);
		mTitleScanRl = (LinearLayout) mView.findViewById(R.id.home_scan_title_rl);
		mTitleNewRl = (LinearLayout) mView.findViewById(R.id.home_msg_title_rl);
		mBackToTopIb = (ImageButton) mView.findViewById(R.id.back_to_top_ib);

		mHomeProListview = (HomeListview) mView
				.findViewById(R.id.pro_lv);
		titleRl = (RelativeLayout) mView.findViewById(R.id.home_title_rl);
		mHomeProListview.setOnRefreshListener(this);
		mHomeProListview.setOnHomeTitleListener(this);
		
		titleRl.setBackgroundColor(Color.argb(0, 231, 59, 83));

	}

	/**
	 * 初始化头部view
	 */
	private void initHeadView() {

		mHeadView = View.inflate(mMainActivityContext,
				R.layout.item_home_head_layout, null);
		mShowView = (SlideShowView) mHeadView.findViewById(R.id.slideshowView);
		mTabGridView = (GridView) mHeadView.findViewById(R.id.home_gridView);

		mHomeIntegralstoreIv = (ImageView) mHeadView.findViewById(R.id.home_integral_store_iv);
		mHomeAIv = (ImageView) mHeadView.findViewById(R.id.a);
		mHomeBIv = (ImageView) mHeadView.findViewById(R.id.b);
		mRecommendedForYouTv = (TextView) mHeadView
				.findViewById(R.id.home_recommended_tv);
		mProListTipLl = (LinearLayout) mHeadView
				.findViewById(R.id.home_pro_list_tip);

		mHomeProListview.addHeaderView(mHeadView, null , false);
		
		//根据规定好的轮播图片长宽比例来设置
		RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		height = (int) Math.ceil(mScreenWidth / 2.33);
		params1.height =  height;
		mShowView.setLayoutParams(params1);
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		params.height = (int) Math.ceil((mScreenWidth * 5 / 9 / 2.33));
		params.bottomMargin = DensityUtils.dp2px(mMainActivityContext, 1);
		mHomeAIv.setLayoutParams(params);
		mHomeBIv.setLayoutParams(params);
		

	}
	
	/**
	 * 设置监听
	 */
	public void setListener() {
		mNetWorkLayout.setOnClickListener(this);
		mSearchGoodsEt.setOnClickListener(this);

		mHomeProListview.setOnItemClickListener(this);
		
		mTabGridView.setOnItemClickListener(this);
		mHomeIntegralstoreIv.setOnClickListener(this);
		mBackToTopIb.setOnClickListener(this);
		mHomeProListview.setOnBackToTopVisiableListener(this);
		mHomeProListview.setTiTileHeight(height);
		
		titleRl.setOnClickListener(this);
		
		mTitleNewRl.setOnClickListener(this);
		mTitleScanRl.setOnClickListener(this);
	}
	
	/**
	 * 初始化数据
	 */
	private void initData() {
		
		/**
		 * 先从sp缓存中取数据 显示
		 * 然后再去服务器加载数据
		 */
		
		if(mSp == null) return;
		if(null != mSp.getHomeProData()) {
			List<ProInfo> list = JsonUtil.getPro("data", mSp.getHomeProData());
			if(null != list) {
				mAllProList = list;
				setSearchData(mAllProList);
				mAllProList = null;
				
			}
		} else {
			setSearchData(null);
		}
		
		loadProData(1);
		loadIndexAdvData();
		initTabGridviewData();
		
	}
	
	/**
	 * 加载首页其他的图片
	 */
	private void initHomeImg() {
		
		if(NetWorkUtil.isNetworkAvailable(mMainActivityContext)) {
			
			MyImageLoader imageLoader;
			if(mMainActivityContext instanceof BaseActivity) {
				imageLoader = ((BaseActivity)mMainActivityContext).mImageLoader;
			} else {
				imageLoader = new MyImageLoader(mMainActivityContext);
			}
			
			imageLoader.loadImage(new StringBuffer().append(Constants.SERVER_URL).append("/app/home_img/a.png").toString(), mHomeIntegralstoreIv);
			imageLoader.loadImage(new StringBuffer().append(Constants.SERVER_URL).append("/app/home_img/b.png").toString(), mHomeAIv);
			imageLoader.loadImage(new StringBuffer().append(Constants.SERVER_URL).append("/app/home_img/c.png").toString(), mHomeBIv);
		}
	}
	
	/**
	 * 加载首页 gridview tab数据 
	 */
	private void initTabGridviewData() {
		
		//先判断sp是否有数据 有就显示
		if(null != mSp.getHomeGridTab()) {
		
			List<HomeGridInfo> gridInfos = JsonUtil.getGridInfos(mSp.getHomeGridTab());
			if(gridInfos != null)
			setGridTabData(gridInfos);
		}
		
		loadTabGridviewData();
		
	}
	
	/**
	 * 网络加载首页标签gridview数据
	 */
	private void loadTabGridviewData() {
		
		if (NetWorkUtil.isNetworkAvailable(mMainActivityContext)) {
			CachedThreadPoolUtils.execute(new HttpGetThread(mMyHandler, 
					Constants.GRIDTAB_URL+"griddata.json", "home_grid"));
		}
		
	}
	
	/**
	 * 获取广告数据
	 */
	private void loadIndexAdvData() {
		if(NetWorkUtil.isNetworkAvailable(mMainActivityContext)) {
			CachedThreadPoolUtils.execute(new HttpGetThread(mMyHandler,
					Constants.API_URL + "appIndexCarouselApi.php", "getAdvdata"));
		}
		
	}

	/**
	 * 获取服务器app版本信息
	 */
	private void loadApkVersionData() {
		if (NetWorkUtil.isNetworkAvailable(mMainActivityContext)) {

			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			parameters.add(new BasicNameValuePair("app_id", Constants.APP_ID));
			parameters.add(new BasicNameValuePair("version_code", Integer.toString(Utils.getVersionCode(mMainActivityContext))));
			CachedThreadPoolUtils.execute(new HttpPostThread(
					mMyHandler, Constants.API_URL
							+ "appVersionUpGradeApi.php",
					parameters, "HomeFragmentV"));
		}

	}

	
	/**
	 * 加载商品
	 */
	private void loadProData(int page) {
		
		System.out.println("--page--"+page);
		if (NetWorkUtil.isNetworkAvailable(mMainActivityContext)) {
			mRecommendedForYouTv.setText("加载中...");
			CachedThreadPoolUtils.execute(new HttpGetThread(mMyHandler,
					new StringBuffer().append(Constants.API_URL).append("appAllProApi.php?page=")
					.append(page).append("&pageSize=").append(Constants.GET_PAGESIZE).toString(), "indexProData"));
		} else {
			mLoadingLayout.setVisibility(View.GONE);
			mLoadingPb.setVisibility(View.GONE);
		}
	}

	/**
	 * 显示数据
	 * 
	 * @param datas
	 */
	public void setSearchData(List<ProInfo> datas) {
		mProListTipLl.setVisibility(View.VISIBLE);
		
		
		if (datas != null && datas.size() > 0) {
			mHomeProListview.setVisibility(View.VISIBLE);
			// mSearchAfterLv.setVisibility(View.VISIBLE);
			// mTabs[mSp.getSearchAfterTab()].setSelected(true);

			refreshList(datas);
			
			String time = TimeUtil.getHourAndMin(System.currentTimeMillis());
			mRecommendedForYouTv.setText("为您推荐(" + time + "已更新)");
		} else if ("400".equals(flag)) {
//			refreshList(datas);
			mRecommendedForYouTv.setText("数据加载失败");

		} else {
//			refreshList(datas);
			mRecommendedForYouTv.setText("数据加载失败");

		}
	}

	/**
	 * 刷新界面
	 * 
	 * @param datas
	 * @param Imagedatas
	 */
	private void refreshList(List<ProInfo> datas) {
		if (mHomeProListAdapter == null) {

			// mSearchAfterLv.setInterface(this);
			if (null != mMainActivityContext) {
				mHomeProListAdapter = new HomeProListAdapter(mMainActivityContext,
						datas);
				mHomeProListview.setAdapter(
						mHomeProListAdapter);
			}

		} else {
			LogTest.LogMsg(TAG, "mHomeProListAdapter 不为空时");
			mHomeProListAdapter.onDateChange(datas);
		}
	}
	
	

	@SuppressLint("HandlerLeak")
	class MyHandler extends Handler {
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case 205://检查apk
				mAppUpgradeUtil = new AppUpgrade(msg, mMainActivityContext,
						mScreenWidth, mMyHandler, ((MainActivity)mMainActivityContext).mSp);
				mAppUpgradeUtil.versionJudge(true);
				break;

			case 230://下载apk
				mAppUpgradeUtil.updateApp(msg);
				break;
			case 204:
				//广告数据加载完才完成下拉刷新
				mHomeProListview.onRefreshComplete();
				
				getIndexAdv(msg);
				mShowView.init(mIndexAdv, mMyHandler);
//				initIcon();

				break;
			case 0x210:
				loadAdvAddress(msg);
				break;
			case 203:// 获取商品
				
				mLoadingLayout.setVisibility(View.GONE);
				mLoadingPb.setVisibility(View.GONE);
				mHomeProListview.onRefreshComplete();
				
				disposePageLoadData(msg);

				LogTest.LogMsg(TAG, "mAll为空 " + (null == mAllProList));
				
				break;
			case 2111:
				initHomeImg();
				getResponseTabGridviewData(msg);
				break;
			case 0x212:
				
				break;
			}

		}

	}
	
	/**
	 * 获取请求返回的gridview tab数据
	 * @param msg
	 */
	private void getResponseTabGridviewData(Message msg) {
		if(msg.obj != null) {
			mTabGridInfos = JsonUtil.getGridInfos(msg.obj.toString());
			if(mTabGridInfos != null) {
				setGridTabData(mTabGridInfos);
				//缓存到sp
				mSp.saveHomeGridTab(msg.obj.toString());
				
			}
		}
		
	}
	
	/**
	 * 设置首页gridview tab数据
	 * @param data
	 */
	private void setGridTabData(List<HomeGridInfo> data) {
		LogTest.LogMsg(TAG, "设置首页Gridview数据");
		if(mGridViewAdapter == null) {
			mGridViewAdapter = new HomeGridViewAdapter(mMainActivityContext, mTabGridView, data);
			mTabGridView.setAdapter(mGridViewAdapter);
			AnimationUtil.setGridviewLayoutAnimation(mMainActivityContext, mTabGridView);
		} else {
			mGridViewAdapter.onDateChange(mTabGridView, data);
			AnimationUtil.setGridviewLayoutAnimation(mMainActivityContext, mTabGridView);
		}
	}
	
	/**
	 * 获取商品图片
	 * @param msg
	 */
	/*private void getResponseImagePath(Message msg) {
		String response = (String) msg.obj;

		if (null != response) {
			String code = JsonUtil.msgJson("code", response);
			if(null != code) {
				if ("200".equals(code)) {
					mSearchProImagePath = JsonUtil.getImgPath("data", response);
					
				}
			}
			
		}
		
		if(null != mAllProImagePath) {
			if(mSearchProImagePath != null) {
				mAllProImagePath.addAll(mSearchProImagePath);
			}
			//直接上面addAll就行了 不用一个一个添加 暂时不用这里的
//			ProCommon.addProImagePathList(mAllProImagePath, mSearchProImagePath);
		} else {
			mAllProImagePath = mSearchProImagePath;
		}
		
		if(isImageFirst && null != msg.obj) {
			mSp.saveHomeProImagePath(response);
			isImageFirst = false;
		}
		
		
	}*/
	
	/**
	 * 加载广告
	 * @param msg
	 */
	private void loadAdvAddress(Message msg) {
		if(null != mIndexAdv) {
			String address = mIndexAdv.get(Integer.parseInt(msg.obj.toString())).getAdvUrl();
			startActivity(new Intent(mMainActivityContext, WebviewActivity.class)
				.putExtra(Constants.WEBVIEW_URL, address));
			AnimationUtil.startHaveSinkActivityAnimation((MainActivity)mMainActivityContext);
		}
		
	}


	/**
	 * 处理判断分页加载数据
	 * 
	 * @param msg
	 */
	private void disposePageLoadData(Message msg) {
		if (null == mAllProList) {
			mAllProList = getAllProData(msg);
			
//			ProCommon.getProImagePath(mMainActivityContext, mMyHandler, mAllProList);
			if(null != mAllProList) {
			}
			
		} else {
			mNewProList = getAllProData(msg);
			if (null != mNewProList && mNewProList.size() > 0) {
				mAllProList.addAll(mNewProList);
				currentPage++;
				
				LogTest.LogMsg(TAG, "currentPage " + currentPage);
			}
			
//			ProCommon.getProImagePath(mMainActivityContext, mMyHandler,mNewProList);
		}
		
		setSearchData(mAllProList);

		
//		//加载图片
//		proImageReturn = ProImage.getProImagePath(mMainActivityContext, mMyHandler, mAllProList);
		
	}
	

	/**
	 * 获取商品数据
	 * 
	 * @param msg
	 * @return
	 */
	private List<ProInfo> getAllProData(Message msg) {
		String response = (String) msg.obj;

		if (null != response) {
//			LogTest.LogMsg(TAG, "response " + response);
			String code = JsonUtil.msgJson("code", response);
			String message = JsonUtil.msgJson("message", response);
			if("next".equals(message)) {
				isHavaNextPage = true;
			} else if("nonext".equals(message)){
				isHavaNextPage = false;
			}
			if ("200".equals(code)) {
				List<ProInfo> searchDataList = JsonUtil
						.getPro("data", response);
				if (null != searchDataList) {
					
					if(isProFirst) {
						mSp.saveHomeProData(response);
						isProFirst = false;
					}
					
					
					return searchDataList;
					
					
				}

			} else {
				flag = "400";
			}

		} else {
			flag = "400";
		}

		return null;
	}

	/**
	 * 获取广告数据
	 * 
	 * @param msg
	 */
	private void getIndexAdv(Message msg) {
		String response = (String) msg.obj;
		if (null != response) {
			String code = JsonUtil.msgJson("code", response);
			if ("200".equals(code)) {
				List<AdvInfo> list = JsonUtil.getIndexAdv("data", response);
				if (null != list) {
					mIndexAdv = list;

					LogTest.LogMsg(TAG, "为空？ " + (null == mIndexAdv));
				} else {
					mIndexAdv = null;
				}
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.network_abnormal_top_layout:
			startActivity(new Intent(Settings.ACTION_SETTINGS));
			AnimationUtil.startHaveSinkActivityAnimation((MainActivity)mMainActivityContext);
			break;
		case R.id.home_search_goods_et:
			startActivity(new Intent(mMainActivityContext, SearchGoodsOnActivity.class));
			AnimationUtil.startHaveSinkActivityAnimation((MainActivity)mMainActivityContext);
			break;
		case R.id.home_integral_store_iv:
			startActivity(new Intent(mMainActivityContext, IntegralStoreActivity.class));
			AnimationUtil.startHaveSinkActivityAnimation((MainActivity)mMainActivityContext);
			break;
		case R.id.back_to_top_ib:

//			startActivity(new Intent(mMainActivityContext, ExpandableListViewTest.class));
			mHomeProListview.smoothScrollToPosition(0);//滚动到顶部 有滚动效果
//			mHomeProListview.setSelection(0);//直接跳到顶部 没有滚动效果
			break;
		case R.id.home_scan_title_rl:
			startActivityForResult(new Intent(mMainActivityContext, CaptureActivity.class), 0x423);
			
			break;
		case R.id.home_msg_title_rl:
			
			Utils.showToast(mMainActivityContext, "消息", 0);
			
			break;
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		if(parent == mHomeProListview) {
			
			if(mAllProList != null) {
//				Utils.showToast(mMainActivityContext, "position  "+position +" "+mAllProList.get(position-2).getpName(), Toast.LENGTH_SHORT);
				Intent intent = new Intent(mMainActivityContext, ProDetailsActivity.class);
				intent.putExtra("pId", mAllProList.get(position-2).getId());
				intent.putExtra("storeId", mAllProList.get(position-2).getStoreId());
				startActivity(intent);
				AnimationUtil.startHaveSinkActivityAnimation((MainActivity)mMainActivityContext);
			}
			
		} else if(parent == mTabGridView) {
			
			if(position == 3) {
				startActivity(new Intent(mMainActivityContext, TodayUpdateActivity.class));
				AnimationUtil.startHaveSinkActivityAnimation((MainActivity)mMainActivityContext);
			}
//			Utils.showToast(mMainActivityContext, ""+(position+1), Toast.LENGTH_SHORT);
		}
		
	}


	
	@Override
	public void onReflash() {

		if (NetWorkUtil.isNetworkAvailable(mMainActivityContext)) {
			loadIndexAdvData();
			mAllProList = null;
			currentPage = 1;
			loadProData(1);
			loadTabGridviewData();
			// //停止轮播
			mShowView.stopPlay();
		} else {
			mHomeProListview.onRefreshComplete();
		}
	}
	
	@Override
	public void onLoadMore() {

		if (NetWorkUtil.isNetworkAvailable(mMainActivityContext)
				&& isHavaNextPage) {
			loadProData(currentPage + 1);
			// //停止轮播
			mShowView.stopPlay();
		} else {
			mHomeProListview.onRefreshComplete();
		}
	}

	
	@Override
	public void onInVisible() {
		LogTest.LogMsg(TAG, "回到顶部按钮隐藏");
		mBackToTopIb.setVisibility(View.GONE);
	}
	
	@Override
	public void onVisible() {
		LogTest.LogMsg(TAG, "回到顶部按钮显示");
		mBackToTopIb.setVisibility(View.VISIBLE);
	}
	
	
	@Override
	public void onStart() {
		super.onStart();
		LogTest.LogMsg(TAG, "HomeFragment-onStart");
//		LogTest.LogMsg(TAG, Utils.getuniqueId(mMainActivityContext));

	}

	@Override
	public void onResume() {
		super.onResume();
		LogTest.LogMsg(TAG, "HomeFragment-onResume");

		// mScrollView.smoothScrollTo(0, 0);
		if (!NetWorkUtil.isNetworkAvailable(mMainActivityContext)) {
			// Utils.showToast(mMainActivityContext, "请检查网络连接", Toast.LENGTH_SHORT);
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		LogTest.LogMsg(TAG, "HomeFragment-onPause");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		LogTest.LogMsg(TAG, "HomeFragment-onDestroy");
	}

	public void isNerwoking() {
		if (!NetWorkUtil.isNetworkAvailable(mMainActivityContext)) {
			Utils.showToast(mMainActivityContext, "网络连接失败", Toast.LENGTH_SHORT);
		}
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);

		if (!hidden) {
			LogTest.LogMsg(TAG, "HomeFragment-onHiddenChanged");
			onResume();
			isNerwoking();
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
	public void setAlpha(int value) {
		int v = (int)(value*((double)(255/ (double)height)));
		System.out.println("-value--"+value+" "+height+"   "+(double)(255/ (double)height));

//		titleRl.setBackgroundColor(Color.argb(v, 231, 59, 83));
		if(v >= 230) {
			titleRl.setBackgroundColor(Color.argb(255, 231, 59, 83));
		} else if(v <= 20){
			titleRl.setBackgroundColor(Color.argb(0, 231, 59, 83));
		} else {
			titleRl.setBackgroundColor(Color.argb(v, 231, 59, 83));
		}
	}

	@Override
	public void setVisibility(boolean isVisibility) {
		titleRl.setVisibility(isVisibility ? View.VISIBLE : View.GONE);
	}
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if(data == null) return;
		
		switch (requestCode) {
		case 0x423:
			
			int width = data.getIntExtra("width", 0);
			int height = data.getIntExtra("height", 0);
			String result = data.getStringExtra("result");
			
			Utils.showToast(mMainActivityContext, result, 0);
			
			break;
		}
	}

}
