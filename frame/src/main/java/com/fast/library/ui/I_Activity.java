package com.fast.library.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * 说明：Activity接口协议
 * @author xiaomi
 */
public interface I_Activity {
    /**
     * 初始化
     */
    void onInitCreate(Bundle bundle);

    /**
     * 初始化参数(onStart中调用)
     */
    void onInitStart();

    /**
     * 点击事件回调方法
     */
    void clickView(View view, int id);

    /**
     * 获取数据
     */
    void getIntentData(Intent intent);
}
