package com.fast.library.Adapter.multi;

import android.support.annotation.NonNull;

import java.util.ArrayList;

/**
 * 说明：TypePool
 */
public interface TypePool {
    void register(@NonNull Class<? extends Item> clazz, @NonNull ItemViewProvider provider);

    int indexOf(@NonNull Class<? extends Item> clazz);

    @NonNull
    ArrayList<Class<? extends Item>> getContents();

    @NonNull
    ArrayList<ItemViewProvider> getProviders();

    @NonNull
    ItemViewProvider getProviderByIndex(int index);

    @NonNull
    <T extends ItemViewProvider> T getProviderByClass(
            @NonNull Class<? extends Item> clazz);
}
