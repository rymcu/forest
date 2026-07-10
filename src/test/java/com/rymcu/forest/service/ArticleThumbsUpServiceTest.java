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


package com.rymcu.forest.service;

import com.rymcu.forest.base.BaseServiceTest;
import com.rymcu.forest.core.exception.BusinessException;
import com.rymcu.forest.dto.ArticleDTO;
import com.rymcu.forest.dto.ArticleSearchDTO;
import com.rymcu.forest.entity.ArticleThumbsUp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 点赞文章测试
 *
 * @author 毛毛虫
 */
class ArticleThumbsUpServiceTest extends BaseServiceTest {

    /**
     * 测试用点赞实体
     */
    private final ArticleThumbsUp articleThumbsUp;

    @Autowired
    private ArticleThumbsUpService articleThumbsUpService;
    @Autowired
    private ArticleService articleService;

    {
            articleThumbsUp = new ArticleThumbsUp();
            articleThumbsUp.setIdArticle(-1L);
            articleThumbsUp.setThumbsUpTime(new Date());
            articleThumbsUp.setIdUser(-1L);
    }


    /**
     * 测试点赞不存在的文章
     */
    @Test
    public void thumbsNotExistsArticle() {
        assertThrows(BusinessException.class, () -> {
            articleThumbsUpService.thumbsUp(articleThumbsUp);
        });
    }

    /**
     * 测试点赞存在的文章
     */
    @Test
    public void thumbsExistsArticle2() {
        assertDoesNotThrow(() -> {
            ArticleSearchDTO articleSearchDTO = new ArticleSearchDTO();
            articleSearchDTO.setTopicUri("news");
            List<ArticleDTO> articles = articleService.findArticles(articleSearchDTO);
            articleThumbsUp.setIdArticle(articles.get(0).getIdArticle());
        });

        int i = articleThumbsUpService.thumbsUp(articleThumbsUp);
        assertEquals(1, i);
    }

}