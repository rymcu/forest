package com.rymcu.forest.web.api.exception;

import com.rymcu.forest.config.BaseExceptionHandler;
import com.rymcu.forest.core.result.GlobalResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice(basePackages = {"com.rymcu.forest.web.api", "com.rymcu.forest.jwt"} )
public class BaseApiExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(BaseExceptionHandler.class);

    @ExceptionHandler(BaseApiException.class)
    @ResponseBody
    public GlobalResult handlerException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
            logger.error(ex.getMessage());
            GlobalResult result = new GlobalResult();
            if (ex instanceof BaseApiException) {
                result.setCode(((BaseApiException) ex).getCode());
                result.setMessage(ex.getMessage());
            } /*else if (ex instanceof Exception) {
                result.setCode(ErrorCode.INTERNAL_SERVER_ERROR.getCode());
                result.setMessage("用户无权限");
            }*/
            result.setSuccess(false);
            return result;
    }
}
