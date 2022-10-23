package com.rymcu.forest.service.impl;

import com.rymcu.forest.core.exception.*;
import com.rymcu.forest.core.service.AbstractService;
import com.rymcu.forest.core.service.redis.RedisService;
import com.rymcu.forest.dto.*;
import com.rymcu.forest.entity.Role;
import com.rymcu.forest.entity.User;
import com.rymcu.forest.entity.UserExtend;
import com.rymcu.forest.jwt.def.JwtConstants;
import com.rymcu.forest.jwt.service.TokenManager;
import com.rymcu.forest.lucene.model.UserLucene;
import com.rymcu.forest.lucene.util.UserIndexUtil;
import com.rymcu.forest.mapper.RoleMapper;
import com.rymcu.forest.mapper.UserExtendMapper;
import com.rymcu.forest.mapper.UserMapper;
import com.rymcu.forest.service.LoginRecordService;
import com.rymcu.forest.service.UserService;
import com.rymcu.forest.util.BeanCopierUtil;
import com.rymcu.forest.util.Utils;
import com.rymcu.forest.web.api.common.UploadController;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;


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
    @Resource
    private RedisService redisService;
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
        return userMapper.findByAccount(account);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean register(String email, String password, String code) {
        String vCode = redisService.get(email);
        if (StringUtils.isNotBlank(vCode)) {
            if (vCode.equals(code)) {
                User user = userMapper.findByAccount(email);
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
                    user = userMapper.findByAccount(email);
                    Role role = roleMapper.selectRoleByInputCode("user");
                    userMapper.insertUserRole(user.getIdUser(), role.getIdRole());
                    UserIndexUtil.addIndex(UserLucene.builder()
                            .idUser(user.getIdUser())
                            .nickname(user.getNickname())
                            .signature(user.getSignature())
                            .build());
                    redisService.delete(email);
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
        User user = userMapper.findByAccount(account);
        if (user != null) {
            if (Utils.comparePwd(password, user.getPassword())) {
                userMapper.updateLastLoginTime(user.getIdUser());
                userMapper.updateLastOnlineTimeByEmail(user.getEmail());
                TokenUser tokenUser = new TokenUser();
                BeanCopierUtil.copy(user, tokenUser);
                tokenUser.setToken(tokenManager.createToken(user.getEmail()));
                tokenUser.setWeights(userMapper.selectRoleWeightsByUser(user.getIdUser()));
                // 保存登录日志
                loginRecordService.saveLoginRecord(tokenUser.getIdUser());
                return tokenUser;
            } else {
                throw new AuthenticationException("密码错误");
            }
        } else {
            throw new UnknownAccountException("账号不存在");
        }
    }

    @Override
    public UserDTO findUserDTOByAccount(String account) {
        return userMapper.selectUserDTOByAccount(account);
    }

    @Override
    public boolean forgetPassword(String code, String password) throws ServiceException {
        String email = redisService.get(code);
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
        String vCode = redisService.get(email);
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
            user.setOnlineStatus(getUserOnlineStatus(user.getEmail()));
        });
        return users;
    }

    private Integer getUserOnlineStatus(String email) {
        String lastOnlineTime = redisService.get(JwtConstants.LAST_ONLINE + email);
        if (StringUtils.isBlank(lastOnlineTime)) {
            return 0;
        }
        return 1;
    }

    @Override
    public Integer updateLastOnlineTimeByEmail(String email) {
        return userMapper.updateLastOnlineTimeByEmail(email);
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
}
