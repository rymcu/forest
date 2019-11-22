package com.rymcu.vertical.service;

import com.rymcu.vertical.core.service.Service;
import com.rymcu.vertical.entity.Article;
import com.rymcu.vertical.entity.Tag;
import com.rymcu.vertical.web.api.exception.MallApiException;

import java.io.UnsupportedEncodingException;

public interface TagService extends Service<Tag> {

    void saveTagArticle(Article article) throws UnsupportedEncodingException, MallApiException;
}
