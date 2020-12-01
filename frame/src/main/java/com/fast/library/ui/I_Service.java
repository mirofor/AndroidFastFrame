package com.fast.library.ui;

/**
 * 说明：规范Activity中服务注册的接口协议
 */
public interface I_Service {
    /**
     * 注册服务
     */
    void registerService();

    /**
     * 解除服务
     */
    void unRegisterService();
}
