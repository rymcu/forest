package com.rymcu.forest.core.exception;

import org.apache.shiro.authc.AccountException;

/**
 * Created on 2022/8/25 19:27.
 *
 * @author ronger
 * @email ronger-x@outlook.com
 */
public class AccountExistsException extends AccountException {
    private static final long serialVersionUID = 3206734387536223284L;

    public AccountExistsException() {
    }

    public AccountExistsException(String message) {
        super(message);
    }

    public AccountExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccountExistsException(Throwable cause) {
        super(cause);
    }
}
