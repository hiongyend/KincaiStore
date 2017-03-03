package com.kincai.store.view.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.kincai.store.R;
import com.kincai.store.model.IBackTopVisiableListener;
import com.kincai.store.model.IReflashListener;
import com.kincai.store.utils.LogTest;

/**
 *
 * @company KCS互联网有限公司
 *
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 *
 * @author kincai
 *
 * @description 首页使用的listview刷新
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.view.custom
 *
 * @time 2015-9-11 上午11:57:32
 *
 */
public class HomeListview extends ListView implements AbsListView.OnScrollListener {
	private static final String TAG = "RefreshListView";
    private static final int STATE_PULL_REFRESH = 0;// 下拉刷新
    private static final int STATE_RELEASE_REFRESH = 1;// 松开刷新
    private static final int STATE_REFRESHING = 2;// 正在刷新

    
    private View mHeaderView;
    IReflashListener iListener;
    IBackTopVisiableListener iBackTopVisiableListener;
    private View mFooterView;
    private int mFooterViewHeight;
    private int startY = -1;// 滑动起点的y坐标
    private int mHeaderViewHeight;

    private int mCurrrentState = STATE_PULL_REFRESH;// 当前状态
    private TextView tvTitle;
    private ImageView ivArrow;
    private ProgressBar pbProgress;
    private RotateAnimation animUp;
    private RotateAnimation animDown;
    
    private boolean mVisible;
	int firstVisibleItem;// 当前第一个可见的item的位置；
	int visibleItemCount;//
	int totalItemCount;//
	int scrollState;// listview 当前滚动状态；
	private int mStart;
	private int mEnd;

    public HomeListview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initHeaderView();
        initFooterView();
    }

    public HomeListview(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeaderView();
        initFooterView();
    }

    public HomeListview(Context context) {
        super(context);
        initHeaderView();
        initFooterView();
    }

    /**
     * 初始化头布局
     */
    private void initHeaderView() {
        mHeaderView = View.inflate(getContext(), R.layout.pull_refresh_head_layout, null);
        this.addHeaderView(mHeaderView);

        tvTitle = (TextView) mHeaderView.findViewById(R.id.tip);
        ivArrow = (ImageView) mHeaderView.findViewById(R.id.arrow);
        pbProgress = (ProgressBar) mHeaderView.findViewById(R.id.progress);

        mHeaderView.measure(0, 0);
        mHeaderViewHeight = mHeaderView.getMeasuredHeight();
        System.out.println(mHeaderViewHeight+" 0000000000000000000000000000000000000000000000000000");

        topPadding(-mHeaderViewHeight);// 隐藏头布局

        initArrowAnim();

    }
    
    
   
	private void topPadding(int topPadding) {
		// setPadding(int left, int top, int right, int bottom)
		mHeaderView.setPadding(mHeaderView.getPaddingLeft(), topPadding,
				mHeaderView.getPaddingRight(),0);
		mHeaderView.invalidate();
	}
    /*
     * 初始化脚布局
     */
    private void initFooterView() {
        mFooterView = View.inflate(getContext(),
                R.layout.pull_load_footer_layout, null);
        this.addFooterView(mFooterView);

        mFooterView.measure(0, 0);
        mFooterViewHeight = mFooterView.getMeasuredHeight();

        mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);// 隐藏

        this.setOnScrollListener(this);
    }

    @Override
    public boolean onTouchEvent( MotionEvent ev) {
    	if(isLoadingMore) {
    		return super.onTouchEvent(ev);
    	}
    	
    	
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
            	
                if (startY == -1) {// 确保startY有效
                    startY = (int) ev.getRawY();
                }

                
                
                if (mCurrrentState == STATE_REFRESHING) {// 正在刷新时不做处理
                    break;
                }

                int endY = (int) ev.getRawY();
                int dy = endY - startY;// 移动便宜量

                
               
                
                if (dy > 0 && getFirstVisiblePosition() == 0) {// 只有下拉并且当前是第一个item,才允许下拉
                	
                	 if(Math.abs(dy) > 2) {
                     	if(iHomeTitleListener != null) {
                     		iHomeTitleListener.setVisibility(false);
                     	}
                     }
                    int padding = dy/3 - mHeaderViewHeight;// 计算padding
                    topPadding(padding);// 隐藏头布局// 设置当前padding

                    if (padding > 0 && mCurrrentState != STATE_RELEASE_REFRESH) {// 状态改为松开刷新
                        mCurrrentState = STATE_RELEASE_REFRESH;
                        refreshState();
                    } else if (padding < 0 && mCurrrentState != STATE_PULL_REFRESH) {// 改为下拉刷新状态
                        mCurrrentState = STATE_PULL_REFRESH;
                        refreshState();
                    }

                    return true;
                }

                break;
            case MotionEvent.ACTION_UP:
                startY = -1;// 重置
                if (mCurrrentState == STATE_RELEASE_REFRESH) {
                    mCurrrentState = STATE_REFRESHING;// 正在刷新
                    topPadding(0);// 显示
                    refreshState();
                } else if (mCurrrentState == STATE_PULL_REFRESH) {
                	 topPadding(-mHeaderViewHeight);// 隐藏头布局
                	 if(iHomeTitleListener != null) {
                  		iHomeTitleListener.setVisibility(true);
                  	}
                }

                break;

            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 刷新下拉控件的布局
     */
    private void refreshState() {
        switch (mCurrrentState) {
            case STATE_PULL_REFRESH:
                tvTitle.setText("下拉刷新");
                ivArrow.setVisibility(View.VISIBLE);
                pbProgress.setVisibility(View.GONE);
                ivArrow.startAnimation(animDown);
                break;
            case STATE_RELEASE_REFRESH:
                tvTitle.setText("松开刷新");
                ivArrow.setVisibility(View.VISIBLE);
                pbProgress.setVisibility(View.GONE);
                ivArrow.startAnimation(animUp);
                break;
            case STATE_REFRESHING:
                tvTitle.setText("正在刷新...");
                ivArrow.clearAnimation();// 必须先清除动画,才能隐藏
                ivArrow.setVisibility(View.INVISIBLE);
                pbProgress.setVisibility(View.VISIBLE);

                if (iListener != null) iListener.onReflash();
                break;

            default:
                break;
        }
    }

    /**
     * 初始化箭头动画
     */
    private void initArrowAnim() {
        // 箭头向上动画
        animUp = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        animUp.setDuration(200);
        animUp.setFillAfter(true);

        // 箭头向下动画
        animDown = new RotateAnimation(-180, -360, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animDown.setDuration(200);
        animDown.setFillAfter(true);

    }

    public void setOnRefreshListener(IReflashListener listener) {
        iListener = listener;
    }
    public void setOnBackToTopVisiableListener(IBackTopVisiableListener backTopVisiableListener) {
    	iBackTopVisiableListener = backTopVisiableListener;
    }

    /**
     * 收起下拉刷新的控件
     * @param success 刷新成功或者失败
     */
    public void onRefreshComplete() {
        if (isLoadingMore) {// 正在加载更多...
            mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);// 隐藏脚布局
            isLoadingMore = false;
        } else {
            mCurrrentState = STATE_PULL_REFRESH;
            tvTitle.setText("下拉刷新");
            ivArrow.setVisibility(View.VISIBLE);
            pbProgress.setVisibility(View.GONE);
            if(iHomeTitleListener != null) {
         		iHomeTitleListener.setVisibility(true);
         	}

            System.out.println(mHeaderViewHeight+"88888888888888888888888888888888888888");
            topPadding(-mHeaderViewHeight);// 隐藏头布局

            
        }
    }
   
    

    private boolean isLoadingMore;

    /**
     * 滑动状态改变时调用
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    	
    	this.scrollState = scrollState;
		LogTest.LogMsg(TAG, "onScrollStateChanged --");


		if ((scrollState == SCROLL_STATE_IDLE || scrollState == SCROLL_STATE_FLING)
				&& getLastVisiblePosition() == getCount() - 1) {//滑动到底部
			if (!isLoadingMore && mCurrrentState != STATE_REFRESHING) {//没有在加载更多 并且没有在刷新
				isLoadingMore = !isLoadingMore;
				mFooterView.setPadding(0, 0, 0, 0);
				if(iListener != null)iListener.onLoadMore();
				setSelection(getCount() - 1);
			}

		}
		
    	
    }

    /**
     * 滑动时调用
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
    		int visibleItemCount, int totalItemCount) {

    	this.firstVisibleItem = firstVisibleItem;
		this.visibleItemCount = visibleItemCount;
		this.totalItemCount = totalItemCount;
		mStart = firstVisibleItem;
		mEnd = firstVisibleItem + visibleItemCount - 2;

		LogTest.LogMsg(TAG, "mStart " + mStart);
		LogTest.LogMsg(TAG, "mEnd " + mEnd);
		LogTest.LogMsg(TAG, "visibleItemCount " + visibleItemCount);
		LogTest.LogMsg(TAG, "lastVisiablePosition "+getLastVisiblePosition());
		LogTest.LogMsg(TAG, "firstVisibleItem " + firstVisibleItem);
		LogTest.LogMsg(TAG, "totalItemCount " + totalItemCount);
		LogTest.LogMsg(TAG, "getCount " + getCount());

		
		//当 当前页面显示的最后可见一条大于当前可见的条数并且还没有显示过 那么显示回到顶部按钮
		if(getLastVisiblePosition() > visibleItemCount && !mVisible) {
			if(iBackTopVisiableListener != null) {
				iBackTopVisiableListener.onVisible();
				mVisible = !mVisible;
			}
			//当 当前页面显示的最后可见一条小于等于当前可见的条数 并且显示过 那么显示回到顶部按钮
		} else if(getLastVisiblePosition() <= visibleItemCount && mVisible) {
			if(iBackTopVisiableListener != null) {
				iBackTopVisiableListener.onInVisible();
				mVisible = !mVisible;
			}
		}
		
		if(getFirstVisiblePosition() == 1) {
			View childAt = this.getChildAt(0);
			if(childAt != null) {
				int height = Math.abs(childAt.getTop());
				System.out.println("==height="+height);
				if(mTitleHeight > height) {
					if(iHomeTitleListener != null){
						iHomeTitleListener.setAlpha(height);
					}
				}
			}
			
		}
		
    }
    
    public int mTitleHeight = 0;
    IHomeTitleListener iHomeTitleListener;
    
    public void setOnHomeTitleListener(IHomeTitleListener listener) {
    	iHomeTitleListener = listener;
    }
    public interface IHomeTitleListener {
    	void setAlpha(int value);
    	void setVisibility(boolean isVisibility);
    }

    public void setTiTileHeight(int height) {
    	mTitleHeight = height;
    }
}
