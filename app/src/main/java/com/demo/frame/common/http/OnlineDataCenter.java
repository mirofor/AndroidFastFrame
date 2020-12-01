package com.demo.frame.common.http;

import com.fast.frame.interrface.OnLoadListener;
import com.fast.library.utils.GsonUtils;

/**
 * 说明：正式接口
 * @author xiaomi
 */
public class OnlineDataCenter {

    private class WrapperOnloadListener<T> extends OnLoadListener<String> {
        private String key;
        private OnLoadListener<T> listener;
        private Class<T> clazz;

        public WrapperOnloadListener(OnLoadListener<T> listener, String key, Class<T> clazz) {
            this.listener = listener;
            this.key = key;
            this.clazz = clazz;
        }

        @Override
        public void onStart() {
            if (listener != null) {
                listener.onStart();
            }
        }

        @Override
        public void onError(int code, String error) {
            if (listener != null) {
                listener.onError(code, error);
            }
        }

        @Override
        public void onFinish() {
            if (listener != null) {
                listener.onFinish();
            }
        }

        @Override
        public void onSuccess(String s) {
            try {
                String o = GsonUtils.optString(s, key);
                T t = GsonUtils.toBean(o, clazz);
                if (t != null) {
                    if (listener != null) {
                        listener.onSuccess(t);
                    }
                } else {
                    onError(-1, "数据为空");
                }
            } catch (Exception e) {
                onError(-1, e.getMessage());
            }
        }
    }



}
