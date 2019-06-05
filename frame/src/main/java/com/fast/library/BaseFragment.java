package com.fast.library;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fast.library.http.HttpTaskKey;
import com.fast.library.ui.SupportFragment;
import com.fast.library.ui.ToastUtils;

/**
 * 说明：Fragment基类(V4)
 */
public abstract class BaseFragment extends SupportFragment implements HttpTaskKey {

    private FragmentActivity mFragmentActivity;
    private int containerId;

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        mFragmentActivity = getActivity();
        return inflater.inflate(getRootViewResID(),null);
    }

    @Override
    public String getHttpTaskKey() {
        return getClass().getSimpleName();
    }

    /***************************************************************************************/

    public void shortToast(int res){
        ToastUtils.get().shortToast(res);
    }
    public void shortToast(String res){
        ToastUtils.get().shortToast(res);
    }
    public void longToast(String res){
        ToastUtils.get().longToast(res);
    }
    public void longToast(int res){
        ToastUtils.get().longToast(res);
    }
    public void cancelToast(){
        ToastUtils.get().cancelToast();
    }

    /***************************************************************************************/

    public void setContainerId(int containerId) {
        this.containerId = containerId;
    }

    public int getContainerId() {
        return containerId;
    }

    /***************************************************************************************/

    public void skipActivity(Class<?> cls) {
        skipActivity(cls);
        mFragmentActivity.finish();
    }

    public void skipActivity(Intent intent) {
        skipActivity(intent);
        mFragmentActivity.finish();
    }

    public void skipActivity(Class<?> cls, Bundle bundle) {
        skipActivity(cls, bundle);
        mFragmentActivity.finish();
    }

    public void showActivity(Class<?> cls) {
        Intent intent = new Intent(mFragmentActivity,cls);
        mFragmentActivity.startActivity(intent);
    }

    public void showActivity(Intent intent) {
        mFragmentActivity.startActivity(intent);
    }

    public void showActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(mFragmentActivity,cls);
        intent.putExtras(bundle);
        mFragmentActivity.startActivity(intent);
    }

    /***************************************************************************************/

    /***************************************************************************************/

    public FragmentManager getSupportFragmentManager() {
        if (mFragmentActivity != null){
            return mFragmentActivity.getSupportFragmentManager();
        }else {
            return null;
        }
    }

    /***************************************************************************************/
}
