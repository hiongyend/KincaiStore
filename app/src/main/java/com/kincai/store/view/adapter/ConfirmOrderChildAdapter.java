package com.kincai.store.view.adapter;

import java.util.ArrayList;
import java.util.List;

import com.kincai.store.R;
import com.kincai.store.bean.CartInfo.CartData.GoodsData;
import com.kincai.store.ui.activity.base.BaseActivity;
import com.kincai.store.utils.Utils;
import com.kincai.store.utils.bitmap_cache.MyImageLoader;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ConfirmOrderChildAdapter extends BaseAdapter {

	private Context mContext;
	private List<GoodsData> datas;
	private String path;
	private MyImageLoader mImageLoader;
	public ConfirmOrderChildAdapter(Context mContext, List<GoodsData> datas) {
		this.mContext = mContext;
		this.datas = datas;
		
		path = Utils.getServerImagePath(mContext, ((BaseActivity)mContext).mSp);
		mImageLoader = ((BaseActivity)mContext).mImageLoader;
	}
	
	public void onDateChange(List<GoodsData> datas) {

		this.datas = datas == null ? new ArrayList<GoodsData>() : datas;
		path = Utils.getServerImagePath(mContext, ((BaseActivity)mContext).mSp);
		mImageLoader = ((BaseActivity)mContext).mImageLoader;
		this.notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public GoodsData getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder viewHolder;
		if(convertView == null) {
			viewHolder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.item_order_pro1_layout, null);
			viewHolder.num = (TextView) convertView.findViewById(R.id.pro_select_num_tv);
			viewHolder.price = (TextView) convertView.findViewById(R.id.pro_mprice_tv);
			viewHolder.attr = (TextView) convertView.findViewById(R.id.pro_select_attribute);
			viewHolder.name = (TextView) convertView.findViewById(R.id.item_pro_title_tv);
			viewHolder.iv = (ImageView) convertView.findViewById(R.id.pro_iv);
			convertView.setTag(viewHolder);
			
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		GoodsData goodsData = datas.get(position);
		
		String urlString = new StringBuffer().append(path).append(goodsData.getGoods_img_url()).toString();
		viewHolder.iv.setTag(urlString);
		mImageLoader.loadImage(urlString, viewHolder.iv);
		viewHolder.num.setText(new StringBuffer().append("x").append(goodsData.getGoods_num()));
		viewHolder.price.setText(new StringBuffer().append("¥").append(goodsData.getIPrice()));
		viewHolder.attr.setText(goodsData.getGoods_attr());
		viewHolder.name.setText(goodsData.getPName());
		return convertView;
	}
	
	class ViewHolder {
		TextView name, attr,price,num;
		ImageView iv;
		
	}

}
