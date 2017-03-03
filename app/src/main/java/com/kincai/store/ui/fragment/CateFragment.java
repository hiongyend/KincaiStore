package com.kincai.store.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import com.kincai.store.Constants;
import com.kincai.store.R;
import com.kincai.store.bean.CateInfo;
import com.kincai.store.bean.ProInfo;
import com.kincai.store.db.CateDao;
import com.kincai.store.model.IReflashListener;
import com.kincai.store.thread.CachedThreadPoolUtils;
import com.kincai.store.thread.HttpGetThread;
import com.kincai.store.ui.activity.MainActivity;
import com.kincai.store.ui.activity.ProDetailsActivity;
import com.kincai.store.ui.activity.SearchGoodsOnActivity;
import com.kincai.store.ui.fragment.base.BaseFragment;
import com.kincai.store.utils.AnimationUtil;
import com.kincai.store.utils.JsonUtil;
import com.kincai.store.utils.LogTest;
import com.kincai.store.utils.NetWorkUtil;
import com.kincai.store.utils.Utils;
import com.kincai.store.view.adapter.CateLeftMenuListAdapter;
import com.kincai.store.view.adapter.HomeProListAdapter;
import com.kincai.store.view.custom.HomeListview;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 * 
 * @author kincai
 * 
 * @description 分类
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.fragment
 *
 * @time 2015-6-29 上午8:13:15
 *
 */
public class CateFragment extends BaseFragment implements OnClickListener,
		OnItemClickListener, IReflashListener {
	private static final String TAG = "CateFragment";
	private RelativeLayout mNetWorkLayout;
	private ListView mCateListView;
	private HomeListview mCateProListview;
	private EditText mCateEditText;
	private ProgressBar mProgressBar;
	private TextView mCateTipTv;

	private List<CateInfo> mCateInfos;
	private MyHandler mMyHandler;
	private CateLeftMenuListAdapter mCateListAdapter;
	private HomeProListAdapter mCateProListAdapter;
	private CateDao mCateDao;
	private List<ProInfo> mSearchProList;
	private List<ProInfo> mNewProList;
	
	/** 当前加载页 */
	private static int currentPage = 1;
	private boolean isHavaNextPage;

	private String mResponseFlag;
	private boolean isNetLoad = false;
	private boolean isFirst = true;

	private int mItem;

	@Override
	public View getContentView() {
		return Utils.getView(mMainActivityContext, R.layout.fragment_cate_layout);
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
//		isNetLoad = true;
		mCateDao = new CateDao(mMainActivityContext);
//		mImageDao = new ImageDao(mMainActivityContext);
		mMyHandler = new MyHandler();
//		mLoadAnimation = AnimationUtils.loadAnimation(mMainActivityContext,
//				R.anim.load_progressbar);
//		mInterpolator = new LinearInterpolator();
//		mLoadAnimation.setInterpolator(mInterpolator);
	}

	/**
	 * 初始化控件
	 */
	public void initView() {
		mNetWorkLayout = (RelativeLayout) mView
				.findViewById(R.id.network_abnormal_top_layout);
		mCateListView = (ListView) mView.findViewById(R.id.cate_lv);
		mCateProListview = (HomeListview) mView
				.findViewById(R.id.cate_right_pro_lv);
		mCateEditText = (EditText) mView.findViewById(R.id.cate_search_et);
		// mCateProgressLayout = (LinearLayout)
		// mView.findViewById(R.id.cate_progress_ll);
		mProgressBar = (ProgressBar) mView.findViewById(R.id.cate_progress);
		mCateTipTv = (TextView) mView.findViewById(R.id.cate_not_tip_tv);

	}

	/**
	 * 初始化分类信息
	 */
	private void initData() {

		mCateInfos = mCateDao.getCateInfo();
		LogTest.LogMsg(TAG, "数据库为空？" + (mCateDao == null));
		if (null != mCateInfos && mCateInfos.size()>0) {
			setCateListData();
		}

		if (NetWorkUtil.isNetworkAvailable(mMainActivityContext)) {
			CachedThreadPoolUtils.execute(new HttpGetThread(mMyHandler,
					Constants.API_URL + "appGetCateApi.php", "GetCate"));
		}
	}

	/**
	 * 加载商品信息
	 * 
	 * @param cateId
	 */
	private void loadProData(String cateId, boolean isLoad, int page) {
		if (NetWorkUtil.isNetworkAvailable(mMainActivityContext)) {

			if (isLoad) {
				mProgressBar.setVisibility(View.VISIBLE);
				mCateTipTv.setVisibility(View.GONE);
			}

			CachedThreadPoolUtils.execute(new HttpGetThread(mMyHandler,
					new StringBuffer().append(Constants.API_URL)
					.append("appGetCateSearchProApi.php?cId=")
					.append(cateId).append("&page=")
					.append(page)
					.append("&pageSize=")
					.append(Constants.GET_PAGESIZE).toString(), "catePro"));
		}

	}

	/**
	 * 设置事件监听
	 */
	public void setListener() {
		mNetWorkLayout.setOnClickListener(this);
		mCateListView.setOnItemClickListener(this);
		mCateEditText.setOnClickListener(this);
		mCateProListview.setOnItemClickListener(this);
		mCateProListview.setOnRefreshListener(this);
	}

	/**
	 * 设置分类
	 */
	private void setCateListData() {
		LogTest.LogMsg(TAG, "数据库为空？" + (mCateDao == null));
		if (null != mCateInfos) {
			if (null == mCateListAdapter) {
				mCateListAdapter = new CateLeftMenuListAdapter(mMainActivityContext,
						mCateInfos);
				mCateListView.setAdapter(mCateListAdapter);
			} else {
				mCateListAdapter.onDateChange(mCateInfos);
			}

			mCateListAdapter.setSelectedPosition(0);
			mCateListAdapter.notifyDataSetInvalidated();
			mItem = mCateInfos.get(0).getId();

			// LogTest.LogMsg("数据库", mCateDao.getCateInfo().toString());
			if (isFirst) {
//				getImageData();
				loadProData(mItem + "", true, 1);
				isFirst = false;
			}

		}

	}

	/**
	 * 
	 * @param datas
	 * @param Imagedatas
	 */
	public void setSearchData(List<ProInfo> datas) {

		// mCateProgressLayout.setVisibility(View.GONE);
		mProgressBar.setVisibility(View.GONE);
		mCateProListview.setVisibility(View.VISIBLE);
		mCateTipTv.setVisibility(View.GONE);

		if (datas != null && datas.size() > 0) {

			refreshList(datas);

		} else if ("402".equals(mResponseFlag)) {
			refreshList(datas);
			mCateProListview.setVisibility(View.GONE);
			mCateTipTv.setVisibility(View.VISIBLE);
			mCateTipTv.setText(getResources().getString(R.string.cate_no_pro));

		} else {
			refreshList(datas);
			mCateProListview.setVisibility(View.GONE);
			mCateTipTv.setVisibility(View.VISIBLE);
			mCateTipTv.setText(getResources().getString(R.string.cate_fail));

		}

	}

	/**
	 * 刷新界面
	 * 
	 * @param datas
	 * @param Imagedatas
	 */
	private void refreshList(List<ProInfo> datas) {
		if (mCateProListAdapter == null) {

			mCateProListAdapter = new HomeProListAdapter(mMainActivityContext, datas);
			mCateProListview.setAdapter(mCateProListAdapter);
		} else {
			LogTest.LogMsg(TAG, "eeeeeee");
			mCateProListAdapter.onDateChange(datas);
		}
	}

	@SuppressLint("HandlerLeak")
	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);

			switch (msg.what) {
			case 2105:
				getResponseCate(msg);

				break;

			case 2106:
//				if (mCateProListAdapter != null) {
//					LogTest.LogMsg(TAG, " reflashComplete---");
//					mCateProListview.reflashComplete();
//				}
				mCateProListview.onRefreshComplete();
				disposePageLoadData(msg);

				

				break;

			case 2107:

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
	
	/*private void getResponseImagePath(Message msg) {
		String response = (String) msg.obj;

		if (null != response) {
			String code = JsonUtil.msgJson("code", response);
			if(null != code) {
				if ("200".equals(code)) {
					mAllProImagePath = JsonUtil.getImgPath("data", response);
					
				}
			}
			
		}
		setSearchData(mSearchProList);
//		if(null != mAllProImagePath) {
//			addProImagePathList(mAllProImagePath, mSearchProImagePath);
//		} else {
//			mAllProImagePath = mSearchProImagePath;
//		}
//		
//		if(isImageFirst && null != msg.obj) {
//			mSp.saveHomeProImagePath(response);
//			isImageFirst = false;
//		}
		
	}*/

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
			if (null != code) {
				if ("200".equals(code)) {
					List<ProInfo> searchDataList = JsonUtil.getPro("data",
							response);
					if (null != searchDataList) {
						return searchDataList;
					}
				} else if ("402".equals(code)) {
					mResponseFlag = "402";
				}
			}
		}
		return null;
	}

	/**
	 * 获取分类返回数据
	 * 
	 * @param msg
	 */
	private void getResponseCate(Message msg) {
		String response = (String) msg.obj;
		if (null != response) {
			String code = JsonUtil.msgJson("code", response);
			if ("200".equals(code)) {
				List<CateInfo> list = JsonUtil.getCate("data", response);
				if (null != list) {
					LogTest.LogMsg("list", list.toString());
					mCateInfos = list;
					saveCateData(list);
					setCateListData();
					return;

				}
			}
		}

		// mCateInfos = mCateDao.getCateInfo();
		// if (null != mCateInfos) {
		// setCateListData();
		// }

	}

	/**
	 * 保存分类 遍历每一条分类判断是否存在此分类 如果存在则跳过 否则保存
	 * 
	 * @param datas
	 *            分类信息
	 */
	private void saveCateData(List<CateInfo> datas) {
		List<CateInfo> dbList = mCateDao.getCateInfo();
		List<CateInfo> newlist = new ArrayList<CateInfo>();
		if (null != dbList) {
			boolean flag = true;
			int datasSize = datas.size();
			int dbListSize = dbList.size();
			for (int i = 0; i < datasSize; i++) {
				CateInfo cateInfo = new CateInfo();
				for (int j = 0; j < dbListSize; j++) {
					flag = true;

					if (datas.get(i).getcName()
							.equals(dbList.get(j).getcName())) {
						flag = false;
						break;
					}
				}
				if (flag) {
					cateInfo.setId(datas.get(i).getId());
					cateInfo.setcName(datas.get(i).getcName());
					newlist.add(cateInfo);
				}
			}
			mCateDao.saveUserInfo(newlist);
		} else {
			mCateDao.saveUserInfo(datas);
		}

	}

	private void initOb() {
		mSearchProList = null;
		currentPage = 1;
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.network_abnormal_top_layout:
			startActivity(new Intent(Settings.ACTION_SETTINGS));
			AnimationUtil.startHaveSinkActivityAnimation((MainActivity)mMainActivityContext);
			break;

		case R.id.cate_search_et:

			startActivity(new Intent(mMainActivityContext, SearchGoodsOnActivity.class));
			AnimationUtil.startHaveSinkActivityAnimation((MainActivity)mMainActivityContext);
			break;
		}
	}

	@Override
	public void onReflash() {

		if(NetWorkUtil.isNetworkAvailable(mMainActivityContext)) {
			initOb();
			loadProData(mItem + "", true,1);
		} else {
			mCateProListview.onRefreshComplete();
		}
		
	}
	
	@Override
	public void onLoadMore() {
		
		if (NetWorkUtil.isNetworkAvailable(mMainActivityContext)
				&& isHavaNextPage) {
			loadProData(mItem + "", true, currentPage+1);
		} else {
			mCateProListview.onRefreshComplete();
		}
		
	}

	

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		if (parent== mCateListView) {
			LogTest.LogMsg(TAG, "mCateListView-position "+position
					+" cate-item-name "+ mCateInfos.get(position).getcName());

			mCateListView.setSelection(position);
			mCateListAdapter.setSelectedPosition(position);
			mCateListAdapter.notifyDataSetInvalidated();

			// mCateProgressLayout.setVisibility(View.VISIBLE);
			// mProgressBar.setVisibility(View.VISIBLE);
			// mCateTipTv.setVisibility(View.GONE);
			mItem = mCateInfos.get(position).getId();
			initOb();
			loadProData(mItem + "", true,1);
		} else if (parent == mCateProListview) {
			if(null != mSearchProList) {
				LogTest.LogMsg(TAG, "mCateProListview-position "+position
						+" catepro-name "+ mSearchProList.get(position).getpName());
				
				Intent intent = new Intent(mMainActivityContext, ProDetailsActivity.class);
				//因为加了头布局 position 0位置已经被头布局占用 所以点击列表第一个item position为1
				intent.putExtra("pId", mSearchProList.get(position-1).getId());
				intent.putExtra("storeId", mSearchProList.get(position-1).getStoreId());
				startActivity(intent);
				AnimationUtil.startHaveSinkActivityAnimation((MainActivity)mMainActivityContext);
			}
		}

	}

	@Override
	public void onStart() {
		super.onStart();
		LogTest.LogMsg(TAG, "CateFragment-onStart");

	}

	@Override
	public void onResume() {
		super.onResume();
		LogTest.LogMsg(TAG, "CateFragment-onResume");

		if (!NetWorkUtil.isNetworkAvailable(mMainActivityContext)) {
			// Utils.showToast(mMainActivityContext, "网络连接失败", Toast.LENGTH_SHORT);
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		LogTest.LogMsg(TAG, "CateFragment-onPause");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		LogTest.LogMsg(TAG, "CateFragment-onDestroy");
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
			LogTest.LogMsg(TAG, "CateFragment-onHiddenChanged");
			onResume();
			isNerwoking();

		}

	}
	@Override
	public void netWork() {
		if (isNetLoad) {
			initOb();
			loadProData(mItem + "", true, 1);
		}
		isNetLoad = false;
		mNetWorkLayout.setVisibility(View.GONE);
	}
	
	@Override
	public void noNetWork() {
		isNetLoad = true;
		mNetWorkLayout.setVisibility(View.VISIBLE);
	}
	
}
