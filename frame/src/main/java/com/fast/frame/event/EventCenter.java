package com.fast.frame.event;

import com.fast.library.utils.StringUtils;
import java.util.HashMap;

/**
 * 说明：事件消息
 */
public class EventCenter<T>{

    public T data;//消息数据
    public String type;//消息类型
    public String what;//不确定消息类型
    public HashMap<String,Object> dataMap;

    public EventCenter(String type,T t){
        this.type = type;
        this.data = t;
    }

    public EventCenter(String type,String what,T t){
        this.type = type;
        this.data = t;
        this.what = what;
    }

    public void put(String key,Object value){
        if (!StringUtils.isEmpty(key) && value != null){
            if (dataMap == null){
                dataMap = new HashMap<>();
            }
            dataMap.put(key, value);
        }
    }

    public Object get(String key){
        if (!StringUtils.isEmpty(key) && dataMap != null && dataMap.containsKey(key)){
            return dataMap.get(key);
        }else {
            return null;
        }
    }

}
