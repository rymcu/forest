/*
 * Copyright (c) 2020 RYMCU (https://rymcu.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */


package com.rymcu.forest.config;

import com.rymcu.forest.util.Utils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Enumeration;

/**
 * WebLogAspect 实现Web层的日志切面
 *
 * @author suwen
 * @date 2020/12/22 9:16 上午
 */
@Aspect
@Component
public class WebLogAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    ThreadLocal<Long> startTime = new ThreadLocal<Long>();

    /**
     * 定义一个切入点. 解释下：
     *
     * <p>~ 第一个 * 代表任意修饰符及任意返回值. ~ 第二个 * 任意包名 ~ 第三个 * 代表任意方法.
     *
     * <p>~ 第四个 * 定义在web包或者子包 ~ 第五个 * 任意方法 ~ .. 匹配任意数量的参数. execution(*
     * xiao.ze.demo.service.impl.*.*(..))
     */
    @Pointcut("execution(* com.rymcu.forest.*.api.*.*.*(..))")
    public void webLog() {
    }

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {

        startTime.set(System.currentTimeMillis());

        // 接收到请求，记录请求内容
        logger.info("WebLogAspect.doBefore()");
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // 记录下请求内容
        logger.info("URL : " + request.getRequestURL().toString());
        logger.info("HTTP_METHOD : " + request.getMethod());
        logger.info("IP : " + Utils.getIpAddress(request));
        logger.info(
                "CLASS_METHOD : "
                        + joinPoint.getSignature().getDeclaringTypeName()
                        + "."
                        + joinPoint.getSignature().getName());
        logger.info("ARGS : " + Arrays.toString(joinPoint.getArgs())
                .replaceAll("(?<=password).*?(?=(nickname|$))", "=****, ")
                .replaceAll("(?<=password).*?(?=(\\)|$))", "=****)]")
                .replaceAll("(?<=password).*?(?=(code|$))", "=****, "));

        // 获取所有参数方法一：
        Enumeration<String> enu = request.getParameterNames();
        while (enu.hasMoreElements()) {
            String paraName = enu.nextElement();
            if ("password".equals(paraName)) {
                continue;
            }
            logger.info(paraName + ": " + request.getParameter(paraName));
        }
    }

    @AfterReturning("webLog()")
    public void doAfterReturning(JoinPoint joinPoint) {

        // 处理完请求，返回内容
        logger.info("WebLogAspect.doAfterReturning()");
        logger.info("耗时（毫秒） : " + (System.currentTimeMillis() - startTime.get()));
    }
}

