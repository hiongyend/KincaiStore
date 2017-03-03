package com.kincai.store.view.adapter;

import java.util.ArrayList;
import java.util.List;

import com.kincai.store.R;
import com.kincai.store.bean.OrderInfo.OrderData;
import com.kincai.store.bean.OrderInfo.OrderData.OrderGoodsData;
import com.kincai.store.ui.activity.ProDetailsActivity;
import com.kincai.store.ui.activity.StoreActivity;
import com.kincai.store.ui.activity.base.BaseActivity;
import com.kincai.store.utils.AnimationUtil;
import com.kincai.store.utils.Utils;
import com.kincai.store.utils.bitmap_cache.MyImageLoader;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2016
 * 
 * @author KINCAI
 * 
 * @description TODO
 * 
 * @project Kincai_Store
 * 
 * @package com.kincai.store.view.adapter
 * 
 * @time 2016-4-29 上午10:13:44
 *
 */
public class OrderDetailAdapter extends BaseExpandableListAdapter {
	private Context mContext;
	private List<OrderData> mGroupData;
	private MyImageLoader mImageLoad;
	private String path;
	
	public OrderDetailAdapter(Context context, List<OrderData> datas) {
	
		mContext = context;
		path = Utils.getServerImagePath(mContext, ((BaseActivity)mContext).mSp);
		mGroupData = (datas == null) ? new ArrayList<OrderData>() : datas;
		mImageLoad = ((BaseActivity)mContext).mImageLoader;
	}
	
	public void onDateChange(List<OrderData> datas) {
		path = Utils.getServerImagePath(mContext, ((BaseActivity)mContext).mSp);
		mGroupData = (datas == null) ? new ArrayList<OrderData>() : datas;
		mImageLoad = ((BaseActivity)mContext).mImageLoader;
		this.notifyDataSetChanged();
	}
	@Override
	public OrderGoodsData getChild(int groupPosition, int childPosition) {
		return mGroupData.get(groupPosition).getOrder_goods().get(groupPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		
		final int child = childPosition;
		final int group = groupPosition;
		ChildViewHolder childViewHolder;
		if(convertView == null) {
			childViewHolder = new ChildViewHolder();
			convertView = View.inflate(mContext, R.layout.item_order_pro3_layout, null);
			childViewHolder.num = (TextView) convertView.findViewById(R.id.pro_select_num_tv);
			childViewHolder.iprice = (TextView) convertView.findViewById(R.id.item_pro_iprice_tv);
			childViewHolder.mprice = (TextView) convertView.findViewById(R.id.item_pro_mprice_tv);
			childViewHolder.attr = (TextView) convertView.findViewById(R.id.pro_select_attribute);
			childViewHolder.name = (TextView) convertView.findViewById(R.id.item_pro_title_tv);
			childViewHolder.iv = (ImageView) convertView.findViewById(R.id.pro_iv);
			convertView.setTag(childViewHolder);
			
		} else {
			childViewHolder = (ChildViewHolder) convertView.getTag();
		}
		OrderGoodsData orderGoodsData = mGroupData.get(groupPosition).getOrder_goods().get(childPosition);
		
		childViewHolder.name.setText(orderGoodsData.getPro_name());
		childViewHolder.iprice.setText(new StringBuffer().append("¥").append(orderGoodsData.getPro_iprice()).toString());
		childViewHolder.mprice.setText(new StringBuffer().append("¥").append(orderGoodsData.getPro_mprice()).toString());
		childViewHolder.attr.setText(new StringBuffer().append(orderGoodsData.getPro_attr()).toString());
		childViewHolder.num.setText(new StringBuffer().append("x").append(orderGoodsData.getGoods_num()).toString());
		
		
		String urls = new StringBuffer().append(path).append(orderGoodsData.getPro_logo_url()).toString();
		mImageLoad.loadImage(urls, childViewHolder.iv);
		childViewHolder.iprice.setText(new StringBuffer().append("¥").append(orderGoodsData.getPro_iprice()).toString());
		
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				mContext.startActivity(new Intent(mContext, ProDetailsActivity.class)
					.putExtra("pId", Integer.parseInt(mGroupData.get(group).getOrder_goods().get(child).getPId()))
					.putExtra("storeId",Integer.parseInt(mGroupData.get(group).getStore_id())));
				AnimationUtil.startHaveSinkActivityAnimation((BaseActivity)mContext);
			}
		});
		
		
		
		return convertView;
		
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return mGroupData.get(groupPosition).getOrder_goods().size();
	}

	@Override
	public OrderData getGroup(int groupPosition) {
		return mGroupData.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return mGroupData.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(final int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		GroupViewHolder groupViewHolder;
		if(convertView == null) {
			groupViewHolder = new GroupViewHolder();
			convertView = View.inflate(mContext, R.layout.item_order_detail_list_group_layout, null);
			groupViewHolder.mShangjia = (TextView) convertView.findViewById(R.id.merchants);
			convertView.setTag(groupViewHolder);
			
			
		} else {
			groupViewHolder = (GroupViewHolder) convertView.getTag();
		}
		
		groupViewHolder.mShangjia.setText(mGroupData.get(groupPosition).getStore_name());
		//group itemview点击 优先消费事件 防止点击时折叠或展开chileview
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mContext.startActivity(new Intent(mContext, StoreActivity.class)
				.putExtra("storeId", mGroupData.get(groupPosition).getStore_id()));
				AnimationUtil.startHaveSinkActivityAnimation((BaseActivity)mContext);
			}
		});
		
		return convertView;
	}
	
	
	class GroupViewHolder{
		TextView mShangjia;
	}
	
	class ChildViewHolder{
		TextView name, attr,iprice, mprice,num;
		ImageView iv;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

}
