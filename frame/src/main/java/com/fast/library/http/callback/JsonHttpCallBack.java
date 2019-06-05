package com.fast.library.http.callback;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 说明：返回JSON对象
 */
@Deprecated
public abstract class JsonHttpCallBack extends StringCallBack {

    public abstract void onSuccess(JSONObject result);

    @Override
    public void onSuccess(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            onSuccess(jsonObject);
        }catch (JSONException e){
            e.printStackTrace();
            onFailure(ERROR_RESPONSE_JSON_EXCEPTION,"数据转换异常");
        }
    }
}
