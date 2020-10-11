package com.demo.frame.ui;

import android.os.Build;

import com.fast.frame.ActivityFrame;
import com.fast.library.HttpUtils;
import com.fast.mvp.presenter.MvpPresenter;

/**
 * 说明：ActivityBase
 */
public abstract class ActivityBase<Presenter extends MvpPresenter> extends ActivityFrame<Presenter> {
    @Override
    public int createLoaderID() {
        return hashCode();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        HttpUtils.cancelKey(getHttpTaskKey());
    }

//    @Override
//    public void onCustomToolBar(Toolbar toolbar) {
//        super.onCustomToolBar(toolbar);
//        toolbar.setNavigationIcon(R.drawable.lefta);
//}

    @Override
    public String getHttpTaskKey() {
        return getClass().getSimpleName();
    }

    @Override
    protected boolean isImmersionBarEnabled() {
        return Build.VERSION.SDK_INT == 24;
    }
}
