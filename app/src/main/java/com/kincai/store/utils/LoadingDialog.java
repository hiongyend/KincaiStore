package com.kincai.store.utils;

import com.kincai.store.R;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 * 
 * @author kincai
 * 
 * @description 加载等待对话框
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.utils
 *
 * @time 2015-7-15 下午9:36:32
 *
 */
public class LoadingDialog {
	
	private Context mContext;
	private View mDialogView;
	private Dialog dialog;
	private TextView mTextView;
	private boolean player;
	public LoadingDialog(Context context, boolean player) {
		this.mContext = context;
		this.player = player;
		mDialogView = View.inflate(mContext, R.layout.loading_dialog_layout, null);
		mTextView = (TextView) mDialogView.findViewById(R.id.loding_dialog_msg_tv);
		initDialog();
	}
	
	private void initDialog() {
		if(dialog != null) {
			dialog.cancel();
		}
		dialog = new Dialog(mContext,R.style.loddingDilog);
		dialog.setContentView(mDialogView);
		dialog.setCanceledOnTouchOutside(false);
		if(player) {
			mTextView.setVisibility(View.VISIBLE);
		} else {
			mTextView.setVisibility(View.GONE);
		}
	}
	
	public void dialogShow() {
		if(dialog != null) {
			dialog.show();
		}
		
	}
	
	public boolean isShow() {
		if(dialog != null) {
			return dialog.isShowing();
		}
		return false;
	}
	
	public void setMessage(CharSequence msg) {
		mTextView.setText(msg);
	}
	
	public void setMessage(int msg) {
		mTextView.setText(msg);
	}
	
	public void dismiss() {
		if(dialog != null) {
			dialog.dismiss();
		}
	}
}
