package com.rymcu.forest.service.impl;

import com.rymcu.forest.core.service.AbstractService;
import com.rymcu.forest.entity.Article;
import com.rymcu.forest.entity.ArticleThumbsUp;
import com.rymcu.forest.entity.User;
import com.rymcu.forest.mapper.ArticleThumbsUpMapper;
import com.rymcu.forest.service.ArticleService;
import com.rymcu.forest.service.ArticleThumbsUpService;
import com.rymcu.forest.util.UserUtils;
import com.rymcu.forest.web.api.exception.BaseApiException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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
    public Map thumbsUp(ArticleThumbsUp articleThumbsUp) throws BaseApiException {
        Map map = new HashMap(3);
        if (Objects.isNull(articleThumbsUp) || Objects.isNull(articleThumbsUp.getIdArticle())) {
            map.put("message", "数据异常,文章不存在!");
            map.put("success", false);
        } else {
            Integer thumbsUpNumber = 1;
            Article article = articleService.findById(String.valueOf(articleThumbsUp.getIdArticle()));
            if (Objects.isNull(article)) {
                map.put("message", "数据异常,文章不存在!");
                map.put("success", false);
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
                map.put("success", true);
                map.put("thumbsUpNumber", thumbsUpNumber);
                if (thumbsUpNumber > 0) {
                    map.put("message", "点赞成功");
                } else {
                    map.put("message", "已取消点赞");
                }
            }
        }
        return map;
    }
}
