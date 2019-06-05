package com.demo.frame.utils;

import com.fast.library.utils.DateUtils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ComStringUtils {
    public static String getFriendlyLength(float lenMeter) {

        if (lenMeter > 1000) {
            float dis = (float) lenMeter / 1000;
            DecimalFormat fnum = new DecimalFormat("##0.0");
            String dstr = fnum.format(dis);
            return dstr + ChString.Kilometer;
        }

        return lenMeter + ChString.Meter;
    }

    public static String getFriendlyTime(int second) {
        if (second > 3600) {
            int hour = second / 3600;
            int miniate = (second % 3600) / 60;
            return hour + "小时" + miniate + "分钟";
        }
        if (second >= 60) {
            int miniate = second / 60;
            return miniate + "分钟";
        }
        return second + "秒";
    }

    public static String formatWeatherTime(String oriTime) {
//        String dateStr = "2018-10-25T11:00:00+08:00";
        SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+SSS", Locale.ENGLISH);
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = null;
        try {
            date1 = dff.parse(oriTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String str1 = df1.format(date1);
        return str1;
    }

    /**
     * 电压
     */
    public static String parseVoltage(long voltage) {
        String result;

        if (voltage == 0) {
            return "0";
        }
        float res = (float) voltage / 1000;
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        result = decimalFormat.format(res);
        return result;
    }

    public static boolean parseValStatus(int value) {
        if (value == 0) {
            return false;
        }
        return true;
    }

    public static String parseValTime(long time) {
        return DateUtils.getLongToStr(time, DateUtils.FORMAT_YYYY_MM_DD_HH_MM_SS_1);
    }

    public static String parseValTimeYMD(long time) {
        return DateUtils.getLongToStr(time, DateUtils.FORMAT_YYYY_MM_DD_1);
    }

    public static String parseWarnType(int type) {
        switch (type) {
            case 1:
                return "开锁";
            case 2:
                return "关锁";
            case 3:
                return "设防位移";
            case 4:
                return "设防震动";
            case 5:
                return "掉电开锁";
            case 6:
                return "掉电位移";
            case 7:
                return "掉电震动";
            case 8:
                return "低电量";
            case 9:
                return "车辆倾倒";
            case 10:
                return "掉电告警";
            case 11:
                return "上电通知";
            default:
                return "";
        }
    }

    public static String fmtMicrometer(String text) {
        DecimalFormat df = null;
        if (text.indexOf(".") > 0) {
            if (text.length() - text.indexOf(".") - 1 == 0) {
                df = new DecimalFormat("###,##0.");
            } else if (text.length() - text.indexOf(".") - 1 == 1) {
                df = new DecimalFormat("###,##0.0");
            } else {
                df = new DecimalFormat("###,##0.00");
            }
        } else {
            df = new DecimalFormat("###,##0");
        }
        double number = 0.0;
        try {
            number = Double.parseDouble(text);
        } catch (Exception e) {
            number = 0.0;
        }
        return df.format(number);
    }

}
