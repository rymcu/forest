package com.rymcu.vertical.service;

import com.rymcu.vertical.core.service.Service;
import com.rymcu.vertical.entity.Role;
import com.rymcu.vertical.entity.User;

import java.util.List;
import java.util.Map;


/**
 *
 * @author CodeGenerator
 * @date 2018/05/29
 */
public interface RoleService extends Service<Role> {

    /**
     * 查询用户角色
     * @param user
     * @return
     * */
    List<Role> selectRoleByUser(User user);

    /**
     * 查询用户角色
     * @param idUser
     * @return
     * */
    List<Role> findByIdUser(Integer idUser);

    /**
     * 更新用户状态
     * @param idRole
     * @param status
     * @return
     * */
    Map updateStatus(Integer idRole, String status);

    /**
     * 添加/更新角色
     * @param role
     * @return
     * */
    Map saveRole(Role role);
}
