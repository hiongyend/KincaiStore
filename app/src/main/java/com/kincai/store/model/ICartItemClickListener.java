package com.kincai.store.model;

import java.util.List;

import com.kincai.store.bean.CartInfo.CartData;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 *
 * @author kincai
 * 
 * @description 购物车适配器监听
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.model
 *
 * @time 2016-2-1 下午5:03:16
 *
 */
public interface ICartItemClickListener {
	void onComeInStore(String storeId);
	void onComeInProDetail(String pId, String storeId);
	void onCartOneDelete(int gropPosition, int childPosition, String cart_id, String cart_goods_id);
	void onCartSelectNum(int gropPosition, int childPosition, String cart_goods_id, int newNum, int oldNum);
	void onCartSelectCheckBox(List<CartData> datas);
}
