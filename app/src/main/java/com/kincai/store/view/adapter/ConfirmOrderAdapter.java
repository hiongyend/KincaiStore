package com.kincai.store.view.adapter;

import java.util.ArrayList;
import java.util.List;

import com.kincai.store.R;
import com.kincai.store.bean.CartInfo.CartData;
import com.kincai.store.view.custom.ItemListview;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

public class ConfirmOrderAdapter extends BaseAdapter {

	private Context mContext;
	private List<CartData> datas;
	public ConfirmOrderAdapter(Context mContext, List<CartData> datas) {
		this.mContext = mContext;
		this.datas = datas;
	}
	
	public void onDateChange(List<CartData> datas) {

		this.datas = datas == null ? new ArrayList<CartData>() : datas;
		this.notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public CartData getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder viewHolder = null;
		if(convertView == null) {
			viewHolder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.item_comfig_order_layout, null);
			viewHolder.maijia = (TextView) convertView.findViewById(R.id.item_confirm_order_merchants);
			viewHolder.yunfei = (TextView) convertView.findViewById(R.id.item_confirm_order_freight_tv);
			viewHolder.price = (TextView) convertView.findViewById(R.id.item_confirm_order_num_and_price);
			viewHolder.msg = (EditText) convertView.findViewById(R.id.item_confirm_order_buyer_msg);
			viewHolder.itemListview = (ItemListview) convertView.findViewById(R.id.item_confirm_order_lv);
			viewHolder.msg.setTag(position);
			convertView.setTag(viewHolder);
			
		} else {
			
			viewHolder = (ViewHolder) convertView.getTag();
			viewHolder.msg.setTag(position);
		}
		
		final ViewHolder mHolder = viewHolder;
		
		final CartData cartData = datas.get(position);
		if(cartData != null) {
			viewHolder.maijia.setText(cartData.getStore_name());
			int size = cartData.getGoods_data().size();
			double price = 0;
			for(int i = 0; i < size; i++) {
				price += (Double.parseDouble(cartData.getGoods_data().get(i).getIPrice())* Double.parseDouble(cartData.getGoods_data().get(i).getGoods_num()));
			}
			viewHolder.yunfei.setText(new StringBuilder().append("¥").append("0.00"));
			viewHolder.price.setText(new StringBuffer().append("共").append(size).append("件商品").append(" 合计")
					.append("¥").append(price));
			viewHolder.itemListview.setAdapter(new ConfirmOrderChildAdapter(mContext, cartData.getGoods_data()));
	
			viewHolder.msg.setText(cartData.getMsg());
			viewHolder.msg.addTextChangedListener(new TextWatcher() {
				
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
					
				}
				
				@Override
				public void afterTextChanged(Editable s) {
					if(s != null && !"".equals(s.toString())) {
						
						int tag = (int) mHolder.msg.getTag();
						datas.get(tag).setMsg(s.toString());
						notifyDataSetChanged();
					}
					
				}
			});
		}
		
		
		
		return convertView;
	}
	
	class ViewHolder {
		TextView maijia,yunfei,price;
		EditText msg;
		ItemListview itemListview;
	}
	
	public List<CartData> getCartDatas() {
		if(datas != null && datas.size() > 0)
			return datas;
		
		return null;
	}

}
