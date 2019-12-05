package com.rymcu.vertical.mapper;

import com.rymcu.vertical.core.mapper.Mapper;
import com.rymcu.vertical.entity.Role;
import com.rymcu.vertical.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleMapper extends Mapper<Role> {

    List<Role> selectRoleByIdUser(@Param("id") Integer id);

    Role selectRoleByInputCode(@Param("inputCode") String inputCode);

    Integer updateStatus(@Param("idRole") Integer idRole, @Param("status") String status);

    Integer update(@Param("idRole") Integer idRole, @Param("name") String name, @Param("inputCode") String inputCode, @Param("weights") Integer weights);
}