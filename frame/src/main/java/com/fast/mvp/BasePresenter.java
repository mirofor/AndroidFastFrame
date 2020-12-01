package com.fast.mvp;

import com.fast.frame.ActivityFrame;
import com.fast.mvp.presenter.MvpPresenter;

/**
 * 说明：BasePresenter
 * @author xiaomi
 */
public abstract class BasePresenter<T extends BaseView> implements MvpPresenter<T> {

    private T mMvpView;
    public ActivityFrame mActivityCommon;

    @Override
    public void onStart() {
        mActivityCommon = getMvpView().getActivity();
    }

    @Override
    public void attachView(T mvpView) {
        mMvpView = mvpView;
        onStart();
    }

    @Override
    public void detachView() {
        mMvpView = null;
    }

    @Override
    public T getMvpView() {
        return mMvpView;
    }

    @Override
    public boolean isViewAttached() {
        return mMvpView != null;
    }

    @Override
    public void checkViewAttached() {
        if (!isViewAttached()) {
            throw new MvpViewNotAttachedException();
        }
    }

    public static class MvpViewNotAttachedException extends RuntimeException {
        MvpViewNotAttachedException(){
            super("请先在请求Presenter数据之前调用attachView()");
        }
    }

}
