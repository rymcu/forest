package com.rymcu.vertical.web.api.exception;

public enum ErrorCode {

    UNAUTHORIZED(401, "请求要求用户的身份认证"),//未认证（签名错误）
    INVALID_TOKEN(402, "TOKEN验证失败，无效的TOKEN！"),
    TOKEN_(402, "TOKEN验证失败，无效的TOKEN！"),
    NOT_FOUND(404, "此接口不存在"),//接口不存在
    INTERNAL_SERVER_ERROR(500, "服务内部异常");

    private int code;
    private String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }


    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}