package com.kincai.store.view.adapter;

import java.util.ArrayList;
import java.util.List;

import com.kincai.store.R;
import com.kincai.store.bean.ProImagePathInfo;
import com.kincai.store.bean.ProInfo;
//import com.kincai.store.model.IImageLoaderListener;
import com.kincai.store.ui.activity.base.BaseActivity;
import com.kincai.store.utils.LogTest;
import com.kincai.store.utils.Utils;
import com.kincai.store.utils.bitmap_cache.MyImageLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.BaseAdapter;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 * 
 * @author kincai
 * 
 * @description 今日更新(用的是自己写的listview)
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.view.adapter
 *
 * @time 2015-7-15 下午9:29:26
 *
 */
public class ProTodayAdapter extends BaseAdapter{

	private static final String TAG = "ProTodayAdapter";
	public static List<ProInfo> datas;
	List<ProImagePathInfo> imagePathList;
	LayoutInflater mInflater;
	public static MyImageLoader mImageLoad;
	public static String[] urls;
	private Context mContext;
	private String path;
//	private int mLayoutId;

	/**
	 * 记录是否刚打开程序，用于解决进入程序不滚动屏幕，不会下载图片的问题。
	 * 
	 */

	public ProTodayAdapter(Context context,
			List<ProInfo> datas,
			List<ProImagePathInfo> imagePathList) {
		LogTest.LogMsg(TAG, "HomeProListAdapter --");
		LogTest.LogMsg(TAG, "image收发为空 " + (imagePathList == null));
		mContext = context;
		path = Utils.getServerImagePath(mContext, ((BaseActivity)mContext).mSp);
		if (datas == null) {
			ProTodayAdapter.datas = new ArrayList<ProInfo>();
		} else {
			ProTodayAdapter.datas = datas;
			LogTest.LogMsg(TAG, "datas.size() " + datas.size());

		}

		if (imagePathList == null) {
			this.imagePathList = new ArrayList<ProImagePathInfo>();
		} else if (imagePathList.size() > 0) {
			mImageLoad = new MyImageLoader(context);
			urls = new String[imagePathList.size()];
			for (int i = 0; i < imagePathList.size(); i++) {
				urls[i] = path + imagePathList.get(i).getAlbumPath();
			}
			LogTest.LogMsg(TAG, "imagePathList.size() " + imagePathList.size());
			this.imagePathList = imagePathList;
		} else if (imagePathList.size() == 0) {
			LogTest.LogMsg(TAG, "image size " + imagePathList.size());
			urls = null;
		}
		this.mInflater = LayoutInflater.from(context);
//		this.mLayoutId = layoutId;
	}

	public void onDateChange(List<ProInfo> datas,
			List<ProImagePathInfo> imagePathList) {
		path = Utils.getServerImagePath(mContext, ((BaseActivity)mContext).mSp);
		if (datas == null) {
			ProTodayAdapter.datas = new ArrayList<ProInfo>();
		} else {
			ProTodayAdapter.datas = datas;
			LogTest.LogMsg(TAG, "datas.size() " + datas.size());
		}

		if (imagePathList == null) {
			this.imagePathList = new ArrayList<ProImagePathInfo>();
		} else if (imagePathList.size() > 0) {
			mImageLoad = new MyImageLoader(mContext);
			urls = new String[imagePathList.size()];
			for (int i = 0; i < imagePathList.size(); i++) {
				urls[i] = path + imagePathList.get(i).getAlbumPath().toString();
			}
			this.imagePathList = imagePathList;
		} else if (imagePathList.size() == 0) {
			urls = null;
		}

		this.notifyDataSetChanged();
	}


	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LogTest.LogMsg(TAG, "getView --");
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(
					R.layout.item_search_pro_result_listview_layout, null);
			holder.price = (TextView) convertView.findViewById(R.id.price_tv);
			holder.title = (TextView) convertView.findViewById(R.id.title_tv);
			holder.evaluate = (TextView) convertView
					.findViewById(R.id.evaluate_tv);
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.search_pro_iv);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.imageView.setImageResource(R.drawable.pro_img);
		String urlString = "";
		LogTest.LogMsg(TAG, "vfvv" + (imagePathList == null));
		if (imagePathList != null && datas != null && imagePathList.size()>0 && datas.size()>0) {
			
			
			LogTest.LogMsg(TAG, "posiition--->"+position);
			if(position < imagePathList.size() && position < datas.size()) {
				urlString = path + imagePathList.get(position).getAlbumPath();
			
			}
			

			holder.imageView.setTag(urlString);

			// mImageLoad.showImageTheard(holder.imageView, urlString);
			LogTest.LogMsg(TAG, "傻逼："+urlString);
			final String subUrl = urlString.replaceAll("[^\\w]", "");
			Bitmap bitmap = mImageLoad.showCacheBitmap(subUrl);

			if (bitmap != null) {
				holder.imageView.setImageBitmap(bitmap);
			} else {
				holder.imageView.setImageResource(R.drawable.pro_img);
			}
		}

		// mImageLoad.ShowimageByAsync(urlString, holder.imageView);
		if(position < imagePathList.size() || position < datas.size()) {
			holder.price.setText("¥ "
					+ datas.get(position).getiPrice());
			
			holder.title.setText(datas.get(position).getpName());
//			int count = (int) (100 + Math.random() * (10001));
//			holder.evaluate.setText("评价数 " + count);
			
			holder.evaluate.setText("已售" + datas.get(position).getSaleNum()+"件");
		}
		

		return convertView;
	}

	class ViewHolder {
		ImageView imageView;
		TextView title;
		TextView price;
		TextView evaluate;

	}


}
