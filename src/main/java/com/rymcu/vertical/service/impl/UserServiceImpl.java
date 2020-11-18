package com.rymcu.vertical.service.impl;

import com.rymcu.vertical.core.service.AbstractService;
import com.rymcu.vertical.core.service.redis.RedisService;
import com.rymcu.vertical.dto.*;
import com.rymcu.vertical.entity.Role;
import com.rymcu.vertical.entity.User;
import com.rymcu.vertical.entity.UserExtend;
import com.rymcu.vertical.jwt.service.TokenManager;
import com.rymcu.vertical.mapper.RoleMapper;
import com.rymcu.vertical.mapper.UserExtendMapper;
import com.rymcu.vertical.mapper.UserMapper;
import com.rymcu.vertical.service.UserService;
import com.rymcu.vertical.util.BeanCopierUtil;
import com.rymcu.vertical.util.Utils;
import com.rymcu.vertical.web.api.common.UploadController;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


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
    @Resource
    private UserExtendMapper userExtendMapper;

    private final static String AVATAR_SVG_TYPE = "1";
    private final static String DEFAULT_AVATAR = "https://static.rymcu.com/article/1578475481946.png";

    @Override
    public User findByAccount(String account) throws TooManyResultsException{
        return userMapper.findByAccount(account);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map register(String email, String password, String code) {
        Map map = new HashMap(2);
        map.put("message","验证码无效！");
        String vCode = redisService.get(email);
        if(StringUtils.isNotBlank(vCode)){
            if(vCode.equals(code)){
                User user = userMapper.findByAccount(email);
                if(user != null){
                    map.put("message","该邮箱已被注册！");
                } else {
                    user = new User();
                    String nickname = email.split("@")[0];
                    nickname = checkNickname(nickname);
                    user.setNickname(nickname);
                    user.setAccount(nickname);
                    user.setEmail(email);
                    user.setPassword(Utils.entryptPassword(password));
                    user.setCreatedTime(new Date());
                    user.setUpdatedTime(user.getCreatedTime());
                    user.setAvatarUrl(DEFAULT_AVATAR);
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

    private String checkNickname(String nickname) {
        nickname = formatNickname(nickname);
        Integer result = userMapper.selectCountByNickName(nickname);
        if (result > 0) {
            StringBuilder stringBuilder = new StringBuilder(nickname);
            return checkNickname(stringBuilder.append("_").append(System.currentTimeMillis()).toString());
        }
        return nickname;
    }

    @Override
    public Map login(String account, String password) {
        Map map = new HashMap(1);
        User user = userMapper.findByAccount(account);
        if(user != null){
            if(Utils.comparePwd(password, user.getPassword())){
                userMapper.updateLastLoginTime(user.getIdUser());
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
    @Transactional(rollbackFor = Exception.class)
    public Map updateUserRole(Integer idUser, Integer idRole) {
        Map map = new HashMap(1);
        Integer result = userMapper.updateUserRole(idUser,idRole);
        if(result == 0) {
            map.put("message","更新失败!");
        }
        return map;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map updateStatus(Integer idUser, String status) {
        Map map = new HashMap(1);
        Integer result = userMapper.updateStatus(idUser,status);
        if(result == 0) {
            map.put("message","更新失败!");
        }
        return map;
    }

    @Override
    public Map findUserInfo(Integer idUser) {
        Map map = new HashMap(1);
        UserInfoDTO user = userMapper.selectUserInfo(idUser);
        if (user == null) {
            map.put("message", "用户不存在!");
        } else {
            UserExtend userExtend = userExtendMapper.selectByPrimaryKey(user.getIdUser());
            if (Objects.isNull(userExtend)) {
                userExtend = new UserExtend();
                userExtend.setIdUser(user.getIdUser());
                userExtendMapper.insertSelective(userExtend);
            }
            map.put("user", user);
            map.put("userExtend", userExtend);
        }
        return map;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map updateUserInfo(UserInfoDTO user) {
        Map map = new HashMap(1);
        user.setNickname(formatNickname(user.getNickname()));
        Integer number = userMapper.checkNicknameByIdUser(user.getIdUser(), user.getNickname());
        if (number > 0) {
            map.put("message", "该昵称已使用!");
            return map;
        }
        if (StringUtils.isNotBlank(user.getAvatarType()) && AVATAR_SVG_TYPE.equals(user.getAvatarType())) {
            String avatarUrl = UploadController.uploadBase64File(user.getAvatarUrl(), 0);
            user.setAvatarUrl(avatarUrl);
            user.setAvatarType("0");
        }
        Integer result = userMapper.updateUserInfo(user.getIdUser(), user.getNickname(), user.getAvatarType(),user.getAvatarUrl(),
                user.getEmail(),user.getPhone(),user.getSignature(), user.getSex());
        if (result == 0) {
            map.put("message", "操作失败!");
            return map;
        }
        map.put("user",user);
        return map;
    }

    private String formatNickname(String nickname) {
        return nickname.replaceAll(".", "");
    }

    @Override
    public Map checkNickname(Integer idUser, String nickname) {
        Map map = new HashMap(1);
        Integer number = userMapper.checkNicknameByIdUser(idUser, nickname);
        if (number > 0) {
            map.put("message", "该昵称已使用!");
        }
        return map;
    }

    @Override
    public Integer findRoleWeightsByUser(Integer idUser) {
        return userMapper.selectRoleWeightsByUser(idUser);
    }

    @Override
    public Author selectAuthor(Integer idUser) {
        return userMapper.selectAuthor(idUser);
    }

    @Override
    public Map updateUserExtend(UserExtend userExtend) {
        Map map = new HashMap(1);
        int result = userExtendMapper.updateByPrimaryKeySelective(userExtend);
        if (result == 0) {
            map.put("message", "操作失败!");
            return map;
        }
        map.put("userExtend", userExtend);
        return map;
    }

    @Override
    public UserExtend selectUserExtendByNickname(String nickname) {
        return userExtendMapper.selectUserExtendByNickname(nickname);
    }

    @Override
    public Map updateEmail(ChangeEmailDTO changeEmailDTO) {
        Map map = new HashMap(2);
        map.put("message","验证码无效！");
        Integer idUser = changeEmailDTO.getIdUser();
        String email = changeEmailDTO.getEmail();
        String code = changeEmailDTO.getCode();
        String vCode = redisService.get(email);
        if(StringUtils.isNotBlank(vCode)){
            if(vCode.equals(code)){
                userMapper.updateEmail(idUser, email);
                map.put("message","更新成功！");
                map.put("email", email);
            }
        }
        return map;
    }

    @Override
    public Map updatePassword(UpdatePasswordDTO updatePasswordDTO) {
        Map map = new HashMap(1);
        String password = Utils.entryptPassword(updatePasswordDTO.getPassword());
        userMapper.updatePasswordById(updatePasswordDTO.getIdUser(), password);
        map.put("message", "更新成功!");
        return map;
    }
}
