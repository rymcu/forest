package com.rymcu.forest.service;

import com.rymcu.forest.core.service.Service;
import com.rymcu.forest.entity.Permission;
import com.rymcu.forest.entity.User;

import java.util.List;


/**
 * @author CodeGenerator
 * @date 2018/05/29
 */
public interface PermissionService extends Service<Permission> {

    /**
     * 获取用户权限
     *
     * @param sysUser
     * @return
     */
    List<Permission> selectPermissionByUser(User sysUser);
}
