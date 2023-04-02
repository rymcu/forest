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