package com.kincai.store.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kincai.store.Constants;
import com.kincai.store.R;
import com.kincai.store.model.IDialog;
import com.kincai.store.ui.activity.SplashActivity;
import com.kincai.store.ui.activity.base.BaseActivity;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 * 
 * @author kincai
 * 
 * @description 工具类
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.utils
 *
 * @time 2015-7-15 下午9:33:40
 *
 */
public class Utils {

	private static final String TAG = "Utils";
	private static Dialog dialog;
	private static Dialog mDialog;
	private static Dialog mSplashDialog;
	private static Toast toast;
	private static int [] arrs;
	
	/**
	 * 获取View
	 * 
	 * @param context
	 * @param layoutId
	 * @return
	 */
//	public static View getView(Context context, int layoutId) {
//		LayoutInflater inflater = (LayoutInflater) context
//				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		View layout = inflater.inflate(layoutId, null);
//
//		return layout;
//	}

	/**
	 * 获取fragment view
	 * 
	 * @param context
	 * @param viewGroup
	 * @param layoutId
	 * @return
	 */
	public static View getViewFrag(Context context, ViewGroup viewGroup,
			int layoutId) {
//		LayoutInflater.from(context);//from里面的代码其实就是返回 (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		View.inflate(context, resource, root)//其实里面也是经过from返回LayoutInflater 再inflate-> public View inflate(int resource, ViewGroup root, boolean attachToRoot) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(layoutId, viewGroup, false);//public View inflate(int resource, ViewGroup root, boolean attachToRoot) {

		return layout;
	}
	
	public static View getView(Context context, int resId) {
		return View.inflate(context, resId, null);
	}

	/**
	 * 
	 * @param context
	 *            上下文对象
	 * @param isShowExtraTip
	 *            是否显示除了正文提示信息外的其他提示 如版本更新提示的版本号等
	 * @param ExtraTip
	 *            除了正文提示信息外的其他提示信息 如版本更新提示的版本号等
	 * @param ScreenWidth
	 *            屏幕宽度
	 * @param tip_msg
	 *            提示标题
	 * @param message
	 *            提示正文信息
	 * @param leftBtnText
	 *            左边按钮内容
	 * @param rightBtnText
	 *            右边按钮内容
	 * @param listener
	 *            回调接口
	 */
	public static void showDialog(final Context context,
			boolean isShowExtraTip, String ExtraTip, int ScreenWidth,
			String tip_msg, String message, String leftBtnText,
			String rightBtnText, final IDialog listener) {
		if (null != dialog ) {
			dialog = null;
		}

		View view = View.inflate(context, R.layout.dialog_layout, null);
		dialog = new Dialog(context, R.style.lrc_dialog);
//		dialog.setCanceledOnTouchOutside(false);
//		dialog.setCancelable(false);
		Window window = dialog.getWindow();
		// 左边出去
		window.setWindowAnimations(R.style.dialog_anim_fade_out);

		dialog.setContentView(view);
		dialog.setCanceledOnTouchOutside(false);

		Window dialogWindow = dialog.getWindow();
		WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.CENTER);// 居中
		layoutParams.width = (int) (ScreenWidth * 0.8); // 宽度

		dialogWindow.setAttributes(layoutParams);

		Button btnRight = (Button) view.findViewById(R.id.btn_dialog_confirm);
		btnRight.setText(rightBtnText);

		Button btnLeft = (Button) view.findViewById(R.id.btn_dialog_cancel);
		btnLeft.setText(leftBtnText);

		TextView textMessage = (TextView) view
				.findViewById(R.id.text_dialog_message);

		TextView textExtraMsg = (TextView) view
				.findViewById(R.id.text_dialog_extra_message);

		TextView textTip = (TextView) view.findViewById(R.id.dialog_tip);

		textTip.setText(tip_msg);

		if (isShowExtraTip) {
			textExtraMsg.setVisibility(View.VISIBLE);
			textExtraMsg.setText(ExtraTip);

		}
		textMessage.setText(message);

		dialog.show();

		/**
		 * 确定按钮
		 */
		btnRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 关闭对话框
				if (dialog != null) {
					dialog.dismiss();
				}
				// 事件回调
				if (listener != null) {
					listener.onDialogClick();
				}
				return;

			}
		});
		/**
		 * 取消按钮 取消按钮只是关闭对话框 不做别的处理
		 */
		btnLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 关闭对话框
				if (dialog != null) {
					dialog.dismiss();
				}

			}
		});
	}
	
	
	
	/**
	 * 小的提示对话框
	 * @param context
	 * @param ScreenWidth
	 * @param content
	 * @param leftBtnText
	 * @param rightBtnText
	 * @param listener
	 */
	public static void showMiniDialog(final Context context,
			 int ScreenWidth,
			String content, String leftBtnText,
			String rightBtnText, final IDialog listener, boolean isCancel) {
		if (null != mDialog) {
			mDialog.cancel();
		}

		View view = View.inflate(context, R.layout.dialog_mini_layout, null);
		mDialog = new Dialog(context, R.style.lrc_dialog);
		mDialog.setCanceledOnTouchOutside(isCancel);
//		dialog.setCancelable(false);
		Window window = mDialog.getWindow();
		// 左边出去
		window.setWindowAnimations(R.style.dialog_anim_fade_out);

		mDialog.setContentView(view);
		mDialog.setCanceledOnTouchOutside(false);

		Window dialogWindow = mDialog.getWindow();
		WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.CENTER);// 居中
		layoutParams.width = (int) (ScreenWidth * 0.7); // 宽度

		dialogWindow.setAttributes(layoutParams);

		Button btnRight = (Button) view.findViewById(R.id.dialog_confirm_btn);
		btnRight.setText(rightBtnText);
		
		Button center = (Button) view.findViewById(R.id.dialog_confirm_btn2);
		center.setText(rightBtnText);
		
		TextView contentTitle = (TextView) view
				.findViewById(R.id.dialog_content_title);
		

		Button btnLeft = (Button) view.findViewById(R.id.dialog_cancel_btn);
		btnLeft.setText(leftBtnText);
		
		center.setVisibility(!isCancel ? View.VISIBLE : View.GONE);
		btnRight.setVisibility(isCancel ? View.VISIBLE : View.GONE);
		btnLeft.setVisibility(isCancel ? View.VISIBLE : View.GONE);
		contentTitle.setVisibility(!isCancel ? View.VISIBLE : View.GONE);
		contentTitle.setText("下线通知");

		if(!isCancel ) {
			layoutParams.width = (int) (ScreenWidth * 0.8); // 宽度
			dialogWindow.setAttributes(layoutParams);
		}
		
		
		TextView contentTv = (TextView) view
				.findViewById(R.id.dialog_content_tv);


		contentTv.setText(content);

		mDialog.show();
		
		
		center.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 关闭对话框
				if (mDialog != null) {
					mDialog.dismiss();
				}
				// 事件回调
				if (listener != null) {
					listener.onDialogClick();
				}
				return;
			}
		});

		/**
		 * 确定按钮
		 */
		btnRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 关闭对话框
				if (mDialog != null) {
					mDialog.dismiss();
				}
				// 事件回调
				if (listener != null) {
					listener.onDialogClick();
				}
				return;

			}
		});
		/**
		 * 取消按钮 取消按钮只是关闭对话框 不做别的处理
		 */
		btnLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 关闭对话框
				if (mDialog != null) {
					mDialog.dismiss();
				}

			}
		});
	}
	
	/**
	 * 程序其实也广告对话框
	 * @param context
	 * @param listener
	 * @param bitmap
	 */
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public static void showSplashDialog(final Context context,
			final IDialog listener, Bitmap bitmap) {
		if (null != mSplashDialog) {
			mSplashDialog = null;
		}
		final Handler handler = new Handler();
		View view = View.inflate(context, R.layout.dialog_splash_layout, null);
		final TextView splashTimes = (TextView) view.findViewById(R.id.splash_times_tv);
		LinearLayout jumpLayout = (LinearLayout) view.findViewById(R.id.jump_ll);
		if(bitmap != null) {
			view.setBackground(new BitmapDrawable(bitmap));
		}
		
		mSplashDialog = new Dialog(context, android.R.style.Theme_Light_NoTitleBar);
		
		mSplashDialog.setContentView(view);
		mSplashDialog.setCancelable(false);
		mSplashDialog.setCanceledOnTouchOutside(false);
		
		mSplashDialog.show();
		
		jumpLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				listener.onDialogClick();
				handler.removeCallbacksAndMessages(null);
				return;
			}
		});
		
		//计时
		new Thread(){
			int time = 4;
			public void run() {
				while(time>0) {
					((SplashActivity)context).runOnUiThread(new Runnable() {
						public void run() {
							splashTimes.setText(time+"s");
						}
					});
					time--;
					try {
						Thread.sleep(1000);
						
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
				}
				
			}
		}.start();
		
		//3秒后跳转
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				if(mSplashDialog != null)
				listener.onDialogClick();
					
			}
		}, 3000);
		
	}
	
	public static boolean SplashDialogIsShow() {
		if(mSplashDialog != null && mSplashDialog.isShowing()) {
			return true;
		}
		return false;
	}
	
	public static void dismissSplashDialog() {
		if(mSplashDialog != null) {
			mSplashDialog.dismiss();
			mSplashDialog = null;
		}
	}
	
	
	

	/**
	 * 获取当前应用版本name
	 */
	public static String getVersionName(Context context) {
		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(),
					0);
			return info.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			
		}
		
		return null;

	}
	/**
	 * 获取当前应用版本code
	 */
	public static int getVersionCode(Context context) {
		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(),
					0);
			return info.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		
		return 0;
		
	}

	/**
	 * 获取设备唯一标识
	 * 
	 * @param context
	 * @return
	 */
	public static String getUniqueId(Context context) {
		String uniqueIuniqueId = "";
		try {
			TelephonyManager telephonyManager = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			//imei
			String imei = telephonyManager.getDeviceId();
			//sim卡
			String simSerialNumber = telephonyManager.getSimSerialNumber();
			if(simSerialNumber == null || "".equals(simSerialNumber)) {
				simSerialNumber = "tt";
			}
			// androidId
			String androidId = Secure.getString(context.getContentResolver(),
					Secure.ANDROID_ID);

//			showToast(context, " "+imei+" "+simSerialNumber+" "+androidId, 1);
			UUID deviceUuid = new UUID(androidId.hashCode(),
					((long) imei.hashCode() << 32) | simSerialNumber.hashCode());

			uniqueIuniqueId = deviceUuid.toString();
			return uniqueIuniqueId;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "";

		
	}

	/**
	 * 自定义toast
	 * 
	 * @param context
	 * @param msg
	 */
	public static void showToast(Context context, String msg, int toastLenght) {
		DisplayMetrics metric;
		int screenWidth;
		if (null != toast) {
			toast.cancel();
			toast = null;
			metric = null;
		}
		
		 
		metric = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metric);
		screenWidth = metric.widthPixels;

		toast = new Toast(context);
		View toastView = getView(context, R.layout.toast_layout);
		TextView toastMsg = (TextView) toastView.findViewById(R.id.toast_tv);
		toastMsg.setText(msg);
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, screenWidth/3);
		
		toast.setDuration(toastLenght == 0 ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG);// 时间
		toast.setView(toastView);
		toast.show();
	}
	
	public static void dismissToast() {

		if(toast != null ) {
			toast.cancel();
			toast = null;
		}
	}
	
	/**
	 * 根据选择的图片显示模式返回不同的商品列表展示路径
	 * @param context
	 * @return
	 */
	public static String getServerImagePath(Context context, SPStorageUtil mSp) {
		
		
		int mode = mSp.getPictureMode();
		if(mode == Constants.PICTURE_MODE_DEFAULT) {
			//智能模式 那么就根据当前使用的网络状态返回图片路径
			if(NetWorkUtil.isWifi(context)) {
				return Constants.IMAGE_PATH_HIGH_QUALITY;
			} else if (NetWorkUtil.isMobile(context)) {
				return Constants.IMAGE_PATH_DEFAULT;
			} 
			
		} else if(mode == Constants.PICTURE_MODE_HIGH_QUALITY) {
			//高质量模式 
			return Constants.IMAGE_PATH_HIGH_QUALITY;
			
		} else if(mode == Constants.PICTURE_MODE_ORDINARY) {
			//普通模式
			return Constants.IMAGE_PATH_DEFAULT;
		}
		
		
		
		return null;
	}
	
	/**
	 * 根据选择的图片显示模式返回不同的商品详情大图路径
	 * @param context
	 * @return
	 */
	public static String getServerProDetailImagePath(Context context, SPStorageUtil mSp) {
		
	
		int mode = mSp.getPictureMode();
		if(mode == Constants.PICTURE_MODE_DEFAULT) {
			//智能模式 那么就根据当前使用的网络状态返回图片路径
			if(NetWorkUtil.isWifi(context)) {
				return Constants.IMAGE_PATH_HIGH_QUALITY_PRO_DETAIL;
			} else if (NetWorkUtil.isMobile(context)) {
				return Constants.IMAGE_PATH_DEFAULT_PRO_DETAIL;
			} 
			
		} else if(mode == Constants.PICTURE_MODE_HIGH_QUALITY) {
			//高质量模式 
			return Constants.IMAGE_PATH_HIGH_QUALITY_PRO_DETAIL;
			
		} else if(mode == Constants.PICTURE_MODE_ORDINARY) {
			//普通模式
			return Constants.IMAGE_PATH_DEFAULT_PRO_DETAIL;
		}
		
		
		
		return null;
		
	}
	
	
	/**
	 * XUtils里面的使用的方法   指定图片的缩放比例 InSampleSize
	 * @param options
	 * @param maxWidth
	 * @param maxHeight
	 * @return
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options, int maxWidth, int maxHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;//默认比例1

        if (width > maxWidth || height > maxHeight) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) maxHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) maxWidth);
            }

            final float totalPixels = width * height;

            final float maxTotalPixels = maxWidth * maxHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > maxTotalPixels) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }
	
	/**
	 * 官方文档使用方法  指定图片的缩放比例 InSampleSize
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static int calculateInSampleSize2(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		LogTest.LogMsg(TAG, "--))((("+inSampleSize);
		
		return inSampleSize;
	}

	/**
	 * 压缩bitmap的inSampleSize
	 * @param context
	 * @param options
	 * @return
	 */
	public static int getServerBitmapOptionsSampleSize(Context context, BitmapFactory.Options options, int compressBitmapMode, SPStorageUtil mSp) {
		int compressMode = compressBitmapMode;
		int mode = mSp.getPictureMode();
		if(mode == Constants.PICTURE_MODE_DEFAULT) {
			//智能模式 那么就根据当前使用的网络状态返回图片路径
			if(NetWorkUtil.isWifi(context)) {
				if (compressMode == 0) {
					return 2;
				} else {
					return 1;
				}
//				return calculateInSampleSize(options, 720, 1028);
			} else if (NetWorkUtil.isMobile(context)) {
				if (compressMode == 2) {
					return 1;
				} else {
					return 2;
				}
//				return calculateInSampleSize(options, 480, 800);
			} 
			
		} else if(mode == Constants.PICTURE_MODE_HIGH_QUALITY) {
			//高质量模式 
//			return calculateInSampleSize(options, 720, 1028);
			if (compressMode == 0) {
				return 2;
			} else {
				return 1;
			}
			
		} else if(mode == Constants.PICTURE_MODE_ORDINARY) {
			//普通模式
//			return calculateInSampleSize(options, 480, 800);
//			return 2;
			if (compressMode == 2) {
				return 1;
			} else {
				return 2;
			}
		}
		
		return 0;
	}
	
	public static Config getServerBitmapInPreferredConfig(Context context, int compressBitmapMode, SPStorageUtil mSp) {
		int compressMode = compressBitmapMode;
		int mode = mSp.getPictureMode();
		if(mode == Constants.PICTURE_MODE_DEFAULT) {
			//智能模式 那么就根据当前使用的网络状态返回图片路径
			if(NetWorkUtil.isWifi(context)) {
				
				if (compressMode == 0) {
					return Config.RGB_565;
					
				} else {
					return Config.ARGB_8888;
				}
				
			} else if (NetWorkUtil.isMobile(context)) {

				if (compressMode == 2) {
					return Config.ARGB_8888;
				} else {
					return Config.RGB_565;
				}
			} 
			
		} else if(mode == Constants.PICTURE_MODE_HIGH_QUALITY) {
			//高质量模式 
			if (compressMode == 0) {
				return Config.RGB_565;
			} else {
				return Config.ARGB_8888;
			}
			
		} else if(mode == Constants.PICTURE_MODE_ORDINARY) {
			//普通模式
			if (compressMode == 2) {
				return Config.ARGB_8888;
			} else {
				return Config.RGB_565;
			}
		}
		
		return null;
	}
	
	public static Config getServerBigBitmapInPreferredConfig(Context context, int compressBitmapMode, SPStorageUtil mSp) {
		int compressMode = compressBitmapMode;
		
		int mode = mSp.getPictureMode();
		if(mode == Constants.PICTURE_MODE_DEFAULT) {
			//智能模式 那么就根据当前使用的网络状态返回图片路径
			if(NetWorkUtil.isWifi(context)) {
				
				//高质量模式 
				if (compressMode == 0) {
					return Config.RGB_565;
					
				} else {
					return Config.ARGB_8888;
				}
				
			} else if (NetWorkUtil.isMobile(context)) {

				//普通模式
				 if(compressMode == 0){
					return Config.ARGB_4444;
				} else {
					return Config.ARGB_8888;
				}
			} 
			
		} else if(mode == Constants.PICTURE_MODE_HIGH_QUALITY) {
			//高质量模式 
			if (compressMode == 0) {
				return Config.ARGB_4444;
				
			} else {
				return Config.ARGB_8888;
			}
			
			
		} else if(mode == Constants.PICTURE_MODE_ORDINARY) {
			//普通模式
			if(compressMode == 0){
				return Config.ARGB_4444;
			} else {
				return Config.ARGB_8888;
			}
		}
		
		return null;
	}
	
	
	/**
	 * 根据手机内存和手机分辨率返回压缩图片的模式
	 * @param context
	 * @return num 0 低配 1中端  2 高端
	 */
	public static int  accordingToPhoneConfigurationInfoCompressBitmap(Context context) {
		int []arrs = getTheScreenResolution(context);
		int screenWidth = arrs[0], screenHeight = arrs[1];
		long totalMemory = getTotalMemory(context);
		
		if(totalMemory < 500) {
			
			return 0;
			
		} else if (totalMemory < 1000 && totalMemory >= 500) {
			if(screenWidth < 480 && screenHeight < 800) {
				return 0;
			} else {
				return 1;
			}
		} else if (totalMemory < 2000 && totalMemory >=1000 ) {
			if((screenWidth <= 720 && screenWidth > 480) && (screenHeight <= 1200 && screenHeight > 780)) {
				return 1;
			} else if ((screenWidth <= 1080 && screenWidth > 720) && (screenHeight <= 1920 && screenHeight > 1200)) {
				return 2;
			}
		} else if(totalMemory >= 2000) {
			return 2;
		}
		
		return 0;
		
	}
	
	/**
	 * 获取总运存大小
	 * @param context
	 * @return
	 */
	public static long getTotalMemory(Context context) {
		String str1 = "/proc/meminfo";// 系统内存信息文件
		String str2;
		String[] arrayOfString;
		long initial_memory = 0;

		try {
			FileReader localFileReader = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(
					localFileReader, 8192);
			str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小

			arrayOfString = str2.split("\\s+");
//			for (String num : arrayOfString) {
//				// Log.i(str2, num + "\t");
//			}

			initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
			localBufferedReader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(initial_memory > 0) {
			LogTest .LogMsg(TAG, "当前手机内存："+(initial_memory / (1024 * 1024)));
			return initial_memory / (1024 * 1024);
		}
		
		// return Formatter.formatFileSize(context, initial_memory);//
		// Byte转换为KB或者MB，内存大小规格化
		// System.out.println("总运存---$amp;>amp;>amp;>amp;$quot;+initial_memory/(1024*1024));
		return 0;

	}

	/**
	 * 获取屏幕分辨率
	 * 
	 * @param context
	 */
	public static int[] getTheScreenResolution(Context context) {
		
		if(arrs == null) {
			arrs = new int[2];
		}
		
		int mScreenWidth = ((BaseActivity)context).mScreenWidth;
		int mScreenHeight = ((BaseActivity)context).mScreenHeight;
		LogTest.LogMsg(TAG, "当前手机分辨率："+mScreenWidth+"*"+mScreenHeight);
		arrs[0] = mScreenWidth;
		arrs[1] = mScreenHeight;
		return arrs;
	}
	
	/**
	 *  使用系统当前日期加以调整md5加密作为照片的名称
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("'userface'_yyyyMMdd_HHmmss");
		return EncryptionUtil.md5Encryption(sdf.format(date))+".png";
	}

	
	public static String getRightData(String response) {
		
		if(TextUtils.isEmpty(response)) return null;
		if(response.contains("<script")) {
			int indexOf = response.indexOf("<");
			return response.substring(0, indexOf);
		}
		
		return null;
		
		
		
		
		
	}
	
}
