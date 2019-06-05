package com.demo.frame.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by aimi on 2018/6/13.
 */

public class CoverScrollView extends ScrollView {

    public CoverScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CoverScrollView(Context context) {
        super(context);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, l, t, oldl, oldt);
        }
    }

    private ScrollViewListener scrollViewListener;

    public void setonScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    public interface ScrollViewListener {

        void onScrollChanged(CoverScrollView scrollView, int x, int y, int oldx,
                             int oldy);

    }

}