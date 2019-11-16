package com.rymcu.vertical.mapper;

import com.rymcu.vertical.core.mapper.Mapper;
import com.rymcu.vertical.dto.RoleDTO;
import com.rymcu.vertical.dto.UserDTO;
import com.rymcu.vertical.entity.Role;
import com.rymcu.vertical.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleMapper extends Mapper<Role> {
    List<RoleDTO> findAllDTO(@Param("role") RoleDTO roleDTO);

    List<Role> selectRoleByUser(@Param("sysUser") User sysUser);

    void insertRoleMenu(@Param("roleId") String id, @Param("menuId") String menuId);

    void deleteRoleMenuByRoleId(@Param("roleId") String roleId);

    void deleteUserRoleByRoleId(@Param("roleId") String roleId);

    RoleDTO findRoleDTOById(@Param("roleId") String roleId);

    String selectRoleIdsByUser(@Param("user") User user);

    List<UserDTO> findUserOfRoleListData(@Param("user") UserDTO userDTO);

    void deleteUser(@Param("userId") String userId, @Param("roleId") String roleId);

    List<UserDTO> findUnAddUserOfRole(@Param("userName") String userName, @Param("roleId") String roleId);

    void insertUserRole(@Param("userId") String userId, @Param("roleId") String roleId);
}