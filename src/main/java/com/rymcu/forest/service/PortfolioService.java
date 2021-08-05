package com.rymcu.forest.service;

import com.rymcu.forest.core.service.Service;
import com.rymcu.forest.dto.PortfolioArticleDTO;
import com.rymcu.forest.dto.PortfolioDTO;
import com.rymcu.forest.dto.UserDTO;
import com.rymcu.forest.entity.Portfolio;
import com.rymcu.forest.web.api.exception.BaseApiException;

import java.util.List;
import java.util.Map;

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
    PortfolioDTO findPortfolioDTOById(Integer idPortfolio, Integer type);

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
     * @throws BaseApiException
     * @return
     */
    Map findUnbindArticles(Integer page, Integer rows, String searchText, Integer idPortfolio) throws BaseApiException;

    /**
     * 绑定文章
     * @param portfolioArticle
     * @return
     */
    Map bindArticle(PortfolioArticleDTO portfolioArticle);

    /**
     * 更新文章排序号
     * @param portfolioArticle
     * @return
     */
    Map updateArticleSortNo(PortfolioArticleDTO portfolioArticle);

    /**
     * 取消绑定文章
     * @param idPortfolio
     * @param idArticle
     * @return
     */
    Map unbindArticle(Integer idPortfolio, Integer idArticle);

    /**
     * 删除作品集
     * @param idPortfolio
     * @return
     */
    Map deletePortfolio(Integer idPortfolio) throws BaseApiException;

    /**
     * 获取作品集列表数据
     * @return
     */
    List<PortfolioDTO> findPortfolios();
}
