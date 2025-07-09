package com.rymcu.forest.core.exception;

import org.apache.shiro.authc.AuthenticationException;

/**
 * 验证码错误异常类
 *
 * @author ronger
 */
public class CaptchaException extends AuthenticationException {
    private static final long serialVersionUID = 1L;

    public CaptchaException() {
        super("验证码不正确");
    }
}
