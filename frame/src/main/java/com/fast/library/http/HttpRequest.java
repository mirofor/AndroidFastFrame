package com.fast.library.http;

import com.fast.library.http.callback.BaseHttpCallBack;
import com.fast.library.utils.FrameConstant;
import com.fast.library.utils.StringUtils;
import okhttp3.Call;

/**
 * 说明：Http请求类
 * @author xiaomi
 */
public class HttpRequest {

    /**********GET****************/
    /**
     * get
     * @param url 请求url
     */
    public static void get(String url){
        get(url,null,null);
    }

    public static void get(String url,RequestParams params){
        get(url,params,null);
    }

    public static void get(String url,BaseHttpCallBack callback){
        get(url, null, callback);
    }

    public static void get(String url,RequestParams params,BaseHttpCallBack callback){
        get(url, params, callback, 0);
    }
    public static void get(String url,RequestParams params,BaseHttpCallBack callback,int timeout){
        if (timeout == 0){
            timeout = HttpConfig.get().getTimeout();
        }
        executeRequest(FrameConstant.Http.GET, url, params, callback, timeout);
    }
    /**********POST****************/
    /**
     * post请求
     * @param url 请求url
     */
    public static void post(String url){
        post(url, null, null);
    }

    public static void post(String url,RequestParams params){
        post(url, params, null);
    }

    public static void post(String url,BaseHttpCallBack callback){
        post(url, null, callback);
    }

    public static void post(String url,RequestParams params,BaseHttpCallBack callback){
        post(url, params, callback, 0);
    }

    public static void post(String url,RequestParams params,BaseHttpCallBack callback,int timeout){
        if (timeout == 0){
            timeout = HttpConfig.get().getTimeout();
        }
        executeRequest(FrameConstant.Http.POST, url, params, callback, timeout);
    }

    /**********PUT****************/
    public static void put(String url){
        put(url, null, null);
    }
    public static void put(String url,RequestParams params){
        put(url, params, null);
    }
    public static void put(String url,BaseHttpCallBack callback){
        put(url, null, callback);
    }
    public static void put(String url,RequestParams params,BaseHttpCallBack callback){
        put(url, params, callback, 0);
    }

    /**
     * put 请求
     * @param url url地址
     * @param params 参数
     * @param callback 回调
     * @param timeout 超时时间
     */
    public static void put(String url,RequestParams params,BaseHttpCallBack callback,int timeout){
        if (timeout == 0){
            timeout = HttpConfig.get().getTimeout();
        }
        executeRequest(FrameConstant.Http.PUT, url, params, callback, timeout);
    }

    /**********DELETE****************/
    public static void delete(String url){
        delete(url, null, null);
    }
    public static void delete(String url,RequestParams params){
        delete(url, params, null);
    }
    public static void delete(String url,BaseHttpCallBack callback){
        delete(url, null, callback);
    }
    public static void delete(String url,RequestParams params,BaseHttpCallBack callback){
        delete(url, params, callback, 0);
    }
    public static void delete(String url,RequestParams params,BaseHttpCallBack callback,int timeout){
        if (timeout == 0){
            timeout = HttpConfig.get().getTimeout();
        }
        executeRequest(FrameConstant.Http.DELETE, url, params, callback, timeout);
    }

    /**********HEAD****************/
    public static void head(String url){
        head(url, null, null);
    }
    public static void head(String url,RequestParams params){
        head(url, params, null);
    }
    public static void head(String url,BaseHttpCallBack callback){
        head(url, null, callback);
    }
    public static void head(String url,RequestParams params,BaseHttpCallBack callback){
        head(url, params, callback, 0);
    }
    public static void head(String url,RequestParams params,BaseHttpCallBack callback,int timeout){
        if (timeout == 0){
            timeout = HttpConfig.get().getTimeout();
        }
        executeRequest(FrameConstant.Http.HEAD, url, params, callback, timeout);
    }

    /**********PATCH****************/
    public static void patch(String url){
        patch(url, null, null);
    }
    public static void patch(String url,RequestParams params){
        patch(url, params, null);
    }
    public static void patch(String url,BaseHttpCallBack callback){
        patch(url, null, callback);
    }
    public static void patch(String url,RequestParams params,BaseHttpCallBack callback){
        patch(url, params, callback, 0);
    }
    public static void patch(String url,RequestParams params,BaseHttpCallBack callback,int timeout){
        if (timeout == 0){
            timeout = HttpConfig.get().getTimeout();
        }
        executeRequest(FrameConstant.Http.PATCH, url, params, callback, timeout);
    }

    /**
     * 说明：取消所有key的请求
     * @param key
     */
    public static void cancelKey(String key){
        HttpTaskHandler.getInstance().removeTask(key);
    }

    /**
     * 说明：取消请求
     * @param url
     */
    public static void cancel(String url){
        if (!StringUtils.isEmpty(url)){
            Call call = OkHttpCallManager.getInstance().getCall(url);
            if (call != null){
                call.cancel();
            }
            OkHttpCallManager.getInstance().removeCall(url);
        }
    }

    /**
     * 说明：执行请求
     * @param method 请求方式
     * @param url 请求链接
     * @param params 请求参数
     * @param callBack 请求回调
     * @param timeout 请求超时
     */
    private static void executeRequest(String method,String url,RequestParams params,BaseHttpCallBack callBack
                                        ,int timeout){
        if (!StringUtils.isEmpty(url)){
            HttpTask task = new HttpTask(method,url,params,callBack,timeout);
            task.execute();
        }
    }
}
