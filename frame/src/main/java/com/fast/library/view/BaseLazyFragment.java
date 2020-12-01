package com.fast.library.view;

import android.os.Bundle;
import android.view.View;

import com.fast.library.BaseFragment;

/**
 * 说明：懒加载Fragment
 *
 * @author xiaomi
 */
public abstract class BaseLazyFragment extends BaseFragment {
    /**
     * 是否创建好视图
     */
    private boolean isPrepared = false;

    @Override
    protected void onInit(Bundle savedInstanceState, View view) {
        onInitCreate(savedInstanceState, view);
        initPrepare();
    }

    private synchronized void initPrepare() {
        if (!isPrepared) {
            isPrepared = true;
            onFirstUserVisible();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isPrepared) {
            if (isVisibleToUser) {
                onUserVisible();
            } else {
                onUserInvisible();
            }
        }
    }

    /**
     * 创建View时调用
     *
     * @param savedInstanceState Bundle
     * @param view               view
     */
    protected abstract void onInitCreate(Bundle savedInstanceState, View view);

    /**
     * 说明：第一次界面可见调用
     */
    protected abstract void onFirstUserVisible();

    /**
     * 说明：界面可见调用
     */
    protected abstract void onUserVisible();

    /**
     * 说明：界面不可见调用
     */
    protected abstract void onUserInvisible();
}
