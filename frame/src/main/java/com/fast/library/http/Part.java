package com.fast.library.http;

import com.fast.library.utils.StringUtils;

/**
 * 说明：包装参数
 * @author xiaomi
 */
public final class Part {

    private String key;
    private String value;
    private FileWrapper fileWrapper;

    public Part(String key,String value){
        setKey(key);
        setValue(value);
    }

    public Part(String key,FileWrapper fileWrapper){
        setKey(key);
        setFileWrapper(fileWrapper);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public FileWrapper getFileWrapper() {
        return fileWrapper;
    }

    public void setFileWrapper(FileWrapper fileWrapper) {
        this.fileWrapper = fileWrapper;
    }

    @Override
    public boolean equals(Object o) {
        if (o==null || !(o instanceof Part)){
            return false;
        }
        Part part1 = (Part)o;
        if (part1 == null){
            return false;
        }
        if (StringUtils.isEquals(part1.getKey(), getKey()) && StringUtils.isEquals(part1.getValue(),getValue())){
            return true;
        }
        return false;
    }
}
