package com.kincai.store.view.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kincai.store.R;
import com.kincai.store.bean.CateInfo;
import com.kincai.store.utils.LogTest;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 * 
 * @author kincai
 * 
 * @description 分类列表适配器
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.view.adapter
 *
 * @time 2015-7-15 下午9:31:50
 *
 */
public class CateLeftMenuListAdapter extends BaseAdapter{

	
	
	private static final String TAG = "AddressListAdapter";
	List<CateInfo> datas;
	LayoutInflater mInflater;

	private int selectedPosition = -1;// 选中的位置  
	public CateLeftMenuListAdapter(Context context, List<CateInfo> datas) {
		LogTest.LogMsg(TAG, "AddressListAdapter --");

		if (datas == null) {
			this.datas = new ArrayList<CateInfo>();
		} else {
			this.datas = datas;
			LogTest.LogMsg(TAG, "datas.size() " + datas.size());

		}

		// LogTest.LogMsg(TAG, "datas.size() "+datas.size());
		// LogTest.LogMsg(TAG, "imagePathList.size() "+imagePathList.size());
		this.mInflater = LayoutInflater.from(context);

	}

	public void onDateChange(List<CateInfo> datas) {

		if (datas == null) {
			this.datas = new ArrayList<CateInfo>();
		} else {
			this.datas = datas;
		}

		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LogTest.LogMsg(TAG, "getView --");
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_cate_menu_listview_layout, null);

			holder.cate = (TextView) convertView
					.findViewById(R.id.cate_item_lv_tv_name);
			holder.cateRelativeLayout = (RelativeLayout) convertView
					.findViewById(R.id.cate_item_ll);
			holder.line = (View) convertView
					.findViewById(R.id.cate_item_line);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		  if (selectedPosition == position) {  
			  holder.cateRelativeLayout.setBackgroundResource(R.color.white);  
			  holder.cate.setEnabled(true);
			  holder.line.setVisibility(View.VISIBLE);
          } else {  
        	  holder.cateRelativeLayout.setBackgroundResource(R.color.listview_default); 
        	  holder.cate.setEnabled(false);
        	  holder.line.setVisibility(View.INVISIBLE);

          }  
		
		 
		holder.cate.setText(datas.get(position).getcName());

		return convertView;
	}

	class ViewHolder {
		TextView cate;
		View line;
		RelativeLayout cateRelativeLayout;
	}
	
	/**
	 * 设置选中哪个item
	 * @param position
	 */
	public void setSelectedPosition(int position) {
		this.selectedPosition = position;
	} 

	
	

	
}
