package com.kincai.store.view.adapter;

import java.util.ArrayList;
import java.util.List;

import com.kincai.store.R;
import com.kincai.store.bean.AddressInfo;
import com.kincai.store.utils.LogTest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * @description 收货地址adapter
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.view.adapter
 *
 * @time 2015-7-15 下午9:33:07
 *
 */
public class AddressListAdapter extends BaseAdapter {

	private static final String TAG = "AddressListAdapter";
	List<AddressInfo> datas;
	LayoutInflater mInflater;

	public AddressListAdapter(Context context, List<AddressInfo> datas) {
		LogTest.LogMsg(TAG, "AddressListAdapter --");

		if (datas == null) {
			this.datas = new ArrayList<AddressInfo>();
		} else {
			this.datas = datas;
			LogTest.LogMsg(TAG, "datas.size() " + datas.size());

		}

		// LogTest.LogMsg(TAG, "datas.size() "+datas.size());
		// LogTest.LogMsg(TAG, "imagePathList.size() "+imagePathList.size());
		this.mInflater = LayoutInflater.from(context);

	}

	public void onDateChange(List<AddressInfo> datas) {

		if (datas == null) {
			this.datas = new ArrayList<AddressInfo>();
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
			convertView = mInflater.inflate(R.layout.item_address_layout, null);

			holder.consignee = (TextView) convertView
					.findViewById(R.id.address_item_consignee_tv);
			holder.consigneeNumber = (TextView) convertView
					.findViewById(R.id.address_item_consignee_number_tv);
			holder.isDefault = (TextView) convertView
					.findViewById(R.id.address_item_isdefault_tv);
			holder.address = (TextView) convertView
					.findViewById(R.id.address_item_address_tv);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.consignee.setText(datas.get(position).getConsignee());
		holder.consigneeNumber.setText(datas.get(position).getPhoneNum());
		if (datas.get(position).getIsDefault() == 1) {
			holder.isDefault.setVisibility(View.VISIBLE);
			holder.isDefault.setText("[默认]");
		} else {
			holder.isDefault.setVisibility(View.GONE);
		}

		holder.address.setText(datas.get(position).getArea() + " "
				+ datas.get(position).getDetailedAddress());

		return convertView;
	}

	class ViewHolder {
		TextView consignee;
		TextView consigneeNumber;
		TextView isDefault;
		TextView address;
	}

}
