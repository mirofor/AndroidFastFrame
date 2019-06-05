package com.demo.frame.helper;

import com.fast.library.utils.SPUtils;

/**
 * 说明：SpHelper
 */
public class SpHelper {

    private static SPUtils sSp = SPUtils.getInstance("FastFrameDemo");

    public interface Key{
        String SIGN = "SIGN";
        String TOKEN = "TOKEN";
        String USER_INFO = "USER_INFO";
        String DEVICE_ID = "DEVICE_ID";
        String USER_ID = "USER_ID";
        String MOBILE = "MOBILE";
        String CITY_LIST_DATA = "CITY_LIST_DATA";
    }

    public static SPUtils getSp(){
        return sSp;
    }

}
