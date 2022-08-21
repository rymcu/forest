package com.rymcu.forest.service.impl;

import com.rymcu.forest.core.exception.BusinessException;
import com.rymcu.forest.core.service.AbstractService;
import com.rymcu.forest.entity.Article;
import com.rymcu.forest.entity.ArticleThumbsUp;
import com.rymcu.forest.entity.User;
import com.rymcu.forest.mapper.ArticleThumbsUpMapper;
import com.rymcu.forest.service.ArticleService;
import com.rymcu.forest.service.ArticleThumbsUpService;
import com.rymcu.forest.util.UserUtils;
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
    public String thumbsUp(ArticleThumbsUp articleThumbsUp) throws Exception {
        if (Objects.isNull(articleThumbsUp) || Objects.isNull(articleThumbsUp.getIdArticle())) {
            throw new BusinessException("数据异常,文章不存在!");
        } else {
            Integer thumbsUpNumber = 1;
            Article article = articleService.findById(String.valueOf(articleThumbsUp.getIdArticle()));
            if (Objects.isNull(article)) {
                throw new BusinessException("数据异常,文章不存在!");
            } else {
                User user = UserUtils.getCurrentUserByToken();
                articleThumbsUp.setIdUser(user.getIdUser());
                ArticleThumbsUp thumbsUp = articleThumbsUpMapper.selectOne(articleThumbsUp);
                if (Objects.isNull(thumbsUp)) {
                    articleThumbsUp.setThumbsUpTime(new Date());
                    articleThumbsUpMapper.insertSelective(articleThumbsUp);
                    // 更新文章点赞数
                } else {
                    articleThumbsUpMapper.deleteByPrimaryKey(thumbsUp.getIdArticleThumbsUp());
                    // 更新文章点赞数
                    thumbsUpNumber = -1;
                }
                articleThumbsUpMapper.updateArticleThumbsUpNumber(articleThumbsUp.getIdArticle(), thumbsUpNumber);
                if (thumbsUpNumber > 0) {
                    return "点赞成功";
                } else {
                    return "已取消点赞";
                }
            }
        }
    }
}
