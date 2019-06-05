package com.demo.frame.pay;

import com.fast.library.bean.Pojo;
/**
 * 说明：WxResponseBean
 */
public class WxResponseBean extends Pojo {

    /**
     * appId : wx094a07b6da1db0d0
     * timeStamp : 1504678158
     * nonceStr : cVeAll0FsXe98tZvz75dWqnh0mc7yNsb
     * signType : MD5
     * package : Sign=WXPay
     * paySign : A4B1003D7B03D994B240C2236010916D
     */
    public String appId;
    public String timeStamp;
    public String nonceStr;
    public String packageInfo;
    public String signType;
    public String partnerId;
    public String prepayId;

//    public PayReq getPayReq() {
//        PayReq req = new PayReq();
//        req.appId = appId;
//        req.partnerId = partnerId;
//        req.prepayId = prepayId;
//        req.packageValue = packageInfo;
//        req.nonceStr = nonceStr;
//        req.timeStamp = timeStamp;
//        req.sign = signType;
//        return req;
//    }
//
//    public static PayReq create(String s) {
//        WxResponseBean wxResponseBean = new WxResponseBean().toBean(s);
//        if (wxResponseBean != null) {
//            return wxResponseBean.getPayReq();
//        }
//        return null;
//    }
}
