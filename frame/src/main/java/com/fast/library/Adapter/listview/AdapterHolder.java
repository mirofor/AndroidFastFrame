package com.fast.library.Adapter.listview;

import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fast.library.utils.StringUtils;
import com.fast.library.utils.UIUtils;

/**
 * 说明：通用Holder
 */
public class AdapterHolder {

    private final SparseArray<View> mViews;
    private final int mPosition;
    private final View mConvertView;

    private AdapterHolder(ViewGroup parent, int layoutId, int position) {
        this.mPosition = position;
        this.mViews = new SparseArray<View>();
        mConvertView = UIUtils.inflate(layoutId, parent, false);
        // setTag
        mConvertView.setTag(this);
    }

    /**
     * 拿到全部View
     *
     * @return
     */
    public SparseArray<View> getAllView() {
        return mViews;
    }

    /**
     * 拿到一个ViewHolder对象
     *
     * @param convertView
     * @param parent
     * @param layoutId
     * @param position
     * @return
     */
    public static AdapterHolder get(View convertView, ViewGroup parent,
                                    int layoutId, int position) {
        if (convertView == null) {
            return new AdapterHolder(parent, layoutId, position);
        } else {
            return (AdapterHolder) convertView.getTag();
        }
    }

    public View getConvertView() {
        return mConvertView;
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
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 说明：获取TextView
     *
     * @param viewId
     * @return
     */
    public TextView getTextView(int viewId) {
        return (TextView) getView(viewId);
    }

    /**
     * 说明：获取ImageView
     *
     * @param viewId
     * @return
     */
    public ImageView getImageView(int viewId) {
        return (ImageView) getView(viewId);
    }

    /**
     * 说明：获取Button
     *
     * @param viewId
     * @return
     */
    public Button getButton(int viewId) {
        return (Button) getView(viewId);
    }

    /**
     * 说明：获取CheckBox
     *
     * @param viewId
     * @return
     */
    public CheckBox getCheckBox(int viewId) {
        return (CheckBox) getView(viewId);
    }

    /**
     * 说明：获取EditText
     *
     * @param viewId
     * @return
     */
    public EditText getEditText(int viewId) {
        return (EditText) getView(viewId);
    }

    /**
     * 为TextView设置字符串
     *
     * @param viewId
     * @param resId
     * @return
     */
    public AdapterHolder setText(int viewId, int resId) {
        TextView view = getView(viewId);
        view.setText(resId);
        return this;
    }

    /**
     * 为TextView设置字符串
     *
     * @param viewId
     * @param text
     * @return
     */
    public AdapterHolder setText(int viewId, CharSequence text) {
        TextView view = getView(viewId);
        view.setText(text);
        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param drawableId
     * @return
     */
    public AdapterHolder setImageResource(int viewId, int drawableId) {
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
    public AdapterHolder setImageBitmap(int viewId, Bitmap bm) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bm);
        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param url
     * @return
     */
    public AdapterHolder setImage(int viewId, String url) {
        if (!StringUtils.isEmpty(url)){
            ImageView image = getView(viewId);
            //TODO 加载图片
//            GlideLoader.into(image,url);
        }
        return this;
    }

    public int getPosition() {
        return mPosition;
    }

}