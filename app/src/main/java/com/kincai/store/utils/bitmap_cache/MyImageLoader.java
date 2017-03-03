package com.kincai.store.utils.bitmap_cache;

import java.util.HashSet;
import java.util.Set;

import com.kincai.store.utils.LogTest;
import com.kincai.store.utils.NetWorkUtil;
import com.kincai.store.utils.bitmap_cache.BitmapNetCacheUtil.MyAsync;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 * 
 * @author kincai
 * 
 * @description 图片下载类
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.utils
 *
 * @time 2015-6-29 上午7:44:28
 *
 */
@SuppressLint("HandlerLeak")
public class MyImageLoader {

	private static final String TAG = "MyImageLoader";
	String mUrl;
	ImageView mImageView;
	/** 保存下载任务对象*/
	private static Set<MyAsync> task;
	
	private Context mContext;
	
	/** 内存缓存*/
	private BitmapMemoryCacheUtil mMemoryCacheUtil;
	/** 操作文件相关类对象的引用*/
	private BitmapLocaFileCacheUtil mLocaFileCacheUtil;
	/** 网络缓存*/
	private BitmapNetCacheUtil mNetCacheUtil;

//	/** 缓存Image的类，当存储Image的大小大于LruCache设定的值，系统自动释放内存*/
//	private LruCache<String, Bitmap> mCache;

	public MyImageLoader( Context mContext) {
		task = new HashSet<MyAsync>();
		mMemoryCacheUtil = new BitmapMemoryCacheUtil();
		mLocaFileCacheUtil = new BitmapLocaFileCacheUtil(mContext);
		mNetCacheUtil = new BitmapNetCacheUtil(mContext, mMemoryCacheUtil, mLocaFileCacheUtil, task);
		
		this.mContext = mContext;
	}

	/**
	 * 获取Bitmap, 内存中没有就去手机或者sd卡中获取，这一步在getView中会调用，比较关键的一步
	 * @param url
	 * @return
	 */
	public Bitmap showCacheBitmap(String url) {
		Bitmap bitmap = mMemoryCacheUtil.getBitmapByCache(url);
		if (bitmap != null) {
			return bitmap;
		} else if (mLocaFileCacheUtil.isFileExists(url)
				&& mLocaFileCacheUtil.getFileSize(url) != 0) {

			long time = System.currentTimeMillis();
			// 不同网络的缓存时间不同
			int cacheTime = NetWorkUtil.isMobile(mContext) ? 3600 * 24 * 2 * 1000
					: 24 * 3600 * 1000;
			if (mLocaFileCacheUtil.getFileLastTime(url) + cacheTime < time) {
				boolean b = mLocaFileCacheUtil.deleteFile(url);
				LogTest.LogMsg(TAG, "删除商品图片 " + b);
			} else {
				// 从手机储存或SD卡获取手机里面获取Bitmap
				bitmap = mLocaFileCacheUtil.getBitmap(url);

				// 将Bitmap 加入内存缓存
				if (bitmap != null) {
					mMemoryCacheUtil.addBitmapToCache(url, bitmap);
				}
			}

			return bitmap;
		}

		return null;
	}
	
	/**
	 * 加载图片
	 * @param url 图片url
	 * @param listene
	 * @return
	 */
	public void loadImage(final String url, ImageView imageView) {
		
			final String subUrl = url.replaceAll("[^\\w]", "");
			Bitmap bitmap = showCacheBitmap(subUrl);
			if(bitmap == null) {
				MyAsync myAsync = mNetCacheUtil.new MyAsync(url,imageView);
				myAsync.execute(url);
				task.add(myAsync);
			} else {
//				return bitmap;
//				ImageView imageView = (ImageView) mListView.findViewWithTag(urlS);
				if(imageView !=null)
				imageView.setImageBitmap(bitmap);
			}
	}
	
	public static void cancelAllTask() {
		// TODO Auto-generated method stub
		if(task != null) {
			for(MyAsync tAsync : task) {
				tAsync.cancel(false);
			}
		}
	}


	
}
