package com.rymcu.forest.entity;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author ronger
 */
@Data
@Table(name = "forest_article_content")
public class ArticleContent {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "JDBC")
    private Long idArticle;

    private String articleContent;

    private String articleContentHtml;

    private Date createdTime;

    private Date updatedTime;

}
