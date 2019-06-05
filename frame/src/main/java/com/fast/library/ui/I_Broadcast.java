package com.fast.library.ui;

/**
 * 说明：规范Activity中广播接受者注册的接口协议
 */
public interface I_Broadcast {
    /**
     * 注册广播
     */
    void registerBroadcast();

    /**
     * 解除注册广播
     */
    void unRegisterBroadcast();
}
