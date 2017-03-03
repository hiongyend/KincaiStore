package com.kincai.store.view.adapter;

import java.util.List;

import com.kincai.store.ui.viewpager.base.BasePager;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

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
 * @package com.kincai.store.view.adapter
 *
 * @time 2015-8-24 下午8:18:19
 *
 */
public class OrderViewPagerAdapter extends PagerAdapter {
	
	List<BasePager> mPagers;
	public OrderViewPagerAdapter(List<BasePager> pagers) {
		mPagers = pagers;
	}
	@Override
	public int getCount() {
		return mPagers.size();
	}
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return (arg0 == arg1);
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View view = mPagers.get(position).mBaseView;
		container.addView(view);
		return view;
	}
	
	

	

}
