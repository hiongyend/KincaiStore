package com.kincai.store.view.custom;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.kincai.store.R;
import com.kincai.store.bean.ProImagePathInfo;
import com.kincai.store.ui.activity.base.BaseActivity;
import com.kincai.store.utils.LogTest;
import com.kincai.store.utils.Utils;
import com.kincai.store.utils.bitmap_cache.MyImageLoader;
import com.lidroid.xutils.BitmapUtils;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 * 
 * @author kincai
 * 
 * @description ViewPager实现商品详情页图片轮播(没有自动播放和循环播放)
 * 
 * @project Kincai_Store
 * 
 * @package com.kincai.store.view.custom
 * 
 * @time 2015-7-14 下午1:39:22
 * 
 */
@SuppressLint("HandlerLeak")
public class ProShowView extends FrameLayout {

	// 自定义轮播图的资源
	private String[] imageUrls;
	// 放轮播图片的ImageView 的list
	private List<ImageView> imageViewsList;
	// 放圆点的View的list
	private List<View> dotViewsList;

	private ViewPager viewPager;
	private static final String TAG = "ProShowView";
	private Context context;

	private Handler mHandler;
	private int count = 0;
	BitmapUtils mBitmapUtils;
	MyImageLoader mImageLoader;
	int imageLenght;

	public ProShowView(Context context) {
		this(context, null);
	}

	public ProShowView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ProShowView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		
		init();

	}
	
	private void init() {
		mImageLoader = ((BaseActivity)context).mImageLoader;
		
	}

	public void init(List<ProImagePathInfo> list, Handler handler) {

		destoryObject();
		LogTest.LogMsg("kong", "data==null" + (null == imageUrls));
		if (null != list) {
			int listSize = list.size();
			imageUrls = new String[listSize];
			for (int i = 0; i < listSize; i++) {
				imageUrls[i] = Utils.getServerProDetailImagePath(context, ((BaseActivity)context).mSp)
						+ list.get(i).getAlbumPath();
			}
		}

		this.mHandler = handler;
		initData();
	}

	/**
	 * 初始化相关Data
	 */
	private void initData() {
		imageViewsList = new ArrayList<ImageView>();
		dotViewsList = new ArrayList<View>();
		
		initUI(context);
	}
	
	private void destoryObject() {
		imageUrls = null;
		imageViewsList = null;
		count = 0;
		
	}

	/**
	 * 初始化Views等UI
	 */
	private void initUI(Context context) {
		if (imageUrls == null || imageUrls.length == 0)
			return;
		
		imageLenght = imageUrls.length;

		LayoutInflater.from(context).inflate(R.layout.layout_slideshow, this,
				true);

		LinearLayout dotLayout = (LinearLayout) findViewById(R.id.dotLayout);
		dotLayout.removeAllViews();

		// 热点个数与图片特殊相等
		for (int i = 0; i < imageLenght; i++) {
			ImageView view = new ImageView(context);
			view.setTag(imageUrls[i]);
//			if (null == imageUrls)// 给一个默认图
//				view.setBackgroundResource(R.drawable.ic_launcher);
			view.setScaleType(ScaleType.FIT_XY);
			imageViewsList.add(view);

			ImageView dotView = new ImageView(context);
			if(i == 0) dotView.setBackgroundResource(R.drawable.dot_focus);
			else dotView.setBackgroundResource(R.drawable.dot_blur);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.leftMargin = 4;
			params.rightMargin = 4;
			dotLayout.addView(dotView, params);
			dotViewsList.add(dotView);
		}

		viewPager = (ViewPager) findViewById(R.id.viewPager);
		viewPager.setFocusable(true);

		viewPager.setAdapter(new MyPagerAdapter());
		viewPager.setOnPageChangeListener(new MyPageChangeListener());
	}

	/**
	 * 填充ViewPager的页面适配器
	 * 
	 */
	private class MyPagerAdapter extends PagerAdapter {

		

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			 container.removeView(imageViewsList.get(position));

		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			final int post = position;
			
			LogTest.LogMsg("count", "count ni " + count);
			final ImageView imageView = imageViewsList.get(position);

			if (count < imageLenght) {
				
			mImageLoader.loadImage(imageView.getTag()+"", imageView);
				
				
//				mBitmapUtils.display(imageView, imageView.getTag() + "");
				count++;
				
				imageView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Message msg = new Message();
						msg.what = 0x210;
						msg.obj = post;
						mHandler.sendMessage(msg);
					}
				});
				
				LogTest.LogMsg(TAG, "displayBitmap 记载图片");
			} 


			container.addView(imageViewsList.get(position));
			return imageViewsList.get(position);
		}

		@Override
		public int getCount() {
			return imageViewsList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}


	}

	/**
	 * 销毁ImageView资源，回收内存
	 * 
	 */
	public void destoryBitmaps() {

		if (null != imageViewsList && imageViewsList.size() > 0
				&& null != dotViewsList) {
			for (int i = 0; i < imageViewsList.size(); i++) {
				ImageView imageView = imageViewsList.get(i);
				Drawable drawable = imageView.getDrawable();
				if (drawable != null) {
					// 解除drawable对view的引用
					drawable.setCallback(null);
				}

			}

		}

	}
	
	
	
	/**
	 * ViewPager的监听器 当ViewPager中页面的状态发生改变时调用
	 * 
	 */
	private class MyPageChangeListener implements OnPageChangeListener {


		/**
		 * 滑动状态改变时
		 */
		@Override
		public void onPageScrollStateChanged(int position) {
		}

		/**
		 * 当页面被滑动时
		 */
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		/**
		 * 当前新的页面被选中的时候调用
		 */
		@Override
		public void onPageSelected(int pos) {
			// TODO Auto-generated method stub

			for (int i = 0; i < dotViewsList.size(); i++) {
				if (i == pos) {
					dotViewsList.get(pos).setBackgroundResource(R.drawable.dot_focus);
				} else {
					dotViewsList.get(i).setBackgroundResource(R.drawable.dot_blur);
				}
			}
		}

	}
	
/*
	*//**
	 * 异步任务,获取数据
	 * 
	 *//*
	class GetListTask extends AsyncTask<String, Integer, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			try {
				// 这里一般调用服务端接口获取一组轮播图片，下面是从百度找的几个图片
				if (null != data) {
					imageUrls = data;
				} else {
					LogTest.LogMsg("rtrtrt", "fff");
					imageUrls = new String[] {
							"http://imagesdsd.zcool.com.cn/56/35/1303967876491.jpg",
							"http://imagesdsd.zcool.com.cn/59/54/m_1303967870670.jpg",
							"http://imagesdsd.zcool.com.cn/47/19/1280115949992.jpg",
							"http://imagesdsd.zcool.com.cn/56/35/1303967876491.jpg",
							"http://imagesdsd.zcool.com.cn/56/35/1303967876491.jpg",
							"http://imagesdsf.zcool.com.cn/59/11/m_1303967844788.jpg",
							"http://image.zcool.com.cn/59/11/m_1303967844788.jpg", };
				}

				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (result) {
				LogTest.LogMsg("rtrtrt", "rrr");
				initUI(context);
			}
		}
	}

	*//**
	 * ImageLoader 图片组件初始化
	 * 
	 * @param context
	 *//*
	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove
									// for
									// release
									// app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}*/

	/**
	 * 得到商品图片中的第一个
	 * 
	 * @return
	 */
	public Drawable getDrawableOne() {
		if (null != imageViewsList && imageViewsList.size() > 0) {
			return imageViewsList.get(0).getDrawable();
		}
		return null;
	}
}