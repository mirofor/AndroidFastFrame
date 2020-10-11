package com.fast.library.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.fast.library.ui.ToastUtils;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * 说明：BaseDialogFragment
 */
public abstract class BaseDialogFragment extends AppCompatDialogFragment {

    private OnDismissListener onDismissListener;
    private AppCompatDialog appCompatDialog;
    private Bundle data;

    public interface OnDismissListener {
        void onDismiss();
    }

    public void setOnDismissListener(OnDismissListener listener){
        this.onDismissListener = listener;
    }

    public void show(FragmentManager manager){
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(this, getClass().getSimpleName());
        transaction.commitAllowingStateLoss();
    }

    public void show(FragmentManager manager, Bundle data){
        this.data = data;
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(this, getClass().getSimpleName());
        transaction.commitAllowingStateLoss();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        appCompatDialog = new AppCompatDialog(getActivity(), getTheme());
        return appCompatDialog;
    }

    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        return height;
    }

    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        return width;
    }

    public void setCanceledOnTouchOutside(boolean cancel){
        if (appCompatDialog != null){
            appCompatDialog.setCanceledOnTouchOutside(cancel);
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(setDialogView(),container);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        onCreateView(view);
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        float dimAmount = setWindowDimAmount();
        if (dimAmount >= 0.0f && dimAmount <= 1.0f){
            Window window = getDialog().getWindow();
            WindowManager.LayoutParams windowParams = window.getAttributes();
            windowParams.dimAmount = dimAmount;
            window.setAttributes(windowParams);
        }
        if (isFullScreen()){
            getDialog().getWindow().setLayout(getScreenWidth(getContext()),
                    getScreenHeight(getContext()));
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (data != null && savedInstanceState != null){
            data.putAll(savedInstanceState);
        }
        onInit(data);
    }

    /**
     * 初始化
     * @param bundle
     */
    public abstract void onInit(Bundle bundle);

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (onDismissListener != null){
            onDismissListener.onDismiss();
        }
    }

    @Override
    public void onDestroyView() {
        onDestroyView(getView());
        super.onDestroyView();
    }

    public abstract int setDialogView();
    public abstract void onCreateView(View view);
    public abstract void onDestroyView(View view);

    /**
     * 设置window背景透明度
     * @return
     */
    public float setWindowDimAmount(){
        return -1;
    }

    public boolean isFullScreen(){
        return false;
    }

    public void toast(String msg){
        ToastUtils.get().shortToast(msg);
    }

    public void toast(@StringRes int msg){
        ToastUtils.get().shortToast(msg);
    }
}
