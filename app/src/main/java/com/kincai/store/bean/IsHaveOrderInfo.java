package com.kincai.store.bean;

import java.io.Serializable;

/**
 * 
 * @author kincai
 *
 */
public class IsHaveOrderInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
//	{"code":200,"message":"成功","data":{"daifukuan":"1","daifahuo":"3","daishouhuo":"","daipingjia":""}}
	 private int code;
	 private String message;
	 private DataEntity data;
	 
	 public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public DataEntity getData() {
		return data;
	}

	public void setData(DataEntity data) {
		this.data = data;
	}

	public static class DataEntity implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		private String daifukuan;
        private String daifahuo;
        private String daishouhuo;
        private String daipingjia;
		public String getDaifukuan() {
			return daifukuan;
		}
		public void setDaifukuan(String daifukuan) {
			this.daifukuan = daifukuan;
		}
		public String getDaifahuo() {
			return daifahuo;
		}
		public void setDaifahuo(String daifahuo) {
			this.daifahuo = daifahuo;
		}
		public String getDaishouhuo() {
			return daishouhuo;
		}
		public void setDaishouhuo(String daishouhuo) {
			this.daishouhuo = daishouhuo;
		}
		public String getDaipingjia() {
			return daipingjia;
		}
		public void setDaipingjia(String daipingjia) {
			this.daipingjia = daipingjia;
		}
		@Override
		public String toString() {
			return "DataEntity [daifukuan=" + daifukuan + ", daifahuo="
					+ daifahuo + ", daishouhuo=" + daishouhuo + ", daipingjia="
					+ daipingjia + "]";
		}
		
        
	 }

	@Override
	public String toString() {
		return "IsHaveOrderInfo [code=" + code + ", message=" + message
				+ ", data=" + data + "]";
	}

	
}
