package com.rymcu.forest.service;

import com.rymcu.forest.base.BaseServiceTest;
import com.rymcu.forest.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.BadSqlGrammarException;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * 权限测试
 */
class PermissionServiceTest extends BaseServiceTest {

    @Autowired
    private PermissionService permissionService;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("获取用户权限")
    void selectPermissionByUser() {
        User user = new User();
        user.setIdUser(1L);
        assertThrows(BadSqlGrammarException.class, () -> {
            permissionService.selectPermissionByUser(user);
        });
    }
}