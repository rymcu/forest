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


package com.rymcu.forest.auth;

import com.rymcu.forest.dto.TokenUser;
import com.rymcu.forest.entity.Role;
import com.rymcu.forest.entity.User;
import com.rymcu.forest.service.RoleService;
import com.rymcu.forest.util.UserUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created on 2022/10/27 20:04.
 *
 * @author ronger
 * @email ronger-x@outlook.com
 * @desc : com.rymcu.forest.auth
 */
public class JwtRealm extends AuthorizingRealm {
    @Resource
    private RoleService roleService;

    @Resource
    private TokenManager manager;


    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof TokenModel;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String accessToken = (String) principals.getPrimaryPrincipal();
        TokenUser tokenUser = UserUtils.getTokenUser(accessToken);
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        User user = new User();
        user.setIdUser(tokenUser.getIdUser());
        try {
            List<Role> roles = roleService.selectRoleByUser(user);
            for (Role role : roles) {
                if (StringUtils.isNotBlank(role.getInputCode())) {
                    authorizationInfo.addRole(role.getInputCode());
                    authorizationInfo.addStringPermission(role.getInputCode());
                }
            }
            // 添加用户权限
            authorizationInfo.addStringPermission("user");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return authorizationInfo;
    }

    /**
     * 认证回调函数, 登录时调用，主要是用来进行身份认证的，也就是说验证用户输入的账号和密码是否正确。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authToken) throws AuthenticationException {
        TokenModel token = (TokenModel) authToken;
        if (!manager.checkToken(token)) {
            throw new UnauthorizedException();
        }
        return new SimpleAuthenticationInfo(token.getPrincipal(), token.getCredentials(), this.getName());
    }
}