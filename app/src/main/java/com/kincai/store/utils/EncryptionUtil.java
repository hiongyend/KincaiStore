package com.kincai.store.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 * 
 * @author kincai
 * 
 * @description 加密工具
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.utils
 *
 * @time 2015-6-25 上午7:32:09
 *
 */
public class EncryptionUtil {

	/**
	 * md5加密
	 * 
	 * @param string
	 * @return
	 */
	/*public static String md5Encryption(String string) {

		return DigestUtils.md5Hex(string);

	}*/

	/**
	 * md5加密
	 * @param string
	 * @return
	 * @throws Exception
	 */
	public static String md5Encryption(String string) {
	    try {
			byte[] hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
			StringBuilder hex = new StringBuilder(hash.length * 2);
			for (byte b : hash) {
			    if ((b & 0xFF) < 0x10) {
			    	hex.append("0");
			    }
			    hex.append(Integer.toHexString(b & 0xFF));
			}
			return hex.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	    
	    return null;
	}
	
	/**
	 * sha1加密
	 * 
	 * @param string
	 * @return
	 */
	/*public static String sha1Encryption(String string) {
		return DigestUtils.shaHex(string);
	}

	*//**
	 * base64加密
	 * 
	 * @param string
	 * @return
	 *//*
	public static String base64Encryption(String string) {
		return new String(Base64.encodeBase64(string.getBytes(), true));
	}

	*//**
	 * base64解密
	 * 
	 * @param string
	 * @return
	 *//*
	public static String base64Decode(String string) {
		return new String(Base64.decodeBase64(string.getBytes()));
	}*/

}
