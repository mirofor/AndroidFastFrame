package com.demo.frame.ui.dialog;

import android.content.Context;
import android.support.annotation.StringRes;
import com.demo.frame.R;
import com.fast.library.dialog.BaseDialog;
import com.fast.library.ui.ToastUtils;
import butterknife.ButterKnife;

/**
 * 说明：CommonDialog
 */
public abstract class DialogCommon extends BaseDialog {

    public DialogCommon(Context context) {
        this(context, R.style.XXDialog);
    }

    public DialogCommon(Context context, int style){
        super(context,style);
    }

    public DialogCommon(Context context, int themeResId, int layoutId) {
        super(context, themeResId,layoutId);
    }

    @Override
    public void onInit() {
        ButterKnife.bind(this);
        onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    public abstract void onCreate();

    public void toast(String msg){
        ToastUtils.get().shortToast(msg);
    }

    public void toast(@StringRes int msg){
        ToastUtils.get().shortToast(msg);
    }
}