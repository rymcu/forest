package com.rymcu.vertical.service;

import com.rymcu.vertical.core.service.Service;
import com.rymcu.vertical.dto.RoleDTO;
import com.rymcu.vertical.dto.UserDTO;
import com.rymcu.vertical.entity.Role;
import com.rymcu.vertical.entity.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * Created by CodeGenerator on 2018/05/29.
 */
public interface RoleService extends Service<Role> {

    List<Role> selectRoleByUser(User sysUser);

    List<RoleDTO> findAllDTO(HttpServletRequest request);

    void saveRole(RoleDTO roleDTO);

    void deleteAssociatedItemsForRole(String id);

    RoleDTO findRoleDTOById(String id);

    void updateRole(RoleDTO roleDTO);

    List<UserDTO> findUserOfRoleListData(HttpServletRequest request);

    void deleteUser(String userId, String roleId);

    List<UserDTO> findUnAddUserOfRole(HttpServletRequest request);

    String addUserOfRole(String roleId, String ids);

    void updateMenu(RoleDTO roleDTO);
}
