/*
 * Copyright (c) 2020 RYMCU (https://rymcu.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */


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