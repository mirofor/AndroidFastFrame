package com.fast.library.http;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.fast.library.http.callback.BaseHttpCallBack;
import com.fast.library.http.callback.JsonHttpCallBack;
import com.fast.library.http.callback.ModelCallBack;
import com.fast.library.http.callback.StringCallBack;
import com.fast.library.utils.FrameConstant;
import com.fast.library.utils.GsonUtils;
import com.fast.library.utils.LogUtils;
import com.fast.library.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.SocketTimeoutException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 说明：Http请求
 * @author xiaomi
 */
public class HttpTask extends AsyncTask<Void, Long, ResponseData> {

    private String method;
    private String url;
    private RequestParams params;
    private BaseHttpCallBack callback;
    private String requestKey;
    private Headers headers;
    private OkHttpClient okHttpClient;
    private boolean debug;
    private int timeout ;

    public HttpTask(String method, String url, RequestParams params, BaseHttpCallBack callback, int timeout) {
        this.method = method;
        this.url = url;
        this.params = params;
        this.callback = callback;
        this.timeout = timeout;
        if (params == null) {
            this.params = new RequestParams();
        }
        this.requestKey = this.params.getHttpTaskKey();
        if (StringUtils.isEmpty(requestKey)) {
            requestKey = FrameConstant.Http.DEFAULT_KEY;
        }
        okHttpClient = HttpConfig.get().getOkHttpClient();
        debug = HttpConfig.get().getDebug();
        //将请求的url及参数组合成一个唯一请求
        HttpTaskHandler.getInstance().addTask(this.requestKey, this);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (params.headers != null) {
            this.headers = params.headers.build();
        }
        if (callback != null) {
            callback.onStart();
        }
    }

    @Override
    protected ResponseData doInBackground(Void... voids) {
        Response response = null;
        ResponseData responseData = new ResponseData();
        try {
            String srcUrl = url;
            //构建请求Request实例
            Request.Builder builder = new Request.Builder();
            switch (method) {
                case FrameConstant.Http.GET:
                    url = UrlUtils.getFullUrl(url, params.getFormParams(), params.isUrlEncoder());
                    builder.get();
                    break;
                case FrameConstant.Http.POST:
                    RequestBody body = params.getRequestBody();
                    if (body != null) {
                        builder.post(new ProgressRequestBody(body, this));
                    }
                    break;
                case FrameConstant.Http.PUT:
                case FrameConstant.Http.PATCH:
                    RequestBody bodyPut = params.getRequestBody();
                    if (bodyPut != null) {
                        builder.put(new ProgressRequestBody(bodyPut, this));
                    }
                    break;
                case FrameConstant.Http.DELETE:
                    url = UrlUtils.getFullUrl(url, params.getFormParams(), params.isUrlEncoder());
                    builder.delete();
                    break;
                case FrameConstant.Http.HEAD:
                    url = UrlUtils.getFullUrl(url, params.getFormParams(), params.isUrlEncoder());
                    builder.head();
                    break;
                default:
                    break;
            }
            if (params.cacheControl != null) {
                builder.cacheControl(params.cacheControl);
            }
            builder.url(url).tag(srcUrl).headers(headers);
            Request request = builder.build();
            if (debug) {
                LogUtils.i("Request Headers : \n"+ headers.toString());
                LogUtils.i(method + "╔════════════════请求数据中════════════════╗\n");
                if (StringUtils.isEquals(method, FrameConstant.Http.POST)) {
                    if (params.getRequestBody() instanceof FormBody) {
                        StringBuilder sb = new StringBuilder();
                        FormBody body = (FormBody) params.getRequestBody();
                        for (int i = 0; i < body.size(); i++) {
                            sb.append(body.encodedName(i) + "=" + body.encodedValue(i) + ",");
                        }
                        sb.delete(sb.length() - 1, sb.length());
                        LogUtils.i("POST {" + sb.toString() + "}");
                    }
                    LogUtils.i("url=" + UrlUtils.getFullUrl(url, params.getFormParams(), params.isUrlEncoder()));
                }else {
                    LogUtils.i("url=" +url);
                }
            }

            Call call = okHttpClient.newCall(request);
            OkHttpCallManager.getInstance().addCall(url, call);
            //执行请求
            response = call.execute();
        } catch (Exception e) {
            if (debug) {
                LogUtils.e(e);
            }
            if (e instanceof SocketTimeoutException ) {
                responseData.setTimeout(true);
            } else if (e instanceof InterruptedIOException && TextUtils.equals(e.getMessage(),
                    "timeout")) {
                responseData.setTimeout(true);
            }
        }

        //处理获取的请求结果
        if (response != null) {
            responseData.setResponseNull(false);
            responseData.setCode(response.code());
            responseData.setMessage(response.message());
            responseData.setSuccess(response.isSuccessful());
            String respBody = "";
            try {
                respBody = response.body().string();
            } catch (IOException e) {
                if (debug) {
                    LogUtils.e(e);
                }
            }
            responseData.setResponse(respBody);
            responseData.setHeaders(response.headers());
        } else {
            responseData.setResponseNull(true);
        }
        return responseData;
    }

    protected void updateProgress(int progress, long networkSpeed, int done) {
        publishProgress((long) progress, networkSpeed, (long) done);
    }

    @Override
    protected void onProgressUpdate(Long... values) {
        super.onProgressUpdate(values);
        if (callback != null) {
            long progress = values[0];
            long speed = values[1];
            boolean done = values[2] == 1L;
//            if (debug) {
//                LogUtils.v("当前进度：" + progress + " ,当前网速：" + speed + " ,完成：" + values[2] + " ,是否完成：" + (done ? "完成" : "未完成"));
//            }
            callback.onProgress((int) progress, values[1], done);
        }
    }

    @Override
    protected void onPostExecute(ResponseData responseData) {
        super.onPostExecute(responseData);
        OkHttpCallManager.getInstance().removeCall(url);
        //判断请求是否在这个集合中
        if (!HttpTaskHandler.getInstance().contains(requestKey)) {
            return;
        }
        if (callback != null) {
            callback.setResponseHeaders(responseData.getHeaders());
            callback.onResponse(responseData.getHttpResponse(), responseData.getResponse(), responseData.getHeaders());
            callback.onResponse(responseData.getResponse(), responseData.getHeaders());
        }
        //请求得到响应
        if (!responseData.isResponseNull()) {
            if (responseData.isSuccess()) {
                String respBody = responseData.getResponse();
                if (debug) {
                    Headers headers = responseData.getHeaders();
                    if (headers != null) {
                        LogUtils.i("\n" + respBody + "\n" );
                        LogUtils.i(method + "╚════════════════返回数据════════════════╝ \n");
                        LogUtils.i(" \n \n");
                    }
                }
                parseResponseBody(responseData, callback);
            } else {//请求失败
                int code = responseData.getCode();
                String msg = responseData.getMessage();
                if (debug) {
                    LogUtils.e("url=" + url + "\n response failure code=" + code + "\n msg=" + msg);
                }
                if (code == 504) {
                    if (callback != null) {
                        callback.onFailure(StringCallBack.ERROR_RESPONSE_TIMEOUT,
                                "网络连接超时");
                    }
                } else {
                    if (callback != null) {
                        callback.onFailure(code, msg);
                    }
                }
            }
        } else {//请求无响应
            if (responseData.isTimeout()) {
                if (callback != null) {
                    callback.onFailure(StringCallBack.ERROR_RESPONSE_TIMEOUT,
                            "网络连接超时");
                }
            } else {
                if (debug) {
                    LogUtils.e("url=" + url + "\n response empty");
                }
                if (callback != null) {
                    callback.onFailure(StringCallBack.ERROR_RESPONSE_UNKNOWN, "网络连接异常");
                }
            }
        }

        if (callback != null) {
            callback.onFinish();
        }
    }

    /**
     * 说明：解析响应数据
     *
     * @param responseData 请求的response
     * @param callback     请求回调
     */
    private void parseResponseBody(ResponseData responseData, BaseHttpCallBack callback) {
        //回调为空，不向下执行
        if (callback == null) {
            return;
        }

        String result = responseData.getResponse();

        if (StringUtils.isEmpty(result)) {
            callback.onFailure(StringCallBack.ERROR_RESPONSE_NULL, "数据请求为空");
        } else {
            if (debug) {
                LogUtils.d(result);
            }
            if (callback instanceof StringCallBack) {
                callback.onSuccess(responseData.getHeaders(), result);
                callback.onSuccess(result);
            } else if (callback instanceof ModelCallBack) {
                try {
                    Object obj = GsonUtils.toBean(result, callback.getClazz());
                    if (obj != null) {
                        callback.onSuccess(responseData.getHeaders(), result);
                        callback.onSuccess(obj);
                    } else {
                        callback.onFailure(BaseHttpCallBack.ERROR_RESPONSE_JSON_EXCEPTION, "数据解析错误");
                    }
                } catch (Exception e) {
                    LogUtils.e(e);
                    callback.onFailure(BaseHttpCallBack.ERROR_RESPONSE_JSON_EXCEPTION, "数据解析错误");
                }
            } else if (callback instanceof JsonHttpCallBack) {
                try {
                    JsonHttpCallBack jsonHttpCallBack = (JsonHttpCallBack) callback;
                    jsonHttpCallBack.onSuccess(responseData.getHeaders(), result);
                    jsonHttpCallBack.onSuccess(new JSONObject(result));
                } catch (JSONException e) {
                    LogUtils.e(e);
                    callback.onFailure(BaseHttpCallBack.ERROR_RESPONSE_JSON_EXCEPTION, "数据解析错误");
                }
            } else if (callback instanceof BaseHttpCallBack) {
                callback.onSuccess(responseData.getHeaders(), result);
                callback.onSuccess(result);
            }
        }
    }

    /**
     * 说明：获取请求的url
     *
     * @return
     */
    public String getUrl() {
        return url;
    }
}
