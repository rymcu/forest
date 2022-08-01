package com.rymcu.forest.core.exception;

/**
 * @author KKould
 */
public class DataDuplicationException extends RuntimeException {

    private static final long serialVersionUID = 3206744387536223284L;

    public DataDuplicationException() {
    }

    public DataDuplicationException(String message) {
        super(message);
    }

    public DataDuplicationException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataDuplicationException(Throwable cause) {
        super(cause);
    }

    public DataDuplicationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
