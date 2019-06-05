package com.fast.library.utils;

import android.os.SystemClock;

/**
 * 说明：服务器时间
 */
public class ServerTimeUtils {

    private static ServerTimeUtils serverTimeUtils = new ServerTimeUtils();

    private boolean isInit = false;
    private SPUtils spUtils = SPUtils.getInstance("serverTime");
    private static String KEY_CURRENT_TIME = "KEY_CURRENT_TIME";
    private static String KEY_FIRST_BOOT_TIME = "KEY_FIRST_BOOT_TIME";

    private ServerTimeUtils(){}

    public static ServerTimeUtils getInstance(){
        return serverTimeUtils;
    }

    public void init(String serverTime, String format){
        long currentTime = DateUtils.getStrToLong(serverTime,format);
        long firstBootTime = SystemClock.elapsedRealtime();
        spUtils.write(KEY_CURRENT_TIME,currentTime);
        spUtils.write(KEY_FIRST_BOOT_TIME,firstBootTime);
        isInit = true;
    }

    /**
     * 获取服务器时间
     * @param format
     * @return
     */
    public String getServerTime(String format){
        String time = "";
        try {
            time =  DateUtils.getLongToStr(getMillSeconds(),format);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (StringUtils.isEmpty(time)){
                time = DateUtils.getNowTime(format);
            }
        }
        return time;
    }

    public long getMillSeconds(){
        if (isInit){
            long currentTime = spUtils.readLong(KEY_CURRENT_TIME);
            long first = spUtils.readLong(KEY_FIRST_BOOT_TIME);
            long second = SystemClock.elapsedRealtime();
            return currentTime + (second - first);
        }else {
            return DateUtils.getMillisecond();
        }
    }

}
