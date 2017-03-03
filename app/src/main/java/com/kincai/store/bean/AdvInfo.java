package com.kincai.store.bean;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 *
 * @author kincai
 * 
 * @description 广告信息实体类
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.bean
 *
 * @time 2015-7-24 下午7:56:12
 *
 */
public class AdvInfo {
	
	private int _id;
	private String imagePath;
	private String advUrl;
	public AdvInfo() {
		// TODO Auto-generated constructor stub
	}
	
	public AdvInfo(int _id, String imagePath, String advUrl) {
		super();
		this._id = _id;
		this.imagePath = imagePath;
		this.advUrl = advUrl;
	}


	public int getId() {
		return _id;
	}
	public void setId(int _id) {
		this._id = _id;
	}
	
	public String getAdvUrl() {
		return advUrl;
	}

	public void setAdvUrl(String advUrl) {
		this.advUrl = advUrl;
	}

	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	@Override
	public String toString() {
		return "AdvInfo [_id=" + _id + ", imagePath=" + imagePath + "]";
	}
	
	
	
}
