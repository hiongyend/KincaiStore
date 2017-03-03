package com.kincai.store.view.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.SeekBar;

public class ScrollSeekBar extends SeekBar {

	public ScrollSeekBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public ScrollSeekBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ScrollSeekBar(Context context) {
		super(context);
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		
		getParent().requestDisallowInterceptTouchEvent(true);
		return super.dispatchTouchEvent(event);
	}
	
	
}
