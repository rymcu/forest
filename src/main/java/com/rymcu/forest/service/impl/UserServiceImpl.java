package com.rymcu.forest.service.impl;

import com.github.f4b6a3.ulid.UlidCreator;
import com.rymcu.forest.auth.JwtConstants;
import com.rymcu.forest.auth.TokenManager;
import com.rymcu.forest.core.exception.*;
import com.rymcu.forest.core.service.AbstractService;
import com.rymcu.forest.dto.*;
import com.rymcu.forest.entity.Role;
import com.rymcu.forest.entity.User;
import com.rymcu.forest.entity.UserExtend;
import com.rymcu.forest.lucene.model.UserLucene;
import com.rymcu.forest.lucene.util.UserIndexUtil;
import com.rymcu.forest.mapper.RoleMapper;
import com.rymcu.forest.mapper.UserExtendMapper;
import com.rymcu.forest.mapper.UserMapper;
import com.rymcu.forest.service.LoginRecordService;
import com.rymcu.forest.service.UserService;
import com.rymcu.forest.util.Utils;
import com.rymcu.forest.web.api.common.UploadController;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * @author CodeGenerator
 * @date 2018/05/29
 */
@Service
public class UserServiceImpl extends AbstractService<User> implements UserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private RoleMapper roleMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Resource
    private TokenManager tokenManager;
    @Resource
    private UserExtendMapper userExtendMapper;
    @Resource
    private LoginRecordService loginRecordService;

    private final static String AVATAR_SVG_TYPE = "1";
    private final static String DEFAULT_AVATAR = "https://static.rymcu.com/article/1578475481946.png";

    @Override
    public User findByAccount(String account) throws TooManyResultsException {
        return userMapper.selectByAccount(account);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean register(String email, String password, String code) {
        String vCode = redisTemplate.boundValueOps(email).get();
        if (StringUtils.isNotBlank(vCode)) {
            if (vCode.equals(code)) {
                User user = userMapper.selectByAccount(email);
                if (user != null) {
                    throw new AccountExistsException("该邮箱已被注册！");
                } else {
                    user = new User();
                    String nickname = email.split("@")[0];
                    user.setNickname(checkNickname(nickname));
                    user.setAccount(checkAccount(nickname));
                    user.setEmail(email);
                    user.setPassword(Utils.entryptPassword(password));
                    user.setCreatedTime(new Date());
                    user.setUpdatedTime(user.getCreatedTime());
                    user.setAvatarUrl(DEFAULT_AVATAR);
                    userMapper.insertSelective(user);
                    user = userMapper.selectByAccount(email);
                    Role role = roleMapper.selectRoleByInputCode("user");
                    userMapper.insertUserRole(user.getIdUser(), role.getIdRole());
                    UserIndexUtil.addIndex(UserLucene.builder()
                            .idUser(user.getIdUser())
                            .nickname(user.getNickname())
                            .signature(user.getSignature())
                            .build());
                    redisTemplate.delete(email);
                    return true;
                }
            }
        }
        throw new CaptchaException();
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

    private String checkAccount(String account) {
        account = formatNickname(account);
        Integer result = userMapper.selectCountByAccount(account);
        if (result > 0) {
            StringBuilder stringBuilder = new StringBuilder(account);
            return checkNickname(stringBuilder.append("_").append(System.currentTimeMillis()).toString());
        }
        return account;
    }

    @Override
    public TokenUser login(String account, String password) {
        User user = userMapper.selectByAccount(account);
        if (user != null) {
            if (Utils.comparePwd(password, user.getPassword())) {
                userMapper.updateLastLoginTime(user.getIdUser());
                userMapper.updateLastOnlineTimeByAccount(user.getAccount());
                TokenUser tokenUser = new TokenUser();
                tokenUser.setToken(tokenManager.createToken(user.getAccount()));
                tokenUser.setRefreshToken(UlidCreator.getUlid().toString());
                redisTemplate.boundValueOps(tokenUser.getRefreshToken()).set(account, JwtConstants.REFRESH_TOKEN_EXPIRES_HOUR, TimeUnit.HOURS);
                // 保存登录日志
                loginRecordService.saveLoginRecord(user.getIdUser());
                return tokenUser;
            }
        }
        throw new AccountException();
    }

    @Override
    public UserDTO findUserDTOByAccount(String account) {
        return userMapper.selectUserDTOByAccount(account);
    }

    @Override
    public boolean forgetPassword(String code, String password) throws ServiceException {
        String email = redisTemplate.boundValueOps(code).get();
        if (StringUtils.isBlank(email)) {
            throw new ServiceException("链接已失效");
        } else {
            int result = userMapper.updatePasswordByEmail(email, Utils.entryptPassword(password));
            if (result == 0) {
                throw new ServiceException("密码修改失败!");
            }
            return true;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUserRole(Long idUser, Long idRole) throws ServiceException {
        Integer result = userMapper.updateUserRole(idUser, idRole);
        if (result == 0) {
            throw new ServiceException("更新失败!");
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateStatus(Long idUser, String status) throws ServiceException {
        Integer result = userMapper.updateStatus(idUser, status);
        if (result == 0) {
            throw new ServiceException("更新失败!");
        }
        return true;
    }

    @Override
    public UserInfoDTO findUserInfo(Long idUser) {
        UserInfoDTO user = userMapper.selectUserInfo(idUser);
        if (user == null) {
            throw new ContentNotExistException("用户不存在!");
        }
        return user;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserInfoDTO updateUserInfo(UserInfoDTO user) throws ServiceException {
        user.setNickname(formatNickname(user.getNickname()));
        Integer number = userMapper.checkNicknameByIdUser(user.getIdUser(), user.getNickname());
        if (number > 0) {
            throw new NicknameOccupyException("该昵称已使用!");
        }
        if (StringUtils.isNotBlank(user.getAvatarType()) && AVATAR_SVG_TYPE.equals(user.getAvatarType())) {
            String avatarUrl = UploadController.uploadBase64File(user.getAvatarUrl(), 0);
            user.setAvatarUrl(avatarUrl);
            user.setAvatarType("0");
        }
        Integer result = userMapper.updateUserInfo(user.getIdUser(), user.getNickname(), user.getAvatarType(), user.getAvatarUrl(), user.getSignature(), user.getSex());
        UserIndexUtil.addIndex(UserLucene.builder()
                .idUser(user.getIdUser())
                .nickname(user.getNickname())
                .signature(user.getSignature())
                .build());
        if (result == 0) {
            throw new ServiceException("操作失败!");
        }

        return user;
    }

    private String formatNickname(String nickname) {
        return nickname.replaceAll("\\.", "");
    }

    public boolean checkNicknameByIdUser(Long idUser, String nickname) {
        Integer number = userMapper.checkNicknameByIdUser(idUser, nickname);
        if (number > 0) {
            return false;
        }
        return true;
    }

    @Override
    public Integer findRoleWeightsByUser(Long idUser) {
        return userMapper.selectRoleWeightsByUser(idUser);
    }

    @Override
    public Author selectAuthor(Long idUser) {
        return userMapper.selectAuthor(idUser);
    }

    @Override
    public UserExtend updateUserExtend(UserExtend userExtend) throws ServiceException {
        int result = userExtendMapper.updateByPrimaryKey(userExtend);
        if (result == 0) {
            throw new ServiceException("操作失败!");
        }
        return userExtend;
    }

    @Override
    public UserExtend selectUserExtendByAccount(String account) {
        return userExtendMapper.selectUserExtendByAccount(account);
    }

    @Override
    public boolean updateEmail(ChangeEmailDTO changeEmailDTO) throws ServiceException {
        Long idUser = changeEmailDTO.getIdUser();
        String email = changeEmailDTO.getEmail();
        String code = changeEmailDTO.getCode();
        String vCode = redisTemplate.boundValueOps(email).get();
        if (StringUtils.isNotBlank(vCode) && StringUtils.isNotBlank(code) && vCode.equals(code)) {
            int result = userMapper.updateEmail(idUser, email);
            if (result == 0) {
                throw new ServiceException("修改邮箱失败!");
            }
            return true;
        }
        throw new CaptchaException();
    }

    @Override
    public boolean updatePassword(UpdatePasswordDTO updatePasswordDTO) {
        String password = Utils.entryptPassword(updatePasswordDTO.getPassword());
        userMapper.updatePasswordById(updatePasswordDTO.getIdUser(), password);
        return true;
    }

    @Override
    public List<UserInfoDTO> findUsers(UserSearchDTO searchDTO) {
        List<UserInfoDTO> users = userMapper.selectUsers(searchDTO);
        users.forEach(user -> {
            user.setOnlineStatus(getUserOnlineStatus(user.getAccount()));
        });
        return users;
    }

    private Integer getUserOnlineStatus(String account) {
        String lastOnlineTime = redisTemplate.boundValueOps(JwtConstants.LAST_ONLINE + account).get();
        if (StringUtils.isBlank(lastOnlineTime)) {
            return 0;
        }
        return 1;
    }

    @Override
    public Integer updateLastOnlineTimeByAccount(String account) {
        return userMapper.updateLastOnlineTimeByAccount(account);
    }

    @Override
    public UserExtend findUserExtendInfo(Long idUser) {
        UserExtend userExtend = userExtendMapper.selectByPrimaryKey(idUser);
        if (Objects.isNull(userExtend)) {
            userExtend = new UserExtend();
            userExtend.setIdUser(idUser);
            userExtendMapper.insertSelective(userExtend);
        }
        return userExtend;
    }

    @Override
    public TokenUser refreshToken(String refreshToken) {
        String account = redisTemplate.boundValueOps(refreshToken).get();
        if (StringUtils.isNotBlank(account)) {
            User nucleicUser = userMapper.selectByAccount(account);
            if (nucleicUser != null) {
                TokenUser tokenUser = new TokenUser();
                tokenUser.setToken(tokenManager.createToken(nucleicUser.getAccount()));
                tokenUser.setRefreshToken(UlidCreator.getUlid().toString());
                redisTemplate.boundValueOps(tokenUser.getRefreshToken()).set(account, JwtConstants.REFRESH_TOKEN_EXPIRES_HOUR, TimeUnit.HOURS);
                redisTemplate.delete(refreshToken);
                return tokenUser;
            }
        }
        throw new UnauthenticatedException();
    }

    @Override
    public Set<String> findUserPermissions(User user) {
        Set<String> permissions = new HashSet<>();
        List<Role> roles = roleMapper.selectRoleByIdUser(user.getIdUser());
        for (Role role : roles) {
            if (StringUtils.isNotBlank(role.getInputCode())) {
                permissions.add(role.getInputCode());
            }
        }
        permissions.add("user");
        return permissions;
    }
}
