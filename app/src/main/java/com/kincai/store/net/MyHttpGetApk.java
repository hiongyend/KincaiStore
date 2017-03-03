package com.kincai.store.net;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;

import com.kincai.store.Constants;

import android.os.Environment;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 * 
 * @author kincai
 * 
 * @description 下载APK 网络请求类
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.net
 *
 * @time 2015-7-22 下午9:05:31
 *
 */
public class MyHttpGetApk {

	@SuppressWarnings("unused")
	public String getApk(String url) {

		try {
			HttpClient client = new DefaultHttpClient();
			client.getParams().setIntParameter(
					HttpConnectionParams.CONNECTION_TIMEOUT, 10 * 1000);
			client.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT,
					10 * 1000);
			HttpGet get = new HttpGet(url);
			HttpResponse response = client.execute(get);

			int code = response.getStatusLine().getStatusCode();
			HttpEntity entity = response.getEntity();
			long length = entity.getContentLength();
			InputStream is = entity.getContent();
			FileOutputStream fileOutputStream = null;
			if (code == 200) {
				if (is != null) {
					File file = new File(
							Environment.getExternalStorageDirectory(),
							Constants.APP_DOWNLOAD_APK_NAME);
					fileOutputStream = new FileOutputStream(file);
					byte[] buf = new byte[1024];
					int ch = -1;
					while ((ch = is.read(buf)) != -1) {
						fileOutputStream.write(buf, 0, ch);
					}

				} 
				fileOutputStream.flush();
				if (fileOutputStream != null) {
					fileOutputStream.close();
				}
				
				return "200";
			} else {
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

}
