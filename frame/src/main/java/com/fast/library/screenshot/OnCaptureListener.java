package com.fast.library.screenshot;

import java.io.File;

/**
 * 说明：OnCaptureListener
 */
public interface OnCaptureListener {
    void onStartCapture();//开始截图
    void onCaptureSuccesss(File file);//截图成功
    void onCaptureError();//截图错误
    void onFinishCapture();//截图完成
}
