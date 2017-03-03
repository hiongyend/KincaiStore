package com.kincai.store.view.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kincai.store.R;
import com.kincai.store.bean.StoreNewProInfo.NewProData;
import com.kincai.store.bean.StoreNewProInfo.NewProData.ProData;
import com.kincai.store.ui.activity.base.BaseActivity;
import com.kincai.store.utils.TimeUtil;
import com.kincai.store.utils.Utils;
import com.kincai.store.utils.bitmap_cache.MyImageLoader;

public class StoreNewProAdapter extends BaseExpandableListAdapter {
	private Context mContext;
	private List<NewProData> mGroupData;
	private MyImageLoader mImageLoad;
	private String path;
	
	public StoreNewProAdapter(Context context, List<NewProData> datas) {
	
		mContext = context;
		path = Utils.getServerImagePath(mContext, ((BaseActivity)mContext).mSp);
		mGroupData = (datas == null) ? new ArrayList<NewProData>() : datas;
		mImageLoad = ((BaseActivity)mContext).mImageLoader;
	}
	
	public void onDateChange(List<NewProData> datas) {
		path = Utils.getServerImagePath(mContext, ((BaseActivity)mContext).mSp);
		mGroupData = (datas == null) ? new ArrayList<NewProData>() : datas;
		mImageLoad = ((BaseActivity)mContext).mImageLoader;
		this.notifyDataSetChanged();
	}
	
	@Override
	public int getGroupCount() {
		return mGroupData.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return mGroupData.get(groupPosition).getProData().size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return mGroupData.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return mGroupData.get(groupPosition).getProData().get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		final ViewChildHolder holder;
		if (convertView == null) {
			holder = new ViewChildHolder();
			convertView = View.inflate(mContext, R.layout.item_store_new_pro_group_layout, null);
			holder.title = (TextView) convertView.findViewById(R.id.date);

			convertView.setTag(holder);
		} else {
			holder = (ViewChildHolder) convertView.getTag();
		}
		
		holder.title.setText(TimeUtil.StringLongToString(new StringBuffer().append(mGroupData.get(groupPosition).getTime())
				.append("000").toString(), "yyyy年MM月dd日"));
		
		
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.item_search_pro_result_listview_layout, null);
			holder.price = (TextView) convertView.findViewById(R.id.price_tv);
			holder.title = (TextView) convertView.findViewById(R.id.title_tv);
			holder.payNum = (TextView) convertView
					.findViewById(R.id.pay_num_tv);
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.search_pro_iv);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		ProData proData = mGroupData.get(groupPosition).getProData().get(childPosition);
		String url = new StringBuffer().append(path).append(proData.getUrl()).toString();
		holder.imageView.setImageResource(R.drawable.pro_img);
			
		holder.imageView.setTag(url);
			mImageLoad.loadImage(url, holder.imageView);

		holder.price.setText(new StringBuffer().append("¥ ").append(proData.getIPrice()));
			
		holder.title.setText(proData.getPName());
		holder.payNum.setText(new StringBuilder().append(proData.getPay_num()).append("人付款"));
		
		return convertView;
	}

	class ViewHolder {
		ImageView imageView;
		TextView title;
		TextView price;
		TextView payNum;

	}
	
	class ViewChildHolder {
		TextView title;

	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}
	
	@Override
	public boolean hasStableIds() {
		return false;
	}
}
