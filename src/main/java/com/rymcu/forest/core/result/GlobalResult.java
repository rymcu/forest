package com.rymcu.forest.core.result;

import lombok.Data;

/**
 * @author ronger
 */
@Data
public class GlobalResult<T> {
    private boolean success = false;
    private T data;
    private int code;
    private String message;

    public GlobalResult() {
    }

    public GlobalResult(ResultCode resultCode) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }

    public static <T> GlobalResult<T> newInstance() {
        return new GlobalResult<>();
    }

}
