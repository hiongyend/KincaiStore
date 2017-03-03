package com.kincai.store.thread;

import com.kincai.store.net.MyHttpGetApk;
import com.kincai.store.utils.LogTest;

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
 * @description 下载APK线程类
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.thread
 *
 * @time 2015-7-24 下午7:41:18
 *
 */
public class DownLoadApk implements Runnable {

	private static final String TAG = null;
	private String mUrl;
	private Handler mHandler;
	private MyHttpGetApk mMyHttpGetApk;
	
	
	public DownLoadApk(String mUrl, Handler mHandler) {
		this.mUrl = mUrl;
		this.mHandler = mHandler;
		mMyHttpGetApk = new MyHttpGetApk();
	}


	@Override
	public void run() {
		// TODO Auto-generated method stub
		Message msg = mHandler.obtainMessage();
		LogTest.LogMsg("DownLoadApk", mUrl);
		try {
			String result = mMyHttpGetApk.getApk(mUrl);
			msg.what = 230;
			msg.obj = result;


			if(null != msg.obj) {
				LogTest.LogMsg(TAG, "-->msg.obj不为空" );
			} else {
				LogTest.LogMsg(TAG, "-->msg.obj为空" );
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			LogTest.LogMsg(TAG, "-->异常");
			
		} finally {
			mHandler.sendMessage(msg);
		}
		
		
	}

}
