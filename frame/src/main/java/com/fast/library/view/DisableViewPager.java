package com.fast.library.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

/**
 * 说明：禁止滑动的ViewPager
 * @author xiaomi
 */
public class DisableViewPager extends ViewPager {

    public DisableViewPager(Context context) {
        super(context);
    }

    public DisableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }
}
