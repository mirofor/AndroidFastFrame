package com.demo.frame.helper;

import com.fast.frame.ActivityFrame;
import com.fast.frame.interrface.OnLoadListener;
import com.fast.library.ui.ActivityStack;
import com.fast.library.utils.GsonUtils;
import com.fast.library.utils.StringUtils;
import com.demo.frame.bean.UserBean;
import com.demo.frame.ui.activity.ActivityStartLogin;

public class UserHelper {

    private static String token, mobile="";
    private static UserBean sUserBean;
    private static String deviceId = "";

    public static UserBean getUser() {
        if (sUserBean == null) {
            sUserBean = GsonUtils.toBean(SpHelper.getSp().readString(SpHelper.Key.USER_INFO), UserBean.class);
        }
        return sUserBean;
    }

    public static void loginSuccess(UserBean bean) {
        sUserBean = bean;
        if (bean != null) {
            SpHelper.getSp().write(SpHelper.Key.USER_INFO, GsonUtils.toJson(bean));
        }
    }

    public static String getToken() {

        if (StringUtils.isEmpty(token)) {
            token = SpHelper.getSp().readString(SpHelper.Key.TOKEN);
        }
        return token;
    }

    public static String getDeviceId() {
        if (StringUtils.isEmpty(deviceId)) {
            deviceId = SpHelper.getSp().readString(SpHelper.Key.DEVICE_ID);
        }
        return deviceId;
    }

    public static void saveToken(String s) {
        token = s;
        SpHelper.getSp().write(SpHelper.Key.TOKEN, s);
    }

    public static void savMobile(String s) {
        mobile = s;
        SpHelper.getSp().write(SpHelper.Key.MOBILE, s);
    }
    public static String getMobile() {

        if (StringUtils.isEmpty(mobile)) {
            mobile = SpHelper.getSp().readString(SpHelper.Key.MOBILE);
        }
        return mobile;
    }

    public static void saveUserId(int s) {
        SpHelper.getSp().write(SpHelper.Key.USER_ID, s);
    }

    public static int getUserId() {
        return SpHelper.getSp().readInt(SpHelper.Key.USER_ID, 0);
    }

    public static void saveDeviceId(String id) {
        deviceId = id;
        SpHelper.getSp().write(SpHelper.Key.DEVICE_ID, id);
    }


    public static void loginOut(ActivityFrame activityFrame) {
        token = null;
        mobile = null;
        sUserBean = null;
        SpHelper.getSp().remove(SpHelper.Key.SIGN);
        SpHelper.getSp().remove(SpHelper.Key.USER_INFO);
        SpHelper.getSp().remove(SpHelper.Key.TOKEN);
        SpHelper.getSp().remove(SpHelper.Key.USER_ID);
        SpHelper.getSp().remove(SpHelper.Key.MOBILE);
        RouterHelper.startLoginTip(activityFrame);
        ActivityStack.create().finishOtherActivity(ActivityStartLogin.class);

    }

    public static void appExit() {
        token = null;
        mobile = null;
        sUserBean = null;
        SpHelper.getSp().remove(SpHelper.Key.SIGN);
        SpHelper.getSp().remove(SpHelper.Key.USER_INFO);
        SpHelper.getSp().remove(SpHelper.Key.TOKEN);
        SpHelper.getSp().remove(SpHelper.Key.USER_ID);
        SpHelper.getSp().remove(SpHelper.Key.MOBILE);
        SpHelper.getSp().clear();
        ActivityStack.create().AppExit();
    }

    public static void appExitOther(Class<?> cls) {
        token = null;
        mobile = null;
        sUserBean = null;
        SpHelper.getSp().remove(SpHelper.Key.SIGN);
        SpHelper.getSp().remove(SpHelper.Key.USER_INFO);
        SpHelper.getSp().remove(SpHelper.Key.TOKEN);
        SpHelper.getSp().remove(SpHelper.Key.USER_ID);
        SpHelper.getSp().remove(SpHelper.Key.MOBILE);
        SpHelper.getSp().clear();
        ActivityStack.create().finishOtherActivity(cls);
    }

    public static boolean isLogin() {
        return StringUtils.isNotEmpty(getToken()) && getUser() != null;
    }

    public static String getCityList() {
        return SpHelper.getSp().readString(SpHelper.Key.CITY_LIST_DATA);
    }

    public static void saveCityList(String s) {
        SpHelper.getSp().write(SpHelper.Key.CITY_LIST_DATA, s);
    }

    /**
     * 获取-更新用户资料信息
     *
     * @param listener
     */
    public static void updateUserInfo(final OnLoadListener<UserBean> listener) {
//        Api.get().userInfo(new OnLoadListener<UserBean>() {
//            @Override
//            public void onStart() {
//                if (listener != null) {
//                    listener.onStart();
//                }
//            }
//
//            @Override
//            public void onError(int code, String error) {
//                if (listener != null) {
//                    listener.onError(code, error);
//                }
//            }
//
//            @Override
//            public void onFinish() {
//                if (listener != null) {
//                    listener.onFinish();
//                }
//            }
//
//            @Override
//            public void onSuccess(UserBean userBean) {
//                loginSuccess(userBean);
//                if (listener != null) {
//                    listener.onSuccess(userBean);
//                }
//            }
//        });
    }

}
