package com.fast.library.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.fast.library.FastFrame;

import java.util.HashMap;
import java.util.Map;

/**
 * 说明：SharedPreferences操作工具类
 * @author xiaomi
 */
public final class SPUtils {

    private static Map<String,SharedPreferences> spMap = new HashMap<>();
    private static Map<String,SPUtils> spUtilsMap = new HashMap<>();
    private String fileName;

    /**
     * 说明：禁止实例化
     */
    private SPUtils(String name) {
        fileName = name;
    }

    public static SPUtils getInstance(String fileName) {
        SPUtils spUtils;
        if (TextUtils.isEmpty(fileName)){
            new RuntimeException("SPUtils this fileName is null");
        }
        if (spMap.get(fileName) == null) {
            spUtils = new SPUtils(fileName);
            spMap.put(fileName, FastFrame.getApplication().getSharedPreferences(fileName,
                    Context.MODE_PRIVATE));
            spUtilsMap.put(fileName,spUtils);
        }else {
            spUtils = spUtilsMap.get(fileName);
        }
        return spUtils;
    }

    /*********************写方法*********************************/

    /**
     * 说明：long
     * @param key
     * @param value
     */
    public void write(String key, long value){
        write(key,String.valueOf(value));
    }

    /**
     * 说明：int
     * @param key
     * @param value
     */
    public void write(String key, int value) {
        write(key,String.valueOf(value));
    }

    /**
     * 说明：boolean
     * @param key
     * @param value
     */
    public void write(String key, boolean value) {
        write(key, String.valueOf(value));
    }

    /**
     * 说明：String
     * @param key
     * @param value
     */
    public void write(String key, String value) {
        spMap.get(fileName).edit().putString(encodeKey(key), encodeValue(value)).apply();
    }

    /*********************读方法*********************************/

    /**
     * 说明：读int
     * @param key
     * @return
     */
    public int readInt(String key) {
        return readInt(key, 0);
    }

    /**
     * 说明：读int
     * @param key
     * @return
     */
    public int readInt(String key, int defaultValue) {
        return Integer.parseInt(readString(key, String.valueOf(defaultValue)));
    }

    /**
     * 说明：读boolean
     * @param key
     * @return
     */
    public boolean readBoolean(String key) {
        return readBoolean(key, false);
    }

    /**
     * 说明：读boolean
     * @param key
     * @param defaultBoolean
     * @return
     */
    public boolean readBoolean(String key,
                               boolean defaultBoolean) {
        return Boolean.parseBoolean(readString(key, String.valueOf(defaultBoolean)));
    }

    /**
     * 说明：读long
     * @param key
     * @return
     */
    public long readLong(String key){
        return readLong(key, 0);
    }

    /**
     * 说明：读long
     * @param key
     * @param defalut
     * @return
     */
    public long readLong(String key,long defalut){
        return Long.parseLong(readString(key, String.valueOf(defalut)));
    }

    /**
     * 说明：读String
     * @param key
     * @return
     */
    public String readString(String key) {
        return readString(key, "");
    }

    /**
     * 说明：读String
     * @param key
     * @param defaultValue
     * @return
     */
    public String readString(String key,String defaultValue) {
        String text = spMap.get(fileName).getString(encodeKey(key), null);
        if (!TextUtils.isEmpty(text)){
            return decodeValue(text);
        }else {
            return defaultValue;
        }
    }

    /**
     * 说明：删除
     * @param key
     */
    public void remove(String key) {
        spMap.get(fileName).edit().remove(encodeKey(key)).remove(key).apply();
    }

    /**
     * 说明：清空
     */
    public void clear() {
        spMap.get(fileName).edit().clear().apply();
    }

    private String encodeKey(String key){
        if (TextUtils.isEmpty(key)) {
            return "";
        }else {
            return Base64.encode(key.getBytes());
        }
    }

    /**
     * 说明：内容编码
     * @param value
     * @return
     */
    private String encodeValue(String value){
        if (!TextUtils.isEmpty(value)) {
            try{
                value = Base64.encode(value.getBytes());
                value = DESUtils.encryption(value, MD5.getMD5(AndroidInfoUtils.getAndroidId()).substring(0, 8));
            }catch (Exception e){
                LogUtils.e(e);
                value = "";
            }

        }
        return value;
    }

    /**
     * 说明：内容解密
     * @param value
     * @return
     */
    private String decodeValue(String value){
        if (!TextUtils.isEmpty(value)) {
            try{
                value = DESUtils.decrypt(value, MD5.getMD5(AndroidInfoUtils.getAndroidId()).substring(0, 8));
                value = new String(Base64.decode(value),"UTF-8");
            }catch (Exception e){
                LogUtils.e(e);
                value = "";
            }
        }
        return value;
    }
}