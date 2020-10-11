package com.fast.library.Adapter.multi;

import android.support.annotation.NonNull;

import com.fast.library.utils.StringUtils;

import java.util.HashMap;

/**
 * 说明：MultiItemViewProvider
 */
public abstract class MultiItemViewProvider<C extends Item> extends ItemViewProvider<C> {
    @Override
    protected void convert(@NonNull MultiItemViewHolder holder, @NonNull C item, int position) {

    }

    @Override
    protected int getItemLayoutId() {
        return 0;
    }

    @Override
    public boolean isMultiType() {
        return true;
    }

    public MultiItemViewProvider<C> registerItem(MultiItemView<C> itemView){
        if (itemView != null && StringUtils.isNotEmpty(itemView.getItemType())){
            if (multiItemViews == null){
                multiItemViews = new HashMap<>();
            }
            multiItemViews.put(itemView.getItemType(),itemView);
        }
        return this;
    }
}