package com.kincai.store;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 * 
 * @author kincai
 * 
 * @description 常量
 * 
 * @project Kincai_Store
 * 
 * @package com.kincai.store
 * 
 * @time 2015-6-26 下午7:58:04
 * 
 */
public class Constants {
	
	

	
	/**
	 * 服务器url
	 */
	public static final String SERVER_URL = "http://kincai.ngrok.cc/shopkincai/";
//	public static final String SERVER_URL = "http://172.21.6.1:888/shopkincai/";
	
	public static final String API_URL = new StringBuffer().append(SERVER_URL).append("app_api/").toString();
	
	public static final String GRIDTAB_URL = new StringBuffer().append(SERVER_URL).append("GridImage/").toString();

	public static final String IMAGE_PATH_DEFAULT = new StringBuffer().append(SERVER_URL).append("image_220/").toString();
	public static final String IMAGE_PATH_HIGH_QUALITY = new StringBuffer().append(SERVER_URL).append("image_350/").toString();

	public static final String IMAGE_PATH_DEFAULT_PRO_DETAIL = new StringBuffer().append(SERVER_URL).append("image_350/").toString();
	public static final String IMAGE_PATH_HIGH_QUALITY_PRO_DETAIL = new StringBuffer().append(SERVER_URL).append("image_800/").toString();
	
	public static final String APP_ID = "1";
	public static final String APP_VERSION_ID = "1";
	
	/**
	 * 科大讯飞语音功能appid
	 */
	public static final String VOICE_APPID = "55d8031b";
	/**
	 * mob 短信验证appkey
	 */
	public static final String SMSSDK_APPKEY = "9a16ae90dfa8";
	/**
	 * mob 短信验证appsecret
	 */
	public static final String SMSSDK_APPSECRET = "daf9bdf52d70fc988b5e1aaaa0b85b71";
	/**
	 * 分批加载每页条数
	 */
	public static final int GET_PAGESIZE = 20;

	/**
	 * 更新apk文件名
	 */
	public static final String APP_DOWNLOAD_APK_NAME = "Kincai_Store.apk";

	/**
	 * 购物车改变action
	 */
	public static final String CART_CHANGE_ACTION = "com.kincai.store.cartChange";

	/**
	 * 修改用户信息广播action
	 */
	public static final String USERINFO_CHANGE_ACTION = "com.kincai.store.changeUserInfo";
	
	/**
	 * 修改用户信息广播action
	 */
	public static final String PERSIONFRAGMENT_ORDER_DATA_UPDATE_ACTION = "com.kincai.store.update_persionfragmentorder_data";
	/**
	 * 完成支付模块 即使没成功 action
	 */
	public static final String ORDER_COMPLETE_PAY = "com.kincai.store.ompletepay";
	/**
	 * 修改用户登录密码信息广播action
	 */
	public static final String USERINFO_LOGIN_PAW_CHANGE_ACTION = "com.kincai.store.changeUserInfo.login.paw";
	/**
	 * 浏览历史信息广播action
	 */
	public static final String BROWSE_HISTORY_CHANGE_ACTION = "com.kincai.store.changeBrowseHistory";

	/**
	 * 用户名限制条件(手机号码)
	 * ^(((13[0-9])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8})|(0\\d{2}-
	 * \\d{8})|(0\\d{3}-\\d{7})$
	 */
	public static final String USERNAME_RESTRICTION = "^(((13[0-9])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8})|(0\\d{2}-\\d{8})|(0\\d{3}-\\d{7})$";

	/**
	 * 昵称
	 */
	public static final String NICKNAME_RESTRICTION = "[a-zA-Z][a-zA-Z0-9]{5,10}";
	/**
	 * 用户密码限制条件
	 */
	public static final String PASSWORD_RESTRICTION = "[a-zA-Z][a-zA-Z0-9]{5,15}";

	/**
	 * 反馈信息现在
	 * 
	 */
	public static final String FEEDBACK_RESTRICTION = "[[\\s\\S]*]{6,199}";

	/**
	 * 邮箱限制
	 */
	public static final String EMAIL_RESTRICTION = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
	/**
	 * sp存储文件名
	 */
	public static final String KINCAI_SP_NAME = "com.kincai.store_preference";
	/**
	 * sp 第一次启动判断
	 */
	public static final String KINCAI_IS_FIRST = "is_first_start";

	/**
	 * sp 第一次启动判断
	 */
	public static final String KINCAI_IS_SPALSH_ADV = "is_splash_adv";
	/**
	 * sp 屏幕亮度是否开启
	 */
	public static final String KINCAI_CHANGE_LIGHT = "is_isCheck_change";
	/**
	 * sp 屏幕亮度值
	 */
	public static final String KINCAI_CHANGE_LIGHT_PRICE = "change_light_price";
	/**
	 * sp 消息提醒是否开启
	 */
	public static final String KINCAI_MESSAGE_WARN = "is_isCheck_message_warn";
	/**
	 * sp wifi自动更新是否开启
	 */
	public static final String KINCAI_AUTOMATE_UPGRADE = "is_isCheck_Automate_upgrade";

	/**
	 * sp 用户登录状态
	 */
	public static final String KINCAI_USER_ISLONGIN = "is_user_login";

	/**
	 * sp 用户头像
	 */
	public static final String KINCAI_USER_FACE = "user_face";
	/**
	 * sp 用户名
	 */
	public static final String KINCAI_USER_NAME = "user_name";

	/**
	 * sp 商品搜索结果界面的tab
	 */
	public static final String KINCAI_SEARCH_RESULT_TAB = "search_tab";
	/**
	 * sp 我的收藏界面的tab
	 */
	public static final String KINCAI_COLLECT_TAB = "collect_tab";
	/**
	 * sp 首页商品展示保存前20条数据
	 */
	public static final String KINCAi_HOME_PRO_20_DATA = "home_20_pro_data";
	/**
	 * sp 首页商品展示保存前20条图片路径
	 */
	public static final String KINCAi_HOME_PRO_20_IMAGE_PATH = "home_20_pro_image_path";
	
	/**
	 * sp 首页八个gridview tab数据保存
	 */
	public static final String KINCAI_HOME_GRIDE_TAB_IIFO = "home_grid_tab_info";

	/**
	 * sp 是否已经添加了assets里面的省市县/区到数据库
	 */
	public static final String KINCAI_IS_ADD_PROVINCE_CITY_ZONE_TO_DB = "is_add_province_city_zone_to_db";
	
	/** sp 图片模式*/
	public static final String KINCAI_PICTURE_MODE = "picture_mode";
	/**
	 * 收藏
	 */
	public static final String KINCAI_COLLECT = "collect";
	/**
	 * 操作类型get
	 */
	public static final String KINCAI_TYPE_GET = "get";
	/**
	 * 操作类型add
	 */
	public static final String KINCAI_TYPE_ADD = "add";
	/**
	 * 操作类型delete
	 */
	public static final String KINCAI_TYPE_DELETE = "delete";

	/**
	 * 操作类型edit
	 */
	public static final String KINCAI_TYPE_EDIT = "edit";
	/**
	 * 购物车
	 */
	public static final String KINCAI_CART = "cart";
	
	/** 图片显示模式 智能模式*/
	public static final int PICTURE_MODE_DEFAULT = 1;
	/** 图片显示模式 高清模式*/
	public static final int PICTURE_MODE_HIGH_QUALITY = 2;
	/** 图片显示模式 普通模式*/
	public static final int PICTURE_MODE_ORDINARY = 3;

	public static final String WEBVIEW_URL = "webview_url";
	/** 手势密码状态 sp*/
	public static final String LOCK_PWD_STATE = "LOCK_PWD_STATE";
	/** 手势密码*/
	public static final String LOCK_PWD = "LOCK_PWD";
	
	/** 支付订单 支付状态 已支付*/
	public static final String ORDER_SUCCESS = "SUCCESS";
	/** 支付订单 支付状态 没支付*/
	public static final String ORDER_NOTPAY = "NOTPAY";
	
	public static final String BMOB_PAY_ID= "8f79426ffc6f3459cded0c344a068372";
	
	public static final String BMOB_PAY_STATE_TYPE_UNKNOW = "unknow";
	public static final String BMOB_PAY_STATE_TYPE_SUCCESS = "succeed";
	public static final String BMOB_PAY_STATE_TYPE_FAIL_6001 = "fail_6001";
	public static final String BMOB_PAY_STATE_TYPE_FAIL__3 = "fail_-3";
	public static final String BMOB_PAY_STATE_TYPE_FAIL_7777 = "fail_7777";
	public static final String BMOB_PAY_STATE_TYPE_FAIL_8888 = "fail_8888";
	public static final String BMOB_PAY_STATE_TYPE_FAIL__2 = "fail_-2";
	public static final String BMOB_QUERY_STATE_TYPE_FAIL_QUERY = "fail_query";
	
}
