package com.rymcu.vertical.mapper;

import com.rymcu.vertical.core.mapper.Mapper;
import com.rymcu.vertical.dto.Author;
import com.rymcu.vertical.dto.UserDTO;
import com.rymcu.vertical.dto.UserInfoDTO;
import com.rymcu.vertical.entity.User;
import org.apache.ibatis.annotations.Param;

/**
 * @author ronger
 */
public interface UserMapper extends Mapper<User> {

    /**
     * 根据账号获取获取用户信息
     * @param account
     * @return
     */
    User findByAccount(@Param("account") String account);

    /**
     * 添加用户权限
     * @param idUser
     * @param idRole
     * @return
     */
    Integer insertUserRole(@Param("idUser") Integer idUser, @Param("idRole") Integer idRole);

    /**
     * 根据账号获取获取用户信息
     * @param account
     * @return
     */
    UserInfoDTO findUserInfoByAccount(@Param("account") String account);

    /**
     * 根据用户昵称获取用户信息
     * @param nickname
     * @return
     */
    UserDTO selectUserDTOByNickname(@Param("nickname") String nickname);

    /**
     * 修改用户密码
     * @param account
     * @param password
     * @return
     */
    Integer updatePasswordByAccount(@Param("account") String account, @Param("password") String password);

    /**
     * 获取用户权限权重
     * @param idUser
     * @return
     */
    Integer selectRoleWeightsByUser(@Param("idUser") Integer idUser);

    /**
     * 更新用户权限
     * @param idUser
     * @param idRole
     * @return
     */
    Integer updateUserRole(@Param("idUser") Integer idUser, @Param("idRole") Integer idRole);

    /**
     * 更新用户状态
     * @param idUser
     * @param status
     * @return
     */
    Integer updateStatus(@Param("idUser") Integer idUser, @Param("status") String status);

    /**
     * 根据昵称获取重名用户数量
     * @param nickname
     * @return
     */
    Integer selectCountByNickName(@Param("nickname") String nickname);

    /**
     * 获取用户信息
     * @param idUser
     * @return
     */
    UserInfoDTO selectUserInfo(@Param("idUser") Integer idUser);

    /**
     * 更新用户信息
     * @param idUser
     * @param nickname
     * @param avatarType
     * @param avatarUrl
     * @param email
     * @param phone
     * @param signature
     * @param sex
     * @return
     */
    Integer updateUserInfo(@Param("idUser") Integer idUser, @Param("nickname") String nickname, @Param("avatarType") String avatarType, @Param("avatarUrl") String avatarUrl, @Param("email") String email, @Param("phone") String phone, @Param("signature") String signature, @Param("sex") String sex);

    /**
     * 验证昵称是否重复
     * @param idUser
     * @param nickname
     * @return
     */
    Integer checkNicknameByIdUser(@Param("idUser") Integer idUser, @Param("nickname") String nickname);
    /**
     * 根据用户 ID 获取作者信息
     * @param id
     * @return
     */
    Author selectAuthor(@Param("id") Integer id);

    /**
     * 更新用户最后登录时间
     * @param idUser
     * @return
     */
    Integer updateLastLoginTime(@Param("idUser") Integer idUser);
}