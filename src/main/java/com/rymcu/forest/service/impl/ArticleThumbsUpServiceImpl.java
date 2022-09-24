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
