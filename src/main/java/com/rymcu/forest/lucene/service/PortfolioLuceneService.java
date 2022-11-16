package com.rymcu.forest.lucene.service;

import com.rymcu.forest.dto.PortfolioDTO;
import com.rymcu.forest.lucene.model.PortfolioLucene;

import java.util.List;

/**
 * PortfolioLuceneService
 *
 * @author suwen
 * @date 2021/4/17 10:10
 */
public interface PortfolioLuceneService {

    /**
     * 批量写入作品集信息到索引
     *
     * @param list
     */
    void writePortfolio(List<PortfolioLucene> list);

    /**
     * 写入单个作品集索引
     *
     * @param id
     */
    void writePortfolio(Long id);

    /**
     * 写入单个作品集索引
     *
     * @param portfolioLucene
     */
    void writePortfolio(PortfolioLucene portfolioLucene);

    /**
     * 更新单个作品集索引
     *
     * @param id
     */
    void updatePortfolio(Long id);

    /**
     * 删除单个作品集索引
     *
     * @param id
     */
    void deletePortfolio(Long id);

    /**
     * 关键词搜索
     *
     * @param value
     * @return
     * @throws Exception
     */
    List<PortfolioLucene> searchPortfolio(String value);

    /**
     * 加载所有作品集内容
     *
     * @return
     */
    List<PortfolioLucene> getAllPortfolioLucene();

    /**
     * 加载所有作品集内容
     *
     * @param ids 作品集id(半角逗号分隔)
     * @return
     */
    List<PortfolioDTO> getPortfoliosByIds(Long[] ids);
}
