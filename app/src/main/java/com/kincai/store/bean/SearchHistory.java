package com.kincai.store.bean;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 *
 * @author kincai
 * 
 * @description 搜素商品历史记录实体类
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.bean
 *
 * @time 2015-7-24 下午7:53:24
 *
 */
public class SearchHistory {

	private int _id;
	private String content;
	public SearchHistory() {

	}
	public SearchHistory(int _id, String content) {
		super();
		this._id = _id;
		this.content = content;
	}
	public int getId() {
		return _id;
	}
	public void setId(int _id) {
		this._id = _id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	
}
