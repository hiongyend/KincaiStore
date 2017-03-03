package com.kincai.store.model;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 *
 * @author kincai
 * 
 * @description ListView上拉下拉刷新接口
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.model
 *
 * @time 2015-8-10 下午12:36:10
 *
 */
public interface IReflashListener {
	
	public void onReflash();
	public void onLoadMore();
}
