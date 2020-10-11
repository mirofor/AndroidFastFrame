package com.fast.library.Adapter.recyclerview;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fast.library.tools.ViewTools;
import com.fast.library.utils.StringUtils;

/**
 * 说明：RecyclerViewHolder
 */
public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    //view的集合类
    private SparseArray<View> mViews;
    private View mItemView;

    public RecyclerViewHolder(View itemView) {
        super(itemView);
        mItemView = itemView;
        mViews = new SparseArray<>();
    }

    /**
     * 通过控件的Id获取对于的控件，如果没有则加入views
     *
     * @param viewId
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mItemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 说明：获取TextView
     * @param viewId
     * @return
     */
    public TextView getTextView(int viewId){
        return (TextView)getView(viewId);
    }

    /**
     * 说明：获取ImageView
     * @param viewId
     * @return
     */
    public ImageView getImageView(int viewId){
        return (ImageView)getView(viewId);
    }

    /**
     * 说明：获取Button
     * @param viewId
     * @return
     */
    public Button getButton(int viewId){
        return (Button)getView(viewId);
    }

    /**
     * 说明：获取CheckBox
     * @param viewId
     * @return
     */
    public CheckBox getCheckBox(int viewId){
        return (CheckBox)getView(viewId);
    }

    /**
     * 说明：获取EditText
     * @param viewId
     * @return
     */
    public EditText getEditText(int viewId){
        return (EditText)getView(viewId);
    }

    /**
     * 说明：为TextView设置字符串
     * @param viewId
     * @param text
     * @return
     */
    public RecyclerViewHolder setText(int viewId, CharSequence text) {
        ViewTools.setText(getView(viewId), text);
        return this;
    }

    /**
     * 说明：为TextView设置字符串
     * @param viewId
     * @param resId
     * @return
     */
    public RecyclerViewHolder setText(int viewId, int resId) {
        ViewTools.setText(getView(viewId),resId);
        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param url
     * @return
     */
    public RecyclerViewHolder setImage(int viewId, String url) {
        if (!StringUtils.isEmpty(url)){
            ImageView view = getView(viewId);
            // TODO 加载图片
//            GlideLoader.into(view,url);
        }
        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param drawableId
     * @return
     */
    public RecyclerViewHolder setImageResource(int viewId, int drawableId) {
        ImageView view = getView(viewId);
        view.setImageResource(drawableId);
        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param bm
     * @return
     */
    public RecyclerViewHolder setImageBitmap(int viewId, Bitmap bm) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bm);
        return this;
    }

    /**
     * 说明：设置点击事件
     * @param viewId
     * @param listener
     * @return
     */
    public RecyclerViewHolder setClickListener(int viewId,View.OnClickListener listener){
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }

    /**
     * 说明：设置长点击事件
     * @param viewId
     * @param listener
     * @return
     */
    public RecyclerViewHolder setLongClickListener(int viewId,View.OnLongClickListener listener){
        View view = getView(viewId);
        view.setOnLongClickListener(listener);
        return this;
    }

}
