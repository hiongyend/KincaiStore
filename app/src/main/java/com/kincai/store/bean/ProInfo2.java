package com.kincai.store.bean;

import java.util.List;

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
 * @time 2015-7-24 下午7:53:56
 *
 */
public class ProInfo2 {

	/*
	 * "cId": "11", "evaluate_num": "0", "iPrice": "100.00", "id": "27",
	 * "isHot": "0", "isShow": "1", "mPrice": "142.00", "pDesc":
	 * "TOPOT 2015夏季新款男士短袖衬衫棉麻衬衣 蓝色 L/175 支持货到付款，清爽透气亚麻衬衫 ", "pName":
	 * "TOPOT 2015夏季新款男士短袖衬衫棉麻衬衣 蓝色 L/175", "pNum": "1236", "pSn": "1456231",
	 * "pubTime": "1433153808", "sale_num": "0", "support_num": "0"
	 */

	public int code;
	public List<getData> data;
	public String message;

	public static class getData {
		public int id;
		public String pName;
		public String pSn;
		public int pNum;
		public String mPrice;
		public String iPrice;
		public String pDesc;
		public String pubTime;
		public int isShow;
		public int isHot;
		public int cId;
		public int evaluateNum;
		public int saleNum;
		public int supportNum;
		@Override
		public String toString() {
			return "getData [id=" + id + ", pName=" + pName + ", pSn=" + pSn
					+ ", pNum=" + pNum + ", mPrice=" + mPrice + ", iPrice="
					+ iPrice + ", pDesc=" + pDesc + ", pubTime=" + pubTime
					+ ", isShow=" + isShow + ", isHot=" + isHot + ", cId="
					+ cId + ", evaluateNum=" + evaluateNum + ", saleNum="
					+ saleNum + ", supportNum=" + supportNum + "]";
		}
		
		
	}

	
}
