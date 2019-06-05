package com.fast.library.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import com.fast.library.FastFrame;

import java.util.ArrayList;

/**
 * 说明：网络工具类
 */
public final class NetUtils {

    private static ArrayList<OnNetWorkChangedListener> sNetWrokChangedListener;
    private static ConnectionChangedReceiver sConnectionChangedReceiver;
    public interface OnNetWorkChangedListener{
        void onNetChanged(boolean isConnect, NetWorkType type);
    }

    public static class ConnectionChangedReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction()) && sNetWrokChangedListener != null){
                for (OnNetWorkChangedListener listener:sNetWrokChangedListener){
                    listener.onNetChanged(isNetConnected(),getNetWorkType());
                }
            }
        }
    }

    /******************************* 网络类型 ****************************************/

    // 网络类型
    public enum  NetWorkType{
        NETTYPE_NONET,NETTYPE_WIFI,NETTYPE_2G,NETTYPE_3G,NETTYPE_4G
    }

    private NetUtils(){}

    /**
     * 说明：获取手机网络状态是否可用
     *
     * @return 返回网络状态【true:网络联通】【false:网络断开】
     */
    public static boolean isNetConnected() {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) FastFrame.getApplication()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            @SuppressLint("MissingPermission") NetworkInfo network = connectivityManager.getActiveNetworkInfo();
            if (connectivityManager != null) {
                if (network != null && network.isConnected()) {
                    if (network.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 说明：获取当前网络类型
     *      字段常量[NetWorkType.NETTYPE_WIFI]
     * @return 0：没有网络 1：WIFI网络 2：2G网络 3：3G网络 4:4G网络
     *         int NETTYPE_NONET = 0;
     *         int NETTYPE_WIFI = 1;
     *         int NETTYPE_2G = 2;
     *         int NETTYPE_3G = 3;
     *         int NETTYPE_4G = 4;
     */
    public static NetWorkType getNetWorkType() {
        NetWorkType strNetworkType = NetWorkType.NETTYPE_NONET;
        @SuppressLint("MissingPermission") NetworkInfo networkInfo = ((ConnectivityManager) FastFrame.getApplication().getSystemService(Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                strNetworkType = NetWorkType.NETTYPE_WIFI;
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                String _strSubTypeName = networkInfo.getSubtypeName();
                int networkType = networkInfo.getSubtype();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN:
                        strNetworkType = NetWorkType.NETTYPE_2G;
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    case TelephonyManager.NETWORK_TYPE_EHRPD:
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                        strNetworkType = NetWorkType.NETTYPE_3G;
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE:
                        strNetworkType = NetWorkType.NETTYPE_4G;
                        break;
                    default:
                        if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA")
                                || _strSubTypeName.equalsIgnoreCase("WCDMA")
                                || _strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                            strNetworkType = NetWorkType.NETTYPE_3G;
                        } else {
                            strNetworkType = NetWorkType.NETTYPE_NONET;
                        }
                        break;
                }
            }
        }
        return strNetworkType;
    }

    /**
     * 说明：判断当前网络是否是wifi
     * @return
     */
    public static boolean isWifi(){
        return getNetWorkType() == NetWorkType.NETTYPE_WIFI;
    }

    /**
     * 说明：判断当前网络是否是2G
     * @return
     */
    public static boolean is2G(){
        return getNetWorkType() == NetWorkType.NETTYPE_2G;
    }

    /**
     * 说明：判断当前网络是否是3G
     * @return
     */
    public static boolean is3G(){
        return getNetWorkType() == NetWorkType.NETTYPE_3G;
    }

    /**
     * 说明：判断当前网络是否是4G
     * @return
     */
    public static boolean is4G(){
        return getNetWorkType() == NetWorkType.NETTYPE_4G;
    }

    /**
     * 说明：判断当前网络是否是3G或是4G
     * @return
     */
    public static boolean is3Gor4G(){
        return is3G() || is4G();
    }

    public static void initNetworkChanggedListener(@Nullable Activity activity){
        if (sConnectionChangedReceiver == null){
            sConnectionChangedReceiver = new ConnectionChangedReceiver();
            IntentFilter filter=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            activity.registerReceiver(sConnectionChangedReceiver, filter);
        }
    }

    public static void clearNetworkChangedListener(@Nullable Activity activity){
        if (sConnectionChangedReceiver != null){
            activity.unregisterReceiver(sConnectionChangedReceiver);
            sConnectionChangedReceiver = null;
        }
    }

    /**
     * 说明：注册网络监听
     * @param listener
     */
    public static void registerNetworkChangedListener(OnNetWorkChangedListener listener){
        if (listener != null){
            if (sNetWrokChangedListener == null){
                sNetWrokChangedListener = new ArrayList<>();
            }
            sNetWrokChangedListener.add(listener);
        }
    }

    /**
     * 说明：取消注册网络监听
     * @param listener
     */
    public static void unRegisterNetworkChangedListener(OnNetWorkChangedListener listener){
        if (listener != null && sNetWrokChangedListener != null){
            sNetWrokChangedListener.remove(listener);
        }
    }
}
