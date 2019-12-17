package com.rymcu.vertical.mapper;

import com.rymcu.vertical.core.mapper.Mapper;
import com.rymcu.vertical.entity.Tag;
import org.apache.ibatis.annotations.Param;

public interface TagMapper extends Mapper<Tag> {
    Integer insertTagArticle(@Param("idTag") Integer idTag, @Param("idArticle") Integer idArticle);

    Integer selectCountTagArticleById(@Param("idTag") Integer idTag, @Param("idArticle") Integer idArticle);

    Integer selectCountUserTagById(@Param("idUser") Integer idUser, @Param("idTag") Integer idTag);

    Integer insertUserTag(@Param("idTag") Integer idTag, @Param("idUser") Integer idUser, @Param("type") Integer type);

    Integer deleteUnusedTag();

    Integer update(@Param("idTag") Integer idTag, @Param("tagUri") String tagUri, @Param("tagIconPath") String tagIconPath, @Param("tagStatus") String tagStatus, @Param("tagDescription") String tagDescription);
}
