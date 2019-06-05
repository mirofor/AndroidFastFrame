package com.demo.frame.ui.view;

import com.fast.mvp.BaseView;

/**
 * 说明：ScanView
 */
public interface ScanView extends BaseView {
    void startScan();
    void stopScan();
    boolean isStopCar();
}
