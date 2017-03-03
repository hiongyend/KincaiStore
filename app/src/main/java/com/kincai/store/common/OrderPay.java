package com.kincai.store.common;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import c.b.BP;
import c.b.PListener;
import c.b.QListener;


import com.kincai.store.Constants;
import com.kincai.store.bean.AddressInfo;
import com.kincai.store.bean.CartInfo.CartData;
import com.kincai.store.bean.CartInfo.CartData.GoodsData;
import com.kincai.store.thread.CachedThreadPoolUtils;
import com.kincai.store.thread.HttpGetThread;
import com.kincai.store.utils.LogTest;
import com.kincai.store.utils.NetWorkUtil;
import com.kincai.store.utils.Utils;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 *
 * @author kincai
 * 
 * @todo 支付类
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.common
 *
 * @time 2015-12-23 上午10:26:56
 *
 */
public class OrderPay {

	/**
		错误码			原因
		-1			微信返回的错误码，可能是未安装微信，也可能是微信没获得网络权限等
		-2			微信支付用户中断操作
		-3			未安装微信支付插件
		102			设置了安全验证，但是签名或IP不对
		6001		支付宝支付用户中断操作
		4000		支付宝支付出错，可能是参数有问题
		1111		解析服务器返回的数据出错，可能是提交参数有问题
		2222		服务器端返回参数出错，可能是提交的参数有问题（如查询的订单号不存在）
		3333		解析服务器数据出错，可能是提交参数有问题
		5277		查询订单号时未输入订单号
		7777		微信客户端未安装
		8888		微信客户端版本不支持微信支付
		9010		网络异常，可能是没有给应用网络权限
		10003		商品名或详情不符合微信/支付宝的规定（如微信商品名不可以超过42个中文）
		10777		上次发起的请求还未处理完成，禁止下次请求，可用BmobPay.ForceFree()解除
		平时比较需要主动处理到的是-3、7777、8888、10777
	 */
	
	private static final String TAG = "OrderPay";
	private static OrderPay mOrderPayInstance;
	private static String mOrderStr;
	private static Activity mActivity;

	private OrderPay() {
	}

	private OrderPay(Activity activity) {
		mActivity = activity;
	}

	public static OrderPay getInstance(Activity activity) {
		if (mOrderPayInstance == null) {
			mOrderPayInstance = new OrderPay(activity);
		}
		return mOrderPayInstance;

	}

	/**
	 * 阿里支付
	 * 
	 * @param price
	 * @param proName
	 */
	public void aliPay(double price, String proName ,String proDesc) {
		BP.pay(mActivity ,proName,proDesc, price, true, new PListener() {

			// 因为网络等原因,支付结果未知(小概率事件),出于保险起见稍后手动查询
			@Override
			public void unknow() {
				LogTest.LogMsg(TAG, "-alipay-unknow-");
				returnState(Constants.BMOB_PAY_STATE_TYPE_UNKNOW);
			}

			// 支付成功,如果金额较大请手动查询确认
			@Override
			public void succeed() {
				Utils.showToast(mActivity, "支付成功", Toast.LENGTH_SHORT);
				LogTest.LogMsg(TAG, "-alipay-succeed-");
				returnState(Constants.BMOB_PAY_STATE_TYPE_SUCCESS);
			}
			// 无论成功与否,返回订单号
			@Override
			public void orderId(String orderId) {
				LogTest.LogMsg(TAG, "-alipay-orderId-" + orderId);
				mOrderStr = orderId;
			}

			// 支付失败,原因可能是用户中断支付操作,也可能是网络原因
			@Override
			public void fail(int arg0, String arg1) {
				//支付宝支付用户中断操作
				if(arg0 == 6001) {
					LogTest.LogMsg(TAG, "-alipay-fail-code:" + arg0);
					
					returnState(Constants.BMOB_PAY_STATE_TYPE_FAIL_6001);
				}
				
				
				
			}
		});
	}

	/**
	 * 微信支付
	 * 
	 * @param price
	 * @param proName
	 */
	public void wxPay(double price, String proName,String proDesc) {
		BP.pay(mActivity ,proName,proDesc, price,false, new PListener() {

			@Override
			public void unknow() {
				LogTest.LogMsg(TAG, "-wxpay-unknow-");
				returnState(Constants.BMOB_PAY_STATE_TYPE_UNKNOW);
			}

			@Override
			public void succeed() {
				Utils.showToast(mActivity, "支付成功", Toast.LENGTH_SHORT);
				LogTest.LogMsg(TAG, "-wxpay-succeed-");
				returnState(Constants.BMOB_PAY_STATE_TYPE_SUCCESS);
			}

			@Override
			public void orderId(String orderId) {
				LogTest.LogMsg(TAG, "-wxpay-orderId-" + orderId);
				mOrderStr = orderId;
			}

			@Override
			public void fail(int code, String arg1) {
				LogTest.LogMsg(TAG, "-wxpay-fail-code:" + code);
				// 当code为-2,意味着用户中断了操作
				// code为-3意味着没有安装BmobPlugin插件
				if(code == -3) {
					Utils.showToast(mActivity, "支付失败", Toast.LENGTH_SHORT);
					returnState(Constants.BMOB_PAY_STATE_TYPE_FAIL__3);
					//TODO 静默安装
				} else if (code == 7777) {
					Utils.showToast(mActivity, "请先安装微信客户端", Toast.LENGTH_SHORT);
					returnState(Constants.BMOB_PAY_STATE_TYPE_FAIL_7777);
					
				} else if (code == 8888) {
					Utils.showToast(mActivity, "当前微信版本不支持微信支付", Toast.LENGTH_SHORT);
					returnState(Constants.BMOB_PAY_STATE_TYPE_FAIL_8888);
				} else if(code == -2) {
					returnState(Constants.BMOB_PAY_STATE_TYPE_FAIL__2);
				}
			}
		});
	}
	
	private void returnState(String type) {
		Intent intent = new Intent();
		intent.setAction(Constants.ORDER_COMPLETE_PAY);
		intent.putExtra("orderId", mOrderStr);
		intent.putExtra("state_type", type);
		mActivity.sendBroadcast(intent);
	}

	/**
	 * 查询订单
	 * @param handler
	 * @param orderId
	 */
	public void query(final Handler handler, final String orderId) {
		if(NetWorkUtil.isNetworkAvailable(mActivity)) {
			BP.query(mActivity, orderId, new QListener() {

				@Override
				public void succeed(String state) {
					LogTest.LogMsg(TAG, "-query-succeed-:" + state);
					//查询成功 但未支付
					if("NOTPAY".equals(state)) {
					} 
					//查询成功 并且支付成功
					else if ("SUCCESS".equals(state)) {
					}
					mOrderStr = orderId;
					query(state, handler);
				}

				@Override
				public void fail(int code, String arg1) {
					LogTest.LogMsg(TAG, "-query-fail-code:" + code);
					Utils.showToast(mActivity, "查询订单失败", Toast.LENGTH_SHORT);
					returnState(Constants.BMOB_QUERY_STATE_TYPE_FAIL_QUERY);
				}
			});
		} else {
			LogTest.LogMsg(TAG, "-query-fail-code:");
			Utils.showToast(mActivity, "查询订单失败", Toast.LENGTH_SHORT);
			returnState(Constants.BMOB_QUERY_STATE_TYPE_FAIL_QUERY);
		}
		
	}

	private void query(String state, Handler handler) {
		if(NetWorkUtil.isNetworkAvailable(mActivity)) {
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("https://api.bmob.cn/1/pay/");
			stringBuilder.append(mOrderStr);
			CachedThreadPoolUtils.execute(new HttpGetThread(handler, stringBuilder
					.toString(), "query_order"));
		} else {
			LogTest.LogMsg(TAG, "-query-fail-code:");
			Utils.showToast(mActivity, "查询订单失败", Toast.LENGTH_SHORT);
			returnState(Constants.BMOB_QUERY_STATE_TYPE_FAIL_QUERY);
		}
		
	}
	
	/**
	 * 生成订单 json字符串
	 * 
	 * @param datas
	 *            封装的购物信息
	 * @param userId
	 *            用户id
	 * @param addressInfo
	 *            地址信息
	 * @param orderNum
	 *            订单号
	 * @param createTime
	 *            创建时间
	 * @param payOrderNum
	 *            支付订单号
	 * @param threeOrderNums
	 *            第三方支付平台订单号
	 * @param payType
	 *            支付类型 0 1
	 * @param payState
	 *            支付状态 NOTPAY SUCCESS
	 * @param payTime
	 *            支付时间 已支付的情况
	 * @param isanonymity
	 *            匿名
	 * @return
	 */
	public String createOrders(List<CartData> datas, String userId, AddressInfo addressInfo
			,String orderNum,String createTime,List<String> payOrderNum, String threeOrderNums
			,String payType, String payState
			,String payTime, String isanonymity) {
		
		if(datas == null || payOrderNum == null) return null;
		if(userId == null || "".equals(userId)) return null;
		if(datas.size() != payOrderNum.size()) return null;
		if(threeOrderNums == null || "".equals(threeOrderNums)) return null;
		if(addressInfo == null) return null;
		if(orderNum == null || "".equals(orderNum)) return null;
		if(createTime == null || "".equals(createTime)) return null;
		String pay_time = (payTime == null || "".equals(payTime)) ? "" : payTime;
		String is_anonymity = (isanonymity == null || "".equals(isanonymity)) ? "" : isanonymity;
		String cancel_reason = "NOTPAY".equals(payState) ? "取消支付" : ("SUCCESS".equals(payState) ? "": "");;
		
		String order_num = orderNum;
		//订单状态
		String pay_state = Constants.ORDER_NOTPAY.equals(payState) ? "0" : (Constants.ORDER_SUCCESS.equals(payState) ? "1": "");
		StringBuilder orderString = new StringBuilder();
		int size = datas.size();
		orderString.append("{\"order\":[");
		for (int i = 0; i < size; i++) {
			CartData cartData = datas.get(i);
			orderString.append("{\"userId\":\"");
			orderString.append(userId);
			orderString.append("\"");
			orderString.append(",\"address_id\":\"");
			orderString.append(addressInfo.getId());
			orderString.append("\"");
			orderString.append(",\"isanonymity\":\"");
			orderString.append(is_anonymity);
			orderString.append("\"");
			orderString.append(",\"create_time\":\"");
			orderString.append(createTime);
			orderString.append("\"");
			orderString.append(",\"pay_type\":\"");
			orderString.append(payType);
			orderString.append("\"");
			orderString.append(",\"trade_state\":\"");
			orderString.append(pay_state);
			orderString.append("\"");
			orderString.append(",\"pay_time\":\"");
			orderString.append(pay_time);
			orderString.append("\"");
			orderString.append(",\"store_id\":\"");
			orderString.append(cartData.getStore_id());
			orderString.append("\"");
			orderString.append(",\"order_num\":\"");
			orderString.append(order_num);
			orderString.append("\"");
			orderString.append(",\"transaction_num\":\"");
			orderString.append(payOrderNum.get(i));
			orderString.append("\"");
			orderString.append(",\"order_number\":\"");
			orderString.append(threeOrderNums);
			orderString.append("\"");
			orderString.append(",\"total_fee\":\"");
			double price = 0;
			
			int goodsSize = cartData.getGoods_data().size();
			for(int j = 0; j < goodsSize; j++) {
				
				price += (Double.parseDouble(cartData.getGoods_data().get(j).getIPrice())
						* Double.parseDouble(cartData.getGoods_data().get(j).getGoods_num()));
			}
			orderString.append(price);
			orderString.append("\"");
			orderString.append(",\"order_goods_num\":\"");
			orderString.append(goodsSize);
			orderString.append("\"");
			orderString.append(",\"consignee_name\":\"");
			orderString.append(addressInfo.getConsignee());
			orderString.append("\"");
			orderString.append(",\"consignee_phone\":\"");
			orderString.append(addressInfo.getPhoneNum());
			orderString.append("\"");
			orderString.append(",\"consignee_address\":\"");
			orderString.append(addressInfo.getArea());
			orderString.append(" ");
			orderString.append(addressInfo.getDetailedAddress());
			orderString.append("\"");
			orderString.append(",\"cancel_order_reason\":\"");
			orderString.append(cancel_reason);
			orderString.append("\"");
			orderString.append(",\"buyer_msg\":\"");
			orderString.append(cartData.getMsg());
			orderString.append("\"");
			orderString.append(",\"yunfei\":\"");
			orderString.append("0.00");
			orderString.append("\"");
			
			orderString.append(",\"goods\":[");
			List<GoodsData> goods_data = cartData.getGoods_data();
			for(int k = 0; k < goodsSize; k++) {
				GoodsData goodsData = goods_data.get(k);
				orderString.append("{\"pId\":\"");
				orderString.append(goodsData.getId());
				orderString.append("\"");
				orderString.append(",\"goods_num\":\"");
				orderString.append(goodsData.getGoods_num());
				orderString.append("\"");
				orderString.append(",\"goods_id\":\"");
				orderString.append(goodsData.getGoods_id() == null ? "" : goodsData.getGoods_id());
				orderString.append("\"");
				orderString.append(",\"pro_name\":\"");
				orderString.append(goodsData.getPName());
				orderString.append("\"");
				orderString.append(",\"pro_iprice\":\"");
				orderString.append(goodsData.getIPrice());
				orderString.append("\"");
				orderString.append(",\"pro_mprice\":\"");
				orderString.append(goodsData.getMPrice());
				orderString.append("\"");
				orderString.append(",\"pro_attr\":\"");
				orderString.append(goodsData.getGoods_attr());
				orderString.append("\"");
				orderString.append(",\"pro_logo_url\":\"");
				orderString.append(goodsData.getGoods_img_url());
				orderString.append("\"");
				orderString.append(",\"add_time\":\"");
				orderString.append(goodsData.getPubTime());
				orderString.append("\"");
				
				if(k == goodsSize - 1) {
					orderString.append("}]");
				} else {
					orderString.append("},");
				}
				
			}
			
			if(i == size-1) {
				orderString.append("}");
			} else {
				orderString.append("},");
			}
			
		}
		orderString.append("]}");
		
		return orderString.toString();
	}
}
