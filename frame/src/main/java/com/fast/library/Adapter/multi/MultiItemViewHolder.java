package com.fast.library.Adapter.multi;

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
 * 说明：MultiItemViewHolder
 */
public class MultiItemViewHolder extends RecyclerView.ViewHolder{

    //view的集合类
    private SparseArray<View> mViews;
    private View mItemView;

    public MultiItemViewHolder(){
        super(null);
    }

    public MultiItemViewHolder(View itemView) {
        super(itemView);
        if (itemView != null){
            mItemView = itemView;
            mViews = new SparseArray<>();
        }
    }

    public void initView(View view){
        mItemView = itemView;
        mViews = new SparseArray<>();
    }

    /**
     * 通过控件的Id获取对应的控件，如果没有则加入views
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
    public MultiItemViewHolder setText(int viewId, CharSequence text) {
        ViewTools.setText(getView(viewId), text);
        return this;
    }

    /**
     * 说明：为TextView设置字符串
     * @param viewId
     * @param resId
     * @return
     */
    public MultiItemViewHolder setText(int viewId, int resId) {
        ViewTools.setText(getView(viewId),resId);
        return this;
    }

    /**
     * 说明：显示view
     * @param viewId
     * @return
     */
    public MultiItemViewHolder setVisible(int viewId) {
        View view = getView(viewId);
        ViewTools.VISIBLE(view);
        return this;
    }

    /**
     * 说明：隐藏view
     * @param viewId
     * @return
     */
    public MultiItemViewHolder setGone(int viewId) {
        View view = getView(viewId);
        ViewTools.GONE(view);
        return this;
    }

    /**
     * 说明：设置显示还是隐藏
     * @param viewId
     * @return
     */
    public MultiItemViewHolder setVisibleOrGone(int viewId, boolean show) {
        View view = getView(viewId);
        if (show){
            ViewTools.VISIBLE(view);
        }else {
            ViewTools.GONE(view);
        }
        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param url
     * @return
     */
    public MultiItemViewHolder setImage(int viewId, String url) {
        if (!StringUtils.isEmpty(url)){
            ImageView view = getView(viewId);
            //TODO 加载图片
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
    public MultiItemViewHolder setImageResource(int viewId, int drawableId) {
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
    public MultiItemViewHolder setImageBitmap(int viewId, Bitmap bm) {
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
    public MultiItemViewHolder setOnClickListener(int viewId, View.OnClickListener listener){
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
    public MultiItemViewHolder setOnLongClickListener(int viewId, View.OnLongClickListener listener){
        View view = getView(viewId);
        view.setOnLongClickListener(listener);
        return this;
    }
}
