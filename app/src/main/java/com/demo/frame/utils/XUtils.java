package com.demo.frame.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Vibrator;
import android.text.TextUtils;

import com.demo.frame.ui.dialog.DialogHelper;
import com.fast.frame.ActivityFrame;
import com.fast.library.FastFrame;
import com.fast.library.utils.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static android.content.Context.VIBRATOR_SERVICE;

public class XUtils {
    public static void vibrate(ActivityFrame activityFrame) {
        Vibrator vibrator = (Vibrator) activityFrame.getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }


    public static void showDefaultDialog(ActivityFrame activityFrame, String message) {

        showDefaultDialog(activityFrame, message, true);
    }

    public static void showDefaultDialog(ActivityFrame activityFrame, String message, boolean isCanceable, DialogInterface.OnClickListener onClickListener) {

        DialogHelper.Builder builder = new DialogHelper.Builder(activityFrame);
        builder.message = message;
        builder.confirmText = "确定";
        builder.isCancelable = isCanceable;
        if (onClickListener != null) {
            builder.confirmListener = onClickListener;
        }
        DialogHelper.showDialog(builder);
    }

    public static void showDefaultDialog(ActivityFrame activityFrame, String message, boolean isCanceable) {
        DialogHelper.Builder builder = new DialogHelper.Builder(activityFrame);
        builder.message = message;
        builder.confirmText = "确定";
        builder.isCancelable = isCanceable;
        builder.confirmListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        };
        DialogHelper.showDialog(builder);
    }

    public static String getSubArrayFirst(Object[] args) {
        String result = "";
        String[] subArray = getSubStrArray(args);
        if (subArray != null) {
            return subArray[0];
        }
        return result;
    }

    public static String[] getSubStrArray(Object[] args) {
        if (args != null && args[0] != null) {
            String ori = args[0].toString();
            String newStr = ori.replace("||", ",");
            return newStr.split(",");
        }
        return null;
    }

    public static String getSubArraySecond(Object[] args) {
        String result = "";

        String[] subArray = getSubStrArray(args);
        if (subArray != null) {
            return subArray[1];
        }
        return result;
    }

    public static boolean isEmitSuccess(Object[] args) {
        boolean isSuccess = false;
        try {
            int flag = Integer.valueOf(getSubArrayFirst(args));
            if (flag == 1) {
                isSuccess = true;
            } else {
                isSuccess = false;
            }
        } catch (Exception e) {
        }
        return isSuccess;
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    public static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    public static String getDeviceLogInfo() {
        long current = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder().append('\n');
        sb.append("CURRENT: ").append(current).append(' ').append(toDateString(current)).append('\n');
        sb.append("BOARD: ").append(Build.BOARD).append('\n');
        sb.append("BOOTLOADER: ").append(Build.BOOTLOADER).append('\n');
        sb.append("BRAND: ").append(Build.BRAND).append('\n');
        sb.append("CPU_ABI: ").append(Build.CPU_ABI).append('\n');
        sb.append("CPU_ABI2: ").append(Build.CPU_ABI2).append('\n');
        sb.append("DEVICE: ").append(Build.DEVICE).append('\n');
        sb.append("DISPLAY: ").append(Build.DISPLAY).append('\n');
        sb.append("FINGERPRINT: ").append(Build.FINGERPRINT).append('\n');
        sb.append("HARDWARE: ").append(Build.HARDWARE).append('\n');
        sb.append("HOST: ").append(Build.HOST).append('\n');
        sb.append("ID: ").append(Build.ID).append('\n');
        sb.append("MANUFACTURER: ").append(Build.MANUFACTURER).append('\n');
        sb.append("MODEL: ").append(Build.MODEL).append('\n');
        sb.append("PRODUCT: ").append(Build.PRODUCT).append('\n');
        sb.append("SERIAL: ").append(Build.SERIAL).append('\n');
        sb.append("TAGS: ").append(Build.TAGS).append('\n');
        sb.append("TYPE: ").append(Build.TYPE).append('\n');
        sb.append("USER: ").append(Build.USER).append('\n');
        sb.append("VERSION.CODENAME: ").append(Build.VERSION.CODENAME).append('\n');
        sb.append("VERSION.INCREMENTAL: ").append(Build.VERSION.INCREMENTAL).append('\n');
        sb.append("VERSION.RELEASE: ").append(Build.VERSION.RELEASE).append('\n');
        sb.append("VERSION.SDK_INT: ").append(Build.VERSION.SDK_INT).append('\n');
        sb.append("LANG: ").append(FastFrame.getApplication().getApplicationContext().getResources().getConfiguration().locale.getLanguage()).append('\n');

        return sb.toString();
    }

    private static String toDateString(long timeMilli) {
        Calendar calc = Calendar.getInstance();
        calc.setTimeInMillis(timeMilli);
        return String.format(Locale.CHINESE, "%04d.%02d.%02d %02d:%02d:%02d:%03d",
                calc.get(Calendar.YEAR), calc.get(Calendar.MONTH) + 1, calc.get(Calendar.DAY_OF_MONTH),
                calc.get(Calendar.HOUR_OF_DAY), calc.get(Calendar.MINUTE), calc.get(Calendar.SECOND), calc.get(Calendar.MILLISECOND));
    }


    public static boolean isForeground(Activity activity) {
        return isForeground(activity, activity.getClass().getName());
    }

    /**
     * 判断某个界面是否在前台
     *
     * @param context   Context
     * @param className 界面的类名
     * @return 是否在前台显示
     */
    public static boolean isForeground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className))
            return false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            if (className.equals(cpn.getClassName()))
                return true;
        }
        return false;
    }

    public static String[] getVoltageNum(String code) {
        String[] nums;

        if (StringUtils.isEmpty(code) || Double.valueOf(code) == 0.0) {
            nums = new String[2];
            nums[0] = "0";
            nums[1] = "0";
            return nums;
        }

        if (code.length() == 1) {
            nums = new String[2];
            nums[0] = "0";
            nums[1] = code;
            return nums;
        }
        if (code.length() > 3) {
            nums = new String[4];
            char[] temp = code.toCharArray();
            nums[0] = String.valueOf(temp[0]);
            nums[1] = String.valueOf(temp[1]);
            nums[2] = String.valueOf(temp[3]);
            return nums;
        }

        nums = new String[2];
        char[] temp = code.toCharArray();
        char num1 = temp[0];
        char num2 = temp[1];

        nums[0] = String.valueOf(num1);
        nums[1] = String.valueOf(num2);

        return nums;
    }


    public static File getSaveFile(Context context) {
        File file = new File(context.getFilesDir(), "pic.jpg");
        return file;
    }

}
