package com.fast.library.Adapter.refresh;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.fast.library.Adapter.multi.MultiTypeAdapter;
import com.fast.library.Adapter.refresh.layout.RecyclerRefreshLayout;
import com.fast.library.Adapter.refresh.tips.DefaultTipsHelper;
import com.fast.library.Adapter.refresh.tips.TipsHelper;
import com.fast.library.R;
import com.fast.library.view.BaseLazyFragment;

public abstract class RecyclerFragment extends BaseLazyFragment {

    private boolean mIsLoading;

    private RecyclerView mRecyclerView;
    private RecyclerRefreshLayout mRecyclerRefreshLayout;

    private TipsHelper mTipsHelper;
    private HeaderViewRecyclerAdapter mHeaderAdapter;
    private MultiTypeAdapter mOriginAdapter;

    private InteractionListener mInteractionListener;

    private final RefreshEventDetector mRefreshEventDetector = new RefreshEventDetector();
    private final AutoLoadEventDetector mAutoLoadEventDetector = new AutoLoadEventDetector();

    @Override
    protected int getRootViewResID() {
        return R.layout.fast_frame_base_refresh_recycler_list_layout;
    }

    @Override
    protected void onUserVisible() {
    }

    @Override
    protected void onUserInvisible() {

    }

    @Override
    protected void onInitCreate(Bundle savedInstanceState, View view) {
        initRecyclerView(view);
        initRecyclerRefreshLayout(view);
        mInteractionListener = createInteraction();
        mTipsHelper = createTipsHelper();
    }

    private void initRecyclerView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        mRecyclerView.addOnScrollListener(mAutoLoadEventDetector);
        RecyclerView.LayoutManager layoutManager = createLayoutManager();
        if (layoutManager != null){
            mRecyclerView.setLayoutManager(layoutManager);
        }
        mOriginAdapter = createAdapter();
        mHeaderAdapter = new HeaderViewRecyclerAdapter(mOriginAdapter);
        mRecyclerView.setAdapter(mHeaderAdapter);
        mHeaderAdapter.adjustSpanSize(mRecyclerView);
    }

    private void initRecyclerRefreshLayout(View view) {
        mRecyclerRefreshLayout = (RecyclerRefreshLayout) view.findViewById(R.id.refresh_layout);
        if (mRecyclerRefreshLayout == null) {
            return;
        }
        if (allowPullToRefresh()) {
            mRecyclerRefreshLayout.setNestedScrollingEnabled(true);
            mRecyclerRefreshLayout.setOnRefreshListener(mRefreshEventDetector);
        } else {
            mRecyclerRefreshLayout.setEnabled(false);
        }
    }

    @NonNull
    protected RecyclerView.LayoutManager createLayoutManager(){
        return new LinearLayoutManager(getActivity());
    }

    @NonNull
    public abstract MultiTypeAdapter createAdapter();

    protected TipsHelper createTipsHelper() {
        return new DefaultTipsHelper(this);
    }

    protected InteractionListener createInteraction() {
        return null;
    }

    @Override
    public void onDestroyView() {
        mRecyclerView.removeOnScrollListener(mAutoLoadEventDetector);
        super.onDestroyView();
    }

    public HeaderViewRecyclerAdapter getHeaderAdapter() {
        return mHeaderAdapter;
    }

    public abstract boolean hasMoreData();

    public MultiTypeAdapter getOriginAdapter() {
        return mOriginAdapter;
    }

    public RecyclerRefreshLayout getRecyclerRefreshLayout() {
        return mRecyclerRefreshLayout;
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public boolean allowPullToRefresh() {
        return true;
    }

    public void refresh() {
        if (isFirstPage()) {
            mTipsHelper.showLoading(true);
        } else {
            mRecyclerRefreshLayout.setRefreshing(true);
        }
        requestRefresh();
    }

    public boolean isFirstPage() {
        return mOriginAdapter.getItemCount() <= 0;
    }

    public class RefreshEventDetector implements RecyclerRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh() {
            requestRefresh();
        }
    }

    public class AutoLoadEventDetector extends RecyclerView.OnScrollListener {

        @Override
        public void onScrolled(RecyclerView view, int dx, int dy) {
            RecyclerView.LayoutManager manager = view.getLayoutManager();
            if (manager.getChildCount() > 0) {
                int count = manager.getItemCount();
                int last = ((RecyclerView.LayoutParams) manager
                        .getChildAt(manager.getChildCount() - 1).getLayoutParams()).getViewAdapterPosition();

                if (last == count - 1 && !mIsLoading && mInteractionListener != null) {
                    requestMore();
                }
            }
        }
    }

    private void requestRefresh() {
        if (mInteractionListener != null && !mIsLoading) {
            mIsLoading = true;
            mInteractionListener.requestRefresh();
        }
    }

    private void requestMore() {
        if (mInteractionListener != null && mInteractionListener.hasMore() && !mIsLoading) {
            mIsLoading = true;
            mInteractionListener.requestMore();
        }else {
            mTipsHelper.hideHasMore();
        }
    }

    public abstract class InteractionListener {
        public void requestRefresh() {
            requestComplete();

            if (mOriginAdapter.isEmpty()) {
                mTipsHelper.showEmpty();
            } else if (hasMore()) {
                mTipsHelper.showHasMore();
            } else {
                mTipsHelper.hideHasMore();
            }
        }

        public void requestMore() {
            requestComplete();
        }

        public void requestFailure() {
            requestComplete();
            mTipsHelper.showError(isFirstPage(), new Exception("网络加载错误"));
        }

        protected void requestComplete() {
            mIsLoading = false;

            if (mRecyclerRefreshLayout != null) {
                mRecyclerRefreshLayout.setRefreshing(false);
            }

            mTipsHelper.hideError();
            mTipsHelper.hideEmpty();
            mTipsHelper.hideLoading();
        }

        protected boolean hasMore() {
            return hasMoreData();
        }
    }
}
