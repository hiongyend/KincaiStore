package com.kincai.store.thread;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;

import com.kincai.store.net.MyHttpGet;
import com.kincai.store.utils.LogTest;
/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 * 
 * @author kincai
 * 
 * @description 下载欢迎图片线程
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.thread
 *
 * @time 2015-7-15 下午9:42:49
 *
 */
public class HttpGetImgThread implements Runnable {
	
	private static final String TAG = "HttpGetImgThread";
	private Handler hand;
	private String url;
//	private MyHttpGet myHttpGet;
	private String mFlag;

	/**
	 * 下载欢迎图片以及用户头像线程
	 * @param hand
	 * @param url
	 */
	public HttpGetImgThread(Handler hand, String url, String flag) {
		this.hand = hand;
		this.url = url;
		this.mFlag = flag;
//		myHttpGet = new MyHttpGet();
	}

	@Override
	public void run() {
		
		// 获取我们回调主ui的message
		Message msg = hand.obtainMessage();
		LogTest.LogMsg("kincai", url);
		try {
			//由于加载数据少 对话框一闪而过 所以加了 等待
			Thread.sleep(500);
			Bitmap bitmap = MyHttpGet.getInstance().httpGetImg(url);
			if("userface".equals(mFlag)) {
				msg.what = 202;
				msg.obj = bitmap;
			} 
			
			if(null != msg.obj) {
				LogTest.LogMsg(TAG, "-->msg.obj不为空" );
			} else {
				LogTest.LogMsg(TAG, "-->msg.obj为空" );
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			LogTest.LogMsg(TAG, "-->异常");
			
		} finally {
			hand.sendMessage(msg);
		}
		
		
	}
}
