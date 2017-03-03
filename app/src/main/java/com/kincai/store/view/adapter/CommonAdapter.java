package com.kincai.store.view.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 * 
 * @author kincai
 * 
 * @description 抽取的公共adapter
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.view.adapter
 *
 * @time 2015-7-15 下午9:29:54
 *
 */
public abstract class CommonAdapter<T> extends BaseAdapter {

	protected Context mContext;
	protected List<T> mDatas;
	protected LayoutInflater layoutInflater;
	private int mLayoutId;
	int selectedPosition = -1;

	public CommonAdapter(Context context, List<T> datas, int layoutId) {
		this.mContext = context;
		this.mDatas = (datas == null) ? new ArrayList<T>() : datas;
		this.mLayoutId = layoutId;
		layoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return mDatas.size();
	}

	@Override
	public T getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = ViewHolder.get(mContext, position, mLayoutId,
				convertView, parent);
		convert(viewHolder, getItem(position), position);
		
		return viewHolder.getConvertView();

	}

	/**
	 * 刷新列表
	 * 
	 * @param mDatas
	 */
	public void notifyRefresh(List<T> mDatas) {
		this.mDatas = mDatas;
		this.notifyDataSetChanged();

	}

	/**
	 * 刷新列表
	 * 
	 */
	public void notifyRefresh() {
		this.notifyDataSetChanged();

	}

	/**
	 * item选择选中
	 * 
	 * @param position
	 */
	public void setSelectedPosition(int position) {
		this.selectedPosition = position;
	}

	public abstract void convert(ViewHolder viewHolder, T t, int position);
}
