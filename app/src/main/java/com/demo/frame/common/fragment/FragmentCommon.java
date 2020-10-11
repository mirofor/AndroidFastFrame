package com.demo.frame.common.fragment;

import com.fast.frame.FragmentFrame;
import com.fast.library.HttpUtils;

/**
 * 说明：FragmentCommon
 * @author xiaomi
 */
public abstract class FragmentCommon extends FragmentFrame {

    @Override
    public void onDestroy() {
        super.onDestroy();
        HttpUtils.cancelKey(getHttpTaskKey());
    }
}
