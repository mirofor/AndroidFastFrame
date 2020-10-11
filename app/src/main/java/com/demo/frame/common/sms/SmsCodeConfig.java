package com.demo.frame.common.sms;

import android.support.annotation.ColorInt;
import android.widget.TextView;

/**
 * 说明：SmsCodeConfig
 */
public class SmsCodeConfig<T extends TextView>{
    private T view;
    private @ColorInt
    int tickBackgroundColor;//计时中背景颜色
    private @ColorInt int normalBackgroundColor;//默认时背景颜色
    private String normalText = "发送验证码";//默认文字
    private long millisInFuture = 60 * 1000;
    private long countDownInterval = 1000;
    private OnSmsCodeCallBack callBack;

    public OnSmsCodeCallBack getCallBack() {
        return callBack;
    }

    public SmsCodeConfig setCallBack(OnSmsCodeCallBack callBack) {
        this.callBack = callBack;
        return this;
    }

    public T getView() {
        return view;
    }

    public SmsCodeConfig setView(T view) {
        this.view = view;
        return this;
    }

    public String getNormalText() {
        return normalText;
    }

    public SmsCodeConfig setNormalText(String normalText) {
        this.normalText = normalText;
        return this;
    }

    public int getTickBackgroundColor() {
        return tickBackgroundColor;
    }

    public SmsCodeConfig setTickBackgroundColor(int tickBackgroundColor) {
        this.tickBackgroundColor = tickBackgroundColor;
        return this;
    }

    public int getNormalBackgroundColor() {
        return normalBackgroundColor;
    }

    public SmsCodeConfig setNormalBackgroundColor(int normalBackgroundColor) {
        this.normalBackgroundColor = normalBackgroundColor;
        return this;
    }

    public long getMillisInFuture() {
        return millisInFuture;
    }

    public SmsCodeConfig setMillisInFuture(long millisInFuture) {
        this.millisInFuture = millisInFuture;
        return this;
    }

    public long getCountDownInterval() {
        return countDownInterval;
    }

    public SmsCodeConfig setCountDownInterval(long countDownInterval) {
        this.countDownInterval = countDownInterval;
        return this;
    }

    public SmsCodeHelper<T> create(){
        if (getView() == null){
            throw new RuntimeException("请绑定一个View");
        }
        return new SmsCodeHelper(this);
    }
}
