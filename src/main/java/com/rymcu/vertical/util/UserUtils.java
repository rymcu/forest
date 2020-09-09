package com.rymcu.vertical.util;

import com.rymcu.vertical.dto.TokenUser;
import com.rymcu.vertical.entity.User;
import com.rymcu.vertical.jwt.def.JwtConstants;
import com.rymcu.vertical.jwt.model.TokenModel;
import com.rymcu.vertical.jwt.service.TokenManager;
import com.rymcu.vertical.mapper.UserMapper;
import com.rymcu.vertical.web.api.exception.ErrorCode;
import com.rymcu.vertical.web.api.exception.BaseApiException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import org.apache.commons.lang.StringUtils;

/**
 * @author ronger
 */
public class UserUtils {

    private static UserMapper userMapper = SpringContextHolder.getBean(UserMapper.class);
    private static TokenManager tokenManager = SpringContextHolder.getBean(TokenManager.class);

    /**
     * 通过token获取当前用户的信息
     * @return
     */
    public static User getCurrentUserByToken() throws BaseApiException {
        String authHeader = ContextHolderUtils.getRequest().getHeader(JwtConstants.AUTHORIZATION);
        if (authHeader == null) {
            return null;
        }
        // 验证token
        Claims claims = null;
        try {
            claims = Jwts.parser().setSigningKey(JwtConstants.JWT_SECRET).parseClaimsJws(authHeader).getBody();
        } catch (final SignatureException e) {
            throw new BaseApiException(ErrorCode.UNAUTHORIZED);
        }
        Object account = claims.getId();
        if (!oConvertUtils.isEmpty(account)) {
            TokenModel model = tokenManager.getToken(authHeader, account.toString());
            if (tokenManager.checkToken(model)) {
                return userMapper.findByAccount(account.toString());
            }
        } else {
            throw new BaseApiException(ErrorCode.UNAUTHORIZED);
        }
        return null;
    }

    public static TokenUser getTokenUser(String token) {
        if(StringUtils.isNotBlank(token)){
            // 验证token
            Claims claims = null;
            try {
                claims = Jwts.parser().setSigningKey(JwtConstants.JWT_SECRET).parseClaimsJws(token).getBody();
            } catch (final SignatureException e) {
                return null;
            }
            Object account = claims.getId();
            if (!oConvertUtils.isEmpty(account)) {
                TokenModel model = tokenManager.getToken(token, account.toString());
                if (tokenManager.checkToken(model)) {
                    User user = userMapper.findByAccount(account.toString());
                    if(user != null){
                        TokenUser tokenUser = new TokenUser();
                        BeanCopierUtil.copy(user, tokenUser);
                        tokenUser.setToken(token);
                        tokenUser.setWeights(userMapper.selectRoleWeightsByUser(user.getIdUser()));
                        return tokenUser;
                    }
                }
            }
        }
        return null;
    }
}
