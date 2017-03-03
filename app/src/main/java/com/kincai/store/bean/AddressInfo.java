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
 * @description 收货地址信息
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.bean
 *
 * @time 2015-7-11 下午2:59:27
 *
 */
public class AddressInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int _id;
	private int userId;
	private String consignee;
	private String phoneNum;
	private String postalCode;
	private String area;
	private String detailedAddress;
	private String addTime;
	private int isDefault;
	
	public AddressInfo() {
		// TODO Auto-generated constructor stub
	}
	public AddressInfo(int _id, int userId, String consignee,
			String phoneNum, String postalCode, String area,
			String detailedAddress, String addTime, int isDefault) {
		super();
		this._id = _id;
		this.userId = userId;
		this.consignee = consignee;
		this.phoneNum = phoneNum;
		this.postalCode = postalCode;
		this.area = area;
		this.detailedAddress = detailedAddress;
		this.addTime = addTime;
		this.isDefault = isDefault;
	}
	public int getId() {
		return _id;
	}
	public void setId(int _id) {
		this._id = _id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getConsignee() {
		return consignee;
	}
	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getDetailedAddress() {
		return detailedAddress;
	}
	public void setDetailedAddress(String detailedAddress) {
		this.detailedAddress = detailedAddress;
	}
	public String getAddTime() {
		return addTime;
	}
	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}
	public int getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(int isDefault) {
		this.isDefault = isDefault;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public String toString() {
		return "AddressInfo [_id=" + _id + ", userId=" + userId
				+ ", consignee=" + consignee + ", phoneNum=" + phoneNum
				+ ", postalCode=" + postalCode + ", area=" + area
				+ ", detailedAddress=" + detailedAddress + ", addTime="
				+ addTime + ", isDefault=" + isDefault + "]";
	} 
	
	

}
