package com.rymcu.forest.service;

import com.rymcu.forest.base.BaseServiceTest;
import com.rymcu.forest.dto.admin.Dashboard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * 开发数据测试
 */
class OpenDataServiceTest extends BaseServiceTest {

    @Autowired
    private OpenDataService openDataService;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("获取最近30天开放数据")
    void lastThirtyDaysData() {
        Map map = openDataService.lastThirtyDaysData();
        assertNotNull(map);
        assertEquals(3, map.size());
    }

    @Test
    @DisplayName("获取统计数据")
    void dashboard() {
        Dashboard dashboard = openDataService.dashboard();
        assertEquals(3, dashboard.getCountUserNum());
        assertEquals(1, dashboard.getCountArticleNum());
    }
}