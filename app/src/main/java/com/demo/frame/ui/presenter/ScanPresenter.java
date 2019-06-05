package com.demo.frame.ui.presenter;

import com.demo.frame.ui.view.ScanView;
import com.fast.mvp.BasePresenter;

public abstract class ScanPresenter extends BasePresenter<ScanView>{

    public String type="";

    public void setType(String type){
        this.type = type;
    }

    public abstract void onScanResult(String text);

}
