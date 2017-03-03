package com.kincai.store.net;

import java.io.InputStream;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;

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
 * @description Http post请求方式工具类
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.net
 *
 * @time 2015-7-24 下午7:43:36
 *
 */
public class MyHttpPost {
	
	private MyHttpPost() {

	}
	private static MyHttpPost myHttpPost;
	
	public static synchronized MyHttpPost getInstance() {
		if(null == myHttpPost) {
			myHttpPost = new MyHttpPost();
		}
		
		return myHttpPost;
	}
	

	@SuppressWarnings("unchecked")
	public <T> String httpPost(String url, List<T> parameters) {

		try {
			HttpClient mHttpClient = new DefaultHttpClient();

			HttpConnectionParams.setConnectionTimeout(mHttpClient.getParams(),
					10 * 1000);
			/* 请求超时 */
			HttpConnectionParams.setSoTimeout(mHttpClient.getParams(), 10 * 1000);

			HttpPost httpPost = new HttpPost(url);

			httpPost.setEntity(new UrlEncodedFormEntity(
					(List<? extends NameValuePair>) parameters, "utf-8"));
			// 敲回车
			HttpResponse response = mHttpClient.execute(httpPost);
			// 得到状态码
			int code = response.getStatusLine().getStatusCode();
			LogTest.LogMsg("MyHttpPost", "code "+code);
			if (code == 200) {

				InputStream is = response.getEntity().getContent();
				String text = StreamUtil.readInputStream(is);
				return text;
			} else if (code == 408) {
				return "408";
			} else {
				return null;
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
}
