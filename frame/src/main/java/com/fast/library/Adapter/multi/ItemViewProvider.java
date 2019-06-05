package com.fast.library.Adapter.multi;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.HashMap;

/**
 * 说明：ItemViewProvider
 */
public abstract class ItemViewProvider<C extends Item> {

    protected HashMap<String,MultiItemView<C>> multiItemViews;

    public interface OnItemClickListener<T extends Item>{
        void onItemClick(int position, T item);
    }

    public interface OnItemLongClickListener<T extends Item>{
        void onItemLongClick(int position, T item);
    }
    
    public OnItemClickListener mItemClickListener;
    public OnItemLongClickListener mItemLongClickListener;

    @NonNull
    protected MultiItemViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new MultiItemViewHolder(inflater.inflate(getItemLayoutId(),parent,false));
    }

    protected void convert(@NonNull MultiItemViewHolder holder, @NonNull C item, int position){}
    protected abstract void convert(@NonNull MultiItemViewHolder holder, @NonNull C item);

    protected abstract int getItemLayoutId();

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

    /**
     * 处理多种类型
     * @return
     */
    public boolean isMultiType(){
        return false;
    }

    /**
     * 分发处理类型
     * @param item
     * @return
     */
    protected String distributeType(C item){
        return null;
    }

    public MultiItemView<C> getMultiItemView(C item){
        MultiItemView<C> itemView;
        if (multiItemViews == null){
            new RuntimeException("你应该先调用registerItem注册！");
        }
        if (distributeType(item) == null){
            new RuntimeException("你应该重写distributeType分发类型！");
        }
        itemView = multiItemViews.get(distributeType(item));
        if (itemView == null){
            itemView = getDefultMultiItemView();
        }
        return itemView;
    }

    protected MultiItemView<C> getDefultMultiItemView(){
        return new EmptyMultiItemView();
    }
}
