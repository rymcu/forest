package com.rymcu.forest.core.service.security;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rymcu.forest.auth.JwtConstants;
import com.rymcu.forest.core.exception.BusinessException;
import com.rymcu.forest.core.service.security.annotation.AuthorshipInterceptor;
import com.rymcu.forest.dto.TokenUser;
import com.rymcu.forest.entity.Article;
import com.rymcu.forest.entity.Portfolio;
import com.rymcu.forest.enumerate.Module;
import com.rymcu.forest.mapper.UserMapper;
import com.rymcu.forest.service.ArticleService;
import com.rymcu.forest.service.PortfolioService;
import com.rymcu.forest.util.UserUtils;
import org.apache.commons.lang.StringUtils;
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

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
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
public class AuthorshipAspect {

    Logger logger = LoggerFactory.getLogger(AuthorshipAspect.class);

    @Pointcut("@annotation(com.rymcu.forest.core.service.security.annotation.AuthorshipInterceptor)")
    public void authorshipPointCut() {
    }

    @Resource
    private ArticleService articleService;
    @Resource
    private PortfolioService portfolioService;
    @Resource
    private UserMapper userMapper;

    /**
     * 检查用户修改信息权限
     *
     * @param joinPoint 连接点
     * @return 方法执行结果
     * @throws Throwable 调用出错
     */
    @Before(value = "authorshipPointCut()")
    public void doBefore(JoinPoint joinPoint) {
        logger.info("检查作者身份 start ...");
        String methodName = joinPoint.getSignature().getName();
        Method method = currentMethod(joinPoint, methodName);
        AuthorshipInterceptor log = method.getAnnotation(AuthorshipInterceptor.class);
        if (Objects.nonNull(log)) {
            boolean isArticle = true;
            if (Module.PORTFOLIO.equals(log.moduleName())) {
                isArticle = false;
            }
            HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
            String id;
            Long idAuthor = 0L;
            if (isAjax(request)) {
                Object[] objects = joinPoint.getArgs();
                JSONObject jsonObject;
                if (objects[0] instanceof Integer) {
                    jsonObject = new JSONObject();
                    if (isArticle) {
                        jsonObject.put("idArticle", objects[0].toString());
                    } else {
                        jsonObject.put("idPortfolio", objects[0].toString());
                    }
                } else {
                    jsonObject = JSONObject.parseObject(JSON.toJSONString(objects[0]));
                }
                if (Objects.nonNull(jsonObject)) {
                    if (isArticle) {
                        id = jsonObject.getString("idArticle");
                        Article article = articleService.findById(id);
                        if (Objects.nonNull(article)) {
                            idAuthor = article.getArticleAuthorId();
                        }
                    } else {
                        id = jsonObject.getString("idPortfolio");
                        Portfolio portfolio = portfolioService.findById(id);
                        if (Objects.nonNull(portfolio)) {
                            idAuthor = portfolio.getPortfolioAuthorId();
                        }
                    }
                }
            } else {
                Map params = getParams(request);
                if (params.isEmpty()) {
                    params = (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
                } else {
                    params.putAll((Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE));
                }
                if (isArticle) {
                    id = (String) params.get("idArticle");
                    Article article = articleService.findById(id);
                    if (Objects.nonNull(article)) {
                        idAuthor = article.getArticleAuthorId();
                    }
                } else {
                    id = (String) params.get("idPortfolio");
                    Portfolio portfolio = portfolioService.findById(id);
                    if (Objects.nonNull(portfolio)) {
                        idAuthor = portfolio.getPortfolioAuthorId();
                    }
                }
            }
            if (idAuthor > 0) {
                String authHeader = request.getHeader(JwtConstants.AUTHORIZATION);
                if (StringUtils.isNotBlank(authHeader)) {
                    TokenUser tokenUser = UserUtils.getTokenUser(authHeader);
                    if (!idAuthor.equals(tokenUser.getIdUser())) {
                        boolean hasPermission = false;
                        if (Module.ARTICLE_TAG.equals(log.moduleName())) {
                            // 判断管理员权限
                            hasPermission = userMapper.hasAdminPermission(tokenUser.getAccount());
                        }
                        if (!hasPermission) {
                            throw new UnauthorizedException();
                        }
                    }
                }
            } else {
                throw new BusinessException("参数异常");
            }
        }
        logger.info("检查作者身份 end ...");
    }

    /**
     * 获取当前执行的方法
     *
     * @param joinPoint  连接点
     * @param methodName 方法名称
     * @return 方法
     */
    private Method currentMethod(JoinPoint joinPoint, String methodName) {
        /**
         * 获取目标类的所有方法，找到当前要执行的方法
         */
        Method[] methods = joinPoint.getTarget().getClass().getMethods();
        Method resultMethod = null;
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                resultMethod = method;
                break;
            }
        }
        return resultMethod;
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
