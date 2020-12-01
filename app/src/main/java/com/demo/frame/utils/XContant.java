package com.demo.frame.utils;

import com.fast.library.utils.SDCardUtils;

import java.io.File;

public class XContant {
    public final static String APP = "Hexin";
    public final static String ENCYPT_KEY = "Hexin2019";
    public final static String NUM_ZERO = "0";
    public final static String NUM_ONE = "1";
    public final static String TOKEN_INVAILED = "重新登录";
    public final static String EMPTY = "";

    //上传图片缓存路径
    public final static String UPLOAD_IMAGE_CACHE = SDCardUtils.getExternalStorage() + File.separator + APP + File.separator;

    public interface Extra {
        String ITEM = "ITEM";
        String TITLE = "TITLE";
        String ID = "ID";
        String SCAN_TYPE = "SCAN_TYPE";
    }

    public interface EventType {
        String UPLOAD_OSS_SUCCESS = "UPLOAD_OSS_SUCCESS"; // 上传OSS成功
        String SINGLE_MESSAGE = "SINGLE_MESSAGE"; // 服务器推送下来的自定义消息，仅仅是 1 或 2 或 3 这样的数字！
    }
}
