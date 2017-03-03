package com.kincai.store.view.adapter;

import java.util.ArrayList;
import java.util.List;

import com.kincai.store.R;
import com.kincai.store.bean.OrderInfo.OrderData.OrderGoodsData;
import com.kincai.store.ui.activity.base.BaseActivity;
import com.kincai.store.utils.Utils;
import com.kincai.store.utils.bitmap_cache.MyImageLoader;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class OrderListChildAdapter extends BaseAdapter {

	private Context mContext;
	private List<OrderGoodsData> mDatas;
	private String path;
	private MyImageLoader mImageLoader;
	public OrderListChildAdapter(Context mContext, List<OrderGoodsData> datas) {
		this.mContext = mContext;
		this.mDatas = datas;
		
		path = Utils.getServerImagePath(mContext, ((BaseActivity)mContext).mSp);
		mImageLoader = ((BaseActivity)mContext).mImageLoader;
	}
	
	public void onDateChange(List<OrderGoodsData> datas) {

		this.mDatas = datas == null ? new ArrayList<OrderGoodsData>() : datas;
		path = Utils.getServerImagePath(mContext, ((BaseActivity)mContext).mSp);
		mImageLoader = ((BaseActivity)mContext).mImageLoader;
		this.notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return mDatas.size();
	}

	@Override
	public OrderGoodsData getItem(int position) {
		return mDatas.get(position);
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
			convertView = View.inflate(mContext, R.layout.item_order_pro2_layout, null);
			viewHolder.num = (TextView) convertView.findViewById(R.id.pro_select_num_tv);
			viewHolder.iprice = (TextView) convertView.findViewById(R.id.item_pro_iprice_tv);
			viewHolder.mprice = (TextView) convertView.findViewById(R.id.item_pro_mprice_tv);
			viewHolder.attr = (TextView) convertView.findViewById(R.id.pro_select_attribute);
			viewHolder.name = (TextView) convertView.findViewById(R.id.item_pro_title_tv);
			viewHolder.iv = (ImageView) convertView.findViewById(R.id.pro_iv);
			convertView.setTag(viewHolder);
			
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		OrderGoodsData orderGoodsData = mDatas.get(position);
		
		viewHolder.name.setText(orderGoodsData.getPro_name());
		viewHolder.iprice.setText(new StringBuffer().append("¥").append(orderGoodsData.getPro_iprice()).toString());
		viewHolder.mprice.setText(new StringBuffer().append("¥").append(orderGoodsData.getPro_mprice()).toString());
		viewHolder.attr.setText(new StringBuffer().append(orderGoodsData.getPro_attr()).toString());
		viewHolder.num.setText(new StringBuffer().append("x").append(orderGoodsData.getGoods_num()).toString());
		
		
		String urls = new StringBuffer().append(path).append(orderGoodsData.getPro_logo_url()).toString();
		mImageLoader.loadImage(urls, viewHolder.iv);
		viewHolder.iprice.setText(new StringBuffer().append("¥").append(orderGoodsData.getPro_iprice()).toString());
		
		
		return convertView;
	}
	
	class ViewHolder {
		TextView name, attr,iprice, mprice,num;
		ImageView iv;
		
	}

}
