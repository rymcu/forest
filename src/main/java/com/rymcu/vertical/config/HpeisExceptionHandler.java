package com.rymcu.vertical.config;

import com.alibaba.fastjson.support.spring.FastJsonJsonView;
import com.rymcu.vertical.core.exception.ServiceException;
import com.rymcu.vertical.core.result.GlobalResult;
import com.rymcu.vertical.core.result.ResultCode;
import com.rymcu.vertical.web.api.exception.MallApiException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理器
 * */
@RestControllerAdvice
public class HpeisExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(HpeisExceptionHandler.class);

    @SuppressWarnings("Duplicates")
    @ExceptionHandler(Exception.class)
    public Object errorHandler(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex){
        if(isAjax(request)){
            GlobalResult result = new GlobalResult();
            if (ex instanceof MallApiException){
                result.setCode(401);
                result.setMessage("用户未登录");
                logger.info("用户未登录");
            } else if (ex instanceof UnauthenticatedException) {
                result.setCode(1000001);
                result.setMessage("token错误");
                logger.info("token错误");
            } else if (ex instanceof UnauthorizedException) {
                result.setCode(1000002);
                result.setMessage("用户无权限");
                logger.info("用户无权限");
            }else if (ex instanceof ServiceException) {//业务失败的异常，如“账号或密码错误”
                result.setCode(((ServiceException) ex).getCode());
                result.setMessage(ex.getMessage());
                logger.info(ex.getMessage());
            } else if (ex instanceof NoHandlerFoundException) {
                result.setCode(ResultCode.NOT_FOUND.getCode());
                result.setMessage(ResultCode.NOT_FOUND.getMessage());
            } else if (ex instanceof ServletException) {
                result.setCode(ResultCode.FAIL.getCode());
                result.setMessage(ex.getMessage());
            }else {
                //系统内部异常,不返回给客户端,内部记录错误日志
                result.setCode(ResultCode.INTERNAL_SERVER_ERROR.getCode());
                String message;
                if (handler instanceof HandlerMethod) {
                    HandlerMethod handlerMethod = (HandlerMethod) handler;
                    message = String.format("接口 [%s] 出现异常，方法：%s.%s，异常摘要：%s",
                            request.getRequestURI(),
                            handlerMethod.getBean().getClass().getName(),
                            handlerMethod.getMethod().getName(),
                            ex.getMessage());
                } else {
                    message = ex.getMessage();
                }
                result.setMessage("操作失败");
                logger.error(message, ex);
            }
            result.setSuccess(false);
            return result;
        }else {
            ModelAndView mv = new ModelAndView();
            FastJsonJsonView view = new FastJsonJsonView();
            Map<String, Object> attributes = new HashMap();
            if (ex instanceof MallApiException){
                attributes.put("code", "401");
                attributes.put("message", "用户未登录");
            } else if (ex instanceof UnauthenticatedException) {
                attributes.put("code", "1000001");
                attributes.put("message", "token错误");
            } else if (ex instanceof UnauthorizedException) {
                attributes.put("code", "1000002");
                attributes.put("message", "用户无权限");
            } else if (ex instanceof ServiceException) {//业务失败的异常，如“账号或密码错误”
                attributes.put("code",((ServiceException) ex).getCode());
                attributes.put("message",ex.getMessage());
                logger.info(ex.getMessage());
            } else if (ex instanceof NoHandlerFoundException) {
                attributes.put("code",ResultCode.NOT_FOUND.getCode());
                attributes.put("message",ResultCode.NOT_FOUND.getMessage());
            } else if (ex instanceof ServletException) {
                attributes.put("code",ResultCode.FAIL.getCode());
                attributes.put("message",ex.getMessage());
            }else {
                //系统内部异常,不返回给客户端,内部记录错误日志
                attributes.put("code",ResultCode.INTERNAL_SERVER_ERROR.getCode());
                String message;
                if (handler instanceof HandlerMethod) {
                    HandlerMethod handlerMethod = (HandlerMethod) handler;
                    message = String.format("接口 [%s] 出现异常，方法：%s.%s，异常摘要：%s",
                            request.getRequestURI(),
                            handlerMethod.getBean().getClass().getName(),
                            handlerMethod.getMethod().getName(),
                            ex.getMessage());
                } else {
                    message = ex.getMessage();
                }
                logger.error(message, ex);
                attributes.put("message","操作失败");
            }
            attributes.put("success",false);
            view.setAttributesMap(attributes);
            mv.setView(view);
            return mv;
        }
    }

    private boolean isAjax(HttpServletRequest request) {
        String requestedWith = request.getHeader("x-requested-with");
        if (requestedWith != null && requestedWith.equalsIgnoreCase("XMLHttpRequest")) {
            return true;
        } else {
            return false;
        }
    }
}
