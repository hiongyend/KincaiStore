package com.kincai.store.common;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.kincai.store.Constants;
import com.kincai.store.model.IDialog;
import com.kincai.store.thread.CachedThreadPoolUtils;
import com.kincai.store.thread.DownLoadApk;
import com.kincai.store.utils.AnimationUtil;
import com.kincai.store.utils.JsonUtil;
import com.kincai.store.utils.LoadingDialog;
import com.kincai.store.utils.LogTest;
import com.kincai.store.utils.NetWorkUtil;
import com.kincai.store.utils.SPStorageUtil;
import com.kincai.store.utils.Utils;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 *
 * @author kincai
 * 
 * @todo 版本更新公共类
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.common
 *
 * @time 2015-6-12 上午8:31:19
 *
 */
public class AppUpgrade {

	private static final String TAG = "AppUpgrade";
	private LoadingDialog mLoadingDialog;
	private Message msg;
	/** apk下载url*/
	private String mApkUrl;
	private Context mContext;
	private Handler mMyHandler;
	private int mScreenWidth;
	private SPStorageUtil mSp;

	/**
	 * 版本更新工具类
	 * 
	 * @param msg
	 *            message
	 * @param context
	 *            上下文
	 * @param mScreenWidth
	 *            屏幕宽度
	 * @param mMyHandler
	 *            handler
	 */
	public AppUpgrade(Message msg, Context context, int mScreenWidth,
			Handler mMyHandler, SPStorageUtil sp) {
		this.msg = msg;
		this.mContext = context;
		this.mScreenWidth = mScreenWidth;
		this.mMyHandler = mMyHandler;
		mLoadingDialog = new LoadingDialog(mContext, false);
		mSp = sp;

	}

	/**
	 * 检查更新判断
	 */
	public void versionJudge(boolean flag) {

		LogTest.LogMsg(TAG, "正在检查更新");
		// mLoadingDialog.dismiss();
		String jsonStringAppVersion = (String) msg.obj;
		if (jsonStringAppVersion != null) {

			List<Map<String, Object>> mJsonList3 = new ArrayList<Map<String, Object>>();
			
			if(!"200".equals(JsonUtil.msgJson("code", jsonStringAppVersion)) ) {
				return;
			}
				
			mJsonList3 = JsonUtil.listJson("data", jsonStringAppVersion);
			
			if(mJsonList3 == null) {
				return;
			}
			String versionName = (String) mJsonList3.get(0).get("version_name");
		

			mApkUrl = Constants.SERVER_URL + "app_apk/"
					+ mJsonList3.get(0).get("apk_url").toString();
			int versionCode = Utils.getVersionCode(mContext);

			LogTest.LogMsg(TAG, versionCode + " 当前应用版本");
			LogTest.LogMsg(TAG, mJsonList3.get(0).get("version_code")
					.toString()
					+ " 网络获取版本");
			
			if(flag) { //为true 就是首页进来自动检查
				//开启wifi自定更新
				if(mSp.getWifiAutomateUpgradeChecked()) {
				
					//在开启WiFi自动更新的情况下 如果WiFi网络 那么自动去下载  如果不是WiFi网络 那么弹出对话框
					if (!NetWorkUtil.isWifi(mContext)) {
						dialogVersionShow(versionCode, versionName);
					} else if (NetWorkUtil.isWifi(mContext) ) {
						
						CachedThreadPoolUtils.execute(new DownLoadApk(mApkUrl,
								mMyHandler));
					} 
				
				} else {//没有开启WiFi自定更新 直接弹出对话框
					
					dialogVersionShow(versionCode, versionName);
					
				}
			
			} else {// 设置那里手动检查更新 

				dialogVersionShow(versionCode, versionName);

			}
		} else {
//			Utils.showToast(mContext, "版本检查失败", Toast.LENGTH_SHORT);
		}

	}
	
	/**
	 * 版本更新对话框显示
	 * @param versionCode
	 * @param versionName
	 */
	private void dialogVersionShow(int versionCode, String versionName) {
		Utils.showDialog(mContext,true,"当前版本：" + versionCode+ "\n最新版本："
				+ versionName,mScreenWidth, "升级提醒",
		"可以下载更新啦\n\n1、优化搜索列表\n2、实时聊天哦", "稍后更新", "立即更新",upGrade);

	}

	/**
	 * 更新确定移动网络时下载操作
	 */
	private IDialog netTip = new IDialog() {

		@Override
		public void onDialogClick() {
			// TODO Auto-generated method stub
			if (NetWorkUtil.isNetworkAvailable(mContext)) {
				LogTest.LogMsg(TAG, "移动网络下载apk");
				mLoadingDialog.dialogShow();
				CachedThreadPoolUtils.execute(new DownLoadApk(mApkUrl,
						mMyHandler));
			}

		}
	};

	/**
	 * 版本更新确认后操作
	 */
	private IDialog upGrade = new IDialog() {
		//
		@Override
		public void onDialogClick() {
			if (NetWorkUtil.isNetworkAvailable(mContext)) {
				LogTest.LogMsg(TAG, "跟新前网络判断");
				if (NetWorkUtil.isMobile(mContext)) {
					Utils.showDialog(mContext, false, "", mScreenWidth, "网络提示",
							"当前为移动网络 确定要更新？", "取消", "确定", netTip);
				} else if (NetWorkUtil.isWifi(mContext)) {
					LogTest.LogMsg(TAG, "wifi网络下载apk");
					mLoadingDialog.dialogShow();
					CachedThreadPoolUtils.execute(new DownLoadApk(mApkUrl,
							mMyHandler));
				}
			}

		}
	};

	/**
	 * 安装apk
	 * 
	 * @param msg
	 */
	public void updateApp(Message msg) {
		 mLoadingDialog.dismiss();
		String respString = (String) msg.obj;
		if (null != respString && respString.equals("200")) {
			LogTest.LogMsg(TAG, "正在安装。。。");
			
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(new File(Environment
					.getExternalStorageDirectory(),
					Constants.APP_DOWNLOAD_APK_NAME)),
					"application/vnd.android.package-archive");
			mContext.startActivity(intent);
			AnimationUtil.startHaveSinkActivityAnimation((Activity) mContext);
		} else {
			Utils.showToast(mContext, "更新失败", Toast.LENGTH_SHORT);
		}

	}

}
