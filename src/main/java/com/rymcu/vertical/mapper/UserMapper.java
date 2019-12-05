package com.rymcu.vertical.mapper;

import com.rymcu.vertical.core.mapper.Mapper;
import com.rymcu.vertical.dto.UserDTO;
import com.rymcu.vertical.dto.UserInfoDTO;
import com.rymcu.vertical.entity.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper extends Mapper<User> {

    User findByAccount(@Param("account") String account);

    Integer insertUserRole(@Param("idUser") Integer idUser, @Param("idRole") Integer idRole);

    UserInfoDTO findUserInfoByAccount(@Param("account") String account);

    UserDTO selectUserDTOByNickname(@Param("nickname") String nickname);

    Integer updatePasswordByAccount(@Param("account") String account, @Param("password") String password);

    Integer selectRoleWeightsByUser(@Param("idUser") Integer idUser);

    Integer updateUserRole(@Param("idUser") Integer idUser, @Param("idRole") Integer idRole);

    Integer updateStatus(@Param("idUser") Integer idUser, @Param("status") String status);

    Integer selectCountByNickName(@Param("nickname") String nickname);
}