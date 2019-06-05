package com.fast.library.Adapter.multi;

import android.support.annotation.NonNull;

/**
 * 说明：MultiItemView
 */
public abstract class MultiItemView<C extends Item>{

    public interface OnItemClickListener<T extends Item>{
        void onItemClick(int position, T item);
    }

    public interface OnItemLongClickListener<T extends Item>{
        void onItemLongClick(int position, T item);
    }

    public OnItemClickListener mItemClickListener;
    public OnItemLongClickListener mItemLongClickListener;

    protected abstract void convert(@NonNull MultiItemViewHolder holder, @NonNull C item, int position);
    protected abstract int getItemLayoutId();
    protected abstract String getItemType();

    /**
     * 设置点击事件
     * @param listener
     */
    public <T extends Item> void setOnItemClickListener(OnItemClickListener<T> listener){
        if (listener != null){
            mItemClickListener = listener;
        }
    }

    /**
     * 设置长点击事件
     * @param listener
     */
    public <T extends Item> void setOnItemLongClickListener(OnItemLongClickListener<T> listener){
        if (listener != null){
            mItemLongClickListener = listener;
        }
    }
}