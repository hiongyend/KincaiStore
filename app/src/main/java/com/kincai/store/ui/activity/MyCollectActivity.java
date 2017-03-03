package com.kincai.store.ui.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout.LayoutParams;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kincai.store.Constants;
import com.kincai.store.R;
import com.kincai.store.bean.ProInfo;
import com.kincai.store.bean.UserInfo;
import com.kincai.store.model.IDialog;
import com.kincai.store.model.IReflashListener;
import com.kincai.store.thread.CachedThreadPoolUtils;
import com.kincai.store.thread.HttpPostThread;
import com.kincai.store.ui.activity.base.BaseSlideActivity;
import com.kincai.store.utils.JsonUtil;
import com.kincai.store.utils.AnimationUtil;
import com.kincai.store.utils.LogTest;
import com.kincai.store.utils.NetWorkUtil;
import com.kincai.store.utils.Utils;
import com.kincai.store.view.adapter.HomeProListAdapter;
import com.kincai.store.view.custom.HomeListview;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 *
 * @author kincai
 * 
 * @description 我的收藏界面
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.ui
 *
 * @time 2015-6-12 下午7:33:32
 *
 */
public class MyCollectActivity extends BaseSlideActivity implements
		IReflashListener, OnItemClickListener, OnItemLongClickListener {

	private static final String TAG = "MyCollectActivity";
	private TextView mTitleTv;
	private LinearLayout mCollectSearchLl;
	private TextView mCollectDefaultTv;
	private ImageView mCollectChangeIv;
	private HomeListview mListview;
	private CheckedTextView mDefaultCtv;
	private CheckedTextView mTimeShortCtv;
	private CheckedTextView mTimeLongCtv;
	private RelativeLayout mDefaultRl;
	private RelativeLayout mFailureRl;
	private LinearLayout mLoadingProgressLl;
	private ProgressBar mProgressBar;

	private LinearLayout mCollectTipLayout;
	private TextView mCollectTipTitleTv;
	private TextView mCollectTipContentTv;
	private Button mNotLoginBtn;

	/** 记录点击哪个筛选按钮 */
	private int index;
	/** 保存点击过的按钮索引 */
	private int currentTabIndex;
	/** 存放筛选按钮 */
	private RelativeLayout[] mTabs;

	private MyHandler mMyHandler;
	private HomeProListAdapter mCollectListAdapter;

	private View customView;
	private PopupWindow popupWindow;
	private int type;

	private static final int TYPE_DEFAULT = 1;
	private static final int TYPE_TIME_SHORT = 2;
	private static final int TYPE_TIME_LONG = 3;
	private static final int TYPE_FAILURE = 4;

	private static final int INDEX_ZERO = 0;
	private static final int INDEX_ONE = 1;

	private List<ProInfo> mSearchProList;
	private List<ProInfo> mNewProList;
	private String userId;
	private String flag;
	
	/** 当前加载页 */
	private static int currentPage = 1;
	private boolean isHavaNextPage;

	
	@Override
	public int initContentView() {
		return R.layout.activity_collect_layout;
	}
	@Override
	public void initDatas() {
		init();
		initView();
		initData();
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
		super.initView();
		mLoadingProgressLl = (LinearLayout) findViewById(R.id.loading_progress_ll);
		mProgressBar = (ProgressBar) findViewById(R.id.loading_progress_p);
		mTitleTv = (TextView) findViewById(R.id.title_tv);
		mCollectSearchLl = (LinearLayout) findViewById(R.id.collect_search_ll);
		mCollectDefaultTv = (TextView) findViewById(R.id.collect_default_tv);
		mCollectChangeIv = (ImageView) findViewById(R.id.popupw_change_iv);
		mListview = (HomeListview) findViewById(R.id.collect_lv);
		mCollectTipLayout = (LinearLayout) findViewById(R.id.collect_tip_ll);
		mCollectTipTitleTv = (TextView) findViewById(R.id.collect_tip_title_tv);
		mCollectTipContentTv = (TextView) findViewById(R.id.collect_tip_content_tv);
		mNotLoginBtn = (Button) findViewById(R.id.collect_not_login_btn);
		mDefaultRl = (RelativeLayout) findViewById(R.id.collect_default_rl);
		mFailureRl = (RelativeLayout) findViewById(R.id.collect_failure_rl_);
		mCollectDefaultTv.setText("默认收藏");

		mCollectChangeIv.setImageResource(R.drawable.follow_accounts_arrow);
		mTitleTv.setText(this.getResources().getString(R.string.my_collect_str));

		mTabs = new RelativeLayout[2];
		mTabs[0] = mDefaultRl;
		mTabs[1] = mFailureRl;
		// mTabs[2] = mHeadPriceBtn;
		// 默认选中第一个
		mTabs[0].setSelected(true);
	}

	/**
	 * 设置监听
	 */
	public void setListener() {
		super.setListener();
		mCollectSearchLl.setOnClickListener(this);
		mDefaultRl.setOnClickListener(this);
		mFailureRl.setOnClickListener(this);
		mNotLoginBtn.setOnClickListener(this);
		mListview.setOnItemClickListener(this);
		mListview.setOnItemLongClickListener(this);

	}

	/**
	 * 初始化数据
	 */
	private void initData() {

		// TODO Auto-generated method stub
		if (mSp.getUserIsLogin()) {
			List<UserInfo> mUserInfos = mBaseUserDao.getUserInfoOne(mSp.getUsername());
			if (null != mUserInfos) {
				userId = String.valueOf(mUserInfos.get(0).getUserId());
				// getImageData();
				if (NetWorkUtil.isNetworkAvailable(MyCollectActivity.this)) {
					// mLoadingDialog.dialogShow();
					mLoadingProgressLl.setVisibility(View.VISIBLE);
					mProgressBar.setVisibility(View.VISIBLE);
					loadData(userId, TYPE_DEFAULT + "", Constants.KINCAI_COLLECT,
							Constants.KINCAI_TYPE_GET, false, 1);
				}
			}
		} else {
			mListview.setVisibility(View.GONE);
			mCollectTipLayout.setVisibility(View.VISIBLE);
			mCollectTipTitleTv.setText("还没登录哦!");
			mCollectTipContentTv.setVisibility(View.GONE);
			mNotLoginBtn.setVisibility(View.VISIBLE);
		}

	}

	/**
	 * 加载数据
	 * 
	 * @param userId
	 * @param pId
	 * @param name
	 * @param type
	 * @param flag
	 */
	private void loadData(String userId, String pId, String name, String type,
			boolean flag, int page) {

		if (NetWorkUtil.isNetworkAvailable(MyCollectActivity.this)) {

			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			parameters.add(new BasicNameValuePair("userId", userId));
			parameters.add(new BasicNameValuePair("pId", pId));
			parameters.add(new BasicNameValuePair("name", name));
			parameters.add(new BasicNameValuePair("type", type));
			parameters.add(new BasicNameValuePair("page", String.valueOf(page)));
			parameters.add(new BasicNameValuePair("pageSize", String.valueOf(Constants.GET_PAGESIZE)));
			if (flag) {
				mLoadingProgressLl.setVisibility(View.VISIBLE);
				mProgressBar.setVisibility(View.VISIBLE);
			}

			CachedThreadPoolUtils.execute(new HttpPostThread(mMyHandler,
					new StringBuffer().append(Constants.API_URL)
					.append("appCollectAndCartApi.php").toString(),
					parameters, "MyCollectActivity"));
		}
	}

	@SuppressLint("HandlerLeak")
	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
			case 2201:

				mListview.onRefreshComplete();
				if (mCollectListAdapter != null) {
					LogTest.LogMsg(TAG, "reflashComplete---");
				}
				
				disposePageLoadData(msg);

				break;
			case 2214:

				getResponseDeleteCollectOne(msg);
				break;
			/*case 2215:
				getResponseImagePath(msg);
				break;*/
			}
		}
	}
	
	/**
	 * 处理判断分页加载数据
	 * 
	 * @param msg
	 */
	private void disposePageLoadData(Message msg) {
		if (null == mSearchProList) {
			mSearchProList = getSearchData(msg);
			
//			ProCommon.getProImagePath(mMainActivityContext, mMyHandler, mAllProList);
			if(null != mSearchProList) {
			}
			
		} else {
			mNewProList = getSearchData(msg);
			if (null != mNewProList && mNewProList.size() > 0) {
				mSearchProList.addAll(mNewProList);
				currentPage++;
				
				LogTest.LogMsg(TAG, "currentPage " + currentPage);
			}
			
//			ProCommon.getProImagePath(mMainActivityContext, mMyHandler,mNewProList);
		}
		
		setSearchData(mSearchProList);

		
//		//加载图片
//		proImageReturn = ProImage.getProImagePath(mMainActivityContext, mMyHandler, mAllProList);
		
	}
	
	/**
	 * 获取商品图片返回数据
	 * @param msg
	 */
//	private void getResponseImagePath(Message msg) {
//		String response = (String) msg.obj;
//
//		if (null != response) {
//			String code = JsonUtil.msgJson("code", response);
//			if(null != code) {
//				if ("200".equals(code)) {
//					mAllProImagePath = JsonUtil.getImgPath("data", response);
//					
//				}
//			}
//			
//		}
//		
//		
////		mAllProImagePath = ProImage
////				.getProImagePath(mAllProImagePath, mSearchProList);
//
//		setSearchData(mSearchProList, mAllProImagePath);
//		
////		if(null != mAllProImagePath) {
////			addProImagePathList(mAllProImagePath, mSearchProImagePath);
////		} else {
////			mAllProImagePath = mSearchProImagePath;
////		}
////		
////		if(isImageFirst && null != msg.obj) {
////			mSp.saveHomeProImagePath(response);
////			isImageFirst = false;
////		}
//	}
	
	
	/**
	 * 获取删除一条收藏返回数据
	 * @param msg
	 */
	private void getResponseDeleteCollectOne(Message msg) {
		String response = (String) msg.obj;
		if (null != response) {
			String code = JsonUtil.msgJson("code", response);
			if (null != code) {
				if ("200".equals(code)) {
					initOb();
					loadData(userId, String.valueOf(mSp.getCollectTab()),
							Constants.KINCAI_COLLECT,
							Constants.KINCAI_TYPE_GET, false,1);
				}
			}
		}

		mTitleTv.setText(getResources().getString(R.string.my_collect_str));

	}

	/**
	 * 显示搜索数据
	 * 
	 * @param datas
	 */
	public void setSearchData(List<ProInfo> datas) {

		mLoadingProgressLl.setVisibility(View.GONE);
		mProgressBar.setVisibility(View.GONE);

		if (datas != null) {
			mListview.setVisibility(View.VISIBLE);
			mCollectTipLayout.setVisibility(View.GONE);
			mCollectTipTitleTv.setText("");
			refreshData(datas);

		} else if ("402".equals(flag)) {
			mListview.setVisibility(View.GONE);
			refreshData(datas);
			setLayoutGoneVisible("还没有收藏宝贝哦！");

		} else if ("403".equals(flag)) {
			mListview.setVisibility(View.GONE);
			refreshData(datas);
			setLayoutGoneVisible("你的收藏夹没有失效宝贝！");
		} else {
			mListview.setVisibility(View.GONE);

			refreshData(datas);
			setLayoutGoneVisible("加载失败！刷新一遍吧！");
		}
	}
	/**
	 * 设置加载数据失败时显示隐藏
	 */
	private void setLayoutGoneVisible(String tip) {
		mCollectTipLayout.setVisibility(View.VISIBLE);
		mCollectTipTitleTv.setText(tip);
		mCollectTipContentTv.setVisibility(View.VISIBLE);
		mCollectTipContentTv.setText("暂时没有数据");
		mNotLoginBtn.setVisibility(View.GONE);
	}

	private void refreshData(List<ProInfo> datas) {
		if (mCollectListAdapter == null) {

			mListview.setOnRefreshListener(this);
			mCollectListAdapter = new HomeProListAdapter(
					MyCollectActivity.this, datas);
			mListview.setAdapter(mCollectListAdapter);

		} else {
			LogTest.LogMsg(TAG, "eeeeeee");
			mCollectListAdapter.onDateChange(datas);
		}
	}

	/**
	 * 获取网络返回的数据
	 * 
	 * @param msg
	 * @return
	 */
	private List<ProInfo> getSearchData(Message msg) {
		String response = (String) msg.obj;

		if (null != response) {
			System.out.println(response);
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
					return searchDataList;
				}

			} else if ("402".equals(code)) {
				flag = "402";
				
				// return new ArrayList<ProInfo>();
			} else if ("403".equals(code)) {
				flag = "403";
				
			}

		}
		
		return null;

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.network_abnormal_top_layout:
			
			startActivity(new Intent(Settings.ACTION_SETTINGS));
			AnimationUtil.startHaveSinkActivityAnimation(this);
			
			
			break;
		case R.id.titlebar_back_iv:
			
			finish();
			AnimationUtil.finishHaveFloatActivityAnimation(this);
			
			
			break;
		case R.id.collect_default_rl:

			if (popupWindow != null && popupWindow.isShowing()) {
				popupWindow.dismiss();

				return;
			} else {
				mCollectChangeIv
						.setImageResource(R.drawable.follow_accounts_up);
				popupWindowMenu();
				popupWindow.setWidth(LayoutParams.MATCH_PARENT);
				popupWindow.setHeight(LayoutParams.MATCH_PARENT);
				popupWindow.showAsDropDown(v, 0, 0);

			}

			type = TYPE_DEFAULT;
			index = INDEX_ZERO;
			
			
			break;
		case R.id.collect_failure_rl_:

			initOb();
			loadData(userId, TYPE_FAILURE + "", Constants.KINCAI_COLLECT,
					Constants.KINCAI_TYPE_GET, true, 1);
			mListview.setVisibility(View.GONE);
			index = INDEX_ONE;
			type = TYPE_FAILURE;
			mSp.saveCollectTab(type);
			
			
			break;
		case R.id.collect_pwindow_default_ctv:
			
			type = TYPE_DEFAULT;
			mSp.saveCollectTab(type);
			setCtvSelect();
			popupWindowDismissAndLoadData(type);
			
			mListview.setVisibility(View.GONE);
			
			mCollectDefaultTv.setText("默认收藏");
			
			break;
		case R.id.collect_pwindow_time_short_to_long_ctv:
			
			type = TYPE_TIME_SHORT;
			mSp.saveCollectTab(type);
			setCtvSelect();
			popupWindowDismissAndLoadData(type);
			mListview.setVisibility(View.GONE);
			
			mCollectDefaultTv.setText("最近收藏");
			
			break;
		case R.id.collect_pwindow_time_long_to_short_ctv:
			
			type = TYPE_TIME_LONG;
			
			mSp.saveCollectTab(type);
			setCtvSelect();
			popupWindowDismissAndLoadData(type);
			
			mListview.setVisibility(View.GONE);
			
			mCollectDefaultTv.setText("历史收藏");
			
			
			break;
		case R.id.collect_search_ll:
			break;
		case R.id.collect_not_login_btn:
			
			startActivity(new Intent(MyCollectActivity.this,
					UserLoginActivity.class));
			finish();
			AnimationUtil.startHaveSinkActivityAnimation(this);
			
			
			break;
		}
		isTabSelect();
	}

	/**
	 * 设置筛选tab选中状态
	 */
	private void isTabSelect() {
		mTabs[currentTabIndex].setSelected(false);
		// 把当前tab设为选中状态
		mTabs[index].setSelected(true);
		currentTabIndex = index;
	}

	/**
	 * 筛选后关闭poppwindow并重新加载数据
	 * @param type
	 */
	private void popupWindowDismissAndLoadData(final int type) {
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (popupWindow != null && popupWindow.isShowing()) {
					popupWindow.dismiss();
					initOb();
					loadData(userId, type + "", Constants.KINCAI_COLLECT,
							Constants.KINCAI_TYPE_GET, true, 1);
				}
			}
		}, 200);
		
	}
	
	private void initOb() {
		mSearchProList = null;
		currentPage = 1;
	}

	/**
	 * 删除一条
	 * 
	 * @param pId
	 */
	private void deleteCollectOne(String pId) {

		if (NetWorkUtil.isNetworkAvailable(MyCollectActivity.this)) {
			mTitleTv.setText(getResources().getString(R.string.delete_now));

			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			parameters.add(new BasicNameValuePair("userId", userId));
			parameters.add(new BasicNameValuePair("name",
					Constants.KINCAI_COLLECT));
			parameters.add(new BasicNameValuePair("type",
					Constants.KINCAI_TYPE_DELETE));
			parameters.add(new BasicNameValuePair("pId", pId));

			// mLoadingProgressLl.setVisibility(View.VISIBLE);
			// mProgressBar.setVisibility(View.VISIBLE);
			// mListview.setVisibility(View.GONE);
			CachedThreadPoolUtils.execute(new HttpPostThread(mMyHandler,
					Constants.API_URL + "appCollectAndCartApi.php",
					parameters, "deleteCollectOne"));
		}
	}

	/**
	 * 右键菜单
	 */
	public void popupWindowMenu() {

		LogTest.LogMsg(TAG, "popupWindow为空？" + (popupWindow == null));

		if (popupWindow == null) {

			customView = View.inflate(MyCollectActivity.this,
					R.layout.popupwindow_collect_layout, null);
			popupWindow = new PopupWindow(customView, 500, 360, true);
			popupWindow.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss() {
					// TODO Auto-generated method stub
					mCollectChangeIv
							.setImageResource(R.drawable.follow_accounts_arrow);
				}
			});
			// popupWindow.setAnimationStyle(R.style.AnimBottom);
			popupWindow.setFocusable(true);// 点击popupWindow外面消失
			customView.setOnTouchListener(new OnTouchListener() {

				@SuppressLint("ClickableViewAccessibility")
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					if (popupWindow != null && popupWindow.isShowing()) {
						popupWindow.dismiss();
						// popupWindow = null;
					}

					return MyCollectActivity.this.onTouchEvent(event);
				}

			});

			mDefaultCtv = (CheckedTextView) customView
					.findViewById(R.id.collect_pwindow_default_ctv);
			mTimeShortCtv = (CheckedTextView) customView
					.findViewById(R.id.collect_pwindow_time_short_to_long_ctv);
			mTimeLongCtv = (CheckedTextView) customView
					.findViewById(R.id.collect_pwindow_time_long_to_short_ctv);

			mDefaultCtv.setOnClickListener(this);
			mTimeShortCtv.setOnClickListener(this);
			mTimeLongCtv.setOnClickListener(this);
		}

		LogTest.LogMsg(TAG, "保存的type " + mSp.getCollectTab());
		setCtvSelect();

	}
	
	/**
	 * 设置ctv选中状态
	 */
	private void setCtvSelect() {
		if (mSp.getCollectTab() == 2) {
			mDefaultCtv.setChecked(false);
			mTimeShortCtv.setChecked(true);
			mTimeLongCtv.setChecked(false);
		} else if (mSp.getCollectTab() == 3) {
			mDefaultCtv.setChecked(false);
			mTimeShortCtv.setChecked(false);
			mTimeLongCtv.setChecked(true);
		} else if (mSp.getCollectTab() == 1) {
			mDefaultCtv.setChecked(true);
			mTimeShortCtv.setChecked(false);
			mTimeLongCtv.setChecked(false);
		} else {
			mDefaultCtv.setChecked(false);
			mTimeShortCtv.setChecked(false);
			mTimeLongCtv.setChecked(false);
		}
	}

	@Override
	public void onReflash() {
		LogTest.LogMsg(TAG, "onReflash ");
		
		if(NetWorkUtil.isNetworkAvailable(MyCollectActivity.this)) {
			initOb();
			loadData(userId, String.valueOf(mSp.getCollectTab()),
					Constants.KINCAI_COLLECT, Constants.KINCAI_TYPE_GET,
					false, 1);
		} else {
			mListview.onRefreshComplete();
		}
		
	}
	
	@Override
	public void onLoadMore() {
		
		if (NetWorkUtil.isNetworkAvailable(this)
				&& isHavaNextPage) {
			loadData(userId, String.valueOf(mSp.getCollectTab()),
					Constants.KINCAI_COLLECT, Constants.KINCAI_TYPE_GET,
					false, currentPage+1);
		} else {
			mListview.onRefreshComplete();
		}	
	}
	
	/**
	 * listview点击
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (null != mSearchProList) {
			Intent intent = new Intent(MyCollectActivity.this,
					ProDetailsActivity.class);
			intent.putExtra("pId", mSearchProList.get(position-1).getId());
			intent.putExtra("storeId", mSearchProList.get(position-1).getStoreId());
			startActivity(intent);
			AnimationUtil.startHaveSinkActivityAnimation(this);
		}

	}

	/**
	 * listview长按
	 */
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		// TODO Auto-generated method stub

		final String pId = String.valueOf(mSearchProList.get(position - 1)
				.getId());
		// LogTest.LogMsg(TAG,
		// "onItemLongClick-position "+position+" iPrice "+mSearchProList.get(position-1).getiPrice());
		Utils.showMiniDialog(MyCollectActivity.this, mScreenWidth, "确定要移除该商品?",
				"取消", "确定", new IDialog() {

					@Override
					public void onDialogClick() {
						// TODO Auto-generated method stub
						deleteCollectOne(pId);
					}
				},true);
		// 如果返回false那么onItemClick仍然会被调用。而且是先调用onItemLongClick，然后调用onItemClick。
		// 如果返回true那么onItemClick就会被吃掉，onItemClick就不会再被调用了。
		// 这里长按弹出对话框后就不需要执行点击事件 所以返回true
		return true;
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
		LogTest.LogMsg(TAG, "MyCollectActivity-onStart");
	}

	@Override
	protected void onResume() {
		super.onResume();
		LogTest.LogMsg(TAG, "MyCollectActivity-onResume");
	}

	@Override
	protected void onPause() {
		super.onPause();
		LogTest.LogMsg(TAG, "MyCollectActivity-onPause");
	}

	@Override
	protected void onDestroy() {
		LogTest.LogMsg(TAG, "MyCollectActivity-onDestroy");
		mSp.saveCollectTab(1);
		super.onDestroy();
		
	}

}
