package com.rymcu.forest.service;

import com.rymcu.forest.core.exception.BusinessException;
import com.rymcu.forest.core.exception.ServiceException;
import com.rymcu.forest.dto.ArticleDTO;
import com.rymcu.forest.dto.ArticleSearchDTO;
import com.rymcu.forest.entity.ArticleThumbsUp;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 点赞文章
 *
 * @author 毛毛虫
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
// 顺序执行单元测试
@TestMethodOrder(MethodOrderer.Random.class)
class ArticleThumbsUpServiceTest {

    /**
     * 测试用点赞实体
     */
    private final ArticleThumbsUp articleThumbsUp;

    @Autowired
    private ArticleThumbsUpService articleThumbsUpService;
    @Autowired
    private ArticleService articleService;

    {
        {
            articleThumbsUp = new ArticleThumbsUp();
            articleThumbsUp.setIdArticle(-1L);
            articleThumbsUp.setThumbsUpTime(new Date());
            articleThumbsUp.setIdUser(-1L);

        }
    }


    /**
     * 测试点赞不存在的文章
     */
    @Test
    void thumbsNotExistsArticle() {
        assertThrows(BusinessException.class, () -> {
            articleThumbsUpService.thumbsUp(articleThumbsUp);
        });
    }

    /**
     * 测试点赞存在的文章
     */
    @Test
    void thumbsExistsArticle() {
        assertThrows(NullPointerException.class, () -> {
            assertDoesNotThrow(() -> {
                List<ArticleDTO> articles = articleService.findArticles(null);
            });

        });
    }

    /**
     * 测试点赞存在的文章
     */
    @Test
    void thumbsExistsArticle2() {
        assertDoesNotThrow(() -> {
            List<ArticleDTO> articles = articleService.findArticles(new ArticleSearchDTO());
            articleThumbsUp.setIdArticle(articles.get(0).getIdArticle());
        });

        int i = articleThumbsUpService.thumbsUp(articleThumbsUp);
        assertEquals(1, i);
    }


    /**
     * 测试点赞存在的文章
     */
    @Test
    void thumbsNotExistsUser() {
        articleThumbsUp.setIdUser(null);
        assertThrows(ServiceException.class, () -> {
            articleThumbsUpService.thumbsUp(articleThumbsUp);
        });
    }


}