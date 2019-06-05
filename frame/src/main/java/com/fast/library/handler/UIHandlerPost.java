package com.fast.library.handler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 说明：在主线程中运行任务
 */
public class UIHandlerPost extends Handler {

    //异步标识
    private static final int ASYNC = 1;
    //同步标识
    private static final int SYNC = 2;
    //异步队列
    private final Queue<Runnable> mAsyncPool;
    //同步队列
    private final Queue<SyncRunnable> mSyncPool;
    private final int maxTimeOut;
    private boolean isAsyncActive;
    private boolean isSyncActive;

    public UIHandlerPost(Looper looper, int maxTime){
        super(looper);
        this.maxTimeOut = maxTime;
        mAsyncPool = new LinkedList<>();
        mSyncPool = new LinkedList<>();
    }

    /**
     * 说明：销毁资源
     */
    public void destroy(){
        this.removeCallbacksAndMessages(null);
        this.mSyncPool.clear();
        this.mAsyncPool.clear();
    }

    /**
     * 说明：添加异步任务
     * @param runnable
     */
    public void async(Runnable runnable){
        synchronized (mAsyncPool){
            mAsyncPool.offer(runnable);
            if (!isAsyncActive){
                isAsyncActive = true;
                if (!sendMessage(obtainMessage(ASYNC))){
                    throw new RuntimeException("不能发送异步任务");
                }
            }
        }
    }

    /**
     * 说明：添加同步任务
     * @param runnable
     */
    public void sync(SyncRunnable runnable){
        synchronized (mSyncPool){
            mSyncPool.offer(runnable);
            if (!isSyncActive){
                isSyncActive = true;
                if (!sendMessage(obtainMessage(SYNC))){
                    throw new RuntimeException("不能发送同步任务");
                }
            }
        }
    }

    @Override
    public void handleMessage(Message msg) {
        if (msg.what == ASYNC){
            boolean rescheduled = false;
            try {
                long started = SystemClock.uptimeMillis();
                while (true){
                    Runnable runnable = mAsyncPool.poll();
                    if (runnable == null){
                        synchronized (mAsyncPool){
                            runnable = mAsyncPool.poll();
                            if (runnable == null){
                                isAsyncActive = false;
                                return;
                            }
                        }
                    }
                    runnable.run();
                    long difTime = SystemClock.uptimeMillis() - started;
                    if (difTime >= maxTimeOut){
                        if (!sendMessage(obtainMessage(ASYNC))){
                            throw new RuntimeException("不能发送异步任务");
                        }
                        rescheduled = true;
                        return;
                    }
                }
            }finally {
                isAsyncActive = rescheduled;
            }
        }else if (msg.what == SYNC){
            boolean rescheduled = false;
            try {
                long started = SystemClock.uptimeMillis();
                while (true){
                    SyncRunnable runnable = mSyncPool.poll();
                    if (runnable == null){
                        synchronized (mSyncPool){
                            runnable = mSyncPool.poll();
                            if (runnable == null){
                                isAsyncActive = false;
                                return;
                            }
                        }
                    }
                    runnable.run();
                    long difTime = SystemClock.uptimeMillis() - started;
                    if (difTime >= maxTimeOut){
                        if (!sendMessage(obtainMessage(SYNC))){
                            throw new RuntimeException("不能发送同步任务");
                        }
                        rescheduled = true;
                        return;
                    }
                }
            }finally {
                isSyncActive = rescheduled;
            }
        }else {
            super.handleMessage(msg);
        }
    }
}
