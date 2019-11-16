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
        roles.forEach(role -> list.addAll(menuMapper.selectMenuByRole(role)));
        HashSet hashSet = new HashSet(list);
        list.clear();
        list.addAll(hashSet);
        return list;
    }

    @Override
    public Menu getParent(Menu e) {
        String parentId = "0";
        if(e != null){
            if(StringUtils.isNotBlank(e.getParentId())){
                parentId = e.getParentId();
            }
        }
        return menuMapper.selectByPrimaryKey(parentId);
    }

    @Override
    public List<MenuDTO> findByParentId(String parentId) {
        List<MenuDTO> list = menuMapper.findByParentId(parentId);
        return  list;
    }

    @Override
    public MenuDTO findMenuDTOById(String id) {
        return menuMapper.findMenuDTOById(id);
    }

    @Override
    public void saveMenu(MenuDTO menuDTO) {
        Menu menu = new Menu();
        BeanCopierUtil.copy(menuDTO,menu);
        //Utils.createBase(menu);
        menuMapper.insert(menu);
        //Utils.removeAllMenu();
    }

    @Override
    public void deleteMenuById(String id) {
        menuMapper.deleteRoleMenu(id);
        menuMapper.deleteMenu(id);
        //Utils.removeAllMenu();
    }

    @Override
    public void updateMenu(MenuDTO menuDTO) {
        Menu menu = menuMapper.selectByPrimaryKey(menuDTO.getId());
        BeanCopierUtil.copy(menuDTO,menu);
        //Utils.updateBase(menu);
        menuMapper.updateByPrimaryKey(menu);
        //Utils.removeAllMenu();
        //UserUtils.removeMenuList();
    }

    @Override
    public List<MenuDTO> findByParentIdAndUserId(String parentId, String userId) {
        return menuMapper.findByParentIdAndUserId(parentId, userId);
    }

    @Override
    public void updateUserStatus(String id, String flag) {
        Menu menu = menuMapper.selectByPrimaryKey(id);
        menu.setStatus(flag);
        //Utils.updateBase(menu);
        menuMapper.updateByPrimaryKey(menu);
        //Utils.removeAllMenu();
    }
}
