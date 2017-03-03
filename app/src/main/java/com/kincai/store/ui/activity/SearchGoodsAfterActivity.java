package com.kincai.store.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.test.suitebuilder.annotation.Suppress;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.kincai.store.Constants;
import com.kincai.store.KincaiApplication;
import com.kincai.store.R;
import com.kincai.store.bean.ProImagePathInfo;
import com.kincai.store.bean.ProInfo;
import com.kincai.store.bean.SearchHistory;
import com.kincai.store.bean.SearchItem;
import com.kincai.store.common.IflytekVoice;
import com.kincai.store.common.ProCommon;
import com.kincai.store.common.IflytekVoice.VoiceRecognizerDialogListener;
import com.kincai.store.common.IflytekVoice.VoiceRecognizerListener;
import com.kincai.store.db.SearchHistoryDao;
import com.kincai.store.db.SearchItemDao;
import com.kincai.store.model.IBackTopVisiableListener;
import com.kincai.store.model.IEditTextVoiceListener;
import com.kincai.store.model.IReflashListener;
import com.kincai.store.thread.CachedThreadPoolUtils;
import com.kincai.store.thread.HttpGetThread;
import com.kincai.store.ui.activity.base.BaseSlideActivity;
import com.kincai.store.utils.AnimationUtil;
import com.kincai.store.utils.JsonUtil;
import com.kincai.store.utils.LoadingDialog;
import com.kincai.store.utils.LogTest;
import com.kincai.store.utils.NetWorkUtil;
import com.kincai.store.utils.SPStorageUtil;
import com.kincai.store.utils.Utils;
import com.kincai.store.view.adapter.HomeProListAdapter;
import com.kincai.store.view.adapter.SearchHistoryListAdapter;
import com.kincai.store.view.adapter.SearchItemListAdapter;
import com.kincai.store.view.custom.CleanableAndVoiceEditTextView;
import com.kincai.store.view.custom.HomeListview;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 * 
 * @author kincai
 * 
 * @description 搜索商品结果（使用自己的listview）
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.ui
 *
 * @time 2015-6-28 下午9:13:11
 *
 */

@SuppressWarnings("unused")
public class SearchGoodsAfterActivity extends BaseSlideActivity implements
		 OnItemClickListener, IReflashListener, IEditTextVoiceListener, IBackTopVisiableListener {

	private static final String TAG = "SearchGoodsAfterActivity";

	/** 在搜索时候的Title Layout */
	private RelativeLayout mTitlebarRl;

	/** 搜索时候的 搜索按钮 */
	private Button mSearchBtn;
	/** 搜索时候的 输入框 */
	private CleanableAndVoiceEditTextView mSearchContentEt;
	/** 搜索时候的 显示提供搜索条目的列表 */
	private ListView mSearchBeforeLv;
	/** 搜索时候的 历史记录列表 */
	private ListView mSearchOnLv;

	/** 实时监听网路状态广播接收者 */

	/** 历史记录列表的adapter */
	private SearchHistoryListAdapter mHistoryAdapter;
	/** 提供搜索条目列表的adapter */
	private SearchItemListAdapter mSearchItemAdapter;
	/** EditText实时监听 */
	private MyTextWatcher mMyTextWatcher;
	/** 清除历史记录 */
	private Button mClearSearchHistoryBtn;

	/** 展示搜索后商品的列表 */
	private HomeListview mSearchAfterLv;

	/** 展示商品ListView的head 的Title Layout */

	private RelativeLayout mTitlebarHeadRl;

	/** 展示商品ListView的head 的返回iv */
	private ImageView mBackHeadIv;
	/** 展示商品ListView的head 的搜索按钮 */
	private Button mSearchHeadBtn;
	/** 展示商品ListView的head 的输入框 */
	private EditText mSearchContentHeadEt;

	/** 展示商品ListView的head 的筛选-推荐 */
	private TextView mHeadRecommendTv;
	/** 展示商品ListView的head 的筛选-销量 */
	private TextView mHeadSalesVolumeTv;
	/** 展示商品ListView的head 的筛选-价格 */
	// private Button mHeadPriceBtn;
	/** 展示商品ListView的head 的筛选-评价 */
	private TextView mHeadEvaluateTv;

	private TextView mHeadFiltrateTv;

	private RelativeLayout mHeadDefaultRl;
	private RelativeLayout mHeadSalesVolumeRl;
	private RelativeLayout mHeadEvaluateRl;
	private RelativeLayout mHeadFiltrateRl;
	private ImageView mChangeIv;
	private ImageButton mBackToTopIb;

	/** 记录点击哪个筛选按钮 */
	private int index;
	/** 保存点击过的按钮索引 */
	private int currentTabIndex;
	/** 存放四个筛选按钮 */
	private RelativeLayout[] mTabs;

	/** 提供搜索的列表 */
	private SearchItemDao mItemDb;

	/** 搜索历史记录数据库 */
	private SearchHistoryDao mSearchHistoryDb;
	/** 历史和条目的listView item监听 */
	private listItemListener mItemListener;
	private MyHandler mMyHandler;
	private List<ProInfo> mSearchProList;
	private List<ProInfo> mNewProList;

	private HomeProListAdapter mSearchProAdapter;

	/** 加载对话框 */

	private LoadingDialog mLoadingDialog;

	private String mSearchStr;
	private PopupWindow popupWindow;
	private ImageView home_down_menu;
	private View customView;

	private CheckedTextView mRecommendCtv;
	private CheckedTextView mPriceLowCtv;
	private CheckedTextView mPriceHeightCtv;

	private LinearLayout mLoadingProgressLl;
	private ProgressBar mProgressBar;

	private int type;
	/** 当前加载页 */
	private static int currentPage = 1;
	private boolean isHavaNextPage;
	
	
	private static final int TYPE_DEFAULT = 1;
	private static final int TYPE_SALES = 2;
	private static final int TYPE_PRICE_LOW = 3;
	private static final int TYPE_EVALUATE = 4;
	private static final int TYPE_PRICE_HEIGHT = 5;
	private static final int TYPE_FILTRATE = 6;

	private static final int INDEX_ZERO = 0;
	private static final int INDEX_ONE = 1;
	private static final int INDEX_TWO = 2;
	private static final int INDEX_THREE = 3;
	
	
	// 科大讯飞语音听写对象
	private SpeechRecognizer mIat;
	// 语音听写UI
	private RecognizerDialog mIatDialog;
	private IflytekVoice mIflytekVoice;
	
	private VoiceRecognizerListener mVoiceRecognizerListener;
	private VoiceRecognizerDialogListener mVoiceRecognizerDialogListener;

	
	@Override
	public int initContentView() {
		return R.layout.activity_search_after_layout;
	}
	@Override
	public void initDatas() {
		init();
		initView();
		initData();
		setHistoryData();
		initHeadView();
		setListener();
	}
	

	/**
	 * 初始化
	 */
	private void init() {
		
		// 初始化科大讯飞语音初始化监听(父类实现)
		setInitListenet();
		// 初始化语音听写
		mIat = SpeechRecognizer.createRecognizer(this, mInitListener);
		mIatDialog = new RecognizerDialog(this, mInitListener);
		mIflytekVoice = new IflytekVoice(this, mIat);
		mMyHandler = new MyHandler();
		mLoadingDialog = new LoadingDialog(SearchGoodsAfterActivity.this, false);
		mSearchHistoryDb = new SearchHistoryDao(SearchGoodsAfterActivity.this);
		mItemDb = new SearchItemDao(SearchGoodsAfterActivity.this);
		mMyTextWatcher = new MyTextWatcher();
		mItemListener = new listItemListener();

	}

	private void initData() {
		loadingData(getIntentData(), TYPE_DEFAULT + "", 1,true);
	}

	/**
	 * 初始化控件
	 */
	public void initView() {
		super.initView();
		mLoadingProgressLl = (LinearLayout) findViewById(R.id.loading_progress_ll);
		mProgressBar = (ProgressBar) findViewById(R.id.loading_progress_p);
		mTitlebarRl = (RelativeLayout) findViewById(R.id.search_title_rl);
		mSearchBtn = (Button) findViewById(R.id.search_btn);
		mSearchContentEt = (CleanableAndVoiceEditTextView) findViewById(R.id.search_goods_et);
		mSearchBeforeLv = (ListView) findViewById(R.id.search_before_lv);
		mSearchOnLv = (ListView) findViewById(R.id.search_on_lv);
		mSearchAfterLv = (HomeListview) findViewById(R.id.search_after_lv);
		mTitlebarRl.setVisibility(View.GONE);
		mBackToTopIb = (ImageButton) findViewById(R.id.back_to_top_ib);
	}

	/**
	 * 设置监听
	 */
	public void setListener() {
		
		super.setListener();
		mSearchBtn.setOnClickListener(this);
		mSearchContentEt.addTextChangedListener(mMyTextWatcher);
		mSearchContentEt.setEditTextVoiceListener(this);
		// mSearchContentHeadEt.addTextChangedListener(mMyTextWatcher);
		mSearchOnLv.setOnItemClickListener(mItemListener);
		mSearchBeforeLv.setOnItemClickListener(mItemListener);
		mSearchAfterLv.setOnRefreshListener(this);
		mBackToTopIb.setOnClickListener(this);
		mSearchAfterLv.setOnBackToTopVisiableListener(this);
		
		
		
		// 下面是head的
		mBackHeadIv.setOnClickListener(this);

		mSearchContentHeadEt.setOnClickListener(this);

		mHeadDefaultRl.setOnClickListener(this);
		mHeadSalesVolumeRl.setOnClickListener(this);
		mHeadEvaluateRl.setOnClickListener(this);
		mHeadFiltrateRl.setOnClickListener(this);
		mSearchAfterLv.setOnItemClickListener(this);
		
		
	}

	/**
	 * 获取传递过来的搜索内容
	 * 
	 * @return
	 */
	private String getIntentData() {

		Intent intent = SearchGoodsAfterActivity.this.getIntent();
		if(null != intent) {
			return intent.getStringExtra("searchContent");
		}
		
		return "";
	}

	/**
	 * 初始化搜索结果listview头部view
	 */
	private void initHeadView() {

		// 搜索结果显示的界面
		// 头部
		View headView = View.inflate(SearchGoodsAfterActivity.this,R.layout.item_search_result_listview_headlayout_layout,
						null);
		mBackHeadIv = (ImageView) headView
				.findViewById(R.id.titlebar_head_back_iv);
		mTitlebarHeadRl = (RelativeLayout) headView
				.findViewById(R.id.search_after_head_title_rl);
		mSearchHeadBtn = (Button) headView.findViewById(R.id.search_head_btn);
		mHeadRecommendTv = (TextView) headView
				.findViewById(R.id.search_result_default_tv);
		mHeadSalesVolumeTv = (TextView) headView
				.findViewById(R.id.search_result_sales_volume_head_tv);
		mSearchContentHeadEt = (EditText) headView
				.findViewById(R.id.search_goods_head_et);
		// mHeadPriceBtn = (Button) headView
		// .findViewById(R.id.search_result_price_head_btn);
		mHeadEvaluateTv = (TextView) headView
				.findViewById(R.id.search_result_evaluate_head_tv);
		mHeadFiltrateTv = (TextView) headView
				.findViewById(R.id.search_result_filtrate_head_tv);

		mHeadDefaultRl = (RelativeLayout) headView
				.findViewById(R.id.search_result_default_rl);
		mHeadSalesVolumeRl = (RelativeLayout) headView
				.findViewById(R.id.search_result_sales_rl);
		mHeadEvaluateRl = (RelativeLayout) headView
				.findViewById(R.id.search_result_evaluate_rl);
		mHeadFiltrateRl = (RelativeLayout) headView
				.findViewById(R.id.search_result_filtrate_rl);
		mChangeIv = (ImageView) headView.findViewById(R.id.popupw_change_iv);
		mChangeIv.setImageResource(R.drawable.follow_accounts_arrow);
		mHeadRecommendTv.setText("默认");

		mSearchContentHeadEt.setText(getIntentData());
		mTabs = new RelativeLayout[4];
		mTabs[0] = mHeadDefaultRl;
		mTabs[1] = mHeadSalesVolumeRl;

		mTabs[2] = mHeadEvaluateRl;
		mTabs[3] = mHeadFiltrateRl;

		// 默认选中第一个
		mTabs[0].setSelected(true);
		mSearchAfterLv.addHeaderView(headView, null, false);
		mSearchAfterLv.setVisibility(View.VISIBLE);
		mSearchStr = getIntentData();

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if(parent == mSearchAfterLv) {
			
			if(null != mSearchProList) {
				LogTest.LogMsg(TAG, mSearchProList.get(position - 2).getiPrice());
				Intent intent = new Intent(SearchGoodsAfterActivity.this, ProDetailsActivity.class);
				intent.putExtra("pId", mSearchProList.get(position-2).getId());
				intent.putExtra("storeId", mSearchProList.get(position-2).getStoreId());
				startActivity(intent);
				AnimationUtil.startHaveSinkActivityAnimation(this);
			}
		}
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
			mSearchAfterLv.setVisibility(View.VISIBLE);

			if (mSearchProAdapter == null) {

				mSearchProAdapter = new HomeProListAdapter(
						SearchGoodsAfterActivity.this, datas);
				mSearchAfterLv.setAdapter(mSearchProAdapter);
			} else {
				LogTest.LogMsg(TAG, "eeeeeee");
				mSearchProAdapter.onDateChange(datas);
			}
		} else {
//			startActivity(new Intent(SearchGoodsAfterActivity.this,
//					SearchGoodsOnActivity.class));
//			finish();
//			AnimationUtil.startHaveSinkActivityAnimation(this);
		}
	}

	/**
	 * 网络请求数据
	 */
	private void loadingData(String itemContent, String type, int page, boolean isLoadDialog) {

		if (!"".equals(itemContent)) {
			if (NetWorkUtil.isNetworkAvailable(SearchGoodsAfterActivity.this)) {
				mSearchOnLv.setVisibility(View.GONE);
				mSearchBeforeLv.setVisibility(View.GONE);
				mTitlebarRl.setVisibility(View.GONE);
				mSearchAfterLv.setVisibility(View.GONE);

				if(isLoadDialog) {
					mLoadingProgressLl.setVisibility(View.VISIBLE);
					mProgressBar.setVisibility(View.VISIBLE);
				} else {
					mSearchAfterLv.setVisibility(View.VISIBLE);
				}
				

				CachedThreadPoolUtils.execute(new HttpGetThread(mMyHandler,
						new StringBuffer().append(Constants.API_URL).append("appSearchProApi.php?proName=")
								.append(itemContent).append("&type=").append(type).append("&page=")
								.append(page).append("&pageSize=").append(Constants.GET_PAGESIZE).toString(),
						"SearchGoodsAfterActivity"));
			}
		} else {
			// 为空的时候 我只要刷新一遍就好

		}
	}

	@SuppressLint("HandlerLeak")
	class MyHandler extends Handler {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 2100:
				break;
			case 2101:
				mSearchAfterLv.onRefreshComplete();
				disposePageLoadData(msg);
				
				
				break;
			case 0x112:
				List<SearchHistory> list = new ArrayList<SearchHistory>();
				mHistoryAdapter.notifyRefresh(list);
				mClearSearchHistoryBtn.setEnabled(false);
				mClearSearchHistoryBtn.setBackgroundResource(R.drawable.dialog_btn_left_bg_down);
//				mClearSearchHistoryBtn.setBackground(getResources()
//						.getDrawable(R.drawable.dialog_btn_left_bg_down));
				mClearSearchHistoryBtn.setText("清除搜索历史");
				break;
			case 0x122:
				mClearSearchHistoryBtn.setText("清除搜索历史");
				break;
			/*case 2215:
//				getResponseImagePath(msg);
				break;*/

			}
		};
	}
	
	/**
	 * 获取返回的商品图片路径
	 * @param msg
	 */
//	private void getResponseImagePath(Message msg) {
//		String response = (String) msg.obj;
//
//		if (null != response) {
//			String code = JsonUtil.msgJson("code", response);
//			if(null != code) {
//				if ("200".equals(code)) {
//					mSearchProImagePath = JsonUtil.getImgPath("data", response);
//					
//				}
//			}
//			
//		}
//		if(null != mAllProImagePath) {
//			if(mSearchProImagePath != null && mSearchProImagePath.size() > 0) {
//				mAllProImagePath.addAll(mSearchProImagePath);
//			}
//			
//			//直接上面addAll就行了 不用一个一个添加 暂时不用这里的
////			ProCommon.addProImagePathList(mAllProImagePath, mSearchProImagePath);
//		} else {
//			mAllProImagePath = mSearchProImagePath;
//		}
//		
//		
//		
////		mAllProImagePath = ProImage
////				.getProImagePath(mAllProImagePath, mSearchProList);
//
//		setSearchData(mSearchProList, mAllProImagePath);
//		
//	}
	
	/**
	 * 处理判断分页加载数据
	 * 
	 * @param msg
	 */
	private void disposePageLoadData(Message msg) {
		if (null == mSearchProList) {
			mSearchProList = getSearchData(msg);
			
//			ProCommon.getProImagePath(SearchGoodsAfterActivity.this, mMyHandler, mSearchProList);
			if(null != mSearchProList) {
			}
			
		} else {
			mNewProList = getSearchData(msg);
			if (null != mNewProList && mNewProList.size() > 0) {
				mSearchProList.addAll(mNewProList);
				currentPage++;
				LogTest.LogMsg(TAG, "currentPage " + currentPage);
			}
			
			//直接上面addAll就行了 不用一个一个添加 暂时不用这里的
//			mSearchProList = ProCommon.addProList(mSearchProList, mNewProList);
//			ProCommon.getProImagePath(SearchGoodsAfterActivity.this, mMyHandler,mNewProList);
		}
		
		setSearchData(mSearchProList);
//		//加载图片
//		proImageReturn = ProImage.getProImagePath(getActivity(), mMyHandler, mAllProList);
		
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
			}

		}

		return null;

	}

	/**
	 * 保存搜索记录
	 * 
	 * @param content
	 */
	private void saveSearchHistory(String content) {

		if (!"".equals(content)) {
			List<SearchHistory> list = new ArrayList<SearchHistory>();
			SearchHistory onHistory = new SearchHistory();
			// sHistory.setContent(mSearchContentEt.getText().toString());
			// list.add(sHistory);
			List<SearchHistory> dbList = mSearchHistoryDb.getSearchInfo();
			if (null != dbList) {
				boolean flag = true;
				for (SearchHistory history : dbList) {

					if (history.getContent().equals(content)) {
						flag = false;
						break;
					}
				}
				if (flag) {
					onHistory.setContent(content);
					list.add(onHistory);
					mSearchHistoryDb.saveSearchInfo(list);
				}

			} else {
				onHistory.setContent(content);
				list.add(onHistory);
				mSearchHistoryDb.saveSearchInfo(list);
			}

		}
	}

	/**
	 * 初始化历史搜索数据
	 */
	private void setHistoryData() {

		// mSearchOnLv.setVisibility(View.VISIBLE);
		// 头部
		View headView = View.inflate(SearchGoodsAfterActivity.this,
				R.layout.item_search_on_listview_headlayout, null);

		// 底部
		View footView =View.inflate(SearchGoodsAfterActivity.this,
				R.layout.item_search_on_listview_footlayout, null);

		mClearSearchHistoryBtn = (Button) footView
				.findViewById(R.id.search_clear_history_bt);
		mClearSearchHistoryBtn.setOnClickListener(this);

		mSearchOnLv.addHeaderView(headView, null, false);
		mSearchOnLv.addFooterView(footView, null, false);

	}

	/**
	 * 显示历史搜索记录
	 */
	private void showHistoryList() {
		mSearchOnLv.setVisibility(View.VISIBLE);
		if (mSearchHistoryDb.getSearchInfo() != null) {
			mClearSearchHistoryBtn.setEnabled(true);
			mClearSearchHistoryBtn.setBackgroundResource(R.drawable.dialog_btn_right_bg);
			/*mClearSearchHistoryBtn.setBackground(getResources().getDrawable(
					R.drawable.dialog_btn_right_bg));*/
		}
		List<SearchHistory> list = mSearchHistoryDb.getSearchInfo();
		mHistoryAdapter = new SearchHistoryListAdapter(
				SearchGoodsAfterActivity.this, list,
				R.layout.item_search_listview_layout);
		mSearchOnLv.setAdapter(mHistoryAdapter);
	}

	/**
	 * 搜索时列表item事件
	 * 
	 * @author kincai
	 * 
	 */
	class listItemListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			LogTest.LogMsg(TAG, "position " + position);

			String itemContent = ((TextView) view
					.findViewById(R.id.search_item_tv)).getText().toString();
			LogTest.LogMsg(TAG, "点击获取的内容 " + itemContent);

			if (NetWorkUtil.isNetworkAvailable(SearchGoodsAfterActivity.this)) {
				mSearchContentEt.setText(itemContent);
				mSearchContentHeadEt.setText(itemContent);
				saveSearchHistory(itemContent);
			}

			mSearchStr = itemContent;

			mSearchProList = null;
			currentPage = 1;
			loadingData(itemContent, String.valueOf(mSp.getSearchAfterTab()), 1, true);

		}

	}

	/**
	 * edittext实时监听
	 * 
	 */
	class MyTextWatcher implements TextWatcher {
//		private CharSequence temp;

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
//			temp = s;
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		@Override
		public void afterTextChanged(Editable s) {

			if (mSearchContentEt.getText().toString().length() > 0) {

				LogTest.LogMsg(TAG, "从历史listview进入模糊查询listview");
				LogTest.LogMsg(TAG, "输入框内容长度 "
						+ mSearchContentEt.getText().toString().length());

				mSearchOnLv.setVisibility(View.GONE);
				mSearchBeforeLv.setVisibility(View.VISIBLE);
				setQueryData(mSearchContentEt.getText().toString());
			}

			if (mSearchContentEt.getText().toString().equals("")) {
				showHistoryList();
				mSearchOnLv.setVisibility(View.VISIBLE);
				mSearchBeforeLv.setVisibility(View.GONE);
			}

		}
	};

	/**
	 * 设置模糊查询列表显示数据
	 */
	private void setQueryData(String item) {

		List<SearchItem> dbList = mItemDb.getFuzzyQueryInfo(item);

		mSearchItemAdapter = new SearchItemListAdapter(
				SearchGoodsAfterActivity.this, dbList,
				R.layout.item_search_listview_layout);
		mSearchBeforeLv.setAdapter(mSearchItemAdapter);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.titlebar_back_iv:
			finish();
			AnimationUtil.finishHaveFloatActivityAnimation(this);
			break;
		case R.id.network_abnormal_top_layout:
			startActivity(new Intent(Settings.ACTION_SETTINGS));
			AnimationUtil.startHaveSinkActivityAnimation(this);
			break;
		case R.id.search_btn:
			search(mSearchContentEt.getText().toString().trim());
			break;
		case R.id.search_clear_history_bt:
			clearHistory();
			break;
		case R.id.titlebar_head_back_iv:
			finish();
			AnimationUtil.finishHaveFloatActivityAnimation(this);
			break;
		case R.id.back_to_top_ib:
			
			mSearchAfterLv.smoothScrollToPosition(0);//滚动到顶部 有滚动效果
//			mSearchAfterLv.setSelection(0);//直接跳到顶部 没有滚动效果
			
			break;
		case R.id.search_result_default_rl:
			LogTest.LogMsg(TAG, "e");

			if (popupWindow != null && popupWindow.isShowing()) {
				popupWindow.dismiss();
				return;
			} else {
				mChangeIv.setImageResource(R.drawable.follow_accounts_up);
				popupWindowMenu();
				popupWindow.showAsDropDown(v, 0, 0);

			}

			// loadingData(mSearchStr, "1");
			type = TYPE_DEFAULT;
			// mSp.saveSearchAfterTab(type);
			index = INDEX_ZERO;
			break;
		case R.id.search_result_sales_rl:

			LogTest.LogMsg(TAG, "e");
			initOb();
			loadingData(mSearchStr, TYPE_SALES + "",1,true);
			type = TYPE_SALES;
			mSp.saveSearchAfterTab(type);
			index = INDEX_ONE;

			break;
		case R.id.search_result_evaluate_rl:

			initOb();
			loadingData(mSearchStr, TYPE_EVALUATE + "",1,true);
			type = TYPE_EVALUATE;
			mSp.saveSearchAfterTab(type);
			index = INDEX_TWO;

			break;
		case R.id.search_result_filtrate_rl:
			initOb();
			loadingData(mSearchStr, TYPE_FILTRATE + "",1,true);
			type = TYPE_FILTRATE;
			mSp.saveSearchAfterTab(type);
			index = INDEX_THREE;

			break;
		case R.id.pwindow_recommend_ctv:
			initOb();
			type = TYPE_DEFAULT;
			mSp.saveSearchAfterTab(type);
			setCtvSelect();
			popupWindowDismissAndLoadData(type);
			mHeadRecommendTv.setText("默认");
			break;
		case R.id.pwindow_price_low_to_high_ctv:

			// mPriceLowCtv.toggle();
			initOb();
			type = TYPE_PRICE_LOW;
			mSp.saveSearchAfterTab(type);
			setCtvSelect();
			popupWindowDismissAndLoadData(type);
			mHeadRecommendTv.setText("价格升");

			break;
		case R.id.pwindow_price_high_to_low_ctv:

			// mPriceHeightCtv.toggle();
			initOb();
			type = TYPE_PRICE_HEIGHT;
			mSp.saveSearchAfterTab(type);
			setCtvSelect();
			popupWindowDismissAndLoadData(type);
			mHeadRecommendTv.setText("价格降");
			break;
		case R.id.search_goods_head_et:

			// mSp.saveSearchAfterTab(mSp)
			mSearchAfterLv.setVisibility(View.GONE);
			mSearchContentEt.setText(mSearchContentHeadEt.getText().toString());

			LogTest.LogMsg(TAG, "mSearchAfterLv.setVisibility(View.GONE)");
			mTitlebarRl.setVisibility(View.VISIBLE);
			LogTest.LogMsg(TAG, "mTitlebarRl.setVisibility(View.VISIBLE)");

			// 当搜索框没有值的时候 才显示历史搜索 否则显示当前输入框内容锁模糊查询的内容
			if (mSearchContentEt.getText().toString().length() == 0) {
				showHistoryList();
			}

			break;
		}

		isTabSelect();

	}
	
	
	private void initOb() {
		mSearchProList = null;
		currentPage = 1;
	}

	/**
	 * 筛选后关闭poppwindow并重新加载数据
	 * @param type
	 */
	private void popupWindowDismissAndLoadData(final int type) {
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				if (popupWindow != null && popupWindow.isShowing()) {
					popupWindow.dismiss();
					
					loadingData(mSearchStr, type + "",1,true);
				}
			}
		}, 200);
		
	}

	/**
	 * 清除搜索历史
	 */
	private void clearHistory() {
		mClearSearchHistoryBtn.setText("正在清除...");
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				int row = mSearchHistoryDb.deleteSearchInfos();
				Message message = Message.obtain();

				if (row > 0) {
					message.what = 0x112;
				} else {
					message.what = 0x122;
				}

				mMyHandler.sendMessage(message);

			}
		}, 500);

	}

	/**
	 * 搜索
	 * 
	 * @param content
	 */
	private void search(String content) {
		if (!"".equals(content)) {
			mSearchStr = content;
			// 按搜索时把记录添加到数据库
			if (NetWorkUtil.isNetworkAvailable(SearchGoodsAfterActivity.this)) {
				mSearchContentEt.setText(content);
				mSearchContentHeadEt.setText(content);
				saveSearchHistory(content);
			}

			LogTest.LogMsg(TAG, "mSearchContentEt.getText().toString() "
					+ content);
			// mSearchContentHeadEt.setText(mSearchContentEt.getText().toString());
			initOb();
			loadingData(content, String.valueOf(mSp.getSearchAfterTab()),1,true);
		}
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
	 * 右键菜单
	 */
	public void popupWindowMenu() {

		LogTest.LogMsg(TAG, "popupWindow为空？" + (popupWindow == null));

		if (popupWindow == null) {

			customView = View.inflate(SearchGoodsAfterActivity.this,
					R.layout.popupwindow_search_menu, null);
			popupWindow = new PopupWindow(customView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
			popupWindow.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss() {
					mChangeIv.setImageResource(R.drawable.follow_accounts_arrow);
				}
			});
			// popupWindow.setAnimationStyle(R.style.AnimBottom);
			popupWindow.setFocusable(true);// 点击popupWindow外面消失
			customView.setOnTouchListener(new OnTouchListener() {

				@SuppressLint("ClickableViewAccessibility")
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if (popupWindow != null && popupWindow.isShowing()) {
						popupWindow.dismiss();
						// popupWindow = null;
					}

					return SearchGoodsAfterActivity.this.onTouchEvent(event);
				}

			});

			mRecommendCtv = (CheckedTextView) customView
					.findViewById(R.id.pwindow_recommend_ctv);
			mPriceLowCtv = (CheckedTextView) customView
					.findViewById(R.id.pwindow_price_low_to_high_ctv);
			mPriceHeightCtv = (CheckedTextView) customView
					.findViewById(R.id.pwindow_price_high_to_low_ctv);

			mRecommendCtv.setOnClickListener(this);
			mPriceLowCtv.setOnClickListener(this);
			mPriceHeightCtv.setOnClickListener(this);
		}

		LogTest.LogMsg(TAG, "保存的type " + mSp.getSearchAfterTab());
		setCtvSelect();

	}
	
	/**
	 * 设置Ctv选中状态
	 */
	private void setCtvSelect() {
		if (mSp.getSearchAfterTab() == 3) {
			mRecommendCtv.setChecked(false);
			mPriceLowCtv.setChecked(true);
			mPriceHeightCtv.setChecked(false);
		} else if (mSp.getSearchAfterTab() == 5) {
			mRecommendCtv.setChecked(false);
			mPriceLowCtv.setChecked(false);
			mPriceHeightCtv.setChecked(true);
		} else if (mSp.getSearchAfterTab() == 1) {
			mRecommendCtv.setChecked(true);
			mPriceLowCtv.setChecked(false);
			mPriceHeightCtv.setChecked(false);
		} else {
			mRecommendCtv.setChecked(false);
			mPriceLowCtv.setChecked(false);
			mPriceHeightCtv.setChecked(false);
		}
	}
	
	
	@Override
	public void onReflash() {

		if (NetWorkUtil.isNetworkAvailable(SearchGoodsAfterActivity.this)) {
			mSearchProList = null;
			currentPage = 1;
			loadingData(mSearchStr, String.valueOf(mSp.getSearchAfterTab()),1,false);
		} else {
			mSearchAfterLv.onRefreshComplete();
		}
	}
	
	@Override
	public void onLoadMore() {
		if (NetWorkUtil.isNetworkAvailable(SearchGoodsAfterActivity.this)
				&& isHavaNextPage) {
			loadingData(mSearchStr, String.valueOf(mSp.getSearchAfterTab()),currentPage+1,false);
		} else {
			mSearchAfterLv.onRefreshComplete();
		}		
	}
	
	/**
	 * 科大讯飞语音监听结果返回
	 * 
	 * @param result 解析后的结果
	 */
	public void setVoiceResult(String result) {
		Utils.showToast(this, result, Toast.LENGTH_LONG);
		mSearchContentEt.setText(result == null ? "" : result);
	}
	
	
	@Override
	public void onVoiceClick() {
//		Utils.showToast(this, "ddd", Toast.LENGTH_SHORT);
		mIflytekVoice.setVoiceParam();
		
		// 不显示听写对话框
		/*if(mVoiceRecognizerListener == null) {
			mVoiceRecognizerListener = mIflytekVoice.new VoiceRecognizerListener();
		}
		int ret = mIat.startListening(mVoiceRecognizerListener);
		if (ret != ErrorCode.SUCCESS) {
			LogTest.LogMsg(TAG, "听写失败,错误码：" + ret);
		}*/
		
		
		//显示听写对话框
		if(mVoiceRecognizerDialogListener == null) {
			mVoiceRecognizerDialogListener = mIflytekVoice.new VoiceRecognizerDialogListener();
		}
		mIatDialog.setListener(mVoiceRecognizerDialogListener);
		mIatDialog.show();
		Utils.showToast(this, "请开始说话", Toast.LENGTH_SHORT);

		
	}
	
	@Override
	public void onInVisible() {
		mBackToTopIb.setVisibility(View.GONE);
		
	}
	
	@Override
	public void onVisible() {
		mBackToTopIb.setVisibility(View.VISIBLE);
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
	protected void onStart() {
		super.onStart();
		LogTest.LogMsg(TAG, "SearchGoodsAfterActivity-onStart");
	}

	@Override
	protected void onResume() {
		super.onResume();
		LogTest.LogMsg(TAG, "SearchGoodsAfterActivity-onResume");

	}

	@Override
	protected void onPause() {
		super.onPause();
		LogTest.LogMsg(TAG, "SearchGoodsAfterActivity-onPause");
	}

	@Override
	protected void onDestroy() {
		mSp.saveSearchAfterTab(1);
		super.onDestroy();
		LogTest.LogMsg(TAG, "SearchGoodsAfterActivity-onDestroy");
		
	}

}
