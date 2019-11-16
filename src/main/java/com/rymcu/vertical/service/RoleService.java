package com.rymcu.vertical.service;

import com.rymcu.vertical.core.service.Service;
import com.rymcu.vertical.entity.Role;
import com.rymcu.vertical.entity.User;

import java.util.List;


/**
 * Created by CodeGenerator on 2018/05/29.
 */
public interface RoleService extends Service<Role> {

    List<Role> selectRoleByUser(User sysUser);
}
