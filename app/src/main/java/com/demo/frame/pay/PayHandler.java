package com.demo.frame.pay;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;

import java.util.Map;

/**
 * 说明：PayHandler
 */
public class PayHandler extends Handler {

    private static final int SDK_PAY_FLAG = 1;
    private PayListener mPayListener;

    public PayHandler() {
    }

    public void handleMessage(Message msg) {
        switch (msg.what) {
            case SDK_PAY_FLAG: {
                PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                /**
                 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                 */
//                String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                String resultStatus = payResult.getResultStatus();
                // 判断resultStatus 为9000则代表支付成功
                if (TextUtils.equals(resultStatus, "9000")) {
                    // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                    if (mPayListener != null) {
                        mPayListener.payResult(PayConstant.Result.SUCCESS);
                    }
                } else if (TextUtils.equals(resultStatus, "6001")) {
                    // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                    if (mPayListener != null) {
                        mPayListener.payResult(PayConstant.Result.CANCEL);
                    }
                } else {
                    // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                    if (mPayListener != null) {
                        mPayListener.payResult(PayConstant.Result.FAIL);
                    }
                }
                break;
            }
            default:
                break;
        }
    }

    /**
     * 支付宝支付
     *
     * @param activity
     * @param data
     */
    public void aliPay(final Activity activity, final String data, PayListener listener) {
        this.mPayListener = listener;
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(activity);
                Map<String, String> result = alipay.payV2(data, false);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                sendMessage(msg);
            }
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }


//    /**
//     * 微信支付
//     * @param context
//     * @param payReq
//     * @param listener
//     */
//    public void wxPay(Context context, PayReq payReq, PayListener listener){
////        WXPayEntryActivity.registerPayListener(listener);
////        WxUtils.pay(context,payReq);
//    }

    public void onDestory() {
        this.mPayListener = null;
//        WXPayEntryActivity.unRegisterPayListener();
    }
}
