package com.rymcu.vertical.mapper;

import com.rymcu.vertical.core.mapper.Mapper;
import com.rymcu.vertical.entity.Tag;
import org.apache.ibatis.annotations.Param;

public interface TagMapper extends Mapper<Tag> {
    Integer insertTagArticle(@Param("idTag") Integer idTag, @Param("idArticle") Integer idArticle);

    Integer selectCountTagArticleById(@Param("idTag") Integer idTag, @Param("idArticle") Integer idArticle);

    Integer selectCountUserTagById(@Param("idUser") Integer idUser, @Param("idTag") Integer idTag);

    Integer insertUserTag(@Param("idTag") Integer idTag, @Param("idUser") Integer idUser, @Param("type") Integer type);
}
