package com.rymcu.vertical.service;

import com.rymcu.vertical.core.service.Service;
import com.rymcu.vertical.dto.LabelModel;
import com.rymcu.vertical.entity.Article;
import com.rymcu.vertical.entity.Tag;
import com.rymcu.vertical.web.api.exception.BaseApiException;

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
    Integer saveTagArticle(Article article, String articleContentHtml) throws UnsupportedEncodingException, BaseApiException;

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
