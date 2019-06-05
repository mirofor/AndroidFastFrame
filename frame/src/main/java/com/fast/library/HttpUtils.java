package com.fast.library;

import android.text.TextUtils;

import com.fast.library.http.HttpRequest;
import com.fast.library.http.RequestParams;
import com.fast.library.http.callback.BaseHttpCallBack;
import com.fast.library.http.callback.DownloadCallBack;
import com.fast.library.http.download.FileDownloadTask;

import java.io.File;


/**
 * 说明：Http请求网络工具类
 */
public class HttpUtils {

    /**
     * 说明：禁止实例化
     */
    private HttpUtils(){}

    /**
     * 说明：发送post请求
     * @param url
     */
    public static void post(String url){
        HttpRequest.post(url);
    }

    /**
     * 说明：发送post请求
     * @param url
     * @param params
     */
    public static void post(String url,RequestParams params){
        HttpRequest.post(url,params);
    }

    /**
     * 说明：发送post请求
     * @param url
     * @param callBack
     */
    public static void post(String url,BaseHttpCallBack callBack){
        HttpRequest.post(url, callBack);
    }

    /**
     * 说明：发送post请求
     * @param url
     * @param params
     * @param callBack
     */
    public static void post(String url,RequestParams params,BaseHttpCallBack callBack){
        HttpRequest.post(url, params, callBack);
    }

    /**
     * 说明：发送get请求
     * @param url
     */
    public static void get(String url){
        HttpRequest.get(url);
    }

    /**
     * 说明：发送get请求
     * @param url
     * @param params
     */
    public static void get(String url,RequestParams params){
        HttpRequest.get(url, params);
    }

    /**
     * 说明：发送get请求
     * @param url
     * @param callBack
     */
    public static void get(String url,BaseHttpCallBack callBack){
        HttpRequest.get(url, callBack);
    }

    /**
     * 说明：发送get请求
     * @param url
     * @param params
     * @param callBack
     */
    public static void get(String url,RequestParams params,BaseHttpCallBack callBack){
        HttpRequest.get(url, params, callBack);
    }

    /**
     * 说明：发送put请求
     * @param url
     */
    public static void put(String url){
        HttpRequest.put(url);
    }

    /**
     * 说明：发送put请求
     * @param url
     * @param params
     */
    public static void put(String url,RequestParams params){
        HttpRequest.put(url, params);
    }

    /**
     * 说明：发送put请求
     * @param url
     * @param callBack
     */
    public static void put(String url,BaseHttpCallBack callBack){
        HttpRequest.put(url, callBack);
    }

    /**
     * 说明：发送put请求
     * @param url
     * @param params
     * @param callBack
     */
    public static void put(String url,RequestParams params,BaseHttpCallBack callBack){
        HttpRequest.put(url, params, callBack);
    }

    /**
     * 说明：发送head请求
     * @param url
     */
    public static void head(String url){
        HttpRequest.head(url);
    }

    /**
     * 说明：发送head请求
     * @param url
     * @param params
     */
    public static void head(String url,RequestParams params){
        HttpRequest.head(url, params);
    }

    /**
     * 说明：发送head请求
     * @param url
     * @param callBack
     */
    public static void head(String url,BaseHttpCallBack callBack){
        HttpRequest.head(url, callBack);
    }

    /**
     * 说明：发送head请求
     * @param url
     * @param params
     * @param callBack
     */
    public static void head(String url,RequestParams params,BaseHttpCallBack callBack){
        HttpRequest.head(url, params, callBack);
    }

    /**
     * 说明：发送delete请求
     * @param url
     */
    public static void delete(String url){
        HttpRequest.delete(url);
    }

    /**
     * 说明：发送delete请求
     * @param url
     * @param params
     */
    public static void delete(String url,RequestParams params){
        HttpRequest.delete(url, params);
    }

    /**
     * 说明：发送delete请求
     * @param url
     * @param callBack
     */
    public static void delete(String url,BaseHttpCallBack callBack){
        HttpRequest.delete(url, callBack);
    }

    /**
     * 说明：发送delete请求
     * @param url
     * @param params
     * @param callBack
     */
    public static void delete(String url,RequestParams params,BaseHttpCallBack callBack){
        HttpRequest.delete(url, params, callBack);
    }

    /**
     * 说明：发送patch请求
     * @param url
     */
    public static void patch(String url){
        HttpRequest.patch(url);
    }

    /**
     * 说明：发送patch请求
     * @param url
     * @param params
     */
    public static void patch(String url,RequestParams params){
        HttpRequest.patch(url, params);
    }

    /**
     * 说明：发送patch请求
     * @param url
     * @param callBack
     */
    public static void patch(String url,BaseHttpCallBack callBack){
        HttpRequest.patch(url, callBack);
    }

    /**
     * 说明：发送patch请求
     * @param url
     * @param params
     * @param callBack
     */
    public static void patch(String url,RequestParams params,BaseHttpCallBack callBack){
        HttpRequest.patch(url, params, callBack);
    }

    /**
     * 说明：取消一个请求
     * @param url
     */
    public static void cancel(String url){
        HttpRequest.cancel(url);
    }

    /**
     * 说明：取消同一个key的请求
     * @param key
     */
    public static void cancelKey(String key){
        HttpRequest.cancelKey(key);
    }

    /**
     * 说明：下载文件
     * @param url
     * @param dir
     * @param name
     * @param callBack
     */
    public static void download(String url,String dir,String name,DownloadCallBack callBack){
        File file = new File(dir,name);
        download(url,file,callBack);
    }

    /**
     * 说明：下载文件
     * @param url
     * @param target
     */
    public static void download(String url,File target){
        download(url, target, null);
    }

    /**
     * 说明：下载文件
     * @param url
     * @param target
     * @param callBack
     */
    public static void download(String url,File target,DownloadCallBack callBack){
        if (!TextUtils.isEmpty(url) && target != null){
            FileDownloadTask task = new FileDownloadTask(url,target,callBack);
            task.execute();
        }
    }

}
