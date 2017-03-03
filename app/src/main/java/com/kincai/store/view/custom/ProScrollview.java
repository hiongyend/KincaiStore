package com.kincai.store.view.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 * 
 * @author kincai
 * 
 * @description 自定义scrollview 解决scrollview和viewpage 手势滑动监听（暂时不用 因为使用自定义viewpager解决了问题）
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.view.custom
 *
 * @time 2015-7-14 下午1:39:07
 *
 */
public class ProScrollview extends ScrollView {

	private GestureDetector mGestureDetector;
	OnTouchListener mGestureListener;

	public ProScrollview(Context context) {
		this(context, null);
	}

	public ProScrollview(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ProScrollview(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mGestureDetector = new GestureDetector(context, new YScrollDetector());
	}

	/**
	 * 触发拦截触摸事件
	 * 一但返回True（代表事件在当前的viewGroup中会被处理），则向下传递之路被截断（所有子控件将没有机会参与Touch事件），
	 * 同时把事件传递给当前的控件的onTouchEvent()处理；返回false，则把事件交给子控件的onInterceptTouchEvent()
	 * 
	 * 
	 * onInterceptTouchEvent方法是关键，重写这个方法使如果ScrollView有touch事件时不被拦截，
	 * 这样只要ScrollView有touch事件优先处理，这样就保证了滑动的流畅。
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return super.onInterceptTouchEvent(ev)
				&& mGestureDetector.onTouchEvent(ev);
	}

	class YScrollDetector extends SimpleOnGestureListener {
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			if (Math.abs(distanceY) > Math.abs(distanceX)) {
				return true;
			}
			return false;
		}
	}

}
