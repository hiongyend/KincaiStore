package com.kincai.store.utils;

import com.kincai.store.R;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.GridView;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 * 
 * @author kincai
 * 
 * @description view动画工具
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.utils
 *
 * @time 2015-7-15 下午9:38:53
 *
 */
public class AnimationUtil {

	private static AlphaAnimation mShowActionAa;
	private static AlphaAnimation mHiddenActionAa;

	private static TranslateAnimation mShowActionTa;
	private static TranslateAnimation mHiddenActionTa;
	
	/**
	 * 布局动画管理器
	 */
	private static LayoutAnimationController lac;

	/**
	 * view隐藏的 AlphaAnimation动画
	 * @param 
	 * @param duration
	 */
	public static void setHideAnimationAa(View view, int duration) {

		if (null == view || duration < 0) {

			return;

		}

		if (null != mHiddenActionAa) {

			mHiddenActionAa.cancel();

		}

		mHiddenActionAa = new AlphaAnimation(1.0f, 0.0f);

		mHiddenActionAa.setDuration(duration);

		mHiddenActionAa.setFillAfter(true);

		view.startAnimation(mHiddenActionAa);

	}

	/**
	 * view 显示的AlphaAnimation动画
	 * @param view
	 * @param duration
	 */
	public static void setShowAnimationAa(View view, int duration) {

		if (null == view || duration < 0) {

			return;

		}

		if (null != mShowActionAa) {

			mShowActionAa.cancel();

		}

		mShowActionAa = new AlphaAnimation(0.0f, 1.0f);

		mShowActionAa.setDuration(duration);

		mShowActionAa.setFillAfter(true);

		view.startAnimation(mShowActionAa);

	}

	/**
	 * view显示的TranslateAnimation动画
	 * @param view
	 * @param duration
	 */
	public static void setShowAnimationTa(View view, int duration) {

		if (null == view || duration < 0) {

			return;

		}

		if (null != mShowActionTa) {

			mShowActionTa.cancel();

		}

		mShowActionTa = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF,
				0.0f);
		mShowActionTa.setDuration(duration);
		view.startAnimation(mShowActionTa);
	}
	
	/**
	 * view隐藏的TranslateAnimation动画
	 * @param view
	 * @param duration
	 */
	public static void setHiddenAnimationTa(View view, int duration) {
		
		if (null == view || duration < 0) {
			
			return;
			
		}
		
		if (null != mHiddenActionTa) {
			
			mHiddenActionTa.cancel();
			
		}
		
		mHiddenActionTa = new TranslateAnimation(Animation.RELATIVE_TO_SELF,     
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f,     
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,     
                -1.0f); 
		mHiddenActionTa.setDuration(duration);
		view.startAnimation(mHiddenActionTa);
		
	}
	
	/**
	 * 进入activity切换动画 (有下沉效果)
	 * @param context
	 */
	public static void startHaveSinkActivityAnimation(Activity activity) {
		activity.overridePendingTransition(R.anim.startactivity_smooth_fade_in, R.anim.startactivity_sink_fade_out);
	}
	/**
	 * 进入activity切换动画 (平滑效果)
	 * @param context
	 */
	public static void startSmoothActivityAnimation(Activity activity) {
		activity.overridePendingTransition(R.anim.startactivity_smooth_fade_in, R.anim.startactivity_smooth_fade_out);
	}
	
	
	/**
	 * 结束activity切换动画 (有上浮效果)
	 * @param activity
	 */
	public static void finishHaveFloatActivityAnimation(Activity activity) {
		activity.overridePendingTransition(R.anim.finish_float_fade_in, R.anim.finish_smooth_fade_out);
	}


	/**
	 * 结束activity切换动画 (平滑效果)
	 * @param activity
	 */
	public static void finishSmoothActivityAnimation(Activity activity) {
		activity.overridePendingTransition(R.anim.finish_smooth_fade_in, R.anim.finish_smooth_fade_out);
	}
	
	/**
	 * gridview布局动画
	 * @param context
	 * @param gridView
	 */
	public static void setGridviewLayoutAnimation(Context context,
			GridView gridView) {
		if (lac == null) {
			lac = new LayoutAnimationController(AnimationUtils.loadAnimation(
					context, R.anim.gridview_zoom));
		}
		lac.setOrder(LayoutAnimationController.ORDER_NORMAL);
		gridView.setLayoutAnimation(lac);
		gridView.startLayoutAnimation();

	}
}
