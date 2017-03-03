package com.kincai.store.bean;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 *
 * @author kincai
 * 
 * @description 分类信息实体类
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.bean
 *
 * @time 2015-7-24 下午7:54:56
 *
 */
public class CateInfo {
	
	private int _id;
	private String cName;
	public CateInfo() {
		// TODO Auto-generated constructor stub
	}
	public CateInfo(int _id, String cName) {
		super();
		this._id = _id;
		this.cName = cName;
	}
	public int getId() {
		return _id;
	}
	public void setId(int _id) {
		this._id = _id;
	}
	public String getcName() {
		return cName;
	}
	public void setcName(String cName) {
		this.cName = cName;
	}
	@Override
	public String toString() {
		return "CateInfo [_id=" + _id + ", cName=" + cName + "]";
	}
	
	
	
	
}
