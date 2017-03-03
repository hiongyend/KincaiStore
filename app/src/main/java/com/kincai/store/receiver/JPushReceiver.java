package com.kincai.store.receiver;

import java.util.List;

import com.kincai.store.Constants;
import com.kincai.store.KincaiApplication;
import com.kincai.store.R;
import com.kincai.store.bean.UserInfo;
import com.kincai.store.ui.activity.MainActivity;
import com.kincai.store.utils.LogTest;
import com.kincai.store.utils.SPStorageUtil;
import com.kincai.store.utils.Utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;

public class JPushReceiver extends BroadcastReceiver {

	private static final String TAG = "JPushReceiver";

	@Override
	public void onReceive(final Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		Log.d(TAG, "onReceive - " + intent.getAction());

		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
			
		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
				.getAction())) {
			System.out.println("收到了自定义消息。消息内容是："
					+ bundle.getString(JPushInterface.EXTRA_MESSAGE));
			String msg = bundle.getString(JPushInterface.EXTRA_MESSAGE);
			
			customMessage(context, msg);

		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
				.getAction())) {
			LogTest.LogMsg(TAG,"收到了通知");
			
			
			// 在这里可以做些统计，或者做些其他工作
		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED
				.equals(intent.getAction())) {
			LogTest.LogMsg(TAG,"用户点击打开了通知");
//				Toast.makeText(PushSetActivity.this,"Custom Builder - 2", Toast.LENGTH_SHORT).show();
			// 在这里可以自己写代码去定义用户点击后的行为
			Intent i = new Intent(context, MainActivity.class); // 自定义打开的界面
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
		} else {
			LogTest.LogMsg(TAG, "Unhandled intent - " + intent.getAction());
		}
	}
	
	private void customMessage(Context context, String msg) {
		Context applicationContext = context.getApplicationContext();
		if(applicationContext instanceof KincaiApplication) {
			KincaiApplication mApplication =(KincaiApplication)applicationContext;
			SPStorageUtil mSp = mApplication.getmSp();
			if(mSp.getUserIsLogin()) {
				String[] split = msg.split(",");
				
				if(split[0].equals(mSp.getUsername()) && !split[1].equals(Utils.getUniqueId(context))) {
					List<UserInfo> mUserInfos = mApplication.getmUserDao().getUserInfoOne(mSp.getUsername());
					if(mUserInfos != null && mUserInfos.size() > 0)
						mApplication.getmUserDao().deleteUser(mUserInfos.get(0).getUserId());
					mSp.saveUserIsLogin(false);
					mSp.saveUsername(null);
					
					final Intent intent2 = new Intent();
					final Intent intent3 = new Intent();
					intent2.setAction(Constants.USERINFO_CHANGE_ACTION);
					intent3.setAction("isloginout");
					intent3.putExtra("loginTime", split[2]);
					intent3.putExtra("deviceType", split[3]);
					context.sendBroadcast(intent2);
					context.sendBroadcast(intent3);
					
					
					
				} else if(split[0].equals(mSp.getUsername()) && split[1].equals(Utils.getUniqueId(context))){
					// 自定义消息不会展示在通知栏，完全要开发者写代码去处理
					return;
					
				} else {
					processCustomMessage(context, msg);
				}
				
				
			}
			
		}
	}

	/**
	 * 接收自定义消息
	 * 
	 * @param context
	 * @param msg
	 */
	public void processCustomMessage(Context context, String msg) {
		NotificationManager nManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		
		Notification.Builder notification = new Notification.Builder(context);
		notification.setSmallIcon(R.drawable.icon);
		notification.setContentTitle("天天购物消息提醒");
		notification.setAutoCancel(true);
		notification.setWhen(System.currentTimeMillis());
		//点击通知跳转到。。。
		Intent intent = new Intent(context, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				intent, 0);

		notification.setContentIntent(pendingIntent);
		nManager.notify(R.drawable.ic_launcher, notification.build());
//		nManager.cancel(0);

	}

}
