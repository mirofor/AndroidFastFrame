package com.fast.library.http;

import android.text.TextUtils;

import com.fast.library.utils.FileUtils;
import com.fast.library.utils.LogUtils;
import com.fast.library.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.CacheControl;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 说明：Http请求参数
 */
public class RequestParams {

    protected final Headers.Builder headers = new Headers.Builder();
    private final List<Part> mParams = new ArrayList<>();
    private final List<Part> mFiles = new ArrayList<>();

    protected HttpTaskKey taskKey;
    private String httpTaskKey;
    private RequestBody requestBody;
    private boolean applicationJson;
    private boolean urlEncoder;//是否进行URL编码
    private String jsonParams;//json参数
    protected CacheControl cacheControl;

    public RequestParams(){
        this("");
    }

    public RequestParams(final String key){
        HttpTaskKey httpTaskKey = new HttpTaskKey() {
            @Override
            public String getHttpTaskKey() {
                return key;
            }
        };
        this.taskKey = httpTaskKey;
        init();
    }

    public RequestParams(HttpTaskKey key){
        this.taskKey = key;
        init();
    }

    public void setHttpTaskKey(final String key){
        HttpTaskKey httpTaskKey = new HttpTaskKey() {
            @Override
            public String getHttpTaskKey() {
                return key;
            }
        };
        this.taskKey = httpTaskKey;
        if (taskKey != null){
            this.httpTaskKey = taskKey.getHttpTaskKey();
        }
    }

    private void init(){
        headers.add("charset", "UTF-8");
        if (taskKey != null){
            this.httpTaskKey = taskKey.getHttpTaskKey();
        }
        //添加公共header
        Headers commonHeaders = HttpConfig.get().getCommonHeader();
        if (commonHeaders != null && commonHeaders.size() > 0){
            for (int i = 0; i < commonHeaders.size(); i++){
                headers.add(commonHeaders.name(i),commonHeaders.value(i));
            }
        }
    }

    public String getHttpTaskKey(){
        return httpTaskKey;
    }

    public boolean isEmpty(){
        return mFiles.isEmpty() && mParams.isEmpty();
    }

    public void put(String key,String value){
        if (value == null){
            value = "";
        }
        Part part = new Part(key,value);
        if (!StringUtils.isEmpty(key) && !mParams.contains(part)){
            mParams.add(part);
        }
    }

    public void put(String key,int value){
        put(key,String.valueOf(value));
    }

    public void put(String key,boolean value){
        put(key,String.valueOf(value));
    }

    public void put(String key,float value){
        put(key,String.valueOf(value));
    }

    public void put(String key,double value){
        put(key,String.valueOf(value));
    }

    public void put(String key,File file){
        if (file == null || !file.exists() || file.length() == 0){
            return;
        }
        boolean isPng = FileUtils.isFileType(file, "png");
        if (isPng){
            put(key,file,ContentType.PNG.getContentType());
            return;
        }
        boolean isJpg = FileUtils.isFileType(file, "jpg");
        if (isJpg){
            put(key,file,ContentType.JPEG.getContentType());
            return;
        }
        if (!isPng && !isJpg){
            put(key,new FileWrapper(file,null));
        }
    }

    public void put(String key,File file,String contentType){
        if (checkFile(file)){
            MediaType mediaType = null;
            try {
                mediaType = MediaType.parse(contentType);
            }catch (Exception e){
                LogUtils.e(e);
            }
            put(key,new FileWrapper(file,mediaType));
        }
    }

    public void put(String key,File file, MediaType mediaType){
        if (checkFile(file)){
            put(key,new FileWrapper(file,mediaType));
        }
    }

    public void putFiles(String key,List<File> files){
        if (files != null){
            for (File file:files){
                if (checkFile(file)){
                    put(key,file);
                }
            }
        }
    }

    public void put(String key,FileWrapper fileWrapper){
        if (!StringUtils.isEmpty(key) && fileWrapper != null){
            File file = fileWrapper.getFile();
            if (checkFile(file)){
                mFiles.add(new Part(key,fileWrapper));
            }
        }
    }

    public void putPart(String key,List<FileWrapper> fileWrappers){
        if (fileWrappers != null){
            for (FileWrapper fileWrappers1:fileWrappers){
                put(key,fileWrappers1);
            }
        }
    }

    public void putPart(List<Part> parts){
        if (parts != null && !parts.isEmpty()){
            this.mParams.addAll(parts);
        }
    }

    /*************************************Header****************************************/

    public void putHeader(String line){
        headers.add(line);
    }

    public void putHeader(String key,String value){
        if (value == null){
            value = "";
        }
        if (!TextUtils.isEmpty(key)){
            headers.add(key, value);
        }
    }

    public void putHeader(String key,int value){
        putHeader(key,String.valueOf(value));
    }

    public void putHeader(String key,float value){
        putHeader(key,String.valueOf(value));
    }

    public void putHeader(String key,double value){
        putHeader(key,String.valueOf(value));
    }

    public void putHeader(String key,boolean value){
        putHeader(key, String.valueOf(value));
    }

    /**
     * 说明：URL编码，只对GET,DELETE,HEAD有效
     */
    public void urlEncoder(){
        urlEncoder = true;
    }

    public boolean isUrlEncoder(){
        return urlEncoder;
    }

    public void setCacheControl(CacheControl cacheControl){
        this.cacheControl = cacheControl;
    }

    public void setApplicationJson(String jsonParams){
        this.applicationJson = true;
        this.jsonParams = jsonParams;
    }

    public void isApplicationJson(){
        applicationJson = true;
    }
    public void isNotApplicationJson(){
        applicationJson = false;
    }

    public void setCustomRequestBody(RequestBody requestBody){
        this.requestBody = requestBody;
    }

    public void setRequestBody(String string){
        setRequestBody(ContentType.TEXT.getContentType(), string);
    }

    public void setRequestBody(String mediaType,String string){
        setRequestBody(MediaType.parse(mediaType), string);
    }

    public void setRequestBody(MediaType mediaType,String string){
        setCustomRequestBody(RequestBody.create(mediaType, string));
    }

    public void clear(){
        mParams.clear();
        mFiles.clear();
    }

    public List<Part> getFormParams(){
        return mParams;
    }

    public RequestBody getRequestBody(){
        RequestBody body = null;
        if (applicationJson){
            String json = "";
            if (jsonParams == null){
                JSONObject jsonObject = new JSONObject();
                for (Part part:mParams){
                    try {
                        jsonObject.put(part.getKey(),part.getValue());
                    }catch (JSONException e){
                        LogUtils.e(e);
                    }
                }
                json = jsonObject.toString();
            }else {
                try {
                    json = new JSONObject(jsonParams).toString();
                }catch (JSONException e){
                    LogUtils.e(e);
                }
            }
            LogUtils.d("post RequestBody :"+json);
            body = RequestBody.create(MediaType.parse(ContentType.JSON.getContentType()),json);
        }else if (requestBody != null){
            body = requestBody;
        }else if (mFiles.size() > 0){
            boolean hasData = false;
            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);
            for (Part part:mParams){
                builder.addFormDataPart(part.getKey(),part.getValue());
                hasData = true;
            }
            for (Part part:mFiles){
                FileWrapper file = part.getFileWrapper();
                if (file != null){
                    builder.addFormDataPart(part.getKey(),file.getFileName(),RequestBody.create(file.getMediaType(),file.getFile()));
                    hasData = true;
                }
            }
            if (hasData){
                body = builder.build();
            }
        }else {
            FormBody.Builder builder = new FormBody.Builder();
            boolean hasData = false;
            for (Part part:mParams){
                builder.add(part.getKey(),part.getValue());
                hasData =true;
            }
            if (hasData){
                body = builder.build();
            }
        }
        return body;
    }

    private boolean checkFile(File file){
        if (file == null || !file.exists() || file.length() == 0 || !file.isFile()){
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Part part:mParams){
            String key = part.getKey();
            String value = part.getValue();
            if (result.length() > 0)
                result.append("&");

            result.append(key);
            result.append("=");
            result.append(value);
        }

        for (Part part:mFiles){
            String key = part.getKey();
            if (result.length() > 0)
                result.append("&");

            result.append(key);
            result.append("=");
            result.append("FILE");
        }

        if(jsonParams != null) {
            result.append(jsonParams.toString());
        }
        return result.toString();
    }

}
