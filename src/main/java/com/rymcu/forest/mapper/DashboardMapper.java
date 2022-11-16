package com.rymcu.forest.mapper;

import com.rymcu.forest.dto.ArticleDTO;
import com.rymcu.forest.dto.BankAccountDTO;
import com.rymcu.forest.dto.UserInfoDTO;
import com.rymcu.forest.dto.admin.DashboardData;

import java.util.List;

/**
 * @author ronger
 */
public interface DashboardMapper {
    /**
     * 获取总用户数
     *
     * @return
     */
    Integer selectUserCount();

    /**
     * 获取新注册用户数
     *
     * @return
     */
    Integer selectNewUserCount();

    /**
     * 获取文章总数
     *
     * @return
     */
    Integer selectArticleCount();

    /**
     * 获取今日发布文章总数
     *
     * @return
     */
    Integer selectNewArticleCount();

    /**
     * 获取浏览量总数
     *
     * @return
     */
    Integer selectCountViewNum();

    /**
     * 获取今日浏览总数
     *
     * @return
     */
    Integer selectTodayViewNum();

    /**
     * 获取最近 30 天文章数据
     *
     * @return
     */
    List<DashboardData> selectLastThirtyDaysArticleData();

    /**
     * 获取最近 30 天用户数据
     *
     * @return
     */
    List<DashboardData> selectLastThirtyDaysUserData();

    /**
     * 获取最近 30 天流量数据
     *
     * @return
     */
    List<DashboardData> selectLastThirtyDaysVisitData();

    /**
     * 获取历史 1 年文章数据
     *
     * @return
     */
    List<DashboardData> selectHistoryArticleData();

    /**
     * 获取历史 1 年用户数据
     *
     * @return
     */
    List<DashboardData> selectHistoryUserData();

    /**
     * 获取历史 1 年访问数据
     *
     * @return
     */
    List<DashboardData> selectHistoryVisitData();

    /**
     * 获取新增用户列表
     *
     * @return
     */
    List<UserInfoDTO> selectNewUsers();

    /**
     * 获取新增银行账号列表
     *
     * @return
     */
    List<BankAccountDTO> selectNewBankAccounts();

    /**
     * 获取新增文章列表
     *
     * @return
     */
    List<ArticleDTO> selectNewArticles();

    /**
     * 获取最近 30 天访客数据
     *
     * @return
     */
    List<DashboardData> selectLastThirtyDaysVisitIpData();

    /**
     * 获取历史 1 年访客数据
     *
     * @return
     */
    List<DashboardData> selectHistoryVisitIpData();
}
