package com.fast.library.banner.holder;

import android.content.Context;
import android.view.View;

/**
 * 说明：Holder
 */
public interface Holder<T> {
    View createView(Context context);
    void convert(Context context, int position, T item);
}
