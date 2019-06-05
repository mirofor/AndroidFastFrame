package com.fast.library.tools;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Set;

/**
 * 说明：App前台检测管理
 */
public class AppForegroundStateManager {

    private static final int MSG_NOTIFY_LISTENERS = 10001;
    public static long APP_CLOSE_VALIDATION_TIME_IN_MS = 30 * 1000;

    private Set<OnAppForegroundStateChangeListener> mStateChangeListener;
    private NotifyListenerHandler mNotifyListenerHandler;
    private WeakReference<Activity> mForegroundActivity;
    private AppForegroundState mCurrentAppState = AppForegroundState.BACKGROUND;

    private static class SingletonHolder{
        public static final AppForegroundStateManager INSTANCE =
                new AppForegroundStateManager();
    }

    private AppForegroundStateManager(){
        mNotifyListenerHandler = new NotifyListenerHandler(Looper.getMainLooper());
        mStateChangeListener = new HashSet<>();
    }

    public static AppForegroundStateManager getInstance(){
        return SingletonHolder.INSTANCE;
    }

    public static AppForegroundStateManager getInstance(long closeValidationTime){
        if (closeValidationTime > 0){
            APP_CLOSE_VALIDATION_TIME_IN_MS = closeValidationTime;
        }
        return SingletonHolder.INSTANCE;
    }

    public void addListener(@NonNull OnAppForegroundStateChangeListener listener){
        mStateChangeListener.add(listener);
    }

    public void removeListener(@NonNull OnAppForegroundStateChangeListener listener){
        mStateChangeListener.remove(listener);
    }

    public void onActivityVisible(Activity activity){
        if (activity == null){
            throw new IllegalArgumentException("onActivityVisible:  "+activity.getClass().getName() + "is not Activity");
        }
        if (mForegroundActivity != null){
            mForegroundActivity.clear();
        }
        mForegroundActivity = new WeakReference<Activity>(activity);
        determineAppForegroundState();
    }

    public void onActivityInVisible(Activity activity){
        if (activity == null){
            throw new IllegalArgumentException("onActivityInVisible:  "+activity.getClass().getName() + "is not Activity");
        }
        if (mForegroundActivity != null){
            Activity aty = mForegroundActivity.get();
            if (aty == activity){
                mForegroundActivity.clear();
                mForegroundActivity = null;
            }
        }
        determineAppForegroundState();
    }

    public boolean isAppInForeground(){
        return mCurrentAppState == AppForegroundState.FOREGROUND;
    }

    private void determineAppForegroundState(){
        AppForegroundState oldState = mCurrentAppState;
        final boolean isInForeground = mForegroundActivity != null && mForegroundActivity.get() != null;
        mCurrentAppState = isInForeground ? AppForegroundState.FOREGROUND:AppForegroundState.BACKGROUND;
        if (oldState != mCurrentAppState){
            validateThenNotifyListeners();
        }
    }

    private void validateThenNotifyListeners(){
        if (mNotifyListenerHandler.hasMessages(MSG_NOTIFY_LISTENERS)){
            mNotifyListenerHandler.removeMessages(MSG_NOTIFY_LISTENERS);
        }else {
            if (mCurrentAppState == AppForegroundState.BACKGROUND){
                mNotifyListenerHandler.sendEmptyMessage(MSG_NOTIFY_LISTENERS);
            }else {
                mNotifyListenerHandler.sendEmptyMessageDelayed(MSG_NOTIFY_LISTENERS,APP_CLOSE_VALIDATION_TIME_IN_MS);
            }
        }
    }

    private void notifyListeners(AppForegroundState newState){
        for (OnAppForegroundStateChangeListener listener : mStateChangeListener){
            listener.onAppForegroundStateChanged(newState);
        }
    }

    public enum AppForegroundState{
        FOREGROUND,//前台
        BACKGROUND//后台
    }

    public interface OnAppForegroundStateChangeListener{
        void onAppForegroundStateChanged(AppForegroundState appState);
    }

    private class NotifyListenerHandler extends Handler{
        private NotifyListenerHandler(Looper looper){
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_NOTIFY_LISTENERS:
                    notifyListeners(mCurrentAppState);
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    }
}
