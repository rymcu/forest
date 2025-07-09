package com.rymcu.forest.mapper;

import com.rymcu.forest.core.mapper.Mapper;
import com.rymcu.forest.entity.Sponsor;
import org.apache.ibatis.annotations.Param;

/**
 * @author ronger
 */
public interface SponsorMapper extends Mapper<Sponsor> {
    /**
     * 更新文章赞赏数
     *
     * @param idArticle
     * @return
     */
    Integer updateArticleSponsorCount(@Param("idArticle") Long idArticle);
}
