package com.rymcu.forest.service;

import com.rymcu.forest.core.service.Service;
import com.rymcu.forest.dto.LabelModel;
import com.rymcu.forest.entity.Article;
import com.rymcu.forest.entity.Tag;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @author ronger
 */
public interface TagService extends Service<Tag> {

    /**
     * 保存文章标签
     *
     * @param article
     * @param articleContentHtml
     * @param userId
     * @return
     * @throws UnsupportedEncodingException
     */
    Integer saveTagArticle(Article article, String articleContentHtml, Long userId) throws UnsupportedEncodingException;

    /**
     * 清除未使用标签
     *
     * @return
     */
    boolean cleanUnusedTag();

    /**
     * 添加/更新标签
     *
     * @param tag
     * @return
     */
    Tag saveTag(Tag tag) throws Exception;

    /**
     * 获取标签列表
     *
     * @return
     */
    List<LabelModel> findTagLabels();
}
