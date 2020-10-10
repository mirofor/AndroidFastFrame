package com.fast.library.http;

import android.text.TextUtils;
import com.fast.library.FastFrame;
import com.fast.library.utils.FrameConstant;
import com.fast.library.utils.LogUtils;
import com.fast.library.utils.StringUtils;
import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.HostnameVerifier;
import okhttp3.Authenticator;
import okhttp3.Cache;
import okhttp3.CertificatePinner;
import okhttp3.CookieJar;
import okhttp3.Dispatcher;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okio.Buffer;

/**
 * 说明：Http配置器
 *
 *         //初始化HttpConfig
 *                 HttpConfig.Builder httpBuilder = new HttpConfig.Builder(this);
 *                 httpBuilder.build().init();
 */
public class HttpConfig {

    private OkHttpClient mOkHttpClient;
    private Proxy proxy;
    private Dispatcher dispatcher;
    private CookieJar cookieJar;
    private Cache cache;
    private Authenticator authenticator;
    private CertificatePinner certificatePinner;
    //失败重试
    private boolean retryConnectionFailure;
    //ssl重定向
    private boolean sslRedirects;
    //重定向
    private boolean redirects;
    //网络拦截器
    private List<Interceptor> networkInterceptorList;
    //拦截器
    private List<Interceptor> interceptorList;
    //全局的参数
    private List<Part> mCommonParams;
    //全局的头信息
    private Headers mCommonHeader;
    //证书列表
    private List<InputStream> mCertificateList;
    private HostnameVerifier mHostnameVerifier;
    //设置超时时间
    private int mTimeout;
    //设置日志输出（调试模式）
    private boolean mDebug;
    //设置信任所有证书（开发模式使用）
    private boolean mTrustAll;

    //用户配置
    private static HttpConfig mHttpConfig;
    //默认配置
    private static HttpConfig mDefaultHttpConfig;

    private HttpConfig(Builder builder){
        this.mCommonHeader = builder.mCommonHeader;
        this.mCommonParams = builder.mCommonParams;
        this.mCertificateList = builder.mCertificateList;
        this.mTimeout = builder.mTimeout;
        this.mHostnameVerifier = builder.mHostnameVerifier;
        this.mDebug = builder.mDebug;
        this.mTrustAll = builder.mTrustAll;
        this.networkInterceptorList = builder.networkInterceptorList;
        this.interceptorList = builder.interceptorList;
        this.retryConnectionFailure = builder.retryConnectionFailure;
        this.redirects = builder.redirects;
        this.sslRedirects = builder.redirects;
        this.cookieJar = builder.cookieJar;
        this.dispatcher = builder.dispatcher;
        this.cache = builder.cache;
        this.authenticator = builder.authenticator;
        this.certificatePinner = builder.certificatePinner;
        this.proxy = builder.proxy;
    }

    /**
     * 说明：初始化
     */
    public void init(){
        if (mHttpConfig != null){
            return;
        }else {
            this.mOkHttpClient = OkHttpFactory.create(this);
            mHttpConfig = this;
        }
    }

    public static class Builder{
        private Proxy proxy;
        private Dispatcher dispatcher;
        private CookieJar cookieJar;
        private Cache cache;
        private Authenticator authenticator;
        private CertificatePinner certificatePinner;
        //失败重试
        private boolean retryConnectionFailure;
        //ssl重定向
        private boolean sslRedirects;
        //重定向
        private boolean redirects;
        //网络拦截器
        private List<Interceptor> networkInterceptorList;
        //拦截器
        private List<Interceptor> interceptorList;
        //全局的参数
        private List<Part> mCommonParams;
        //全局的头信息
        private Headers mCommonHeader;
        //证书列表
        private List<InputStream> mCertificateList;
        private HostnameVerifier mHostnameVerifier;
        //设置超时时间
        private int mTimeout;
        //设置日志输出（调试模式）
        private boolean mDebug;
        //设置信任所有证书（开发模式使用）
        private boolean mTrustAll;

        public Builder(){
            this.mCertificateList = new ArrayList<>();
            this.networkInterceptorList = new ArrayList<>();
            this.interceptorList = new ArrayList<>();
            this.mTimeout = FrameConstant.Http.TIMEOUT;
            this.mDebug = FastFrame.isDebug;
            this.mTrustAll = false;
            retryConnectionFailure = true;
        }

        /**
         * 说明：添加公共参数
         * @param params 参数
         * @return Builder
         */
        public Builder setCommonParams(List<Part> params){
            this.mCommonParams = params;
            return this;
        }

        /**
         * 说明：添加公共Header
         * @param header header
         * @return Builder
         */
        public Builder setCommonHeaders(Headers header){
            this.mCommonHeader = header;
            return this;
        }

        /**
         * 说明：添加证书
         * @param certificates certificates
         * @return Builder
         */
        public Builder setCertificates(InputStream...certificates){
            for (InputStream is : certificates){
                if (is != null){
                    mCertificateList.add(is);
                }
            }
            return this;
        }

        /**
         * 说明：添加证书
         * @param certificates certificates...
         * @return Builder
         */
        public Builder setCertificates(String...certificates){
            for (String s : certificates){
                if (!StringUtils.isEmpty(s)){
                    mCertificateList.add(new Buffer().writeUtf8(s).inputStream());
                }
            }
            return this;
        }

        public Builder setHostnameVerifier(HostnameVerifier hostnameVerifier) {
            this.mHostnameVerifier = hostnameVerifier;
            return this;
        }

        /**
         * 说明：设置超时时间
         * @param timeout  超时时间
         * @return Builder
         */
        public Builder setTimeout(int timeout){
            this.mTimeout = timeout;
            return this;
        }

        /**
         * 说明：开启调试模式（输出Log日志）
         * @param debug true/false
         * @return 调试模式
         */
        public Builder setDebug(boolean debug){
            this.mDebug = debug;
            return this;
        }

        /**
         * 说明：设置信任所有证书（开发模式使用）
         * @param trust
         * @return
         */
        public Builder setTrustAll(boolean trust){
            this.mTrustAll = trust;
            return this;
        }

        /**
         * 说明：设置网络拦截器
         * @param interceptors 拦截器
         * @return Builder
         */
        public Builder setNetworkInterceptors(List<Interceptor> interceptors){
            if (interceptors != null){
                networkInterceptorList.addAll(interceptors);
            }
            return this;
        }

        /**
         * 说明：设置网络拦截器
         * @param interceptors
         * @return
         */
        public Builder setNetworkInterceptors(Interceptor interceptors){
            if (interceptors != null){
                networkInterceptorList.add(interceptors);
            }
            return this;
        }

        /**
         * 说明：应用网络拦截器
         * @param interceptors 拦截器
         * @return Builder
         */
        public Builder setInterceptors(List<Interceptor> interceptors){
            this.interceptorList = interceptors;
            return this;
        }

        /**
         * 说明：失败重试
         * @param retryConnectionFailue
         * @return Builder
         */
        public Builder setRetryConnectionFailue(boolean retryConnectionFailue){
            this.retryConnectionFailure = retryConnectionFailue;
            return this;
        }

        /**
         * 说明：设置重定向
         * @param redirect
         * @return Builder
         */
        public Builder setRedirect(boolean redirect){
            this.redirects = redirect;
            return this;
        }

        /**
         * 说明：设置SSL重定向
         * @param redirects
         * @return Builder
         */
        public Builder setSslRedirect(boolean redirects){
            this.sslRedirects = redirects;
            return this;
        }

        /**
         * 设置cookie jar
         * @param cookieJar
         * @return Builder
         */
        public Builder setCookieJar(CookieJar cookieJar) {
            this.cookieJar = cookieJar;
            return this;
        }

        /**
         * 设置缓存
         * @param cache
         * @return Builder
         */
        public Builder setCache(Cache cache) {
            this.cache = cache;
            return this;
        }

        /**
         * 设置缓存-并且添加网络拦截器修改响应头(有无网络都先读缓存)
         * 强制响应缓存者根据该值校验新鲜性.即与自身的Age值,与请求时间做比较.如果超出max-age值,则强制去服务器端验证.以确保返回一个新鲜的响应.
         * @param cache cache
         * @param cacheTime 缓存时间 单位秒
         * @return Builder
         */
        public Builder setCacheAge(Cache cache, final int cacheTime) {
            setCache(cache, String.format("max-age=%d", cacheTime));

            return this;
        }

        /**
         * 设置缓存-并且添加网络拦截器修改响应头(有无网络都先读缓存)
         * 允许缓存者发送一个过期不超过指定秒数的陈旧的缓存.
         * @param cache cache
         * @param cacheTime 缓存时间 单位秒
         * @return Builder
         */
        public Builder setCacheStale(Cache cache, final int cacheTime) {
            setCache(cache, String.format("max-stale=%d", cacheTime));
            return this;
        }

        /**
         * 设置缓存-并且添加网络拦截器修改响应头(有无网络都先读缓存)
         * @param cache cache
         * @param cacheControlValue Cache-Control值
         * @return Builder
         */
        public Builder setCache(Cache cache, final String cacheControlValue) {
            Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                            .removeHeader("Pragma")
                            .header("Cache-Control", cacheControlValue)
                            .build();
                }
            };
            networkInterceptorList.add(REWRITE_CACHE_CONTROL_INTERCEPTOR);
            this.cache = cache;
            return this;
        }

        /**
         * 设置Authenticator
         * @param authenticator authenticator
         * @return Builder
         */
        public Builder setAuthenticator(Authenticator authenticator) {
            this.authenticator = authenticator;
            return this;
        }

        public Builder setProxy(Proxy proxy) {
            this.proxy = proxy;
            return this;
        }

        /**
         * 设置Dispatcher实例
         * @param dispatcher dispatcher
         * @return Builder
         */
        public Builder setDispatcher(Dispatcher dispatcher) {
            this.dispatcher = dispatcher;
            return this;
        }

        public HttpConfig build(){
            return new HttpConfig(this);
        }
    }

    /**
     * 说明：获取Http配置器
     * @return HttpConfig
     */
    public static HttpConfig get(){
        if (mHttpConfig == null){
            return getDefaultHttpConfig();
        }else {
            return mHttpConfig;
        }
    }

    /**
     * 说明：获取默认的配置器
     * @return HttpConfig
     */
    private static HttpConfig getDefaultHttpConfig(){
        if (mDefaultHttpConfig == null){
            mDefaultHttpConfig = new Builder().setTimeout(FrameConstant.Http.TIMEOUT).build();
            mDefaultHttpConfig.init();
        }
        return mDefaultHttpConfig;
    }

    /**
     * 说明：获取OkHttpClient客户端
     * @return OkHttpClient
     */
    public OkHttpClient getOkHttpClient(){
        return mOkHttpClient;
    }

    public List<Part> getCommonParams() {
        return mCommonParams;
    }

    public List<InputStream> getCertificateList() {
        return mCertificateList;
    }


    public HostnameVerifier getHostnameVerifier() {
        return mHostnameVerifier;
    }

    /**
     * 说明：获取超时时间
     * @return 超时时间
     */
    public int getTimeout() {
        LogUtils.d("HttpConfig timeout = "+mTimeout);
        return mTimeout;
    }

    /**
     * 说明：是否开启调试
     * @return 是否调试模式
     */
    public boolean getDebug(){
        return mDebug;
    }

    /**
     * 说明：信任所有证书（开发模式使用）
     * @return 是否信任证书
     */
    public boolean getTrustAll(){
        return mTrustAll;
    }

    /**
     * 说明：获取头信息
     * @return Headers
     */
    public Headers getCommonHeader() {
        return mCommonHeader;
    }

    public List<Interceptor> getNetworkInterceptorList() {
        return networkInterceptorList;
    }

    //说明：获取应用拦截器
    public List<Interceptor> getInterceptorList() {
        return interceptorList;
    }

    public boolean getRetryConnectionFailure(){
        return retryConnectionFailure;
    }

    public boolean getRedirects(){
        return redirects;
    }

    public boolean getSslRedirects(){
        return sslRedirects;
    }

    public CookieJar getCookieJar() {
        return cookieJar;
    }

    public Cache getCache() {
        return cache;
    }

    public Authenticator getAuthenticator() {
        return authenticator;
    }

    public CertificatePinner getCertificatePinner() {
        return certificatePinner;
    }

    public Proxy getProxy() {
        return proxy;
    }

    public Dispatcher getDispatcher() {
        return dispatcher;
    }

    /**
     * 说明：修改公共请求参数信息
     * @param key key
     * @param value value
     */
    public void updateCommonParams(String key,String value){
        boolean add = false;
        List<Part> commonParams = getCommonParams();
        if (commonParams != null){
            for (Part part:commonParams){
                if (part != null && TextUtils.equals(part.getKey(),key)){
                    part.setValue(value);
                    add = true;
                    break;
                }
            }
        }
        if (!add){
            commonParams.add(new Part(key,value));
        }
    }

    /**
     * 说明：修改公共header信息
     * @param key key
     * @param value value
     */
    public void updateCommonHeader(String key,String value){
        Headers headers = getCommonHeader();
        if (headers == null){
            headers = new Headers.Builder().build();
        }
        this.mCommonHeader = headers.newBuilder().set(key,value).build();
    }
}
