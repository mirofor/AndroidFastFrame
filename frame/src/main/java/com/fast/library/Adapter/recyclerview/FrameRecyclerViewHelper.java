package com.fast.library.Adapter.recyclerview;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fast.library.Adapter.divider.GridLayoutDividerItemDecoration;

/**
 * 说明：RecyclerView帮助类，设置LayoutManager,设置Adapter
 */
public class FrameRecyclerViewHelper {

    /**
     * 设置GridLayoutManager
     * @param rvItems
     * @param adapter
     * @param spanCount
     * @param spacing 单位dp
     * @return
     */
    public static GridLayoutManager bindGridLayoutManager(RecyclerView rvItems, RecyclerView.Adapter adapter,
                                                          int spanCount,int spacing){
        GridLayoutManager manager = null;
        if (rvItems != null){
            manager = new GridLayoutManager(rvItems.getContext(),spanCount);
            if (spacing > 0){
                rvItems.addItemDecoration(new GridLayoutDividerItemDecoration(rvItems.getContext(),spanCount));
            }
            rvItems.setLayoutManager(manager);
            rvItems.setAdapter(adapter);
        }
        return manager;
    }

}
