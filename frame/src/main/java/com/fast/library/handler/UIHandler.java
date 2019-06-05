package com.fast.library.handler;

import android.os.Looper;

/**
 * 说明：在主线程(子线程)中运行任务
 */
public class UIHandler {

    private static UIHandlerPost sUIPoster = null;
    private final static int maxTime = 20;

    private static UIHandlerPost getPoster(){
        if (sUIPoster == null){
            synchronized (UIHandler.class){
                if (sUIPoster == null){
                    sUIPoster = new UIHandlerPost(Looper.getMainLooper(),maxTime);
                }
            }
        }
        return sUIPoster;
    }

    /**
     * 说明：在主线程异步完成任务
     * @param runnable
     */
    public static void async(Runnable runnable){
        if (Looper.myLooper() == Looper.getMainLooper()){
            runnable.run();
            return;
        }
        getPoster().async(runnable);
    }

    /**
     * 说明：在主线程同步完成任务
     * @param runnable
     */
    public static void sync(Runnable runnable){
        if (Looper.myLooper() == Looper.getMainLooper()){
            runnable.run();
            return;
        }
        SyncRunnable syncRunnable = new SyncRunnable(runnable);
        getPoster().sync(syncRunnable);
        syncRunnable.waitRun();
    }

    /**
     * 说明：在主线程同步完成任务
     * @param runnable
     */
    public static void sync(Runnable runnable,int waitTime,boolean cancel){
        if (Looper.myLooper() == Looper.getMainLooper()){
            runnable.run();
            return;
        }
        SyncRunnable syncRunnable = new SyncRunnable(runnable);
        getPoster().sync(syncRunnable);
        syncRunnable.waitRun(waitTime, cancel);
    }

    public static void destroy(){
        if (sUIPoster != null){
            sUIPoster.destroy();
            sUIPoster = null;
        }
    }

    /**
     * 说明：任务运行在子线程
     */
    public static void subThread(Runnable runnable){
        if (runnable != null){
            new Thread(runnable).start();
        }
    }
}
