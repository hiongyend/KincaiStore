package com.kincai.store.ui.activity;

import java.util.List;

import com.kincai.store.R;
import com.kincai.store.bean.AreaCityInfo;
import com.kincai.store.bean.AreaProvinceInfo;
import com.kincai.store.bean.AreaZoneInfo;
import com.kincai.store.db.AreaDao;
import com.kincai.store.ui.activity.base.BaseSlideActivity;
import com.kincai.store.utils.AnimationUtil;
import com.kincai.store.view.adapter.AreaCityAdapter;
import com.kincai.store.view.adapter.AreaProvinceAdapter;
import com.kincai.store.view.adapter.AreaZoneAdapter;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 * 
 * @author kincai
 * 
 * @description 地区选择
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.ui
 *
 * @time 2015-7-9 下午12:01:08
 *
 */
public class AreaSelectActivity extends BaseSlideActivity implements OnClickListener,
		OnItemClickListener {

	private TextView mTitleTv;
	private TextView mAreaIsSelectTv;
	private ListView mAreaListView;

	/** 地区数据库管理*/
	private AreaDao mAreaDao;
	private List<AreaProvinceInfo> mProvinceInfos;
	private List<AreaCityInfo> mCityInfos;
	private List<AreaZoneInfo> mZoneInfos;

	private AreaProvinceAdapter mAreaProvinceAdapter;
	private AreaCityAdapter mAreaCityAdapter;
	private AreaZoneAdapter mAreaZoneAdapter;
	/** listview点击的是哪个数据 默认0 为省份 1为市 2为县/区*/
	private int itemClickNum = 0;

		
	@Override
	public int initContentView() {
		return R.layout.activity_area_select_layout;
	}
	@Override
	public void initDatas() {
		init();
		initView();
		loadProvinceData();
		setListener();
	}

	/**
	 * 初始化
	 */
	private void init() {
		mAreaDao = new AreaDao(AreaSelectActivity.this);

	}

	/**
	 * 初始化控件
	 */
	public void initView() {
		super.initView();
		mTitleTv = (TextView) findViewById(R.id.title_tv);
		mAreaIsSelectTv = (TextView) findViewById(R.id.area_is_select_tv);
		mAreaListView = (ListView) findViewById(R.id.area_lv);

		mTitleTv.setText(getResources().getString(R.string.select_area_str));
	}

	/**
	 * 设置事件监听
	 */
	public void setListener() {
		super.setListener();
		mAreaListView.setOnItemClickListener(this);
	}

	/**
	 * 首次进来加载省份数据
	 */
	private void loadProvinceData() {
		mProvinceInfos = mAreaDao.getProvinceInfo();
		if (null != mProvinceInfos) {
			mAreaProvinceAdapter = new AreaProvinceAdapter(
					AreaSelectActivity.this, mProvinceInfos,
					R.layout.item_area_listview_layout);

			mAreaListView.setAdapter(mAreaProvinceAdapter);
		}

	}

	/**
	 * 根据 provinceId从数据库加载市数据
	 * @param provinceId
	 */
	private void loadCityData(int provinceId) {
		mCityInfos = mAreaDao.getCityInfo(provinceId);
		if (null != mCityInfos) {
			mAreaProvinceAdapter = null;
			mAreaCityAdapter = new AreaCityAdapter(AreaSelectActivity.this,
					mCityInfos, R.layout.item_area_listview_layout);
			mAreaListView.setAdapter(mAreaCityAdapter);
		}
	}

	/**
	 * 根据cityId从数据库加载县/区数据
	 * @param cityId
	 */
	private void loadZoneData(int cityId) {
		mZoneInfos = mAreaDao.getZoneInfo(cityId);
		if (null != mZoneInfos) {
			mAreaCityAdapter = null;
			mAreaZoneAdapter = new AreaZoneAdapter(AreaSelectActivity.this,
					mZoneInfos, R.layout.item_area_listview_layout);
			mAreaListView.setAdapter(mAreaZoneAdapter);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.titlebar_back_iv:
			finish();
			AnimationUtil.finishHaveFloatActivityAnimation(this);
			break;

		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (itemClickNum == 0) {
			if (null != mProvinceInfos) {
				int provinceId = mProvinceInfos.get(position).getProvince_id();
				mAreaIsSelectTv.setVisibility(View.VISIBLE);
				mAreaIsSelectTv.setText(mProvinceInfos.get(position)
						.getProvince_name());
				loadCityData(provinceId);
				itemClickNum = 1;
			}

		} else if (itemClickNum == 1) {
			if (null != mCityInfos) {
				int cityId = mCityInfos.get(position).getCity_id();
				mAreaIsSelectTv.setText(mAreaIsSelectTv.getText().toString()
						+ mCityInfos.get(position).getCity_name());
				loadZoneData(cityId);
				itemClickNum = 2;
			}

		} else if (itemClickNum == 2) {
			if (null != mZoneInfos) {
				Intent data = new Intent();
				data.putExtra("area", mAreaIsSelectTv.getText().toString()
						+ mZoneInfos.get(position).getZone_name());
				setResult(0, data);
				finish();
				AnimationUtil.finishHaveFloatActivityAnimation(this);
			}

		}
	}
	
	@Override
	public void netWork() {
//		mSettingTitleRl.setVisibility(View.VISIBLE);
		mNetWorkLayout.setVisibility(View.GONE);
	}
	
	@Override
	public void noNetWork() {
		mNetWorkLayout.setVisibility(View.VISIBLE);
//		mSettingTitleRl.setVisibility(View.GONE);
	}

}
