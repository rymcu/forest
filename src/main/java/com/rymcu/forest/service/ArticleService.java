package com.rymcu.forest.service;

import com.rymcu.forest.core.service.Service;
import com.rymcu.forest.dto.ArticleDTO;
import com.rymcu.forest.dto.ArticleSearchDTO;
import com.rymcu.forest.entity.Article;
import com.rymcu.forest.entity.User;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @author ronger
 */
public interface ArticleService extends Service<Article> {

    /**
     * 根据检索内容/标签查询文章列表
     *
     * @param searchDTO
     * @return
     */
    List<ArticleDTO> findArticles(ArticleSearchDTO searchDTO);

    /**
     * 查询文章详情信息
     *
     * @param id
     * @param type
     * @return
     */
    ArticleDTO findArticleDTOById(Long id, Integer type);

    /**
     * 查询主题下文章列表
     *
     * @param name
     * @return
     */
    List<ArticleDTO> findArticlesByTopicUri(String name);

    /**
     * 查询标签下文章列表
     *
     * @param name
     * @return
     */
    List<ArticleDTO> findArticlesByTagName(String name);

    /**
     * 查询用户文章列表
     *
     * @param idUser
     * @return
     */
    List<ArticleDTO> findUserArticlesByIdUser(Long idUser);

    /**
     * 新增/更新文章
     *
     * @param article
     * @param user
     * @return
     * @throws UnsupportedEncodingException
     */
    Long postArticle(ArticleDTO article, User user) throws UnsupportedEncodingException;

    /**
     * 删除文章
     *
     * @param id
     * @return
     */
    Integer delete(Long id);

    /**
     * 增量文章浏览数
     *
     * @param id
     */
    void incrementArticleViewCount(Long id);

    /**
     * 获取分享链接数据
     *
     * @param id
     * @param account
     * @return
     */
    String share(Integer id, String account);

    /**
     * 查询草稿文章类别
     *
     * @return
     */
    List<ArticleDTO> findDrafts(Long userId);

    /**
     * 查询作品集下文章
     *
     * @param idPortfolio
     * @return
     */
    List<ArticleDTO> findArticlesByIdPortfolio(Long idPortfolio);

    /**
     * 查询作品集下未绑定文章
     *
     * @param idPortfolio
     * @param searchText
     * @param idUser
     * @return
     */
    List<ArticleDTO> selectUnbindArticles(Long idPortfolio, String searchText, Long idUser);

    /**
     * 更新文章标签
     *
     * @param idArticle
     * @param tags
     * @param userId
     * @return
     * @throws UnsupportedEncodingException
     */
    Boolean updateTags(Long idArticle, String tags, Long userId) throws UnsupportedEncodingException;

    /**
     * 更新文章优选状态
     *
     * @param idArticle
     * @param articlePerfect
     * @return
     */
    Boolean updatePerfect(Long idArticle, String articlePerfect);

    /**
     * 获取公告列表
     *
     * @return
     */
    List<ArticleDTO> findAnnouncements();

    /**
     * 变更文章状态
     *
     * @param idArticle
     * @param articleStatus
     * @param remarks
     * @return
     */
    Boolean updateStatus(Long idArticle, String articleStatus, String remarks);
}
