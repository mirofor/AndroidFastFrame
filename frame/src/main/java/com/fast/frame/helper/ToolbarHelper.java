package com.fast.frame.helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.fast.library.R;

import androidx.annotation.LayoutRes;
import androidx.appcompat.widget.Toolbar;

/**
 * 说明：ToolbarHelper
 * @author xiaomi
 */
public class ToolbarHelper {
    //上下文
    private Context mContext;
    //toolbar
    private Toolbar mToolbar;
    private View mToolbarView;
    //用户自定义view
    private View mUserView;
    //base view
    private LinearLayout mContentView;
    private LayoutInflater mInflater;

    public ToolbarHelper(Context context, @LayoutRes int layoutId){
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
        initContentView();
        initToolbar();
        initUserView(layoutId);
    }

    /**
     * 说明：初始化用户自定义布局
     * @param id
     */
    private void initUserView(int id){
        mUserView = mInflater.inflate(id,null);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mUserView.setLayoutParams(params);
        mContentView.addView(mUserView);
    }

    /**
     * 说明：初始化toolbar
     */
    private void initToolbar(){
        View toolbar = mInflater.inflate(R.layout.fast_frame_view_toolbar, null);
        mToolbar = (Toolbar)toolbar.findViewById(R.id.id_tool_bar);
        mToolbarView = (View)toolbar.findViewById(R.id.id_tool_bar_top);
        mContentView.addView(toolbar);
    }

    /**
     * 说明：初始化整个内容
     */
    private void initContentView(){
        mContentView = new LinearLayout(mContext);
        mContentView.setOrientation(LinearLayout.VERTICAL);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mContentView.setLayoutParams(params);
    }

    public Toolbar getToolbar(){
        return mToolbar;
    }
    public View getToolbarTopView(){
        return mToolbarView;
    }

    public View getView(){
        return mContentView;
    }
}
