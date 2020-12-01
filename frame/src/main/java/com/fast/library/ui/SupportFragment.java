package com.fast.library.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.fast.library.utils.LogUtils;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * 说明：SupportFragment
 *
 * @author xiaomi
 */
public abstract class SupportFragment extends Fragment implements OnClickListener {

    protected Context context;
    protected View fragmentRootView;
    private int resId = 0;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        setViewBefore(context);
    }

    protected void runOnUiThread(Runnable runnable) {
        if (context instanceof Activity) {
            ((Activity) context).runOnUiThread(runnable);
        }
    }

    protected abstract View inflaterView(LayoutInflater inflater,
                                         ViewGroup container, Bundle bundle);

    /**
     * 说明：设置布局文件
     *
     * @param resId
     */
    public final void setRootViewResID(int resId) {
        this.resId = resId;
    }

    protected int getRootViewResID() {
        return resId;
    }

    /**
     * 说明：获取数据
     *
     * @param bundle bundle
     */
    protected void getBundleData(Bundle bundle) {
    }

    /**
     * 说明：初始化数据onActivityCreated
     *
     * @param savedInstanceState bundle
     * @param view               view
     */
    protected abstract void onInit(Bundle savedInstanceState, View view);

    /**
     * 说明：初始化数据onCreateView
     *
     * @param view view
     */
    protected void onInitCreateView(View view) {
    }

    /**
     * 说明：在绑定数据之前调用
     *
     * @param context
     */
    protected void setViewBefore(Context context) {
    }

    /**
     * 说明：点击事件
     *
     * @param v  view
     * @param id id
     */
    protected void clickView(View v, int id) {
    }

    /**
     * 说明：点击事件
     *
     * @param v view
     */
    @Override
    public void onClick(View v) {
        clickView(v, v.getId());
    }

    /**
     * onCreateView
     *
     * @param inflater           LayoutInflater
     * @param container          ViewGroup Container
     * @param savedInstanceState Bundle
     * @return View
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (fragmentRootView == null) {
            String annotateError = AnnotateViewUtils.init(this);
            if (!TextUtils.isEmpty(annotateError)) {
                LogUtils.e(annotateError);
            }
            fragmentRootView = inflaterView(inflater, container, savedInstanceState);
            onInitCreateView(fragmentRootView);
        } else {
            ViewGroup parent = (ViewGroup) fragmentRootView.getParent();
            if (parent != null) {
                parent.removeView(fragmentRootView);
            }
        }
        return fragmentRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() != null) {
            getBundleData(getArguments());
        }
        onInit(savedInstanceState, fragmentRootView);
    }

    @SuppressWarnings("unchecked")
    protected <T extends View> T bind(int id) {
        return (T) fragmentRootView.findViewById(id);
    }

    @SuppressWarnings("unchecked")
    protected <T extends View> T bind(int id, boolean click) {
        T view = (T) fragmentRootView.findViewById(id);
        if (click) {
            view.setOnClickListener(this);
        }
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        LogUtils.v(this.getClass().getName(), "--->onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        LogUtils.v(this.getClass().getName(), "--->onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        LogUtils.v(this.getClass().getName(), "--->onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        LogUtils.v(this.getClass().getName(), "--->onStop");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        LogUtils.v(this.getClass().getName(), "--->onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        fragmentRootView = null;
        LogUtils.v(this.getClass().getName(), "--->onDestroyView");
        super.onDestroyView();
    }
}

