package com.fast.library.Adapter.multi;

import android.support.annotation.NonNull;

import com.fast.library.R;

/**
 * 说明：EmptyMultiItemView
 */
public class EmptyMultiItemView extends MultiItemView{
    @Override
    protected void convert(@NonNull MultiItemViewHolder holder, @NonNull Item item, int position) {

    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_empty_multi;
    }

    @Override
    protected String getItemType() {
        return "";
    }
}