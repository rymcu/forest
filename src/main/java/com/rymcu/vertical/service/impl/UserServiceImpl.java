package com.rymcu.vertical.service.impl;

import com.rymcu.vertical.core.service.AbstractService;
import com.rymcu.vertical.core.service.redis.RedisService;
import com.rymcu.vertical.dto.TUser;
import com.rymcu.vertical.dto.UserInfoDTO;
import com.rymcu.vertical.entity.Role;
import com.rymcu.vertical.entity.User;
import com.rymcu.vertical.jwt.service.TokenManager;
import com.rymcu.vertical.mapper.RoleMapper;
import com.rymcu.vertical.mapper.UserMapper;
import com.rymcu.vertical.service.UserService;
import com.rymcu.vertical.util.BeanCopierUtil;
import com.rymcu.vertical.util.Utils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by CodeGenerator on 2018/05/29.
 */
@Service
public class UserServiceImpl extends AbstractService<User> implements UserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private RedisService redisService;
    @Resource
    private TokenManager tokenManager;

    @Override
    public User findByAccount(String account) throws TooManyResultsException{
        return userMapper.findByAccount(account);
    }

    @Override
    @Transactional
    public Map register(String email, String password, String code) {
        Map map = new HashMap();
        map.put("message","验证码无效！");
        String vcode = redisService.get(email);
        if(StringUtils.isNotBlank(vcode)){
            if(vcode.equals(code)){
                User user = userMapper.findByAccount(email);
                if(user != null){
                    map.put("message","该邮箱已被注册！");
                } else {
                    user = new User();
                    user.setAccount(email);
                    user.setNickname(email);
                    user.setEmail(email);
                    user.setPassword(Utils.entryptPassword(password));
                    user.setCreatedTime(new Date());
                    user.setUpdatedTime(user.getCreatedTime());
                    userMapper.insertSelective(user);
                    user = userMapper.findByAccount(email);
                    Role role = roleMapper.selectRoleByInputCode("user");
                    userMapper.insertUserRole(user.getIdUser(), role.getIdRole());
                    map.put("message","注册成功！");
                    map.put("flag",1);
                    redisService.delete(email);
                }
            }
        }
        return map;
    }

    @Override
    public Map login(String account, String password) {
        Map map = new HashMap();
        User user = new User();
        user.setAccount(account);
        user = userMapper.selectOne(user);
        if(user != null){
            if(Utils.comparePwd(password, user.getPassword())){
                user.setLastLoginTime(new Date());
                userMapper.updateByPrimaryKeySelective(user);
                TUser tUser = new TUser();
                BeanCopierUtil.copy(user,tUser);
                tUser.setToken(tokenManager.createToken(account));
                map.put("user",tUser);
            } else {
                map.put("message","密码错误！");
            }
        } else {
            map.put("message","该账号不存在！");
        }
        return map;
    }
}
