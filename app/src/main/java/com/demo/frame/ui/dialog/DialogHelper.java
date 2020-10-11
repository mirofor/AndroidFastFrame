package com.demo.frame.ui.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.fast.library.utils.StringUtils;

public class DialogHelper {

    public static class Builder {
        public final Context mContext;

        public Builder(Context context) {
            this.mContext = context;
        }

        public String confirmText;
        public String cancelText;
        public boolean isCancelable = true;
        public DialogInterface.OnClickListener confirmListener;
        public DialogInterface.OnClickListener cancelListener;
        public String title = "提示";
        public String message;
    }

    public static void showDialog(Builder builder) {
        if (builder != null && builder.mContext != null) {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(builder.mContext);
            alertBuilder.setTitle(builder.title);
            if (StringUtils.isNotEmpty(builder.message)) {
                alertBuilder.setMessage(builder.message);
            }
            if (StringUtils.isNotEmpty(builder.confirmText)) {
                alertBuilder.setPositiveButton(builder.confirmText, builder.confirmListener);
            }
            if (StringUtils.isNotEmpty(builder.cancelText)) {
                alertBuilder.setNegativeButton(builder.cancelText, builder.cancelListener);
            }
            alertBuilder.setCancelable(builder.isCancelable);
            alertBuilder.create().show();
        }
    }

}
