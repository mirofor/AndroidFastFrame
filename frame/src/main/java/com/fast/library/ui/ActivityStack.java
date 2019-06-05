package com.fast.library.ui;

import android.app.Activity;
import java.lang.ref.WeakReference;
import java.util.Stack;

/**
 * 说明：应用程序Activity管理类：用于Activity管理和应用程序退出
 */
public class ActivityStack {

    private static Stack<WeakReference<Activity>> activities;
    private static final ActivityStack instance = new ActivityStack();

    private ActivityStack(){}

    public static ActivityStack create(){
        return instance;
    }

    /**
     * 说明：获取当前Activity栈中数量
     * @return
     */
    public int getCount(){
        int count = 0;
        if (activities != null) {
            count = activities.size();
        }
        return count;
    }

    /**
     * 说明：将activity添加到栈中
     * @param activity
     */
    public void addActivity(Activity activity){
        if (activities == null) {
            activities = new Stack<>();
        }
        activities.add(new WeakReference<>(activity));
    }

    /**
     * 说明：获取栈顶的Activity
     * @return
     */
    public Activity topActivity(){
        if (activities == null || activities.isEmpty() || activities.lastElement().get() == null) {
            throw new NullPointerException(
                    "Activity stack is Null,your Activity must extend FlyActivity");
        }
        return activities.lastElement().get();
    }

    /**
     * 说明：检查是否存在该Activity
     * @param cls
     * @return
     */
    public boolean isCreateActivity(Class<?> cls){
        return findActivity(cls) != null;
    }

    /**
     * 说明：查找activity,没有则返回null
     * @param cls
     * @return
     */
    public Activity findActivity(Class<?> cls){
        Activity activity = null;
        for (WeakReference<Activity> aty : activities) {
            if (aty != null && aty.get() != null && aty.get().getClass().equals(cls)) {
                activity = aty.get();
                break;
            }
        }
        return activity;
    }

    /**
     * 说明：结束指定activity
     * @param activity
     */
    public void finishActivity(Activity activity){
        if (activity != null) {
            if (activities.contains(activity)){
                activities.remove(activity);
            }
            if (!activity.isFinishing()){
                activity.finish();
            }
        }
    }

    /**
     * 说明：结束指定activity
     * @param cls
     */
    public void finishActivity(Class<?> cls){
        if (activities != null && !activities.isEmpty()) {
            WeakReference<Activity> temp = null;
            for (WeakReference<Activity> aty : activities) {
                if (aty != null && aty.get() != null && aty.get().getClass().equals(cls)) {
                    temp = aty;
                }
            }
            if (temp != null){
                finishActivity(temp.get());
            }
        }
    }

    /**
     * 说明：清除cls外其他所有activity
     * @param cls
     */
    public void finishOtherActivity(Class<?> cls){
        if (activities != null && !activities.isEmpty() && cls != null) {
            WeakReference<Activity> temp = null;
            for (WeakReference<Activity> aty : activities) {
                if (aty.get() != null && aty.get().getClass() != null && !aty.get().getClass().equals(cls)){
                    temp = aty;
                }
            }
            if (temp != null){
                finishActivity(temp.get());
            }
        }
    }

    /**
     * 说明：结束所有activity
     */
    public void finishAllActivity(){
        if (activities != null && !activities.isEmpty()) {
            for(int i = 0,size = activities.size();i<size;i++){
                if (null != activities.get(i)) {
                    finishActivity(activities.get(i).get());
                }
            }
            activities.clear();
        }
    }

    /**
     * 说明：退出应用
     */
    public void AppExit(){
        try {
            finishAllActivity();
        } catch (Exception e) {
            e.printStackTrace();
            Runtime.getRuntime().exit(-1);
        }
    }
}