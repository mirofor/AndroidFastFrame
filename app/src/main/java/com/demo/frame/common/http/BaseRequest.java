package com.demo.frame.common.http;

import com.fast.library.http.RequestParams;
import com.fast.library.utils.StringUtils;
import com.demo.frame.helper.UserHelper;

/**
 * 说明：BaseRequest
 */
public abstract class BaseRequest {

    public RequestParams getReqParams() {
        return requestParams;
    }

    private RequestParams requestParams = new RequestParams();
    private String key;
    private String newLng, newlat;

    public BaseRequest() {
    }

    public BaseRequest(String key) {
        this.key = key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public RequestParams getReqeustParams() {
        requestParams.clear();
        requestParams.setHttpTaskKey(StringUtils.isEmpty(key) ? "key" : key);

        // 添加共通header
//        requestParams.putHeader("app_version", AndroidInfoUtils.versionName());
//        requestParams.putHeader("app_terminal_system", "android");

        if (StringUtils.isNotEmpty(UserHelper.getToken())) {
                requestParams.putHeader("Authorization", "Bearer "+UserHelper.getToken());
        }
//        if (UserHelper.getUserId() !=0){
//            requestParams.putHeader("user_id", UserHelper.getUserId());
//        }


        add(requestParams);
//        addParams("imei", AndroidInfoUtils.getTerminalCode());
//        addParams("lng", StringUtils.isNotEmpty(newLng) ? newLng : String.valueOf(LocationHelper.lng));
//        addParams("lat", StringUtils.isNotEmpty(newlat) ? newlat : String.valueOf(LocationHelper.lat));
        return requestParams;
    }

    public void setLocation(double lng, double lat) {
        this.newLng = String.valueOf(lng);
        this.newlat = String.valueOf(lat);
    }

    public abstract void add(RequestParams requestParams);

    public void addParams(String params, String data) {
        if (StringUtils.isNotEmpty(data) && StringUtils.isNotEmpty(params)) {
            requestParams.put(params, data);
        }
    }
}
