package com.rymcu.vertical.service.impl;

import com.rymcu.vertical.core.service.AbstractService;
import com.rymcu.vertical.dto.ArticleDTO;
import com.rymcu.vertical.dto.Author;
import com.rymcu.vertical.dto.UserDTO;
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
import com.rymcu.vertical.web.api.exception.MallApiException;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Service
public class ArticleServiceImpl extends AbstractService<Article> implements ArticleService {

    @Resource
    private ArticleMapper articleMapper;
    @Resource
    private TagService tagService;
    @Resource
    private UserService userService;

    private static final String DOMAIN = "https://rymcu.com";

    @Override
    public List<ArticleDTO> findArticles(String searchText, String tag) {
        List<ArticleDTO> list = articleMapper.selectArticles(searchText, tag);
        list.forEach(article->{
            article = genArticle(article,0);
        });
        return list;
    }

    @Override
    @Transactional
    public Map postArticle(Integer idArticle, String articleTitle, String articleContent, String articleContentHtml, String articleTags, HttpServletRequest request) throws MallApiException, UnsupportedEncodingException {
        Map map = new HashMap();
        Article article;
        User user = UserUtils.getWxCurrentUser();
        if(idArticle == null || idArticle == 0){
            article = new Article();
            article.setArticleTitle(articleTitle);
            article.setArticleAuthorId(user.getIdUser());
            article.setArticleTags(articleTags);
            article.setCreatedTime(new Date());
            article.setUpdatedTime(article.getCreatedTime());
            articleMapper.insertSelective(article);
            article.setArticlePermalink(DOMAIN + "/article/"+article.getIdArticle());
            article.setArticleLink("/article/"+article.getIdArticle());
            articleMapper.insertArticleContent(article.getIdArticle(),articleContent,articleContentHtml);
        } else {
            article = articleMapper.selectByPrimaryKey(idArticle);
            if(user.getIdUser() != article.getArticleAuthorId()){
                map.put("message","非法访问！");
                return map;
            }
            article.setArticleTitle(articleTitle);
            article.setArticleTags(articleTags);
            if(StringUtils.isNotBlank(articleContentHtml)){
                Integer length = articleContentHtml.length();
                if(length>200){
                    length = 200;
                }
                String articlePreviewContent = articleContentHtml.substring(0,length);
                article.setArticlePreviewContent(Html2TextUtil.getContent(articlePreviewContent));
            }
            article.setUpdatedTime(new Date());
            articleMapper.updateArticleContent(article.getIdArticle(),articleContent,articleContentHtml);
        }
        tagService.saveTagArticle(article);
        articleMapper.updateByPrimaryKeySelective(article);

        map.put("id", article.getIdArticle());
        return map;
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
            article = genArticle(article,0);
        });
        return list;
    }

    private ArticleDTO genArticle(ArticleDTO article,Integer type) {
        Author author = articleMapper.selectAuthor(article.getArticleAuthorId());
        article.setArticleAuthor(author);
        article.setTimeAgo(Utils.getTimeAgo(article.getUpdatedTime()));
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
              if(length>200){
                  length = 200;
              }
              String articlePreviewContent = articleContent.getArticleContentHtml().substring(0,length);
              article.setArticlePreviewContent(Html2TextUtil.getContent(articlePreviewContent));
          }
        }
        return article;
    }
}
