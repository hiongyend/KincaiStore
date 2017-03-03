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
 * @description 商品信息实体类
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.bean
 *
 * @time 2015-7-24 下午7:54:28
 *
 */
public class ProInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int _id;
	private int storeId;
	private String pName;
	private String pSn;
	private int pNum;
	private String mPrice;
	private String iPrice;
	private String pDesc;
	private String pubTime;
	private int isShow;
	private int isHot;
	private int cId;
	private int evaluateNum;
	private int saleNum;
	private int supportNum;
	private int integral;
	private int paynum;
	private String url;

	public ProInfo() {
		
	}

	
	public ProInfo(int _id, int storeId, String pName, String pSn, int pNum,
			String mPrice, String iPrice, String pDesc, String pubTime,
			int isShow, int isHot, int cId, int evaluateNum, int saleNum,
			int supportNum, int integral, int paynum, String url) {
		super();
		this._id = _id;
		this.storeId = storeId;
		this.pName = pName;
		this.pSn = pSn;
		this.pNum = pNum;
		this.mPrice = mPrice;
		this.iPrice = iPrice;
		this.pDesc = pDesc;
		this.pubTime = pubTime;
		this.isShow = isShow;
		this.isHot = isHot;
		this.cId = cId;
		this.evaluateNum = evaluateNum;
		this.saleNum = saleNum;
		this.supportNum = supportNum;
		this.integral = integral;
		this.paynum = paynum;
		this.url = url;
	}


	public int getId() {
		return _id;
	}

	public void setId(int _id) {
		this._id = _id;
	}
	

	public int getStoreId() {
		return storeId;
	}

	public void setStoreId(int storeId) {
		this.storeId = storeId;
	}

	public String getpName() {
		return pName;
	}

	public void setpName(String pName) {
		this.pName = pName;
	}

	public String getpSn() {
		return pSn;
	}

	public void setpSn(String pSn) {
		this.pSn = pSn;
	}

	public int getpNum() {
		return pNum;
	}

	public void setpNum(int pNum) {
		this.pNum = pNum;
	}

	public String getmPrice() {
		return mPrice;
	}

	public void setmPrice(String mPrice) {
		this.mPrice = mPrice;
	}

	public String getiPrice() {
		return iPrice;
	}

	public void setiPrice(String iPrice) {
		this.iPrice = iPrice;
	}

	public String getpDesc() {
		return pDesc;
	}

	public void setpDesc(String pDesc) {
		this.pDesc = pDesc;
	}

	public String getPubTime() {
		return pubTime;
	}

	public void setPubTime(String pubTime) {
		this.pubTime = pubTime;
	}

	public int getIsShow() {
		return isShow;
	}

	public void setIsShow(int isShow) {
		this.isShow = isShow;
	}

	public int getIsHot() {
		return isHot;
	}

	public void setIsHot(int isHot) {
		this.isHot = isHot;
	}

	public int getcId() {
		return cId;
	}

	public void setcId(int cId) {
		this.cId = cId;
	}

	public int getEvaluateNum() {
		return evaluateNum;
	}

	public void setEvaluateNum(int evaluateNum) {
		this.evaluateNum = evaluateNum;
	}

	public int getSaleNum() {
		return saleNum;
	}

	public void setSaleNum(int saleNum) {
		this.saleNum = saleNum;
	}

	public int getSupportNum() {
		return supportNum;
	}

	public void setSupportNum(int supportNum) {
		this.supportNum = supportNum;
	}
	

	public int getIntegral() {
		return integral;
	}

	public void setIntegral(int integral) {
		this.integral = integral;
	}

	public int getPaynum() {
		return paynum;
	}

	public void setPaynum(int paynum) {
		this.paynum = paynum;
	}


	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}


	@Override
	public String toString() {
		return "ProInfo [_id=" + _id + ", storeId=" + storeId + ", pName="
				+ pName + ", pSn=" + pSn + ", pNum=" + pNum + ", mPrice="
				+ mPrice + ", iPrice=" + iPrice + ", pDesc=" + pDesc
				+ ", pubTime=" + pubTime + ", isShow=" + isShow + ", isHot="
				+ isHot + ", cId=" + cId + ", evaluateNum=" + evaluateNum
				+ ", saleNum=" + saleNum + ", supportNum=" + supportNum
				+ ", integral=" + integral + ", paynum=" + paynum + ", url="
				+ url + "]";
	}

	

}
