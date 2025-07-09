package com.rymcu.forest.auth;

import com.rymcu.forest.util.UserUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;

/**
 * @author ronger
 */
public class BaseHashedCredentialsMatcher extends HashedCredentialsMatcher {


    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        UserUtils.getTokenUser(token.getCredentials().toString());
        return true;
    }
}
