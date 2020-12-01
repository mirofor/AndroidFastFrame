package com.fast.library.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Looper;
import android.os.SystemClock;

import com.fast.library.FastFrame;
import com.fast.library.ui.ActivityStack;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 说明：手机日志的工具类
 * @author xiaomi
 */
public abstract class CrashHandler implements UncaughtExceptionHandler {

    private static String crashFile;
    //错误日志文件名
    private String fileName = "crash";
    // 用来存储设备信息和异常信息
    private Map<String, String> infos = new HashMap<>();
    //错误信息
    private StringBuffer sb = new StringBuffer();

    public CrashHandler() {
        //设置错误文件目录
        crashFile = setCrashFilePath();
        //生成错误日志文件名
        fileName = setFileName();
    }

    /**
     * 初始化
     */
    public void init(){
        Thread.setDefaultUncaughtExceptionHandler(this);
        if (isCleanHistory()){
            FileUtils.deleteAllFile(new File(crashFile));
        }
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        // 处理异常
        handleException(ex, crashFile);
        SystemClock.sleep(setLoadingTime());
        // 退出应用
        ActivityStack.create().AppExit();
    }

    /**
     * 说明：设置退出应用的等待时间
     * @return
     */
    public long setLoadingTime(){
        return 3000;
    }

    /**
     * 说明：自定义错误处理,收集错误信息 发送错误报告等操作均在此完成. 开发者可以根据自己的情况来自定义异常处理逻辑
     *
     * @return true代表处理该异常，不再向上抛异常，
     *         false代表不处理该异常(可以将该log信息存储起来)然后交给上层(这里就到了系统的异常处理)去处理，
     *         简单来说就是true不会弹出那个错误提示框，false就会弹出
     */
    private void handleException(final Throwable ex, String filePath) {
        if (ex == null) {
            return;
        }
        // 收集设备参数信息
        collectDeviceInfo(FastFrame.getApplication());
        // 保存日志文件
        String upFile = saveCrashInfo2File(ex, filePath);
        //上传服务器
        upCrashLog(new File(upFile),sb.toString());
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                showCrashTip();
                Looper.loop();
            }

        }.start();
    }

    /**
     * 说明：收集设备参数信息
     *
     * @param ctx
     */
    private void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
                    PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null"
                        : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (NameNotFoundException e) {
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
            } catch (Exception e) {
            }
        }
    }

    /**
     * 说明：保存错误信息到文件中
     *
     * @param ex
     * @return 返回文件名称,便于将文件传送到服务器
     */
    private String saveCrashInfo2File(Throwable ex, String filePath) {
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }
        // 时间
        sb.append(toDateString()+ "\n");

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        if (isSaveLocal()){
            FileUtils.saveFileCache(sb.toString().getBytes(), filePath, fileName);
        }
        return filePath + fileName;
    }

    private String toDateString(){
        long current = System.currentTimeMillis();
        Calendar calc = Calendar.getInstance();
        calc.setTimeInMillis(current);
        return String.format(Locale.CHINESE, "%04d.%02d.%02d %02d:%02d:%02d:%03d",
                calc.get(Calendar.YEAR), calc.get(Calendar.MONTH) + 1, calc.get(Calendar.DAY_OF_MONTH),
                calc.get(Calendar.HOUR_OF_DAY), calc.get(Calendar.MINUTE), calc.get(Calendar.SECOND), calc.get(Calendar.MILLISECOND));
    }

    /**
     * 说明：向服务器发送错误日志
     * @param file 本地错误文件
     * @param error 错误信息
     */
    public abstract void upCrashLog(File file,String error);

    /**
     * 说明：设置文件名
     */
    public abstract String setFileName();

    /**
     * 说明：程序崩溃退出时调用
     */
    public void showCrashTip(){}

    /**
     * 说明：是否保存本地日志文件
     */
    public boolean isSaveLocal(){
        return true;
    }

    /**
     * 说明：设置崩溃日志保存路径
     * @return
     */
    public abstract String setCrashFilePath();

    /**
     * 说明：开启后，每次新打开应用都会清理崩溃日志
     * @return
     */
    public boolean isCleanHistory(){
        return false;
    }

}

