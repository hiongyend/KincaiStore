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
 * @description 地区/县实体类
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.bean
 *
 * @time 2015-7-24 下午7:55:12
 *
 */
public class AreaZoneInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int zone_id;
	private String zone_name;
	private int city_id;
	
	public AreaZoneInfo() {
		// TODO Auto-generated constructor stub
	}

	public AreaZoneInfo(int zone_id, String zone_name, int city_id) {
		super();
		this.zone_id = zone_id;
		this.zone_name = zone_name;
		this.city_id = city_id;
	}

	public int getZone_id() {
		return zone_id;
	}

	public void setZone_id(int zone_id) {
		this.zone_id = zone_id;
	}

	public String getZone_name() {
		return zone_name;
	}

	public void setZone_name(String zone_name) {
		this.zone_name = zone_name;
	}

	public int getCity_id() {
		return city_id;
	}

	public void setCity_id(int city_id) {
		this.city_id = city_id;
	}

	@Override
	public String toString() {
		return "AddressZoneInfo [zone_id=" + zone_id + ", zone_name="
				+ zone_name + ", city_id=" + city_id + "]";
	}
	
	
	
}
