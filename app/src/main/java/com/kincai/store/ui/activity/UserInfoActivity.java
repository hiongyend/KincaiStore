package com.kincai.store.ui.activity;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.kincai.store.Constants;
import com.kincai.store.R;
import com.kincai.store.bean.UserInfo;
import com.kincai.store.common.LoadUserFace;
import com.kincai.store.ui.activity.base.BaseSlideActivity;
import com.kincai.store.utils.AnimationUtil;
import com.kincai.store.utils.LogTest;
import com.kincai.store.utils.Utils;
import com.kincai.store.utils.bitmap_cache.BitmapLocaFileCacheUtil;
import com.kincai.store.view.custom.CircleImageView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 *
 * @author kincai
 * 
 * @description 用户信息类
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.ui
 *
 * @time 2015-7-24 下午7:29:37
 *
 */
public class UserInfoActivity extends BaseSlideActivity {
	private static final String TAG = "UserInfoActivity";
	private CircleImageView mUserChangeFaceIv;
	private RelativeLayout mUserNickRl;
	private RelativeLayout mUserSexRl;
	private RelativeLayout mUserConsigneeAddressRl, mUserChangeLockPwdRl;
	
	private CheckedTextView mUserChangePwdCtv, mUserLockPwdCtv;
	private RelativeLayout mUserLoginPwdRl;
	private RelativeLayout mUserPayPwdRl;
	private LinearLayout mPwdLayout;
	private TextView mUserNickTv;
	private TextView mUserSexTv;
	private ViewStub mViewStub;

	private ChangeUserInfoReceiver mInfoReceiver;

	private String mUserId= "";

	private MyHandler mMyHandler;

	private String mFacePath;

	public static UserInfoActivity instance = null;
	private BitmapLocaFileCacheUtil locaFileCacheUtil;

	private String photoPath;
	/** 图片保存路径*/
	private final String TEMP_PHOTO_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ File.separator
			+ "kincaiStoreImage";
	private static final int PHOTO_CARMERA = 1;
	private static final int PHOTO_PICK = 2;
	private static final int PHOTO_CUT = 3;
	// 创建一个以当前系统时间为名称的文件，防止重复
	private File tempFile;
	private HttpUtils httpUtils;
	private Bitmap bmp;

	
	
	@Override
	public int initContentView() {
		return R.layout.activity_user_info_layout;
	}
	@Override
	public void initDatas() {
		instance = this;
		init();
		initView();
		initData();
		setListener();
		registerUserInfoReceiver();
	}
	

	/**
	 * 初始化
	 */
	private void init() {

		httpUtils = new HttpUtils(10000);
		mMyHandler = new MyHandler();

	}

	/**
	 * 初始化控件
	 */
	public void initView() {
		super.initView();
		mUserChangeFaceIv = (CircleImageView) findViewById(R.id.user_change_face_iv);
		mUserNickRl = (RelativeLayout) findViewById(R.id.user_nick_relativeLayout);
		mUserSexRl = (RelativeLayout) findViewById(R.id.user_sex_relativeLayout);
		mUserConsigneeAddressRl = (RelativeLayout) findViewById(R.id.user_consignee_address_relativeLayout);
		mUserChangePwdCtv = (CheckedTextView) findViewById(R.id.user_change_password_ctv);
		
		mUserChangeLockPwdRl = (RelativeLayout) findViewById(R.id.user_change_lock_pwd_relativeLayout);
		mUserLockPwdCtv = (CheckedTextView) findViewById(R.id.lock_pwd_ctv);
		
		mUserNickTv = (TextView) findViewById(R.id.user_nick_tv);
		mUserSexTv = (TextView) findViewById(R.id.user_sex_tv);

	}

	/**
	 * 设置监听
	 */
	public void setListener() {
		super.setListener();
		mUserChangeFaceIv.setOnClickListener(this);
		mUserNickRl.setOnClickListener(this);
		mUserSexRl.setOnClickListener(this);
		mUserConsigneeAddressRl.setOnClickListener(this);
		mUserChangePwdCtv.setOnClickListener(this);
		mUserChangeLockPwdRl.setOnClickListener(this);
		mUserLockPwdCtv.setOnClickListener(this);
		
		registerForContextMenu(mUserChangeFaceIv);

	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		List<UserInfo> mUserInfos = mBaseUserDao.getUserInfoOne(mSp.getUsername());
		LogTest.LogMsg(TAG, "用户查询是否不为空" + (mUserInfos != null));

		if (null != mUserInfos) {
			mUserId = String.valueOf(mUserInfos.get(0).getUserId());
			mFacePath = mUserInfos.get(0).getFace();
			mUserNickTv.setText(mUserInfos.get(0).getNickname());
			mUserSexTv.setText(mUserInfos.get(0).getSex());

			LoadUserFace.userFace(this, mMyHandler, mFacePath, mUserChangeFaceIv);
			initLockPwdState();
		} else {
			mSp.saveUserIsLogin(false);

		}

	}
	
	private void initLockPwdState() {
		mUserLockPwdCtv.setChecked(mSp.getLockPwdState());
		mUserChangeLockPwdRl.setVisibility(mSp.getLockPwdState() ? View.VISIBLE: View.GONE);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.titlebar_back_iv:
			finish();
			AnimationUtil.finishHaveFloatActivityAnimation(this);
			break;
		case R.id.network_abnormal_top_layout:
			startActivity(new Intent(Settings.ACTION_SETTINGS));
			AnimationUtil.startHaveSinkActivityAnimation(this);
			break;
		case R.id.user_change_face_iv:
			
			photoPath = Utils.getPhotoFileName();
			tempFile = new File(TEMP_PHOTO_PATH,photoPath);
			
			mUserChangeFaceIv.showContextMenu();//默认不写这个的话 只是注册了上下文菜单的话 需要长按才能显示
			break;
		case R.id.user_nick_relativeLayout:
			startActivity(new Intent(UserInfoActivity.this,
					ChangeNicknameActivity.class).putExtra("nickname", mUserNickTv.getText().toString()));
			AnimationUtil.startHaveSinkActivityAnimation(this);
			break;
		case R.id.user_sex_relativeLayout:
			startActivity(new Intent(UserInfoActivity.this,
					ChangeSexActivity.class));
			AnimationUtil.startHaveSinkActivityAnimation(this);
			break;
		case R.id.user_consignee_address_relativeLayout:
			startActivity(new Intent(UserInfoActivity.this,
					ConsigneeAddressActivity.class));
			AnimationUtil.startHaveSinkActivityAnimation(this);
			break;
		case R.id.user_change_password_ctv:
			mViewStub = (ViewStub) findViewById(R.id.ViewStub_paw);
			LogTest.LogMsg(TAG, "mViewStub为空？ "+(mViewStub==null));
			if (mViewStub != null) {
				View inflatedView = mViewStub.inflate();
				mUserLoginPwdRl = (RelativeLayout) inflatedView.findViewById(R.id.user_login_password_relativeLayout);
				mUserPayPwdRl = (RelativeLayout) inflatedView.findViewById(R.id.user_pay_password_relativeLayout);
				mPwdLayout = (LinearLayout) inflatedView.findViewById(R.id.pwd_linearlayout);
				
				mUserLoginPwdRl.setOnClickListener(this);
				mUserPayPwdRl.setOnClickListener(this);
			}

			mUserChangePwdCtv.toggle();
			if (mUserChangePwdCtv.isChecked()) {

				AnimationUtil.setShowAnimationAa(mPwdLayout, 500);
				mPwdLayout.setVisibility(View.VISIBLE);

			} else {
				AnimationUtil.setHideAnimationAa(mPwdLayout, 500);
				mPwdLayout.setVisibility(View.GONE);
			}

			break;
			
		case R.id.lock_pwd_ctv:
			mUserLockPwdCtv.toggle();
			
			if(mUserLockPwdCtv.isChecked()) {//选择就去设置手势 
				startActivityForResult(new Intent(UserInfoActivity.this, LockComeInMoneyActivity.class).putExtra("settingType", 1), 222);
				
			} else {
				startActivityForResult(new Intent(UserInfoActivity.this, LockComeInMoneyActivity.class).putExtra("settingType", 3), 222);
			}
			
			break;
		case R.id.user_change_lock_pwd_relativeLayout:
			
			startActivityForResult(new Intent(UserInfoActivity.this, LockComeInMoneyActivity.class).putExtra("settingType", 2), 222);
			
			break;
		case R.id.user_login_password_relativeLayout:
			startActivity(new Intent(UserInfoActivity.this,
					ChangeLoginPwdActivity.class));
			AnimationUtil.startHaveSinkActivityAnimation(this);
			break;
		case R.id.user_pay_password_relativeLayout:

			break;

		}
	}

	/**
	 * 创建上传头像上下文菜单
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
//		menu.setHeaderTitle("修改头像");
//		menu.add(0, 0, 0, "弹出长按菜单0");  
//		menu.add(0, 1, 0, "弹出长按菜单1");     

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.upload_userface_context_menu, menu);
	}
	
	/**
	 * 上传头像上下文菜单的选择
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.upload_userface_menu_photograph://拍照
//			Utils.showToast(this, "1", Toast.LENGTH_SHORT);
			startCamera();
			break;
		case R.id.upload_userface_menu_photo_album://相册
//			Utils.showToast(this, "2", Toast.LENGTH_SHORT);
			startPick();
			break;

		}
		return super.onContextItemSelected(item);
	}
	

	/**
	 * 调用系统相机
	 */
	private void startCamera() {
		// 调用系统的拍照功能
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra("camerasensortype", 2); // 调用前置摄像头
		intent.putExtra("autofocus", true); // 自动对焦
		intent.putExtra("fullScreen", false); // 全屏
		intent.putExtra("showActionIcons", false);
		// 指定调用相机拍照后照片的存储路径
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
		startActivityForResult(intent, PHOTO_CARMERA);
	}
	
	/**
	 * 调用系统相册
	 */
	private void startPick() {
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				"image/*");
		startActivityForResult(intent, PHOTO_PICK);
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case PHOTO_CARMERA:
			startPhotoZoom(Uri.fromFile(tempFile), 300);
			break;
		case PHOTO_PICK:
			if (null != data)startPhotoZoom(data.getData(), 300);
			break;
		case PHOTO_CUT:
			if (null != data)setPicToView(data);
			break;
		case 222:
			
			if(resultCode == 333) {
				initLockPwdState();
			}
			
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 调用系统裁剪
	 * @param uri
	 * @param size
	 */
	private void startPhotoZoom(Uri uri, int size) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// crop为true是设置在开启的intent中设置显示的view可以裁剪
		intent.putExtra("crop", true);
		// aspectX,aspectY是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX,outputY是裁剪图片的宽高
		intent.putExtra("outputX", size);
		intent.putExtra("outputY", size);
		// 设置是否返回数据
		intent.putExtra("return-data", true);
		startActivityForResult(intent, PHOTO_CUT);
	}

	/**
	 * 裁剪完成后 保存图片并上传图片到服务器
	 * @param data
	 */
	private void setPicToView(Intent data) {
		Bundle bundle = data.getExtras();
		if (null != bundle) {
			bmp = bundle.getParcelable("data");

//			mUserChangeFaceIv.setImageBitmap(bmp);
			if(locaFileCacheUtil == null) {
				locaFileCacheUtil = new BitmapLocaFileCacheUtil(this);
			}
			try {
				locaFileCacheUtil.savaBitmap(photoPath, bmp);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			upLoadBitmap();
			LogTest.LogMsg("MainActivity", tempFile.getAbsolutePath());
		}
	}
	
	
	/**
	 * 上传头像
	 */
	private void upLoadBitmap() {
		RequestParams params=new RequestParams();
		params.addBodyParameter("userId", mUserId );
		params.addBodyParameter("userface", tempFile);
		httpUtils.send(HttpMethod.POST,Constants.API_URL+"appUploadFileApi.php", params,new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException e, String msg) {
				Utils.showToast(UserInfoActivity.this, "上传失败，检查一下服务器地址是否正确", Toast.LENGTH_SHORT);
				LogTest.LogMsg(TAG, e.getExceptionCode() + "====="
						+ msg);
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				LogTest.LogMsg(TAG, "====upload_success====="
						+ responseInfo.result);
				if(responseInfo.result.equals("200")) {//表示服务器端处理成功
					mBaseUserDao.upDateOne(mUserId, photoPath);
					Intent intent = new Intent();
					intent.setAction(Constants.USERINFO_CHANGE_ACTION);
					sendBroadcast(intent);
					Utils.showToast(UserInfoActivity.this, "上传成功", Toast.LENGTH_SHORT);
//					mUserChangeFaceIv.setImageBitmap(bmp);
					LogTest.LogMsg(TAG, "====upload_success====="
							+ responseInfo.result);
				}
				
			}
		});
	}
	
	@SuppressLint("HandlerLeak")
	class MyHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
			case 202:
				LoadUserFace.getUserFaceResponse(UserInfoActivity.this, mUserChangeFaceIv, mFacePath, msg);

				break;

			default:
				break;
			}
		}
	}

	/**
	 * 注册用户信息广播
	 */
	private void registerUserInfoReceiver() {
		IntentFilter filter = new IntentFilter(Constants.USERINFO_CHANGE_ACTION);
		mInfoReceiver = new ChangeUserInfoReceiver();
		registerReceiver(mInfoReceiver, filter);
	}


	private void unRegisterUserInfoReceiver() {
		unregisterReceiver(mInfoReceiver);
	}

	@Override
	public void netWork() {
		mNetWorkLayout.setVisibility(View.GONE);
	}
	
	@Override
	public void noNetWork() {
		mNetWorkLayout.setVisibility(View.VISIBLE);
	}

	/**
	 * @author kincai
	 * 
	 * @todo 修改用户信息广播接收着
	 * 
	 * @package com.kincai.store.ui
	 * 
	 * @time 2015-5-28 上午8:11:22
	 * 
	 */
	class ChangeUserInfoReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (Constants.USERINFO_CHANGE_ACTION.equals(intent.getAction())) {
				LogTest.LogMsg(TAG, "接收到广播");

				initData();
			} else {
				LogTest.LogMsg(TAG, "no");
			}
		}

	}


	@Override
	protected void onStart() {
		super.onStart();
		// initData();
		LogTest.LogMsg(TAG, "UserInfoActivity-onStart");
	}

	@Override
	protected void onResume() {
		super.onResume();

		// initData();
		LogTest.LogMsg(TAG, "UserInfoActivity-onResume");

	}

	@Override
	protected void onPause() {
		super.onPause();
		LogTest.LogMsg(TAG, "UserInfoActivity-onPause");
	}

	@Override
	protected void onDestroy() {
		unRegisterUserInfoReceiver();
		super.onDestroy();
		
		LogTest.LogMsg(TAG, "UserInfoActivity-onDestroy");

	}

}
