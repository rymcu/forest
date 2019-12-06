package com.rymcu.vertical.service;

import com.rymcu.vertical.core.service.Service;
import com.rymcu.vertical.entity.Article;
import com.rymcu.vertical.entity.Tag;
import com.rymcu.vertical.web.api.exception.BaseApiException;

import java.io.UnsupportedEncodingException;

/**
 * @author ronger
 */
public interface TagService extends Service<Tag> {

    /**
     * 保存文章标签
     * @param article
     * @throws UnsupportedEncodingException
     * @throws BaseApiException
     * @return
     * */
    Integer saveTagArticle(Article article) throws UnsupportedEncodingException, BaseApiException;
}
