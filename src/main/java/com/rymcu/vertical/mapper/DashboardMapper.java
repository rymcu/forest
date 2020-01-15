package com.rymcu.vertical.mapper;

import org.apache.ibatis.annotations.Param;

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
}
