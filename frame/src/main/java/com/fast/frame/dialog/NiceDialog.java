package com.fast.frame.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.fast.frame.interrface.OnDismissListener;

public class NiceDialog extends BaseNiceDialog {
    private ViewConvertListener convertListener;
    private OnDismissListener onDismissListener;

    public static NiceDialog init() {
        return new NiceDialog();
    }

    @Override
    public int intLayoutId() {
        return layoutId;
    }

    @Override
    public void convertView(ViewHolder holder, BaseNiceDialog dialog) {
        if (convertListener != null) {
            convertListener.convertView(holder, dialog);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDismissListener != null){
            onDismissListener.onDismiss();
        }
    }

    public NiceDialog setOnDismissListener(OnDismissListener listener) {
        this.onDismissListener = listener;
        return this;
    }

    public NiceDialog setLayoutId(@LayoutRes int layoutId) {
        this.layoutId = layoutId;
        return this;
    }

    public NiceDialog setConvertListener(ViewConvertListener convertListener) {
        this.convertListener = convertListener;
        return this;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            Parcelable serializable = savedInstanceState.getParcelable("listener");
            if (serializable != null){
                convertListener = (ViewConvertListener) serializable;
            }
        }
    }

    /**
     * 保存接口
     *
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (convertListener != null){
            outState.putParcelable("listener", convertListener);
        }
    }
}
