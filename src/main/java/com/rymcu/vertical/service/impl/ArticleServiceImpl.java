package com.rymcu.vertical.service.impl;

import com.rymcu.vertical.core.service.AbstractService;
import com.rymcu.vertical.dto.ArticleDTO;
import com.rymcu.vertical.dto.ArticleTagDTO;
import com.rymcu.vertical.dto.Author;
import com.rymcu.vertical.entity.Article;
import com.rymcu.vertical.entity.ArticleContent;
import com.rymcu.vertical.entity.User;
import com.rymcu.vertical.mapper.ArticleMapper;
import com.rymcu.vertical.service.ArticleService;
import com.rymcu.vertical.service.TagService;
import com.rymcu.vertical.service.UserService;
import com.rymcu.vertical.util.Html2TextUtil;
import com.rymcu.vertical.util.UserUtils;
import com.rymcu.vertical.util.Utils;
import com.rymcu.vertical.web.api.exception.BaseApiException;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private static final String DOMAIN = "https://rymcu.com";

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
    public List<ArticleDTO> findArticlesByTopicName(String name) {
        List<ArticleDTO> articleDTOS = articleMapper.selectArticlesByTopicName(name);
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
        Article article1;
        if(article.getIdArticle() == null || article.getIdArticle() == 0){
            article1 = new Article();
            article1.setArticleTitle(articleTitle);
            article1.setArticleAuthorId(user.getIdUser());
            article1.setArticleTags(articleTags);
            article1.setCreatedTime(new Date());
            article1.setUpdatedTime(article1.getCreatedTime());
            articleMapper.insertSelective(article1);
            article1.setArticlePermalink(DOMAIN + "/article/"+article1.getIdArticle());
            article1.setArticleLink("/article/"+article1.getIdArticle());
            articleMapper.insertArticleContent(article1.getIdArticle(),articleContent,articleContentHtml);
        } else {
            article1 = articleMapper.selectByPrimaryKey(article.getIdArticle());
            if(!user.getIdUser().equals(article1.getArticleAuthorId())){
                map.put("message","非法访问！");
                return map;
            }
            article1.setArticleTitle(articleTitle);
            article1.setArticleTags(articleTags);
            if(StringUtils.isNotBlank(articleContentHtml)){
                Integer length = articleContentHtml.length();
                if(length > MAX_PREVIEW){
                    length = 200;
                }
                String articlePreviewContent = articleContentHtml.substring(0,length);
                article1.setArticlePreviewContent(Html2TextUtil.getContent(articlePreviewContent));
            }
            article1.setUpdatedTime(new Date());
            articleMapper.updateArticleContent(article1.getIdArticle(),articleContent,articleContentHtml);
        }
        tagService.saveTagArticle(article1);
        articleMapper.updateByPrimaryKeySelective(article1);

        map.put("id", article1.getIdArticle());
        return map;
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
        } else {
          if(StringUtils.isBlank(article.getArticlePreviewContent())){
              ArticleContent articleContent = articleMapper.selectArticleContent(article.getIdArticle());
              Integer length = articleContent.getArticleContentHtml().length();
              if(length > MAX_PREVIEW){
                  length = 200;
              }
              String articlePreviewContent = articleContent.getArticleContentHtml().substring(0,length);
              article.setArticlePreviewContent(Html2TextUtil.getContent(articlePreviewContent));
          }
        }
        return article;
    }
}
