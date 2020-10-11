package com.fast.frame.dialog.base;

import android.view.View;

import com.fast.frame.dialog.BaseNiceDialog;
import com.fast.frame.dialog.ViewHolder;
import com.fast.frame.interrface.OnConfirmListener;
import com.fast.library.R;
import com.fast.library.utils.StringUtils;
import com.fast.library.utils.UIUtils;

/**
 * 说明：FrameConfirmDialog
 *
 * @author xiaomi
 */
public class FrameConfirmDialog extends BaseNiceDialog {

    private OnConfirmListener mConfirmListener;
    private String title, message, cancel, confirm;

    public FrameConfirmDialog setOnConfirmListener(OnConfirmListener listener) {
        this.mConfirmListener = listener;
        return this;
    }

    public FrameConfirmDialog setTitle(String title) {
        if (StringUtils.isEmpty(title)) {
            title = UIUtils.getString(R.string.def_confirm_title);
        }
        this.title = title;
        return this;
    }

    public FrameConfirmDialog setMessage(String message) {
        if (StringUtils.isEmpty(message)) {
            message = UIUtils.getString(R.string.def_confirm_message);
        }
        this.message = message;
        return this;
    }

    public FrameConfirmDialog setCancelText(String cancelText) {
        if (StringUtils.isEmpty(cancelText)) {
            cancelText = UIUtils.getString(R.string.def_confirm_cancel);
        }
        this.cancel = cancelText;
        return this;
    }

    public FrameConfirmDialog setConfirmText(String confirmText) {
        if (StringUtils.isEmpty(confirmText)) {
            confirmText = UIUtils.getString(R.string.def_confirm_ok);
        }
        this.confirm = confirmText;
        return this;
    }

    @Override
    public int intLayoutId() {
        return R.layout.fast_frame_def_confirm_layout;
    }

    @Override
    public void convertView(ViewHolder holder, BaseNiceDialog dialog) {
        holder.setText(R.id.tv_confirm_title, title);
        holder.setText(R.id.tv_confirm_message, message);
        holder.setText(R.id.tv_confirm_cancel, cancel);
        holder.setText(R.id.tv_confirm_ok, confirm);
        holder.setOnClickListener(R.id.tv_confirm_cancel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mConfirmListener != null) {
                    mConfirmListener.onCancel(FrameConfirmDialog.this);
                }
            }
        });
        holder.setOnClickListener(R.id.tv_confirm_ok, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mConfirmListener != null) {
                    mConfirmListener.onConfirm(FrameConfirmDialog.this);
                }
            }
        });
    }
}
