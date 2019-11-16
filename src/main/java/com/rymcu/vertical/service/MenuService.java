package com.rymcu.vertical.service;

import com.rymcu.vertical.core.service.Service;
import com.rymcu.vertical.dto.MenuDTO;
import com.rymcu.vertical.entity.Menu;
import com.rymcu.vertical.entity.User;

import java.util.List;


/**
 * Created by CodeGenerator on 2018/05/29.
 */
public interface MenuService extends Service<Menu> {

    List<Menu> selectMenuByUser(User sysUser);

    Menu getParent(Menu e);

    List<MenuDTO> findByParentId(String roleId);

    MenuDTO findMenuDTOById(String id);

    void saveMenu(MenuDTO menuDTO);

    void deleteMenuById(String id);

    void updateMenu(MenuDTO menuDTO);

    List<MenuDTO> findByParentIdAndUserId(String parentId, String userId);

    void updateUserStatus(String id, String flag);
}
