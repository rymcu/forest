package com.rymcu.vertical.service.impl;

import com.rymcu.vertical.core.service.AbstractService;
import com.rymcu.vertical.dto.ArticleDTO;
import com.rymcu.vertical.dto.Author;
import com.rymcu.vertical.entity.Article;
import com.rymcu.vertical.mapper.ArticleMapper;
import com.rymcu.vertical.service.ArticleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ArticleServiceImpl extends AbstractService<Article> implements ArticleService {

    @Resource
    private ArticleMapper articleMapper;

    private static final String DOMAIN = "https://rymcu.com";

    @Override
    public List<ArticleDTO> articles(String searchText, String tag) {
        List<ArticleDTO> list = articleMapper.selectArticles(searchText, tag);
        list.forEach(article->{
            article = genArticle(article);
        });
        return list;
    }

    @Override
    @Transactional
    public Map postArticle(Integer idArticle, String articleTitle, String articleContent, String articleContentHtml, String articleTags, HttpServletRequest request) {
        Map map = new HashMap();
        Article article;
        if(idArticle == null || idArticle == 0){
            article = new Article();
            article.setArticleTitle(articleTitle);
            article.setArticleAuthorId(5);
            article.setArticleTags(articleTags);
            article.setCreatedTime(new Date());
            article.setUpdatedTime(article.getCreatedTime());
            articleMapper.insertSelective(article);
            article.setArticlePermalink(DOMAIN + "/article/"+article.getIdArticle());
            article.setArticleLink("/article/"+article.getIdArticle());
            articleMapper.insertArticleContent(article.getIdArticle(),articleContent,articleContentHtml);
        } else {
            article = articleMapper.selectByPrimaryKey(idArticle);
            article.setArticleTitle(articleTitle);
            article.setArticleTags(articleTags);
            article.setUpdatedTime(new Date());
            articleMapper.updateArticleContent(article.getIdArticle(),articleContent,articleContentHtml);
        }
        articleMapper.updateByPrimaryKeySelective(article);
        map.put("id", article.getIdArticle());
        return map;
    }

    @Override
    public ArticleDTO findArticleDTOById(Integer id) {
        ArticleDTO articleDTO = articleMapper.selectArticleDTOById(id);
        articleDTO = genArticle(articleDTO);
        return articleDTO;
    }

    private ArticleDTO genArticle(ArticleDTO article) {
        Author author = articleMapper.selectAuthor(article.getArticleAuthorId());
        article.setArticleAuthor(author);
        return article;
    }
}
