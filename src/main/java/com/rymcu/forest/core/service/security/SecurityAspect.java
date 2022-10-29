package com.rymcu.forest.core.service.security;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rymcu.forest.auth.JwtConstants;
import com.rymcu.forest.dto.TokenUser;
import com.rymcu.forest.util.UserUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 检查用户修改信息权限
 *
 * @author ronger
 */
@Aspect
@Component
public class SecurityAspect {

    Logger logger = LoggerFactory.getLogger(SecurityAspect.class);

    @Pointcut("@annotation(com.rymcu.forest.core.service.security.annotation.SecurityInterceptor)")
    public void securityPointCut() {
    }

    /**
     * 检查用户修改信息权限
     *
     * @param joinPoint 连接点
     * @return 方法执行结果
     * @throws Throwable 调用出错
     */
    @Before(value = "securityPointCut()")
    public void doBefore(JoinPoint joinPoint) {
        logger.info("检查用户修改信息权限 start ...");
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String idUser = "";
        if (isAjax(request)) {
            Object[] objects = joinPoint.getArgs();
            JSONObject jsonObject;
            if (objects[0] instanceof Integer) {
                idUser = objects[0].toString();
            } else {
                jsonObject = JSONObject.parseObject(JSON.toJSONString(objects[0]));
                if (Objects.nonNull(jsonObject)) {
                    idUser = jsonObject.getString("idUser");
                }
            }
        } else {
            Map params = getParams(request);
            if (params.isEmpty()) {
                params = (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            } else {
                params.putAll((Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE));
            }
            idUser = (String) params.get("idUser");
        }
        if (Objects.nonNull(idUser)) {
            String authHeader = request.getHeader(JwtConstants.AUTHORIZATION);
            if (StringUtils.isNotBlank(authHeader)) {
                TokenUser tokenUser = UserUtils.getTokenUser(authHeader);
                if (!idUser.equals(tokenUser.getIdUser().toString())) {
                    throw new UnauthorizedException();
                }
            }
        } else {
            throw new UnauthenticatedException();
        }
        logger.info("检查用户修改信息权限 end ...");
    }

    private Map<String, String> getParams(HttpServletRequest request) {
        Map<String, String> paramsMap = new HashMap<>(10);
        Enumeration<String> paraNames = request.getParameterNames();
        while (paraNames.hasMoreElements()) {
            String key = paraNames.nextElement();
            if ("password".equals(key)) {
                continue;
            }
            paramsMap.put(key, request.getParameter(key));
        }
        return paramsMap;
    }

    private boolean isAjax(HttpServletRequest request) {
        String requestedWith = request.getHeader("x-requested-with");
        if (requestedWith != null && "XMLHttpRequest".equalsIgnoreCase(requestedWith)) {
            return true;
        }
        String contentType = request.getContentType();
        return StringUtils.isNotBlank(contentType) && contentType.contains("application/json");
    }
}
