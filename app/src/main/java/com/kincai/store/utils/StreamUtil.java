package com.kincai.store.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 * 
 * @author kincai
 * 
 * @description 输出流工具类  把流转化为字符串输出
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.utils
 *
 * @time 2015-7-15 下午9:34:38
 *
 */
public class StreamUtil {
	public static String readInputStream(InputStream is) {
		ByteArrayOutputStream bos = null;
		try {
			bos = new ByteArrayOutputStream();
			int len = 0;
			byte[] date = new byte[1024];
			while((len = is.read(date)) != -1) {
				bos.write(date, 0, len);
			}
			
			byte[] result = bos.toByteArray();
			String temp = new String(result);
//			if(temp.contains("utf-8")) {
//				return temp;
//			} else if(temp.contains("gb2312")) {
//				return new String(result,"gb2312");
//			} 
//			else if(temp.contains("gbk")) {
//				return new String(result,"gbk");
//			}
			return temp;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if(bos != null) {
					bos.close();
				}
				if( is!= null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} 
			
			
		}
	}
	
}
