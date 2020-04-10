package com.rymcu.vertical.mapper;

import com.rymcu.vertical.core.mapper.Mapper;
import com.rymcu.vertical.dto.PortfolioDTO;
import com.rymcu.vertical.entity.Portfolio;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ronger
 */
public interface PortfolioMapper extends Mapper<Portfolio> {
    /**
     * 查询用户作品集
     * @param idUser
     * @return
     */
    List<PortfolioDTO> selectUserPortfoliosByIdUser(@Param("idUser") Integer idUser);

    /**
     * 查询作品集
     * @param id
     * @return
     */
    PortfolioDTO selectPortfolioDTOById(@Param("id") Integer id);

    /**
     * 统计作品集下文章数
     * @param idPortfolio
     * @return
     */
    Integer selectCountArticleNumber(@Param("idPortfolio") Integer idPortfolio);

    /**
     * 查询文章是否已绑定
     * @param idArticle
     * @param idPortfolio
     * @return
     */
    Integer selectCountPortfolioArticle(@Param("idArticle") Integer idArticle, @Param("idPortfolio") Integer idPortfolio);

    /**
     * 插入文章与作品集绑定数据
     * @param idArticle
     * @param idPortfolio
     * @param maxSortNo
     * @return
     */
    Integer insertPortfolioArticle(@Param("idArticle") Integer idArticle, @Param("idPortfolio") Integer idPortfolio, @Param("maxSortNo") Integer maxSortNo);

    /**
     * 查询作品集下最大排序号
     * @param idPortfolio
     * @return
     */
    Integer selectMaxSortNo(@Param("idPortfolio") Integer idPortfolio);
}
