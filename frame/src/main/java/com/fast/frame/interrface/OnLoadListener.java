package com.fast.frame.interrface;

public abstract class OnLoadListener<T>{
    public void onStart(){}
    public void onFinish(){}
    public abstract void onSuccess(T t);
    public void onError(int code,String error){}
    public void onErrorExit(int code,String error){}
    public boolean showToastError(){
        return true;
    }
}
