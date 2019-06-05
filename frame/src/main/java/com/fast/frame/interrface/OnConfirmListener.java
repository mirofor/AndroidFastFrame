package com.fast.frame.interrface;

import com.fast.frame.dialog.BaseNiceDialog;

/**
 * 说明：
 */
public interface OnConfirmListener {
    void onCancel(BaseNiceDialog dialog);
    void onConfirm(BaseNiceDialog dialog);
}
