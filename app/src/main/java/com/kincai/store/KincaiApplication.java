package com.kincai.store;

import java.util.LinkedList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.res.Configuration;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.widget.TextView;


import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;
import com.easemob.chat.EMChat;
import com.iflytek.cloud.SpeechUtility;
import com.kincai.store.db.UserDao;
import com.kincai.store.utils.LogTest;
import com.kincai.store.utils.SPStorageUtil;
import com.kincai.store.utils.bitmap_cache.MyImageLoader;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 *
 * @author kincai
 * 
 * @description TODO
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store
 *
 * @time 2015-7-24 下午7:56:52
 *
 */
/**
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 *
 * @author kincai
 * 
 * @description TODO
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store
 *
 * @time 2015-9-10 上午10:23:33
 *
 */
public class KincaiApplication extends Application {
	
	
	public LocationClient mLocationClient;
	public GeofenceClient mGeofenceClient;
	public MyLocationListener mMyLocationListener;
	public Vibrator mVibrator;
	public TextView mLocationResult;
	
	
	private List<Activity> activityList = new LinkedList<Activity>();  
	@SuppressLint("UseSparseArrays")
    
    private static SPStorageUtil mSp;
    public static DisplayMetrics metric;
    
    private static MyImageLoader mImageLoader;
    private static KincaiApplication mKincaiApplication;
    private static int mScreenWidth;
	
    private static UserDao mUserDao;
    
    @Override
    public void onCreate() {
    	
    	mSp = new SPStorageUtil(getApplicationContext());
    	metric = new DisplayMetrics();
    	mUserDao = new UserDao(getApplicationContext());
       
		// 应用程序入口处调用，避免手机内存过小，杀死后台进程后通过历史intent进入Activity造成SpeechUtility对象为null
		// 如在Application中调用初始化，需要在Mainifest中注册该Applicaiton
		// 注意：此接口在非主进程调用会返回null对象，如需在非主进程使用语音功能，请增加参数：SpeechConstant.FORCE_LOGIN+"=true"
		// 参数间使用半角“,”分隔。
		// 设置你申请的应用appid,请勿在'='与appid之间添加空格及空转义符

    	SpeechUtility.createUtility(this, new StringBuilder().append("appid=").append(Constants.VOICE_APPID).toString());
		//设置成false时关闭语音云SDK日志打印
		com.iflytek.cloud.Setting.setShowLog(false);
    	
    	super.onCreate();
    	mKincaiApplication = this;
    	EMChat.getInstance().init(this);
    	 
    	/**
    	 * debugMode == true 时为打开，sdk 会在log里输入调试信息
    	 * @param debugMode
    	 * 在做代码混淆的时候需要设置成false
    	 */
    	EMChat.getInstance().setDebugMode(true);//在做打包混淆时，要关闭debug模式，避免消耗不必要的资源
    	
    	mLocationClient = new LocationClient(this.getApplicationContext());
		mMyLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mMyLocationListener);
		mGeofenceClient = new GeofenceClient(getApplicationContext());
		
		mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
		
    }
    
    public static KincaiApplication getKincaiApplication() {
    	return mKincaiApplication;
    }
    
    
    public MyImageLoader getmImageLoader() {
		return mImageLoader;
	}

	public void setmImageLoader(MyImageLoader mImageLoader) {
		KincaiApplication.mImageLoader = mImageLoader;
	}

	public SPStorageUtil getmSp() {
		return mSp;
	}

	public DisplayMetrics getMetric() {
		return metric;
	}

	public UserDao getmUserDao() {
		return mUserDao;
	}
	

	public int getmScreenWidth() {
		return mScreenWidth;
	}

	public void setmScreenWidth(int mScreenWidth) {
		KincaiApplication.mScreenWidth = mScreenWidth;
	}


	/**
	 * 实现定位回调监听
	 */
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			
			try {
				StringBuffer sb = new StringBuffer(125);
				sb.append(location.getProvince().substring(0, location.getProvince().length()-1));
				sb.append(">"+location.getCity());
				sb.append(">"+location.getDistrict());
				
				logMsg(sb.toString());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}


	}
	
	/**
	 * 显示请求字符串
	 * @param str
	 */
	public void logMsg(String str) {
		try {
			if (mLocationResult != null)
				
				mLocationResult.setText("配送至 "+str+" 快递 0.0元");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    
    /**
     *  添加Activity到容器中  
     * @param activity
     */
    public void addActivity(Activity activity) {
    	if(activity == null) return;
        activityList.add(activity); 
        LogTest.LogMsg("MyApplication", "添加addActivty "+activity.getClass().getSimpleName());
    }  
  
//    public void addMinaActivity(Activity activity, int key) {  
//    	
//        mainActivityMap.put(key, activity);
//        LogTest.LogMsg("MyApplication",  "添加MainActivity"+key+mainActivityMap.get(key).getClass().getSimpleName());
//        
//    }  
    /**
     *  关闭除了MainActivty之外的activity  
     */
    public void exitInAdditionToTheMainAtivityOfOtherActivities() {  
    	if(activityList == null || activityList.size() == 0) return;
        for (Activity activity : activityList) {
        	if(!activity.getClass().getSimpleName().equals("MainActivity")) {
        		LogTest.LogMsg("MyApplication", "关闭除了MainActivity "+activity.getClass().getSimpleName());
        		 activity.finish();  
        	}
           
        }  
        activityList.clear(); 
    } 
    
    /**
     * 退出指定activity集
     * @param activities
     */
    public void exitMoreActivity(Activity...activities) {
    	
    	if(activityList == null || activities == null || activityList.size() == 0 || activities.length == 0) return;
//    	int length = activities.length;
//    	int size = activityList.size();
    	LogTest.LogMsg("MyApplication", "activityList.size()  "+activityList.size());
    	for(int i = 0; i < activityList.size(); i++) {
    		
    		for (int j = 0; j < activities.length; j++) {
				if(activityList.get(i).getClass().equals(activities[j].getClass().getSimpleName())) {
					LogTest.LogMsg("MyApplication","移除activity "+activities[j].getClass().getSimpleName());
					activities[j].finish();
					activityList.remove(i);
					
					break;
				}
			}
    		
    		LogTest.LogMsg("MyApplication", "activityList.size()  "+activityList.size());
    	}
	}
    
    /**
     * 退出指定activity集
     * @param activities
     */
    public void exitMoreActivity(String...activitySimpleName) {
    	
    	if(activityList == null || activitySimpleName == null || activityList.size() == 0 || activitySimpleName.length == 0) return;
//    	int length = activities.length;
//    	int size = activityList.size();
    	LogTest.LogMsg("MyApplication", "activityList.size()  "+activityList.size());
    	for(int i = 0; i < activityList.size(); i++) {
    		
    		for (int j = 0; j < activitySimpleName.length; j++) {
				if(activityList.get(i).getClass().getSimpleName().equals(activitySimpleName[j])) {
					LogTest.LogMsg("MyApplication","移除activity "+activitySimpleName[j]);
//					activities[j].finish();
					activityList.get(i).finish();
					activityList.remove(i);
					
					break;
				}
			}
    		
    		LogTest.LogMsg("MyApplication", "activityList.size()  "+activityList.size());
    	}
	}
    
    
    /**
     * activity栈size
     * @return
     */
    public int activityListSize() {
    	if(activityList == null) return 0;
    	System.out.println("--->"+activityList.size());
    	return activityList.size();
    }
//    
    /**
     * 是否指定activity
     * @param activity
     * @return
     */
    public boolean isMyActivity(Activity activity) {
    	if(activityList != null && activity != null) 
    	for(Activity activitys : activityList) {
    		if(activitys.getClass().getSimpleName().equals(activity.getClass().getSimpleName())) {
    			return true;
    		} 
    		
    	}
    	return false;
    	
    }
    
    /**
     * 退出整个进程
     */
    public void remove() {
    	if(activityList == null || activityList.size()==0) return;
    	for(int i = 0; i < activityList.size(); i++) {
    		activityList.get(i).finish();
    		activityList.remove(i);
    	}
    	
    	System.exit(0);
    }
	
    /**
     * 退出指定一个activity
     * @param activity
     */
    public void exitOneActivity(Activity activity) {
    	if(activityList == null || activityList.size()==0 || activity == null) return;
    	int activityListSize = activityList.size();
    	for(int i = 0; i < activityListSize; i++) {
    		if(activityList.get(i).getClass().getSimpleName().equals(activity.getClass().getSimpleName())) {
    			activityList.get(i).finish();
        		activityList.remove(i);
        		LogTest.LogMsg("MyApplication", "移除one的activity  "+activity.getClass().getSimpleName());
        		
        		return;
    		}
    		
    	}
    	
	}
	
	public void onTerminate() {
		super.onTerminate();
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
	
	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}
	
}
