package com.kincai.store.common;

import com.kincai.store.model.IDialog;
import com.kincai.store.thread.CachedThreadPoolUtils;
import com.kincai.store.thread.HttpGetThread;
import com.kincai.store.ui.activity.base.BaseActivity;
import com.kincai.store.utils.NetWorkUtil;
import com.kincai.store.utils.Utils;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

public class OrderUpdateRequest {


	public static void sendRequest(final Context context, final Handler handler, final String url, String title, String tip) {
		
		Utils.showDialog(context, false, "", ((BaseActivity)context).mScreenWidth, title, tip, "取消", "确定", new IDialog() {
			
			@Override
			public void onDialogClick() {
				if(NetWorkUtil.isNetworkAvailable(context)) {
					CachedThreadPoolUtils.execute(new HttpGetThread(handler, url, "orderUpdate"));
				} else {
					Utils.showToast(context, "网络异常！！！", Toast.LENGTH_SHORT);
				}
			}
		});
		
		
		
	}
}
