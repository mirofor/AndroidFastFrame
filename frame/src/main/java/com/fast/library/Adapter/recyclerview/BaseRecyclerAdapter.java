package com.fast.library.Adapter.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import com.fast.library.utils.UIUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * 说明：BaseRecyclerAdapter
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerViewHolder> {

    private List<T> mData;
    private View mEmptyView;
    private RecyclerView mRecyclerView;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    public BaseRecyclerAdapter(RecyclerView recyclerView){
        this(recyclerView,null);
    }

    public BaseRecyclerAdapter(RecyclerView recyclerView,List<T> data){
        mData = data == null ? new ArrayList<T>(0) : data;
        this.mRecyclerView = recyclerView;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final RecyclerViewHolder holder = new RecyclerViewHolder(UIUtils.inflate(getItemLayoutId(viewType), parent, false));
        if (mOnItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.itemView,holder.getLayoutPosition());
                }
            });
        }
        if (mOnItemLongClickListener != null){
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemLongClickListener.onItemLongClick(holder.itemView,holder.getLayoutPosition());
                    return true;
                }
            });
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        if (position >= 0 && position < mData.size()){
            convert(holder,mData.get(position),position,getItemViewType(position));
        }else {
            convert(holder,null,position,getItemViewType(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    /**
     * 根据条目类型获取资源文件
     * @param viewType
     * @return
     */
    public abstract int getItemLayoutId(int viewType);

    @Override
    public int getItemCount() {
        return mData.size();
    }

    /**
     * 说明：赋值
     * @param holder
     * @param item
     * @param position
     */
    public abstract void convert(RecyclerViewHolder holder,T item,int position,int viewType);

    /**
     * 设置空布局
     * @param emptyView
     */
    public void setEmptyView(View emptyView){
        mEmptyView = emptyView;
        updateEmptyStatus();
    }

    /**
     * 说明：刷新状态
     */
    private void updateEmptyStatus(){
        boolean isEmpty = getData() == null || getData().isEmpty() ? true : false;
        if (isEmpty){
            if (mEmptyView != null){
                mEmptyView.setVisibility(View.VISIBLE);
            }
            if (mRecyclerView != null){
                mRecyclerView.setVisibility(View.GONE);
            }
        }else {
            if (mEmptyView != null){
                mEmptyView.setVisibility(View.GONE);
            }
            if (mRecyclerView != null){
                mRecyclerView.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 说明：添加数据
     * @param position
     * @param item
     */
    public void add(int position,T item){
        if (item != null && position >=0 && position < mData.size()){
            mData.add(position, item);
            notifyItemInserted(position);
        }
    }

    /**
     * 说明：添加数据
     * @param item
     */
    public void add(T item){
        if (item != null){
            mData.add(item);
            notifyItemRangeChanged(mData.size()-1,1);
        }
    }

    /**
     * 说明：添加数据
     * @param position
     * @param data
     */
    public void addAll(int position,List<T> data){
        if (data == null || data.isEmpty()){
            return;
        }
        if (position >=0 && position < mData.size()){
            mData.addAll(position, data);
            notifyItemRangeChanged(position,data.size());
        }
    }

    /**
     * 说明：添加数据
     * @param data
     */
    public void addAll(List<T> data){
        if (data == null || data.isEmpty()){
            return;
        }
        mData.addAll(data);
        notifyItemRangeChanged(mData.size()-data.size()-1,data.size());
    }

    /**
     * 说明：修改数据
     * @param position
     * @param item
     */
    public void changed(int position,T item){
        if (checkPostion(position) && item != null){
            mData.set(position,item);
            notifyItemChanged(position);
        }
    }

    /**
     * 说明：删除数据
     * @param position
     */
    public void remove(int position){
        if (checkPostion(position)){
            mData.remove(position);
            notifyItemRemoved(position);
            updateEmptyStatus();
        }
    }

    private boolean checkPostion(int position){
        return position >=0 && position < mData.size();
    }


    /**
     * 说明：刷新数据
     * @param data
     */
    public void refresh(List<T> data){
        if (data == null || data.isEmpty()){
            mData.clear();
        }else {
            mData = data;
        }
        notifyDataSetChanged();
        updateEmptyStatus();
    }

    /**
     * 说明：获取数据
     * @return
     */
    public List<T> getData(){
        return mData;
    }

    /**
     * 说明：设置点击事件
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener listener){
        mOnItemClickListener = listener;
    }

    /**
     * 说明：设置点击事件
     * @param listener
     */
    public void setOnItemLongClickListener(OnItemLongClickListener listener){
        mOnItemLongClickListener = listener;
    }

    /**
     * 说明：点击
     */
    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    /**
     * 说明：长按
     */
    public interface OnItemLongClickListener{
        void onItemLongClick(View view, int position);
    }
}
