package com.fast.library.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.fast.library.utils.LogUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

/**
 * 说明：FrameActivity为Activity基类
 * @author xiaomi
 */
public abstract class FrameActivity extends AppCompatActivity implements OnClickListener,
        I_Broadcast, I_Activity, I_SkipActivity,I_Service{

    public static final String TAG = FrameActivity.class.getSimpleName();
    protected SupportFragment currentSupportFragment;
    private boolean isInitStart = false;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setViewBefor();
        if (getIntent() != null){
            getIntentData(getIntent());
        }
        String annotateError = AnnotateViewUtils.init(this);
        if (!TextUtils.isEmpty(annotateError)){
            LogUtils.e(TAG,annotateError);
        }else {
            onInitCreate(bundle);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!isInitStart){
            onInitStart();
            registerBroadcast();
            registerService();
            isInitStart = true;
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T bind(int id) {
        return (T) findViewById(id);
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T bind(int id, boolean click) {
        T view = (T) findViewById(id);
        if (click) {
            view.setOnClickListener(this);
        }
        return view;
    }

    @Override
    protected void onDestroy() {
        unRegisterBroadcast();
        unRegisterService();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        clickView(v,v.getId());
    }

    @Override
    public abstract void clickView(View v,int id);

    @Override
    public void registerBroadcast() {}

    @Override
    public void unRegisterBroadcast() {}

    @Override
    public void registerService() {}

    @Override
    public void unRegisterService() {}

    /**
     * 说明:设置界面之前调用
     */
    protected void setViewBefor(){}

    /**
     * 说明：用Fragment替换视图
     * @param srcView 被替换视图
     * @param targetFragment 用来替换的Fragment
     */
    public void changeFragment(int srcView, SupportFragment targetFragment) {
        if (targetFragment.equals(currentSupportFragment)) {
            return;
        }
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        if (!targetFragment.isAdded()) {
            transaction.add(srcView, targetFragment, targetFragment.getClass()
                    .getName());
        }
        if (targetFragment.isHidden()) {
            transaction.show(targetFragment);
        }
        if (currentSupportFragment != null
                && currentSupportFragment.isVisible()) {
            transaction.hide(currentSupportFragment);
        }
        currentSupportFragment = targetFragment;
        transaction.commitAllowingStateLoss();
    }

    protected void removeFromParent(View view) {
        ViewGroup viewGroup = (ViewGroup) view.getParent();
        if (viewGroup != null) {
            viewGroup.removeView(view);
        }
    }
}

