package com.rymcu.forest.service;

import com.rymcu.forest.dto.admin.Dashboard;

import java.util.Map;

/**
 * @author ronger
 */
public interface DashboardService {

    /**
     * 统计系统数据
     * @return
     * */
    Dashboard dashboard();

    /**
     * 统计最近三十天数据
     * @return
     */
    Map lastThirtyDaysData();

    /**
     * 获取历史数据
     * @return
     */
    Map history();
}
