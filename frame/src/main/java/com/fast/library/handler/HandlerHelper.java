package com.fast.library.handler;

import android.os.Handler;
import android.os.Message;

/**
 * 说明：Handler帮助类
 */
public class HandlerHelper extends Handler{

    private static HandlerHelper sHanlder = null;
    private final static int MSG_WHAT_MAINRUN = 0;
    private static MainRunListener sMainRunListener = null;

    private HandlerHelper(){}

    private static HandlerHelper get(){
        if (sHanlder == null){
            sHanlder = new HandlerHelper();
        }
        return sHanlder;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what){
            case MSG_WHAT_MAINRUN:
                if (sMainRunListener != null){
                    sMainRunListener.run();
                }
                break;
        }
    }

    public static void postDelay(long delay,Runnable runnable){
        get().postDelayed(runnable, delay);
    }

    public static void postDelayMain(long delay,MainRunListener listener){
        sMainRunListener = listener;
        get().sendEmptyMessageDelayed(MSG_WHAT_MAINRUN,delay);
    }

    /**
     * 说明：运行在主线程的方法
     */
    public interface MainRunListener{
        void run();
    }

}
