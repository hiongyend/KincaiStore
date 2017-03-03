package com.kincai.store.view.adapter;

import com.kincai.store.utils.bitmap_cache.MyImageLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * ViewHolder viewHolder;
		if(convertView == null) {
			viewHolder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.list_item, parent,false);
			viewHolder.title = (TextView) convertView.findViewById(R.id.tv_title);
			viewHolder.message = (TextView) convertView.findViewById(R.id.tv_message);
			viewHolder.date = (TextView) convertView.findViewById(R.id.tv_date);
			viewHolder.phoneNumber = (TextView) convertView.findViewById(R.id.tv_icon);
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		DataBean bean = mDataList.get(position);
		viewHolder.title.setText(bean.getTitle());
		viewHolder.message.setText(bean.getMessage());
		viewHolder.date.setText(bean.getDate());
		viewHolder.phoneNumber.setText(bean.getPhoneNumber());
		return convertView;
	}
	
	private class ViewHolder {
		TextView title;
		TextView message;
		TextView date;
		TextView phoneNumber;
	}
	
	根据上面的来封装这个ViewHolder 实现通用
 * @author kincai
 *
 */
public class ViewHolder {
	/**
	 * SparseArrays map integers to Objects. Unlike a normal array of Objects,
	 * there can be gaps in the indices. It is intended to be more efficient
	 * than using a HashMap to map Integers to Objects.
	 * SparseArrays整数映射到对象。不像一个正常的对象数组,
	 * 指数可能有差距。它的目的是成为更有效率　　*比使用HashMap将整数映射到对象
	 * 
	 * 是一个比HashMap有效率的            <integer,object> 的情况可使用
	 */
	private SparseArray<View> mView;
	int mPosition;
	private View mConvertView;

	/**
	 * convertview为空 时调用
	 * @param context
	 * @param position
	 * @param layoutId
	 * @param parent
	 */
	private ViewHolder(Context context, int position, int layoutId,
			ViewGroup parent) {
		this.mPosition = position;
		mView = new SparseArray<View>();
		mConvertView = LayoutInflater.from(context).inflate(layoutId, parent,false);
		mConvertView.setTag(this);
	}

	/**
	 * holder对象
	 * @param context
	 * @param position
	 * @param layoutId
	 * @param convertView
	 * @param parent
	 * @return
	 */
	public static ViewHolder get(Context context, int position, int layoutId,
			View convertView, ViewGroup parent) {
		if(convertView == null) {
			//等于空就加载布局给convertview
			return new ViewHolder(context, position, layoutId, parent);
		} else {
			//不为空就直接取
			ViewHolder holder = (ViewHolder) convertView.getTag();
			holder.mPosition = position;
			return holder;
		}

	}
	
	/**
	 * 获取view
	 * @param ViewId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends View> T getView(int ViewId) {
		View view = mView.get(ViewId);
		if(view == null) {
			view = mConvertView.findViewById(ViewId);
			mView.put(ViewId, view);
		}
		return (T) view;
	}
//	public View getView(int ViewId) {
//		View view = mView.get(ViewId);
//		if(view == null) {
//			view = mConvertView.findViewById(ViewId);
//			mView.put(ViewId, view);
//		}
//		return view;
//	}
	
	/**
	 * 提供设置textview的值
	 * @param viewId
	 * @param str
	 * @return
	 */
	public ViewHolder setTextViewText(int viewId,String str) {
		TextView textView = getView(viewId);
		textView.setText(str);
		return this;
		
	}
	
	/**
	 * 提供设置imageview
	 * @param viewId
	 * @param resId
	 * @return
	 */
	public ViewHolder setImageResourse(int viewId, int resId) {
		ImageView imageView = getView(viewId);
		imageView.setImageResource(resId);
		return this;
		
	}
	
	/**
	 * 提供设置imageview
	 * @param viewId
	 * @param resId
	 * @return
	 */
	public ViewHolder setImageBitmap(int viewId, Bitmap bm) {
		ImageView imageView = getView(viewId);
		imageView.setImageBitmap(bm);
		return this;
		
	}
	
	public ViewHolder setImageFromNet(int viewId, String url, MyImageLoader imageLoader) {
		ImageView imageView = getView(viewId);
		imageLoader.loadImage(url, imageView);
		return this;
		
	}
	
	/**
	 * 设置view的可见性
	 * @param visibility
	 * @param views
	 */
	public void setViewVisibility(boolean visibility, int...views){
		for(int view : views) {
			getView(view).setVisibility(visibility ? View.VISIBLE : View.GONE);
		}
	}
	
	/**
	 * 得到ConvertView 进行返回
	 * @return
	 */
	public View getConvertView() {
		
		return mConvertView;
	}

	/**
	 * 
	 * @return
	 */
	public int getPosition() {
		return mPosition;
	}
}
