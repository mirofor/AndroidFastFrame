package com.demo.frame.common.sms;

import android.os.CountDownTimer;
import android.widget.TextView;

/**
 * 说明：发送短信倒计时
 */
@SuppressWarnings("unchecked")
public class SmsCodeHelper<T extends TextView> {

    private String tickTextFormat = "%s秒";
    private MyCountTimer mCountTimer;
    private SmsCodeConfig<T> mConfig;

    private class MyCountTimer extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public MyCountTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if (mConfig.getTickBackgroundColor() != 0){
                mConfig.getView().setBackgroundColor(mConfig.getTickBackgroundColor());
            }
            mConfig.getView().setEnabled(false);
            long s = millisUntilFinished / 1000;
            mConfig.getView().setText(String.format(String.valueOf(tickTextFormat),s));
            if (mConfig.getCallBack() != null){
                mConfig.getCallBack().onTick(mConfig.getView(),s);
            }
        }

        @Override
        public void onFinish() {
            if (mConfig.getNormalBackgroundColor() != 0){
                mConfig.getView().setBackgroundColor(mConfig.getNormalBackgroundColor());
            }
            mConfig.getView().setEnabled(true);
            mConfig.getView().setText(mConfig.getNormalText());

            if (mConfig.getCallBack() != null){
                mConfig.getCallBack().onFinish(mConfig.getView());
            }
        }
    }

    public static SmsCodeConfig getConfig(){
        return new SmsCodeConfig();
    }

    public SmsCodeHelper(SmsCodeConfig<T> config){
        this.mConfig = config;
        mCountTimer = new MyCountTimer(mConfig.getMillisInFuture(),mConfig.getCountDownInterval());
    }

    public void start(){
        if (mCountTimer != null){
            mCountTimer.start();
        }
        if (mConfig.getCallBack() != null){
            mConfig.getCallBack().onStart();
        }
    }

    public void cancel(){
        if (mCountTimer != null){
            mCountTimer.cancel();
            mCountTimer.onFinish();
        }
        if (mConfig.getCallBack() != null){
            mConfig.getCallBack().onCancel();
        }
    }

    public void cancelTimer(){
        if (mCountTimer != null){
            mCountTimer.cancel();
        }
        if (mConfig.getNormalBackgroundColor() != 0){
            mConfig.getView().setBackgroundColor(mConfig.getNormalBackgroundColor());
        }
        mConfig.getView().setEnabled(true);
        mConfig.getView().setText(mConfig.getNormalText());

        if (mConfig.getCallBack() != null){
            mConfig.getCallBack().onCancel();
        }
    }

    public void onDestory(){
        if (mCountTimer != null){
            mCountTimer.cancel();
        }
    }
}
