package com.demo.frame.common.sms;

import android.widget.TextView;

/**
 * 说明：OnSmsCodeCallBack
 */
public class OnSmsCodeCallBack<T extends TextView> {
    public void onTick(T view,long secondsUntilFinished) {}
    public void onFinish(T view) {}
    public void onStart() {}
    public void onCancel() {}
}
