package com.fast.library.Adapter.viewpager;

import android.os.Bundle;

/**
 * 说明：ViewPageInfo
 */
public class ViewPageInfo {
    public final String title;
    public final Bundle params;
    public final Class<?> clazz;

    public ViewPageInfo(String title, Class<?> clazz, Bundle params){
        this.title = title;
        this.clazz = clazz;
        this.params = params;
    }

}
