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
import com.rymcu.forest.dto.ArticleDTO;
import com.rymcu.forest.dto.BankAccountDTO;
import com.rymcu.forest.dto.UserInfoDTO;
import com.rymcu.forest.dto.admin.Dashboard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DashboardServiceTest extends BaseServiceTest {

    @Autowired
    private DashboardService dashboardService;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("统计系统数据")
    void dashboard() {
        Dashboard result = dashboardService.dashboard();
        assertEquals(1, result.getCountArticleNum());
    }

    @Test
    @DisplayName("统计最近三十天数据")
    void lastThirtyDaysData() {
        Map result = dashboardService.lastThirtyDaysData();
        assertEquals(5, result.size());
        assertEquals(30, ((List) result.get("visits")).size());
    }

    @Test
    @DisplayName("获取历史数据")
    void history() {
        Map result = dashboardService.history();
        assertEquals(5, result.size());
        assertEquals(12, ((List) result.get("visits")).size());
    }

    @Test
    @DisplayName("获取新增用户列表")
    void newUsers() {
        List<UserInfoDTO> result = dashboardService.newUsers();
        assertEquals(0L, result.size());
    }

    @Test
    @DisplayName("获取新增银行账号列表")
    void newBankAccounts() {
        List<BankAccountDTO> result = dashboardService.newBankAccounts();
        assertEquals(0L, result.size());
    }

    @Test
    @DisplayName("获取新增文章列表")
    void newArticles() {
        List<ArticleDTO> result = dashboardService.newArticles();
        assertEquals(0L, result.size());
    }
}