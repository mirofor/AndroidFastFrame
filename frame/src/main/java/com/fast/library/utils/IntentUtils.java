package com.fast.library.utils;

import android.content.Intent;

/**
 * 说明：获取Intent数据
 */
public class IntentUtils {

    public static String getStringExtra(Intent intent,String key){
        if (!StringUtils.isEmpty(key) && intent != null && intent.hasExtra(key)){
            return intent.getStringExtra(key);
        }else {
            return "";
        }
    }

}
