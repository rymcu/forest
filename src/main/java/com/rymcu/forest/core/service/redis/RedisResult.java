package com.rymcu.forest.core.service.redis;

import com.rymcu.forest.entity.BaseDO;

import java.util.List;

/**
 * redis 中取得的结果
 * Created by liwei on 2017/2/7.
 */
public class RedisResult<T> extends BaseDO {

    /**
     * redis中是否存在
     */
    private boolean exist = false;

    /**
     * redis中取得的数据
     */
    private T result;

    /**
     * redis中取得的List数据
     */
    private List<T> listResult;
    /**
     * redis中的key是否存在。true:表示redis中存在Key,但对应的值为空值标记
     */
    private boolean keyExists = false;
    /**
     * redis中key 对应在对象值
     */
    private T resultObj;


    public boolean isExist() {
        return exist;
    }

    public void setExist(boolean exist) {
        this.exist = exist;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public List<T> getListResult() {
        return listResult;
    }

    public void setListResult(List<T> listResult) {
        this.listResult = listResult;
    }

    public void setKeyExists(boolean keyExists) {
        this.keyExists = keyExists;
    }

    public boolean isKeyExists() {
        return keyExists;
    }

    public T getResultObj() {
        return resultObj;
    }

    public void setResultObj(T resultObj) {
        this.resultObj = resultObj;
    }
}
