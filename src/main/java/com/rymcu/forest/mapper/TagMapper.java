package com.rymcu.forest.mapper;

import com.rymcu.forest.core.mapper.Mapper;
import com.rymcu.forest.dto.LabelModel;
import com.rymcu.forest.entity.Tag;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ronger
 */
public interface TagMapper extends Mapper<Tag> {

    /**
     * 插入标签文章表(forest_tag_article)相关信息
     *
     * @param idTag
     * @param idArticle
     * @return
     */
    Integer insertTagArticle(@Param("idTag") Long idTag, @Param("idArticle") Long idArticle);

    /**
     * 统计标签使用数(文章)
     *
     * @param idTag
     * @param idArticle
     * @return
     */
    Integer selectCountTagArticleById(@Param("idTag") Long idTag, @Param("idArticle") Long idArticle);

    /**
     * 获取用户标签数
     *
     * @param idUser
     * @param idTag
     * @return
     */
    Integer selectCountUserTagById(@Param("idUser") Long idUser, @Param("idTag") Long idTag);

    /**
     * 插入用户标签信息
     *
     * @param idTag
     * @param idUser
     * @param type
     * @return
     */
    Integer insertUserTag(@Param("idTag") Long idTag, @Param("idUser") Long idUser, @Param("type") Integer type);

    /**
     * 删除未使用标签
     *
     * @return
     */
    Integer deleteUnusedTag();

    /**
     * 更新标签信息
     *
     * @param idTag
     * @param tagUri
     * @param tagIconPath
     * @param tagStatus
     * @param tagDescription
     * @param tagReservation
     * @return
     */
    Integer update(@Param("idTag") Long idTag, @Param("tagUri") String tagUri, @Param("tagIconPath") String tagIconPath, @Param("tagStatus") String tagStatus, @Param("tagDescription") String tagDescription, @Param("tagReservation") String tagReservation);

    /**
     * 查询标签列表
     *
     * @return
     */
    List<LabelModel> selectTagLabels();
}
