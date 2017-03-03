package com.kincai.store.common;

import java.util.ArrayList;
import java.util.List;


import android.os.Message;
import android.view.View;
import android.widget.ImageView;

import com.kincai.store.bean.ProImagePathInfo;
import com.kincai.store.bean.ProInfo;
import com.kincai.store.db.ImageDao;
import com.kincai.store.utils.JsonUtil;
import com.kincai.store.utils.LogTest;
import com.kincai.store.utils.bitmap_cache.MyImageLoader;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 *
 * @author kincai
 * 
 * @description 商品图片公共类
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.common
 *
 * @time 2015-7-24 下午7:51:20
 *
 */
public class ProCommon {

	private static final String TAG = "ProImage";

	/**
	 * 商品和图片路径进行匹配
	 * 
	 * @param allList
	 *            图片
	 * @param searchProList
	 *            查找的商品
	 * @return
	 */
	public static List<ProImagePathInfo> getProImagePath(
			List<ProImagePathInfo> allList, List<ProInfo> searchProList) {

		List<ProImagePathInfo> newProImagePathList = new ArrayList<ProImagePathInfo>();

		if (null != allList && null != searchProList) {
			int searchProListSize = searchProList.size();
			int allListSize = allList.size();
			for (int i = 0; i < searchProListSize; i++) {
				ProImagePathInfo pathInfo = new ProImagePathInfo();
				// Map<String, Object> newMap = new HashMap<String, Object>();
				for (int j = 0; j < allListSize; j++) {
					if (String.valueOf(allList.get(j).getpId()).trim()
							.equals(String.valueOf(searchProList.get(i).getId()).trim())) {
						pathInfo.setAlbumPath(allList.get(j).getAlbumPath());
						newProImagePathList.add(pathInfo);
						// LogTest.LogMsg(TAG, "pid " +
						// allList.get(j).getpId());
						break;
					}
				}
			}

			LogTest.LogMsg(TAG, "筛选 " + allList.size());
			return newProImagePathList;

		} else {
			return null;
		}

	}

	/**
	 * 获得所有商品图片路径
	 * 
	 * @param msg
	 */
	public static List<ProImagePathInfo> getAllProImagePath(Message msg,
			ImageDao imageDao) {
		List<ProImagePathInfo> list = null;
		String response = (String) msg.obj;
		if (response != null) {
			String code = JsonUtil.msgJson("code", response);
			if ("200".equals(code)) {
				list = JsonUtil.getImgPath("data", response);
				if (null != list) {
					return list;
				}
			}
		}
		List<ProImagePathInfo> imagePathInfos = imageDao.getImagePathInfo();
		if (null != imagePathInfos) {
			return imagePathInfos;
		} else {
			return null;
		}

	}

	/**
	 * 保存图片路径
	 * 
	 * @param msg
	 */
	public static void saveImagePath(List<ProImagePathInfo> data,
			ImageDao mImageDao) {
		List<ProImagePathInfo> jsonItems = data;
		if (jsonItems != null) {

			List<ProImagePathInfo> dbList = mImageDao.getImagePathInfo();
			List<ProImagePathInfo> list = new ArrayList<ProImagePathInfo>();
			if (dbList != null) {
				boolean flag = true;
				for (int i = 0; i < jsonItems.size(); i++) {
					ProImagePathInfo proInfo = new ProImagePathInfo();
					for (int j = 0; j < dbList.size(); j++) {
						flag = true;
						if (jsonItems.get(i).getAlbumPath()
								.equals(dbList.get(j).getAlbumPath())) {
							flag = false;
							break;
						}
					}
					if (flag) {
						proInfo.setpId(jsonItems.get(i).getpId());
						proInfo.setAlbumPath(jsonItems.get(i).getAlbumPath());
						list.add(proInfo);
					}
				}
				mImageDao.saveImagePathInfo(list);
			} else {
				mImageDao.saveImagePathInfo(jsonItems);
			}
		}
	}

	/**
	 * 根据商品从服务器获取图片路径
	 * @param mContext
	 * @param hand
	 * @param datas
	 * @return
	 */
//	public static void getProImagePath(Context mContext, Handler hand,
//			List<ProInfo> datas) {
//
//		String str = "";
//		if (null != datas) {
//			int dataSize = datas.size();
//			for (int i = 0; i < dataSize; i++) {
//				if(i == dataSize-1) {
//					str += String.valueOf(datas.get(i).getId());
//				} else {
//					str += String.valueOf(datas.get(i).getId()) + ",";
//				}
//				
//			}
//
//			if (NetWorkUtil.isNetworkAvailable(mContext)) {
//				List<NameValuePair> parameters = new ArrayList<NameValuePair>();
//				parameters.add(new BasicNameValuePair("pIdStr", str));
//				CachedThreadPoolUtils.execute(new HttpPostThread(hand,
//						Constants.API_URL + "appGetImagePathApi.php",
//						parameters, "getImagePath"));
//			} 
//		} 
//
//	}
	
	/**
	 * 从 新获取到的商品添加到已有的商品  (暂时没用)
	 * 
	 * @param allLists
	 * @param list
	 * @return
	 */
	public static List<ProInfo> addProList(List<ProInfo> allLists,
			List<ProInfo> list) {

		if (null == list) {
			return allLists;
		} else {
			int size = list.size();
			for (int i = 0; i < size; i++) {
				allLists.add(list.get(i));
			}

			return allLists;
		}

	}
	
	/**
	 * 从 新获取到的商品路径添加到已有的商品路径 (暂时没用)
	 * 
	 * @param allLists
	 * @param list
	 * @return
	 */
	public static List<ProImagePathInfo> addProImagePathList(
			List<ProImagePathInfo> allLists, List<ProImagePathInfo> list) {
		
		if (null == list) {
			return allLists;
		} else {
			int size = list.size();
			for (int i = 0; i < size; i++) {
				allLists.add(list.get(i));
			}
			
			return allLists;
		}
		
	}
	
	/**
	 * 显示当前屏幕的图片，先会去查找LruCache，LruCache没有就去sd卡或者手机目录查找，在没有就开启线程去下载
	 * 
	 * @param firstVisibleItem
	 * @param visibleItemCount
	 */
	public static void showImage(int start, int end, String[] urls,
			View mListView, MyImageLoader mImageLoad) {
		if (urls != null) {

			try {
				for (int i = start; i < end; i++) {
					String urlS = urls[i];
					final ImageView mImageView = (ImageView) mListView
							.findViewWithTag(urlS);
					LogTest.LogMsg(TAG, "url:" + urlS);
					mImageLoad.loadImage(urlS, mImageView);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
