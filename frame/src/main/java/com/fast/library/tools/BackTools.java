package com.fast.library.tools;

/**
 * 说明：双击退出应用（在Activity的onBackPressed方法中调用该方法的onBackPressed）
 */
public class BackTools {

    private static long touchTime = 0;

    public static void onBackPressed(BackExit backExit){
        long waitTime = backExit.setWaitTime();
        if (waitTime <= 0){
            waitTime = 2000;
        }
        long currentTime = System.currentTimeMillis();
        if ((currentTime - touchTime) >= waitTime){
            backExit.showTips();
            touchTime = currentTime;
        }else {
            //退出
            backExit.exit();
        }
    }

}
