package com.fast.library;

import android.app.Application;

/**
 * 说明：初始化
 */
public class FastFrame {

    //上下文
    private static Application mApplication;
    //是否调试
    public static boolean isDebug = true;

    /**
     * 说明：框架初始化
     * @param application
     */
    public static void init(Application application){
        mApplication = application;
    }

    /**
     * 说明：框架初始化
     * @param application
     */
    public static void init(Application application,boolean debug){
        mApplication = application;
        isDebug = debug;
    }

    /**
     * 说明：设置调试模式
     * @param debug
     */
    public static void setDebug(boolean debug){
        isDebug = debug;
    }

    /**
     * 说明：获取上下文
     * @return
     */
    public static Application getApplication(){
        if (mApplication == null){
            throw new RuntimeException("FastFrame框架没有初始化，请在Applciation中调用init()，进行初始化");
        }
        return mApplication;
    }

}
