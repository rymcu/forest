package com.rymcu.forest.service;

import com.rymcu.forest.core.service.Service;
import com.rymcu.forest.entity.ArticleThumbsUp;
import com.rymcu.forest.web.api.exception.BaseApiException;

/**
 * @author ronger
 */
public interface ArticleThumbsUpService extends Service<ArticleThumbsUp> {
    /**
     * 点赞
     *
     * @param articleThumbsUp
     * @return
     */
    int thumbsUp(ArticleThumbsUp articleThumbsUp);
}
