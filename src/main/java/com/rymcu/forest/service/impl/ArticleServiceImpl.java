package com.rymcu.forest.service.impl;

import com.rymcu.forest.core.constant.NotificationConstant;
import com.rymcu.forest.core.service.AbstractService;
import com.rymcu.forest.dto.*;
import com.rymcu.forest.entity.Article;
import com.rymcu.forest.entity.ArticleContent;
import com.rymcu.forest.entity.Tag;
import com.rymcu.forest.entity.User;
import com.rymcu.forest.lucene.service.LuceneService;
import com.rymcu.forest.mapper.ArticleMapper;
import com.rymcu.forest.service.ArticleService;
import com.rymcu.forest.service.TagService;
import com.rymcu.forest.service.UserService;
import com.rymcu.forest.util.*;
import com.rymcu.forest.web.api.exception.BaseApiException;
import com.rymcu.forest.web.api.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ArticleServiceImpl extends AbstractService<Article> implements ArticleService {

    @Resource
    private ArticleMapper articleMapper;
    @Resource
    private TagService tagService;
    @Resource
    private UserService userService;
    @Resource
    private LuceneService luceneService;

    @Value("${resource.domain}")
    private String domain;

    private static final int MAX_PREVIEW = 200;
    private static final String DEFAULT_STATUS = "0";
    private static final String DEFAULT_TOPIC_URI = "news";
    private static final int ADMIN_ROLE_WEIGHTS = 2;

    @Override
    public List<ArticleDTO> findArticles(ArticleSearchDTO searchDTO) {
        List<ArticleDTO> list;
        if (StringUtils.isNotBlank(searchDTO.getTopicUri()) && !DEFAULT_TOPIC_URI.equals(searchDTO.getTopicUri())) {
            list = articleMapper.selectArticlesByTopicUri(searchDTO.getTopicUri());
        } else {
            list = articleMapper.selectArticles(searchDTO.getSearchText(), searchDTO.getTag(), searchDTO.getTopicUri());
        }
        list.forEach(articleDTO -> genArticle(articleDTO, 0));
        return list;
    }

    @Override
    public ArticleDTO findArticleDTOById(Integer id, Integer type) {
        ArticleDTO articleDTO = articleMapper.selectArticleDTOById(id, type);
        if (articleDTO == null) {
            return null;
        }
        genArticle(articleDTO, type);
        return articleDTO;
    }

    @Override
    public List<ArticleDTO> findArticlesByTopicUri(String name) {
        List<ArticleDTO> list = articleMapper.selectArticlesByTopicUri(name);
        list.forEach(articleDTO -> genArticle(articleDTO, 0));
        return list;
    }

    @Override
    public List<ArticleDTO> findArticlesByTagName(String name) {
        return articleMapper.selectArticlesByTagName(name);
    }

    @Override
    public List<ArticleDTO> findUserArticlesByIdUser(Integer idUser) {
        List<ArticleDTO> list = articleMapper.selectUserArticles(idUser);
        list.forEach(articleDTO -> genArticle(articleDTO, 0));
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
        String articleContentHtml = XssUtils.replaceHtmlCode(article.getArticleContentHtml());
        User user = UserUtils.getCurrentUserByToken();
        if (Objects.isNull(user)) {
            throw new BaseApiException(ErrorCode.INVALID_TOKEN);
        }
        String reservedTag = checkTags(articleTags);
        boolean notification = false;
        if (StringUtils.isNotBlank(reservedTag)) {
            Integer roleWeights = userService.findRoleWeightsByUser(user.getIdUser());
            if (roleWeights > ADMIN_ROLE_WEIGHTS) {
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
            newArticle = articleMapper.selectByPrimaryKey(article.getIdArticle());
            // 如果文章之前状态为草稿则应视为新发布文章
            if (DEFAULT_STATUS.equals(newArticle.getArticleStatus())) {
                isUpdate = true;
            }
            newArticle.setArticleTitle(articleTitle);
            newArticle.setArticleTags(articleTags);
            newArticle.setArticleStatus(article.getArticleStatus());
            newArticle.setUpdatedTime(new Date());
            articleMapper.updateArticleContent(newArticle.getIdArticle(), articleContent, articleContentHtml);
        }

        // 发送相关通知
        if (DEFAULT_STATUS.equals(newArticle.getArticleStatus())) {
            // 发送系统通知
            if (notification) {
                NotificationUtils.sendAnnouncement(newArticle.getIdArticle(), NotificationConstant.Article, newArticle.getArticleTitle());
            } else {
                // 发送关注通知
                StringBuilder dataSummary = new StringBuilder();
                if (isUpdate) {
                    dataSummary.append(user.getNickname()).append("更新了文章: ").append(newArticle.getArticleTitle());
                    NotificationUtils.sendArticlePush(newArticle.getIdArticle(), NotificationConstant.UpdateArticle, dataSummary.toString(), newArticle.getArticleAuthorId());
                } else {
                    dataSummary.append(user.getNickname()).append("发布了文章: ").append(newArticle.getArticleTitle());
                    NotificationUtils.sendArticlePush(newArticle.getIdArticle(), NotificationConstant.PostArticle, dataSummary.toString(), newArticle.getArticleAuthorId());
                }
            }
            // 草稿不更新索引
            if (isUpdate) {
                log.info("更新文章索引，id={}", newArticle.getIdArticle());
                luceneService.updateArticle(newArticle.getIdArticle().toString());
            } else {
                log.info("写入文章索引，id={}", newArticle.getIdArticle());
                luceneService.writeArticle(newArticle.getIdArticle().toString());
            }
            // 更新文章链接
            newArticle.setArticlePermalink(domain + "/article/" + newArticle.getIdArticle());
            newArticle.setArticleLink("/article/" + newArticle.getIdArticle());
        } else {
            // 更新文章链接
            newArticle.setArticlePermalink(domain + "/draft/" + newArticle.getIdArticle());
            newArticle.setArticleLink("/draft/" + newArticle.getIdArticle());
        }
        tagService.saveTagArticle(newArticle, articleContentHtml);

        if (StringUtils.isNotBlank(articleContentHtml)) {
            String previewContent = Html2TextUtil.getContent(articleContentHtml);
            if (previewContent.length() > MAX_PREVIEW) {
                previewContent = previewContent.substring(0, MAX_PREVIEW);
            }
            newArticle.setArticlePreviewContent(previewContent);
        }
        articleMapper.updateByPrimaryKeySelective(newArticle);

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
    public Map delete(Integer id) throws BaseApiException {
        Map<String, String> map = new HashMap(1);
        int result;
        // 判断是否有评论
        boolean isHavComment = articleMapper.existsCommentWithPrimaryKey(id);
        if (isHavComment) {
            map.put("message", "已有评论的文章不允许删除!");
        } else {
            // 删除关联数据(作品集关联关系,标签关联关系)
            deleteLinkedData(id);
            // 删除文章
            result = articleMapper.deleteByPrimaryKey(id);
            luceneService.deleteArticle(id.toString());
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
        // 删除文章内容表
        articleMapper.deleteArticleContent(id);
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
        if (Objects.isNull(user)) {
            throw new BaseApiException(ErrorCode.INVALID_TOKEN);
        }
        StringBuilder shareUrl = new StringBuilder(article.getArticlePermalink());
        shareUrl.append("?s=").append(user.getAccount());
        Map map = new HashMap(1);
        map.put("shareUrl", shareUrl);
        return map;
    }

    @Override
    public List<ArticleDTO> findDrafts() throws BaseApiException {
        User user = UserUtils.getCurrentUserByToken();
        if (Objects.isNull(user)) {
            throw new BaseApiException(ErrorCode.INVALID_TOKEN);
        }
        List<ArticleDTO> list = articleMapper.selectDrafts(user.getIdUser());
        list.forEach(articleDTO -> genArticle(articleDTO, 0));
        return list;
    }

    @Override
    public List<ArticleDTO> findArticlesByIdPortfolio(Integer idPortfolio) {
        List<ArticleDTO> list = articleMapper.selectArticlesByIdPortfolio(idPortfolio);
        list.forEach(articleDTO -> genArticle(articleDTO, 0));
        return list;
    }

    @Override
    public List<ArticleDTO> selectUnbindArticles(Integer idPortfolio, String searchText, Integer idUser) {
        List<ArticleDTO> list = articleMapper.selectUnbindArticlesByIdPortfolio(idPortfolio, searchText, idUser);
        list.forEach(articleDTO -> genArticle(articleDTO, 0));
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
            tagService.saveTagArticle(article, "");
            map.put("success", true);
        } else {
            map.put("success", false);
            map.put("message", "更新失败,文章不存在!");
        }
        return map;
    }

    @Override
    public Map updatePerfect(Integer idArticle, String articlePerfect) {
        Map map = new HashMap(2);
        int result = articleMapper.updatePerfect(idArticle, articlePerfect);
        if (result == 0) {
            map.put("success", false);
            map.put("message", "设置优选文章失败!");
        } else {
            map.put("success", true);
        }
        return map;
    }

    @Override
    public List<ArticleDTO> findAnnouncements() {
        List<ArticleDTO> list = articleMapper.selectAnnouncements();
        list.forEach(articleDTO -> genArticle(articleDTO, 0));
        return list;
    }

    private ArticleDTO genArticle(ArticleDTO article, Integer type) {
        Integer articleList = 0;
        Integer articleView = 1;
        Integer articleEdit = 2;
        Author author = genAuthor(article);
        article.setArticleAuthor(author);
        article.setTimeAgo(Utils.getTimeAgo(article.getUpdatedTime()));
        List<ArticleTagDTO> tags = articleMapper.selectTags(article.getIdArticle());
        article.setTags(tags);
        if (!type.equals(articleList)) {
            ArticleContent articleContent = articleMapper.selectArticleContent(article.getIdArticle());
            if (type.equals(articleView)) {
                article.setArticleContent(XssUtils.replaceHtmlCode(articleContent.getArticleContentHtml()));
                // 获取所属作品集列表数据
                List<PortfolioArticleDTO> portfolioArticleDTOList = articleMapper.selectPortfolioArticles(article.getIdArticle());
                portfolioArticleDTOList.forEach(this::genPortfolioArticles);
                article.setPortfolios(portfolioArticleDTOList);
            } else if (type.equals(articleEdit)) {
                article.setArticleContent(articleContent.getArticleContent());
            } else {
                article.setArticleContent(XssUtils.replaceHtmlCode(articleContent.getArticleContentHtml()));
            }
        }
        return article;
    }

    private PortfolioArticleDTO genPortfolioArticles(PortfolioArticleDTO portfolioArticleDTO) {
        List<ArticleDTO> articles = articleMapper.selectPortfolioArticlesByIdPortfolioAndSortNo(portfolioArticleDTO.getIdPortfolio(), portfolioArticleDTO.getSortNo());
        portfolioArticleDTO.setArticles(articles);
        return portfolioArticleDTO;
    }

    private Author genAuthor(ArticleDTO article) {
        Author author = new Author();
        User user = userService.findById(String.valueOf(article.getArticleAuthorId()));
        author.setUserNickname(article.getArticleAuthorName());
        author.setUserAvatarURL(article.getArticleAuthorAvatarUrl());
        author.setIdUser(article.getArticleAuthorId());
        author.setUserAccount(user.getAccount());
        return author;
    }
}
