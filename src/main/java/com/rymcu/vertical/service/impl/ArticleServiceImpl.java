package com.rymcu.vertical.service.impl;

import com.rymcu.vertical.core.constant.NotificationConstant;
import com.rymcu.vertical.core.constant.ProjectConstant;
import com.rymcu.vertical.core.service.AbstractService;
import com.rymcu.vertical.dto.*;
import com.rymcu.vertical.entity.Article;
import com.rymcu.vertical.entity.ArticleContent;
import com.rymcu.vertical.entity.Tag;
import com.rymcu.vertical.entity.User;
import com.rymcu.vertical.mapper.ArticleMapper;
import com.rymcu.vertical.service.ArticleService;
import com.rymcu.vertical.service.CommentService;
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
import java.util.*;

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
    @Resource
    private CommentService commentService;

    @Value("${resource.domain}")
    private String domain;
    @Value("${env}")
    private String env;

    private static final int MAX_PREVIEW = 200;
    private static final String defaultStatus = "0";
    private static final String defaultTopicUri = "news";

    @Override
    public List<ArticleDTO> findArticles(ArticleSearchDTO searchDTO) {
        List<ArticleDTO> list;
        if (StringUtils.isNotBlank(searchDTO.getTopicUri()) && !defaultTopicUri.equals(searchDTO.getTopicUri())) {
            list = articleMapper.selectArticlesByTopicUri(searchDTO.getTopicUri());
        } else {
            list = articleMapper.selectArticles(searchDTO.getSearchText(), searchDTO.getTag(), searchDTO.getTopicUri());
        }
        list.forEach(article -> {
            genArticle(article, 0);
        });
        return list;
    }

    @Override
    public ArticleDTO findArticleDTOById(Integer id, Integer type) {
        ArticleDTO articleDTO = articleMapper.selectArticleDTOById(id, type);
        if (articleDTO == null) {
            return null;
        }
        articleDTO = genArticle(articleDTO, type);
        return articleDTO;
    }

    @Override
    public List<ArticleDTO> findArticlesByTopicUri(String name) {
        List<ArticleDTO> articleDTOS = articleMapper.selectArticlesByTopicUri(name);
        articleDTOS.forEach(articleDTO -> {
            genArticle(articleDTO, 0);
        });
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
        list.forEach(article -> {
            genArticle(article, 0);
        });
        return list;
    }

    @Override
    @Transactional(rollbackFor = {UnsupportedEncodingException.class, BaseApiException.class})
    public Map postArticle(ArticleDTO article, HttpServletRequest request) throws UnsupportedEncodingException, BaseApiException {
        Map map = new HashMap(1);
        if (StringUtils.isBlank(article.getArticleTitle())) {
            map.put("message", "标题不能为空！");
            return map;
        }
        if (StringUtils.isBlank(article.getArticleContent())) {
            map.put("message", "正文不能为空！");
            return map;
        }
        boolean isUpdate = false;
        String articleTitle = article.getArticleTitle();
        String articleTags = article.getArticleTags();
        String articleContent = article.getArticleContent();
        String articleContentHtml = article.getArticleContentHtml();
        User user = UserUtils.getCurrentUserByToken();
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
        if (article.getIdArticle() == null || article.getIdArticle() == 0) {
            newArticle = new Article();
            newArticle.setArticleTitle(articleTitle);
            newArticle.setArticleAuthorId(user.getIdUser());
            newArticle.setArticleTags(articleTags);
            newArticle.setCreatedTime(new Date());
            newArticle.setUpdatedTime(newArticle.getCreatedTime());
            newArticle.setArticleStatus(article.getArticleStatus());
            articleMapper.insertSelective(newArticle);
            articleMapper.insertArticleContent(newArticle.getIdArticle(), articleContent, articleContentHtml);
        } else {
            isUpdate = true;
            newArticle = articleMapper.selectByPrimaryKey(article.getIdArticle());
            if (!user.getIdUser().equals(newArticle.getArticleAuthorId())) {
                map.put("message", "非法访问！");
                return map;
            }
            newArticle.setArticleTitle(articleTitle);
            newArticle.setArticleTags(articleTags);
            newArticle.setArticleStatus(article.getArticleStatus());
            newArticle.setUpdatedTime(new Date());
            articleMapper.updateArticleContent(newArticle.getIdArticle(), articleContent, articleContentHtml);
        }

        if (notification && defaultStatus.equals(newArticle.getArticleStatus())) {
            NotificationUtils.sendAnnouncement(newArticle.getIdArticle(), NotificationConstant.Article, newArticle.getArticleTitle());
        }

        tagService.saveTagArticle(newArticle);

        if (defaultStatus.equals(newArticle.getArticleStatus())) {
            newArticle.setArticlePermalink(domain + "/article/" + newArticle.getIdArticle());
            newArticle.setArticleLink("/article/" + newArticle.getIdArticle());
        } else {
            newArticle.setArticlePermalink(domain + "/draft/" + newArticle.getIdArticle());
            newArticle.setArticleLink("/draft/" + newArticle.getIdArticle());
        }

        if (StringUtils.isNotBlank(articleContentHtml)) {
            Integer length = articleContentHtml.length();
            if (length > MAX_PREVIEW) {
                length = MAX_PREVIEW;
            }
            String articlePreviewContent = articleContentHtml.substring(0, length);
            newArticle.setArticlePreviewContent(Html2TextUtil.getContent(articlePreviewContent));
        }
        articleMapper.updateByPrimaryKeySelective(newArticle);

        // 推送百度 SEO
        if (!ProjectConstant.ENV.equals(env)
                && defaultStatus.equals(newArticle.getArticleStatus())
                && articleContent.length() >= MAX_PREVIEW) {
            if (isUpdate) {
                BaiDuUtils.sendUpdateSEOData(newArticle.getArticlePermalink());
            } else {
                BaiDuUtils.sendSEOData(newArticle.getArticlePermalink());
            }
        }

        map.put("id", newArticle.getIdArticle());
        return map;
    }

    private String checkTags(String articleTags) {
        // 判断文章是否有标签
        if (StringUtils.isBlank(articleTags)) {
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

                for (String articleTag : articleTagArr) {
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
        Map<String, String> map = new HashMap(1);
        Integer result;
        // 判断是否有评论
        boolean isHavComment = articleMapper.existsCommentWithPrimaryKey(id);
        if (isHavComment) {
            map.put("message", "已有评论的文章不允许删除!");
        } else {
            // 删除关联数据(作品集关联关系,标签关联关系)
            deleteLinkedData(id);
            // 删除文章
            result = articleMapper.deleteByPrimaryKey(id);
            if (result < 1) {
                map.put("message", "删除失败!");
            }
        }
        return map;
    }

    private void deleteLinkedData(Integer id) {
        // 删除关联作品集
        articleMapper.deleteLinkedPortfolioData(id);
        // 删除引用标签记录
        articleMapper.deleteTagArticle(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void incrementArticleViewCount(Integer id) {
        Article article = articleMapper.selectByPrimaryKey(id);
        Integer articleViewCount = article.getArticleViewCount() + 1;
        articleMapper.updateArticleViewCount(article.getIdArticle(), articleViewCount);
    }

    @Override
    public Map share(Integer id) throws BaseApiException {
        Article article = articleMapper.selectByPrimaryKey(id);
        User user = UserUtils.getCurrentUserByToken();
        StringBuilder shareUrl = new StringBuilder(article.getArticlePermalink());
        shareUrl.append("?s=").append(user.getNickname());
        Map map = new HashMap(1);
        map.put("shareUrl", shareUrl);
        return map;
    }

    @Override
    public List<ArticleDTO> findDrafts() throws BaseApiException {
        User user = UserUtils.getCurrentUserByToken();
        List<ArticleDTO> list = articleMapper.selectDrafts(user.getIdUser());
        list.forEach(article -> {
            genArticle(article, 0);
        });
        return list;
    }

    @Override
    public List<ArticleDTO> findArticlesByIdPortfolio(Integer idPortfolio) {
        List<ArticleDTO> list = articleMapper.selectArticlesByIdPortfolio(idPortfolio);
        list.forEach(article -> {
            genArticle(article, 0);
        });
        return list;
    }

    @Override
    public List<ArticleDTO> selectUnbindArticles(Integer idPortfolio, String searchText, Integer idUser) {
        List<ArticleDTO> list = articleMapper.selectUnbindArticlesByIdPortfolio(idPortfolio, searchText, idUser);
        list.forEach(article -> {
            genArticle(article, 0);
        });
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map updateTags(Integer idArticle, String tags) throws UnsupportedEncodingException, BaseApiException {
        Map map = new HashMap(2);
        Article article = articleMapper.selectByPrimaryKey(idArticle);
        if (Objects.nonNull(article)) {
            article.setArticleTags(tags);
            articleMapper.updateArticleTags(idArticle, tags);
            tagService.saveTagArticle(article);
            map.put("success", true);
        } else {
            map.put("success", false);
            map.put("message", "更新失败,文章不存在!");
        }
        return map;
    }

    private ArticleDTO genArticle(ArticleDTO article, Integer type) {
        Integer ARTICLE_LIST = 0;
        Integer ARTICLE_VIEW = 1;
        Integer ARTICLE_EDIT = 2;
        Author author = genAuthor(article);
        article.setArticleAuthor(author);
        article.setTimeAgo(Utils.getTimeAgo(article.getUpdatedTime()));
        List<ArticleTagDTO> tags = articleMapper.selectTags(article.getIdArticle());
        article.setTags(tags);
        if (!type.equals(ARTICLE_LIST)) {
            ArticleContent articleContent = articleMapper.selectArticleContent(article.getIdArticle());
            if (type.equals(ARTICLE_VIEW)) {
                article.setArticleContent(articleContent.getArticleContentHtml());
                // 获取所属作品集列表数据
                List<PortfolioArticleDTO> portfolioArticleDTOList = articleMapper.selectPortfolioArticles(article.getIdArticle());
                article.setPortfolios(portfolioArticleDTOList);
            } else if (type.equals(ARTICLE_EDIT)) {
                article.setArticleContent(articleContent.getArticleContent());
            } else {
                article.setArticleContent(articleContent.getArticleContentHtml());
            }
        }
        return article;
    }

    private Author genAuthor(ArticleDTO article) {
        Author author = new Author();
        author.setUserNickname(article.getArticleAuthorName());
        author.setUserAvatarURL(article.getArticleAuthorAvatarUrl());
        author.setIdUser(article.getArticleAuthorId());
        return author;
    }
}
