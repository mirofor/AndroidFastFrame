package com.demo.frame.common;

import android.os.Bundle;
import com.demo.frame.common.fragment.FragmentBind;
import com.demo.frame.common.fragment.FragmentConfig;
import com.fast.frame.ActivityFrame;

/**
 * 说明：CommonRouter
 */
public class CommonRouter {

    public static void startFragment(ActivityFrame activityFrame, Class<? extends FragmentBind> clazz, Bundle bundle){
        if (activityFrame == null || clazz == null){
            return;
        }
        activityFrame.showActivity(ActivityBindFragment.class, FragmentConfig.getConfig(clazz.getName(),bundle));
    }
    public static void skipFragment(ActivityFrame activityFrame, Class<? extends FragmentBind> clazz, Bundle bundle){
        if (activityFrame == null || clazz == null){
            return;
        }
        activityFrame.skipActivity(ActivityBindFragment.class, FragmentConfig.getConfig(clazz.getName(),bundle));
    }
}
