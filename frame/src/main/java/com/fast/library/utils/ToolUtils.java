package com.fast.library.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Looper;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.text.format.Formatter;
import android.view.View;

import com.fast.frame.ActivityFrame;
import com.fast.library.FastFrame;
import com.fast.library.tools.TaskEngine;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

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
     * 延迟显示输入法
     *
     * @param activityFrame
     * @param view
     */
    public static void showSoftInput(final ActivityFrame activityFrame, final View view) {
        TaskEngine.getInstance().schedule(new TimerTask() {
            @Override
            public void run() {
                if (ToolUtils.isNotFinish(activityFrame)) {
                    activityFrame.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            KeyBoardUtils.showSoftInput(view);
                        }
                    });
                }
            }
        }, 300);
    }

    /**
     * 页面没有关闭
     * @param activity
     * @return
     */
    public static boolean isNotFinish(Activity activity){
        if (activity == null || activity.isFinishing()){
            return false;
        }else {
            return true;
        }
    }

    /**
     * 说明：创建快捷方式
     *
     * @param cxt   Context
     * @param icon  快捷方式图标
     * @param title 快捷方式标题
     * @param cls   要启动的类
     */
    public static void createDeskShortCut(Context cxt, int icon, String title,
                                   Class<?> cls) {
        // 创建快捷方式的Intent
        Intent shortcutIntent = new Intent(
                "com.android.launcher.action.INSTALL_SHORTCUT");
        // 不允许重复创建
        shortcutIntent.putExtra("duplicate", false);
        // 需要现实的名称
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);
        // 快捷图片
        Parcelable ico = Intent.ShortcutIconResource.fromContext(
                cxt.getApplicationContext(), icon);
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, ico);
        Intent intent = new Intent(cxt, cls);
        // 下面两个属性是为了当应用程序卸载时桌面上的快捷方式会删除
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        // 点击快捷图片，运行的程序主入口
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
        // 发送广播。OK
        cxt.sendBroadcast(shortcutIntent);
    }

    /**
     * 说明：使用浏览器打开
     */
    public static void openBrowser(Activity activity,String url){
        try {
            if (StringUtils.isEmpty(url) || !url.startsWith("http") || activity == null){
                return;
            }
            Uri  uri = Uri.parse(url);
            Intent  intent = new  Intent(Intent.ACTION_VIEW, uri);
            activity.startActivity(intent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 说明：安装apk
     *
     * @param context
     * @param file
     */
    public static void installApk(Context context, File file) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setType("application/vnd.android.package-archive");
        intent.setData(Uri.fromFile(file));
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 说明：清理后台进程与服务
     *
     * @param cxt 应用上下文对象context
     * @return 被清理的数量
     */
    @SuppressLint("MissingPermission")
    public static int gc(Context cxt) {
        int count = 0; // 清理掉的进程数
        ActivityManager am = (ActivityManager) cxt
                .getSystemService(Context.ACTIVITY_SERVICE);
        // 获取正在运行的service列表
        List<RunningServiceInfo> serviceList = am.getRunningServices(100);
        if (serviceList != null)
            for (RunningServiceInfo service : serviceList) {
                if (service.pid == android.os.Process.myPid())
                    continue;
                try {
                    android.os.Process.killProcess(service.pid);
                    count++;
                } catch (Exception e) {
                    e.getStackTrace();
                    continue;
                }
            }

        // 获取正在运行的进程列表
        List<RunningAppProcessInfo> processList = am.getRunningAppProcesses();
        if (processList != null)
            for (RunningAppProcessInfo process : processList) {
                // 一般数值大于RunningAppProcessInfo.IMPORTANCE_SERVICE的进程都长时间没用或者空进程了
                // 一般数值大于RunningAppProcessInfo.IMPORTANCE_VISIBLE的进程都是非可见进程，也就是在后台运行着
                if (process.importance > RunningAppProcessInfo.IMPORTANCE_VISIBLE) {
                    // pkgList 得到该进程下运行的包名
                    String[] pkgList = process.pkgList;
                    for (String pkgName : pkgList) {
                        try {
                            am.killBackgroundProcesses(pkgName);
                            count++;
                        } catch (Exception e) { // 防止意外发生
                            e.getStackTrace();
                            continue;
                        }
                    }
                }
            }
        return count;
    }

    /**
     * 说明：拨打电话号码
     *
     * @param number 电话号码
     */
    public static void callPhone(Context context, String number) {
        try {
            Uri uri = Uri.parse("tel:" + number);
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(uri);
            if(ActivityCompat.checkSelfPermission(context, "android.permission.CALL_PHONE") != 0) {
                return;
            }
            context.startActivity(intent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 说明：判断是否是UI线程
     * @return
     */
    public static boolean isMainThread(){
        return Looper.myLooper() == Looper.getMainLooper();
    }

    /**
     * 说明：刷新图库
     *
     * @param context
     * @param file
     */
    public static void refreshPic(Context context, File file) {
        try {
            // 最后通知图库更新
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 说明：文件单位转换
     *
     * @param context
     * @param size
     * @return
     */
    public static String formatFileSize(Context context, long size) {
        return Formatter.formatFileSize(context, size);
    }

    /**
     * 说明：执行command命令，返回文本
     *
     * @param command 命令
     * @return 字符串
     */
    public static String cmdForResult(String command) {
        StringBuilder sb = new StringBuilder();
        DataOutputStream dos = null;
        BufferedReader reader = null;
        try {
            Process process = Runtime.getRuntime().exec("su");
            dos = new DataOutputStream(process.getOutputStream());
            dos.writeBytes(command + "\n");
            dos.flush();
            dos.writeBytes("exit\n");
            dos.flush();

            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        } finally {
            FileUtils.closeIO(dos, reader);
        }
    }

    /**
     * 说明：将文本内容复制到剪切板中
     *
     * @param context
     * @param content 要复制的内容
     */
    public static boolean copyText(Context context, String content) {
        try {
            if (content == null || content == null) {
                return false;
            } else {
                ClipboardManager clip = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                clip.setPrimaryClip(ClipData.newPlainText(null, content));
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
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
                String versionCode = pi.versionCode + "";
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

