package com.rymcu.forest.service;

import com.rymcu.forest.base.BaseServiceTest;
import com.rymcu.forest.entity.LoginRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LoginRecordServiceTest extends BaseServiceTest {

    @Autowired
    private LoginRecordService loginRecordService;

    @BeforeEach
    void setUp() {
        //设置user-agent
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return;
        }
        MockHttpServletRequest request = (MockHttpServletRequest) attributes.getRequest();
        request.addHeader("user-agent", "Windows 10 or Windows Server 2016\", \"windows nt 10\\\\.0\", \"windows nt (10\\\\.0)\" ");
    }

    @Test
    @DisplayName("保存登录记录")
    void saveLoginRecord() {
        LoginRecord loginRecord = loginRecordService.saveLoginRecord(1L);
        assertNotNull(loginRecord);
    }

    @Test
    @DisplayName("获取用户登录记录")
    void findLoginRecordByIdUser() {
        List<LoginRecord> loginRecordByIdUser = loginRecordService.findLoginRecordByIdUser(1);
        assertTrue(loginRecordByIdUser.isEmpty());

    }


    @Test
    @DisplayName("测试全部")
    void doAll() {
        LoginRecord loginRecord = loginRecordService.saveLoginRecord(1L);
        assertNotNull(loginRecord);

        List<LoginRecord> loginRecordByIdUser = loginRecordService.findLoginRecordByIdUser(1);
        assertFalse(loginRecordByIdUser.isEmpty());

    }
}