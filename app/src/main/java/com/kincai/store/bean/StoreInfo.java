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
 * Time 2016-01-30 12:27
 */
public class StoreInfo {

    /**
     * code : 200
     * message : 成功
     * data : {"store_id":"1","user_id":"44","store_name":"班尼路旗舰店","store_introduction":"始于1981，班尼路凭借着物超所值及简约主义的品牌宗旨，致力于提供多样化的服饰选择。如今，已在国内多个城市开设3000多间分店，已成功在亚洲树立起物超所值、多元服饰的品牌形象。全民狂欢节过后，汇总人气时装，献给把世界当做舞台的你","phone":"13560577498","logo_url":"175716bbf6e884b26f6857b8fe1fc38c.png","shop_img_url":"94e3f2285ea26d1603367208bd50e553.jpg","create_time":"1451883605000","pro_num":"7","attent_num":"0","pro_desc_value":"5.0","seller_service_value":"5.0","logistics_service_value":"5.0","credit_rating":"1","shopkeeper_nickname":"kincai","shopkeeper_phone":"13560577498","good_reputation_ratio":"99.9%"}
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
     * shopkeeper_nickname : kincai
     * shopkeeper_phone : 13560577498
     * good_reputation_ratio : 99.9%
     */

    private StoreData data;

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStoreData(StoreData data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public StoreData getStoreData() {
        return data;
    }

    public static class StoreData {
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
        private String shopkeeper_nickname;
        private String shopkeeper_phone;
        private String good_reputation_ratio;
        private String address;

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

        public void setShopkeeper_nickname(String shopkeeper_nickname) {
            this.shopkeeper_nickname = shopkeeper_nickname;
        }

        public void setShopkeeper_phone(String shopkeeper_phone) {
            this.shopkeeper_phone = shopkeeper_phone;
        }

        public void setGood_reputation_ratio(String good_reputation_ratio) {
            this.good_reputation_ratio = good_reputation_ratio;
        }
        
        public void setAddress(String address) {
			this.address = address;
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

        public String getShopkeeper_nickname() {
            return shopkeeper_nickname;
        }

        public String getShopkeeper_phone() {
            return shopkeeper_phone;
        }

        public String getGood_reputation_ratio() {
            return good_reputation_ratio;
        }

		public String getAddress() {
			return address;
		}
        
    }
}
