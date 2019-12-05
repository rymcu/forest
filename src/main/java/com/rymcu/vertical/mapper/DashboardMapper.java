package com.rymcu.vertical.mapper;

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

    Integer selectArticleCount();

    Integer selectNewArticleCount();
}
