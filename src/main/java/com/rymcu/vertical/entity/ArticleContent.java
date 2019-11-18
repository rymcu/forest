package com.rymcu.vertical.entity;

import lombok.Data;

import java.util.Date;

@Data
public class ArticleContent {

    private Integer idArticle;

    private String articleContent;

    private String articleContentHtml;

    private Date createdTime;

    private Date updatedTime;

}
