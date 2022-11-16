package com.rymcu.forest.lucene.mapper;

import com.rymcu.forest.dto.PortfolioDTO;
import com.rymcu.forest.lucene.model.PortfolioLucene;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * PortfolioLuceneMapper
 *
 * @author suwen
 * @date 2021/4/17 10:00
 */
@Mapper
public interface PortfolioLuceneMapper {

    /**
     * 加载所有作品集信息
     *
     * @return
     */
    List<PortfolioLucene> getAllPortfolioLucene();

    /**
     * 加载所有作品集信息
     *
     * @param ids 作品集id(半角逗号分隔)
     * @return
     */
    List<PortfolioDTO> getPortfoliosByIds(@Param("ids") Long[] ids);

    /**
     * 加载作品集
     *
     * @param id 用户id
     * @return
     */
    PortfolioLucene getById(@Param("id") Long id);
}
