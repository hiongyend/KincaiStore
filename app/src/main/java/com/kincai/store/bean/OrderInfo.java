package com.kincai.store.bean;

import java.io.Serializable;
import java.util.List;

/**
 * copyright KCS互联网有限公司版权所有 (c) 2014-2015
 * .
 * Author Administrator
 * .
 * project Mdks
 * .
 * description TODO
 * .
 * Time 2016-04-28 9:28
 */
public class OrderInfo implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
     * message : 成功
     * data : [{"order_number":"f1454bbffbbd70f07b1dad1a73699475","order_goods_num":"1","cancel_order_reason":"","trade_state":"1","store_id":"5","pay_time":"1461750052000","successful_trade_time":null,"order_id":"7","evaluateState":"0","shipments_time":null,"consignee_phone":"13560577498","isanonymity":"1","transaction_num":"20161614617500532201461750053220","order_goods":[{"pro_name":"女装 法兰绒格子衬衫(长袖) 161547 优衣库UNIQLO","pro_iprice":"199.00","pId":"80","goods_num":"1","pro_logo_url":"1288a1ad6713cb1f31a51ccbf752fc84.png","pro_attr":"颜色分类：粉红；尺码：170/L","pro_mprice":"269.00","order_goods_id":"10","add_time":"1454816205","order_id":"7"}],"consignee_name":"欧阳达生","address_id":"13","pay_type":"1","order_num":"161461750053220","total_fee":"199.0","userId":"44","create_time":"1461750053220","consignee_address":"广东省广州市萝岗区 广东省广州市萝岗区科学大道286号七喜大厦408-409","yunfei":"0.00","buyer_msg":""},{"order_number":"f1454bbffbbd70f07b1dad1a73699475","order_goods_num":"2","cancel_order_reason":"","trade_state":"1","store_id":"1","pay_time":"1461750052000","successful_trade_time":null,"order_id":"8","evaluateState":"0","shipments_time":null,"consignee_phone":"13560577498","isanonymity":"1","transaction_num":"20161614617500532201461750053221","order_goods":[{"pro_name":"班尼路/Baleno男装 青年圆领印花长袖t恤","pro_iprice":"69.00","pId":"81","goods_num":"1","pro_logo_url":"c70deed34df1a680ef74b06fe13362c9.png","pro_attr":"颜色分类：粉红；尺码：170/L","pro_mprice":"109.00","order_goods_id":"11","add_time":"1454996651","order_id":"8"},{"pro_name":"班尼路/Baleno秋装新款格子衬衫女长袖 百搭英伦学院风修身衬衣 粉红格 01C M","pro_iprice":"99.00","pId":"76","goods_num":"1","pro_logo_url":"b39fc1c3610cc8cda786dd4ca49072f3.png","pro_attr":"颜色分类：粉红；尺码：170/L","pro_mprice":"109.00","order_goods_id":"12","add_time":"1454744318","order_id":"8"}],"consignee_name":"欧阳达生","address_id":"13","pay_type":"1","order_num":"161461750053220","total_fee":"168.0","userId":"44","create_time":"1461750053220","consignee_address":"广东省广州市萝岗区 广东省广州市萝岗区科学大道286号七喜大厦408-409","yunfei":"0.00","buyer_msg":""},{"order_number":"37c1693b39f041d4c86ac1f0aac3af0d","order_goods_num":"2","cancel_order_reason":"","trade_state":"1","store_id":"5","pay_time":"1461749725000","successful_trade_time":null,"order_id":"1","evaluateState":"0","shipments_time":null,"consignee_phone":"13560577498","isanonymity":"1","transaction_num":"20161614617497262181461749726210","order_goods":[{"pro_name":"女装 法兰绒格子衬衫(长袖) 161547 优衣库UNIQLO","pro_iprice":"199.00","pId":"80","goods_num":"1","pro_logo_url":"1288a1ad6713cb1f31a51ccbf752fc84.png","pro_attr":"颜色分类：粉红；尺码：170/L","pro_mprice":"269.00","order_goods_id":"1","add_time":"1454816205","order_id":"1"},{"pro_name":"韩都衣舍 韩版2015夏装新款女装直筒刺绣图案浅色牛仔短裤GW4357② 蓝色A M","pro_iprice":"58.00","pId":"66","goods_num":"1","pro_logo_url":"7b7da08407375da662b84eb690de54ee.png","pro_attr":"颜色分类：粉红；尺码：170/L","pro_mprice":"89.00","order_goods_id":"2","add_time":"1433202043","order_id":"1"}],"consignee_name":"欧阳达生","address_id":"13","pay_type":"1","order_num":"161461749726218","total_fee":"257.0","userId":"44","create_time":"1461749726218","consignee_address":"广东省广州市萝岗区 广东省广州市萝岗区科学大道286号七喜大厦408-409","yunfei":"0.00","buyer_msg":""},{"order_number":"37c1693b39f041d4c86ac1f0aac3af0d","order_goods_num":"2","cancel_order_reason":"","trade_state":"1","store_id":"1","pay_time":"1461749725000","successful_trade_time":null,"order_id":"2","evaluateState":"0","shipments_time":null,"consignee_phone":"13560577498","isanonymity":"1","transaction_num":"20161614617497262181461749726211","order_goods":[{"pro_name":"班尼路/Baleno男装 青年圆领印花长袖t恤","pro_iprice":"69.00","pId":"81","goods_num":"1","pro_logo_url":"c70deed34df1a680ef74b06fe13362c9.png","pro_attr":"颜色分类：粉红；尺码：170/L","pro_mprice":"109.00","order_goods_id":"3","add_time":"1454996651","order_id":"2"},{"pro_name":"班尼路Baleno 甜美字母印花圆领T恤短袖","pro_iprice":"79.00","pId":"79","goods_num":"1","pro_logo_url":"636303cf6bda2a40abe6dff29bfb72b1.png","pro_attr":"颜色分类：粉红；尺码：170/L","pro_mprice":"109.00","order_goods_id":"4","add_time":"1454815503","order_id":"2"}],"consignee_name":"欧阳达生","address_id":"13","pay_type":"1","order_num":"161461749726218","total_fee":"148.0","userId":"44","create_time":"1461749726218","consignee_address":"广东省广州市萝岗区 广东省广州市萝岗区科学大道286号七喜大厦408-409","yunfei":"0.00","buyer_msg":""},{"order_number":"37c1693b39f041d4c86ac1f0aac3af0d","order_goods_num":"1","cancel_order_reason":"","trade_state":"1","store_id":"2","pay_time":"1461749725000","successful_trade_time":null,"order_id":"3","evaluateState":"0","shipments_time":null,"consignee_phone":"13560577498","isanonymity":"1","transaction_num":"20161614617497262181461749726212","order_goods":[{"pro_name":"暖盈好 连衣裙 夏2015夏装新款 女装 上新 女装网纱蕾丝修身棉麻雪纺连衣裙女1513 图片色 L","pro_iprice":"128.00","pId":"62","goods_num":"1","pro_logo_url":"8aeca6fb31da8e2e3268c8a2c6b584bc.png","pro_attr":"颜色分类：粉红；尺码：170/L","pro_mprice":"158.00","order_goods_id":"5","add_time":"1433201530","order_id":"3"}],"consignee_name":"欧阳达生","address_id":"13","pay_type":"1","order_num":"161461749726218","total_fee":"128.0","userId":"44","create_time":"1461749726218","consignee_address":"广东省广州市萝岗区 广东省广州市萝岗区科学大道286号七喜大厦408-409","yunfei":"0.00","buyer_msg":""},{"order_number":"37c1693b39f041d4c86ac1f0aac3af0d","order_goods_num":"1","cancel_order_reason":"","trade_state":"1","store_id":"3","pay_time":"1461749725000","successful_trade_time":null,"order_id":"4","evaluateState":"0","shipments_time":null,"consignee_phone":"13560577498","isanonymity":"1","transaction_num":"20161614617497262181461749726213","order_goods":[{"pro_name":"南极人 2015 夏装新款女短裤韩版时尚女式休闲短裤 N3M5F50592A 西瓜红 M","pro_iprice":"46.00","pId":"67","goods_num":"1","pro_logo_url":"d3365ab57a50f752eda5533cd4644399.png","pro_attr":"颜色分类：粉红；尺码：170/L","pro_mprice":"78.00","order_goods_id":"6","add_time":"1433202135","order_id":"4"}],"consignee_name":"欧阳达生","address_id":"13","pay_type":"1","order_num":"161461749726218","total_fee":"46.0","userId":"44","create_time":"1461749726218","consignee_address":"广东省广州市萝岗区 广东省广州市萝岗区科学大道286号七喜大厦408-409","yunfei":"0.00","buyer_msg":""},{"order_number":"37c1693b39f041d4c86ac1f0aac3af0d","order_goods_num":"1","cancel_order_reason":"","trade_state":"1","store_id":"4","pay_time":"1461749725000","successful_trade_time":null,"order_id":"5","evaluateState":"0","shipments_time":null,"consignee_phone":"13560577498","isanonymity":"1","transaction_num":"20161614617497262181461749726214","order_goods":[{"pro_name":"工厂店GCD 夏装显瘦百搭休闲短裤 女 夏 修身休闲时尚短裤 女 J28 白色 26","pro_iprice":"63.00","pId":"72","goods_num":"1","pro_logo_url":"f98687c3ff7ca08e0663577968097347.png","pro_attr":"颜色分类：粉红；尺码：170/L","pro_mprice":"152.00","order_goods_id":"7","add_time":"1433202560","order_id":"5"}],"consignee_name":"欧阳达生","address_id":"13","pay_type":"1","order_num":"161461749726218","total_fee":"63.0","userId":"44","create_time":"1461749726218","consignee_address":"广东省广州市萝岗区 广东省广州市萝岗区科学大道286号七喜大厦408-409","yunfei":"0.00","buyer_msg":""},{"order_number":"37c1693b39f041d4c86ac1f0aac3af0d","order_goods_num":"2","cancel_order_reason":"","trade_state":"1","store_id":"6","pay_time":"1461749725000","successful_trade_time":null,"order_id":"6","evaluateState":"0","shipments_time":null,"consignee_phone":"13560577498","isanonymity":"1","transaction_num":"20161614617497262181461749726215","order_goods":[{"pro_name":"AIYOO 雪纺连衣裙 夏季新款 修身大码圆领百褶短袖 女长裙女修身裙子893 深蓝色 L偏小一码","pro_iprice":"66.00","pId":"55","goods_num":"1","pro_logo_url":"b0b7b88fd8ba35b80d925b2ac1434bf4.png","pro_attr":"颜色分类：粉红；尺码：170/L","pro_mprice":"85.00","order_goods_id":"8","add_time":"1433200940","order_id":"6"},{"pro_name":"我伊 2015春夏装女装新款韩版印花修身雪纺衫女中袖衬衫短裤两件套 1576黑色 XXL","pro_iprice":"99.00","pId":"45","goods_num":"1","pro_logo_url":"c8c681af6bdc58835771a15609f63d6d.png","pro_attr":"颜色分类：粉红；尺码：170/L","pro_mprice":"156.00","order_goods_id":"9","add_time":"1433199594","order_id":"6"}],"consignee_name":"欧阳达生","address_id":"13","pay_type":"1","order_num":"161461749726218","total_fee":"165.0","userId":"44","create_time":"1461749726218","consignee_address":"广东省广州市萝岗区 广东省广州市萝岗区科学大道286号七喜大厦408-409","yunfei":"0.00","buyer_msg":""}]
     * code : 200
     */
    private String message;
    private List<OrderData> data;
    private int code;

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(List<OrderData> data) {
        this.data = data;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public List<OrderData> getData() {
        return data;
    }

    public int getCode() {
        return code;
    }

    public class OrderData implements Serializable {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		/**
         * order_number : f1454bbffbbd70f07b1dad1a73699475
         * order_goods_num : 1
         * cancel_order_reason :
         * trade_state : 1
         * store_id : 5
         * pay_time : 1461750052000
         * successful_trade_time : null
         * order_id : 7
         * evaluateState : 0
         * shipments_time : null
         * consignee_phone : 13560577498
         * isanonymity : 1
         * transaction_num : 20161614617500532201461750053220
         * order_goods : [{"pro_name":"女装 法兰绒格子衬衫(长袖) 161547 优衣库UNIQLO","pro_iprice":"199.00","pId":"80","goods_num":"1","pro_logo_url":"1288a1ad6713cb1f31a51ccbf752fc84.png","pro_attr":"颜色分类：粉红；尺码：170/L","pro_mprice":"269.00","order_goods_id":"10","add_time":"1454816205","order_id":"7"}]
         * consignee_name : 欧阳达生
         * address_id : 13
         * pay_type : 1
         * order_num : 161461750053220
         * total_fee : 199.0
         * userId : 44
         * create_time : 1461750053220
         * consignee_address : 广东省广州市萝岗区 广东省广州市萝岗区科学大道286号七喜大厦408-409
         * yunfei : 0.00
         * buyer_msg :
         * store_name : 班尼路旗舰店
         */
        private String order_number;
        private String order_goods_num;
        private String cancel_order_reason;
        private String trade_state;
        private String store_id;
        private String pay_time;
        private String successful_trade_time;
        private String order_id;
        private String evaluateState;
        private String shipments_time;
        private String consignee_phone;
        private String isanonymity;
        private String transaction_num;
        private List<OrderGoodsData> order_goods;
        private String consignee_name;
        private String address_id;
        private String pay_type;
        private String order_num;
        private String total_fee;
        private String userId;
        private String create_time;
        private String consignee_address;
        private String yunfei;
        private String buyer_msg;
        private String store_name;

        public String getStore_name() {
			return store_name;
		}

		public void setStore_name(String store_name) {
			this.store_name = store_name;
		}

		public void setOrder_number(String order_number) {
            this.order_number = order_number;
        }

        public void setOrder_goods_num(String order_goods_num) {
            this.order_goods_num = order_goods_num;
        }

        public void setCancel_order_reason(String cancel_order_reason) {
            this.cancel_order_reason = cancel_order_reason;
        }

        public void setTrade_state(String trade_state) {
            this.trade_state = trade_state;
        }

        public void setStore_id(String store_id) {
            this.store_id = store_id;
        }

        public void setPay_time(String pay_time) {
            this.pay_time = pay_time;
        }

        public void setSuccessful_trade_time(String successful_trade_time) {
            this.successful_trade_time = successful_trade_time;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        public void setEvaluateState(String evaluateState) {
            this.evaluateState = evaluateState;
        }

        public void setShipments_time(String shipments_time) {
            this.shipments_time = shipments_time;
        }

        public void setConsignee_phone(String consignee_phone) {
            this.consignee_phone = consignee_phone;
        }

        public void setIsanonymity(String isanonymity) {
            this.isanonymity = isanonymity;
        }

        public void setTransaction_num(String transaction_num) {
            this.transaction_num = transaction_num;
        }

        public void setOrder_goods(List<OrderGoodsData> order_goods) {
            this.order_goods = order_goods;
        }

        public void setConsignee_name(String consignee_name) {
            this.consignee_name = consignee_name;
        }

        public void setAddress_id(String address_id) {
            this.address_id = address_id;
        }

        public void setPay_type(String pay_type) {
            this.pay_type = pay_type;
        }

        public void setOrder_num(String order_num) {
            this.order_num = order_num;
        }

        public void setTotal_fee(String total_fee) {
            this.total_fee = total_fee;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public void setConsignee_address(String consignee_address) {
            this.consignee_address = consignee_address;
        }

        public void setYunfei(String yunfei) {
            this.yunfei = yunfei;
        }

        public void setBuyer_msg(String buyer_msg) {
            this.buyer_msg = buyer_msg;
        }

        public String getOrder_number() {
            return order_number;
        }

        public String getOrder_goods_num() {
            return order_goods_num;
        }

        public String getCancel_order_reason() {
            return cancel_order_reason;
        }

        public String getTrade_state() {
            return trade_state;
        }

        public String getStore_id() {
            return store_id;
        }

        public String getPay_time() {
            return pay_time;
        }

        public String getSuccessful_trade_time() {
            return successful_trade_time;
        }

        public String getOrder_id() {
            return order_id;
        }

        public String getEvaluateState() {
            return evaluateState;
        }

        public String getShipments_time() {
            return shipments_time;
        }

        public String getConsignee_phone() {
            return consignee_phone;
        }

        public String getIsanonymity() {
            return isanonymity;
        }

        public String getTransaction_num() {
            return transaction_num;
        }

        public List<OrderGoodsData> getOrder_goods() {
            return order_goods;
        }

        public String getConsignee_name() {
            return consignee_name;
        }

        public String getAddress_id() {
            return address_id;
        }

        public String getPay_type() {
            return pay_type;
        }

        public String getOrder_num() {
            return order_num;
        }

        public String getTotal_fee() {
            return total_fee;
        }

        public String getUserId() {
            return userId;
        }

        public String getCreate_time() {
            return create_time;
        }

        public String getConsignee_address() {
            return consignee_address;
        }

        public String getYunfei() {
            return yunfei;
        }

        public String getBuyer_msg() {
            return buyer_msg;
        }

        public class OrderGoodsData implements Serializable {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			/**
             * pro_name : 女装 法兰绒格子衬衫(长袖) 161547 优衣库UNIQLO
             * pro_iprice : 199.00
             * pId : 80
             * goods_num : 1
             * pro_logo_url : 1288a1ad6713cb1f31a51ccbf752fc84.png
             * pro_attr : 颜色分类：粉红；尺码：170/L
             * pro_mprice : 269.00
             * order_goods_id : 10
             * add_time : 1454816205
             * order_id : 7
             */
            private String pro_name;
            private String pro_iprice;
            private String pId;
            private String goods_num;
            private String pro_logo_url;
            private String pro_attr;
            private String pro_mprice;
            private String order_goods_id;
            private String add_time;
            private String order_id;

            public void setPro_name(String pro_name) {
                this.pro_name = pro_name;
            }

            public void setPro_iprice(String pro_iprice) {
                this.pro_iprice = pro_iprice;
            }

            public void setPId(String pId) {
                this.pId = pId;
            }

            public void setGoods_num(String goods_num) {
                this.goods_num = goods_num;
            }

            public void setPro_logo_url(String pro_logo_url) {
                this.pro_logo_url = pro_logo_url;
            }

            public void setPro_attr(String pro_attr) {
                this.pro_attr = pro_attr;
            }

            public void setPro_mprice(String pro_mprice) {
                this.pro_mprice = pro_mprice;
            }

            public void setOrder_goods_id(String order_goods_id) {
                this.order_goods_id = order_goods_id;
            }

            public void setAdd_time(String add_time) {
                this.add_time = add_time;
            }

            public void setOrder_id(String order_id) {
                this.order_id = order_id;
            }

            public String getPro_name() {
                return pro_name;
            }

            public String getPro_iprice() {
                return pro_iprice;
            }

            public String getPId() {
                return pId;
            }

            public String getGoods_num() {
                return goods_num;
            }

            public String getPro_logo_url() {
                return pro_logo_url;
            }

            public String getPro_attr() {
                return pro_attr;
            }

            public String getPro_mprice() {
                return pro_mprice;
            }

            public String getOrder_goods_id() {
                return order_goods_id;
            }

            public String getAdd_time() {
                return add_time;
            }

            public String getOrder_id() {
                return order_id;
            }
        }
    }
}
