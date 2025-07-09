package com.rymcu.forest.service;

import com.github.pagehelper.PageInfo;
import com.rymcu.forest.core.exception.ServiceException;
import com.rymcu.forest.core.service.Service;
import com.rymcu.forest.dto.ArticleDTO;
import com.rymcu.forest.dto.PortfolioArticleDTO;
import com.rymcu.forest.dto.PortfolioDTO;
import com.rymcu.forest.dto.UserDTO;
import com.rymcu.forest.entity.Portfolio;

import java.util.List;

/**
 * @author ronger
 */
public interface PortfolioService extends Service<Portfolio> {

    /**
     * 查询用户作品集
     *
     * @param userDTO
     * @return
     */
    List<PortfolioDTO> findUserPortfoliosByUser(UserDTO userDTO);

    /**
     * 查询作品集
     *
     * @param idPortfolio
     * @param type
     * @return
     */
    PortfolioDTO findPortfolioDTOById(Long idPortfolio, Integer type);

    /**
     * 保持/更新作品集
     *
     * @param portfolio
     * @return
     */
    Portfolio postPortfolio(Portfolio portfolio);

    /**
     * 查询作品集下未绑定文章
     *
     * @param page
     * @param rows
     * @param searchText
     * @param idPortfolio
     * @param idUser
     * @return
     */
    PageInfo<ArticleDTO> findUnbindArticles(Integer page, Integer rows, String searchText, Long idPortfolio, Long idUser);

    /**
     * 绑定文章
     *
     * @param portfolioArticle
     * @return
     * @throws ServiceException
     */
    boolean bindArticle(PortfolioArticleDTO portfolioArticle) throws ServiceException;

    /**
     * 更新文章排序号
     *
     * @param portfolioArticle
     * @return
     * @throws ServiceException
     */
    boolean updateArticleSortNo(PortfolioArticleDTO portfolioArticle) throws ServiceException;

    /**
     * 取消绑定文章
     *
     * @param idPortfolio
     * @param idArticle
     * @return
     * @throws ServiceException
     */
    boolean unbindArticle(Long idPortfolio, Long idArticle) throws ServiceException;


    /**
     * 删除作品集
     *
     * @param idPortfolio
     * @param idUser
     * @param roleWeights
     * @return
     */
    boolean deletePortfolio(Long idPortfolio, Long idUser, Integer roleWeights);


    /**
     * 获取作品集列表数据
     *
     * @return
     */
    List<PortfolioDTO> findPortfolios();
}
