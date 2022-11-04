package com.demo.frame;

import android.content.Context;

import com.fast.frame.FrameApp;
import com.fast.library.http.HttpConfig;
import com.tamsiree.rxkit.RxTool;

import androidx.multidex.MultiDex;


public class BikeApp extends FrameApp {
    public static boolean APP_DUBUG = false;

    @Override
    public void onCreate() {
        super.onCreate();
        RxTool.init(this);
        disableApiDialog();
    }


    @Override
    protected HttpConfig.Builder setHttpBuilder() {
        HttpConfig.Builder builder = new HttpConfig.Builder();
        builder.setTrustAll(true);
        builder.setDebug(true);
        return builder;
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

}
