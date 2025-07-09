package com.rymcu.forest.service;

import com.rymcu.forest.core.exception.ServiceException;
import com.rymcu.forest.core.service.Service;
import com.rymcu.forest.dto.*;
import com.rymcu.forest.entity.User;
import com.rymcu.forest.entity.UserExtend;
import org.apache.ibatis.exceptions.TooManyResultsException;

import java.util.List;
import java.util.Set;


/**
 * @author CodeGenerator
 * @date 2018/05/29
 */
public interface UserService extends Service<User> {

    /**
     * 通过账号查询用户信息
     *
     * @param account
     * @return User
     * @throws TooManyResultsException
     */
    User findByAccount(String account) throws TooManyResultsException;

    /**
     * 注册接口
     *
     * @param email    邮箱
     * @param password 密码
     * @param code     验证码
     * @return Map
     */
    boolean register(String email, String password, String code);

    /**
     * 登录接口
     *
     * @param account  邮箱
     * @param password 密码
     * @return Map
     */
    TokenUser login(String account, String password);

    /**
     * 通过 account 获取用户信息接口
     *
     * @param account 昵称
     * @return UserDTO
     */
    UserDTO findUserDTOByAccount(String account);

    /**
     * 找回密码接口
     *
     * @param code     验证码
     * @param password 密码
     * @return Map
     * @throws ServiceException
     */
    boolean forgetPassword(String code, String password) throws ServiceException;

    /**
     * 更新用户角色接口
     *
     * @param idUser 用户 id
     * @param idRole 角色 id
     * @return Map
     * @throws ServiceException
     */
    boolean updateUserRole(Long idUser, Long idRole) throws ServiceException;

    /**
     * 更新用户状态
     *
     * @param idUser 用户 id
     * @param status 状态
     * @return Map
     * @throws ServiceException
     */
    boolean updateStatus(Long idUser, String status) throws ServiceException;

    /**
     * 获取用户信息
     *
     * @param idUser
     * @return
     */
    UserInfoDTO findUserInfo(Long idUser);

    /**
     * 更新用户信息
     *
     * @param user
     * @return
     * @throws ServiceException
     */
    UserInfoDTO updateUserInfo(UserInfoDTO user) throws ServiceException;

    /**
     * 验证昵称是否重复
     *
     * @param idUser
     * @param nickname
     * @return
     */
    boolean checkNicknameByIdUser(Long idUser, String nickname);

    /**
     * 获取用户权限
     *
     * @param idUser
     * @return
     */
    Integer findRoleWeightsByUser(Long idUser);

    /**
     * 查询作者信息
     *
     * @param idUser
     * @return
     */
    Author selectAuthor(Long idUser);

    /**
     * 更新用户扩展信息
     *
     * @param userExtend
     * @return
     */
    UserExtend updateUserExtend(UserExtend userExtend) throws ServiceException;

    /**
     * 获取用户扩展信息
     *
     * @param account
     * @return
     */
    UserExtend selectUserExtendByAccount(String account);

    /**
     * 更换邮箱
     *
     * @param changeEmailDTO
     * @return
     * @throws ServiceException
     */
    boolean updateEmail(ChangeEmailDTO changeEmailDTO) throws ServiceException;

    /**
     * 更新密码
     *
     * @param updatePasswordDTO
     * @return
     */
    boolean updatePassword(UpdatePasswordDTO updatePasswordDTO);

    /**
     * 查询用户列表
     *
     * @param searchDTO
     * @return
     */
    List<UserInfoDTO> findUsers(UserSearchDTO searchDTO);

    /**
     * 通过邮箱更新用户最后登录时间
     *
     * @param account
     * @return
     */
    Integer updateLastOnlineTimeByAccount(String account);

    /**
     * 查询用户扩展信息
     *
     * @param idUser
     * @return
     */
    UserExtend findUserExtendInfo(Long idUser);

    /**
     * 刷新  token
     *
     * @param refreshToken
     * @return
     */
    TokenUser refreshToken(String refreshToken);

    /**
     * 查询用户权限
     *
     * @param user
     * @return
     */
    Set<String> findUserPermissions(User user);

    boolean hasAdminPermission(String account);
}
