package com.fast.library.tools;

import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 说明： * Performs tasks using worker threads. It also allows tasks to be scheduled to be
 * run at future dates. This class mimics relevant methods in both
 * {@link ExecutorService} and {@link Timer}. Any {@link TimerTask} that's
 * scheduled to be run in the future will automatically be run using the thread
 * executor's thread pool. This means that the standard restriction that TimerTasks
 * should run quickly does not apply.
 */
public class TaskEngine {

    private static TaskEngine instance = null;
    private Timer timer;
    private ExecutorService executorService;
    private Map<TimerTask,TimerTaskWrapper> taskWrapperMap = new ConcurrentHashMap<>();

    private TaskEngine(){
        timer =  new Timer("timer-spark",true);
        executorService = Executors.newCachedThreadPool(new ThreadFactory() {
            final AtomicInteger threadNumber = new AtomicInteger(1);
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(Thread.currentThread().getThreadGroup(),r,"pool-spark"+threadNumber.getAndIncrement(),0);
                thread.setDaemon(true);
                if (thread.getPriority() != Thread.NORM_PRIORITY){
                    thread.setPriority(Thread.NORM_PRIORITY);
                }
                return thread;
            }
        });
    }

    public static TaskEngine getInstance(){
        synchronized (TaskEngine.class){
            if (instance == null){
                instance = new TaskEngine();
            }
        }
        return instance;
    }

    public ExecutorService getExecutorService(){
        return executorService;
    }

    public Future<?> submit(Runnable task){
        return executorService.submit(task);
    }

    public void schedule(TimerTask task,long delay){
        timer.schedule(new TimerTaskWrapper(task),delay);
    }

    public void schedule(TimerTask task, Date time){
        timer.schedule(new TimerTaskWrapper(task),time);
    }

    public void schedule(TimerTask task,long delay,long period){
        TimerTaskWrapper taskWrapper = new TimerTaskWrapper(task);
        taskWrapperMap.put(task,taskWrapper);
        timer.schedule(taskWrapper,delay,period);
    }

    public void cancelScheduledTask(TimerTask task){
        TimerTask timerTask = taskWrapperMap.remove(task);
        if (timerTask != null){
            timerTask.cancel();
        }
    }

    public void shutdown(){
        if (executorService != null){
            executorService.shutdownNow();
            executorService = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void close(){
        instance = null;
    }

    private class TimerTaskWrapper extends TimerTask{

        private TimerTask timerTask;

        public TimerTaskWrapper(TimerTask timerTask){
            this.timerTask = timerTask;
        }

        @Override
        public void run() {
            executorService.submit(timerTask);
        }
    }
}
