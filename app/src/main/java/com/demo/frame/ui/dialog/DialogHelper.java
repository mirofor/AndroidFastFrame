package com.demo.frame.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import com.fast.library.utils.StringUtils;

import androidx.appcompat.app.AlertDialog;

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

    /**
     * 选择图片
     *
     * @param context
     * @param takeListener
     * @param selectedListener
     * @return
     */
    public static DialogSelected uploadPic(Context context, final View.OnClickListener takeListener,
                                           final View.OnClickListener selectedListener) {
        DialogSelected.SelectBuilder selectBuilder = new DialogSelected.SelectBuilder(context);
        DialogSelected.SelectSpan takeGallerySpan = new DialogSelected.SelectSpan();
        takeGallerySpan.content = "从相册中选择";
        takeGallerySpan.listener = new DialogSelected.SpanClickListener() {
            @Override
            public void onClick(View view, Dialog dialog) {
                if (selectedListener != null) {
                    selectedListener.onClick(view);
                }
                dialog.dismiss();
            }
        };
        DialogSelected.SelectSpan takePhotoSpan = new DialogSelected.SelectSpan();
        takePhotoSpan.content = "拍照";
        takePhotoSpan.listener = new DialogSelected.SpanClickListener() {
            @Override
            public void onClick(View view, Dialog dialog) {
                if (takeListener != null) {
                    takeListener.onClick(view);
                }
                dialog.dismiss();
            }
        };
        selectBuilder.setTitleText("上传图片")
                .addSelectSpan(takePhotoSpan, takeGallerySpan);
        return selectBuilder.build();
    }

    public static DialogSelected selectSignViewType(Context context, String itemText1, final View.OnClickListener takeListener,
                                                    String itemText2, final View.OnClickListener selectedListener, String titleTip) {
        DialogSelected.SelectBuilder selectBuilder = new DialogSelected.SelectBuilder(context);

        DialogSelected.SelectSpan takePhotoSpan = new DialogSelected.SelectSpan();
        takePhotoSpan.content = itemText1;
        takePhotoSpan.listener = (view, dialog) -> {
            if (takeListener != null) {
                takeListener.onClick(view);
            }
            dialog.dismiss();
        };

        DialogSelected.SelectSpan takeGallerySpan = new DialogSelected.SelectSpan();
        takeGallerySpan.content = itemText2;
        takeGallerySpan.listener = (view, dialog) -> {
            if (selectedListener != null) {
                selectedListener.onClick(view);
            }
            dialog.dismiss();
        };
        selectBuilder.setTitleText(titleTip)
                .addSelectSpan(takePhotoSpan, takeGallerySpan);
        return selectBuilder.build();
    }
}
