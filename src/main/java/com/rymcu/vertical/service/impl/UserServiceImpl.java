package com.rymcu.vertical.service.impl;

import com.rymcu.vertical.core.service.AbstractService;
import com.rymcu.vertical.core.service.redis.RedisService;
import com.rymcu.vertical.dto.TokenUser;
import com.rymcu.vertical.dto.UserDTO;
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
 *
 * @author CodeGenerator
 * @date 2018/05/29
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
        Map map = new HashMap(2);
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
                    user.setNickname(email.split("@")[0]);
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
        Map map = new HashMap(1);
        User user = new User();
        user.setAccount(account);
        user = userMapper.selectOne(user);
        if(user != null){
            if(Utils.comparePwd(password, user.getPassword())){
                user.setLastLoginTime(new Date());
                userMapper.updateByPrimaryKeySelective(user);
                TokenUser tokenUser = new TokenUser();
                BeanCopierUtil.copy(user, tokenUser);
                tokenUser.setToken(tokenManager.createToken(account));
                tokenUser.setWeights(userMapper.selectRoleWeightsByUser(user.getIdUser()));
                map.put("user", tokenUser);
            } else {
                map.put("message","密码错误！");
            }
        } else {
            map.put("message","该账号不存在！");
        }
        return map;
    }

    @Override
    public UserDTO findUserDTOByNickname(String nickname) {
        UserDTO user = userMapper.selectUserDTOByNickname(nickname);
        return user;
    }

    @Override
    public Map forgetPassword(String code, String password) {
        Map map = new HashMap<>(2);
        String account = redisService.get(code);
        System.out.println("account:\n"+account);
        if(StringUtils.isBlank(account)){
            map.put("message","链接已失效");
        } else {
          userMapper.updatePasswordByAccount(account,Utils.entryptPassword(password));
          map.put("message","修改成功，正在跳转登录登陆界面！");
          map.put("flag",1);
        }
        return map;
    }

    @Override
    @Transactional
    public Map updateUserRole(Integer idUser, Integer idRole) {
        Map map = new HashMap(1);
        Integer result = userMapper.updateUserRole(idUser,idRole);
        if(result == 0) {
            map.put("message","更新失败!");
        }
        return map;
    }

    @Override
    @Transactional
    public Map updateStatus(Integer idUser, String status) {
        Map map = new HashMap(1);
        Integer result = userMapper.updateStatus(idUser,status);
        if(result == 0) {
            map.put("message","更新失败!");
        }
        return map;
    }
}
