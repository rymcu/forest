package com.rymcu.forest.core.result;

/**
 * 响应码枚举，参考HTTP状态码的语义
 *
 * @author ronger
 */
public enum ResultCode {
    // 成功
    SUCCESS(1, "SUCCESS"),
    // 失败
    FAIL(400, "访问失败"),
    // 未认证（签名错误）
    UNAUTHENTICATED(401, "签名错误"),
    // 未认证（签名错误）
    UNAUTHORIZED(403, "用户无权限"),
    // 接口不存在
    NOT_FOUND(404, "此接口不存在"),
    // 服务器内部错误
    INTERNAL_SERVER_ERROR(500, "系统繁忙,请稍后再试"),
    // 参数错误
    INVALID_PARAM(10000, "参数错误"),
    // 未知账号
    UNKNOWN_ACCOUNT(10001, "未知账号"),
    // 账号或密码错误
    INCORRECT_ACCOUNT_OR_PASSWORD(10002, "账号或密码错误");
    private final int code;
    private final String message;

    ResultCode(int code, String message) {
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
