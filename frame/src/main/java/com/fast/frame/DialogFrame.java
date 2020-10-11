package com.fast.frame;

import android.support.v7.app.AppCompatActivity;
import com.fast.frame.dialog.base.FrameConfirmDialog;
import com.fast.frame.dialog.base.FrameLoadingDialog;
import com.fast.frame.interrface.ILoadingDialog;
import com.fast.frame.interrface.OnConfirmListener;

/**
 * 说明：DialogFrame
 */
public class DialogFrame {

    private DialogFrame(){}

    public static ILoadingDialog loadingDialog(){
        return FrameLoadingDialog.newInstance();
    }

    public static void showConfirmDialog(AppCompatActivity activity,String message,
                                         OnConfirmListener listener){
        showConfirmDialog(activity,null,message,null,null,listener);
    }

    public static void showConfirmDialog(AppCompatActivity activity, String title,
                                         String message, String cancel, String ok,
                                         OnConfirmListener listener){
        FrameConfirmDialog dialog = new FrameConfirmDialog();
        dialog.setTitle(title).setMessage(message).setCancelText(cancel)
        .setConfirmText(ok).setOnConfirmListener(listener).setDimAmount(0.5f)
                .setMargin(60)
                .setOutCancel(false)
                .show(activity.getSupportFragmentManager());
    }
}
