package com.fast.library.utils;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.fast.library.BuildConfig;
import com.fast.library.FastFrame;

import java.io.File;

import androidx.core.app.ActivityCompat;

/**
 * 说明：手机信息相关工具类
 */

@SuppressLint("MissingPermission")
public final class AndroidInfoUtils {

    private AndroidInfoUtils() {
    }

    /**
     * 说明：获取手机IMEI号码(国际移动身份识别码)
     * 由15位数字组成的”电子串号”，其组成结构为TAC（6位数字）+FAC（两位数字）+SNR（6位数字）+SP （1位数字）。
     * 它与每台手机一一对应，而且该码是全世界唯一的。每一只手机在组装完成后都将被赋予一个全球唯一的一组号码，这个号码从生产到交付使用都将被制造生产的厂商所记录。
     * IMEI码贴在手机背面的标志上，并且读写于手机内存中。它也是该手机在厂家的”档案”和”身份证号”。
     *
     * @return 返回手机IMEI号码
     */
    public static String getImeiCode() {
        String result = "";
        try {
            if (ActivityCompat.checkSelfPermission(FastFrame.getApplication(), "android.permission.READ_PHONE_STATE") != 0) {
                return "";
            }
            final TelephonyManager tm = (TelephonyManager) FastFrame.getApplication()
                    .getSystemService(Context.TELEPHONY_SERVICE);

            //对于现在的手机市场，几乎所有的Android手机都是双卡了，对于双卡手机肯定是不止有一个IMEI值。
            //全网通手机会有两个IMEI和一个MEID，不支持电信的双卡手机有两个IMEI，那又该怎么获取呢？
//            Method method = tm.getClass().getMethod("getDeviceId", int.class);
//            String imei1 = tm.getDeviceId();
//            String imei2 = (String) method.invoke(tm, PHONE_TYPE_GSM);
//            String meid = (String) method.invoke(tm, PHONE_TYPE_CDMA);
//            LogUtils.i("getDeviceId = " + imei1);
//            LogUtils.i("getDeviceId(PHONE_TYPE_GSM) = " + imei2);
//            LogUtils.i("getDeviceId(PHONE_TYPE_CDMA) = " + meid);

            if (tm.getDeviceId() != null) {
                result = tm.getDeviceId();
            } else {
                result = getAndroidId();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 说明：获取手机IMSI号码(国际移动用户识别码)
     * 区别移动用户的标志，储存在SIM卡中，可用于区别移动用户的有效信息。其总长度不超过15位，同样使用0～9的数字。
     * 其中MCC是移动用户所属国家代号，占3位数字，中国的MCC规定为460；MNC是移动网号码，最多由两位数字组成，用于识别移动用户所归属的移动通信网；
     * MSIN是移动用户识别码，用以识别某一移动通信网中的移动用户。
     *
     * @return 返回手机IMSI号码
     */
    public static String getImsiCode() {
        String result = "";
        try {
            if (ActivityCompat.checkSelfPermission(FastFrame.getApplication(), "android.permission.READ_PHONE_STATE") != 0) {
                return "";
            }
            final TelephonyManager tm = (TelephonyManager) FastFrame.getApplication()
                    .getSystemService(Context.TELEPHONY_SERVICE);
            if (tm != null) {
                result = tm.getSubscriberId();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
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
     * 说明：获取本机手机号码
     *
     * @return 返回本机手机号码
     */
    public static String getMobilNumber() {
        String result = "";
        try {
            final TelephonyManager tm = (TelephonyManager) FastFrame.getApplication()
                    .getSystemService(Context.TELEPHONY_SERVICE);
            result = tm.getLine1Number();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 说明：myPid
     *
     * @return
     */
    public static int myPid() {
        return android.os.Process.myPid();
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
     * 说明：获取手机MAC地址
     *
     * @return 返回手机MAC地址
     */
//    public static String getMacAddress() {
//        String res = "";
//        try {
//            final WifiManager wifiManager = (WifiManager) FastFrame.getApplication()
//                    .getSystemService(Context.WIFI_SERVICE);
//            final WifiInfo info = wifiManager.getConnectionInfo();
//            if (null != info) {
//                res = info.getMacAddress();
//            }
//            if (TextUtils.isEmpty(res)) {
//                res = "00:00:00:00:00:00";
//            }
//        } catch (Exception e) {
//            res = "";
//        }
//        return res;
//    }

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
     * 说明：获取当前应用程序的VersionCode
     *
     * @return 返回当前应用的版本号
     */
    public static int versionCode() {
        try {
            PackageInfo info = FastFrame.getApplication().getPackageManager().getPackageInfo(
                    FastFrame.getApplication().getPackageName(), 0);
            return info.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 说明：检测手机空间可用大小 get the space is left over on phone self
     */
    public static long getRealSizeOnPhone() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        @SuppressWarnings("deprecation")
        long blockSize = stat.getBlockSize();
        @SuppressWarnings("deprecation")
        long availableBlocks = stat.getAvailableBlocks();
        long realSize = blockSize * availableBlocks;
        return realSize;
    }

    /**
     * 说明：获取设备终端码
     *
     * @return
     */
    public static String getTerminalCode() {
        String result = "";
        try {
            String imeiCode = getImeiCode();
            String androidId = getAndroidId();
            result = MD5.getMD5(imeiCode + androidId);
            if (FastFrame.isDebug){
                Log.i("AndroidId:", imeiCode);
                Log.i("ImeiCode:", androidId);
                Log.i("MD5(IMEI+AndroidId):", result);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取手机系统SDK版本
     *
     * @return 如API 17 则返回 17
     */
    public static int getSDKVersion() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 获取设备的可用内存大小
     *
     * @return 当前内存大小
     */
    public static int getDeviceUsableMemory() {
        ActivityManager am = (ActivityManager) FastFrame.getApplication()
                .getSystemService(Context.ACTIVITY_SERVICE);
        MemoryInfo mi = new MemoryInfo();
        am.getMemoryInfo(mi);
        // 返回当前系统的可用内存
        return (int) (mi.availMem / (1024 * 1024));
    }

    /**
     * 说明：获取当前线程名称
     *
     * @return
     */
    public static String getCurProcessName() {
        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) FastFrame.getApplication()
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    private boolean isMainProcess() {
        int pid = android.os.Process.myPid();
        String processNameString = "";
        ActivityManager m = (ActivityManager) FastFrame.getApplication().getSystemService(
                Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess :
                m.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                processNameString = appProcess.processName;
            }
        }
        return TextUtils.equals(BuildConfig.APPLICATION_ID, processNameString);
    }

}
