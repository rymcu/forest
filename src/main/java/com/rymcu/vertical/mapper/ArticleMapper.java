package com.rymcu.vertical.mapper;

import com.rymcu.vertical.core.mapper.Mapper;
import com.rymcu.vertical.dto.ArticleDTO;
import com.rymcu.vertical.dto.Author;
import com.rymcu.vertical.entity.Article;
import com.rymcu.vertical.entity.ArticleContent;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ArticleMapper extends Mapper<Article> {
    List<ArticleDTO> selectArticles(@Param("searchText") String searchText, @Param("tag") String tag);

    Author selectAuthor(@Param("id") Integer id);

    ArticleDTO selectArticleDTOById(@Param("id") Integer id);

    Integer insertArticleContent(@Param("idArticle") Integer idArticle, @Param("articleContent") String articleContent, @Param("articleContentHtml") String articleContentHtml);

    Integer updateArticleContent(@Param("idArticle") Integer idArticle, @Param("articleContent") String articleContent, @Param("articleContentHtml") String articleContentHtml);

    ArticleContent selectArticleContent(@Param("idArticle") Integer idArticle);

    List<ArticleDTO> selectArticlesByTopicName(@Param("topicName") String topicName);

    List<ArticleDTO> selectArticlesByTagName(@Param("tagName") String tagName);

    List<ArticleDTO> selectUserArticles(@Param("idUser") Integer idUser);
}
