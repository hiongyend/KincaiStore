package com.kincai.store.bean;

/**
 * copyright KCS互联网有限公司版权所有 (c) 2014-2015
 * .
 * Author kincai
 * .
 * project My Application
 * .
 * description TODO
 * .
 * Time 2016-02-04 11:53
 */
public class ProDetailInfo {

    /**
     * code : 200
     * message : 成功
     * data : {"store_id":"1","user_id":"44","store_name":"班尼路旗舰店","store_introduction":"始于1981，班尼路凭借着物超所值及简约主义的品牌宗旨，致力于提供多样化的服饰选择。如今，已在国内多个城市开设3000多间分店，已成功在亚洲树立起物超所值、多元服饰的品牌形象。全民狂欢节过后，汇总人气时装，献给把世界当做舞台的你","phone":"13560577498","logo_url":"175716bbf6e884b26f6857b8fe1fc38c.png","shop_img_url":"94e3f2285ea26d1603367208bd50e553.jpg","create_time":"1451883605000","pro_num":"7","attent_num":"0","pro_desc_value":"5.0","seller_service_value":"5.0","logistics_service_value":"5.0","credit_rating":"1","proData":{"id":"27","pName":"TOPOT 2015夏季新款男士短袖衬衫棉麻衬衣 蓝色 L/175","pSn":"1456231","pNum":"1236","mPrice":"142.00","iPrice":"100.00","pDesc":"TOPOT 2015夏季新款男士短袖衬衫棉麻衬衣 蓝色 L/175 支持货到付款，清爽透气亚麻衬衫\n","pubTime":"1433153808","isShow":"1","isHot":"0","cId":"11","evaluate_num":"0","sale_num":"0","support_num":"0","integral":"0","pay_num":"0","store_id":"1"}}
     */

    private int code;
    private String message;
    /**
     * store_id : 1
     * user_id : 44
     * store_name : 班尼路旗舰店
     * store_introduction : 始于1981，班尼路凭借着物超所值及简约主义的品牌宗旨，致力于提供多样化的服饰选择。如今，已在国内多个城市开设3000多间分店，已成功在亚洲树立起物超所值、多元服饰的品牌形象。全民狂欢节过后，汇总人气时装，献给把世界当做舞台的你
     * phone : 13560577498
     * logo_url : 175716bbf6e884b26f6857b8fe1fc38c.png
     * shop_img_url : 94e3f2285ea26d1603367208bd50e553.jpg
     * create_time : 1451883605000
     * pro_num : 7
     * attent_num : 0
     * pro_desc_value : 5.0
     * seller_service_value : 5.0
     * logistics_service_value : 5.0
     * credit_rating : 1
     * proData : {"id":"27","pName":"TOPOT 2015夏季新款男士短袖衬衫棉麻衬衣 蓝色 L/175","pSn":"1456231","pNum":"1236","mPrice":"142.00","iPrice":"100.00","pDesc":"TOPOT 2015夏季新款男士短袖衬衫棉麻衬衣 蓝色 L/175 支持货到付款，清爽透气亚麻衬衫\n","pubTime":"1433153808","isShow":"1","isHot":"0","cId":"11","evaluate_num":"0","sale_num":"0","support_num":"0","integral":"0","pay_num":"0","store_id":"1"}
     */

    private ProDetailData data;

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setProDetailData(ProDetailData data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public ProDetailData getProDetailData() {
        return data;
    }

    public static class ProDetailData {
        private String store_id;
        private String user_id;
        private String store_name;
        private String store_introduction;
        private String phone;
        private String logo_url;
        private String shop_img_url;
        private String create_time;
        private String pro_num;
        private String attent_num;
        private String pro_desc_value;
        private String seller_service_value;
        private String logistics_service_value;
        private String credit_rating;
        /**
         * id : 27
         * pName : TOPOT 2015夏季新款男士短袖衬衫棉麻衬衣 蓝色 L/175
         * pSn : 1456231
         * pNum : 1236
         * mPrice : 142.00
         * iPrice : 100.00
         * pDesc : TOPOT 2015夏季新款男士短袖衬衫棉麻衬衣 蓝色 L/175 支持货到付款，清爽透气亚麻衬衫

         * pubTime : 1433153808
         * isShow : 1
         * isHot : 0
         * cId : 11
         * evaluate_num : 0
         * sale_num : 0
         * support_num : 0
         * integral : 0
         * pay_num : 0
         * store_id : 1
         */

        private ProDatas proData;

        public void setStore_id(String store_id) {
            this.store_id = store_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public void setStore_name(String store_name) {
            this.store_name = store_name;
        }

        public void setStore_introduction(String store_introduction) {
            this.store_introduction = store_introduction;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public void setLogo_url(String logo_url) {
            this.logo_url = logo_url;
        }

        public void setShop_img_url(String shop_img_url) {
            this.shop_img_url = shop_img_url;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public void setPro_num(String pro_num) {
            this.pro_num = pro_num;
        }

        public void setAttent_num(String attent_num) {
            this.attent_num = attent_num;
        }

        public void setPro_desc_value(String pro_desc_value) {
            this.pro_desc_value = pro_desc_value;
        }

        public void setSeller_service_value(String seller_service_value) {
            this.seller_service_value = seller_service_value;
        }

        public void setLogistics_service_value(String logistics_service_value) {
            this.logistics_service_value = logistics_service_value;
        }

        public void setCredit_rating(String credit_rating) {
            this.credit_rating = credit_rating;
        }

        public void setProDatas(ProDatas proData) {
            this.proData = proData;
        }

        public String getStore_id() {
            return store_id;
        }

        public String getUser_id() {
            return user_id;
        }

        public String getStore_name() {
            return store_name;
        }

        public String getStore_introduction() {
            return store_introduction;
        }

        public String getPhone() {
            return phone;
        }

        public String getLogo_url() {
            return logo_url;
        }

        public String getShop_img_url() {
            return shop_img_url;
        }

        public String getCreate_time() {
            return create_time;
        }

        public String getPro_num() {
            return pro_num;
        }

        public String getAttent_num() {
            return attent_num;
        }

        public String getPro_desc_value() {
            return pro_desc_value;
        }

        public String getSeller_service_value() {
            return seller_service_value;
        }

        public String getLogistics_service_value() {
            return logistics_service_value;
        }

        public String getCredit_rating() {
            return credit_rating;
        }

        public ProDatas getProDatas() {
            return proData;
        }

        public static class ProDatas {
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
        }
    }
}
