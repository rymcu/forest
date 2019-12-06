package com.rymcu.vertical.service;

import com.rymcu.vertical.core.service.Service;
import com.rymcu.vertical.entity.Permission;
import com.rymcu.vertical.entity.User;

import java.util.List;


/**
 *
 * @author CodeGenerator
 * @date 2018/05/29
 */
public interface PermissionService extends Service<Permission> {

    List<Permission> selectMenuByUser(User sysUser);
}
