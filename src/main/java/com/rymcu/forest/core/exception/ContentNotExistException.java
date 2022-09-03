package com.rymcu.forest.core.exception;

/**
 * @author KKould
 */
public class ContentNotExistException extends BusinessException {
    private static final long serialVersionUID = 3206734387536223284L;

    public ContentNotExistException() {
    }

    public ContentNotExistException(String message) {
        super(message);
    }

    public ContentNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public ContentNotExistException(Throwable cause) {
        super(cause);
    }

    public ContentNotExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
