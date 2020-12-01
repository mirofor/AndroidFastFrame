package com.fast.library.http;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 说明：Http请求辅助类
 * @author xiaomi
 */
public class HttpTaskHandler {

    private static Map<String,List<HttpTask>> taskMap;
    //请求处理器为单例
    private static HttpTaskHandler handler = null;
    private static Object lock = new Object();

    private HttpTaskHandler(){
        taskMap = new ConcurrentHashMap<>();
    }

    public static HttpTaskHandler getInstance(){
        if (handler == null){
            synchronized (lock){
                if (handler == null){
                    handler = new HttpTaskHandler();
                }
            }
        }
        return handler;
    }

    /**
     * 说明：移除key
     * @param key
     */
    public void removeTask(String key){
        if (taskMap != null && taskMap.containsKey(key)){
            List<HttpTask> tasks = taskMap.get(key);
            if (tasks != null && tasks.size() > 0){
                for (HttpTask task:tasks){
                    if (!task.isCancelled()){
                        task.cancel(true);
                    }
                }
            }
            taskMap.remove(key);
        }
    }

    /**
     * 说明：将请求放到map中
     * @param key
     * @param task
     */
    public void addTask(String key,HttpTask task){
        List<HttpTask> tasks = null;
        if (taskMap.containsKey(key)){
            tasks = taskMap.get(key);
            tasks.add(task);
            taskMap.put(key,tasks);
        }else{
            tasks = new ArrayList<>();
            tasks.add(task);
            taskMap.put(key, tasks);
        }
    }

    /**
     * 说明：判断key是否存在
     * @param key
     * @return
     */
    public boolean contains(String key){
        return taskMap.containsKey(key);
    }
}
