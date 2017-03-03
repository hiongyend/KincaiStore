package com.kincai.store.view.adapter;

import java.util.ArrayList;
import java.util.List;

import com.kincai.store.R;
import com.kincai.store.bean.OrderInfo.OrderData;
import com.kincai.store.bean.OrderInfo.OrderData.OrderGoodsData;
import com.kincai.store.model.IOrderClickListener;
import com.kincai.store.model.IOrderListClickListener;
import com.kincai.store.view.custom.ItemListview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class OrderListAdapter2 extends BaseAdapter {

	private List<OrderData> mDatas;
	private Context mContext;
	
	private IOrderListClickListener iOrderListClickListener;
	private IOrderClickListener iOrderClickListener;
	public OrderListAdapter2(Context context, List<OrderData> datas) {
		this.mContext = context;
		this.mDatas = datas == null ? new ArrayList<OrderData>() : datas;
	}
	
	public void onChange( List<OrderData> datas) {
		this.mDatas = datas == null ? new ArrayList<OrderData>() : datas;
		this.notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		return mDatas.size();
	}

	@Override
	public OrderData getItem(int position) {
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
			convertView = View.inflate(mContext, R.layout.item_order_list_layout, null);
			//rl
			viewHolder.mDaizhifuRl = (RelativeLayout) convertView.findViewById(R.id.order_detail_bottom_daizhifu);
			viewHolder.mDaifahuoRl = (RelativeLayout) convertView.findViewById(R.id.order_detail_bottom_daifahuo);
			viewHolder.mDaishouhuoRl = (RelativeLayout) convertView.findViewById(R.id.order_detail_bottom_daishouhuo);
			viewHolder.mDaiPingjiaRl = (RelativeLayout) convertView.findViewById(R.id.order_detail_bottom_daipingjia);
			viewHolder.mDaiGuanbiRl = (RelativeLayout) convertView.findViewById(R.id.order_detail_bottom_guanbi);
			
			//btn
			viewHolder.mDzhCancelBtn = (Button) convertView.findViewById(R.id.daizhifu_cencel_order_btn);
			viewHolder.mDzhPayBtn = (Button) convertView.findViewById(R.id.daizhifu_pay_btn);
			
			viewHolder.mDfhtuikuanBtn = (Button) convertView.findViewById(R.id.daifahuo_tuikuan_btn);
			viewHolder.mDfhRemindFahuoBtn = (Button) convertView.findViewById(R.id.daifahuo_remind_fahuo_btn);
			
			viewHolder.mDshConfigShBtn = (Button) convertView.findViewById(R.id.daishouhuo_wuliu_btn);
			viewHolder.mDshwuliuBtn = (Button) convertView.findViewById(R.id.daishouhuo_wuliu_btn);
			
			viewHolder.mDpjPingjiaBtn = (Button) convertView.findViewById(R.id.daipingjia_pingjia_btn);
			viewHolder.mDpjwliuBtn = (Button) convertView.findViewById(R.id.daipingjia_wuliu_btn);
			viewHolder.mDpjdeleteBtn = (Button) convertView.findViewById(R.id.daipingjia_delete_order_btn);
			
			viewHolder.mGbDeleteBtn = (Button) convertView.findViewById(R.id.guanbi_delete_order_btn);
			
			
			//tv
			viewHolder.mShangjiaTv = (TextView) convertView.findViewById(R.id.order_list_item_merchants);
			viewHolder.mTradeStateTv = (TextView) convertView.findViewById(R.id.order_list_item_trade_state_tv);
			viewHolder.mOrderItemListTv = (TextView) convertView.findViewById(R.id.order_list_item_num);
			
			
			//lv
			viewHolder.mItemListview = (ItemListview) convertView.findViewById(R.id.item_order_list_lv);
			
			
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		
		OrderData orderData = mDatas.get(position);
		
		// '交易状态 0待付款 1代发货 2待收货 3交易成功 4交易关闭',
		String state = "";
		int flag = 0;
		switch (Integer.parseInt(orderData.getTrade_state())) {
		case 0:
			state = "等待买家付款";
			flag = 0;
			break;
		case 1:
			state = "等待商家发货";
			flag = 1;
			break;
		case 2:
			state = "等待收货";
			flag = 2;
			break;
		case 3:
			state = "交易成功";
			if(Integer.parseInt(orderData.getEvaluateState()) == 1) {
				flag = 3;//待评价
			} else if (Integer.parseInt(orderData.getEvaluateState()) == 2) {
				flag = 5;//已评价
			}
			
			break;
		case 4:
			state = "交易关闭";
			flag = 4;
			break;
		}

		viewHolder.mShangjiaTv.setText(orderData.getStore_name());
		viewHolder.mTradeStateTv.setText(state);
		List<OrderGoodsData> order_goods = orderData.getOrder_goods();
		int size = order_goods.size();
		int goods_num= 0;
		for (int i = 0; i < size; i++) {
			goods_num += Integer.parseInt(order_goods.get(i).getGoods_num());
			
		}
		viewHolder.mOrderItemListTv.setText(new StringBuilder().append("共计")
				.append(goods_num).append("件商品 ")
				.append("合计：¥").append(orderData.getTotal_fee())
				.append("（含运费：¥").append(orderData.getYunfei()).append("）").toString());
		
		setVisibility(viewHolder, flag);
		
		
		viewHolder.mItemListview.setAdapter(new OrderListChildAdapter(mContext, orderData.getOrder_goods()));
		
		viewHolder.mShangjiaTv.setOnClickListener(new MyOnClickListener(position));
		viewHolder.mItemListview.setOnItemClickListener(new MyOnItemlvClickListener(position));
		viewHolder.mDzhCancelBtn.setOnClickListener(new MyOnClickListener(position));
		viewHolder.mDzhPayBtn.setOnClickListener(new MyOnClickListener(position));
		viewHolder.mDfhRemindFahuoBtn.setOnClickListener(new MyOnClickListener(position));
		viewHolder.mDfhtuikuanBtn.setOnClickListener(new MyOnClickListener(position));
		
		viewHolder.mDshConfigShBtn.setOnClickListener(new MyOnClickListener(position));
		viewHolder.mDshwuliuBtn.setOnClickListener(new MyOnClickListener(position));
		viewHolder.mDpjPingjiaBtn.setOnClickListener(new MyOnClickListener(position));
		viewHolder.mDpjwliuBtn.setOnClickListener(new MyOnClickListener(position));
		
		viewHolder.mDpjdeleteBtn.setOnClickListener(new MyOnClickListener(position));
		viewHolder.mGbDeleteBtn.setOnClickListener(new MyOnClickListener(position));
		
		return convertView;
	}
	class ViewHolder {
		TextView mShangjiaTv,mTradeStateTv,mOrderItemListTv;
		ItemListview mItemListview;
		RelativeLayout mDaizhifuRl,mDaifahuoRl,mDaishouhuoRl,mDaiPingjiaRl,mDaiGuanbiRl;
		Button mDzhCancelBtn,mDzhPayBtn,mDfhtuikuanBtn,mDfhRemindFahuoBtn,mDshConfigShBtn,mDshwuliuBtn
		,mDpjPingjiaBtn,mDpjwliuBtn,mDpjdeleteBtn,mGbDeleteBtn;
		
	}
	
	class MyOnItemlvClickListener implements OnItemClickListener {

		int position;
		public MyOnItemlvClickListener(int position) {
			this.position = position;
		}
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			
			if(iOrderListClickListener != null) {
				iOrderListClickListener.onOrderListClick(position);
			}
			
		}
		
	}
	
	class MyOnClickListener implements OnClickListener {
		int position;
		public MyOnClickListener(int position) {
			this.position = position;
		}
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.daizhifu_pay_btn:
				Toast.makeText(mContext, "daizhifu_pay_btn"+position, Toast.LENGTH_SHORT).show();
				if(iOrderClickListener != null) {
					iOrderClickListener.onOrderClick(position, "daizhifu_pay_btn");
				}
				break;
			case R.id.daizhifu_cencel_order_btn:
				Toast.makeText(mContext, "daizhifu_cencel_order_btn"+position, Toast.LENGTH_SHORT).show();
				if(iOrderClickListener != null) {
					iOrderClickListener.onOrderClick(position, "daizhifu_cencel_order_btn");
				}
				break;
			case R.id.daifahuo_tuikuan_btn:
				Toast.makeText(mContext, "daifahuo_tuikuan_btn"+position, Toast.LENGTH_SHORT).show();
				if(iOrderClickListener != null) {
					iOrderClickListener.onOrderClick(position, "daifahuo_tuikuan_btn");
				}
				break;
			case R.id.daifahuo_remind_fahuo_btn:
				Toast.makeText(mContext, "daifahuo_remind_fahuo_btn"+position, Toast.LENGTH_SHORT).show();
				if(iOrderClickListener != null) {
					iOrderClickListener.onOrderClick(position, "daifahuo_remind_fahuo_btn");
				}
				break;
			case R.id.daishouhuo_confirm_receive_btn:
				Toast.makeText(mContext, "daishouhuo_confirm_receive_btn"+position, Toast.LENGTH_SHORT).show();
				if(iOrderClickListener != null) {
					iOrderClickListener.onOrderClick(position, "daishouhuo_confirm_receive_btn");
				}
				break;
			case R.id.daishouhuo_wuliu_btn:
				Toast.makeText(mContext, "daishouhuo_wuliu_btn"+position, Toast.LENGTH_SHORT).show();
				if(iOrderClickListener != null) {
					iOrderClickListener.onOrderClick(position, "wuliu_btn");
				}
				break;
			case R.id.daipingjia_delete_order_btn:
				Toast.makeText(mContext, "daipingjia_delete_order_btn"+position, Toast.LENGTH_SHORT).show();
				if(iOrderClickListener != null) {
					iOrderClickListener.onOrderClick(position, "delete_order_btn");
				}
				break;
			case R.id.daipingjia_pingjia_btn:
				Toast.makeText(mContext, "daipingjia_pingjia_btn"+position, Toast.LENGTH_SHORT).show();
				if(iOrderClickListener != null) {
					iOrderClickListener.onOrderClick(position, "daipingjia_pingjia_btn");
				}
				break;
			case R.id.daipingjia_wuliu_btn:
				Toast.makeText(mContext, "daipingjia_wuliu_btn"+position, Toast.LENGTH_SHORT).show();
				if(iOrderClickListener != null) {
					iOrderClickListener.onOrderClick(position, "wuliu_btn");
				}
				break;
			case R.id.guanbi_delete_order_btn:
				Toast.makeText(mContext, "guanbi_delete_order_btn"+position, Toast.LENGTH_SHORT).show();
				if(iOrderClickListener != null) {
					iOrderClickListener.onOrderClick(position, "delete_order_btn");
				}
				break;
			/*case R.id.item_order_list_lv:
				
				Toast.makeText(mContext, "order_list_item_"+position, Toast.LENGTH_SHORT).show();
				break;*/
			case R.id.order_list_item_merchants:
				Toast.makeText(mContext, "商家被点击 "+position, Toast.LENGTH_SHORT).show();
				if(iOrderClickListener != null) {
					iOrderClickListener.onOrderClick(position, "shangjia_order_btn");
				}
				
				break;
				
			}
		}
	}
	
	private void setVisibility(ViewHolder viewHolder, int flag) {
		viewHolder.mDaizhifuRl.setVisibility(flag == 0 ? View.VISIBLE : View.GONE);
		viewHolder.mDaifahuoRl.setVisibility(flag == 1 ? View.VISIBLE : View.GONE);
		viewHolder.mDaishouhuoRl.setVisibility(flag == 2 ? View.VISIBLE : View.GONE);
		viewHolder.mDaiPingjiaRl.setVisibility(flag == 3 ? View.VISIBLE : View.GONE);
		viewHolder.mDaiPingjiaRl.setVisibility( flag == 5 ? View.VISIBLE : View.GONE);
		viewHolder.mDpjPingjiaBtn.setVisibility(flag == 3 ? View.VISIBLE : View.GONE);
		viewHolder.mDpjPingjiaBtn.setVisibility(flag == 5 ? View.GONE : View.VISIBLE);
		viewHolder.mDaiGuanbiRl.setVisibility(flag == 4 ? View.VISIBLE : View.GONE);
		
		
//		viewHolder.setViewVisibility(flag == 1 , R.id.order_detail_bottom_daifahuo);
//		viewHolder.setViewVisibility(flag == 2 , R.id.order_detail_bottom_daishouhuo);
//		viewHolder.setViewVisibility(flag == 3 , R.id.order_detail_bottom_daipingjia, R.id.daipingjia_pingjia_btn);
//		viewHolder.setViewVisibility(flag == 4 , R.id.order_detail_bottom_guanbi);
//		if(flag == 5) {
//			viewHolder.setViewVisibility(true , R.id.order_detail_bottom_daipingjia);
//			viewHolder.setViewVisibility(false, R.id.daipingjia_pingjia_btn);
//		}
		
		
	}
	
	
	public void setOnOrderListClickListener(IOrderListClickListener iOrderListClickListener) {
		this.iOrderListClickListener = iOrderListClickListener;
		
	}
	
	public void setOnOrderClickListener(IOrderClickListener iOrderClickListener) {
		this.iOrderClickListener = iOrderClickListener;
		
	}

}
