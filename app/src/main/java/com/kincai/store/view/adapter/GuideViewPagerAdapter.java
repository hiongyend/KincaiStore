package com.kincai.store.view.adapter;

import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 * 
 * @author kincai
 * 
 * @description 导航界面viewpager的adapter
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.view.adapter
 *
 * @time 2015-7-23 上午11:22:37
 *
 */
public class GuideViewPagerAdapter extends PagerAdapter {

	Context mContext;
	private List<ImageView> mlList;
	
	public GuideViewPagerAdapter(Context context, List<ImageView> list) {
		this.mContext = context;
		mlList = list;
	}
	
	/**
	 * 当我们不需要的view在这里销毁
	 */
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		container.removeView(mlList.get(position));
	}

	/**
	 * 加载view地方法 类似于listview的getView方法
	 */
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		container.addView(mlList.get(position));
		return mlList.get(position);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mlList.size();
	}

	/**
	 * 判断当前view是不是我们需要的对象
	 */
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return (arg0 == arg1);
	}

}
