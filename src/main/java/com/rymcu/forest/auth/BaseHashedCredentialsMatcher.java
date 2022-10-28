package com.rymcu.forest.auth;

import com.rymcu.forest.dto.TokenUser;
import com.rymcu.forest.util.UserUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.UnauthenticatedException;

import java.util.Objects;

/**
 * @author ronger
 */
public class BaseHashedCredentialsMatcher extends HashedCredentialsMatcher {

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        TokenModel tokenModel = (TokenModel) token;
        String accessToken = (String) tokenModel.getCredentials();
        if (StringUtils.isBlank(accessToken)) {
            throw new UnauthenticatedException();
        }
        TokenUser tokenUser = UserUtils.getTokenUser(accessToken);
        if (Objects.isNull(tokenUser)) {
            throw new UnauthenticatedException();
        }
        return true;
    }
}
