package com.rymcu.forest.jwt.aop;


import com.rymcu.forest.jwt.def.JwtConstants;
import com.rymcu.forest.jwt.model.TokenModel;
import com.rymcu.forest.jwt.service.TokenManager;
import com.rymcu.forest.web.api.exception.BaseApiException;
import com.rymcu.forest.web.api.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * Restful请求， Token校验规则拦截器（JWT）
 * 
 * @author scott
 * @date 2015/7/30.
 */
public class RestAuthTokenInterceptor implements HandlerInterceptor {

	@Autowired
	private TokenManager manager;

	@Override
	public void afterCompletion(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse, Object obj, Exception exception) throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception {
		
		//从header中得到token
		String authHeader = request.getHeader(JwtConstants.AUTHORIZATION);
		if(StringUtils.isBlank(authHeader)){
			authHeader = request.getHeader(JwtConstants.UPLOAD_TOKEN);
		}
		if (StringUtils.isBlank(authHeader)) {
            throw new BaseApiException(ErrorCode.UNAUTHORIZED);
        }
		// 验证token
		Claims claims = null;
		try {
		    claims = Jwts.parser().setSigningKey(JwtConstants.JWT_SECRET).parseClaimsJws(authHeader).getBody();
		}catch (final SignatureException e) {
			throw new BaseApiException(ErrorCode.INVALID_TOKEN);
		}
		
		Object username = claims.getId();
		if (Objects.isNull(username)) {
			throw new BaseApiException(ErrorCode.INVALID_TOKEN);
        }
		TokenModel model = manager.getToken(authHeader,username.toString());
		if (manager.checkToken(model)) {
			//如果token验证成功，将对象传递给下一个请求
            request.setAttribute(JwtConstants.CURRENT_TOKEN_CLAIMS, claims);
			//如果token验证成功，将token对应的用户id存在request中，便于之后注入
			request.setAttribute(JwtConstants.CURRENT_USER_NAME, model.getUsername());
			return true;
		} else {
			throw new BaseApiException(ErrorCode.TOKEN_);
		}
	}

	@Override
	public void postHandle(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse, Object obj, ModelAndView modelandview) throws Exception {
		// TODO Auto-generated method stub
	}

}
