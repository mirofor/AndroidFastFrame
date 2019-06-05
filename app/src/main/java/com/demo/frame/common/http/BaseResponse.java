package com.demo.frame.common.http;

/**
 * 说明：BaseResponse
 */
public class BaseResponse {
    public int status;
    public int code;
    public String msg;
    public boolean success;
//    public boolean isSuccess(){
//        return status == Api.ErrorCode.ok;
//    }
    public static final String MESSAGE="message";
    public boolean isSuccess(){
        return success;
    }
}
