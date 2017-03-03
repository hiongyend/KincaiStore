package com.kincai.store.utils;

import com.kincai.store.Constants;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 * 
 * @author kincai
 * 
 * @description SharedPreferences储存
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.utils
 *
 * @time 2015-7-15 下午9:34:47
 *
 */
public class SPStorageUtil {
	private SharedPreferences mSp;
	private Editor mEditor;


	@SuppressLint("CommitPrefEdits")
	public SPStorageUtil(Context context) {
		if (null == mSp) {
			mSp = context.getSharedPreferences(Constants.KINCAI_SP_NAME,
					Context.MODE_PRIVATE);
		}
		mEditor = mSp.edit();
	}

	/**
	 * 保存程序是否启动过
	 * 
	 * @param b
	 */
	public void saveIsFirstStart(boolean b) {
		mEditor.putBoolean(Constants.KINCAI_IS_FIRST, b);
		mEditor.commit();
	}

	/**
	 * 获取程序是否启动过
	 * 
	 * @return
	 */
	public boolean getIsFirstStart() {
		return mSp.getBoolean(Constants.KINCAI_IS_FIRST, true);
	}

	/**
	 * 保存是否有程序启动广告
	 * 
	 * @param b
	 */
	public void saveIsSplashAdv(boolean b) {
		mEditor.putBoolean(Constants.KINCAI_IS_SPALSH_ADV, b);
		mEditor.commit();
	}

	/**
	 * 获取是否有程序启动广告
	 * 
	 * @return
	 */
	public boolean getIsSplashAdv() {
		return mSp.getBoolean(Constants.KINCAI_IS_SPALSH_ADV, false);
	}
	
	/**
	 * 保存改变屏幕亮度选中状态
	 * 
	 * @param b
	 */
	/*public void saveIsChangeLightChecked(boolean b) {
		mEditor.putBoolean(Constants.KINCAI_CHANGE_LIGHT, b);
		mEditor.commit();
	}*/

	/**
	 * 获取改变屏幕亮度选中状态
	 * 
	 * @return
	 */
	/*public boolean getIsChangeLightChecked() {
		return mSp.getBoolean(Constants.KINCAI_CHANGE_LIGHT, false);
	}*/

	/**
	 * 保存改变屏幕亮度值
	 * 
	 * @param b
	 */
	/*public void saveChangeLightPrice(int lightPrice) {
		mEditor.putInt(Constants.KINCAI_CHANGE_LIGHT_PRICE, lightPrice);
		mEditor.commit();
	}*/

	/**
	 * 获取改变屏幕亮度值
	 * 
	 * @return
	 */
	/*public int getChangeLightPrice() {
		return mSp.getInt(Constants.KINCAI_CHANGE_LIGHT_PRICE, 0);
	}*/

	/**
	 * 保存消息提醒选中状态
	 * 
	 * @param b
	 */
	public void saveIsMessageWarnChecked(boolean b) {
		mEditor.putBoolean(Constants.KINCAI_MESSAGE_WARN, b);
		mEditor.commit();
	}

	/**
	 * 获取消息提醒选中状态
	 * 
	 * @return
	 */
	public boolean getIsMessageWarnChecked() {
		return mSp.getBoolean(Constants.KINCAI_MESSAGE_WARN, true);
	}

	/**
	 * 保存wifi下自动更新选中状态
	 * 
	 * @param b
	 */
	public void saveWifiAutomateUpgradeChecked(boolean b) {
		mEditor.putBoolean(Constants.KINCAI_AUTOMATE_UPGRADE, b);
		mEditor.commit();
	}

	/**
	 * 获取wifi下自动更新选中状态
	 * 
	 * @return
	 */
	public boolean getWifiAutomateUpgradeChecked() {
		return mSp.getBoolean(Constants.KINCAI_AUTOMATE_UPGRADE, false);
	}

	/**
	 * 保存登录状态
	 * 
	 * @param b
	 */
	public void saveUserIsLogin(boolean b) {
		mEditor.putBoolean(Constants.KINCAI_USER_ISLONGIN, b);
		mEditor.commit();
	}

	/**
	 * 获取登录状态
	 * 
	 * @return
	 */
	public boolean getUserIsLogin() {
		return mSp.getBoolean(Constants.KINCAI_USER_ISLONGIN, false);
	}

	/**
	 * 保存用户头像路径
	 * 
	 * @param b
	 */
	public void saveUserFace(String face) {
		mEditor.putString(Constants.KINCAI_USER_FACE, face);
		mEditor.commit();
	}

	/**
	 * 获得用户头像路径
	 * 
	 * @return
	 */
	public String getUserFace() {
		return mSp.getString(Constants.KINCAI_USER_FACE, null);
	}

	/**
	 * 保存用户名作为读取数据库指定用户的标识
	 * 
	 * @param b
	 */
	public void saveUsername(String name) {
		mEditor.putString(Constants.KINCAI_USER_NAME, name);
		mEditor.commit();
	}

	/**
	 * 获取用户名作为读取数据库指定用户的标识
	 * 
	 * @return
	 */
	public String getUsername() {
		return mSp.getString(Constants.KINCAI_USER_NAME, null);
	}

	/**
	 * 保存搜索结果界面的tab选中那个
	 * 
	 * @param index
	 */
	public void saveSearchAfterTab(int index) {
		mEditor.putInt(Constants.KINCAI_SEARCH_RESULT_TAB, index);
		mEditor.commit();
	}

	/**
	 * 获取搜索结果界面的tab选中那个
	 * 
	 * @return
	 */
	public int getSearchAfterTab() {
		return mSp.getInt(Constants.KINCAI_SEARCH_RESULT_TAB, 1);
	}

	/**
	 * 保存搜索结果界面的tab选中那个
	 * 
	 * @param index
	 */
	public void saveCollectTab(int index) {
		mEditor.putInt(Constants.KINCAI_COLLECT_TAB, index);
		mEditor.commit();
	}

	/**
	 * 获取搜索结果界面的tab选中那个
	 * 
	 * @return
	 */
	public int getCollectTab() {
		return mSp.getInt(Constants.KINCAI_COLLECT_TAB, 1);
	}

	/**
	 * 保存取保存的首页前20条商品数据
	 * 
	 * @param proData
	 */
	public void saveHomeProData(String proData) {
		mEditor.putString(Constants.KINCAi_HOME_PRO_20_DATA, proData);
		mEditor.commit();
	}

	/**
	 * 获取保存的首页前20条商品数据
	 * 
	 * @return
	 */
	public String getHomeProData() {
		return mSp.getString(Constants.KINCAi_HOME_PRO_20_DATA, null);
	}
	/**
	 * 保存取保存的首页前20条商品路径
	 * 
	 * @param proData
	 */
	public void saveHomeProImagePath(String proImagePath) {
		mEditor.putString(Constants.KINCAi_HOME_PRO_20_IMAGE_PATH, proImagePath);
		mEditor.commit();
	}
	
	/**
	 * 获取保存的首页前20条商品图片路径
	 * 
	 * @return
	 */
	public String getHomeProImagePath() {
		return mSp.getString(Constants.KINCAi_HOME_PRO_20_IMAGE_PATH, null);
	}
	
	/**
	 * 保存是否已经添加了assets里面的省市县/区到数据库
	 * @param isAdd
	 */
	public void saveIsAddProvinceCityZoneToDb(boolean isAdd) {
		mEditor.putBoolean(Constants.KINCAI_IS_ADD_PROVINCE_CITY_ZONE_TO_DB, isAdd);
		mEditor.commit();
	}
	/**
	 * 获取是否已经添加了assets里面的省市县/区到数据库
	 * @return
	 */
	public boolean getIsAddProvinceCityZoneToDb() {
		return mSp.getBoolean(Constants.KINCAI_IS_ADD_PROVINCE_CITY_ZONE_TO_DB, false);
	}
	
	/**
	 * 保存首页gridview标签数据
	 * @param info
	 */
	public void saveHomeGridTab(String info) {
		mEditor.putString(Constants.KINCAI_HOME_GRIDE_TAB_IIFO, info);
		mEditor.commit();
	}
	
	/**
	 * 获取首页gridview标签数据
	 * @return
	 */
	public String getHomeGridTab() {
		return mSp.getString(Constants.KINCAI_HOME_GRIDE_TAB_IIFO, null);
		
	}
	
	/**
	 * 保存设置的图片显示模式
	 * @param mode
	 */
	public void savePictureMode(int mode) {
		mEditor.putInt(Constants.KINCAI_PICTURE_MODE, mode);
		mEditor.commit();
	}
	
	/**
	 * 获取图片显示模式
	 * @return
	 */
	public int getPictureMode() {
		return mSp.getInt(Constants.KINCAI_PICTURE_MODE, Constants.PICTURE_MODE_ORDINARY);
		
	}
	
	
	/**
	 * 
	 * @param 
	 */
	public void saveLockPwdState(boolean state) {
		mEditor.putBoolean(Constants.LOCK_PWD_STATE, state);
		mEditor.commit();
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean getLockPwdState() {
		return mSp.getBoolean(Constants.LOCK_PWD_STATE, false);
		
	}
	
	
	/**
	 * 
	 * @param 
	 */
	public void saveLockPwd(String pwd) {
		mEditor.putString(Constants.LOCK_PWD, pwd);
		mEditor.commit();
	}
	
	/**
	 * 
	 * @return
	 */
	public String getLockPwd() {
		return mSp.getString(Constants.LOCK_PWD, "");
		
	}
	
	

}
