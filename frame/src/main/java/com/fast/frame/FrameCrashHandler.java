package com.fast.frame;

import com.fast.library.utils.CrashHandler;
import com.fast.library.utils.DateUtils;
import com.fast.library.utils.FrameConstant;

import java.io.File;

public class FrameCrashHandler extends CrashHandler {

    private static FrameCrashHandler crashHandler = new FrameCrashHandler();

    public static FrameCrashHandler getInstance() {
        return crashHandler;
    }

    @Override
    public void upCrashLog(File file, String error) {

    }

    @Override
    public String setFileName() {
        return "crash_" + DateUtils.getNowTime(DateUtils.FORMAT_YYYY_MM_DD_HH_MM_SS_4) + ".txt";
    }

    @Override
    public String setCrashFilePath() {
        return FrameConstant.Crash.PATH;
    }

    @Override
    public boolean isCleanHistory() {
        return true;
    }
}
