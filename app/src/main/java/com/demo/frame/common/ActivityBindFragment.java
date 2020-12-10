package com.demo.frame.common;

import android.content.Intent;
import android.view.View;

import com.demo.frame.R;
import com.demo.frame.common.fragment.FragmentBind;
import com.demo.frame.common.fragment.FragmentConfig;
import com.demo.frame.ui.ActivityBase;
import com.fast.library.ui.ContentView;

import androidx.appcompat.widget.Toolbar;

/**
 * 说明：ActivityBindFragment
 */
@ContentView(R.layout.activity_bind_fragment)
public class ActivityBindFragment extends ActivityBase {

    private FragmentBind mFragment;

    @Override
    public boolean isShowTitleBar() {
        return mFragment == null ? false : mFragment.isShowTitleBar();
    }

    @Override
    public boolean isShowTitleBarBack() {
        return mFragment == null ? false : mFragment.isShowTitleBarBack();
    }

    @Override
    public void onCancelButtonListener() {
        super.onCancelButtonListener();
        if (mFragment!=null){
            mFragment.onCancelButtonListener();
        }
    }

    @Override
    public boolean isCustomCancelAction() {
        return mFragment == null ? false : mFragment.isCustomCancelAction();
    }

    @Override
    public String bindTitleBarText() {
        return mFragment == null ? "" : mFragment.bindTitleBarText();
    }

    @Override
    public int bindTitleBarTextRes() {
        return mFragment == null ? 0 : mFragment.bindTitleBarTextRes();
    }

    @Override
    public int bindTitleBar() {
        return mFragment == null ? 0 : mFragment.bindTitleBar();
    }

    @Override
    public void onCustomToolBar(Toolbar toolbar) {
        super.onCustomToolBar(toolbar);
        if (mFragment != null){
            mFragment.onCustomToolBar(toolbar);
        }
    }

    @Override
    public void onCustomTitleBar(View titlebar) {
        super.onCustomTitleBar(titlebar);
        if (mFragment != null){
            mFragment.onCustomTitleBar(titlebar);
        }
    }

    @Override
    public void getIntentData(Intent intent) {
        super.getIntentData(intent);
        mFragment = FragmentConfig.getFragment(this);
    }

    @Override
    public void onInitStart() {
        if (mFragment != null){
            changeFragment(R.id.fl_content,mFragment);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mFragment != null){
            mFragment.onActivityResult(requestCode,resultCode,data);
        }
    }

//    @NonNull
//    @Override
//    public Loader onCreateLoader(int id, @Nullable Bundle args) {
//        return null;
//    }
//
//    @Override
//    public void onLoadFinished(@NonNull Loader loader, Object data) {
//
//    }
}