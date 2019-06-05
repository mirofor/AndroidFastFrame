package com.demo.frame.oss;


import com.demo.frame.helper.UserHelper;
import com.fast.library.utils.StringUtils;

public class OSSConfig {
    // 访问的endpoint地址-1bdc
//    public static final String accessKeyId = "LTAI3r4cVyIuD7oK";
//    public static final String accessKeySecret = "vT2C2ZfeYRqe8EHzslomym99TmTs57";
//    public static final String imgFilePath = "https://1bike-yy.oss-cn-shanghai.aliyuncs.com/";

    // 访问的endpoint地址-1bdc
    public static final String accessKeyId = "LTAILHiNLQ8e0OIH";
    public static final String accessKeySecret = "vaosvsdpHZjotMGGWbJXrK16O6NaQ7";
    public static final String endpoint = "http://oss-cn-shanghai.aliyuncs.com";
    public static final String bucket = "wyzn";

    public static final String imgFilePath = "https://wyzn.oss-cn-shanghai.aliyuncs.com/";

    public static String getImgFilePath() {
        return imgFilePath + uploadObject;
    }

    public static String getImgFilePath(String path) {
        return imgFilePath + path;
    }

    public static final String imgEndpoint = "http://img-cn-shanghai.aliyuncs.com";
    //callback 测试地址
    public static final String callbackAddress = "http://oss-demo.aliyuncs.com:23450";
    // STS 鉴权服务器地址，使用前请参照文档 https://help.aliyun.com/document_detail/31920.html 介绍配置STS 鉴权服务器地址。
    public static final String STSSERVER = "http://*.*.*.*:****/sts/getsts";//STS 地址

    public static String uploadFilePath = ""; //本地文件上传地址
    public static String uploadObject = "";
    public static String downloadObject = "";

    public static final int DOWNLOAD_SUC = 1;
    public static final int DOWNLOAD_Fail = 2;
    public static final int UPLOAD_SUC = 3;
    public static final int UPLOAD_Fail = 4;
    public static final int UPLOAD_PROGRESS = 5;
    public static final int LIST_SUC = 6;
    public static final int HEAD_SUC = 7;
    public static final int RESUMABLE_SUC = 8;
    public static final int SIGN_SUC = 9;
    public static final int BUCKET_SUC = 10;
    public static final int GET_STS_SUC = 11;
    public static final int MULTIPART_SUC = 12;
    public static final int STS_TOKEN_SUC = 13;
    public static final int FAIL = 9999;
    public static final int REQUESTCODE_AUTH = 10111;
    public static final int REQUESTCODE_LOCALPHOTOS = 10112;

    public static final int MESSAGE_UPLOAD_2_OSS = 10002;

    public static String getOssFile(String path, String fileName) {
        if (UserHelper.getMobile().isEmpty()) {
            return "wyGps/" + System.currentTimeMillis() + ".png";
        }

        if (StringUtils.isNotEmpty(path)) {
            return "wyGps/" + UserHelper.getMobile() + "/" + path + "/" + fileName + ".png";
        }
        return "wyGps/" + UserHelper.getMobile() + "/" + fileName + ".png";
    }
}
