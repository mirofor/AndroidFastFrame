package com.fast.library.ui;

/**
 * 说明：规范Activity中服务注册的接口协议
 */
public interface I_Service {
    /**
     * 注册广播
     */
    void registerService();

    /**
     * 解除注册广播
     */
    void unRegisterService();
}
