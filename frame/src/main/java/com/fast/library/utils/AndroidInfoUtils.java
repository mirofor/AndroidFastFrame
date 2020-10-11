package com.fast.library.utils;

import android.annotation.SuppressLint;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.provider.Settings.Secure;

import com.fast.library.FastFrame;

/**
 * 说明：手机信息相关工具类
 */

@SuppressLint("MissingPermission")
public final class AndroidInfoUtils {

    private AndroidInfoUtils() {
    }


    /**
     * 获取手机Android_ID
     *
     * @return MacAddress String
     */
    public static String getAndroidId() {
        String androidId = Secure.getString(FastFrame.getApplication().getContentResolver(),
                Secure.ANDROID_ID);
        return androidId;
    }

    /**
     * 说明：获取系统信息
     *
     * @return
     */
    public static String getOs() {
        return Build.VERSION.RELEASE;
    }


    /**
     * 说明：获取当前应用程序的VersionName
     * <p>
     * 当前上下文环境
     *
     * @return 返回当前应用的版本号
     */
    public static String versionName() {
        try {
            PackageInfo info = FastFrame.getApplication().getPackageManager().getPackageInfo(
                    FastFrame.getApplication().getPackageName(), 0);
            return info.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取手机系统SDK版本
     *
     * @return 如API 17 则返回 17
     */
    public static int getSDKVersion() {
        return Build.VERSION.SDK_INT;
    }


}
