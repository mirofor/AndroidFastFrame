package com.demo.frame.common.fragment;

import android.view.View;

import com.demo.frame.common.ActivityBindFragment;
import com.fast.frame.interrface.IFragmentTitleBar;

import androidx.appcompat.widget.Toolbar;

/**
 * 说明：FragmentBind
 */
public abstract class FragmentBind extends FragmentCommon implements IFragmentTitleBar {

    @Override
    public void setTitleBarText(String titleText) {
        if (activity() != null && activity() instanceof ActivityBindFragment) {
            ActivityBindFragment activityBindFragment = (ActivityBindFragment) activity();
            activityBindFragment.setTitleBarText(titleText);
        }
    }

    @Override
    public int bindTitleBarTextRes() {
        return 0;
    }

    @Override
    public int bindTitleBar() {
        return 0;
    }
    @Override
    public boolean isCustomCancelAction() {
        return false;
    }

    @Override
    public void onCancelButtonListener() {
    }

    @Override
    public void onCustomToolBar(Toolbar toolbar) {

    }

    @Override
    public void onCustomTitleBar(View titlebar) {

    }

    public void finish() {
        if (activity() != null && !activity().isFinishing()) {
            activity().finish();
        }
    }

}