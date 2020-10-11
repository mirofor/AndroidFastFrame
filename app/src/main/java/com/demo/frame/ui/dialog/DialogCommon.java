package com.demo.frame.ui.dialog;

import android.content.Context;

import com.demo.frame.R;
import com.fast.library.dialog.BaseDialog;
import com.fast.library.ui.ToastUtils;

import androidx.annotation.StringRes;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 说明：CommonDialog
 */
public abstract class DialogCommon extends BaseDialog {

    Unbinder unbinder;

    public DialogCommon(Context context) {
        this(context, R.style.XXDialog);
    }

    public DialogCommon(Context context, int style) {
        super(context, style);
    }

    public DialogCommon(Context context, int themeResId, int layoutId) {
        super(context, themeResId, layoutId);
    }

    @Override
    public void onInit() {
        unbinder = ButterKnife.bind(this);
        onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    public abstract void onCreate();

    public void toast(String msg) {
        ToastUtils.get().shortToast(msg);
    }

    public void toast(@StringRes int msg) {
        ToastUtils.get().shortToast(msg);
    }
}