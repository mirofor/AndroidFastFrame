package com.fast.library.Adapter.multi;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 说明：MultiTypeAdapter
 */
public class MultiTypeAdapter<T extends Item> extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements
        FlatTypeAdapter,TypePool {

    protected final List<T> items;
    protected LayoutInflater inflater;
    protected TypePool delegate;

    public MultiTypeAdapter(@NonNull List<T> items){
        this.delegate = new MultiTypePool();
        this.items = items;
    }
    public MultiTypeAdapter(@NonNull List<T> items,TypePool pool){
        this.delegate = pool;
        this.items = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (inflater == null){
            inflater = LayoutInflater.from(parent.getContext());
        }
        final ItemViewProvider provider = getProviderByIndex(getProviderIndex(viewType));
        if(provider.isMultiType()){
            return getMultiItemHolder(provider,parent,viewType);
        }else {
            return getSingleItemHolder(provider,parent,viewType);
        }
    }

    /**
     * 1对1
     * @return
     */
    private MultiItemViewHolder getSingleItemHolder(final ItemViewProvider provider, ViewGroup parent, int viewType){
        final MultiItemViewHolder holder = provider.onCreateViewHolder(inflater,parent);
        if (provider.mItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getLayoutPosition();
                    provider.mItemClickListener.onItemClick(position,items.get(position));
                }
            });
        }
        if (provider.mItemLongClickListener != null){
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = holder.getLayoutPosition();
                    provider.mItemLongClickListener.onItemLongClick(position,items.get(position));
                    return true;
                }
            });
        }
        return holder;
    }

    /**
     * 1对多
     * @return
     */
    private MultiItemViewHolder getMultiItemHolder(final ItemViewProvider provider, ViewGroup parent, final int position){
        final T item = items.get(position);
        final MultiItemView<T> multiItemView = provider.getMultiItemView(item);
        final MultiItemViewHolder holder = new MultiItemViewHolder(inflater.inflate(multiItemView.getItemLayoutId(),parent,false));
        if (multiItemView.mItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    multiItemView.mItemClickListener.onItemClick(position,item);
                }
            });
        }
        if (multiItemView.mItemLongClickListener != null){
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    multiItemView.mItemLongClickListener.onItemLongClick(position,items.get(position));
                    return true;
                }
            });
        }
        return holder;
    }

    /**
     * 如果是一对多，返回索引position
     * 如果是一对一，返回provider 的 索引index*-1
     * @param position
     * @return 根据正负，判断是一对多还是一对一，
     */
    @Override
    public int getItemViewType(int position) {
        Item item = items.get(position);
        int index = indexOf(onFlattenClass(item));
        ItemViewProvider provider = getProviderByIndex(index);
        if (provider.isMultiType()){
            return position;
        }else {
            return -1 * (index + 1);
        }
    }

    private int getProviderIndex(int viewType){
        if (viewType >= 0){
            Item item = items.get(viewType);
            return indexOf(onFlattenClass(item));
        }else {
            return (viewType * -1) - 1;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        T item = items.get(position);
        int index = indexOf(onFlattenClass(item));
        ItemViewProvider provider = getProviderByIndex(index);
        if (provider.isMultiType()){
            MultiItemView<T> multiItemView = provider.getMultiItemView(item);
            multiItemView.convert((MultiItemViewHolder) holder,item,position);
        }else {
            provider.convert((MultiItemViewHolder) holder, item,position);
            provider.convert((MultiItemViewHolder) holder, item);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @NonNull
    @Override
    public Class onFlattenClass(@NonNull Item item) {
        return item.getClass();
    }

    @NonNull
    @Override
    public Item onFlattenItem(@NonNull Item item) {
        return item;
    }

    public void registerAll(@NonNull final MultiTypePool pool) {
        for (int i = 0; i < pool.getContents().size(); i++) {
            delegate.register(pool.getContents().get(i), pool.getProviders().get(i));
        }
    }

    @Override
    public void register(@NonNull Class<? extends Item> clazz, @NonNull ItemViewProvider provider) {
        delegate.register(clazz,provider);
    }

    @Override
    public int indexOf(@NonNull Class<? extends Item> clazz) {
        int index = delegate.indexOf(clazz);
        if (index >= 0) {
            return index;
        }else {
            throw  new RuntimeException("Do you have registered the provider for {className}.class in the adapter/pool?"
                    .replace("{className}", clazz.getSimpleName()));
        }
    }

    @NonNull
    @Override
    public ArrayList<Class<? extends Item>> getContents() {
        return delegate.getContents();
    }

    @NonNull
    @Override
    public ArrayList<ItemViewProvider> getProviders() {
        return delegate.getProviders();
    }

    @NonNull
    @Override
    public ItemViewProvider getProviderByIndex(int index) {
        return delegate.getProviderByIndex(index);
    }

    @NonNull
    @Override
    public <Provider extends ItemViewProvider> Provider getProviderByClass(@NonNull Class<? extends Item> clazz) {
        return delegate.getProviderByClass(clazz);
    }

    public List<T> getData(){
        return items;
    }

    public boolean isEmpty(){
        return items == null || items.isEmpty();
    }

    /**
     * 添加一条数据到底部
     * @param item
     */
    public void add(T item){
        if (item != null){
            items.add(item);
            notifyItemRangeChanged(items.size()-1,1);
        }
    }

    /**
     * 说明：添加数据
     * @param position
     * @param item
     */
    public void add(int position,T item){
        if (item != null && position >=0 && position < items.size()){
            items.add(position, item);
            notifyItemInserted(position);
        }
    }

    /**
     * 说明：添加数据
     * @param position
     * @param data
     */
    public void addAll(int position,List<T> data){
        if (data == null){
            return;
        }
        if (position >=0 && position < items.size()){
            items.addAll(position, data);
            notifyItemRangeChanged(position,data.size());
        }
    }

    /**
     * 说明：添加数据
     * @param data
     */
    public void addAll(List<T> data){
        if (data == null){
            return;
        }
        items.addAll(data);
        notifyItemRangeChanged(items.size()-data.size()-1,data.size());
    }

    /**
     * 说明：删除数据
     * @param position
     */
    public void remove(int position){
        if (checkPostion(position)){
            items.remove(position);
            notifyItemRemoved(position);
        }
    }

    /**
     * 说明：删除数据
     * @param t
     */
    public void remove(T t){
        if (items != null && items.contains(t)){
            items.remove(t);
            notifyDataSetChanged();
        }
    }

    /**
     * 说明：修改数据
     * @param position
     * @param item
     */
    public void changed(int position,T item){
        if (checkPostion(position) && item != null){
            items.set(position,item);
            notifyItemChanged(position);
        }
    }

    /**
     * 说明：刷新数据
     * @param data
     */
    public void refresh(List<T> data){
        items.clear();
        if (data != null){
            items.addAll(data);
        }
        notifyDataSetChanged();
    }

    private boolean checkPostion(int position){
        return position >=0 && position < items.size();
    }
}
