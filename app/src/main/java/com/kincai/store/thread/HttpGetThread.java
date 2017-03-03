package com.kincai.store.thread;

import com.kincai.store.net.MyHttpGet;
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

	private static final String TAG = "HttpGetThread";
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
		LogTest.LogMsg("kincai", mUrl);
		try {
			String result = MyHttpGet.getInstance().httpGet(mUrl);
			if ("SearchItem".equals(mFlag)) {
				msg.what = 210;
				msg.obj = result;
			} else if ("AllProImagePath".equals(mFlag)) {
				msg.what = 2100;
				msg.obj = result;
			} else if ("SearchGoodsAfterActivity".equals(mFlag)) {
				msg.what = 2101;
				msg.obj = result;
			} else if ("AllProImagePathToCart".equals(mFlag)) {
				msg.what = 2102;
				msg.obj = result;
			} else if ("AllProImagePathToBrowse".equals(mFlag)) {
				msg.what = 2103;
				msg.obj = result;
			} else if ("AllProImagePathToHome".equals(mFlag)) {
				msg.what = 2104;
				msg.obj = result;
			} else if ("GetCate".equals(mFlag)) {
				msg.what = 2105;
				msg.obj = result;
			} else if ("catePro".equals(mFlag)) {
				msg.what = 2106;
				msg.obj = result;
			} else if ("AllProImagePathToCate".equals(mFlag)) {
				msg.what = 2107;
				msg.obj = result;
			} else if ("ProDetailsImage".equals(mFlag)) {
				msg.what = 2108;
				msg.obj = result;
			} else if ("proCartNum".equals(mFlag)) {
				msg.what = 2109;
				msg.obj = result;
			} else if("MyCartActivityCartNum".equals(mFlag)) {
				msg.what = 2110;
				msg.obj = result;
			} else if ("indexProData".equals(mFlag)) {
				msg.what = 203;
				msg.obj = result;
			} else if("getAdvdata".equals(mFlag)) {
				msg.what = 204;
				msg.obj = result;
			} else if ("home_grid".equals(mFlag)) {
				msg.what = 2111;
				msg.obj = result;
			} else if ("integralStore".equals(mFlag)) {
				msg.what = 2112;
				msg.obj = result;
			} else if ("haveSplashAdv".equals(mFlag)) {
				msg.what = 2113;
				msg.obj = result;
			} else if("robot_chat".equals(mFlag)) {
				msg.what = 2114;
				msg.obj = result;
			} else if("query_order".equals(mFlag)) {
				msg.what = 2115;
				msg.obj = result;
			} else if("default_address".equals(mFlag)) {
				msg.what = 2116;
				msg.obj = result;
			} else if ("allOrderData".equals(mFlag)) {
				msg.what = 2117;
				msg.obj = result;
			} else if ("upDateProPayNum".equals(mFlag)) {
				msg.what = 2118;
				msg.obj = result;
			} else if ("orderUpdate".equals(mFlag)) {
				msg.what = 2119;
				msg.obj = result;
			} else if("isHaveOrder".equals(mFlag)) {
				msg.what = 2120;
				msg.obj = result;
			} else if ("getStoredata".equals(mFlag)) {
				msg.what = 2121;
				msg.obj = result;
			} else if("cart_edit_goodsnum".equals(mFlag)) {
				msg.what = 2122;
				msg.obj = result;
			} else if ("cart_edit_goodsdeleteOne".equals(mFlag)) {
				msg.what = 2123;
				msg.obj = result;
			} else if ("cart_edit_goodsdeleteSelect".equals(mFlag)) {
				msg.what = 2125;
				msg.obj = result;
			} else if("ProDetail".equals(mFlag)) {
				msg.what = 2126;
				msg.obj = result;
			} else if("getStoreProdata".equals(mFlag)) {
				msg.what = 2127;
				msg.obj = result;
			} else if ("isStoreCollect".equals(mFlag)) {
				msg.what = 2128;
				msg.obj = result;
			} else if ("addStoreCollect".equals(mFlag)) {
				msg.what = 2129;
				msg.obj = result;
			} else if ("getStoreAllAndNew".equals(mFlag)) {
				msg.what = 2130;
				msg.obj = result;
			} else if("getStoreNewProdata".equals(mFlag)) {
				msg.what = 2131;
				msg.obj = result;
			} else if ("logout".equals(mFlag)) {
				msg.what = 2132;
				msg.obj = result;
			} else if("isdevicelogin".equals(mFlag)) {
				msg.what = 2133;
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
