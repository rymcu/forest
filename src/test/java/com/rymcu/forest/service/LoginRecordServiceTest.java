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