package com.rymcu.forest.mapper;

import com.rymcu.forest.core.mapper.Mapper;
import com.rymcu.forest.dto.Author;
import com.rymcu.forest.dto.UserDTO;
import com.rymcu.forest.dto.UserInfoDTO;
import com.rymcu.forest.dto.UserSearchDTO;
import com.rymcu.forest.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ronger
 */
public interface UserMapper extends Mapper<User> {

    /**
     * 根据账号获取获取用户信息
     *
     * @param account
     * @return
     */
    User selectByAccount(@Param("account") String account);

    /**
     * 添加用户权限
     *
     * @param idUser
     * @param idRole
     * @return
     */
    Integer insertUserRole(@Param("idUser") Long idUser, @Param("idRole") Long idRole);

    /**
     * 根据账号获取获取用户信息
     *
     * @param account
     * @return
     */
    UserInfoDTO findUserInfoByAccount(@Param("account") String account);

    /**
     * 根据用户昵称获取用户信息
     *
     * @param account
     * @return
     */
    UserDTO selectUserDTOByAccount(@Param("account") String account);

    /**
     * 修改用户密码
     *
     * @param email
     * @param password
     * @return
     */
    Integer updatePasswordByEmail(@Param("email") String email, @Param("password") String password);

    /**
     * 获取用户权限权重
     *
     * @param idUser
     * @return
     */
    Integer selectRoleWeightsByUser(@Param("idUser") Long idUser);

    /**
     * 更新用户权限
     *
     * @param idUser
     * @param idRole
     * @return
     */
    Integer updateUserRole(@Param("idUser") Long idUser, @Param("idRole") Long idRole);

    /**
     * 更新用户状态
     *
     * @param idUser
     * @param status
     * @return
     */
    Integer updateStatus(@Param("idUser") Long idUser, @Param("status") String status);

    /**
     * 根据昵称获取重名用户数量
     *
     * @param nickname
     * @return
     */
    Integer selectCountByNickName(@Param("nickname") String nickname);

    /**
     * 获取用户信息
     *
     * @param idUser
     * @return
     */
    UserInfoDTO selectUserInfo(@Param("idUser") Long idUser);

    /**
     * 更新用户信息
     *
     * @param idUser
     * @param nickname
     * @param avatarType
     * @param avatarUrl
     * @param signature
     * @param sex
     * @return
     */
    Integer updateUserInfo(@Param("idUser") Long idUser, @Param("nickname") String nickname, @Param("avatarType") String avatarType, @Param("avatarUrl") String avatarUrl, @Param("signature") String signature, @Param("sex") String sex);

    /**
     * 验证昵称是否重复
     *
     * @param idUser
     * @param nickname
     * @return
     */
    Integer checkNicknameByIdUser(@Param("idUser") Long idUser, @Param("nickname") String nickname);

    /**
     * 根据用户 ID 获取作者信息
     *
     * @param id
     * @return
     */
    Author selectAuthor(@Param("id") Long id);

    /**
     * 更新用户最后登录时间
     *
     * @param idUser
     * @return
     */
    Integer updateLastLoginTime(@Param("idUser") Long idUser);

    /**
     * 更换邮箱
     *
     * @param idUser
     * @param email
     * @return
     */
    Integer updateEmail(@Param("idUser") Long idUser, @Param("email") String email);

    /**
     * 更新密码
     *
     * @param idUser
     * @param password
     * @return
     */
    Integer updatePasswordById(@Param("idUser") Long idUser, @Param("password") String password);

    /**
     * 查询用户数据
     *
     * @param searchDTO
     * @return
     */
    List<UserInfoDTO> selectUsers(@Param("searchDTO") UserSearchDTO searchDTO);

    /**
     * 更新用户最后在线时间
     *
     * @param account
     * @return
     */
    Integer updateLastOnlineTimeByAccount(@Param("account") String account);

    /**
     * 判断用户是否拥有管理员权限
     *
     * @param email
     * @return
     */
    boolean hasAdminPermission(@Param("email") String email);

    /**
     * 验证账号是否重复
     *
     * @param account
     * @return
     */
    Integer selectCountByAccount(@Param("account") String account);
}