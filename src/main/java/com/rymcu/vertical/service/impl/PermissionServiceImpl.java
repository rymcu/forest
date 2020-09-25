package com.rymcu.vertical.service.impl;

import com.rymcu.vertical.core.service.AbstractService;
import com.rymcu.vertical.entity.Permission;
import com.rymcu.vertical.entity.Role;
import com.rymcu.vertical.entity.User;
import com.rymcu.vertical.mapper.PermissionMapper;
import com.rymcu.vertical.service.PermissionService;
import com.rymcu.vertical.service.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


/**
 *
 * @author CodeGenerator
 * @date 2018/05/29
 */
@Service
public class PermissionServiceImpl extends AbstractService<Permission> implements PermissionService {
    @Resource
    private PermissionMapper permissionMapper;

    @Resource
    private RoleService roleService;

    @Override
    public List<Permission> selectPermissionByUser(User sysUser) {
        List<Permission> list = new ArrayList<Permission>();
        List<Role> roles = roleService.selectRoleByUser(sysUser);
        roles.forEach(role -> list.addAll(permissionMapper.selectMenuByIdRole(role.getIdRole())));
        HashSet hashSet = new HashSet(list);
        list.clear();
        list.addAll(hashSet);
        return list;
    }
}
