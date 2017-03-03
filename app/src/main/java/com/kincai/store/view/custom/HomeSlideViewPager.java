package com.kincai.store.view.custom;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 *
 * @author kincai
 * 
 * @description 自定义viewpager   解决viewpager与其他竖直方向可滑动控件的滑动冲突
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.view.custom
 *
 * @time 2015-9-15 上午7:05:27
 *
 */
public class HomeSlideViewPager extends ViewPager {

	int startX;
	int startY;

	public HomeSlideViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public HomeSlideViewPager(Context context) {
		super(context);
	}

	/**
	 * 事件分发, 请求父控件及祖宗控件是否拦截事件
	 * 1. 右划, 而且是第一个页面, 需要父控件拦截
	 * 2. 左划, 而且是最后一个页面, 需要父控件拦截
	 * 3. 上下滑动, 需要父控件拦截
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// 不要拦截,这样是为了保证ACTION_MOVE调用
			getParent().requestDisallowInterceptTouchEvent(true);
			startX = (int) ev.getRawX();//屏幕的 getX是父控件的坐标
			startY = (int) ev.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:

			int endX = (int) ev.getRawX();
			int endY = (int) ev.getRawY();

			if (Math.abs(endX - startX) > Math.abs(endY - startY)) {// 左右滑动
				getParent().requestDisallowInterceptTouchEvent(true);
			} else {// 上下滑动
				getParent().requestDisallowInterceptTouchEvent(false);
			}

			break;

		}

		return super.dispatchTouchEvent(ev);
	}

}
