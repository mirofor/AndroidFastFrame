package com.fast.library.Adapter.refresh.tips;

import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.fast.library.Adapter.refresh.RecyclerFragment;
import com.fast.library.Adapter.refresh.layout.RecyclerRefreshLayout;
import com.fast.library.R;
import com.fast.library.utils.UIUtils;

public class DefaultTipsHelper implements TipsHelper {

    protected final RecyclerFragment mFragment;
    protected final RecyclerView mRecyclerView;
    protected final RecyclerRefreshLayout mRefreshLayout;
    protected final ImageView mLoadingView;

    public DefaultTipsHelper(RecyclerFragment fragment) {
        mFragment = fragment;
        mRecyclerView = fragment.getRecyclerView();
        mRefreshLayout = fragment.getRecyclerRefreshLayout();

        mLoadingView = new ImageView(fragment.getActivity());
        mLoadingView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        mLoadingView.setImageResource(R.drawable.fast_frame_spinner);
        mLoadingView.setPadding(0, UIUtils.dip2px(10),
                0, UIUtils.dip2px(10));
        mLoadingView.setLayoutParams(new RecyclerView.LayoutParams(
                RecyclerRefreshLayout.LayoutParams.MATCH_PARENT,
                UIUtils.dip2px(40)));
    }

    @Override
    public void showEmpty() {
        hideLoading();
        TipsUtils.showTips(mRecyclerView, TipsType.EMPTY);
    }

    @Override
    public void hideEmpty() {
        TipsUtils.hideTips(mRecyclerView, TipsType.EMPTY);
    }

    @Override
    public void showLoading(boolean firstPage) {
        hideEmpty();
        hideError();
//        if (firstPage) {
//            View tipsView = TipsUtils.showTips(mRecyclerView, TipsType.LOADING);
//            AnimationDrawable drawable = (AnimationDrawable) ((ImageView) tipsView).getDrawable();
//            drawable.start();
//            return;
//        }

//        maybe you like this mode
        if (firstPage){
            mRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mRefreshLayout.setRefreshing(true);
                }
            });
        }
    }

    @Override
    public void hideLoading() {
        TipsUtils.hideTips(mRecyclerView, TipsType.LOADING);
    }

    @Override
    public void showError(boolean firstPage, Throwable error) {
        String errorMessage = error.getMessage();
        if (firstPage) {
            View tipsView = TipsUtils.showTips(mRecyclerView, TipsType.LOADING_FAILED);
            tipsView.findViewById(R.id.retry_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFragment.refresh();
                }
            });
            if (!TextUtils.isEmpty(errorMessage)) {
                ((TextView) tipsView.findViewById(R.id.description)).setText(errorMessage);
            }
            return;
        }

        Toast.makeText(mLoadingView.getContext(), errorMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void hideError() {
        TipsUtils.hideTips(mRecyclerView, TipsType.LOADING_FAILED);
    }

    @Override
    public void showHasMore() {
        if (!mFragment.getHeaderAdapter().containsFooterView(mLoadingView)) {
            ((AnimationDrawable) mLoadingView.getDrawable()).start();
            mFragment.getHeaderAdapter().addFooterView(mLoadingView);
        }
    }

    @Override
    public void hideHasMore() {
        mFragment.getHeaderAdapter().removeFooterView(mLoadingView);
    }
}
