package com.kincai.store.ui.viewpager.page;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kincai.store.Constants;
import com.kincai.store.R;
import com.kincai.store.bean.StoreInfo;
import com.kincai.store.bean.StoreInfo.StoreData;
import com.kincai.store.ui.activity.StoreActivity;
import com.kincai.store.ui.activity.base.BaseActivity;
import com.kincai.store.ui.viewpager.base.StoreBasePager;
import com.kincai.store.utils.TimeUtil;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 *
 * @author kincai
 * 
 * @description TODO
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.ui.viewpager.page
 *
 * @time 2015-8-24 下午9:27:36
 *
 */
public class StoreJianjiePage extends StoreBasePager {

	private StoreInfo mStoreInfo;
	private boolean isHaveData;
	
	private TextView mStoreName, mStoreAttent, mStoreNum,mStoreHaoping,mStoreAddress, mStoreKaidianTime,
					mStoreMiaoshu,mStoreFuwu,mStoreWuliu,mStoreZhangGui,mStoreFuwuDianhua;
	ImageView mStoreLogo, mStoreXingyong;
	public StoreJianjiePage(Context context, int type ,String storeId, StoreInfo storeInfo) {
		super(context, type,storeId);
		mStoreInfo = storeInfo;
	}

	@Override
	public View initView() {
		return View.inflate(mContext, R.layout.pager_store_jianjie_layout, null);
	}

	@Override
	public void initData() {
		if (mContext instanceof StoreActivity) {
			((StoreActivity) mContext).setTopRlVisibility(false);
		}
		
		if(!isHaveData) {
			initViews();
			init();
		} 
	}
	
	private void init() {

		isHaveData = true;
		
		StoreData storeData = mStoreInfo.getStoreData();
		mStoreName.setText(storeData.getStore_name());
		mStoreAttent.setText(new StringBuilder().append(storeData.getAttent_num()).append("粉丝"));
		mStoreNum.setText(storeData.getStore_id());
		mStoreHaoping.setText(storeData.getGood_reputation_ratio());
		mStoreAddress.setText(storeData.getAddress());
		mStoreKaidianTime.setText(TimeUtil.StringLongToString(storeData.getCreate_time(), "yyyy-MM-dd HH:mm:ss"));
		mStoreMiaoshu.setText(storeData.getPro_desc_value());
		mStoreFuwu.setText(storeData.getSeller_service_value());
		mStoreWuliu.setText(storeData.getLogistics_service_value());
		mStoreZhangGui.setText(storeData.getShopkeeper_nickname());
		mStoreFuwuDianhua.setText(storeData.getShopkeeper_phone());
//		mStoreXingyong = (ImageView) mStoreBaseView.findViewById(R.id.store_xinyong_iv);
		((BaseActivity)mContext).mImageLoader.loadImage(new StringBuilder()
		.append(Constants.SERVER_URL)
		.append("image_store/logo/")
		.append(storeData.getLogo_url()).toString(), mStoreLogo);
		
		
	}
	
	private void initViews() {
		mStoreName = (TextView) mStoreBaseView.findViewById(R.id.store_name_tv);
		mStoreAttent = (TextView) mStoreBaseView.findViewById(R.id.store_attent_tv);
		mStoreNum = (TextView) mStoreBaseView.findViewById(R.id.store_num);
		mStoreHaoping = (TextView) mStoreBaseView.findViewById(R.id.store_haoping);
		mStoreAddress = (TextView) mStoreBaseView.findViewById(R.id.store_address);
		mStoreKaidianTime = (TextView) mStoreBaseView.findViewById(R.id.store_create_time);
		mStoreMiaoshu = (TextView) mStoreBaseView.findViewById(R.id.store_miaoshu);
		mStoreFuwu = (TextView) mStoreBaseView.findViewById(R.id.store_fuwutaidu);
		mStoreWuliu = (TextView) mStoreBaseView.findViewById(R.id.store_wuliufuwu);
		mStoreZhangGui = (TextView) mStoreBaseView.findViewById(R.id.store_zhanggui);
		mStoreFuwuDianhua = (TextView) mStoreBaseView.findViewById(R.id.store_fuwudianhua);
		mStoreLogo = (ImageView) mStoreBaseView.findViewById(R.id.store_logo_iv);
		mStoreXingyong = (ImageView) mStoreBaseView.findViewById(R.id.store_xinyong_iv);
	}
	
	
}
