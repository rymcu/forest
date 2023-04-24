package com.rymcu.forest.service;

import com.rymcu.forest.base.BaseServiceTest;
import com.rymcu.forest.entity.Role;
import com.rymcu.forest.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 角色service测试
 */
class RoleServiceTest extends BaseServiceTest {

    @Autowired
    private RoleService roleService;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("查询用户角色")
    void selectRoleByUser() {
        User user = new User();
        user.setIdUser(1L);
        List<Role> roles = roleService.selectRoleByUser(user);
        assertFalse(roles.isEmpty());

        user.setIdUser(0L);
        roleService.selectRoleByUser(user);
        assertFalse(roles.isEmpty());
    }

    @Test
    @DisplayName("查询用户角色")
    void findByIdUser() {
        List<Role> roles = roleService.findByIdUser(1L);
        assertFalse(roles.isEmpty());

        roles = roleService.findByIdUser(0L);
        assertTrue(roles.isEmpty());
    }

    @Test
    @DisplayName("更新角色状态")
    void updateStatus() {
        boolean b = roleService.updateStatus(1L, "1");
        assertTrue(b);
    }

    @Test
    @DisplayName("添加/更新角色")
    void saveRole() {
        Role role = new Role();
        role.setName("test_role");
        role.setStatus("0");
        role.setWeights(1);
        boolean b = roleService.saveRole(role);
        assertTrue(b);
    }
}