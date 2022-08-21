package com.rymcu.forest.service;

import com.github.pagehelper.PageInfo;
import com.rymcu.forest.core.service.Service;
import com.rymcu.forest.dto.PortfolioArticleDTO;
import com.rymcu.forest.dto.PortfolioDTO;
import com.rymcu.forest.dto.UserDTO;
import com.rymcu.forest.entity.Portfolio;
import com.rymcu.forest.web.api.exception.BaseApiException;

import java.util.List;

/**
 * @author ronger
 */
public interface PortfolioService extends Service<Portfolio> {

    /**
     * 查询用户作品集
     * @param userDTO
     * @return
     */
    List<PortfolioDTO> findUserPortfoliosByUser(UserDTO userDTO);

    /** 查询作品集
     * @param idPortfolio
     * @param type
     * @return
     */
    PortfolioDTO findPortfolioDTOById(Long idPortfolio, Integer type);

    /**
     * 保持/更新作品集
     * @param portfolio
     * @throws BaseApiException
     * @return
     */
    Portfolio postPortfolio(Portfolio portfolio) throws BaseApiException;

    /**
     * 查询作品集下未绑定文章
     *
     * @param page
     * @param rows
     * @param searchText
     * @param idPortfolio
     * @return
     * @throws BaseApiException
     */
    PageInfo findUnbindArticles(Integer page, Integer rows, String searchText, Long idPortfolio) throws Exception;

    /**
     * 绑定文章
     *
     * @param portfolioArticle
     * @return
     */
    boolean bindArticle(PortfolioArticleDTO portfolioArticle) throws Exception;

    /**
     * 更新文章排序号
     *
     * @param portfolioArticle
     * @return
     */
    boolean updateArticleSortNo(PortfolioArticleDTO portfolioArticle) throws Exception;

    /**
     * 取消绑定文章
     *
     * @param idPortfolio
     * @param idArticle
     * @return
     */
    boolean unbindArticle(Long idPortfolio, Long idArticle) throws Exception;


    /**
     * 删除作品集
     *
     * @param idPortfolio
     * @param idUser
     * @param roleWeights
     * @return
     * @throws BaseApiException
     * @throws IllegalAccessException
     */
    boolean deletePortfolio(Long idPortfolio, Long idUser, Integer roleWeights) throws BaseApiException, IllegalAccessException;


    /**
     * 获取作品集列表数据
     * @return
     */
    List<PortfolioDTO> findPortfolios();
}
