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
 * @description bomb订单
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.ui.viewpager.page
 *
 * @time 2015-12-25 下午3:27:36
 *
 */
public class QueryOrderInfo implements Serializable {

	
	private static final long serialVersionUID = 1L;
	/**
	 * name : 商品 
	 * body : 商品详情 
	 * create_time : 2015-03-24 11:14:58 
	 * out_trade_no : 9f392618f449a71c6fcfdee38d2b29e4 
	 * transaction_id : 2015061100001000330057820379 
	 * pay_type : WECHATPAY 
	 * total_fee : 0.01
	 * trade_state : NOTPAY
	 *
	 * •name : 订单或商品名称 
	 * •body-商品详情 
	 * •create_time : 调起支付的时间
	 * •out_trade_no : Bmob系统的订单号 
	 * •transaction_id : 微信或支付宝的系统订单号 
	 * •pay_type :WECHATPAY（微信支付）或ALIPAY（支付宝支付） 
	 * •total_fee : 订单总金额 
	 * •trade_state : NOTPAY（未支付）或 SUCCESS（支付成功）
	 */

	private String body;
    private String create_time;
    private String name;
    private String out_trade_no;
    private String pay_type;
    private String total_fee;
    private String trade_state;
    private String transaction_id;

    public void setBody(String body) {
        this.body = body;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }

    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }

    public void setTrade_state(String trade_state) {
        this.trade_state = trade_state;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getBody() {
        return body;
    }

    public String getCreate_time() {
        return create_time;
    }

    public String getName() {
        return name;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public String getPay_type() {
        return pay_type;
    }

    public String getTotal_fee() {
        return total_fee;
    }

    public String getTrade_state() {
        return trade_state;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

	@Override
	public String toString() {
		return "OrderInfo [body=" + body + ", create_time=" + create_time
				+ ", name=" + name + ", out_trade_no=" + out_trade_no
				+ ", pay_type=" + pay_type + ", total_fee=" + total_fee
				+ ", trade_state=" + trade_state + ", transaction_id="
				+ transaction_id + "]";
	}
	
	 
}
