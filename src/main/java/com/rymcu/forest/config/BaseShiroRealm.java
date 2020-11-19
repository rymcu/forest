package com.rymcu.forest.config;

import com.rymcu.forest.core.constant.ShiroConstants;
import com.rymcu.forest.core.exception.CaptchaException;
import com.rymcu.forest.entity.Permission;
import com.rymcu.forest.entity.Role;
import com.rymcu.forest.entity.User;
import com.rymcu.forest.service.PermissionService;
import com.rymcu.forest.service.RoleService;
import com.rymcu.forest.service.UserService;
import com.rymcu.forest.util.Encodes;
import com.rymcu.forest.util.Utils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * @author ronger
 * @since 2018/05/28 11:00
 * 自定义权限匹配和账号密码匹配
 * */
public class BaseShiroRealm extends AuthorizingRealm {
    @Resource
    private RoleService roleService;

    @Resource
    private PermissionService permissionService;

    @Resource
    private UserService userService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        Principal principal =  (Principal)principals.getPrimaryPrincipal();
        User user = new User();
        user.setIdUser(principal.getId());
        try {
            List<Role> roles = roleService.selectRoleByUser(user);
            for (Role role : roles) {
                if(StringUtils.isNotBlank(role.getInputCode())){
                    authorizationInfo.addRole(role.getInputCode());
                }
            }
            List<Permission> permissions = permissionService.selectPermissionByUser(user);
            for (Permission perm : permissions) {
                if (perm.getPermissionCategory() != null) {
                    authorizationInfo.addStringPermission(perm.getPermissionCategory());
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
     * */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        //获取用户的输入的账号.
        String username = token.getUsername();

        User user = null;
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (!org.springframework.util.StringUtils.isEmpty(attributes.getRequest().getAttribute(ShiroConstants.CURRENT_CAPTCHA))) {
            throw new CaptchaException();
        }
        try {
            user = userService.findByAccount(username);
        } catch (TooManyResultsException e) {
            e.printStackTrace();
        }
        if (user == null) {
            return null;
        }
        if (!"0".equals(user.getStatus())) { //账户冻结(是否允许登陆)
            throw new LockedAccountException();
        }
        byte[] salt = Encodes.decodeHex(user.getPassword().substring(0,16));
        return new SimpleAuthenticationInfo(new Principal(user, token.isMobileLogin()),
                user.getPassword().substring(16), ByteSource.Util.bytes(salt), getName());
    }

    /**
     * 授权用户信息
     */
    public static class Principal  implements Serializable {

        private static final long serialVersionUID = 1L;

        private Integer id; // 编号
        private String account; // 登录名
        private String name; // 姓名
        private boolean mobileLogin; // 是否手机登录

//		private Map<String, Object> cacheMap;

        public Principal(User user, boolean mobileLogin) {
            this.id = user.getIdUser();
            this.account = user.getAccount();
            this.name = user.getNickname();
            this.mobileLogin = mobileLogin;
        }

        public Integer getId() {
            return id;
        }

        public String getAccount() {
            return account;
        }

        public String getName() {
            return name;
        }

        public boolean isMobileLogin() {
            return mobileLogin;
        }

        /**
         * 获取SESSIONID
         */
        public String getSessionid() {
            try{
                return (String) Utils.getSession().getId();
            }catch (Exception e) {
                return "";
            }
        }

        @Override
        public String toString() {
            return id.toString();
        }

    }
}
