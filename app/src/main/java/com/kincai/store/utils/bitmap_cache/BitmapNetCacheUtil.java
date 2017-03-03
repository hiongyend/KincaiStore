package com.kincai.store.utils.bitmap_cache;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Set;

import com.kincai.store.R;
import com.kincai.store.ui.activity.base.BaseActivity;
import com.kincai.store.utils.LogTest;
import com.kincai.store.utils.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 *
 * @author kincai
 * 
 * @description 网络缓存工具类
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.utils.bitmap_cache
 *
 * @time 2015-6-29 上午7:44:28
 *
 */
public class BitmapNetCacheUtil {

	
	private static final String TAG = "BitmapNetCacheUtil";
	/** 内存缓存*/
	private BitmapMemoryCacheUtil mMemoryCacheUtil;
	/** 操作文件相关类对象的引用*/
	private BitmapLocaFileCacheUtil mLocaFileCacheUtil;
	/** 保存下载任务对象*/
	private static Set<MyAsync> mTask;

	private BitmapFactory.Options option;
	private Context mContext;
	private int mCompressBitmapMode;
	public BitmapNetCacheUtil(Context context, BitmapMemoryCacheUtil memoryCacheUtil,
			BitmapLocaFileCacheUtil locaFileCacheUtil, Set<MyAsync> task) {
		mContext = context;
		mMemoryCacheUtil = memoryCacheUtil;
		mLocaFileCacheUtil = locaFileCacheUtil;
		mTask = task;
		mCompressBitmapMode = Utils.accordingToPhoneConfigurationInfoCompressBitmap(context);
		
	}
	
	/**
	 * 下载图片
	 * 
	 * @param url
	 * @return
	 */
	public Bitmap getBitmap(String url) {
		Bitmap bitmap;
		InputStream is = null;
		try {
			LogTest.LogMsg(TAG, "-使用网络缓存-");
			URL url2 = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) url2.openConnection();
			is = new BufferedInputStream(conn.getInputStream());
			
			if(option == null) {
				option = new BitmapFactory.Options();
			}
//			option.inJustDecodeBounds=true; 
//			BitmapFactory.decodeStream(is, null, option);
//			LogTest.LogMsg("aaaaaaaaa", Utils.calculateInSampleSize2(option, 90, 90)+"");
			option.inSampleSize = Utils.getServerBitmapOptionsSampleSize(mContext, option, mCompressBitmapMode, ((BaseActivity)mContext).mSp);
//			option.inSampleSize = Utils.calculateInSampleSize2(option, 90, 90);
			option.inPreferredConfig = Utils.getServerBitmapInPreferredConfig(mContext, mCompressBitmapMode, ((BaseActivity)mContext).mSp);// 设置图片格式
//			LogTest.LogMsg("bbbbbbbb",Utils.getServerBitmapInPreferredConfig(mContext).toString());
//			option.inPurgeable = true;
//			option.inInputShareable = true;
//			option.inJustDecodeBounds=false; 
			bitmap = BitmapFactory.decodeStream(is, null, option);
			
			
			conn.disconnect();
			return bitmap;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return null;
	}
	
	class MyAsync extends AsyncTask<String, Void, Bitmap> {

//		private ImageView mImageView2;
		 String mUrl2;
		 ImageView imageView;

		public MyAsync( String url, final ImageView imageView) {
//			mImageView2 = imageView;
			mUrl2 = url;
			this.imageView = imageView;
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			// TODO Auto-generated method stub
			String url1 = params[0];
			final String subUrl = url1.replaceAll("[^\\w]", "");
			Bitmap bitmap = getBitmap(url1);
			if(bitmap != null) {
				mMemoryCacheUtil.addBitmapToCache(subUrl, bitmap);
				try {
					mLocaFileCacheUtil.savaBitmap(subUrl, bitmap);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
//			ImageView imageView = (ImageView) mListView.findViewWithTag(mUrl2);
			
			if(imageView != null && result != null) {
				imageView.setImageBitmap(result);
			} else if(imageView != null && result == null ){
				imageView.setImageResource(R.drawable.pro_img);
			}
			
//			listene.onImageLoader(result, mUrl2);
			
			mTask.remove(this);
//			if (mUrl2.equals(mImageView2.getTag())) {
//				mImageView2.setImageBitmap(result);
//			}

		}

	}
}
