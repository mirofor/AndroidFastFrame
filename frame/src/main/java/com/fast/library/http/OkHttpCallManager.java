package com.fast.library.http;


import com.fast.library.utils.StringUtils;
import java.util.concurrent.ConcurrentHashMap;
import okhttp3.Call;

/**
 * 说明：OkHttp请求管理器
 */
public class OkHttpCallManager {

    private ConcurrentHashMap<String, Call> callMap;
    private static volatile OkHttpCallManager okHttpCallManager = null;

    private OkHttpCallManager(){
        callMap = new ConcurrentHashMap<>();
    }

    public static OkHttpCallManager getInstance(){
        OkHttpCallManager manager = okHttpCallManager;
        if (manager == null){
            synchronized (OkHttpCallManager.class){
                manager = okHttpCallManager;
                if (manager == null){
                    manager = new OkHttpCallManager();
                    okHttpCallManager = manager;
                }
            }
        }
        return manager;
    }

    public void addCall(String url,Call call){
        if (call != null && !StringUtils.isEmpty(url)){
            callMap.put(url,call);
        }
    }

    public Call getCall(String url){
        if (!StringUtils.isEmpty(url)){
            return callMap.get(url);
        }
        return null;
    }

    public void removeCall(String url){
        if (!StringUtils.isEmpty(url)){
            callMap.remove(url);
        }
    }

}
