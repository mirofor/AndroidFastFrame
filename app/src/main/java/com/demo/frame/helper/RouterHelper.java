package com.demo.frame.helper;

import android.app.Activity;
import android.content.Intent;

import com.demo.frame.ui.activity.ActivityHome;
import com.fast.frame.ActivityFrame;
import com.demo.frame.ui.activity.ActivityStart;
import com.demo.frame.ui.activity.ActivityStartLogin;

/**
 * 页面跳转
 */
public class RouterHelper {

    public static void startSplash(Activity activityFrame) {
        Intent intent = new Intent(activityFrame, ActivityStart.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activityFrame.startActivity(intent);
        activityFrame.finish();
    }


    /**
     * 注册+登录提示页面
     */
    public static void startLoginTip(ActivityFrame activityFrame) {
        activityFrame.showActivity(ActivityStartLogin.class);
    }

    /**
     * 注册+登录提示页面
     */
    public static void startHomePage(ActivityFrame activityFrame) {
        activityFrame.showActivity(ActivityHome.class);
    }



}
