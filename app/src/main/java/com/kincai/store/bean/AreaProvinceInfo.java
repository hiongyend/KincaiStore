package com.kincai.store.bean;

import java.io.Serializable;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 *
 * @author kincai
 * 
 * @description  地区/省实体类
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.bean
 *
 * @time 2015-7-24 下午7:55:46
 *
 */
public class AreaProvinceInfo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int province_id;
	private String province_name;
	
	public AreaProvinceInfo() {
		// TODO Auto-generated constructor stub
	}

	public AreaProvinceInfo(int province_id, String province_name) {
		super();
		this.province_id = province_id;
		this.province_name = province_name;
	}

	public int getProvince_id() {
		return province_id;
	}

	public void setProvince_id(int province_id) {
		this.province_id = province_id;
	}

	public String getProvince_name() {
		return province_name;
	}

	public void setProvince_name(String province_name) {
		this.province_name = province_name;
	}

	@Override
	public String toString() {
		return "AddressProvinceInfo [province_id=" + province_id
				+ ", province_name=" + province_name + "]";
	}
	
	
	
	
}
