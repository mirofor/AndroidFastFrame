package com.fast.library.Adapter.refresh.tips;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.fast.library.Adapter.refresh.SRecyclerFragment;
import com.fast.library.R;
import com.fast.library.utils.UIUtils;
import com.fast.library.view.CircleProgressView;

public class SDefaultTipsHelper implements TipsHelper {

    protected final SRecyclerFragment mFragment;
    protected final RecyclerView mRecyclerView;
    protected final SwipeRefreshLayout mRefreshLayout;
    protected final LinearLayout mFootLoadingView;

    public SDefaultTipsHelper(SRecyclerFragment fragment) {
        mFragment = fragment;
        mRecyclerView = fragment.getRecyclerView();
        mRefreshLayout = fragment.getRecyclerRefreshLayout();

        mFootLoadingView = (LinearLayout) UIUtils.inflate(R.layout.fast_frame_footer_loading_view);
        CircleProgressView cpv = (CircleProgressView) mFootLoadingView.findViewById(R.id.pull_to_refresh_load_progress);
        cpv.setCircleColos(R.color.loading_footer_color);
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
    }

    @Override
    public void hideError() {
        TipsUtils.hideTips(mRecyclerView, TipsType.LOADING_FAILED);
    }

    @Override
    public void showHasMore() {
        if (!mFragment.getHeaderAdapter().containsFooterView(mFootLoadingView)) {
            mFragment.getHeaderAdapter().addFooterView(mFootLoadingView);
        }
    }

    @Override
    public void hideHasMore() {
        mFragment.getHeaderAdapter().removeFooterView(mFootLoadingView);
    }
}