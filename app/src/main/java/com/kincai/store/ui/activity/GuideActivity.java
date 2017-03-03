package com.kincai.store.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.kincai.store.R;
import com.kincai.store.utils.AnimationUtil;
import com.kincai.store.utils.DensityUtils;
import com.kincai.store.utils.LogTest;
import com.kincai.store.utils.SPStorageUtil;
import com.kincai.store.view.adapter.GuideViewPagerAdapter;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 * 
 * @author kincai
 * 
 * @description 导航界面
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.ui
 *
 * @time 2015-7-23 上午10:17:49
 *
 */
public class GuideActivity extends Activity implements OnPageChangeListener,
		OnClickListener {
	
	private static final String TAG = "GuideActivity";
	private ViewPager mViewPager;
	private Button mStartBtn;
	private LinearLayout mDotLl;
	private View mRedPoint;
	private ImageView mJumpIv;
	
	private GuideViewPagerAdapter mPagerAdapter;
	private List<ImageView> mPagerImageList;
	private int[] mIds = { R.drawable.bg_guide_new_1,
			R.drawable.bg_guide_new_2, R.drawable.bg_guide_new_3,
			R.drawable.bg_guide_new_4 };
	private SPStorageUtil mSp;
	
	private int startX;
	private int endX;
	
    int mScreenWidth;
    boolean isFirstGuide = true;
    boolean isBtnVisibility = true;
    
    private int mPointWidth;// 圆点间的距离
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_guide_layout);
		init();
		initView();
		setListener();
		
	}
	/**
	 * 初始化
	 */
	private void init() {
		DisplayMetrics metric = new DisplayMetrics();
		GuideActivity.this.getWindowManager().getDefaultDisplay().getMetrics(metric);
		mScreenWidth = metric.widthPixels;
		
		mSp = new SPStorageUtil(GuideActivity.this);
		
		mPagerImageList = new ArrayList<ImageView>();
		getIntentData();
	}
	
	private void getIntentData() {
		Intent intent = getIntent();
		if(intent != null) {
			isFirstGuide = intent.getBooleanExtra("isGuideFirst", false);
		}
	}
	
	/**
	 * 初始化控件
	 */
	private void initView() {
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		mJumpIv = (ImageView) findViewById(R.id.guide_jump_iv);
		mStartBtn = (Button) findViewById(R.id.start_btn);
		mDotLl = (LinearLayout) findViewById(R.id.dot_ll);
		mRedPoint = findViewById(R.id.view_red_point);
		
		//初始化导航图片
		for (int image : mIds) {
			ImageView imageView = new ImageView(GuideActivity.this);
			imageView.setImageResource(image);
			imageView.setScaleType(ScaleType.FIT_XY);
			mPagerImageList.add(imageView);
		}
		
		// 初始化引导页的小圆点
        for (int i = 0; i < mPagerImageList.size(); i++) {
            View point = new View(this);
            point.setBackgroundResource(R.drawable.shape_guide_dot_gray);// 设置引导页默认圆点  灰色

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    DensityUtils.dp2px(this, 10), DensityUtils.dp2px(this, 10));
            if (i > 0) {//第二个开始才设置左边距
                params.leftMargin = DensityUtils.dp2px(this, 10);// 设置圆点间隔
            }

            point.setLayoutParams(params);// 设置圆点的大小

            mDotLl.addView(point);// 将圆点添加给线性布局
        }
		
		mPagerAdapter = new GuideViewPagerAdapter(this, mPagerImageList);
	
		mViewPager.setAdapter(mPagerAdapter);
		
	}
	
	private void setListener() {
		mStartBtn.setOnClickListener(this);
		mViewPager.setOnPageChangeListener(this);
		mJumpIv.setOnClickListener(this);
		
		mDotLl.getViewTreeObserver().addOnGlobalLayoutListener(
                new OnGlobalLayoutListener() {

                    // 当layout执行结束后回调此方法
                    @SuppressWarnings("deprecation")
					@Override
                    public void onGlobalLayout() {
//                        System.out.println("layout 结束");
                    	mDotLl.getViewTreeObserver()
                                .removeGlobalOnLayoutListener(this);
                        mPointWidth = mDotLl.getChildAt(1).getLeft()
                                - mDotLl.getChildAt(0).getLeft();
//                        System.out.println("圆点距离:" + mPointWidth);
                    }
                }
        );
		
		
		mViewPager.setOnTouchListener(new OnTouchListener() {
			
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					 startX = (int) event.getX();
					 
					 LogTest.LogMsg(TAG, "onTouch-startX-->"+startX);
					break;
				case MotionEvent.ACTION_MOVE:
					
					break;
				case MotionEvent.ACTION_UP:
					endX = (int) event.getX();
					LogTest.LogMsg(TAG, "onTouch-endX-->"+endX);
					
					LogTest.LogMsg(TAG, "mScreenWidth/3-->"+(mScreenWidth/3));
					LogTest.LogMsg(TAG, "startX - endX-->"+(startX - endX));
					
					//最后一页并且向左滑的距离大于屏幕的三分之一 就跳到主界面
					if(mViewPager.getCurrentItem() == mViewPager.getAdapter()
							.getCount() - 1 && (startX - endX > mScreenWidth/3)) {
						startToActivity();
					}
					
					
					break;

				}
				
				//返回true表示拦截  viewpager就不动了
				return false;
			}
		});
	}

	
	/**
	 * 滑动状态改变的时候调用
	 */
	@Override
	public void onPageScrollStateChanged(int position) {
		
	}

	
	/**
	 * 当页面被滑动时调用
	 */
	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		
		int len = (int) (mPointWidth * positionOffset) + position
                * mPointWidth;
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mRedPoint
                .getLayoutParams();// 获取当前红点的布局参数
        params.leftMargin = len;// 设置左边距

        mRedPoint.setLayoutParams(params);// 重新给小红点设置布局参数
        
        if(position == 2 && positionOffsetPixels < mScreenWidth-20) {
        	if(isBtnVisibility) {
        		isBtnVisibility = false;
            	mStartBtn.setVisibility(View.GONE);
        	}
        	
        }
        if(position == 2 && positionOffsetPixels >= mScreenWidth-20) {
        	if(!isBtnVisibility) {
        		mStartBtn.setVisibility(View.VISIBLE);// 显示开始体验的按钮
                mStartBtn.startAnimation(AnimationUtils.loadAnimation(GuideActivity.this, R.anim.btn_guide_alpha));
        	}
        	
        	isBtnVisibility = true;
        	 
        }
	}

	/**
	 * 当前新的页面被选中的时候调用
	 */
	@Override
	public void onPageSelected(int position) {
		 if (/*position == mPagerImageList.size() - 1 && !isBtnVisibility*/position != mPagerImageList.size() - 1) {// 最后一个页面
//             mStartBtn.setVisibility(View.VISIBLE);// 显示开始体验的按钮
//             mStartBtn.startAnimation(AnimationUtils.loadAnimation(GuideActivity.this, R.anim.btn_guide_alpha));
//         } else {
        	 mStartBtn.setVisibility(View.GONE);
         }
//		//设置圆点状态 
//		for (int i = 0; i < mDotLl.getChildCount(); i++) {
//			if(position == i) {
//				mDotLl.getChildAt(i).setBackgroundResource(R.drawable.dot_focus);
//			} else {
//				mDotLl.getChildAt(i).setBackgroundResource(R.drawable.shape_guide_dot_gray);
//			}
//		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.start_btn:
			startToActivity();
			break;
		case R.id.guide_jump_iv://跳过
			startToActivity();
			break;
		}
	}
	
	/**
	 * 跳转到主界面
	 */
	private void startToActivity() {
		if(isFirstGuide) {
			mSp.saveIsFirstStart(false);
			startActivity(new Intent(GuideActivity.this, MainActivity.class));
			finish();
			AnimationUtil.startSmoothActivityAnimation(this);
		} else {
			finish();
			AnimationUtil.finishHaveFloatActivityAnimation(this);
		}
		
		
	}
}
