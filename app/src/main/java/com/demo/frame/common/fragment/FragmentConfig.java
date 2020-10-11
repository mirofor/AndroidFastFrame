package com.demo.frame.common.fragment;

import android.os.Bundle;

import com.demo.frame.common.ActivityBindFragment;


/**
 * 说明：FragmentConfig
 * @author xiaomi
 */
public class FragmentConfig{

    private static String FRAGMENT_CONFIG_KEY = "FRAGMENT_CONFIG_KEY";
    public static String TITLE = "TITLE";
    public static String URL = "URL";
    public static String URL_CONTENT = "URL_CONTENT";
    public static String USE_RECEIVED_TITLE = "USE_RECEIVED_TITLE";

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
