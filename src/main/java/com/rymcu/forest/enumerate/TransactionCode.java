package com.rymcu.forest.enumerate;

/**
 * @author ronger
 */
public enum TransactionCode {

    INSUFFICIENT_BALANCE(901, "余额不足"),
    UNKNOWN_ACCOUNT(902, "账号不存在");

    private int code;

    private String message;

    TransactionCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public int getCode() {
        return this.code;
    }
}
