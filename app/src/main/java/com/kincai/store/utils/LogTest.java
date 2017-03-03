package com.kincai.store.utils;

import android.util.Log;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 * 
 * @author kincai
 * 
 * @description log日志
 * 
 * @project Kincai_Store
 * 
 * @package com.kincai.store.utils
 * 
 * @time 2015-7-11 下午4:19:55
 * 
 */
public class LogTest {

	private static boolean debug = true;

	public static void LogMsg(String msg) {

		if (debug) {
			LogMsg("LogTest", msg);
		}

	}

	public static void LogMsg(String log, String msg) {

		if (debug) {
			Log.i(log, msg);
		}

	}

}
