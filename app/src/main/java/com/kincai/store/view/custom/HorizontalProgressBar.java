package com.kincai.store.view.custom;

import com.kincai.store.R;
import com.kincai.store.ui.activity.base.BaseActivity;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 *
 * @author kincai
 * 
 * @description 水平进度条
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.view.custom
 *
 * @time 2015-9-20 上午8:26:55
 *
 */
public class HorizontalProgressBar extends View {
	
	int mProgress;//进度
	int mMaxProgress;//最大进度
	private int mProgressColor;
	int mBorder;//宽度
	int mRatio;//比例
	int mScreenWidth = ((BaseActivity)getContext()).mScreenWidth;//屏幕宽度
	Paint mPaint;//画笔
	
	public HorizontalProgressBar(Context context) {
		this(context, null);
	}
	
	public HorizontalProgressBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public HorizontalProgressBar(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init();
		
		
		if(attrs != null) {
			TypedArray a = context.obtainStyledAttributes(attrs,
					R.styleable.HorizontalProgressBar);
			mBorder = a.getDimensionPixelSize(R.styleable.HorizontalProgressBar_border, 5);
			mProgressColor = a.getColor(R.styleable.HorizontalProgressBar_progress_color, mProgressColor);
			
			a.recycle();
		}
	}
	
	/**
	 * 初始化
	 */
	private void init() {
		mProgress = 0;
		mMaxProgress = 100;
		mRatio = mScreenWidth / mMaxProgress;
		mProgressColor = getContext().getResources().getColor(R.color.approach_red);
		mPaint = new Paint();
		mPaint.setColor(mProgressColor);
	}


	@Override
	protected void onDraw(Canvas canvas) {
		
		canvas.drawRect(0, 0, mProgress, mBorder, mPaint);
	}
	

	/**
	 * 设置进度
	 * @param progress
	 */
	public void setProgress(int progress) {
		this.mProgress = mRatio * progress;
		postInvalidate();
	}
	
	/**
	 * 设置最大进度
	 * @param maxProgress
	 */
	public void setMaxProgress(int maxProgress) {
		this.mMaxProgress = maxProgress;
		mRatio = mScreenWidth / mMaxProgress;
	}
	
}
