package com.kincai.store.common;

import android.content.Context;
import android.os.Bundle;

import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.kincai.store.ui.activity.SearchGoodsAfterActivity;
import com.kincai.store.ui.activity.SearchGoodsOnActivity;
import com.kincai.store.utils.JsonUtil;
import com.kincai.store.utils.LogTest;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 *
 * @author kincai
 * 
 * @description 科大讯飞语音类
 *
 * @project Kincai_Store
 *
 * @package com.kincai.store.common
 *
 * @time 2015-8-25 下午2:03:21
 *
 */
public class IflytekVoice {

	protected static final String TAG = "IflytekVoice";

	public InitListener mInitListener; 

	// 科大讯飞语音听写对象
	private SpeechRecognizer mIat;
	private Context mContext;
	public IflytekVoice(Context context, SpeechRecognizer mIat) {
		mContext = context;
		this.mIat = mIat;
		
	}
	/**
	 * 设置语音听写参数
	 */
	public void setVoiceParam() {
		
//			// 清空参数
//			mIat.setParameter(SpeechConstant.PARAMS, null);
//
//			// 设置听写引擎
//			mIat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
//			// 设置返回结果格式
//			mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");
//
//			
//		
//			// 设置语言
//			mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
//			// 设置语言区域
//			mIat.setParameter(SpeechConstant.ACCENT, "mandarin");
//			
//
//			// 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
//			mIat.setParameter(SpeechConstant.VAD_BOS, "5000");
//			
//			// 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
//			mIat.setParameter(SpeechConstant.VAD_EOS, "1000");
//			
//			// 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
//			mIat.setParameter(SpeechConstant.ASR_PTT, "0");
//			
//			// 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
//			// 注：AUDIO_FORMAT参数语记需要更新版本才能生效
////			mIat.setParameter(SpeechConstant.AUDIO_FORMAT,"wav");
////			mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/iat.wav");
//		
		mIat.setParameter(SpeechConstant.DOMAIN, "iat");
		mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
		mIat.setParameter(SpeechConstant.ACCENT, "mandarin");
		
	}
	
	/**
	 * 听写监听器(无UI界面)。
	 */
	public class VoiceRecognizerListener implements RecognizerListener {

		
		/**
		 * 是否录音返回的第一个结果
		 */
		boolean isFirstResult = true;
		
		@Override
		public void onBeginOfSpeech() {
			// 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
			LogTest.LogMsg(TAG, "听写监听器->"+"开始说话");
		}

		@Override
		public void onError(SpeechError error) {
			// Tips：
			// 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
			// 如果使用本地功能（语记）需要提示用户开启语记的录音权限。
			LogTest.LogMsg(TAG, "听写监听器->错误信息:"+error.getPlainDescription(true));
		}

		@Override
		public void onEndOfSpeech() {
			// 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
			LogTest.LogMsg(TAG, "听写监听器->"+"结束说话");
		}

		@Override
		public void onResult(RecognizerResult results, boolean isLast) {
			LogTest.LogMsg(TAG, "听写监听器->语音结果："+results.getResultString());

			if(isFirstResult) {
				
				LogTest.LogMsg(TAG, "听写监听器-回调结果");
				callBackVoiceResult(results.getResultString());
				
				isFirstResult = false;
			}
			
			if (isLast) {
				// TODO 最后的结果
//				Utils.showToast(mContext, "最后 "+results.getResultString(), Toast.LENGTH_LONG);
				isFirstResult = true;
			}
		}

		@Override
		public void onVolumeChanged(int volume, byte[] data) {
			LogTest.LogMsg(TAG, "听写监听器->"+"当前正在说话，音量大小：" + volume);
			LogTest.LogMsg(TAG, "听写监听器->"+"返回音频数据："+data.length);
		}

		@Override
		public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
			// 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
			// 若使用本地能力，会话id为null
			//	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
			//		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
			//		Log.d(TAG, "session id =" + sid);
			//	}
		}
	};
	
	
	/**
	 * 听写UI监听器
	 */
	public class VoiceRecognizerDialogListener implements RecognizerDialogListener {
		/**
		 * 是否录音返回的第一个结果
		 */
		boolean isFirstResult = true;
		
		
		//isLast 返回结果最后那条
		public void onResult(RecognizerResult results, boolean isLast) {

			if (isFirstResult) {

				LogTest.LogMsg(TAG, "听写UI监听器-回调结果");
				callBackVoiceResult(results.getResultString());

				isFirstResult = false;
			}

			if (isLast) {
				// TODO 最后的结果
				// Utils.showToast(mContext, "最后 "+results.getResultString(),
				// Toast.LENGTH_LONG);
				isFirstResult = true;
			}

		}

		/**
		 * 识别回调错误.
		 */
		public void onError(SpeechError error) {
			LogTest.LogMsg(TAG, "听写UI监听器->错误信息:"+error.getPlainDescription(true));
		}

	};

	/**
	 * 回调语音结果
	 * @param results
	 */
	private void callBackVoiceResult(String results) {
		
		String parseResult = JsonUtil.parseIatResult(results);
		
		//回调对应类的方法
		if(mContext instanceof SearchGoodsOnActivity) {
			((SearchGoodsOnActivity)mContext).setVoiceResult(parseResult);
		} else if (mContext instanceof SearchGoodsAfterActivity) {
			((SearchGoodsAfterActivity)mContext).setVoiceResult(parseResult);
		}
	}
	
}
