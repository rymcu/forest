package com.rymcu.vertical.util;

import com.rymcu.vertical.entity.User;
import com.rymcu.vertical.jwt.def.JwtConstants;
import com.rymcu.vertical.jwt.model.TokenModel;
import com.rymcu.vertical.jwt.service.TokenManager;
import com.rymcu.vertical.mapper.UserMapper;
import com.rymcu.vertical.web.api.exception.ErrorCode;
import com.rymcu.vertical.web.api.exception.MallApiException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;

public class UserUtils {

    private static UserMapper userMapper = SpringContextHolder.getBean(UserMapper.class);
    private static TokenManager tokenManager = SpringContextHolder.getBean(TokenManager.class);

    /**
     * 通过token获取当前用户的信息
     * @return
     */
    public static User getWxCurrentUser() throws MallApiException {
        String authHeader = ContextHolderUtils.getRequest().getHeader(JwtConstants.AUTHORIZATION);
        if (authHeader == null) {
            return null;
        }
        // 验证token
        Claims claims = null;
        try {
            claims = Jwts.parser().setSigningKey(JwtConstants.JWT_SECRET).parseClaimsJws(authHeader).getBody();
        } catch (final SignatureException e) {

        }
        Object account = claims.getId();
        if (!oConvertUtils.isEmpty(account)) {
            TokenModel model = tokenManager.getToken(authHeader, account.toString());
            if (tokenManager.checkToken(model)) {
                return userMapper.findByAccount(account.toString());
            }
        } else {
            throw new MallApiException(ErrorCode.UNAUTHORIZED);
        }
        return null;
    }
}
