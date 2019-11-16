package com.rymcu.vertical.service.impl;

import com.rymcu.vertical.core.service.AbstractService;
import com.rymcu.vertical.dto.MenuDTO;
import com.rymcu.vertical.entity.Menu;
import com.rymcu.vertical.entity.Role;
import com.rymcu.vertical.entity.User;
import com.rymcu.vertical.mapper.MenuMapper;
import com.rymcu.vertical.service.MenuService;
import com.rymcu.vertical.service.RoleService;
import com.rymcu.vertical.util.BeanCopierUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;


/**
 * Created by CodeGenerator on 2018/05/29.
 */
@Service
@Transactional
public class MenuServiceImpl extends AbstractService<Menu> implements MenuService {
    @Resource
    private MenuMapper menuMapper;

    @Resource
    private RoleService roleService;

    @Override
    public List<Menu> selectMenuByUser(User sysUser) {
        List<Menu> list = new ArrayList<Menu>();
        List<Role> roles = roleService.selectRoleByUser(sysUser);
        roles.forEach(role -> list.addAll(menuMapper.selectMenuByIdRole(role.getIdRole())));
        HashSet hashSet = new HashSet(list);
        list.clear();
        list.addAll(hashSet);
        return list;
    }
}
