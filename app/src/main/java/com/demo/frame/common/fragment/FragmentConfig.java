package com.demo.frame.common.fragment;

import android.os.Bundle;

import com.demo.frame.common.ActivityBindFragment;


/**
 * 说明：FragmentConfig
 */
public class FragmentConfig{

    private static String FRAGMENT_CONFIG_KEY = "FRAGMENT_CONFIG_KEY";
    public static String TITLE = "TITLE";
    public static String URL = "URL";
    public static String URL_CONTENT = "URL_CONTENT";
    public static String USE_RECEIVED_TITLE = "USE_RECEIVED_TITLE";

    public static String ORDER_FILE_ID = "ORDER_FILE_ID"; // 文件id
    public static String SIGN_USER_ID = "SIGN_USER_ID"; // 签署人user_id
    public static String SIGN_RECORD_ID = "SIGN_RECORD_ID"; // 签署sign表记录id
    public static String SIGN_FILE_DETAIL = "SIGN_FILE_DETAIL"; // 签署文件详情
    public static String SIGN_IS_PREVIEW = "SIGN_IS_PREVIEW"; // 只预览
    public static String SIGN_FILE_URL = "SIGN_FILE_URL";// 签署文件地址
    public static String IS_FROM_CUSTOM_SIGNN = "IS_FROM_CUSTOM_SIGNN"; // 是否从手签版页面进入

    private FragmentConfig(){}

    public static Bundle getConfig(String name, Bundle data){
        Bundle bundle = new Bundle();
        if (data != null){
            bundle.putAll(data);
        }
        bundle.putString(FRAGMENT_CONFIG_KEY,name);
        return bundle;
    }

    public static FragmentBind getFragment(ActivityBindFragment activityBindFragment){
        if (activityBindFragment != null && activityBindFragment.getIntent() != null &&
                activityBindFragment.getIntent().getExtras() != null &&
                activityBindFragment.getIntent().getExtras().containsKey(FRAGMENT_CONFIG_KEY)){
            return (FragmentBind) FragmentBind.instantiate(activityBindFragment,
                    activityBindFragment.getIntent().getExtras().getString(FRAGMENT_CONFIG_KEY),activityBindFragment.getIntent().getExtras());
        }else {
            return null;
        }
    }

}
