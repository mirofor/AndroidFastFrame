package com.demo.frame.sms;

/**
 * 短信接收器回调接口
 */
public interface SmsResponseCallback {
	
	/**
	 * 返回短信内容
	 * 
	 * @param smsContent
	 * @see [类、类#方法、类#成员]
	 */
	void onCallbackSmsContent(String smsContent);
}
