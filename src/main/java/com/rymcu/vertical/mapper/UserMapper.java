package com.rymcu.vertical.mapper;

import com.rymcu.vertical.core.mapper.Mapper;
import com.rymcu.vertical.dto.RoleDTO;
import com.rymcu.vertical.dto.UserDTO;
import com.rymcu.vertical.dto.UserExportDTO;
import com.rymcu.vertical.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface UserMapper extends Mapper<User> {
    List<UserDTO> findAllDTO(@Param("user") UserDTO userDTO);

    UserDTO findUserDTOById(@Param("idUser") Integer idUser);

    void insertUserRole(@Param("idUser") Integer idUser, @Param("idRole") Integer idRole);

    void deleteUserRoleByUserId(@Param("idUser") Integer idUser);

    void deleteUserById(@Param("idUser") Integer idUser);

    void resetPassword(@Param("idUser") Integer idUser, @Param("password") String password);

    void updateLastLoginTime(@Param("idUser") Integer idUser, @Param("date") Date date);

    User findByLoginName(@Param("loginName") String loginName);

    List<UserDTO> queryUserByInputCode(@Param("eName") String eName);

    List<UserExportDTO> findUserExportData(@Param("user") UserDTO userDTO);

    List<RoleDTO> findRoleOfUser(@Param("idUser") Integer idUser);
}