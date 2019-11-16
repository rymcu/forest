package com.rymcu.vertical.core.result;

import lombok.Data;

@Data
public class GlobalResult<T> {
    private boolean success = false;
    private T data;
    private int code;
    private String message;

    public GlobalResult() {
    }

    public static  <T> GlobalResult<T> newInstance() {
        return new GlobalResult();
    }

}
