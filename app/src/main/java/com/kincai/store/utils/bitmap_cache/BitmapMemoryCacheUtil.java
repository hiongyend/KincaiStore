package com.kincai.store.utils.bitmap_cache;

import com.kincai.store.utils.LogTest;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 *
 * @author kincai
 * 
 * @description 内存缓存工具类
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.utils.bitmap_cache
 *
 * @time 2015-6-29 上午7:42:36
 *
 */
public class BitmapMemoryCacheUtil {
	
	private static final String TAG = "BitmapMemoryCacheUtil";
	/** 缓存Image的类，当存储Image的大小大于LruCache设定的值，系统自动释放内存*/
	private LruCache<String, Bitmap> mCache;
	public BitmapMemoryCacheUtil() {
		
		int maxCacheSize = (int) Runtime.getRuntime().maxMemory();
		int cacheSize = maxCacheSize / 8;
		mCache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap value) {
				// TODO Auto-generated method stub
//				value.getByteCount();//不兼容低版本 使用下面方法 其实getByteCount里面就是这个原理
				return value.getRowBytes() * value.getHeight();

			}
		};
		
	}
	
	/**
	 * 添加图片到内存缓存
	 * @param key
	 * @param bitmap
	 */
	public void addBitmapToCache(String key, Bitmap bitmap) {
		if(getBitmapByCache(key) == null) {
			mCache.put(key, bitmap);
		}
		
	}
	
	/**
	 * 获取内存缓存图片
	 * @param key
	 * @return
	 */
	public Bitmap getBitmapByCache(String key) {
		LogTest.LogMsg(TAG, "-使用内存缓存-");
		return mCache.get(key);
	}
}
