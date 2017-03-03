package com.kincai.store.thread;

import java.util.List;

import org.apache.http.NameValuePair;

import android.os.Handler;
import android.os.Message;

import com.kincai.store.net.MyHttpPost;
import com.kincai.store.utils.LogTest;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 * 
 * @author kincai
 * 
 * @description Http Post请求
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.thread
 *
 * @time 2015-7-15 下午9:39:19
 *
 */
public class HttpPostThread implements Runnable {

	private static final String TAG = "HttpPostThread";
	private Handler mHand;
	private String mUrl;
//	private MyHttpPost httpPost;
	private String mFlag;
	List<NameValuePair> mlistParameters;

	/**
	 * post请求
	 * 
	 * @param hand
	 * @param url
	 */
	public HttpPostThread(Handler hand, String url,
			List<NameValuePair> mlistParameters, String flag) {
		this.mHand = hand;
		// 拼接访问服务器完整的地址
		this.mUrl = url;
		this.mFlag = flag;
		this.mlistParameters = mlistParameters;
//		httpPost = new MyHttpPost();
	}

	@Override
	public void run() {
		// 获取我们回调主ui的message
		Message msg = mHand.obtainMessage();
		LogTest.LogMsg("kincai", mUrl);
		try {
			// 由于加载数据少 对话框一闪而过 所以加了 等待
			Thread.sleep(500);
			String result = MyHttpPost.getInstance().httpPost(mUrl, mlistParameters);
			if ("FeedBackActivity".equals(mFlag)) {
				msg.what = 220;
				msg.obj = result;
			} else if ("SearchGoodsAfterActivity".equals(mFlag)) {
				msg.what = 2200;
				msg.obj = result;
			} else if ("MyCollectActivity".equals(mFlag)) {
				msg.what = 2201;
				msg.obj = result;
			} else if ("MyCartActivity".equals(mFlag)) {
				msg.what = 2202;
				msg.obj = result;
			} else if ("getAddress".equals(mFlag)) {
				msg.what = 2203;
				msg.obj = result;
			} else if ("getBrowse".equals(mFlag)) {
				msg.what = 2204;
				msg.obj = result;
			} else if ("getBrowseClear".equals(mFlag)) {
				msg.what = 2205;
				msg.obj = result;
			} else if ("getBrowseNum".equals(mFlag)) {
				msg.what = 2206;
				msg.obj = result;
			} else if ("deleteCart".equals(mFlag)) {
				msg.what = 2207;
				msg.obj = result;
			} else if ("addaddress".equals(mFlag)) {
				msg.what = 2208;
				msg.obj = result;
			} else if ("isCollect".equals(mFlag)) {
				msg.what = 2209;
				msg.obj = result;
			} else if ("addCollect".equals(mFlag)) {
				msg.what = 2210;
				msg.obj = result;
			} else if ("addBrowseHistory".equals(mFlag)) {
				msg.what = 2211;
				msg.obj = result;
			} else if ("addCart".equals(mFlag)) {
				msg.what = 2212;
				msg.obj = result;
			} else if("deleteCartOne".equals(mFlag)) {
				msg.what = 2213;
				msg.obj = result;
			} else if("deleteCollectOne".equals(mFlag)) {
				msg.what = 2214;
				msg.obj = result;
			} else if("getImagePath".equals(mFlag)) {
				msg.what = 2215;
				msg.obj = result;
			} else if ("editAddress".equals(mFlag)) {
				msg.what = 2216;
				msg.obj = result;
			} else if ("deleteAddress".equals(mFlag)) {
				msg.what = 2217;
				msg.obj = result;
			} else if ("HomeFragmentV".equals(mFlag)) {
				msg.what = 205;
				msg.obj = result;
			} else if ("SettingActivityV".equals(mFlag)) {
				msg.what = 2050;
				msg.obj = result;
			} else if ("login".equals(mFlag)) {
				msg.what = 207;
				msg.obj = result;
			} else if ("reg".equals(mFlag)) {
				msg.what = 2070;
				msg.obj = result;
			} else if ("changeUserInfo".equals(mFlag)) {
				msg.what = 208;
				msg.obj = result;
			} else if ("uploadOrderdata".equals(mFlag)) {
				msg.what = 2218;
				msg.obj = result;
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
			mHand.sendMessage(msg);
		}


	}

}
