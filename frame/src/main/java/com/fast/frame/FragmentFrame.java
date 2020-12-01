package com.fast.frame;

import android.os.Bundle;
import android.view.View;

import com.fast.frame.event.EventUtils;
import com.fast.frame.interrface.IFrameRegister;
import com.fast.frame.interrface.ILoadingDialog;
import com.fast.library.utils.UIUtils;
import com.fast.library.view.BaseLazyFragment;

import androidx.annotation.StringRes;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 说明：FragmentFrame
 */
public abstract class FragmentFrame extends BaseLazyFragment implements IFrameRegister {

    protected ActivityFrame mActivity;
    public ILoadingDialog mLoadingDialog;

    Unbinder unbinder;

    @Override
    protected void onUserVisible() {
    }

    @Override
    protected void onUserInvisible() {

    }

    @Override
    protected void onInitCreate(Bundle savedInstanceState, View view) {
        if (isRegisterEventBus()) {
            EventUtils.registerEventBus(this);
        }
        mActivity = (ActivityFrame) getActivity();
        if (isBindButterKnife()) {
            unbinder = ButterKnife.bind(this, view);
        }
        //初始化沉浸式
        if (isImmersionBarEnabled()) {
            mActivity.initImmersionBar();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (isRegisterEventBus()) {
            EventUtils.unRegisterEventBus(this);
        }
        if (isBindButterKnife()) {
            if (unbinder != null) {
                unbinder.unbind();
            }

        }
    }

    @Override
    public boolean isRegisterEventBus() {
        return false;
    }

    @Override
    public boolean isBindButterKnife() {
        return true;
    }

    /**
     * 是否可以使用沉浸式
     * Is immersion bar enabled boolean.
     *
     * @return the boolean
     */
    public boolean isImmersionBarEnabled() {
        return true;
    }

    final protected ActivityFrame activity() {
        return mActivity;
    }

    /****************************** 加载框 *********************************/

    public void showLoading() {
        showLoading(null);
    }

    public void showLoading(@StringRes int text) {
        showLoading(UIUtils.getString(text));
    }

    public void showLoading(String text) {
        showLoading(text, true);
    }

    public void showLoading(String text, boolean isCancel) {
        dismissLoading();
        mLoadingDialog = setLoadingDialog();
        mLoadingDialog.setCancel(isCancel)
                .setText(text)
                .showDialog(getSupportFragmentManager());
    }

    public void dismissLoading() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismissDialog();
        }
        mLoadingDialog = null;
    }

    public ILoadingDialog setLoadingDialog() {
        return DialogFrame.loadingDialog();
    }

    /****************************** 加载框 *********************************/
}