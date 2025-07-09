package com.rymcu.forest.mapper;

import com.rymcu.forest.core.mapper.Mapper;
import com.rymcu.forest.dto.PortfolioDTO;
import com.rymcu.forest.entity.Portfolio;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ronger
 */
public interface PortfolioMapper extends Mapper<Portfolio> {
    /**
     * 查询用户作品集
     *
     * @param idUser
     * @return
     */
    List<PortfolioDTO> selectUserPortfoliosByIdUser(@Param("idUser") Long idUser);

    /**
     * 查询作品集
     *
     * @param id
     * @param type
     * @return
     */
    PortfolioDTO selectPortfolioDTOById(@Param("id") Long id, @Param("type") Integer type);

    /**
     * 统计作品集下文章数
     *
     * @param idPortfolio
     * @return
     */
    Integer selectCountArticleNumber(@Param("idPortfolio") Long idPortfolio);

    /**
     * 查询文章是否已绑定
     *
     * @param idArticle
     * @param idPortfolio
     * @return
     */
    Integer selectCountPortfolioArticle(@Param("idArticle") Long idArticle, @Param("idPortfolio") Long idPortfolio);

    /**
     * 插入文章与作品集绑定数据
     *
     * @param idArticle
     * @param idPortfolio
     * @param maxSortNo
     * @return
     */
    Integer insertPortfolioArticle(@Param("idArticle") Long idArticle, @Param("idPortfolio") Long idPortfolio, @Param("maxSortNo") Integer maxSortNo);

    /**
     * 查询作品集下最大排序号
     *
     * @param idPortfolio
     * @return
     */
    Integer selectMaxSortNo(@Param("idPortfolio") Long idPortfolio);

    /**
     * 更新文章排序号
     *
     * @param idPortfolio
     * @param idArticle
     * @param sortNo
     * @return
     */
    Integer updateArticleSortNo(@Param("idPortfolio") Long idPortfolio, @Param("idArticle") Long idArticle, @Param("sortNo") Integer sortNo);

    /**
     * 取消绑定文章
     *
     * @param idPortfolio
     * @param idArticle
     * @return
     */
    Integer unbindArticle(@Param("idPortfolio") Long idPortfolio, @Param("idArticle") Long idArticle);

    /**
     * 获取作品集列表数据
     *
     * @return
     */
    List<PortfolioDTO> selectPortfolios();
}
