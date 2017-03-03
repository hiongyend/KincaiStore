package cn.smssdk.gui;

import android.os.Handler;
import android.os.Message;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 * 
 * @author kincai
 * 
 * @description  Http Get 请求
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.thread
 *
 * @time 2015-7-15 下午9:42:38
 *
 */
public class HttpGetThread implements Runnable {

	private Handler mHand;
	private String mUrl;
	private String mFlag;
//	private MyHttpGet myHttpGet = new MyHttpGet();

	/**
	 * 
	 * @param hand
	 * @param url
	 */
	public HttpGetThread(Handler hand, String url, String flag) {
		this.mHand = hand;
		// 拼接访问服务器完整的地址
		this.mUrl = url;
		this.mFlag = flag;
	}

	@Override
	public void run() {
		// 获取我们回调主ui的message
		Message msg = mHand.obtainMessage();
//		LogTest.LogMsg("kincai", mUrl);
		try {
			String result = MyHttpGet.getInstance().httpGet(mUrl);
			if ("res".equals(mFlag)) {
				msg.what = 0x2222;
				msg.obj = result;
			}
				

			if(null != msg.obj) {
//				LogTest.LogMsg(TAG, "-->msg.obj不为空" );
			} else {
//				LogTest.LogMsg(TAG, "-->msg.obj为空" );
			}
			
		} catch (Exception e) {
			e.printStackTrace();
//			LogTest.LogMsg(TAG, "-->异常");

		} finally {
			mHand.sendMessage(msg);
		}

		
	}
}
