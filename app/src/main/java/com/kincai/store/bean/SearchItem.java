package com.kincai.store.bean;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 *
 * @author kincai
 * 
 * @description 搜索条目实体类
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.bean
 *
 * @time 2015-7-24 下午7:53:08
 *
 */
public class SearchItem {
	private int _id;
	private String item;
	public SearchItem() {
		// TODO Auto-generated constructor stub
	}
	public SearchItem(int _id, String item) {
		super();
		this._id = _id;
		this.item = item;
	}
	public int getId() {
		return _id;
	}
	public void setId(int _id) {
		this._id = _id;
	}
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	
	
}
