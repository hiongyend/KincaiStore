package com.kincai.store.ui.activity;

import android.content.Intent;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.kincai.store.R;
import com.kincai.store.model.ILockCompleteListener;
import com.kincai.store.ui.activity.base.BaseActivity;
import com.kincai.store.utils.AnimationUtil;
import com.kincai.store.view.custom.LocusPassWordView;

public class LockComeInMoneyActivity extends BaseActivity implements ILockCompleteListener{

	private LocusPassWordView mLockView;
	private TextView pwdTextView, mTitleTv;
	
	/**
	 * 1 设置手势 2修改手势 3关闭 4解锁进入应用
	 */
	private int mSettingType;
	private int mCount;
	private String mPassword;
	@Override
	public int initContentView() {
		return R.layout.activity_lock_comin_money_layout;
	}
	@Override
	public void initDatas() {
		init();
		initView();
		setListener();
	}
	
	private void init() {
		getIntentData();
	}
	
	public void getIntentData() {
		
		Intent intent = this.getIntent();
		if(intent != null) {
			mSettingType = intent.getIntExtra("settingType", 0);
		}
	}

	@Override
	public void initView() {
		super.initView();
		mTitleTv = (TextView) findViewById(R.id.title_tv);
		mLockView = (LocusPassWordView) findViewById(R.id.lock_pwd_view);
		pwdTextView = (TextView) findViewById(R.id.lock_pwd_tv);
		pwdTextView.setText(mSettingType == 1 ? "绘制解锁图案" : (mSettingType == 2 ? "请输入原手势密码" : (mSettingType == 3 ? "请输入原手势密码" : (mSettingType == 4 ? "绘制解锁图案" : ""))));
		mTitleTv.setText(mSettingType == 1 ? "设置手势密码" : (mSettingType == 2 ? "修改手势密码" : (mSettingType == 3 ? "关闭手势密码" : (mSettingType == 4 ? "手势解锁" : ""))));
	}
	
	@Override
	public void setListener() {
		super.setListener();
		mLockView.setOnCompleteListener(this);
		
	}
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.network_abnormal_top_layout:
			startActivity(new Intent(Settings.ACTION_SETTINGS));
			AnimationUtil.startHaveSinkActivityAnimation(this);
			break;
		case R.id.titlebar_back_iv:
			setResult(333);
			finish();
			break;
		}
	}
	
	@Override
	public void netWork() {
		
	}

	@Override
	public void noNetWork() {
		
	}
	
	@Override
	public void onLockComplete(String password) {
		
		if(TextUtils.isEmpty(password)) {
			mLockView.passwordError();
			pwdTextView.setText("操作有误");
			return;		
			
		}
		if(mSettingType == 1) {//设置密码
			mCount++;
			if(mCount == 2) {
				if(!mPassword.equals(password)) {
					//两次密码不一样
					mLockView.passwordError();
					mCount = 1;
					pwdTextView.setText("与上次绘制不一致，请重新绘制");
					return;
				} else {
					// TODO 相同就 储存密码 结束
					mSp.saveLockPwd(password);
					mSp.saveLockPwdState(true);
					setResult(333);
					finish();
					
					return;
				}
			}
			
			mPassword = password;
			pwdTextView.setText("再次绘制解锁图案");
			mLockView.clearPassword();
			
		} else if(mSettingType == 2) {//修改密码
			mCount++;
			if(mCount>1) {//新密码
			
				if(mCount == 3) {
					if(!mPassword.equals(password)) {
						//两次密码不一样
						mLockView.passwordError();
						mCount = 2;
						pwdTextView.setText("与上次绘制不一致，请重新绘制");
						return;
					} else {
						// TODO 相同就 储存密码 结束
						mSp.saveLockPwd(password);
						mSp.saveLockPwdState(true);
						setResult(333);
						finish();
						return;
					}
				}
				mLockView.clearPassword();
				mPassword = password;
				pwdTextView.setText("再次绘制解锁图案");
				
				return;
			}
			
			//TODO 判断存储的原来密码是否跟绘制的密码一样
			if(password.equals(mSp.getLockPwd())) {//相同
				mLockView.clearPassword();
				pwdTextView.setText("绘制解锁图案");
			} else {
				mLockView.passwordError();
				pwdTextView.setText("密码错了,请重新输入");
				mCount = 0;
			}
			
			
		} else if (mSettingType == 3) {//关闭
			
			mCount++;
			
			if(mCount == 1) {
				if(password.equals(mSp.getLockPwd())) {//密码正确
					//设置为关闭
					
					mSp.saveLockPwd("");
					mSp.saveLockPwdState(false);
					setResult(333);
					finish();
					
					return;
				} else {
					mLockView.passwordError();
					pwdTextView.setText("密码错了,请重新输入");
					
					mCount = 0;
					return;
				}
			}
		} else if(mSettingType == 4) {//解锁进入应用
			mCount++;
			
			if(mCount == 1) {
				if(password.equals(mSp.getLockPwd())) {//密码正确
					startActivity(new Intent(LockComeInMoneyActivity.this, BalanceActivity.class));
					finish();
					
					return;
				} else {
					mLockView.passwordError();
					pwdTextView.setText("密码错了,请重新输入");
					
					mCount = 0;
					return;
				}
			} 
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			setResult(333);
			this.finish();
			return true;

		}
		return super.onKeyDown(keyCode, event);
	}

}
