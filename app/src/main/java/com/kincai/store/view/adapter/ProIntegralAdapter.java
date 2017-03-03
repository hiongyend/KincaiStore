package com.kincai.store.view.adapter;

import java.util.ArrayList;
import java.util.List;

import com.kincai.store.R;
import com.kincai.store.bean.ProInfo;
//import com.kincai.store.model.IImageLoaderListener;
import com.kincai.store.ui.activity.base.BaseActivity;
import com.kincai.store.utils.LogTest;
import com.kincai.store.utils.Utils;
import com.kincai.store.utils.bitmap_cache.MyImageLoader;

import android.content.Context;
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
 * @description 积分商城(用的是自己写的listview)
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.view.adapter
 *
 * @time 2015-7-15 下午9:29:26
 *
 */
public class ProIntegralAdapter extends BaseAdapter{

	private static final String TAG = "MyListAdapter";
	public List<ProInfo> mDatas;
	LayoutInflater mInflater;
	public MyImageLoader mImageLoad;
	private Context mContext;
	private String path;
//	private int mLayoutId;

	/**
	 * 记录是否刚打开程序，用于解决进入程序不滚动屏幕，不会下载图片的问题。
	 * 
	 */

	public ProIntegralAdapter(Context context,
			List<ProInfo> datas) {
		LogTest.LogMsg(TAG, "HomeProListAdapter --");
		mContext = context;
		path = Utils.getServerImagePath(mContext, ((BaseActivity)mContext).mSp);
		if (datas == null) {
			mDatas = new ArrayList<ProInfo>();
		} else {
			mDatas = datas;
			LogTest.LogMsg(TAG, "datas.size() " + datas.size());

		}

		mImageLoad = new MyImageLoader(context);
	}

	public void onDateChange(List<ProInfo> datas) {
		path = Utils.getServerImagePath(mContext, ((BaseActivity)mContext).mSp);
		if (datas == null) {
			mDatas = new ArrayList<ProInfo>();
		} else {
			mDatas = datas;
			LogTest.LogMsg(TAG, "datas.size() " + datas.size());
		}

		mImageLoad = new MyImageLoader(mContext);

		this.notifyDataSetChanged();
	}


	@Override
	public int getCount() {
		return mDatas.size();
	}

	@Override
	public Object getItem(int position) {
		return mDatas.get(position);
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
			convertView = View.inflate(mContext,
					R.layout.item_search_pro_result_listview_layout, null);
			holder.price = (TextView) convertView.findViewById(R.id.price_tv);
			holder.title = (TextView) convertView.findViewById(R.id.title_tv);
			holder.evaluate = (TextView) convertView
					.findViewById(R.id.pay_num_tv);
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.search_pro_iv);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.imageView.setImageResource(R.drawable.pro_img);
		String urlString = "";
		if (mDatas != null && mDatas.size()>0) {
			
			
			LogTest.LogMsg(TAG, "posiition--->"+position);
			if(position < mDatas.size()) {
				urlString = path + mDatas.get(position).getUrl();
			
			}

			holder.imageView.setTag(urlString);

			LogTest.LogMsg(TAG, "傻逼："+urlString);

			mImageLoad.loadImage(urlString, holder.imageView);
			
		}

		if(position < mDatas.size()) {
			holder.price.setText(mDatas.get(position).getIntegral()+"积分");
			
			holder.title.setText(mDatas.get(position).getpName());
			
			holder.evaluate.setText(new StringBuilder(mDatas.get(position).getPaynum()).append("人付款"));
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
