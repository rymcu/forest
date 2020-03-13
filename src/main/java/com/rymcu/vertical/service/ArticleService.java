package com.rymcu.vertical.service;

import com.rymcu.vertical.core.service.Service;
import com.rymcu.vertical.dto.ArticleDTO;
import com.rymcu.vertical.entity.Article;
import com.rymcu.vertical.web.api.exception.BaseApiException;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * @author ronger
 */
public interface ArticleService extends Service<Article> {

    /**
     * 根据检索内容/标签查询文章列表
     * @param searchText
     * @param tag
     * @return
     * */
    List<ArticleDTO> findArticles(String searchText, String tag);

    /**
     * 查询文章详情信息
     * @param id
     * @param i
     * @return
     * */
    ArticleDTO findArticleDTOById(Integer id, int i);

    /**
     * 查询主题下文章列表
     * @param name
     * @return
     * */
    List<ArticleDTO> findArticlesByTopicUri(String name);

    /**
     * 查询标签下文章列表
     * @param name
     * @return
     * */
    List<ArticleDTO> findArticlesByTagName(String name);

    /**
     * 查询用户文章列表
     * @param idUser
     * @return
     * */
    List<ArticleDTO> findUserArticlesByIdUser(Integer idUser);

    /**
     * 新增/更新文章
     * @param article
     * @param request
     * @throws UnsupportedEncodingException
     * @throws BaseApiException
     * @return
     * */
    Map postArticle(ArticleDTO article, HttpServletRequest request) throws UnsupportedEncodingException, BaseApiException;

    /**
     * 删除文章
     * @param id
     * @return
     * */
    Map delete(Integer id);

    /**
     * 增量文章浏览数
     * @param id
     */
    void incrementArticleViewCount(Integer id);

    /**
     * 查询草稿文章类别
     * @return
     */
    List<ArticleDTO> findDrafts() throws BaseApiException;
}
