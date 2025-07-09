package com.rymcu.forest.auth;

import com.alibaba.fastjson2.JSONObject;
import com.rymcu.forest.core.result.GlobalResult;
import com.rymcu.forest.core.result.GlobalResultGenerator;
import com.rymcu.forest.core.result.ResultCode;
import com.rymcu.forest.util.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * Created on 2022/10/27 19:58.
 *
 * @author ronger
 * @email ronger-x@outlook.com
 * @desc : com.rymcu.forest.auth
 */
@Slf4j
public class JwtFilter extends BasicHttpAuthenticationFilter {
    /**
     * 判断用户是否想要登入。
     * 检测 header 里面是否包含 Authorization 字段即可
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String authorization = httpServletRequest.getHeader(JwtConstants.AUTHORIZATION);
        if (StringUtils.isBlank(authorization)) {
            // 编辑器上传文件使用 X-Upload-Token 请求头传递 token
            authorization = httpServletRequest.getHeader(JwtConstants.UPLOAD_TOKEN);
        }
        return authorization != null;
    }

    /**
     *
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String authorization = httpServletRequest.getHeader(JwtConstants.AUTHORIZATION);
        if (StringUtils.isBlank(authorization)) {
            // 编辑器上传文件使用 X-Upload-Token 请求头传递 token
            authorization = httpServletRequest.getHeader(JwtConstants.UPLOAD_TOKEN);
        }
        // 验证token
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(JwtConstants.JWT_SECRET).parseClaimsJws(authorization).getBody();
        } catch (final SignatureException e) {
            throw new UnauthenticatedException();
        }
        Object username = claims.getId();
        if (Objects.isNull(username)) {
            throw new UnauthenticatedException();
        }
        TokenModel token = new TokenModel(username.toString(), authorization);
        // 提交给realm进行登入，如果错误他会抛出异常并被捕获
        getSubject(request, response).login(token);
        // 如果没有抛出异常则代表登入成功，返回true
        return true;
    }

    /**
     * 这里我们详细说明下为什么最终返回的都是true，即允许访问
     * 例如我们提供一个地址 GET /article
     * 登入用户和游客看到的内容是不同的
     * 如果在这里返回了false，请求会被直接拦截，用户看不到任何东西
     * 所以我们在这里返回true，Controller中可以通过 subject.isAuthenticated() 来判断用户是否登入
     * 如果有些资源只有登入用户才能访问，我们只需要在方法上面加上 @RequiresAuthentication 注解即可
     * 但是这样做有一个缺点，就是不能够对GET,POST等请求进行分别过滤鉴权(因为我们重写了官方的方法)，但实际上对应用影响不大
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) {
        if (isLoginAttempt(request, response)) {
            try {
                return executeLogin(request, response);
            } catch (Exception e) {
                onLoginFail(response);
            }
        }
        onLoginFail(response);
        return false;
    }

    /**
     * 对跨域提供支持
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control" +
                "-Request-Headers"));
        // 跨域时会首先发送一个 option 请求，这里我们给 option 请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }

    /**
     * 将非法请求跳转到 /401
     */
    private void onLoginFail(ServletResponse response) {
        try {
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.setContentType("application/json");
            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.getOutputStream().write(JSONObject.toJSONString(new GlobalResult<>(ResultCode.UNAUTHENTICATED)).getBytes());
            httpServletResponse.getOutputStream().flush();
        } catch (IOException e) {
            // 错误日志
            log.error(e.getMessage());
        }
    }
}
