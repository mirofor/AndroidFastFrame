package com.demo.frame.ui.presenter;

import com.fast.frame.event.EventUtils;
import com.vondear.rxtool.view.RxToast;

/**
 * 扫描车辆编号进行绑定
 */
public class SmartBindBikeScanPresenter extends ScanPresenter {

    /**
     * 车牌号
     */
    private String mStrBikeNum = "";

    @Override
    public void onScanResult(String text) {
        mStrBikeNum = text;
        RxToast.success("扫码结果：" + text);
        EventUtils.postData(type, mStrBikeNum);
        getMvpView().getActivity().finish();
    }

}
