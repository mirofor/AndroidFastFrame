package com.fast.library.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * 说明：SD卡工具类
 */
public class SDCardUtils {

    /**
     * 说明：禁止实例化
     */
    private SDCardUtils(){}

    /**
     * 说明：SD卡是否可用
     * @return
     */
    public static boolean isAvailable(){
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)
                || !Environment.isExternalStorageRemovable();
    }

    /**
     * 说明：外部目录 sdcard/...
     * @return
     */
    public static File getExternalStorage(){
        return Environment.getExternalStorageDirectory();
    }

    /**
     * 说明：外部缓存目录 [SDCard/Android/data/应用包名/...]
     * @param context
     * @return
     */
    public static File getExternalCache(Context context){
        File cache = null;
        if (context != null){
            cache = context.getExternalCacheDir();
        }
        return cache;
    }

    /**
     * 说明：内部缓存目录 [/data/data/应用包名/cache/...]
     * @param context
     * @return
     */
    public static File getInnerCache(Context context){
        File cache = null;
        if (context != null){
            cache = context.getCacheDir();
        }
        return cache;
    }

    /**
     * 说明：获取手机内部路径[/data/data/应用包名/files]
     * @param context
     * @return
     */
    public static String getFilesDir(Context context) {
        return context.getFilesDir().getAbsolutePath();
    }
}
