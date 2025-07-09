package com.rymcu.forest.util;

import com.rymcu.forest.auth.JwtConstants;
import com.rymcu.forest.auth.TokenManager;
import com.rymcu.forest.auth.TokenModel;
import com.rymcu.forest.dto.TokenUser;
import com.rymcu.forest.entity.User;
import com.rymcu.forest.mapper.UserMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.UnauthenticatedException;

import java.util.Objects;

/**
 * @author ronger
 */
public class UserUtils {

    private static final UserMapper userMapper = SpringContextHolder.getBean(UserMapper.class);
    private static final TokenManager tokenManager = SpringContextHolder.getBean(TokenManager.class);

    /**
     * 通过token获取当前用户的信息
     *
     * @return
     */
    public static User getCurrentUserByToken() {
        String authHeader = ContextHolderUtils.getRequest().getHeader(JwtConstants.AUTHORIZATION);
        if (authHeader == null) {
            throw new UnauthenticatedException();
        }
        // 验证token
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(JwtConstants.JWT_SECRET).parseClaimsJws(authHeader).getBody();
        } catch (final SignatureException e) {
            throw new UnauthenticatedException();
        }
        Object account = claims.getId();
        if (StringUtils.isNotBlank(Objects.toString(account, ""))) {
            TokenModel model = tokenManager.getToken(authHeader, account.toString());
            if (tokenManager.checkToken(model)) {
                User user = userMapper.selectByAccount(account.toString());
                if (Objects.nonNull(user)) {
                    return user;
                }
            }
        }
        throw new UnauthenticatedException();
    }

    public static TokenUser getTokenUser(String token) {
        if (StringUtils.isNotBlank(token)) {
            // 验证token
            Claims claims;
            try {
                claims = Jwts.parser().setSigningKey(JwtConstants.JWT_SECRET).parseClaimsJws(token).getBody();
            } catch (final SignatureException e) {
                throw new UnauthenticatedException();
            }
            Object account = claims.getId();
            if (StringUtils.isNotBlank(Objects.toString(account, ""))) {
                TokenModel model = tokenManager.getToken(token, account.toString());
                if (tokenManager.checkToken(model)) {
                    User user = userMapper.selectByAccount(account.toString());
                    if (Objects.nonNull(user)) {
                        TokenUser tokenUser = new TokenUser();
                        BeanCopierUtil.copy(user, tokenUser);
                        tokenUser.setAccount(user.getEmail());
                        tokenUser.setToken(token);
                        return tokenUser;
                    }
                }
            }
        }
        throw new UnauthenticatedException();
    }

    public static boolean isAdmin(String email) {
        return userMapper.hasAdminPermission(email);
    }
}
