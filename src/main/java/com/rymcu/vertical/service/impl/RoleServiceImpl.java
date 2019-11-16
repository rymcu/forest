package com.rymcu.vertical.service.impl;

import com.rymcu.vertical.core.service.AbstractService;
import com.rymcu.vertical.dto.RoleDTO;
import com.rymcu.vertical.dto.UserDTO;
import com.rymcu.vertical.entity.Role;
import com.rymcu.vertical.entity.User;
import com.rymcu.vertical.mapper.RoleMapper;
import com.rymcu.vertical.service.RoleService;
import com.rymcu.vertical.util.BeanCopierUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;


/**
 * Created by CodeGenerator on 2018/05/29.
 */
@Service
@Transactional
public class RoleServiceImpl extends AbstractService<Role> implements RoleService {
    @Resource
    private RoleMapper roleMapper;

    @Override
    public List<Role> selectRoleByUser(User sysUser) {
        List<Role> roles = roleMapper.selectRoleByUser(sysUser);
        return roles;
    }

    @Override
    public List<RoleDTO> findAllDTO(HttpServletRequest request) {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setName(request.getParameter("name"));
        roleDTO.setEnname(request.getParameter("enName"));
        return roleMapper.findAllDTO(roleDTO);
    }

    @Override
    public void saveRole(RoleDTO roleDTO) {
        Role role = new Role();
        BeanCopierUtil.copy(roleDTO,role);
        //Utils.createBase(role);
        roleMapper.insert(role);

        String menuIds = roleDTO.getMenuIds();
        String[] m = menuIds.split(",");
        for (int i = 0,len = m.length; i < len; i++) {
            roleMapper.insertRoleMenu(role.getIdRole(),m[i]);
        }
    }

    @Override
    public void deleteAssociatedItemsForRole(String roleId) {

        roleMapper.deleteUserRoleByRoleId(roleId);

        roleMapper.deleteByPrimaryKey(roleId);
    }

    @Override
    public RoleDTO findRoleDTOById(String id) {
        return roleMapper.findRoleDTOById(id);
    }

    @Override
    public void updateRole(RoleDTO roleDTO) {
        String menuIds = "";
        Role role = roleMapper.selectByPrimaryKey(roleDTO.getId());

        BeanCopierUtil.copy(roleDTO,role);
        //Utils.updateBase(role);
        roleMapper.updateByPrimaryKey(role);

        if("0".equals(roleDTO.getMenuIds())){
            menuIds = roleMapper.findRoleDTOById(roleDTO.getId()).getMenuIds();
        }else {
            menuIds = roleDTO.getMenuIds();
        }

        roleMapper.deleteRoleMenuByRoleId(role.getIdRole());

        String[] m = menuIds.split(",");
        for (int i = 0,len = m.length; i < len; i++) {
            roleMapper.insertRoleMenu(role.getIdRole(),m[i]);
        }

    }

    @Override
    public List<UserDTO> findUserOfRoleListData(HttpServletRequest request) {
        UserDTO userDTO = new UserDTO();
        userDTO.setLoginName(request.getParameter("loginName"));
        userDTO.setInputCode(request.getParameter("inputCode"));
        userDTO.setName(request.getParameter("name"));
        userDTO.setStatus(request.getParameter("status"));
        userDTO.setOfficeId(request.getParameter("officeId"));
        userDTO.setRemarks(request.getParameter("roleId"));// 为不创建多余字段，此处使用 Remark 字段存放 roleId 数据
        return roleMapper.findUserOfRoleListData(userDTO);
    }

    @Override
    public void deleteUser(String userId, String roleId) {
        roleMapper.deleteUser(userId,roleId);
    }

    @Override
    public List<UserDTO> findUnAddUserOfRole(HttpServletRequest request) {
        String roleId = request.getParameter("roleId");
        String userName = request.getParameter("userName");
        return roleMapper.findUnAddUserOfRole(userName,roleId);
    }

    @Override
    public String addUserOfRole(String roleId, String ids) {
        String message = "";
        String[] userIds = ids.split(",");
        for (String userId:userIds) {
            if(!(",".equals(userId) || StringUtils.isBlank(userId))){
                roleMapper.insertUserRole(userId,roleId);
            }
        }
        return message;
    }

    @Override
    public void updateMenu(RoleDTO roleDTO) {
        String menuIds;
        if("0".equals(roleDTO.getMenuIds())){
            menuIds = roleMapper.findRoleDTOById(roleDTO.getId()).getMenuIds();
        }else {
            menuIds = roleDTO.getMenuIds();
        }

        roleMapper.deleteRoleMenuByRoleId(roleDTO.getId());

        String[] m = menuIds.split(",");
        for (int i = 0,len = m.length; i < len; i++) {
            roleMapper.insertRoleMenu(roleDTO.getId(),m[i]);
        }
    }
}
