package com.fast.library.Adapter.multi;

import android.support.annotation.NonNull;

/**
 * 说明：FlatTypeAdapter
 */
public interface FlatTypeAdapter {
    @NonNull Class onFlattenClass(@NonNull Item item);
    @NonNull
    Item onFlattenItem(@NonNull final Item item);
}
