package com.rymcu.forest.service;

import com.rymcu.forest.core.service.Service;
import com.rymcu.forest.dto.LabelModel;
import com.rymcu.forest.entity.Article;
import com.rymcu.forest.entity.Tag;
import com.rymcu.forest.web.api.exception.BaseApiException;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * @author ronger
 */
public interface TagService extends Service<Tag> {

    /**
     * 保存文章标签
     * @param article
     * @param articleContentHtml
     * @throws UnsupportedEncodingException
     * @throws BaseApiException
     * @return
     * */
    Integer saveTagArticle(Article article, String articleContentHtml, Long userId) throws UnsupportedEncodingException, BaseApiException;

    /**
     * 清除未使用标签
     * @return
     * */
    Map cleanUnusedTag();

    /**
     * 添加/更新标签
     * @param tag
     * @return
     */
    Map saveTag(Tag tag);

    /**
     * 获取标签列表
     * @return
     */
    List<LabelModel> findTagLabels();
}
