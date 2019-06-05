package com.fast.frame.helper;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.fast.library.R;
import com.fast.library.tools.ViewTools;
import com.fast.library.utils.UIUtils;
import com.fast.library.view.CircleProgressView;

public class DefaultEmptyHelper implements EmptyHelper {

    private View mView;
    private TextView tvState;
    private CircleProgressView cpvLoading;

    @Override
    public void init(ViewGroup group) {
        mView = UIUtils.inflate(R.layout.fast_frame_layout_error);
        tvState = ViewTools.find(mView,R.id.tv_error_state);
        cpvLoading = ViewTools.find(mView,R.id.cpv_loading);
        if (group instanceof RelativeLayout){
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mView.setLayoutParams(params);
        }else if (group instanceof LinearLayout){
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mView.setLayoutParams(params);
        }else{
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mView.setLayoutParams(params);
        }
        group.addView(mView);
        setCircleLoadingColor(cpvLoading);
    }

    public void setCircleLoadingColor(CircleProgressView cpv){}

    @Override
    public void showEmpty(String empty, View.OnClickListener listener) {
        ViewTools.VISIBLE(mView);
        ViewTools.GONE(cpvLoading);
        ViewTools.VISIBLE(tvState);
        ViewTools.setText(tvState,empty);
        tvState.setOnClickListener(listener);
    }

    @Override
    public void loading() {
        ViewTools.VISIBLE(mView);
        ViewTools.VISIBLE(cpvLoading);
        ViewTools.GONE(tvState);
    }

    @Override
    public void showError(String error, View.OnClickListener listener) {
        ViewTools.VISIBLE(mView);
        ViewTools.GONE(cpvLoading);
        ViewTools.VISIBLE(tvState);
        ViewTools.setText(tvState,error);
        tvState.setOnClickListener(listener);
    }

    @Override
    public void showSuccess() {
        ViewTools.GONE(mView);
    }
}
