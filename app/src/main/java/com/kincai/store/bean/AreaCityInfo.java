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
 * @description  地区/市实体类
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.bean
 *
 * @time 2015-7-24 下午7:55:59
 *
 */
public class AreaCityInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int city_id;
	private String city_name;
	private int province_id;
	
	public AreaCityInfo() {
		// TODO Auto-generated constructor stub
	}

	public AreaCityInfo(int city_id, String city_name, int province_id) {
		super();
		this.city_id = city_id;
		this.city_name = city_name;
		this.province_id = province_id;
	}

	public int getCity_id() {
		return city_id;
	}

	public void setCity_id(int city_id) {
		this.city_id = city_id;
	}

	public String getCity_name() {
		return city_name;
	}

	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}

	public int getProvince_id() {
		return province_id;
	}

	public void setProvince_id(int province_id) {
		this.province_id = province_id;
	}

	@Override
	public String toString() {
		return "AddressCityInfo [city_id=" + city_id + ", city_name="
				+ city_name + ", province_id=" + province_id + "]";
	}
	
	
	
}
