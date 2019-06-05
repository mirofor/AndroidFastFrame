package com.fast.mvp.presenter;

import com.fast.mvp.view.MvpView;

/**
 * 说明：MvpPresenter
 */
public interface MvpPresenter<VH extends MvpView> {
    void attachView(VH mvpView);
    void onStart();
    void detachView();
    VH getMvpView();
    boolean isViewAttached();
    void checkViewAttached();
}
