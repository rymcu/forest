package com.rymcu.vertical.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "vertical_article_content")
public class ArticleContent {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "JDBC")
    private Integer idArticle;

    private String articleContent;

    private String articleContentHtml;

    private Date createdTime;

    private Date updatedTime;

}
