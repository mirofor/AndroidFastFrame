package com.fast.mvp;

import com.fast.frame.ActivityFrame;
import com.fast.mvp.view.MvpView;

/**
 * 说明：BaseView
 * @author xiaomi
 */
public interface BaseView extends MvpView {
    ActivityFrame getActivity();
}
