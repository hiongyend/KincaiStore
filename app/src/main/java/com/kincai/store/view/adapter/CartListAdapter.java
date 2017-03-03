package com.kincai.store.view.adapter;

import java.util.ArrayList;
import java.util.List;


import com.kincai.store.R;
import com.kincai.store.bean.CartInfo.CartData;
import com.kincai.store.bean.CartInfo.CartData.GoodsData;
import com.kincai.store.model.ICartItemClickListener;
import com.kincai.store.model.IDialog;
import com.kincai.store.ui.activity.base.BaseActivity;
import com.kincai.store.utils.Utils;
import com.kincai.store.utils.bitmap_cache.MyImageLoader;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CartListAdapter extends BaseExpandableListAdapter {
	
	private Context mContext;
	private List<CartData> mGroupData;
	private MyImageLoader mImageLoad;
	private String path;
//	private boolean isEditState;
	private ICartItemClickListener iCartItemClickListener;
	private boolean isAllEdit;
	
	public CartListAdapter(Context context, List<CartData> datas) {
	
		mContext = context;
		path = Utils.getServerImagePath(mContext, ((BaseActivity)mContext).mSp);
		mGroupData = (datas == null) ? new ArrayList<CartData>() : datas;
		mImageLoad = ((BaseActivity)mContext).mImageLoader;
	}
	
	public void onDateChange(List<CartData> datas) {
		path = Utils.getServerImagePath(mContext, ((BaseActivity)mContext).mSp);
		mGroupData = (datas == null) ? new ArrayList<CartData>() : datas;
		mImageLoad = ((BaseActivity)mContext).mImageLoader;
		this.notifyDataSetChanged();
	}

	@Override
	public int getGroupCount() {
		return mGroupData.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return mGroupData.get(groupPosition).getGoods_data().size();
	}

	@Override
	public CartData getGroup(int groupPosition) {
		return mGroupData.get(groupPosition);
	}

	@Override
	public GoodsData getChild(int groupPosition, int childPosition) {
		return mGroupData.get(groupPosition).getGoods_data().get(childPosition);
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
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getGroupView(final int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		GroupViewHolder groupViewHolder;
		if(convertView == null) {
			groupViewHolder = new GroupViewHolder();
			convertView = View.inflate(mContext, R.layout.item_cart_group_listview_layout, null);
			groupViewHolder.mGroupCbLl = (LinearLayout) convertView.findViewById(R.id.cart_item_group_pro_cb_ll);
			groupViewHolder.mGroupCbIv = (ImageView) convertView.findViewById(R.id.cart_item_group_cb_iv);
			groupViewHolder.mMerchants = (TextView) convertView.findViewById(R.id.cart_item_merchants_tv);
			groupViewHolder.mGroupEdit = (TextView) convertView.findViewById(R.id.cart_item_group_edit_tv);
			groupViewHolder.view1 = convertView.findViewById(R.id.view1);
			
			convertView.setTag(groupViewHolder);
		} else {
			
			groupViewHolder = (GroupViewHolder) convertView.getTag();
		}
		
		
		groupViewHolder.mGroupEdit.setVisibility(isAllEdit ? View.GONE : View.VISIBLE);
		groupViewHolder.view1.setVisibility(isAllEdit ? View.GONE : View.VISIBLE);
		
		final boolean isEdit = mGroupData.get(groupPosition).getIsGroupEdit();
		final boolean isGroupCheck = mGroupData.get(groupPosition).getIsGroupCheck();
		groupViewHolder.mGroupEdit.setText(isEdit ? "完成" : "编辑");
		groupViewHolder.mMerchants.setText(mGroupData.get(groupPosition).getStore_name());
		
		groupViewHolder.mGroupCbIv.setImageResource(isGroupCheck ? R.drawable.ic_selltip_checkbox_check : R.drawable.ic_selltip_checkbox_normal);
		
		//店铺
		groupViewHolder.mMerchants.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				System.out.println("group.getStore_id()---- : "+groupPosition+"  "+mGroupData.get(groupPosition).getStore_id());
				if(iCartItemClickListener != null) {
					iCartItemClickListener.onComeInStore(mGroupData.get(groupPosition).getStore_id());
				}
			}
		});
		
		//编辑
		groupViewHolder.mGroupEdit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mGroupData.get(groupPosition).setGroupEdit(isEdit ? false : true);
				
				notifyDataSetChanged();
				
			}
			
		});
		
		//选择
		groupViewHolder.mGroupCbLl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				System.out.println("mGroupCbLl --- "+groupPosition);
				
				mGroupData.get(groupPosition).setIsGroupCheck(!isGroupCheck);
				setItemAllChildisCheck(groupPosition);
				notifyDataSetChanged();
				
				if(iCartItemClickListener != null) {
					iCartItemClickListener.onCartSelectCheckBox(mGroupData);
				}
			}
		});
		//group itemview点击 优先消费事件 防止点击时折叠或展开chileview
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});
		
		return convertView;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		final ChildViewHodler childViewHodler;
		if(convertView == null) {
			childViewHodler = new ChildViewHodler();
			
			convertView = View.inflate(mContext, R.layout.item_cart_pro_listview_layout, null);
			childViewHodler.mChildCbIvLl= (LinearLayout) convertView.findViewById(R.id.cart_item_pro_cb_ll);
			childViewHodler.mChildCbIv = (ImageView) convertView.findViewById(R.id.cart_item_pro_cb_iv);
			childViewHodler.mProIv = (ImageView) convertView.findViewById(R.id.cart_pro_iv);
			childViewHodler.mRl = (RelativeLayout) convertView.findViewById(R.id.cart_pro_item_rl);
			childViewHodler.mEditLl = (LinearLayout) convertView.findViewById(R.id.cart_pro_item_ll);
			childViewHodler.mChildEditDeleteBtn = (Button) convertView.findViewById(R.id.cart_pro_item_edit_delete_btn);
			
			childViewHodler.mChildAttr = (TextView) convertView.findViewById(R.id.cart_pro_item_select_attribute);
			childViewHodler.mChildEditAttr = (TextView) convertView.findViewById(R.id.cart_pro_item_edit_attr_tv);
			childViewHodler.mChildEditJia = (TextView) convertView.findViewById(R.id.cart_pro_item_edit_jia_tv);
			childViewHodler.mChildEditJian = (TextView) convertView.findViewById(R.id.cart_pro_item_edit_jian_tv);
			childViewHodler.mChildEditSelectNum = (TextView) convertView.findViewById(R.id.cart_pro_item_edit_yunsuanjieguo_num_tv);
			childViewHodler.mChildMprice = (TextView) convertView.findViewById(R.id.cart_pro_item_mprice_tv);
			childViewHodler.mChildYuanPrice = (TextView) convertView.findViewById(R.id.cart_pro_item_yuanprice_tv);
			childViewHodler.mChildProName = (TextView) convertView.findViewById(R.id.cart_pro_item_name_tv);
			childViewHodler.mChildSelectNum = (TextView) convertView.findViewById(R.id.cart_pro_item_select_num_tv);
			
			convertView.setTag(childViewHodler);
		} else {
			
			childViewHodler = (ChildViewHodler) convertView.getTag();
		}
		
		
		childViewHodler.mChildEditDeleteBtn.setVisibility(isAllEdit ? View.GONE : View.VISIBLE);
		
		final  boolean isChildCheck = mGroupData.get(groupPosition).getGoods_data().get(childPosition).getIsChildCheck();
		String url = new StringBuilder().append(path).append(mGroupData.get(groupPosition).getGoods_data().get(childPosition).getGoods_img_url()).toString();
		childViewHodler.mProIv.setTag(url);
		mImageLoad.loadImage(url, childViewHodler.mProIv);
		
		childViewHodler.mChildCbIv.setImageResource(isChildCheck ? R.drawable.ic_selltip_checkbox_check : R.drawable.ic_selltip_checkbox_normal);
		
		//编辑前
		childViewHodler.mChildAttr.setText(mGroupData.get(groupPosition).getGoods_data().get(childPosition).getGoods_attr());
		childViewHodler.mChildMprice.setText(new StringBuilder().append("¥").append(mGroupData.get(groupPosition).getGoods_data().get(childPosition).getIPrice()));
		childViewHodler.mChildYuanPrice.setText(new StringBuilder().append("¥").append(mGroupData.get(groupPosition).getGoods_data().get(childPosition).getMPrice()));
		childViewHodler.mChildProName.setText(mGroupData.get(groupPosition).getGoods_data().get(childPosition).getPName());
		childViewHodler.mChildSelectNum.setText(new StringBuilder().append("x").append(mGroupData.get(groupPosition).getGoods_data().get(childPosition).getGoods_num()));
		
		boolean isGroupEdit = mGroupData.get(groupPosition).getIsGroupEdit();
		
		childViewHodler.mRl.setVisibility(isGroupEdit ? View.GONE : View.VISIBLE);
		childViewHodler.mEditLl.setVisibility(isGroupEdit ? View.VISIBLE : View.GONE);
		
		//编辑后
		childViewHodler.mChildEditSelectNum.setText(mGroupData.get(groupPosition).getGoods_data().get(childPosition).getGoods_num());
		childViewHodler.mChildEditAttr.setText(mGroupData.get(groupPosition).getGoods_data().get(childPosition).getGoods_attr());
		childViewHodler.mChildEditJian.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int  goods_num = Integer.parseInt((mGroupData.get(groupPosition).getGoods_data().get(childPosition).getGoods_num()));
				if(goods_num > 1) {
					goods_num -= 1;
					mGroupData.get(groupPosition).getGoods_data().get(childPosition).setGoods_num(String.valueOf(goods_num));
					childViewHodler.mChildEditSelectNum.setText(String.valueOf(goods_num));
					if(mGroupData.get(groupPosition).getGoods_data().get(childPosition).getIsChildCheck() && iCartItemClickListener != null) {
						iCartItemClickListener.onCartSelectCheckBox(mGroupData);
					}
					
					if(iCartItemClickListener != null) {
						iCartItemClickListener.onCartSelectNum(groupPosition, childPosition,
								mGroupData.get(groupPosition).getGoods_data().get(childPosition).getGoods_id(), goods_num ,(goods_num+1));
					}
				} else {
					Utils.showToast(mContext, "亲受不了了，不能再减了！", 0);
				}
			}
		});
		childViewHodler.mChildEditJia.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int  goods_num = Integer.parseInt((mGroupData.get(groupPosition).getGoods_data().get(childPosition).getGoods_num()));
				if(goods_num < Integer.parseInt(mGroupData.get(groupPosition).getGoods_data().get(childPosition).getPNum())) {
					goods_num += 1;
					mGroupData.get(groupPosition).getGoods_data().get(childPosition).setGoods_num(String.valueOf(goods_num));
					childViewHodler.mChildEditSelectNum.setText(String.valueOf(goods_num));
					if(mGroupData.get(groupPosition).getGoods_data().get(childPosition).getIsChildCheck() && iCartItemClickListener != null) {
						iCartItemClickListener.onCartSelectCheckBox(mGroupData);
					}
					if(iCartItemClickListener != null) {
						iCartItemClickListener.onCartSelectNum(groupPosition, childPosition,
								mGroupData.get(groupPosition).getGoods_data().get(childPosition).getGoods_id(), goods_num, (goods_num-1));
					}
				} else {
					Utils.showToast(mContext, "亲受不了了，人家仓库的货都不够了！", 0);
				}
			}
		});
		final IDialog iDialog = new IDialog() {
			
			@Override
			public void onDialogClick() {
				System.out.println(" --========--- "+groupPosition+" "+childPosition+" "+ mGroupData.get(groupPosition).getCart_id()+" "+
								mGroupData.get(groupPosition).getGoods_data().get(childPosition).getGoods_id());
				
				if(iCartItemClickListener != null) {
					iCartItemClickListener.onCartOneDelete(groupPosition, childPosition, mGroupData.get(groupPosition).getCart_id(), 
								mGroupData.get(groupPosition).getGoods_data().get(childPosition).getGoods_id());
				}
			}
		};
		
		childViewHodler.mChildEditDeleteBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				System.out.println("delete--- : "+groupPosition+"  "+childPosition);
				Utils.showMiniDialog(mContext, ((BaseActivity)mContext).mScreenWidth, "确定要删除宝贝？", "取消", "确定", iDialog ,true);
			}
		});
		
		
		
		childViewHodler.mChildCbIvLl.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				mGroupData.get(groupPosition).getGoods_data().get(childPosition).setChildCheck(!isChildCheck);
				
				mGroupData.get(groupPosition).setIsGroupCheck(setGroupCheckFromChild(groupPosition));
				notifyDataSetChanged();
				
				if(iCartItemClickListener != null) {
					iCartItemClickListener.onCartSelectCheckBox(mGroupData);
				}
				System.out.println("mGroupCbLl --- "+groupPosition+" "+childPosition);
			}
		});
		
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				System.out.println("concerView--- : "+groupPosition+"  "+childPosition);
				if(iCartItemClickListener != null) {
					iCartItemClickListener.onComeInProDetail(mGroupData.get(groupPosition).getGoods_data().get(childPosition).getId(), mGroupData.get(groupPosition).getStore_id());
				}
			}
		});
		
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}
	
	class GroupViewHolder {
		LinearLayout mGroupCbLl;
		ImageView mGroupCbIv;
		TextView mMerchants;
		TextView mGroupEdit;
		View view1;
		
		
	}
	
	class ChildViewHodler {
		LinearLayout mChildCbIvLl;
		ImageView mChildCbIv;
		ImageView mProIv;
		TextView mChildProName, mChildAttr, mChildMprice, mChildYuanPrice,mChildSelectNum,
			mChildEditJian,mChildEditJia,mChildEditSelectNum,mChildEditAttr;
		Button mChildEditDeleteBtn;
		RelativeLayout mRl;
		LinearLayout mEditLl;
	}

	public void setiOnCartItemClickListener(ICartItemClickListener iCartItemClickListener) {
		this.iCartItemClickListener = iCartItemClickListener;
	}
	
	/**
	 * 根据父级选中状态改变子级是否选中
	 * @param groupPosition
	 */
	private void setItemAllChildisCheck(int groupPosition) {
		List<GoodsData> goods_data = mGroupData.get(groupPosition).getGoods_data();
		int size = goods_data.size();
		for (int i = 0; i < size; i++) {
			mGroupData.get(groupPosition).getGoods_data().get(i).setChildCheck(mGroupData.get(groupPosition).getIsGroupCheck() ? true : false);
		}
		
	}
	
	/**
	 * 设置当前组是否选中 根据子级是否选中
	 * @param groupPosition
	 * @return
	 */
	private boolean setGroupCheckFromChild(int groupPosition) {
		
		List<GoodsData> goods_data = mGroupData.get(groupPosition).getGoods_data();
		int size = goods_data.size();
		for (int i = 0; i < size; i++) {
			if(!goods_data.get(i).getIsChildCheck()) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * 全选或非全选
	 * @param isCheck
	 */
	public void setAllCheck(boolean isCheck) {

		int size = mGroupData.size();
		for (int i = 0; i < size; i++) {
			mGroupData.get(i).setIsGroupCheck(isCheck);
			int size2 = mGroupData.get(i).getGoods_data().size();
			for (int j = 0; j < size2; j++) {
				mGroupData.get(i).getGoods_data().get(j).setChildCheck(isCheck);
			}
		}
		notifyDataSetChanged();
		
		if(iCartItemClickListener != null) {
			iCartItemClickListener.onCartSelectCheckBox(mGroupData);
		}
	}
	
	private void setGroupNotEdit(boolean isEdit) {
		int size = mGroupData.size();
		for (int i = 0; i < size; i++) {
			mGroupData.get(i).setGroupEdit(isEdit);
		}
		
	}
	
	public void setAllEdit(boolean isAllEdit) {
		this.isAllEdit = isAllEdit;
		setGroupNotEdit(isAllEdit);
		notifyDataSetChanged();
	}
	
	public void setCartEditSelectNumIsSuccess(int groupPosition, int childPosition, boolean isSuccess, int oldNum) {

		if(!isSuccess) {//不成功才要恢复之前数量
			mGroupData.get(groupPosition).getGoods_data().get(childPosition).setGoods_num(String.valueOf(oldNum));
			notifyDataSetChanged();
		}
		
	}
	
	public void setCartEditDeleteOneIsSuccess(int groupPosition, int childPosition, boolean isSuccess) {

		if(isSuccess) {//成功才删除 移除一条 
			mGroupData.get(groupPosition).getGoods_data().remove(childPosition);
			mGroupData.get(groupPosition).setCart_num(String.valueOf(Integer.parseInt(mGroupData.get(groupPosition).getCart_num())-1));
			if(mGroupData.get(groupPosition).getGoods_data().size()==0) {
				mGroupData.remove(groupPosition);
			}
			
			if(mGroupData != null && mGroupData.size() == 0) {
				// TODO通知界面显示无数据
			}
			notifyDataSetChanged();
		}
	}
	
	public void setCartEditDeleteSelectIsSuccess(String goodsIdArr, boolean isSuccess) {
		if(isSuccess) {
			String[] goodsId = goodsIdArr.split(",");
			int size = mGroupData.size();
			
			for (int k = 0; k < goodsId.length; k++) {
				kincai:
				for (int i = 0; i < size; i++) {
					List<GoodsData> goodsDatas = mGroupData.get(i).getGoods_data();
					int size2 = goodsDatas.size();
					
					for (int j = 0; j < size2; j++) {
						
						if(goodsDatas.get(j).getGoods_id().equals(goodsId[k])) {
							mGroupData.get(i).getGoods_data().remove(j);
							if(mGroupData.get(i).getGoods_data().size() == 0){
								mGroupData.remove(i);
							}
							break kincai;
							
						} 
					}
				}
			}
			
			if(mGroupData != null && mGroupData.size() == 0) {
				// TODO通知界面显示无数据
			}
			
			notifyDataSetChanged();
		}
	}
}
