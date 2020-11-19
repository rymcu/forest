package com.rymcu.forest.mapper;

import com.rymcu.forest.dto.admin.DashboardData;

import java.util.List;

/**
 * @author ronger
 */
public interface DashboardMapper {
    /**
     * 获取总用户数
     * @return
     * */
    Integer selectUserCount();

    /**
     * 获取新注册用户数
     * @return
     * */
    Integer selectNewUserCount();

    /**
     * 获取文章总数
     * @return
     */
    Integer selectArticleCount();

    /**
     * 获取今日发布文章总数
     * @return
     */
    Integer selectNewArticleCount();

    /**
     * 获取浏览量总数
     * @return
     */
    Integer selectCountViewNum();

    /**
     * 获取今日浏览总数
     * @return
     */
    Integer selectTodayViewNum();

    /**
     * 获取最近 30 天文章数据
     * @return
     */
    List<DashboardData> selectLastThirtyDaysArticleData();

    /**
     * 获取最近 30 天文章数据
     * @return
     */
    List<DashboardData> selectLastThirtyDaysUserData();

    /**
     * 获取最近 30 天文章数据
     * @return
     */
    List<DashboardData> selectLastThirtyDaysVisitData();

    /**
     *
     * @return
     */
    List<DashboardData> selectHistoryArticleData();

    /**
     * @return
     */
    List<DashboardData> selectHistoryUserData();

    /**
     * @return
     */
    List<DashboardData> selectHistoryVisitData();
}
