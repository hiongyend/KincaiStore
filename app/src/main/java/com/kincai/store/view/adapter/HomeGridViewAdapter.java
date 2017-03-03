package com.kincai.store.view.adapter;

import java.util.List;

import com.kincai.store.R;
import com.kincai.store.bean.HomeGridInfo;
import com.kincai.store.ui.activity.base.BaseActivity;
import com.kincai.store.utils.LogTest;
import com.kincai.store.utils.bitmap_cache.MyImageLoader;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
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
 * @description 
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.view.adapter
 *
 * @time 2015-8-16 下午8:31:57
 *
 */
public class HomeGridViewAdapter extends BaseAdapter {

	private static final String TAG = "HomeGridViewAdapter";
	private static MyImageLoader mImageLoad;
	private List<HomeGridInfo> mData;
	public static String[] urls;
	private static Context mContext;

	/** XUtils加载图片工具*/
//	private BitmapUtils mBitmapUtils;//暂时不用

	/**
	 * 记录是否刚打开程序，用于解决进入程序不滚动屏幕，不会下载图片的问题。
	 * 参考http://blog.csdn.net/guolin_blog/article/details/9526203#comments
	 */

	public HomeGridViewAdapter(Context context,GridView gridView,List<HomeGridInfo> data) {

		mContext = context;
		this.mData = data;
		mImageLoad = ((BaseActivity)mContext).mImageLoader;
	}

	public void onDateChange(GridView gridView,List<HomeGridInfo> data) {

		mData = data;
		this.notifyDataSetChanged();
	}


	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LogTest.LogMsg(TAG, "getView --");
		final ViewHolder holder;
		
		LogTest.LogMsg(TAG,"getView "+position );
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(mContext,
					R.layout.item_home_gridview_layout, null);
			holder.title = (TextView) convertView.findViewById(R.id.item_home_gridview_tv);
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.item_home_gridview_iv);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.title.setText(mData.get(position).getTitle());
		
//		mBitmapUtils.display(holder.imageView, mData.get(position).getImageUrl());
		
		
//		holder.imageView.setImageResource(R.drawable.pro_img);
		
		mImageLoad.loadImage(mData.get(position).getImageUrl(), holder.imageView);

		return convertView;
	}

	class ViewHolder {
		ImageView imageView;
		TextView title; 

	}


}
