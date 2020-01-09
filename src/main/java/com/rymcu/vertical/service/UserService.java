package com.rymcu.vertical.service;

import com.rymcu.vertical.core.service.Service;
import com.rymcu.vertical.dto.UserDTO;
import com.rymcu.vertical.dto.UserInfoDTO;
import com.rymcu.vertical.entity.User;
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
}
