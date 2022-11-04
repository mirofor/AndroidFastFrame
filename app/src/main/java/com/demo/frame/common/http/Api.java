package com.demo.frame.common.http;

import com.fast.frame.interrface.OnLoadListener;
import com.fast.library.http.callback.BaseHttpCallBack;
import com.fast.library.http.callback.StringCallBack;
import com.fast.library.utils.GsonUtils;
import com.fast.library.utils.LogUtils;
import com.fast.library.utils.StringUtils;
import com.fast.library.utils.ToastUtils;
import com.fast.library.utils.ToolUtils;
import com.demo.frame.BikeApp;
import com.demo.frame.utils.XContant;

/**
 * 说明：Api
 */
public class Api {

    private static OnlineDataCenter onlineDataCenter = new OnlineDataCenter();

    public static OnlineDataCenter get() {
        return onlineDataCenter;
    }

    public static String getApi() {
        if (BikeApp.APP_DUBUG) { //测试环境
            return "http://tech.com";
        }
        return "http://techl.com";
    }

    public static String getImgApi() {
        return getApi() + "/storage/";
    }

    public static String getFinalFilePath(String serverFilePath) {
        if (StringUtils.isEmpty(serverFilePath)) {
            return "";
        }
        if (serverFilePath.startsWith("http") || serverFilePath.startsWith("https")) {
            return serverFilePath;
        } else {
            return getApi() + "/storage/" + serverFilePath;
        }
    }

    public static class Method {
        public final static String Uploadcoord = getApi() + "/userapi/user/uploadcoord";
        public final static String AlipayrRiding = getApi() + "/userapi/user/alipayriding";


    }

    public static StringCallBack getStringCallBack(final OnLoadListener<String> listener) {
        StringCallBack callBack = new StringCallBack() {
            @Override
            public void onStart() {
                if (listener != null) {
                    listener.onStart();
                }
            }

            @Override
            public void onFinish() {
                if (listener != null) {
                    listener.onFinish();
                }
            }

            @Override
            public void onSuccess(String result) {
                try {
                    if (StringUtils.isNotEmpty(result)) {
                        BaseResponse response = GsonUtils.toBean(result, BaseResponse.class);
                        if (response.isSuccess()) {//成功
                            if (listener != null) {
                                listener.onSuccess(result);
                            }
                        } else {
                            handleError(result, listener);
                        }
                    } else {
                        onFailure(BaseHttpCallBack.ERROR_RESPONSE_NULL, null);
                    }
                } catch (Exception e) {
//                    if (listener != null) {
//                        listener.onError(-1, "数据转换错误");
//                    }
                    LogUtils.e("error:", ToolUtils.collectErrorInfo(e));

                }
            }

            @Override
            public void onFailure(int errorCode, String msg) {
                if (errorCode == BaseHttpCallBack.ERROR_RESPONSE_NO_NETWORK ||
                        errorCode == BaseHttpCallBack.ERROR_RESPONSE_TIMEOUT ||
                        errorCode == BaseHttpCallBack.ERROR_RESPONSE_UNKNOWN) {
                    if (listener != null) {
                        listener.onError(errorCode, "网络连接超时");
                    }
                } else {
                    if (listener != null) {
                        listener.onError(errorCode, StringUtils.isNotEmpty(msg) ? msg : "网络连接错误");
                    }
                }
            }
        };
        return callBack;
    }

    public static <T> StringCallBack getModeCallBack(final Class<T> srcClass, final OnLoadListener<T> listener) {
        StringCallBack callBack = new StringCallBack() {
            @Override
            public void onStart() {
                if (listener != null) {
                    listener.onStart();
                }
            }

            @Override
            public void onFinish() {
                if (listener != null) {
                    listener.onFinish();
                }
            }

            @Override
            public void onSuccess(String result) {
                try {
                    if (StringUtils.isNotEmpty(result)) {
                        BaseResponse response = GsonUtils.toBean(result, BaseResponse.class);
                        if (response.isSuccess()) {//成功
                            T t = GsonUtils.toBean(result, srcClass);
                            if (listener != null) {
                                listener.onSuccess(t);
                            }
                        } else {
                            handleError(result, listener);
                        }
                    } else {
                        onFailure(BaseHttpCallBack.ERROR_RESPONSE_NULL, null);
                    }
                } catch (Exception e) {
                    onFailure(ERROR_RESPONSE_JSON_EXCEPTION, "数据转换错误");
                    LogUtils.e("数据类型转换错误：\n");
                    LogUtils.e(ToolUtils.collectErrorInfo(e));
                }
            }

            @Override
            public void onFailure(int errorCode, String msg) {
                if (errorCode == BaseHttpCallBack.ERROR_RESPONSE_NO_NETWORK ||
                        errorCode == BaseHttpCallBack.ERROR_RESPONSE_TIMEOUT ||
                        errorCode == BaseHttpCallBack.ERROR_RESPONSE_UNKNOWN) {
                    if (listener != null) {
                        ToastUtils.get().shortToast("网络连接超时");
                        listener.onError(errorCode, "网络连接超时");
                    }
                } else {
                    if (listener != null) {
                        ToastUtils.get().shortToast("请求失败~");
                        listener.onError(errorCode, StringUtils.isNotEmpty(msg) ? msg : "请求失败~");
                    }
                }
            }
        };
        return callBack;
    }

    /**
     * 接口错误码处理
     *
     * @param response
     * @param listener
     */
    private static void handleError(String response, OnLoadListener listener) {
        String error = GsonUtils.optString(response, BaseResponse.MESSAGE);

        // 是否强制退出
//        int isQuit = GsonUtils.optInt(response, "is_quit");
//        if (isQuit == 1) {
//            listener.onErrorExit(GsonUtils.optInt(response, "status"), error);
//        } else {
//            if (StringUtils.isNotEmpty(error) && listener.showToastError()) {
//                RxToast.error(error);
//            }
//        }
        if (StringUtils.isNotEmpty(error) && listener.showToastError()) {
            ToastUtils.get().shortToast(error);

        }
        if (listener != null) {
//            listener.onError(GsonUtils.optInt(response, "status"), error);
            if (error.contains(XContant.TOKEN_INVAILED)) {
                listener.onError(BaseHttpCallBack.ERROR_TOKEN_INVAILED, error);
            } else {
                listener.onError(BaseHttpCallBack.ERROR_CODE_DEFAULT, error);
            }
        }
    }

    public static String getInfo(String result) {
        return GsonUtils.optString(result, BaseResponse.MESSAGE);
    }
}