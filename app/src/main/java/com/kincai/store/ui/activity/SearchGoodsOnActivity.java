package com.kincai.store.ui.activity;

import java.util.ArrayList;
import java.util.List;

import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.kincai.store.R;
import com.kincai.store.bean.SearchHistory;
import com.kincai.store.bean.SearchItem;
import com.kincai.store.common.IflytekVoice;
import com.kincai.store.common.IflytekVoice.VoiceRecognizerDialogListener;
import com.kincai.store.common.IflytekVoice.VoiceRecognizerListener;
import com.kincai.store.db.SearchHistoryDao;
import com.kincai.store.db.SearchItemDao;
import com.kincai.store.model.IEditTextVoiceListener;
import com.kincai.store.ui.activity.base.BaseSlideActivity;
import com.kincai.store.utils.AnimationUtil;
import com.kincai.store.utils.LogTest;
import com.kincai.store.utils.NetWorkUtil;
import com.kincai.store.utils.Utils;
import com.kincai.store.view.adapter.SearchHistoryListAdapter;
import com.kincai.store.view.adapter.SearchItemListAdapter;
import com.kincai.store.view.custom.CleanableAndVoiceEditTextView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 *
 * @author kincai
 * 
 * @description 搜索界面 非结果界面
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.ui
 *
 * @time 2015-6-27 下午8:25:32
 *
 */
public class SearchGoodsOnActivity extends BaseSlideActivity implements IEditTextVoiceListener{

	private static final String TAG = "SearchGoodsOnActivity";

//	private RelativeLayout mTitlebarRl;
	private Button mSearchBtn;
	private CleanableAndVoiceEditTextView mSearchContentEt;
	private ListView mSearchBeforeLv;
	private ListView mSearchOnLv;

	private SearchHistoryDao mSearchHistoryDb;
	private SearchHistoryListAdapter mHistoryAdapter;
	private SearchItemListAdapter mSearchItemAdapter;
	private MyTextWatcher mMyTextWatcher;

	private Button mClearSearchHistoryBtn;

	private SearchItemDao mItemDb;

	private HistoryItemListener mHistoryItemListener;
	private MyHandler mMyHandler;
	
	// 科大讯飞语音听写对象
	private SpeechRecognizer mIat;
	// 语音听写UI
	private RecognizerDialog mIatDialog;

	
	private IflytekVoice mIflytekVoice;

	@SuppressWarnings("unused")
	private VoiceRecognizerListener mVoiceRecognizerListener;
	private VoiceRecognizerDialogListener mVoiceRecognizerDialogListener;
		
	
	@Override
	public int initContentView() {
		return R.layout.activity_search_on_layout;
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
		
		// 初始化科大讯飞语音初始化监听(父类实现)
		setInitListenet();
		// 初始化语音听写
		mIat = SpeechRecognizer.createRecognizer(this, mInitListener);
		mIatDialog = new RecognizerDialog(this, mInitListener);
		mIflytekVoice = new IflytekVoice(this, mIat);
		mSearchHistoryDb = new SearchHistoryDao(SearchGoodsOnActivity.this);
		mMyHandler = new MyHandler();
		mItemDb = new SearchItemDao(SearchGoodsOnActivity.this);
		mMyTextWatcher = new MyTextWatcher();
		mHistoryItemListener = new HistoryItemListener();
	}

	/**
	 * 初始化控件
	 */
	public void initView() {
		super.initView();
//		mTitlebarRl = (RelativeLayout) findViewById(R.id.search_title_rl);
		mSearchBtn = (Button) findViewById(R.id.search_btn);
		mSearchContentEt = (CleanableAndVoiceEditTextView) findViewById(R.id.search_goods_et);
		mSearchBeforeLv = (ListView) findViewById(R.id.search_before_lv);
		mSearchOnLv = (ListView) findViewById(R.id.search_on_lv);
		mSearchOnLv.setVisibility(View.VISIBLE);
	}

	/**
	 * 设置监听
	 */
	public void setListener() {
		super.setListener();
		mSearchBtn.setOnClickListener(this);
		mSearchContentEt.addTextChangedListener(mMyTextWatcher);
		mSearchOnLv.setOnItemClickListener(mHistoryItemListener);
		mSearchBeforeLv.setOnItemClickListener(mHistoryItemListener);
		mSearchContentEt.setEditTextVoiceListener(this);
		mSearchContentEt.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			
				if(actionId==EditorInfo.IME_ACTION_SEARCH) {
					
					if(mSearchContentEt.getText().toString().trim().equals("")) {
						search(mSearchContentEt.getText().toString().trim());
					} else {
					}
					return true;
				}
				return false;
			}
		});

	}
	

	/**
	 * 点击
	 * 
	 * @author kincai
	 * 
	 */
	class HistoryItemListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			LogTest.LogMsg(TAG, position + " position");
			// 从1开始
			String content = ((TextView) view.findViewById(R.id.search_item_tv))
					.getText().toString();
			LogTest.LogMsg(TAG, "点击获取的内容 " + content);
			search(content);
		}

	}

	// private OnItemClickListener = new onItemCli

	/**
	 * 初始化数据
	 */
	private void initData() {

		// 头部
		View headView = View.inflate(SearchGoodsOnActivity.this,
				R.layout.item_search_on_listview_headlayout, null);

		// 底部
		View footView = View.inflate(SearchGoodsOnActivity.this,
				R.layout.item_search_on_listview_footlayout, null);

		mClearSearchHistoryBtn = (Button) footView
				.findViewById(R.id.search_clear_history_bt);
		mClearSearchHistoryBtn.setOnClickListener(this);

		mSearchOnLv.addHeaderView(headView, null, false);
		mSearchOnLv.addFooterView(footView, null, false);
		List<SearchHistory> list = mSearchHistoryDb.getSearchInfo();

		mHistoryAdapter = new SearchHistoryListAdapter(SearchGoodsOnActivity.this,
				list, R.layout.item_search_listview_layout);
		mSearchOnLv.setAdapter(mHistoryAdapter);

	}

	/**
	 * edittext实时监听
	 * 
	 */
	class MyTextWatcher implements TextWatcher {
		@SuppressWarnings("unused")
		private CharSequence temp;

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			temp = s;
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			// mTextView.setText(s);//将输入的内容实时显示
		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub

			if (mSearchContentEt.getText().toString().length() > 0) {

				LogTest.LogMsg(TAG, "从历史listview进入模糊查询listview");
				LogTest.LogMsg(TAG, "输入框内容长度 "
						+ mSearchContentEt.getText().toString().length());

				mSearchOnLv.setVisibility(View.GONE);
				mSearchBeforeLv.setVisibility(View.VISIBLE);
				setQueryData(mSearchContentEt.getText().toString());

			}

			if (mSearchContentEt.getText().toString().equals("")) {
				mSearchOnLv.setVisibility(View.VISIBLE);
				mSearchBeforeLv.setVisibility(View.GONE);
			}

		}
	};

	/**
	 * 设置模糊查询列表显示数据
	 */
	private void setQueryData(String item) {
		// TODO Auto-generated method stub

		List<SearchItem> dbList = mItemDb.getFuzzyQueryInfo(item);

		mSearchItemAdapter = new SearchItemListAdapter(
				SearchGoodsOnActivity.this, dbList,
				R.layout.item_search_listview_layout);
		mSearchBeforeLv.setAdapter(mSearchItemAdapter);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
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
			// 按搜索时把记录添加到数据库
			search(mSearchContentEt.getText().toString().trim());
			break;
		case R.id.search_clear_history_bt:
			clearHistory();
			break;

		default:
			break;
		}
	}

	private void clearHistory() {
		mClearSearchHistoryBtn.setText("正在清除...");
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
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
	 * 点击搜索按钮执行操作
	 */
	private void search(String etStr) {
		if(NetWorkUtil.isNetworkAvailable(SearchGoodsOnActivity.this)) {
		
			if (!"".equals(etStr)) {
				List<SearchHistory> list = new ArrayList<SearchHistory>();
				SearchHistory onHistory = new SearchHistory();
				// sHistory.setContent(mSearchContentEt.getText().toString());
				// list.add(sHistory);
				// 判断需不需要保存搜索历史
				List<SearchHistory> dbList = mSearchHistoryDb.getSearchInfo();
				if (null != dbList) {
					boolean flag = true;
					for (SearchHistory history : dbList) {
	
						if (history.getContent().equals(etStr)) {
							flag = false;
							break;
						}
					}
					if (flag) {
						onHistory.setContent(etStr);
						list.add(onHistory);
						mSearchHistoryDb.saveSearchInfo(list);
					}
	
				} else {
					onHistory.setContent(etStr);
					list.add(onHistory);
					mSearchHistoryDb.saveSearchInfo(list);
				}
	
				Intent intent = new Intent(SearchGoodsOnActivity.this,
						SearchGoodsAfterActivity.class);
				intent.putExtra("searchContent", etStr);
				startActivity(intent);
				SearchGoodsOnActivity.this.finish();
				AnimationUtil.startHaveSinkActivityAnimation(this);
			}
		}
	}

	@SuppressLint("HandlerLeak")
	class MyHandler extends Handler {
		public void handleMessage(Message msg) {
			switch (msg.what) {
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
			}
		};
	}
	
	/**
	 * 科大讯飞语音监听结果返回
	 * 
	 * @param results 解析后的结果
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
		LogTest.LogMsg(TAG, "SearchGoodsOnActivity-onStart");
	}

	@Override
	protected void onResume() {
		super.onResume();
		LogTest.LogMsg(TAG, "SearchGoodsOnActivity-onResume");

	}

	@Override
	protected void onPause() {
		super.onPause();
		LogTest.LogMsg(TAG, "SearchGoodsOnActivity-onPause");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		LogTest.LogMsg(TAG, "SearchGoodsOnActivity-onDestroy");
		
	}
}
