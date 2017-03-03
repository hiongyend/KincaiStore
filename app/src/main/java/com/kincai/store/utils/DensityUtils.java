package com.kincai.store.utils;

import android.content.Context;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 *
 * @author kincai
 * 
 * @description dp px之间的转换
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.utils
 *
 * @time 2015-8-12 下午7:25:28
 *
 */
public class DensityUtils {

	/**
	 * dp转px
	 */
	public static int dp2px(Context ctx, float dp) {
		//设备像素缩放比
		return  (int) (dp * (ctx.getResources().getDisplayMetrics().density) + 0.5f);// 4.9->5 4.4->4

	}

	/**
	 * px转dp
	 */
	public static float px2dp(Context ctx, int px) {
		return px / (ctx.getResources().getDisplayMetrics().density);
	}
}
