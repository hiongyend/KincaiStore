package com.kincai.store.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 * 
 * @author kincai
 * 
 * @description 网络状态工具
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.utils
 *
 * @time 2015-7-15 下午9:35:51
 *
 */
public class NetWorkUtil {
	
	
	/**
	 * 判断网络状态
	 * @param context
	 * @return
	 */
	public static boolean isNetworkAvailable(Context context) { 
		ConnectivityManager connectivity = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE); 
		if (connectivity == null) { 
			return false; 
		} 
		else { 
			NetworkInfo[] info = connectivity.getAllNetworkInfo(); 
			if (info != null) { 
				int infoLenght = info.length;
				for (int i = 0; i < infoLenght; i++) { 
					if (info[i].getState() == NetworkInfo.State.CONNECTED) { 
						return true; 
					} 
				} 
			} 
		} 
		
		return false; 
	}
	
	/**
	 * 检查是否是WIFI 
	 * @param context
	 * @return
	 */
	public static boolean isWifi(Context context) {
		NetworkInfo info = getNetworkInfo(context);
		if (info != null) {
			if (info.getType() == ConnectivityManager.TYPE_WIFI)
				return true;
		}
		return false;
	}

	/** 
	 * 检查是否是移动网络 
	 * @param context
	 * @return
	 */
	public static boolean isMobile(Context context) {
		NetworkInfo info = getNetworkInfo(context);
		if (info != null) {
			if (info.getType() == ConnectivityManager.TYPE_MOBILE)
				return true;
		}
		return false;
	}
	
	/**
	 * 获得网络信息
	 * @param context
	 * @return
	 */
	private static NetworkInfo getNetworkInfo(Context context) {

		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		return cm.getActiveNetworkInfo();
	}
}
