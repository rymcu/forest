package com.rymcu.forest.lucene.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ArticleLucene
 *
 * @author suwen
 * @date 2021/2/3 09:57
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleLucene {

    /**
     * 文章编号
     */
    private Long idArticle;

    /**
     * 文章标题
     */
    private String articleTitle;

    /**
     * 文章内容
     */
    private String articleContent;

    /**
     * 相关度评分
     */
    private String score;
}
