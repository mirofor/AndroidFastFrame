package com.fast.library;

import android.content.Intent;
import android.view.View;

import com.fast.library.http.HttpTaskKey;
import com.fast.library.ui.AbstractActivity;
import com.fast.library.utils.ToastUtils;
import com.fast.mvp.presenter.MvpPresenter;

import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;


/**
 * 说明：Activity基类
 * @author xiaomi
 */
public abstract class BaseActivity<Presenter extends MvpPresenter> extends AbstractActivity implements HttpTaskKey, LoaderManager.LoaderCallbacks<Presenter> {

    private Presenter mPresenter;

    protected final String HTTP_TASK_KEY = "key_" + hashCode();

    @Override
    public String getHttpTaskKey() {
        return HTTP_TASK_KEY;
    }

    @Override
    public void getIntentData(Intent intent) {
    }

    /***************************************************************************************/

    public void shortToast(int res) {
        ToastUtils.get().shortToast(res);
    }

    public void shortToast(String res) {
        ToastUtils.get().shortToast(res);
    }

    public void longToast(String res) {
        ToastUtils.get().longToast(res);
    }

    public void longToast(int res) {
        ToastUtils.get().longToast(res);
    }

    public void cancelToast() {
        ToastUtils.get().cancelToast();
    }

    /***************************************************************************************/

    @Override
    public void clickView(View v, int id) {

    }

    /***************************************************************************************/

    public abstract int createLoaderID();

    public Presenter getPresenter() {
        return mPresenter;
    }


    @Override
    public void onLoadFinished(Loader<Presenter> loader, Presenter data) {
        mPresenter = data;
    }


    @Override
    public void onLoaderReset(Loader<Presenter> loader) {
        mPresenter = null;
    }
    /***************************************************************************************/

}
