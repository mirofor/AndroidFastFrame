package com.fast.library.http;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * 说明：OkHttp工厂
 */
public class OkHttpFactory {

    public static OkHttpClient create(HttpConfig config) {
        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();
        if (config == null){
            return okHttpBuilder.build();
        }
        //设置请求时间
        okHttpBuilder.connectTimeout(config.getTimeout(), TimeUnit.MILLISECONDS);
        okHttpBuilder.writeTimeout(config.getTimeout(), TimeUnit.MILLISECONDS);
        okHttpBuilder.readTimeout(config.getTimeout(), TimeUnit.MILLISECONDS);
        //请求支持重定向
        okHttpBuilder.followRedirects(config.getRedirects());
        okHttpBuilder.followSslRedirects(config.getSslRedirects());
        //设置网络拦截器
        if (config.getNetworkInterceptorList() != null && !config.getNetworkInterceptorList().isEmpty()){
            okHttpBuilder.networkInterceptors().addAll(config.getNetworkInterceptorList());
        }
        //设置应用拦截器
        if (config.getInterceptorList() != null && !config.getInterceptorList().isEmpty()){
            okHttpBuilder.interceptors().addAll(config.getInterceptorList());
        }
        if (config.getHostnameVerifier() != null){
            okHttpBuilder.hostnameVerifier(config.getHostnameVerifier());
        }
        //设置证书
        if (config.getCertificateList() != null && !config.getCertificateList().isEmpty()){
            HttpsCerManager httpsCerManager = new HttpsCerManager(okHttpBuilder);
            httpsCerManager.setCertificates(config.getCertificateList());
        }else if (config.getTrustAll()){
            HttpsCerManager httpsCerManager = new HttpsCerManager(okHttpBuilder);
            httpsCerManager.setTrustAll();
        }
        if (config.getCookieJar() != null){
            okHttpBuilder.cookieJar(config.getCookieJar());
        }
        if (config.getCache() != null){
            okHttpBuilder.cache(config.getCache());
        }
        if (config.getAuthenticator() != null){
            okHttpBuilder.authenticator(config.getAuthenticator());
        }
        if (config.getDispatcher() != null){
            okHttpBuilder.dispatcher(config.getDispatcher());
        }
        if (config.getProxy() != null){
            okHttpBuilder.proxy(config.getProxy());
        }
        return okHttpBuilder.build();
    }

}
