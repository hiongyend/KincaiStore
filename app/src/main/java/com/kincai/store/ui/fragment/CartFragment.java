package com.kincai.store.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.kincai.store.Constants;
import com.kincai.store.R;
import com.kincai.store.bean.CartInfo;
import com.kincai.store.bean.CartInfo.CartData;
import com.kincai.store.bean.CartInfo.CartData.GoodsData;
import com.kincai.store.bean.UserInfo;
import com.kincai.store.model.ICartItemClickListener;
import com.kincai.store.model.IDialog;
import com.kincai.store.model.IReflashListener;
import com.kincai.store.thread.CachedThreadPoolUtils;
import com.kincai.store.thread.HttpGetThread;
import com.kincai.store.thread.HttpPostThread;
import com.kincai.store.ui.activity.ConfirmOrderActivity;
import com.kincai.store.ui.activity.MainActivity;
import com.kincai.store.ui.activity.ProDetailsActivity;
import com.kincai.store.ui.activity.StoreActivity;
import com.kincai.store.ui.activity.UserLoginActivity;
import com.kincai.store.ui.activity.base.BaseActivity;
import com.kincai.store.ui.fragment.base.BaseFragment;
import com.kincai.store.utils.AnimationUtil;
import com.kincai.store.utils.GsonTools;
import com.kincai.store.utils.JsonUtil;
import com.kincai.store.utils.LoadingDialog;
import com.kincai.store.utils.LogTest;
import com.kincai.store.utils.NetWorkUtil;
import com.kincai.store.utils.Utils;
import com.kincai.store.view.adapter.CartListAdapter;
import com.kincai.store.view.custom.RefreshExpandableListView;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
 * @description 购物车
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.fragment
 *
 * @time 2015-7-24 下午7:48:24
 *
 */
public class CartFragment extends BaseFragment implements OnClickListener,
		IReflashListener, ICartItemClickListener {

	private static final String TAG = "CartFragment";
	private TextView mTitleProNumTv;
	private TextView mTitleEditTv;
	private TextView mTitleTv;

	private RefreshExpandableListView mListview;
	private RelativeLayout mCartTipLayout;
	private LinearLayout mCartHejiLl;
	private TextView mCartTipTv;
	private LinearLayout mChooseAllCbLl;
	private ImageView mChooseAllCbIv;
	private TextView mAllPriceTv;
	private Button mJiesuanBtn;
	private Button mNotLoginBtn;
	private RelativeLayout mNetWorkLayout;
	private LinearLayout mLoadingProgressLl;
	private ProgressBar mProgressBar;
	
	private ChangeCartReceiver mCartReceiver;
	
	private MyHandler mMyHandler;
	private String userId;

	private List<CartData> mSearchProList;
	private List<CartData> mNewProList;
	
	/** 当前加载页 */
	private static int currentPage = 1;
	private boolean isHavaNextPage;
	
	private CartListAdapter mCartListAdapter;
	

	private String flag;
	/** 是否编辑全部*/
	private boolean isEditAll;
	/** 是否选中了商品一件或多件都为true 否则false*/
	private boolean isSelectPro;
	/** 是否选中全部商品*/
	private boolean isSelectAllPro;
	/** 购物车选中商品价格*/
	private double mPrice;
	/** 标志下次进来的时候是否需要加载*/
	private boolean isLoad;

	/** 是否已经加载有数据了*/
	private boolean isHaveData;
	
	/** 购物车加载是否正在请求数据 true 还未返回 防止连续多次加载同一数据*/
	private boolean isNowLoad;
	
	private LoadingDialog mLoadingDialog;
	/**
	 * 选中后筛选后的数据
	 */
	
	private List<CartData> removeCartNoSelect = new ArrayList<>();
	
	StringBuilder goodsIdArr;
	@Override
	public View getContentView() {
		return Utils.getView(mMainActivityContext, R.layout.fragment_cart_layout);
	}
	
	@Override
	public void initDatas() {
		init();
		initView();
		initData();
		setListener();
		registerCartReceiver();
	}

	/**
	 * 初始化
	 */
	private void init() {
		mMyHandler = new MyHandler();
		
		isSelectPro = false;
		isSelectAllPro = false;
		isEditAll = false;
		isHaveData = false;
		isNowLoad = false;
		
	}

	/**
	 * 初始化控件
	 */
	public void initView() {
		mLoadingProgressLl = (LinearLayout) mView
				.findViewById(R.id.loading_progress_ll);
		mProgressBar = (ProgressBar) mView
				.findViewById(R.id.loading_progress_p);
		mNetWorkLayout = (RelativeLayout) mView
				.findViewById(R.id.network_abnormal_top_layout);
		mTitleTv = (TextView) mView.findViewById(R.id.cart_title_tv);
		mTitleProNumTv = (TextView) mView
				.findViewById(R.id.cart_title_pro_num_tv);
		mTitleEditTv = (TextView) mView.findViewById(R.id.cart_tite_edit_tv);
		mListview = (RefreshExpandableListView) mView.findViewById(R.id.cart_lv);
		mChooseAllCbLl = (LinearLayout) mView.findViewById(R.id.cart_all_choose_cb_ll);
		mChooseAllCbIv = (ImageView) mView.findViewById(R.id.cart_all_choose_cb_iv);
		mCartHejiLl = (LinearLayout) mView.findViewById(R.id.cart_heji_ll);
		mJiesuanBtn = (Button) mView.findViewById(R.id.cart_jiesuan_btn);

		mCartTipLayout = (RelativeLayout) mView.findViewById(R.id.cart_tip_ll);
		mCartTipTv = (TextView) mView.findViewById(R.id.cart_tip_tv);
		mNotLoginBtn = (Button) mView
				.findViewById(R.id.cart_collect_not_login_btn);
		mAllPriceTv = (TextView) mView.findViewById(R.id.cart_all_price_tv);
		
		mTitleTv.setText(getResources().getString(R.string.main_tab_cart));
	}

	/**
	 * 设置监听
	 */
	public void setListener() {
		mNetWorkLayout.setOnClickListener(this);
		mTitleEditTv.setOnClickListener(this);
		mTitleEditTv.setOnClickListener(this);
		mNotLoginBtn.setOnClickListener(this);

		mJiesuanBtn.setOnClickListener(this);
		mListview.setOnRefreshListener(this);
		mChooseAllCbLl.setOnClickListener(this);
		

	}

	/**
	 * 初始化数据
	 */
	private void initData() {

		if (mSp.getUserIsLogin()) {
			List<UserInfo> mUserInfos = mUserDao.getUserInfoOne(mSp.getUsername());
			if (null != mUserInfos) {
				userId = String.valueOf(mUserInfos.get(0).getUserId());
//				getImageData();
				loadData(userId, "27", Constants.KINCAI_CART,
						Constants.KINCAI_TYPE_GET, true, 1);
				
			}
		} else {
			mListview.setVisibility(View.GONE);
			mCartTipLayout.setVisibility(View.VISIBLE);
			mCartTipTv.setText("还没登录哦!");
			isLoad = true;
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

		System.out.println("--page--"+page);
		if (NetWorkUtil.isNetworkAvailable(mMainActivityContext)) {

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

			isNowLoad = true;
			CachedThreadPoolUtils.execute(new HttpPostThread(mMyHandler,
					new StringBuffer().append(Constants.API_URL).append("appCollectAndCartApi.php").toString(),
					parameters, "MyCartActivity"));

		}
	}

	@SuppressLint("HandlerLeak")
	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 2202:
				
				isNowLoad = false;
				mLoadingProgressLl.setVisibility(View.GONE);
				mProgressBar.setVisibility(View.GONE);
				mListview.onRefreshComplete();
				mPrice = 0;
				disposePageLoadData(msg);
				break;

			case 2207:
				getDeleteData(msg);
				break;
			case 2213:
				getResponseDeleteCartOne(msg);
				break;
			case 2122://编辑选泽数量
				
				getResponseEditCartItemOneNum(msg);
				break;
			case 2123:
				getResponseEditCartItemDeleteOne(msg);
				break;
			case 2125:
				getResponseEditCartItemDeleteSelect(msg);
				break;
			} 
		}
	}
	
	private void getResponseEditCartItemDeleteSelect(Message msg) {
		String response = (String) msg.obj;
		
		if (null != response) {
			System.out.println(" response--"+response);
			if("200".equals(JsonUtil.msgJson("code", response))){
				//成功
				cartEditCartItemDeleteSelectIsSuccess(true);
			} else {
				//失败
				cartEditCartItemDeleteSelectIsSuccess(false);
			}
		} else {
			//失败
			cartEditCartItemDeleteSelectIsSuccess(false);
		}
		
		goodsIdArr = null;
		if(mLoadingDialog != null && mLoadingDialog.isShow()) {
			mLoadingDialog.dismiss();
		}
	}
	
	private void cartEditCartItemDeleteSelectIsSuccess(boolean isSuccess) {
		if(mCartListAdapter != null) {
			mCartListAdapter.setCartEditDeleteSelectIsSuccess(goodsIdArr.toString(), isSuccess);
		}

	}
	
	private void getResponseEditCartItemDeleteOne(Message msg) {
		String response = (String) msg.obj;
		if (null != response) {
			if("200".equals(JsonUtil.msgJson("code", response))){
				//成功
				cartEditCartItemDeleteOneIsSuccess(true);
			} else {
				//失败
				cartEditCartItemDeleteOneIsSuccess(false);
			}
		} else {
			//失败
			cartEditCartItemDeleteOneIsSuccess(false);
		}
		
		/*mCartOneEditCartId = "";*/
		initCartEditData();
		
		if(mLoadingDialog != null && mLoadingDialog.isShow()) {
			mLoadingDialog.dismiss();
		}
	}
	
	/**
	 * 回调在适配器 是否修改成功 不成功则恢复修改前数量
	 * @param isSuccess
	 */
	private void cartEditCartItemDeleteOneIsSuccess(boolean isSuccess) {
		if(mCartListAdapter != null) {
			mCartListAdapter.setCartEditDeleteOneIsSuccess(mGroupPosition, mChildPosition, isSuccess);
		}
	}
	
	/**
	 * 编辑数量返回数据
	 * @param msg
	 */
	private void getResponseEditCartItemOneNum(Message msg) {
		String response = (String) msg.obj;
		if (null != response) {
			if("200".equals(JsonUtil.msgJson("code", response))){
				//成功
				cartEditCartItemOneNumIsSuccess(true);
			} else {
				//失败
				cartEditCartItemOneNumIsSuccess(false);
			}
		} else {
			//失败
			cartEditCartItemOneNumIsSuccess(false);
		}
		
		mCartSelectOneEditOldNum = -1;
		initCartEditData();
		if(mLoadingDialog != null && mLoadingDialog.isShow()) {
			mLoadingDialog.dismiss();
		}
	}
	
	/**
	 * 回调在适配器 是否修改成功 不成功则恢复修改前数量
	 * @param isSuccess
	 */
	private void cartEditCartItemOneNumIsSuccess(boolean isSuccess) {
		if(mCartListAdapter != null) {
			mCartListAdapter.setCartEditSelectNumIsSuccess(mGroupPosition, mChildPosition, isSuccess, mCartSelectOneEditOldNum);
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
			
		} else {
			mNewProList = getSearchData(msg);
			if (null != mNewProList && mNewProList.size() > 0) {
				mSearchProList.addAll(mNewProList);
				currentPage++;
				
				LogTest.LogMsg(TAG, "currentPage " + currentPage);
			}
		}
		
		setSearchData(mSearchProList);

	}

	/**
	 * 获得删除一条商品返回的信息
	 * @param msg
	 */
	private void getResponseDeleteCartOne(Message msg) {
		String response = (String) msg.obj;
		if (null != response) {
			String code = JsonUtil.msgJson("code", response);
			if (null != code && "200".equals(code)) {
				initOb();
				loadData(userId, "27", Constants.KINCAI_CART,
						Constants.KINCAI_TYPE_GET, false, 1);
				mTitleProNumTv.setVisibility(View.VISIBLE);
				mTitleTv.setText(getResources().getString(
						R.string.main_tab_cart));

				return;
			}
		}
		//否则标题栏还原
		
		mTitleTv.setText(getResources().getString(R.string.main_tab_cart));
		mTitleProNumTv.setVisibility(View.VISIBLE);
		if(null != mSearchProList) {
			mTitleProNumTv.setText("(" + mSearchProList.size() + ")");
		} else {
			mTitleProNumTv.setText("");
		}
		
		

	}

	/**
	 * 删除购物车返回数据
	 * 
	 * @param msg
	 */
	private void getDeleteData(Message msg) {
		String response = (String) msg.obj;
		if (null != response) {
			String code = JsonUtil.msgJson("code", response);
			if (null != code && "200".equals(code)) {
					isEditAllList();
					mTitleProNumTv.setText("(0)");
					initOb();
					loadData(userId, "", Constants.KINCAI_CART,
							Constants.KINCAI_TYPE_GET, false, 1);
				
			} else {
				// 删除失败
				mListview.setVisibility(View.VISIBLE);
				mLoadingProgressLl.setVisibility(View.GONE);
				mProgressBar.setVisibility(View.GONE);
			}

		}

	}


	/**
	 * 显示数据
	 * 
	 * @param datas
	 */
	public void setSearchData(List<CartData> datas) {
		
		if (datas != null && datas.size() > 0) {
			isLoad = false;
			isHaveData = true;
			isSelectAllPro = false;
			isSelectPro = false;
			mChooseAllCbIv.setImageResource(R.drawable.ic_selltip_checkbox_normal);
			mListview.setVisibility(View.VISIBLE);
			mCartTipLayout.setVisibility(View.GONE);
			
			mAllPriceTv.setText("¥"+mPrice);
			mCartTipTv.setText("");
			mTitleEditTv.setVisibility(View.VISIBLE);

			int size = datas.size();
			int size2 = 0;
			for (int i = 0; i < size; i++) {
				size2 += datas.get(i).getGoods_data().size();
			}
			mTitleProNumTv.setText("(" + size2 + ")");
			
			mJiesuanBtn.setText(new StringBuilder().append("结算(").append(0).append(")").toString());
			refreshList(datas);
			tip(true);

		} else if ("402".equals(flag)) {
			if(!isHaveData) {
				refreshList(datas);
				isLoad = true;
				mTitleProNumTv.setText("");
				setLayoutGoneVisible("您的购物车什么都没有哦！");
				tip(false);
			} else {
				Utils.showToast(mMainActivityContext, "刷新数据失败！", 0);
			}
			
			
		} else if("400".equals(flag)){
			if(!isHaveData) {
				isLoad = true;
				mTitleProNumTv.setText("");
				refreshList(datas);
				setLayoutGoneVisible("购物车加载失败哦！");
				tip(false);
			} else {
				Utils.showToast(mMainActivityContext, "刷新数据失败！", 0);
			}
			
		}
	}
	
	/**
	 * 设置加载数据失败时显示隐藏
	 */
	private void setLayoutGoneVisible(String tipText) {
		mListview.setVisibility(View.GONE);
		mTitleEditTv.setVisibility(View.GONE);
		mCartTipLayout.setVisibility(View.VISIBLE);
		mCartTipTv.setText(tipText);
		mNotLoginBtn.setVisibility(View.GONE);
	}

	/**
	 * 刷新界面
	 * 
	 * @param datas
	 * @param Imagedatas
	 */
	private void refreshList(List<CartData> datas) {
		if (mCartListAdapter == null) {

			mCartListAdapter = new CartListAdapter(mMainActivityContext, datas);
			mListview.setAdapter(mCartListAdapter);
			mCartListAdapter.setiOnCartItemClickListener(this);
		} else {
			LogTest.LogMsg(TAG, "eeeeeee");
			mCartListAdapter.onDateChange(datas);
		}
		
		expandGroup();
	}
	
	
	private void expandGroup() {

		int size = mCartListAdapter.getGroupCount();
		for (int i = 0; i < size; i++) {
			mListview.expandGroup(i);
		}
	}
	
	
	/**
	 * 获取网络返回的数据
	 * 
	 * @param msg
	 * @return
	 */
	private List<CartData> getSearchData(Message msg) {
		String response = (String) msg.obj;

		if (null != response) {
			CartInfo cartInfo = GsonTools.changeGsonToBean(response, CartInfo.class);
			if("next".equals(cartInfo.getMessage())) {
				isHavaNextPage = true;
			} else if("nonext".equals(cartInfo.getMessage())) {
				isHavaNextPage = false;
			}
			if(cartInfo != null && 200 == cartInfo.getCode()) {
				List<CartData> cartData = cartInfo.getCartData();
				if(cartData != null) {
					return cartData;
				}
			} else if (cartInfo != null && 402 == cartInfo.getCode()) {
				flag = "402";
			} else if (cartInfo != null && 400 == cartInfo.getCode()) {
				flag = "400";
			}
		} else {
			flag = "400";
		}
		return null;
	}

	/**
	 * 是否显示编辑底部界面
	 */
	private void isEditAllList() {
		isEditAll = !isEditAll;

		mTitleEditTv.setText(isEditAll ? "完成" : "编辑全部");
		
		mCartHejiLl.setVisibility(isEditAll ? View.INVISIBLE : View.VISIBLE);
		
		mJiesuanBtn.setText(isEditAll ? "删除" : new StringBuilder().append("结算(").append(selectNum).append(")").toString());

		if(mCartListAdapter != null) {
			mCartListAdapter.setAllEdit(isEditAll);
		}
	}

	private int selectNum;
	private int mCartSelectOneEditOldNum;
	/*private String mCartOneEditGoodsId;
	private String mCartOneEditCartId;*/
	private int mGroupPosition;
	private int mChildPosition;
	

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.network_abnormal_top_layout:
			startActivity(new Intent(Settings.ACTION_SETTINGS));
			AnimationUtil.startHaveSinkActivityAnimation((MainActivity)mMainActivityContext);
			break;

		case R.id.cart_tite_edit_tv:

			isEditAllList();
			break;
		case R.id.cart_collect_not_login_btn:
			startActivity(new Intent(mMainActivityContext, UserLoginActivity.class));
			AnimationUtil.startHaveSinkActivityAnimation((MainActivity)mMainActivityContext);
			break;
		case R.id.cart_jiesuan_btn:
			
			if(isSelectAllPro) {
				// 全选
				if(isEditAll) {
					Utils.showToast(mMainActivityContext, "全选 删除", 0);
					cartEditDeleteSelect();
				} else {
					Utils.showToast(mMainActivityContext, "全选 结算", 0);
					cartJiesuan();
				}
				
			} else if(!isSelectAllPro && isSelectPro){
				// 有选 不是全选
				if(isEditAll) {
					Utils.showToast(mMainActivityContext, "有选 不是全选 删除", 0);
					cartEditDeleteSelect();
				} else {
					Utils.showToast(mMainActivityContext, "有选 不是全选 结算", 0);
					cartJiesuan();
				}
				
			} else if(!isSelectPro) {
				// 一个都没选
				Utils.showToast(mMainActivityContext, isEditAll ? "请选择要删除的宝贝" : "请选择要购买的商品", 0);
			}
			break;
		case R.id.cart_all_choose_cb_ll:
			if (!isSelectAllPro) {
				// 全选 刷新列表 
				mChooseAllCbIv.setImageResource(R.drawable.ic_selltip_checkbox_check);
				if(mCartListAdapter != null) {
					mCartListAdapter.setAllCheck(true);
				}
			} else {
				// 不全选 刷新列表 
				mChooseAllCbIv.setImageResource(R.drawable.ic_selltip_checkbox_normal);
				if(mCartListAdapter != null) {
					mCartListAdapter.setAllCheck(false);
				}
			}
			
			
			
			break;
		}

	}
	
	private void cartJiesuan() {

		if(removeCartNoSelect != null) {
			CartInfo cartInfo = new CartInfo();
			cartInfo.setCartData(removeCartNoSelect);
			Intent intent = new Intent(mMainActivityContext, ConfirmOrderActivity.class);
			intent.putExtra("cartinfo", cartInfo);
			mMainActivityContext.startActivity(intent);
		}
	}

	/**
	 * 删除选中的购物车商品
	 */
	private void cartEditDeleteSelect() {
		Utils.showMiniDialog(mMainActivityContext,((BaseActivity)mMainActivityContext).mScreenWidth, "确定删除选中宝贝？", "取消", "确定", new IDialog() {
			
			@Override
			public void onDialogClick() {
				if(NetWorkUtil.isNetworkAvailable(mMainActivityContext)) {
					if(mLoadingDialog == null) {
						mLoadingDialog = new LoadingDialog(mMainActivityContext, false);
					}
					mLoadingDialog.dialogShow();
					
					goodsIdArr = new StringBuilder();
					int size = removeCartNoSelect.size();
					for (int i = 0; i < size; i++) {
						List<GoodsData> goodsDatas = removeCartNoSelect.get(i).getGoods_data();
						int size2 = goodsDatas.size();
						for (int j = 0; j < size2; j++) {
							if(j == size2-1 && i== size-1) {
								goodsIdArr.append(goodsDatas.get(j).getGoods_id());
							} else {
								goodsIdArr.append(goodsDatas.get(j).getGoods_id()).append(",");
							}
							
						}
						
					}
//					System.out.println(" goodsIdArr --"+goodsIdArr.toString());
					
					CachedThreadPoolUtils.execute(new HttpGetThread(mMyHandler, 
							new StringBuffer().append(Constants.API_URL)
							.append("appCartEditDeleteSelectGoodsApi.php?cart_goods_id_arr=")
							.append(goodsIdArr).toString(), "cart_edit_goodsdeleteSelect"));
				}
				
			}
		},true);

	}

	
	/**
	 * 重新进入的时候
	 */
	private void againComeIn() {
		if (NetWorkUtil.isNetworkAvailable(mMainActivityContext)) {

			if (mSp.getUserIsLogin()) {
				mListview.setVisibility(View.VISIBLE);
				mCartTipLayout.setVisibility(View.GONE);
				mCartTipTv.setText("");
				mTitleEditTv.setText("编辑全部");
				List<UserInfo> mUserInfos = mUserDao.getUserInfoOne(mSp.getUsername());
				if (null != mUserInfos) {
					userId = String.valueOf(mUserInfos.get(0).getUserId());
					// getImageData();
				}

				initOb();
				loadData(userId, "27", Constants.KINCAI_CART,
						Constants.KINCAI_TYPE_GET, false, 1);

			} else {
				isLoad = true;
				mListview.setVisibility(View.GONE);
				mTitleEditTv.setVisibility(View.GONE);
				mCartTipLayout.setVisibility(View.VISIBLE);
				mCartTipTv.setText("还没登录哦!");
				mNotLoginBtn.setVisibility(View.VISIBLE);

			}
		} else {

		}
	}
	
	

	private void initOb() {
		mSearchProList = null;
		currentPage = 1;
	}

	@Override
	public void onReflash() {

		if (NetWorkUtil.isNetworkAvailable(mMainActivityContext)) {
			
			if(isEditAll) {
				Utils.showToast(mMainActivityContext, "编辑未完成无法刷新！", 0);
				mListview.onRefreshComplete();
			} else {
				initOb();
				loadData(userId, "27", Constants.KINCAI_CART,
						Constants.KINCAI_TYPE_GET, false, 1);
			}
			
		} else {
			mListview.onRefreshComplete();
		}
	}

	@Override
	public void onLoadMore() {

		if (NetWorkUtil.isNetworkAvailable(mMainActivityContext)
				&& isHavaNextPage) {
			if(isEditAll) {
				Utils.showToast(mMainActivityContext, "编辑未完成无法刷新！", 0);
				mListview.onRefreshComplete();
			} else {
				loadData(userId, "27", Constants.KINCAI_CART,
						Constants.KINCAI_TYPE_GET, false, currentPage + 1);
			}
			
		} else {
			mListview.onRefreshComplete();
		}
	}

	/**
	 * mainactivity tab回调方法
	 * 
	 * @param isNotNull
	 *            购物车没数据的时候 false tab不显示红点 购物车没数据的时候 true tab显示红点
	 */
	private void tip(boolean isNotNull) {
		if (mMainActivityContext instanceof MainActivity) {
			((MainActivity) mMainActivityContext).tip(isNotNull);
		}
	}
	
	@Override
	public void netWork() {
		System.out.println("--netWork--");
		if (isLoad && !isNowLoad) {
			System.out.println("--netWork--isLoad--");
			againComeIn();
		}
		mNetWorkLayout.setVisibility(View.GONE);
	}
	
	@Override
	public void noNetWork() {
		System.out.println("--noNetWork--");
		mNetWorkLayout.setVisibility(View.VISIBLE);
	}

	/**
	 * 修改信息广播接收者
	 * 
	 * @author kincai
	 * 
	 */
	class ChangeCartReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (Constants.CART_CHANGE_ACTION.equals(intent.getAction())) {
				LogTest.LogMsg(TAG, "接收到广播");
				if (null != userId && !"".equals(userId)) {
					initOb();
					loadData(userId, "27", Constants.KINCAI_CART,
							Constants.KINCAI_TYPE_GET, false, 1);
				}

				// 历史记录改变广播
			} else if (Constants.USERINFO_CHANGE_ACTION.equals(intent.getAction())) {
				userId = "";
//				if(mUserName != null && mSp.getUsername() != null &&!mSp.getUsername().equals(mUserName)) {
//					isHaveData = false;
//					System.out.println(mSp.getUsername()+" "+mUserName);
//				}
				if(!mSp.getUserIsLogin())
				isHaveData = false;
				
				againComeIn();
			} else {
				LogTest.LogMsg(TAG, "没有收到广播");
			}
		}

	}

	/**
	 * 广播注册
	 */
	private void registerCartReceiver() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.CART_CHANGE_ACTION);
		filter.addAction(Constants.USERINFO_CHANGE_ACTION);
		mCartReceiver = new ChangeCartReceiver();
		mMainActivityContext.registerReceiver(mCartReceiver, filter);
	}

	/**
	 * 购物车广播注销
	 */
	private void unRegisterCartReceiver() {
		mMainActivityContext.unregisterReceiver(mCartReceiver);
	}

	
	@Override
	public void onStart() {
		super.onStart();
		LogTest.LogMsg(TAG, "CartFragment-onStart");

	}

	@Override
	public void onResume() {
		super.onResume();
		LogTest.LogMsg(TAG, "CartFragment-onResume");
		if (!NetWorkUtil.isNetworkAvailable(mMainActivityContext)) {
			// Utils.showToast(mMainActivityContext, "网络连接失败", Toast.LENGTH_SHORT);
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		if(mLoadingDialog != null && mLoadingDialog.isShow()) {
			mLoadingDialog.dismiss();
			mLoadingDialog = null;
		}
		LogTest.LogMsg(TAG, "CartFragment-onPause");
	}

	@Override
	public void onStop() {
		super.onStop();
		LogTest.LogMsg(TAG, "CartFragment-onStop");
	}
	
	@Override
	public void onDestroy() {
		unRegisterCartReceiver();
		super.onDestroy();
		LogTest.LogMsg(TAG, "CartFragment-onDestroy");
		
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
			LogTest.LogMsg(TAG, "CartFragment-onHiddenChanged");
			// initData();
			if (isLoad && !isNowLoad) {
				againComeIn();
			}

//			onResume();
			isNerwoking();
		}

	}

	/**
	 * 进入商店
	 */
	@Override
	public void onComeInStore(String storeId) {
//		System.out.println("--storeId--"+storeId);
		//TODO 进入店铺
		startActivity(new Intent(mMainActivityContext, StoreActivity.class)
		.putExtra("storeId", storeId));
		AnimationUtil.startHaveSinkActivityAnimation((MainActivity)mMainActivityContext);
	}

	/**
	 * 进入商品详情
	 */
	@Override
	public void onComeInProDetail(String pId, String storeId) {
//		System.out.println("--pId--"+pId+" --storeId--"+storeId);
		startActivity(new Intent(mMainActivityContext, ProDetailsActivity.class)
				.putExtra("pId", Integer.parseInt(pId)).putExtra("storeId", Integer.parseInt(storeId)));
		
		AnimationUtil.startHaveSinkActivityAnimation((MainActivity)mMainActivityContext);
	}

	/**
	 * 设置底部数据
	 * @param selectNum
	 * @param isAll
	 */
	private void setBottomData(int selectNum, boolean isAll) {
		
		mJiesuanBtn.setText(isEditAll ? "删除" : new StringBuilder().append("结算(").append(selectNum).append(")").toString());
		mAllPriceTv.setText(new StringBuilder().append("¥").append(mPrice).toString());
		
		mChooseAllCbIv.setImageResource(isAll ? R.drawable.ic_selltip_checkbox_check : R.drawable.ic_selltip_checkbox_normal);
	}
	
	/**
	 * 删除一条数据
	 */
	@Override
	public void onCartOneDelete(int groupPosition, int childPosition,
			String cart_id, String cart_goods_id) {
//		System.out.println("--gropPosition--"+groupPosition+" --childPosition--"+childPosition+"--onCartOneDelete--cart_id--"+cart_id+" cart_goods_id"+cart_goods_id);
		/*mCartOneEditGoodsId = cart_goods_id;
		mCartOneEditCartId = cart_id;*/
		mGroupPosition = groupPosition;
		mChildPosition = childPosition;
		cartDeleteOne(cart_id, cart_goods_id);
	}

	/**
	 * 删除一条数据
	 * @param cart_id
	 * @param cart_goods_id
	 */
	private void cartDeleteOne(String cart_id, String cart_goods_id) {
		if(NetWorkUtil.isNetworkAvailable(mMainActivityContext)) {
			if(mLoadingDialog == null) {
				mLoadingDialog = new LoadingDialog(mMainActivityContext, false);
			}
			mLoadingDialog.dialogShow();
			
			CachedThreadPoolUtils.execute(new HttpGetThread(mMyHandler, 
					new StringBuffer().append(Constants.API_URL)
					.append("appCartEditDeleteOneGoodsApi.php?cart_id=").append(cart_id).append("&cart_goods_id=")
					.append(cart_goods_id).toString()
					, "cart_edit_goodsdeleteOne"));
		} else {
//			mCartOneEditCartId = "";
			initCartEditData();
		}
		
	}
	
	/**
	 * 编辑数量回调
	 */
	@Override
	public void onCartSelectNum(int groupPosition, int childPosition, String cart_goods_id, int newNum,  int oldNum) {
//		System.out.println("--onCartSelectNum--newNum--"+newNum+" cart_goods_id"+cart_goods_id+" oldNum"+oldNum);
		mCartSelectOneEditOldNum = oldNum;
		/*mCartOneEditGoodsId = cart_goods_id;*/
		mGroupPosition = groupPosition;
		mChildPosition = childPosition;
		editCartItemOneNum(newNum, cart_goods_id);
	}
	
	/**
	 * 请求服务器修改购物车选择商品数量数据
	 * @param newNum
	 * @param cart_goods_id
	 */
	private void editCartItemOneNum(int newNum, String cart_goods_id) {
		if(NetWorkUtil.isNetworkAvailable(mMainActivityContext)) {
			if(mLoadingDialog == null) {
				mLoadingDialog = new LoadingDialog(mMainActivityContext, false);
			}
			mLoadingDialog.dialogShow();
			
			CachedThreadPoolUtils.execute(new HttpGetThread(mMyHandler, 
					new StringBuffer().append(Constants.API_URL)
					.append("appCartEditSelectGoodsNumApi.php?cart_goods_id=")
					.append(cart_goods_id).append("&newNum=").append(newNum).toString()
					, "cart_edit_goodsnum"));
		} else {
			mCartSelectOneEditOldNum = -1;
			initCartEditData();
		}
	}

	private void initCartEditData() {
//		mCartOneEditGoodsId = "";
		mGroupPosition = -1;
		mChildPosition = -1;
	}
	
	/**
	 * 选中回调
	 */
	@Override
	public void onCartSelectCheckBox(List<CartData> datas) {
		mPrice = 0;
//		System.out.println("--datas--"+datas.get(0).getIsGroupCheck());
		
		
		removeCartNoSelect = cartIsSelectData(datas);
		selectNum = 0;
		int size = removeCartNoSelect.size();
		for (int i = 0; i < size; i++) {
			selectNum += removeCartNoSelect.get(i).getGoods_data().size();
			List<GoodsData> goods_data = removeCartNoSelect.get(i).getGoods_data();
			int sizes = goods_data.size();
			for (int j = 0; j < sizes; j++) {
				mPrice += Double.parseDouble(goods_data.get(j).getIPrice()) * Integer.parseInt(goods_data.get(j).getGoods_num());
			}
		}
		boolean cartIsAllSelect = cartIsAllSelect(datas);
		isSelectAllPro = cartIsAllSelect;
		if(isSelectAllPro) isSelectPro = true;
		else isSelectPro = removeCartNoSelect.size() > 0 ? true : false;
		
		setBottomData(selectNum, cartIsAllSelect);
		
	}
	
	/**
	 * 筛选出选中的购物车商品1
	 * @param datas 回调过来的购物车数据
	 * @return
	 */
	private List<CartData> cartIsSelectData(List<CartData> datas) {
		List<CartData> listCartData = new ArrayList<>();
		List<CartData> oldListCartData = datas;
		int size = datas.size();
		boolean iss = false;
		for (int i = 0; i < size; i++) {
			CartData cartData = new CartData();
			if(oldListCartData.get(i).getIsGroupCheck()) {
				listCartData.add(oldListCartData.get(i));
				continue;
			} else {
				iss = false;
				List<GoodsData> oldGoods_data = oldListCartData.get(i).getGoods_data();
				int size2 = oldGoods_data.size();
				
				List<GoodsData> GoodsDatas = new ArrayList<>();
				for (int j = 0; j < size2; j++) {
					
					if(oldGoods_data.get(j).getIsChildCheck()) {
						GoodsDatas.add(oldGoods_data.get(j));
						iss = true;
						continue;
					} 
				}
				
				if(iss) {
					cartData.setStore_id(oldListCartData.get(i).getStore_id());
					cartData.setStore_name(oldListCartData.get(i).getStore_name());
					cartData.setGoods_data(GoodsDatas);
				}
				
				
			}
			if(iss)
			listCartData.add(cartData);
			
		}
		
		
		
		return listCartData;
		
	}
	
	/**
	 * 是否全选
	 * @param datas 回调过来的购物车数据
	 * @return
	 */
	private boolean cartIsAllSelect(List<CartData> datas) {
		if(datas == null) return false;
		int size = datas.size();
		for (int i = 0; i < size; i++) {
			if(!datas.get(i).getIsGroupCheck()) {
				return false;
			}
		}
		
		return true;
	}

}
