package com.kincai.store.view.adapter;

import java.util.List;

import com.kincai.store.R;
import com.kincai.store.bean.RobotChatMessageInfo;
import com.kincai.store.bean.RobotChatMessageInfo.Type;
import com.kincai.store.view.custom.CircleImageView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 *
 * @author kincai
 * 
 * @description 天天机器人聊天适配器
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.view.adapter
 *
 * @time 2015-9-29 上午10:43:46
 *
 */
public class ChatMessageAdapter extends BaseAdapter {
	private List<RobotChatMessageInfo> mDatasLists;
	private Context mContext;
	IChatIvLinstener ivLinstener;
	public ChatMessageAdapter(Context context, List<RobotChatMessageInfo> mDatasList) {
		this.mDatasLists = mDatasList;
		this.mContext = context;
	}

	public void setChangeData(List<RobotChatMessageInfo> mDatasList) {
		this.mDatasLists = mDatasList;
		this.notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return mDatasLists.size();
	}

	@Override
	public RobotChatMessageInfo getItem(int position) {
		return mDatasLists.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * 处理listview多布局
	 */
	@Override
	public int getItemViewType(int position) {
		RobotChatMessageInfo chatMessage = mDatasLists.get(position);
		if (chatMessage.getType() == Type.INCOMING) {
			return 0;
		}
		return 1;
	}

	/**
	 * 处理listview多布局返回类型数目
	 */
	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		RobotChatMessageInfo chatMessage = getItem(position);
		ViewHolder viewHolder = null;
		if (convertView == null) {
			// 通过ItemType设置不同的布局
			if (getItemViewType(position) == 0) {
				convertView = View.inflate(mContext, R.layout.item_robot_chat_from_msg,
						null);
				viewHolder = new ViewHolder();
				viewHolder.mMsg = (TextView) convertView
						.findViewById(R.id.id_from_msg_info);
			} else {
				convertView = View.inflate(mContext, R.layout.item_robot_chat_to_msg,
						null);
				viewHolder = new ViewHolder();
				viewHolder.mMsg = (TextView) convertView
						.findViewById(R.id.id_to_msg_info);
				viewHolder.iv = (CircleImageView) convertView.findViewById(R.id.chat_to_iv);
				if(ivLinstener != null) {
					ivLinstener.setChatToMsgBtm(viewHolder.iv);
				}
			}
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		// 设置数据
		viewHolder.mMsg.setText(chatMessage.getMsg());
		return convertView;
	}

	private final class ViewHolder {
		TextView mMsg;
		CircleImageView iv;
	}
	
	public void setChatToMsgBtmListener(IChatIvLinstener ivLinstener) {
		this.ivLinstener = ivLinstener;
	}
	
	public interface IChatIvLinstener {
		public void setChatToMsgBtm(CircleImageView iv);
	}

}
