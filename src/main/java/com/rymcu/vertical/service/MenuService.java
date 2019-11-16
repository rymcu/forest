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
}
