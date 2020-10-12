package com.demo.frame.ui.activity;

import com.demo.frame.R;
import com.demo.frame.ui.ActivityCommon;
import com.fast.library.ui.ContentView;
import com.fast.library.ui.ToastUtils;

/**
 * @author xiaomi
 */
@ContentView(R.layout.activity_bao_home)
public class ActivityHome extends ActivityCommon {

    @Override
    protected boolean isImmersionBarEnabled() {
        return true;
    }

    @Override
    public boolean isShowTitleBar() {
        return true;
    }

    @Override
    public boolean isCustomCancelAction() {
        return true;
    }

    @Override
    public boolean isShowTitleBarBack() {
        return true;
    }

    @Override
    public String bindTitleBarText() {
        return "首页";
    }

    @Override
    public void onCancelButtonListener() {
        ToastUtils.get().shortToast("返回");
    }


}
