package com.rymcu.forest.service;

import com.rymcu.forest.dto.admin.Dashboard;

import java.util.Map;

/**
 * Created on 2022/3/14 13:51.
 *
 * @author ronger
 * @email ronger-x@outlook.com
 * @packageName com.rymcu.forest.service
 */
public interface OpenDataService {
    /**
     * 获取最近 30 天开放数据
     *
     * @return
     */
    Map lastThirtyDaysData();

    /**
     * 获取统计数据
     *
     * @return
     */
    Dashboard dashboard();
}
