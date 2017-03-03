package com.kincai.store.common;

import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

import com.kincai.store.Constants;
import com.kincai.store.R;
import com.kincai.store.thread.CachedThreadPoolUtils;
import com.kincai.store.thread.HttpGetImgThread;
import com.kincai.store.utils.NetWorkUtil;
import com.kincai.store.utils.bitmap_cache.BitmapLocaFileCacheUtil;
import com.kincai.store.view.custom.CircleImageView;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 * 
 * @author kincai
 * 
 * @description 加载头像
 * 
 * @project Kincai_Store
 * 
 * @package com.kincai.store.common
 * 
 * @time 2015-8-30 下午6:31:37
 * 
 */
public class LoadUserFace {
	public static BitmapLocaFileCacheUtil mFileUtils;

	/**
	 * 加载头像
	 * 
	 * @param context
	 * @param handler
	 * @param facePath
	 * @param userFaceIv
	 */
	public static void userFace(Context context, Handler handler,
			String facePath, CircleImageView userFaceIv) {
		if (mFileUtils == null) {
			mFileUtils = new BitmapLocaFileCacheUtil(context);
		}

		long time = System.currentTimeMillis();
		if (mFileUtils.getFileLastTime(facePath) + 3600 * 24 * 7 * 1000 < time) {
			mFileUtils.deleteFile(facePath);
		}

		if (!mFileUtils.isFileExists(facePath)) {
			if (NetWorkUtil.isNetworkAvailable(context)) {
				CachedThreadPoolUtils.execute(new HttpGetImgThread(handler,
						new StringBuffer().append(Constants.SERVER_URL).append("userFace/")
						.append(facePath).toString(),
						"userface"));
			}
		} else {
			userFaceIv.setImageBitmap(mFileUtils.getBitmap(facePath));
		}
	}

	/**
	 * 获取请求返回的头像bitmap
	 * 
	 * @param context
	 * @param userFace
	 * @param facePath
	 * @param msg
	 */
	public static void getUserFaceResponse(Context context,
			CircleImageView userFace, String facePath, Message msg) {
		if (mFileUtils == null) {
			mFileUtils = new BitmapLocaFileCacheUtil(context);
		}
		Bitmap mMyFaceBitmap = (Bitmap) msg.obj;
		if (mMyFaceBitmap != null) {
			// mIvStartImg.setImageBitmap(mStartBitmap);
			try {
				mFileUtils.savaBitmap(facePath, mMyFaceBitmap);
				userFace.setImageBitmap(mMyFaceBitmap);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			mMyFaceBitmap = BitmapFactory.decodeResource(
					context.getResources(), R.drawable.ic_useraccount_headview);
			userFace.setImageBitmap(mMyFaceBitmap);
		}
	}

}
