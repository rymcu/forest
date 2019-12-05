package com.rymcu.vertical.service;

import com.rymcu.vertical.core.service.Service;
import com.rymcu.vertical.entity.Role;
import com.rymcu.vertical.entity.User;

import java.util.List;
import java.util.Map;


/**
 * Created by CodeGenerator on 2018/05/29.
 */
public interface RoleService extends Service<Role> {

    List<Role> selectRoleByUser(User sysUser);

    List<Role> findByIdUser(Integer idUser);

    Map updateStatus(Integer idRole, String status);

    Map saveRole(Role role);
}
