package com.rymcu.forest.mapper;

import com.rymcu.forest.core.mapper.Mapper;
import com.rymcu.forest.dto.ArticleDTO;
import com.rymcu.forest.dto.ArticleTagDTO;
import com.rymcu.forest.dto.PortfolioArticleDTO;
import com.rymcu.forest.entity.Article;
import com.rymcu.forest.entity.ArticleContent;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ronger
 */
public interface ArticleMapper extends Mapper<Article> {

    /**
     * 获取文章列表
     * @param searchText
     * @param tag
     * @param topicUri
     * @return
     */
    List<ArticleDTO> selectArticles(@Param("searchText") String searchText, @Param("tag") String tag, @Param("topicUri") String topicUri);

    /**
     * 根据文章 ID 查询文章
     * @param id
     * @param type
     * @return
     */
    ArticleDTO selectArticleDTOById(@Param("id") Integer id, @Param("type") int type);

    /**
     * 保存文章内容
     * @param idArticle
     * @param articleContent
     * @param articleContentHtml
     * @return
     */
    Integer insertArticleContent(@Param("idArticle") Integer idArticle, @Param("articleContent") String articleContent, @Param("articleContentHtml") String articleContentHtml);

    /**
     * 更新文章内容
     * @param idArticle
     * @param articleContent
     * @param articleContentHtml
     * @return
     */
    Integer updateArticleContent(@Param("idArticle") Integer idArticle, @Param("articleContent") String articleContent, @Param("articleContentHtml") String articleContentHtml);

    /**
     * 获取文章正文内容
     * @param idArticle
     * @return
     */
    ArticleContent selectArticleContent(@Param("idArticle") Integer idArticle);

    /**
     * 获取主题下文章列表
     * @param topicName
     * @return
     */
    List<ArticleDTO> selectArticlesByTopicUri(@Param("topicName") String topicName);

    /**
     * 获取标签下文章列表
     * @param tagName
     * @return
     */
    List<ArticleDTO> selectArticlesByTagName(@Param("tagName") String tagName);

    /**
     * 获取用户文章列表
     * @param idUser
     * @return
     */
    List<ArticleDTO> selectUserArticles(@Param("idUser") Integer idUser);

    /**
     * 删除文章标签
     * @param id
     * @return
     */
    Integer deleteTagArticle(@Param("id") Integer id);

    /**
     * 获取文章标签列表
     * @param idArticle
     * @return
     */
    List<ArticleTagDTO> selectTags(@Param("idArticle") Integer idArticle);

    /**
     * 更新文章浏览数
     * @param id
     * @param articleViewCount
     * @return
     */
    Integer updateArticleViewCount(@Param("id") Integer id, @Param("articleViewCount") Integer articleViewCount);

    /**
     * 获取草稿列表
     * @param idUser
     * @return
     */
    List<ArticleDTO> selectDrafts(@Param("idUser") Integer idUser);

    /**
     * 删除未使用的文章标签
     * @param idArticleTag
     * @return
     */
    Integer deleteUnusedArticleTag(@Param("idArticleTag") Integer idArticleTag);

    /**
     * 查询作品集下文章
     * @param idPortfolio
     * @return
     */
    List<ArticleDTO> selectArticlesByIdPortfolio(@Param("idPortfolio") Integer idPortfolio);

    /**
     * 查询作品集未绑定文章
     * @param idPortfolio
     * @param searchText
     * @param idUser
     * @return
     */
    List<ArticleDTO> selectUnbindArticlesByIdPortfolio(@Param("idPortfolio") Integer idPortfolio, @Param("searchText") String searchText, @Param("idUser") Integer idUser);

    /**
     * 查询文章所属作品集列表
     * @param idArticle
     * @return
     */
    List<PortfolioArticleDTO> selectPortfolioArticles(@Param("idArticle") Integer idArticle);

    /**
     * 更新文章标签
     * @param idArticle
     * @param tags
     * @return
     */
    Integer updateArticleTags(@Param("idArticle") Integer idArticle, @Param("tags") String tags);

    /**
     * 判断是否有评论
     * @param id
     * @return
     */
    boolean existsCommentWithPrimaryKey(@Param("id") Integer id);

    /**
     * 删除关联作品集数据
     * @param id
     * @return
     */
    Integer deleteLinkedPortfolioData(@Param("id") Integer id);

    /**
     * 更新文章连接及预览内容
     * @param idArticle
     * @param articleLink
     * @param articlePermalink
     * @param articlePreviewContent
     * @return
     */
    Integer updateArticleLinkAndPreviewContent(@Param("idArticle") Integer idArticle, @Param("articleLink") String articleLink, @Param("articlePermalink") String articlePermalink, @Param("articlePreviewContent") String articlePreviewContent);

    /**
     * 根据专题主键及当前文章排序号获取专题下文章大纲
     * @param idPortfolio
     * @param sortNo
     * @return
     */
    List<ArticleDTO> selectPortfolioArticlesByIdPortfolioAndSortNo(@Param("idPortfolio") Integer idPortfolio, @Param("sortNo") Integer sortNo);

    /**
     * 更新文章优选状态
     * @param idArticle
     * @param articlePerfect
     * @return
     */
    int updatePerfect(@Param("idArticle") Integer idArticle, @Param("articlePerfect") String articlePerfect);
}
