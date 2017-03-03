package com.kincai.store.model;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 *
 * @author kincai
 * 
 * @description 订单列表点击回调接口
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.model
 *
 * @time 2016-1-6 上午10:33:1
 *
 */
public interface IOrderListClickListener {
	
	/** 订单item点击*/
	void onOrderListClick(int position);
}
