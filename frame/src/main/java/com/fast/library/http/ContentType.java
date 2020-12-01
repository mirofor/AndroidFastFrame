package com.fast.library.http;

/**
 * 说明：文件内容类型
 * @author xiaomi
 */
public enum  ContentType {

    /**
     * text
     */
    TEXT("text/plain; charset=UTF-8"),
    /**
     * 图片
     */
    PNG("image/png; charset=UTF-8"),
    JPEG("image/jpeg; charset=UTF-8"),
    /**
     * JSON
     */
    JSON("application/json; charset=UTF-8");

    private String contentType;

    ContentType(String contentType){
        this.contentType = contentType;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
