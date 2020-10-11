package com.fast.frame;

import android.os.Bundle;

import androidx.loader.content.Loader;

/**
 * 说明：ActivityXFrame
 */
public abstract class ActivityXFrame extends ActivityFrame{

    @Override
    public int createLoaderID() {
        return 0;
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {

    }
}
