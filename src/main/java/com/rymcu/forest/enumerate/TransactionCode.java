package com.rymcu.forest.enumerate;

/**
 * @author ronger
 */
public enum TransactionCode {

    InsufficientBalance(901, "余额不足");

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
