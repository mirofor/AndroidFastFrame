package com.fast.library.utils;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.fast.library.FastFrame;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 说明：简单工具类
 */
public final class ToolUtils {
    private static long lastClickTime;
    /**
     * 上次点击view id
     */
    private static long lastClickId;

    public static boolean isFastDoubleClick(int id) {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (lastClickId == id && (0 < timeD && timeD < 1000)) {
            return true;
        }
        lastClickId = id;
        lastClickTime = time;
        return false;
    }

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 1000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }


    /**
     * 页面没有关闭
     *
     * @param activity
     * @return
     */
    public static boolean isNotFinish(Activity activity) {
        if (activity == null || activity.isFinishing()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 说明：获取错误信息
     *
     * @return
     */
    public static String collectErrorInfo(Throwable ex) {
        Map<String, String> infos = new HashMap<String, String>();
        try {
            PackageManager pm = FastFrame.getApplication().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(FastFrame.getApplication().getPackageName(),
                    PackageManager.GET_ACTIVITIES);
            if (pi != null) {

                String versionName = pi.versionName == null ? "null"
                        : pi.versionName;
                String versionCode =  "";

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    versionCode = pi.getLongVersionCode()+"";
                } else {
                    versionCode = pi.versionCode+"";
                }

                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
            } catch (Exception e) {
            }
        }

        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        sb.append(writer.toString());
        return sb.toString();
    }


}

