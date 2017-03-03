package com.kincai.store.view.custom;


import com.kincai.store.R;
import com.kincai.store.model.IEditTextVoiceListener;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 * 
 * @author kincai
 * 
 * @description 在焦点变化时和输入内容发生变化时均要判断是否显示右边clean图标 并且可在布局设置是否有语音识别按钮
 * 
 * @project Kincai_Store
 * 
 * @package com.kincai.store.view.custom
 * 
 * @time 2015-7-15 下午9:25:19
 * 
 */
public class CleanableAndVoiceEditTextView extends EditText {
	private Context mContext;
	private Drawable mRightDrawable;
	/** 是否获取焦点*/
	private boolean isHasFocus;
	private boolean isOpenVoice;
	private boolean isCanVoice;
	
	private IEditTextVoiceListener iEditTextVoiceListener;

	public CleanableAndVoiceEditTextView(Context context) {
		super(context);
		mContext = context;
		init();
	}

	public CleanableAndVoiceEditTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		if(attrs != null) {
			TypedArray a = context.obtainStyledAttributes(attrs,
					R.styleable.CleanableAndVoiceEditTextView);
			isOpenVoice = a.getBoolean(R.styleable.CleanableAndVoiceEditTextView_have_voice_icon, false);
			
			a.recycle();
		}
		
		init();
	}

	public CleanableAndVoiceEditTextView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		init();
	}

	/**
	 * 初始化
	 */
	private void init() {
		// getCompoundDrawables:
		// Returns drawables for the left, top, right, and bottom borders.
		Drawable[] drawables = this.getCompoundDrawables();

		// 取得right位置的Drawable
		// 即我们在布局文件中设置的android:drawableRight
		mRightDrawable = drawables[2];

		// 设置焦点变化的监听
		this.setOnFocusChangeListener(new FocusChangeListenerImpl());
		// 设置EditText文字变化的监听
		this.addTextChangedListener(new TextWatcherImpl());
		// 初始化时让右边clean图标不可见
		setClearDrawableVisible(false);
	}
	
	/**
	 * 当手指抬起的位置在clean的图标的区域 我们将此视为进行清除操作 
	 * getWidth():得到控件的宽度
	 * event.getX():抬起时的坐标(改坐标是相对于控件本身而言的)
	 * getTotalPaddingRight():clean的图标左边缘至控件右边缘的距离
	 * getPaddingRight():clean的图标右边缘至控件右边缘的距离 
	 * 于是: getWidth() - getTotalPaddingRight()
	 * 表示: 控件左边到clean的图标左边缘的区域   即图标左边缘x坐标
	 * getWidth() - getPaddingRight()
	 * 表示: 控件左边到clean的图标右边缘的区域  即图标右边缘x坐标
	 * 所以这两者之间的区域刚好是clean的图标的区域
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_UP:
			
			//点击的返回是不是在drawableRight图标范围   大于左边缘 并且小于右边缘 
			boolean isClean = (event.getX() > (getWidth() - getTotalPaddingRight()))
				&& (event.getX() < (getWidth() - getPaddingRight()));
			
			
			//输入框为空 显示的是语音识别图标并且点击范围在drawableRight图标范围内 那么回调给界面
			if(isCanVoice && isClean) {
				if(iEditTextVoiceListener != null)
				iEditTextVoiceListener.onVoiceClick();
			}
			//输入框内容不为空 显示的是清除图标并且点击范围在drawableRight图标范围内 那么清除输入框内容
			else if(!isCanVoice && isClean) {
				setText("");
			}
			
			break;

		}
		return super.onTouchEvent(event);
	}

	/**
	 * 焦点监听
	 */
	private class FocusChangeListenerImpl implements OnFocusChangeListener {
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			isHasFocus = hasFocus;
			if (isHasFocus) {
				boolean isVisible = getText().toString().length() >= 1;
				setClearDrawableVisible(isVisible);
			} else {
				setClearDrawableVisible(false);
			}
		}

	}

	/**
	 * 当输入结束后判断是否显示右边clean的图标
	 */
	private class TextWatcherImpl implements TextWatcher {
		@Override
		public void afterTextChanged(Editable s) {
			boolean isVisible = getText().toString().length() >= 1 && isFocused() ;
			setClearDrawableVisible(isVisible);
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {

		}

	}

	/**
	 * 隐藏或者显示右边clean的图标
	 * 
	 * @param isVisible
	 */
	protected void setClearDrawableVisible(boolean isVisible) {
		Drawable rightDrawable;
		if (isVisible) {
			rightDrawable = mRightDrawable;
			//不可以点击语音识别图标
			isCanVoice = false;
		} else {//当输入框内容为空的时候才判断下面是否有语音识别按钮
			
			//当打开了语音按钮功能 即drawableRight图片有语音那个图标
			if(isOpenVoice) {
				rightDrawable = mContext.getResources().getDrawable(R.drawable.ic_search_voice_light);
				rightDrawable.setBounds(getCompoundDrawables()[2].getBounds());
				//可以点击语音识别图标
				isCanVoice = true;
			} else {//没有开启 语音按钮功能 即drawableRight图片有语音那个图标
				rightDrawable = null;
				//不可以点击语音识别图标
				isCanVoice = false;
			}
			
		}
		// 使用代码设置该控件left, top, right, and bottom处的图标
		setCompoundDrawables(getCompoundDrawables()[0],
				getCompoundDrawables()[1], rightDrawable,
				getCompoundDrawables()[3]);
	}
	/**
	 * 显示一个动画,以提示用户输入
	 * 
	 * @param CycleTimes
	 *            动画重复次数
	 * @param duration
	 *            持续时间
	 */
	public void setShakeAnimation(int cycleTimes, int duration) {
		this.setAnimation(shakeAnimation(cycleTimes, duration));
	}

	/**
	 * CycleTimes 动画重复的次数
	 * 
	 * @param CycleTimes
	 *            动画重复次数
	 * @param duration
	 *            持续时间
	 * @return 动画
	 */
	public Animation shakeAnimation(int CycleTimes, int duration) {
		Animation translateAnimation = new TranslateAnimation(0, 10, 0, 10);
		translateAnimation.setInterpolator(new CycleInterpolator(CycleTimes));
		translateAnimation.setDuration(duration);
		return translateAnimation;
	}
	
	/**
	 * 设置科大语音voice监听
	 * 
	 * @param iEditTextVoiceListener edittext语音图片点击监听
	 */
	public void setEditTextVoiceListener(
			IEditTextVoiceListener iEditTextVoiceListener) {

		this.iEditTextVoiceListener = iEditTextVoiceListener;
	}

}