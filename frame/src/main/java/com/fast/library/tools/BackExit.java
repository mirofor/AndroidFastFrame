package com.fast.library.tools;

import com.fast.library.ui.ActivityStack;

/**
 * 说明：默认退出处理
 * @author xiaomi
 */
public class BackExit {

    /**
     * 说明：退出提示
     */
    public void showTips() {
    }

    public void exit() {
        ActivityStack.create().AppExit();
    }

    public long setWaitTime() {
        return 2000;
    }
}
