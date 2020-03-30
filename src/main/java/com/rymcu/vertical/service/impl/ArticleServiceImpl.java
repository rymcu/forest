package com.rymcu.vertical.service.impl;

import com.rymcu.vertical.core.constant.NotificationConstant;
import com.rymcu.vertical.core.service.AbstractService;
import com.rymcu.vertical.dto.ArticleDTO;
import com.rymcu.vertical.dto.ArticleTagDTO;
import com.rymcu.vertical.dto.Author;
import com.rymcu.vertical.entity.Article;
import com.rymcu.vertical.entity.ArticleContent;
import com.rymcu.vertical.entity.Tag;
import com.rymcu.vertical.entity.User;
import com.rymcu.vertical.mapper.ArticleMapper;
import com.rymcu.vertical.service.ArticleService;
import com.rymcu.vertical.service.TagService;
import com.rymcu.vertical.service.UserService;
import com.rymcu.vertical.util.*;
import com.rymcu.vertical.web.api.exception.BaseApiException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ronger
 */
@Service
public class ArticleServiceImpl extends AbstractService<Article> implements ArticleService {

    @Resource
    private ArticleMapper articleMapper;
    @Resource
    private TagService tagService;
    @Resource
    private UserService userService;

    @Value("${resource.domain}")
    private static String domain;

    private static final int MAX_PREVIEW = 200;

    @Override
    public List<ArticleDTO> findArticles(String searchText, String tag) {
        List<ArticleDTO> list = articleMapper.selectArticles(searchText, tag);
        list.forEach(article->{
            genArticle(article,0);
        });
        return list;
    }

    @Override
    public ArticleDTO findArticleDTOById(Integer id, int type) {
        ArticleDTO articleDTO = articleMapper.selectArticleDTOById(id);
        articleDTO = genArticle(articleDTO,type);
        return articleDTO;
    }

    @Override
    public List<ArticleDTO> findArticlesByTopicUri(String name) {
        List<ArticleDTO> articleDTOS = articleMapper.selectArticlesByTopicUri(name);
        return articleDTOS;
    }

    @Override
    public List<ArticleDTO> findArticlesByTagName(String name) {
        List<ArticleDTO> articleDTOS = articleMapper.selectArticlesByTagName(name);
        return articleDTOS;
    }

    @Override
    public List<ArticleDTO> findUserArticlesByIdUser(Integer idUser) {
        List<ArticleDTO> list = articleMapper.selectUserArticles(idUser);
        list.forEach(article->{
            genArticle(article,0);
        });
        return list;
    }

    @Override
    @Transactional(rollbackFor = { UnsupportedEncodingException.class,BaseApiException.class })
    public Map postArticle(ArticleDTO article, HttpServletRequest request) throws UnsupportedEncodingException, BaseApiException {
        Map map = new HashMap(1);
        if(StringUtils.isBlank(article.getArticleTitle())){
            map.put("message","标题不能为空！");
            return map;
        }
        if(StringUtils.isBlank(article.getArticleContent())){
            map.put("message","正文不能为空！");
            return map;
        }
        String articleTitle = article.getArticleTitle();
        String articleTags = article.getArticleTags();
        String articleContent = article.getArticleContent();
        String articleContentHtml = article.getArticleContentHtml();
        User user = UserUtils.getWxCurrentUser();
        String reservedTag = checkTags(articleTags);
        boolean notification = false;
        if (StringUtils.isNotBlank(reservedTag)) {
            Integer roleWeights = userService.findRoleWeightsByUser(user.getIdUser());
            if (roleWeights > 2) {
                map.put("message", StringEscapeUtils.unescapeJava(reservedTag) + "标签为系统保留标签!");
                return map;
            } else {
                notification = true;
            }
        }
        Article newArticle;
        if(article.getIdArticle() == null || article.getIdArticle() == 0){
            newArticle = new Article();
            newArticle.setArticleTitle(articleTitle);
            newArticle.setArticleAuthorId(user.getIdUser());
            newArticle.setArticleTags(articleTags);
            newArticle.setCreatedTime(new Date());
            newArticle.setUpdatedTime(newArticle.getCreatedTime());
            articleMapper.insertSelective(newArticle);
            newArticle.setArticlePermalink(domain + "/article/"+newArticle.getIdArticle());
            newArticle.setArticleLink("/article/"+newArticle.getIdArticle());
            articleMapper.insertArticleContent(newArticle.getIdArticle(),articleContent,articleContentHtml);
            BaiDuUtils.sendSEOData(newArticle.getArticlePermalink());
        } else {
            newArticle = articleMapper.selectByPrimaryKey(article.getIdArticle());
            if(!user.getIdUser().equals(newArticle.getArticleAuthorId())){
                map.put("message","非法访问！");
                return map;
            }
            newArticle.setArticleTitle(articleTitle);
            newArticle.setArticleTags(articleTags);
            if(StringUtils.isNotBlank(articleContentHtml)){
                Integer length = articleContentHtml.length();
                if(length > MAX_PREVIEW){
                    length = 200;
                }
                String articlePreviewContent = articleContentHtml.substring(0,length);
                newArticle.setArticlePreviewContent(Html2TextUtil.getContent(articlePreviewContent));
            }
            newArticle.setUpdatedTime(new Date());
            articleMapper.updateArticleContent(newArticle.getIdArticle(),articleContent,articleContentHtml);
            BaiDuUtils.updateSEOData(newArticle.getArticlePermalink());
        }

        if (notification) {
            NotificationUtils.sendAnnouncement(newArticle.getIdArticle(), NotificationConstant.Article, newArticle.getArticleTitle());
        }

        tagService.saveTagArticle(newArticle);
        articleMapper.updateByPrimaryKeySelective(newArticle);

        map.put("id", newArticle.getIdArticle());
        return map;
    }

    private String checkTags(String articleTags) {
        // 判断文章是否有标签
        if(StringUtils.isBlank(articleTags)){
            return "";
        }
        // 判断是否存在系统配置的保留标签词
        Condition condition = new Condition(Tag.class);
        condition.createCriteria().andEqualTo("tagReservation", "1");
        List<Tag> tags = tagService.findByCondition(condition);
        if (tags.isEmpty()) {
            return "";
        } else {
            String[] articleTagArr = articleTags.split(",");
            for (Tag tag : tags) {
                if (StringUtils.isBlank(tag.getTagTitle())) {
                    continue;
                }

                for (String articleTag: articleTagArr) {
                    if (StringUtils.isBlank(articleTag)) {
                        continue;
                    }
                    if (articleTag.equals(tag.getTagTitle())) {
                        return tag.getTagTitle();
                    }
                }
            }
        }

        return "";
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map delete(Integer id) {
        Map<String,String> map = new HashMap(1);
        Integer result;
        // 删除引用标签记录
        result = articleMapper.deleteTagArticle(id);
        if (result > 0){
            result = articleMapper.deleteByPrimaryKey(id);
            if (result < 1){
                map.put("message", "删除失败!");
            }
        } else {
            map.put("message", "删除失败!");
        }
        return map;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void incrementArticleViewCount(Integer id) {
        Article article = articleMapper.selectByPrimaryKey(id);
        Integer articleViewCount = article.getArticleViewCount() + 1;
        articleMapper.updateArticleViewCount(article.getIdArticle(), articleViewCount);
    }

    private ArticleDTO genArticle(ArticleDTO article,Integer type) {
        Author author = articleMapper.selectAuthor(article.getArticleAuthorId());
        article.setArticleAuthor(author);
        article.setTimeAgo(Utils.getTimeAgo(article.getUpdatedTime()));
        List<ArticleTagDTO> tags = articleMapper.selectTags(article.getIdArticle());
        article.setTags(tags);
        if(type == 1){
            ArticleContent articleContent = articleMapper.selectArticleContent(article.getIdArticle());
            article.setArticleContent(articleContent.getArticleContentHtml());
        } else if(type == 2){
            ArticleContent articleContent = articleMapper.selectArticleContent(article.getIdArticle());
            article.setArticleContent(articleContent.getArticleContent());
        }

        if(StringUtils.isBlank(article.getArticlePreviewContent())){
            ArticleContent articleContent = articleMapper.selectArticleContent(article.getIdArticle());
            Integer length = articleContent.getArticleContentHtml().length();
            if(length > MAX_PREVIEW){
              length = 200;
            }
            String articlePreviewContent = articleContent.getArticleContentHtml().substring(0,length);
            article.setArticlePreviewContent(Html2TextUtil.getContent(articlePreviewContent));
        }
        return article;
    }
}
