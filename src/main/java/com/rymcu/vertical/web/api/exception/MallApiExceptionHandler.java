package com.rymcu.vertical.web.api.exception;

import com.rymcu.vertical.config.HpeisExceptionHandler;
import com.rymcu.vertical.core.result.GlobalResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice(basePackages = {"com.rymcu.vertical.web.api", "com.rymcu.vertical.jwt"} )
public class MallApiExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(HpeisExceptionHandler.class);

    @ExceptionHandler(MallApiException.class)
    @ResponseBody
    public GlobalResult handlerException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
            logger.error(ex.getMessage());
            GlobalResult result = new GlobalResult();
            if (ex instanceof MallApiException) {
                result.setCode(((MallApiException) ex).getCode());
                result.setMessage(ex.getMessage());
            } /*else if (ex instanceof Exception) {
                result.setCode(ErrorCode.INTERNAL_SERVER_ERROR.getCode());
                result.setMessage("用户无权限");
            }*/
            result.setSuccess(false);
            return result;
    }
}
