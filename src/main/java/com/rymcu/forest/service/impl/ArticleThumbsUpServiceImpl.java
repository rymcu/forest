/*
 * Copyright (c) 2020 RYMCU (https://rymcu.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */


package com.rymcu.forest.service.impl;

import com.rymcu.forest.core.exception.BusinessException;
import com.rymcu.forest.core.service.AbstractService;
import com.rymcu.forest.entity.Article;
import com.rymcu.forest.entity.ArticleThumbsUp;
import com.rymcu.forest.mapper.ArticleThumbsUpMapper;
import com.rymcu.forest.service.ArticleService;
import com.rymcu.forest.service.ArticleThumbsUpService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Objects;

/**
 * @author ronger
 */
@Service
public class ArticleThumbsUpServiceImpl extends AbstractService<ArticleThumbsUp> implements ArticleThumbsUpService {

    @Resource
    private ArticleThumbsUpMapper articleThumbsUpMapper;
    @Resource
    private ArticleService articleService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int thumbsUp(ArticleThumbsUp articleThumbsUp) {
        int thumbsUpNumber = 1;
        Article article = articleService.findById(String.valueOf(articleThumbsUp.getIdArticle()));
        if (Objects.isNull(article)) {
            throw new BusinessException("数据异常,文章不存在!");
        } else {
            ArticleThumbsUp thumbsUp = articleThumbsUpMapper.selectOne(articleThumbsUp);
            if (Objects.isNull(thumbsUp)) {
                // 点赞
                articleThumbsUp.setThumbsUpTime(new Date());
                articleThumbsUpMapper.insertSelective(articleThumbsUp);
            } else {
                // 取消点赞
                articleThumbsUpMapper.deleteByPrimaryKey(thumbsUp.getIdArticleThumbsUp());
                thumbsUpNumber = -1;
            }
            // 更新文章点赞数
            articleThumbsUpMapper.updateArticleThumbsUpNumber(articleThumbsUp.getIdArticle(), thumbsUpNumber);
            return thumbsUpNumber;
        }
    }
}
