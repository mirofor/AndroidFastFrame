package com.fast.frame.event;

import com.fast.library.utils.LogUtils;
import com.fast.library.utils.StringUtils;
import org.greenrobot.eventbus.EventBus;


/**
 * 说明：EventBus事件中心
 * <p/>
 * 作者：fanly
 * <p/>
 * 类型：Class
 * <p/>
 * 时间：2016/11/15 9:13
 * <p/>
 * 版本：verson 1.0
 */
public class EventUtils {

    /**
     * 注册EventBus
     * @param object
     */
    public static void registerEventBus(Object object){
        if (object != null){
            if (!EventBus.getDefault().isRegistered(object)){
                LogUtils.e("----- EventBus --- start register ----");
                EventBus.getDefault().register(object);
            }
        }
    }

    /**
     * 解除EventBus
     * @param object
     */
    public static void unRegisterEventBus(Object object){
        if (object != null){
            if (EventBus.getDefault().isRegistered(object)){
                EventBus.getDefault().unregister(object);
            }
        }
    }

    /**
     * 判断自己的事件类型
     * @param type
     * @param center
     * @return
     */
    public static boolean isMyEvent(String type,EventCenter center){
        return StringUtils.isEquals(type,center.type);
    }

    /**
     * 发送Event
     * @param eventCenter
     */
    public static void post(EventCenter eventCenter){
        EventBus.getDefault().post(eventCenter);
    }

    /**
     * 发送数据
     * @param type
     * @param t
     * @param <T>
     */
    public static<T> void postData(String type,T t){
        post(new EventCenter<T>(type,t));
    }

    /**
     * 发送Event
     * @param type
     */
    public static void postDefult(String type){
        post(new EventCenter<String>(type,""));
    }

}
