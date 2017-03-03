package com.kincai.store.ui.activity.base;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.kincai.store.utils.AnimationUtil;
import com.kincai.store.utils.LogTest;

import android.view.MotionEvent;
import android.view.VelocityTracker;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 * 
 * @author kincai
 * 
 * @description 右滑可以关闭Activity的基类
 * 
 * @project Kincai_Store
 * 
 * @package com.kincai.store.ui
 * 
 * @time 2015-7-13 上午10:18:29
 * 
 */
public abstract class BaseSlideActivity extends BaseActivity {
	
	private static final String TAG = "BaseSlideActivity";
	
	/** 手指上下滑动时的最小速度*/
	protected static final int YSPEED_MIN = 1000;

	/** 手指向右滑动时的最小距离*/
	protected static final int XDISTANCE_MIN = 50;

	/** 手指向上滑或下滑时的最小距离*/
	protected static final int YDISTANCE_MIN = 36;

	/** 记录手指按下时的横坐标*/
	protected float xDown;

	/** 记录手指按下时的纵坐标。*/
	protected float yDown;

	/** 记录手指移动时的横坐标*/
	protected float xMove;

	/** 记录手指移动时的纵坐标*/
	protected float yMove;
	
	
	protected int distanceX;
	protected int distanceY;
	
	protected int ySpeed;

	/** 用于计算手指滑动的速度*/
	protected VelocityTracker mVelocityTracker;
	
	/** 科大讯飞语音识别初始化监听*/
	public InitListener mInitListener;

	/**
	 * 事件分发 处理右滑结束activity
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		createVelocityTracker(event);
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			xDown = event.getRawX();
			yDown = event.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			xMove = event.getRawX();
			yMove = event.getRawY();
			// 滑动的距离
			int distanceX = (int) (xMove - xDown);
			int distanceY = (int) (yMove - yDown);
			// 获取顺时速度
			ySpeed = getScrollVelocity();
			// 关闭Activity需满足以下条件：
			// 1.x轴滑动的距离>XDISTANCE_MIN
			// 2.y轴滑动的距离在YDISTANCE_MIN范围内 上下范围(y坐标差值distanceY正负)
			// 3.y轴上（即上下滑动的速度）<XSPEED_MIN，如果大于，则认为用户意图是在上下滑动而非左滑结束Activity
			if (distanceX > XDISTANCE_MIN
					&& (distanceY < YDISTANCE_MIN && distanceY > -YDISTANCE_MIN)
					&& ySpeed < YSPEED_MIN) {
				finish();
				
				AnimationUtil.finishHaveFloatActivityAnimation(this);
			}
			break;
		case MotionEvent.ACTION_UP:
			recycleVelocityTracker();
			break;
		default:
			break;
		}
		return super.dispatchTouchEvent(event);

	}

	/**
	 * 创建VelocityTracker对象，并将触摸界面的滑动事件加入到VelocityTracker当中。
	 * 
	 * @param event
	 * 
	 */
	public void createVelocityTracker(MotionEvent event) {
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);
	}

	/**
	 * 回收VelocityTracker对象。
	 */
	public void recycleVelocityTracker() {
		mVelocityTracker.recycle();
		mVelocityTracker = null;
	}
	
	/**
	 * 初始化科大讯飞语音初始化监听
	 */
	public void setInitListenet() {
		/**
		 * 初始化语音听写监听器。
		 */
		 mInitListener = new InitListener() {

			@Override
			public void onInit(int code) {
				LogTest.LogMsg(TAG, "SpeechRecognizer init() code = " + code);
				if (code != ErrorCode.SUCCESS) {
					LogTest.LogMsg(TAG, "初始化失败，错误码：" + code);
				}
			}
		};
	}

	/**
	 * 
	 * @return 滑动速度，以每秒钟移动了多少像素值为单位。
	 */
	public int getScrollVelocity() {
		// 1000速度
		mVelocityTracker.computeCurrentVelocity(1000);
		int velocity = (int) mVelocityTracker.getYVelocity();
		return Math.abs(velocity);
	}
	
	@Override
	protected void onRestart() {
		LogTest.LogMsg(TAG, "BaseSlideActivity-onRestart");
		super.onRestart();
	}
	
	@Override
	protected void onStart() {
		LogTest.LogMsg(TAG, "BaseSlideActivity-onStart");
		super.onStart();
		
	}

	@Override
	protected void onResume() {
		LogTest.LogMsg(TAG, "BaseSlideActivity-onResume");
		super.onResume();
		
	}

	@Override
	protected void onPause() {
		LogTest.LogMsg(TAG, "BaseSlideActivity-onPause");
		super.onPause();
		
	}
	
	@Override
	protected void onStop() {
		LogTest.LogMsg(TAG, "BaseSlideActivity-onStop");
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		LogTest.LogMsg(TAG, "BaseSlideActivity-onDestroy");
		super.onDestroy();
		
	}

}
