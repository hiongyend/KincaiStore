package com.kincai.store.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 *
 * @author kincai
 * 
 * @description  购物车信息
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.bean
 * 
 * @Time 2016-01-30 10:53
 */
public class CartInfo implements Serializable {


    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
     * code : 200
     * message : 成功
     * data : [{"cart_id":"2","store_id":"2","store_name":"以纯旗舰店","cart_num":"2","goods_data":[{"id":"64","pName":"素沛美衣2015夏新款连衣裙无袖长裙宽松雪纺女装 136白色 M","pSn":"225642","pNum":"565","mPrice":"168.00","iPrice":"148.00","pDesc":"素沛美衣2015夏新款连衣裙无袖长裙宽松雪纺女装 136白色 M","pubTime":"1433201736","isShow":"1","isHot":"0","cId":"18","evaluate_num":"0","sale_num":"0","support_num":"0","integral":"0","pay_num":"0","store_id":"2","goods_num":"1"},{"id":"62","pName":"暖盈好 连衣裙 夏2015夏装新款 女装 上新 女装网纱蕾丝修身棉麻雪纺连衣裙女1513 图片色 L","pSn":"895647","pNum":"586","mPrice":"158.00","iPrice":"128.00","pDesc":"暖盈好 连衣裙 夏2015夏装新款 女装 上新 女装网纱蕾丝修身棉麻雪纺连衣裙女1513 图片色 L","pubTime":"1433201530","isShow":"1","isHot":"0","cId":"18","evaluate_num":"0","sale_num":"0","support_num":"0","integral":"0","pay_num":"0","store_id":"2","goods_num":"2"}]},{"store_id":"5","store_name":"优衣库旗舰店","cart_num":"2","goods_data":[{"id":"73","pName":"VSTUAIE高贝春夏新款休闲七分裤女装修身职业休闲裤 女士小脚铅笔长裤 白色 27码2尺0","pSn":"156896","pNum":"254","mPrice":"156.00","iPrice":"102.00","pDesc":"VSTUAIE高贝春夏新款休闲七分裤女装修身职业休闲裤 女士小脚铅笔长裤 白色 27码2尺0","pubTime":"1433206351","isShow":"1","isHot":"0","cId":"19","evaluate_num":"0","sale_num":"0","support_num":"0","integral":"0","pay_num":"0","store_id":"5","goods_num":"1"},{"id":"71","pName":"娅丽达2015夏季新款休闲裤女韩版显瘦女士A字短裤三分裤热裤E2408 白色K0 27","pSn":"156245","pNum":"487","mPrice":"200.00","iPrice":"43.00","pDesc":"娅丽达2015夏季新款休闲裤女韩版显瘦女士A字短裤三分裤热裤E2408 白色K0 27","pubTime":"1433202484","isShow":"1","isHot":"0","cId":"14","evaluate_num":"0","sale_num":"0","support_num":"0","integral":"0","pay_num":"1","store_id":"5","goods_num":"1"}]},{"store_id":"3","store_name":"七匹狼旗舰店","cart_num":"3","goods_data":[{"id":"75","pName":"沃播 男士秋季免烫长袖衬衫男款纯棉休闲衬衣商务修身 C079A C079黑色 175","pSn":"2154652","pNum":"252","mPrice":"156.00","iPrice":"118.00","pDesc":"沃播 男士秋季免烫长袖衬衫男款纯棉休闲衬衣商务修身 C079A C079黑色 175","pubTime":"1442809958","isShow":"1","isHot":"0","cId":"11","evaluate_num":"0","sale_num":"0","support_num":"0","integral":"0","pay_num":"0","store_id":"3","goods_num":"2"},{"id":"74","pName":"优蜜雅 衬衫女装2015夏季新款女装韩版V领优雅甜美通勤OL商务衬衣长袖打底衫大码女装衬衫 白色 M","pSn":"4562135","pNum":"156","mPrice":"99.00","iPrice":"88.00","pDesc":"优蜜雅 衬衫女装2015夏季新款女装韩版V领优雅甜美通勤OL商务衬衣长袖打底衫大码女装衬衫 白色 M","pubTime":"1435204479","isShow":"1","isHot":"0","cId":"11","evaluate_num":"0","sale_num":"0","support_num":"0","integral":"0","pay_num":"1","store_id":"3","goods_num":"1"},{"id":"67","pName":"南极人 2015 夏装新款女短裤韩版时尚女式休闲短裤 N3M5F50592A 西瓜红 M","pSn":"145211","pNum":"324","mPrice":"78.00","iPrice":"46.00","pDesc":"南极人 2015 夏装新款女短裤韩版时尚女式休闲短裤 N3M5F50592A 西瓜红 M","pubTime":"1433202135","isShow":"1","isHot":"0","cId":"14","evaluate_num":"0","sale_num":"0","support_num":"0","integral":"0","pay_num":"0","store_id":"3","goods_num":"2","goods_id":"5"}]}]
     */

    private int code;
    private String message;
    /**
     * store_id : 2
     * store_name : 以纯旗舰店
     * cart_num : 2
     * goods_data : [{"id":"64","pName":"素沛美衣2015夏新款连衣裙无袖长裙宽松雪纺女装 136白色 M","pSn":"225642","pNum":"565","mPrice":"168.00","iPrice":"148.00","pDesc":"素沛美衣2015夏新款连衣裙无袖长裙宽松雪纺女装 136白色 M","pubTime":"1433201736","isShow":"1","isHot":"0","cId":"18","evaluate_num":"0","sale_num":"0","support_num":"0","integral":"0","pay_num":"0","store_id":"2","goods_num":"1"},{"id":"62","pName":"暖盈好 连衣裙 夏2015夏装新款 女装 上新 女装网纱蕾丝修身棉麻雪纺连衣裙女1513 图片色 L","pSn":"895647","pNum":"586","mPrice":"158.00","iPrice":"128.00","pDesc":"暖盈好 连衣裙 夏2015夏装新款 女装 上新 女装网纱蕾丝修身棉麻雪纺连衣裙女1513 图片色 L","pubTime":"1433201530","isShow":"1","isHot":"0","cId":"18","evaluate_num":"0","sale_num":"0","support_num":"0","integral":"0","pay_num":"0","store_id":"2","goods_num":"2","goods_id":"5"}]
     */

    private List<CartData> data;

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCartData(List<CartData> data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public List<CartData> getCartData() {
        return data;
    }

    public static class CartData implements Serializable {
    	/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String cart_id;
        private String store_id;
        private String store_name;
        private String cart_num;
        private boolean isGroupCheck = false;
        private boolean isGroupEdit = false;
        private String msg = "";
        public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}
        /**
         * id : 64
         * pName : 素沛美衣2015夏新款连衣裙无袖长裙宽松雪纺女装 136白色 M
         * pSn : 225642
         * pNum : 565
         * mPrice : 168.00
         * iPrice : 148.00
         * pDesc : 素沛美衣2015夏新款连衣裙无袖长裙宽松雪纺女装 136白色 M
         * pubTime : 1433201736
         * isShow : 1
         * isHot : 0
         * cId : 18
         * evaluate_num : 0
         * sale_num : 0
         * support_num : 0
         * integral : 0
         * pay_num : 0
         * store_id : 2
         * goods_num : 1
         */

        private List<GoodsData> goods_data;

        
        public void setCart_id(String cart_id) {
			this.cart_id = cart_id;
		}

		public void setStore_id(String store_id) {
            this.store_id = store_id;
        }

        public void setStore_name(String store_name) {
            this.store_name = store_name;
        }

        public void setCart_num(String cart_num) {
            this.cart_num = cart_num;
        }

        public void setGoods_data(List<GoodsData> goods_data) {
            this.goods_data = goods_data;
        }

        public void setIsGroupCheck(boolean isGroupCheck) {
			this.isGroupCheck = isGroupCheck;
		}

		public String getCart_id() {
			return cart_id;
		}

		public String getStore_id() {
            return store_id;
        }

        public String getStore_name() {
            return store_name;
        }

        public String getCart_num() {
            return cart_num;
        }

        public boolean getIsGroupCheck() {
			return isGroupCheck;
		}

		public boolean getIsGroupEdit() {
			return isGroupEdit;
		}

		public void setGroupEdit(boolean isGroupEdit) {
			this.isGroupEdit = isGroupEdit;
		}

		public List<GoodsData> getGoods_data() {
            return goods_data;
        }
        
		@Override
		public String toString() {
			return "CartData [cart_id=" + cart_id + ", store_id=" + store_id
					+ ", store_name=" + store_name + ", cart_num=" + cart_num
					+ ", goods_data=" + goods_data + "]";
		}


		public static class GoodsData implements Serializable {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			private String id;
            private String pName;
            private String pSn;
            private String pNum;
            private String mPrice;
            private String iPrice;
            private String pDesc;
            private String pubTime;
            private String isShow;
            private String isHot;
            private String cId;
            private String evaluate_num;
            private String sale_num;
            private String support_num;
            private String integral;
            private String pay_num;
            private String store_id;
            private String goods_id;
            private String goods_num;
            private String url;
            private String cart_goods_attr;

			private boolean isChildCheck = false;

            public void setId(String id) {
                this.id = id;
            }

            public void setPName(String pName) {
                this.pName = pName;
            }

            public void setPSn(String pSn) {
                this.pSn = pSn;
            }

            public void setPNum(String pNum) {
                this.pNum = pNum;
            }

            public void setMPrice(String mPrice) {
                this.mPrice = mPrice;
            }

            public void setIPrice(String iPrice) {
                this.iPrice = iPrice;
            }

            public void setPDesc(String pDesc) {
                this.pDesc = pDesc;
            }

            public void setPubTime(String pubTime) {
                this.pubTime = pubTime;
            }

            public void setIsShow(String isShow) {
                this.isShow = isShow;
            }

            public void setIsHot(String isHot) {
                this.isHot = isHot;
            }

            public void setCId(String cId) {
                this.cId = cId;
            }

            public void setEvaluate_num(String evaluate_num) {
                this.evaluate_num = evaluate_num;
            }

            public void setSale_num(String sale_num) {
                this.sale_num = sale_num;
            }

            public void setSupport_num(String support_num) {
                this.support_num = support_num;
            }

            public void setIntegral(String integral) {
                this.integral = integral;
            }

            public void setPay_num(String pay_num) {
                this.pay_num = pay_num;
            }

            public void setStore_id(String store_id) {
                this.store_id = store_id;
            }
            
            public void setGoods_id(String goods_id) {
				this.goods_id = goods_id;
			}
            
            public void setGoods_num(String goods_num) {
                this.goods_num = goods_num;
            }

			public void setGoods_img_url(String goods_img_url) {
				this.url = goods_img_url;
			}

			public void setGoods_attr(String goods_attr) {
				this.cart_goods_attr = goods_attr;
			}

			public void setChildCheck(boolean isChildCheck) {
				this.isChildCheck = isChildCheck;
			}

			public String getId() {
                return id;
            }

            public String getPName() {
                return pName;
            }

            public String getPSn() {
                return pSn;
            }

            public String getPNum() {
                return pNum;
            }

            public String getMPrice() {
                return mPrice;
            }

            public String getIPrice() {
                return iPrice;
            }

            public String getPDesc() {
                return pDesc;
            }

            public String getPubTime() {
                return pubTime;
            }

            public String getIsShow() {
                return isShow;
            }

            public String getIsHot() {
                return isHot;
            }

            public String getCId() {
                return cId;
            }

            public String getEvaluate_num() {
                return evaluate_num;
            }

            public String getSale_num() {
                return sale_num;
            }

            public String getSupport_num() {
                return support_num;
            }

            public String getIntegral() {
                return integral;
            }

            public String getPay_num() {
                return pay_num;
            }

            public String getStore_id() {
                return store_id;
            }
            
            public String getGoods_id() {
				return goods_id;
			}
            
            public String getGoods_num() {
                return goods_num;
            }

			public String getGoods_img_url() {
				return url;
			}
			
			public String getGoods_attr() {
				return cart_goods_attr;
			}

			public boolean getIsChildCheck() {
				return isChildCheck;
			}

			@Override
			public String toString() {
				return "GoodsData [id=" + id + ", pName=" + pName + ", pSn="
						+ pSn + ", pNum=" + pNum + ", mPrice=" + mPrice
						+ ", iPrice=" + iPrice + ", pDesc=" + pDesc
						+ ", pubTime=" + pubTime + ", isShow=" + isShow
						+ ", isHot=" + isHot + ", cId=" + cId
						+ ", evaluate_num=" + evaluate_num + ", sale_num="
						+ sale_num + ", support_num=" + support_num
						+ ", integral=" + integral + ", pay_num=" + pay_num
						+ ", store_id=" + store_id + ", goods_id=" + goods_id
						+ ", goods_num=" + goods_num + ", url=" + url
						+ ", goods_attr=" + cart_goods_attr + ", isChildCheck="
						+ isChildCheck + "]";
			}

			
        }
    }

	@Override
	public String toString() {
		return "CartInfo [code=" + code + ", message=" + message + ", data="
				+ data + "]";
	}
    
}
