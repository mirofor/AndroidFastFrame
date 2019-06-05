package com.fast.library.bean;


import com.fast.library.utils.GsonUtils;

/**
 * 说明：Bean继承该类
 */
public class Pojo implements I_POJO {
    @Override
    public String getType() {
        return getClass().getName();
    }

    @Override
    public <T> T toBean(String json) {
        return GsonUtils.toBean(json, getType());
    }

    @Override
    public <T> T toList(String json) {
        return GsonUtils.toList(json, getType());
    }
}
