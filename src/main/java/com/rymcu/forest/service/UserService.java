package com.rymcu.forest.service;

import com.rymcu.forest.core.service.Service;
import com.rymcu.forest.dto.*;
import com.rymcu.forest.entity.User;
import com.rymcu.forest.entity.UserExtend;
import org.apache.ibatis.exceptions.TooManyResultsException;

import java.util.Map;


/**
 *
 * @author CodeGenerator
 * @date 2018/05/29
 */
public interface UserService extends Service<User> {

    /**
     * 通过账号查询用户信息
     * @param account
     * @throws TooManyResultsException
     * @return User
     * */
    User findByAccount(String account) throws TooManyResultsException;

    /**
     * 注册接口
     * @param email 邮箱
     * @param password  密码
     * @param code 验证码
     * @return Map
     * */
    Map register(String email, String password, String code);

    /**
     * 登录接口
     * @param account 邮箱
     * @param password  密码
     * @return Map
     * */
    Map login(String account, String password);

    /**
     * 通过 nickname 获取用户信息接口
     * @param nickname 昵称
     * @return  UserDTO
     * */
    UserDTO findUserDTOByNickname(String nickname);

    /**
     * 找回密码接口
     * @param code 验证码
     * @param password 密码
     * @return Map
     * */
    Map forgetPassword(String code, String password);

    /**
     * 更新用户角色接口
     * @param idUser 用户 id
     * @param idRole 角色 id
     * @return Map
     * */
    Map updateUserRole(Integer idUser, Integer idRole);

    /**
     * 更新用户状态
     * @param idUser 用户 id
     * @param status 状态
     * @return Map
     * */
    Map updateStatus(Integer idUser, String status);

    /**
     * 获取用户信息
     * @param idUser
     * @return
     */
    Map findUserInfo(Integer idUser);

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    Map updateUserInfo(UserInfoDTO user);

    /**
     * 验证昵称是否重复
     * @param idUser
     * @param nickname
     * @return
     */
    Map checkNickname(Integer idUser, String nickname);

    /**
     * 获取用户权限
     * @param idUser
     * @return
     */
    Integer findRoleWeightsByUser(Integer idUser);

    /**
     * 查询作者信息
     * @param idUser
     * @return
     */
    Author selectAuthor(Integer idUser);

    /**
     * 更新用户扩展信息
     * @param userExtend
     * @return
     */
    Map updateUserExtend(UserExtend userExtend);

    /**
     * 获取用户扩展信息
     * @param nickname
     * @return
     */
    UserExtend selectUserExtendByNickname(String nickname);

    /**
     * 更换邮箱
     * @param changeEmailDTO
     * @return
     */
    Map updateEmail(ChangeEmailDTO changeEmailDTO);

    /**
     * 更新密码
     * @param updatePasswordDTO
     * @return
     */
    Map updatePassword(UpdatePasswordDTO updatePasswordDTO);
}
