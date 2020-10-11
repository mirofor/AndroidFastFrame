package com.fast.library.ui;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.fast.library.utils.StringUtils;

/**
 * 说明：WebViewLoader
 */
public class WebViewLoader {

    private WebView webView;
    private Adapter mAdapter;

    public interface Adapter {
        boolean shouldOverrideUrlLoading(WebView view, String url);

        void onProgressChanged(WebView view, int newProgress);
    }

    public WebViewLoader(WebView webView, Adapter adapter) {
        this.webView = webView;
        this.mAdapter = adapter;
    }

    public void init() {
        if (webView == null) {
            return;
        }
        WebSettings settings = webView.getSettings();
        //支持获取手势焦点
        webView.requestFocusFromTouch();
        //支持JS
        settings.setJavaScriptEnabled(true);
        //支持插件
        settings.setPluginState(WebSettings.PluginState.ON);
        //设置适应屏幕
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        //支持缩放
        settings.setSupportZoom(false);
        //隐藏原生的缩放控件
        settings.setDisplayZoomControls(false);
        //支持内容重新布局
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.supportMultipleWindows();
        //设置缓存模式
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setAppCacheEnabled(true);
        settings.setAppCachePath(webView.getContext().getCacheDir().getAbsolutePath());
        //设置可访问文件
        settings.setAllowFileAccess(true);
        //当webview调用requestFocus时为webview设置节点
        settings.setNeedInitialFocus(true);
        //支持自动加载图片
        settings.setNeedInitialFocus(true);
        //设置编码格式
        settings.setDefaultTextEncodingName("UTF-8");
        //设置WebViewClient
        webView.setWebViewClient(mWebViewClient);
        //设置WebChromeClient
        webView.setWebChromeClient(mWebChromeClient);
    }

    @SuppressLint("JavascriptInterface")
    public void addJavascriptInterface(Object object, String name) {
        if (webView != null) {
            //设置js回调
            webView.addJavascriptInterface(object, name);
        }
    }

    private WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (mAdapter != null) {
                mAdapter.onProgressChanged(view, newProgress);
            }
        }
    };

    /**
     * 说明：加载网页
     */
    private WebViewClient mWebViewClient = new WebViewClient() {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (mAdapter != null) {
                return mAdapter.shouldOverrideUrlLoading(view, url);
            }
            return super.shouldOverrideUrlLoading(view, url);
        }

        /**
         * 说明：网页开始加载
         * @param view
         * @param url
         * @param favicon
         */
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
        }

        /**
         * 说明：网页加载完成
         * @param view
         * @param url
         */
        @Override
        public void onPageFinished(WebView view, String url) {
        }

        /**
         * 说明：网页加载失败
         * @param view
         * @param errorCode
         * @param description
         * @param failingUrl
         */
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }
    };

    /**
     * 加载网页
     *
     * @param url
     */
    public void loadUrl(final String url) {
        if (!StringUtils.isEmpty(url)) {
            webView.post(new Runnable() {
                @Override
                public void run() {
                    webView.loadUrl(url);
                }
            });
        }
    }

    public void onDestroy() {
        webView = null;
        mAdapter = null;
    }
}
