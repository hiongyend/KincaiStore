package com.kincai.store.bean;

import java.util.List;

/**
 * copyright KCS互联网有限公司版权所有 (c) 2014-2015
 * .
 * Author kincai
 * .
 * project My Application
 * .
 * description TODO
 * .
 * Time 2016-02-08 13:23
 */
public class StoreNewProInfo {

    /**
     * code : 200
     * data : [{"proData":[{"cId":"13","evaluate_num":"0","iPrice":"79.00","id":"79","integral":"0","isHot":"0","isShow":"1","mPrice":"109.00","pDesc":"班尼路Baleno 甜美字母印花圆领T恤短袖 学院风纯棉创意上衣女装 A99纯黑 X","pName":"班尼路Baleno 甜美字母印花圆领T恤短袖","pNum":"56","pSn":"698","pay_num":"0","pubTime":"1454815503","sale_num":"0","store_id":"1","support_num":"0","url":"636303cf6bda2a40abe6dff29bfb72b1.png"},{"cId":"11","evaluate_num":"0","iPrice":"139.00","id":"78","integral":"0","isHot":"0","isShow":"1","mPrice":"199.00","pDesc":"班尼路/Baleno2015秋 甜美长袖牛津纺衬衫女 韩版学院风修身衬衣 淡珊瑚红 L","pName":"班尼路/Baleno2015秋 甜美长袖牛津纺衬衫女","pNum":"666","pSn":"56869566","pay_num":"0","pubTime":"1454815219","sale_num":"0","store_id":"1","support_num":"0","url":"ff6d1fe84494e7b7b363c616f682c691.png"}],"time":"1454815503"},{"proData":[{"cId":"11","evaluate_num":"0","iPrice":"99.00","id":"76","integral":"0","isHot":"0","isShow":"1","mPrice":"109.00","pDesc":"班尼路/Baleno秋装新款格子衬衫女长袖 百搭英伦学院风修身衬衣 粉红格 01C M","pName":"班尼路/Baleno秋装新款格子衬衫女长袖 百搭英伦学院风修身衬衣 粉红格 01C M","pNum":"66","pSn":"658965896","pay_num":"0","pubTime":"1454744318","sale_num":"0","store_id":"1","support_num":"0","url":"b39fc1c3610cc8cda786dd4ca49072f3.png"},{"cId":"11","evaluate_num":"0","iPrice":"139.00","id":"78","integral":"0","isHot":"0","isShow":"1","mPrice":"199.00","pDesc":"班尼路/Baleno2015秋 甜美长袖牛津纺衬衫女 韩版学院风修身衬衣 淡珊瑚红 L","pName":"班尼路/Baleno2015秋 甜美长袖牛津纺衬衫女","pNum":"666","pSn":"56869566","pay_num":"0","pubTime":"1454815219","sale_num":"0","store_id":"1","support_num":"0","url":"ff6d1fe84494e7b7b363c616f682c691.png"}],"time":"1454745083"}]
     * message : nonext
     */

    private int code;
    private String message;
    /**
     * proData : [{"cId":"13","evaluate_num":"0","iPrice":"79.00","id":"79","integral":"0","isHot":"0","isShow":"1","mPrice":"109.00","pDesc":"班尼路Baleno 甜美字母印花圆领T恤短袖 学院风纯棉创意上衣女装 A99纯黑 X","pName":"班尼路Baleno 甜美字母印花圆领T恤短袖","pNum":"56","pSn":"698","pay_num":"0","pubTime":"1454815503","sale_num":"0","store_id":"1","support_num":"0","url":"636303cf6bda2a40abe6dff29bfb72b1.png"},{"cId":"11","evaluate_num":"0","iPrice":"139.00","id":"78","integral":"0","isHot":"0","isShow":"1","mPrice":"199.00","pDesc":"班尼路/Baleno2015秋 甜美长袖牛津纺衬衫女 韩版学院风修身衬衣 淡珊瑚红 L","pName":"班尼路/Baleno2015秋 甜美长袖牛津纺衬衫女","pNum":"666","pSn":"56869566","pay_num":"0","pubTime":"1454815219","sale_num":"0","store_id":"1","support_num":"0","url":"ff6d1fe84494e7b7b363c616f682c691.png"}]
     * time : 1454815503
     */

    private List<NewProData> data;

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setNewProData(List<NewProData> data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public List<NewProData> getNewProData() {
        return data;
    }

    public static class NewProData {
        private String time;
        /**
         * cId : 13
         * evaluate_num : 0
         * iPrice : 79.00
         * id : 79
         * integral : 0
         * isHot : 0
         * isShow : 1
         * mPrice : 109.00
         * pDesc : 班尼路Baleno 甜美字母印花圆领T恤短袖 学院风纯棉创意上衣女装 A99纯黑 X
         * pName : 班尼路Baleno 甜美字母印花圆领T恤短袖
         * pNum : 56
         * pSn : 698
         * pay_num : 0
         * pubTime : 1454815503
         * sale_num : 0
         * store_id : 1
         * support_num : 0
         * url : 636303cf6bda2a40abe6dff29bfb72b1.png
         */

        private List<ProData> proData;

        public void setTime(String time) {
            this.time = time;
        }

        public void setProData(List<ProData> proData) {
            this.proData = proData;
        }

        public String getTime() {
            return time;
        }

        public List<ProData> getProData() {
            return proData;
        }

        public static class ProData {
            private String cId;
            private String evaluate_num;
            private String iPrice;
            private String id;
            private String integral;
            private String isHot;
            private String isShow;
            private String mPrice;
            private String pDesc;
            private String pName;
            private String pNum;
            private String pSn;
            private String pay_num;
            private String pubTime;
            private String sale_num;
            private String store_id;
            private String support_num;
            private String url;

            public void setCId(String cId) {
                this.cId = cId;
            }

            public void setEvaluate_num(String evaluate_num) {
                this.evaluate_num = evaluate_num;
            }

            public void setIPrice(String iPrice) {
                this.iPrice = iPrice;
            }

            public void setId(String id) {
                this.id = id;
            }

            public void setIntegral(String integral) {
                this.integral = integral;
            }

            public void setIsHot(String isHot) {
                this.isHot = isHot;
            }

            public void setIsShow(String isShow) {
                this.isShow = isShow;
            }

            public void setMPrice(String mPrice) {
                this.mPrice = mPrice;
            }

            public void setPDesc(String pDesc) {
                this.pDesc = pDesc;
            }

            public void setPName(String pName) {
                this.pName = pName;
            }

            public void setPNum(String pNum) {
                this.pNum = pNum;
            }

            public void setPSn(String pSn) {
                this.pSn = pSn;
            }

            public void setPay_num(String pay_num) {
                this.pay_num = pay_num;
            }

            public void setPubTime(String pubTime) {
                this.pubTime = pubTime;
            }

            public void setSale_num(String sale_num) {
                this.sale_num = sale_num;
            }

            public void setStore_id(String store_id) {
                this.store_id = store_id;
            }

            public void setSupport_num(String support_num) {
                this.support_num = support_num;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getCId() {
                return cId;
            }

            public String getEvaluate_num() {
                return evaluate_num;
            }

            public String getIPrice() {
                return iPrice;
            }

            public String getId() {
                return id;
            }

            public String getIntegral() {
                return integral;
            }

            public String getIsHot() {
                return isHot;
            }

            public String getIsShow() {
                return isShow;
            }

            public String getMPrice() {
                return mPrice;
            }

            public String getPDesc() {
                return pDesc;
            }

            public String getPName() {
                return pName;
            }

            public String getPNum() {
                return pNum;
            }

            public String getPSn() {
                return pSn;
            }

            public String getPay_num() {
                return pay_num;
            }

            public String getPubTime() {
                return pubTime;
            }

            public String getSale_num() {
                return sale_num;
            }

            public String getStore_id() {
                return store_id;
            }

            public String getSupport_num() {
                return support_num;
            }

            public String getUrl() {
                return url;
            }
        }
    }
}
