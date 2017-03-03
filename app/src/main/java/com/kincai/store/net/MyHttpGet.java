package com.kincai.store.net;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.kincai.store.utils.LogTest;
import com.kincai.store.utils.StreamUtil;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 *
 * @author kincai
 * 
 * @description Http get请求方式工具类
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.net
 *
 * @time 2015-7-24 下午7:44:04
 *
 */
public class MyHttpGet {
	
	private MyHttpGet() {
		
		
	}
	
	private static MyHttpGet myHttpGet;
	
	public static synchronized MyHttpGet getInstance() {
		if(null == myHttpGet) {
			myHttpGet = new MyHttpGet();
			
		}
		return myHttpGet;
	}
	
	/**
	 * 获取返回string
	 * @param url
	 * @return
	 */
	public String httpGet(String url) {
//		String result = null;// 我们的网络交互返回值
		try {
			
			String httpType = url.substring(0, url.indexOf(":"));
			if("https".equals(httpType)) {
				
				return doHttpsGet(url, 5000);
			}
			
			HttpClient HttpClient = new DefaultHttpClient();
			HttpClient.getParams().setIntParameter(
					HttpConnectionParams.CONNECTION_TIMEOUT, 10 * 1000);
			HttpClient.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT,
					10 * 1000);
			
			HttpGet httpGet = new HttpGet(url);
			
			// 敲回车 返回服务器请求的信息
			HttpResponse response = HttpClient.execute(httpGet);
			// 得到状态码
			int code = response.getStatusLine().getStatusCode();
			String text = null;
			if (code == 200) {

				InputStream is = response.getEntity().getContent();
				text = StreamUtil.readInputStream(is);
				
				return text;

			} else if(code == 408){
				LogTest.LogMsg("httpGet 返回码", code+"");
				return "408";
			} else {
				LogTest.LogMsg("httpGet 返回码", code+"");
				return null;
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogTest.LogMsg("httpGet 返回码", "ee");
			return null;
		} finally {
			
		}
		
		
	}
	
public String doHttpsGet(String urlStr, int timeout) {
		
		InputStream is = null;
		URL url = null;
		HttpURLConnection conn = null;
		try {
			url = new URL(urlStr);
//				trustAllHosts();
//				HttpsURLConnection https = (HttpsURLConnection)url.openConnection();
//				if (url.getProtocol().toLowerCase().equals("https")) {
//					https.setHostnameVerifier(new MyHostnameVerifier());
//					conn = https;
//				} else {
				conn = (HttpURLConnection)url.openConnection();
//				}
				conn.setReadTimeout(timeout);
				conn.setConnectTimeout(timeout);
				conn.setRequestMethod("GET");
				
				conn.setRequestProperty("X-Bmob-Application-Id", "8f79426ffc6f3459cded0c344a068372");
				conn.setRequestProperty("X-Bmob-REST-API-Key", "1829f380199cd98eca659fcae05d0860");
				conn.setRequestProperty("Content-Type", "application/json");
				is = conn.getInputStream();
				
				
				return StreamUtil.readInputStream(is);
			
			} catch (Exception e1) {
				e1.printStackTrace();
			} finally {
				try {
					if (conn != null) {
						conn.disconnect();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		
		return null;
	}
	
	/**
	 * 获取返回bitmap
	 * @param url
	 * @return
	 */
	public Bitmap httpGetImg(String url) {
//		String result = null;// 我们的网络交互返回值
		
		BufferedInputStream bStream = null;
		try {
			
			HttpClient httpClient = new DefaultHttpClient();
			httpClient.getParams().setIntParameter(
					HttpConnectionParams.CONNECTION_TIMEOUT, 5 * 1000);
			httpClient.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT,
					30 * 1000);
			
			HttpGet httpGet = new HttpGet(url);
			// 敲回车 返回服务器请求的信息
			HttpResponse response = httpClient.execute(httpGet);
			// 得到状态码
			int code = response.getStatusLine().getStatusCode();
			Bitmap bitmap = null;
			if (code == 200) {

				InputStream is = response.getEntity().getContent();
				bStream = new BufferedInputStream(is);
				bitmap = BitmapFactory.decodeStream(bStream);
				
				return bitmap;

			} else {
				return null;
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		
	}
	

}
