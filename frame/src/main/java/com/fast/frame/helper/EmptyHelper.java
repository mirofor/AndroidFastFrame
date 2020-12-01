package com.fast.frame.helper;

import android.view.View;
import android.view.ViewGroup;

/**
 * @author xiaomi
 */
public interface EmptyHelper {

    void init(ViewGroup group);

    /**
     * 暂无数据
     * @param empty
     * @param listener
     */
    void showEmpty(String empty, View.OnClickListener listener);

    /**
     * 加载中
     */
    void loading();

    /**
     * 加载失败
     * @param error
     * @param listener
     */
    void showError(String error, View.OnClickListener listener);

    /**
     * 加载成功
     */
    void showSuccess();
}
