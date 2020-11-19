package com.rymcu.forest.dto;

import lombok.Data;

/**
 * @author ronger
 */
@Data
public class ArticleTagDTO {

    private Integer idArticleTag;

    private Integer idTag;

    private Integer idArticle;

    private String tagTitle;

    private String tagUri;

    private String tagDescription;

    private String tagIconPath;

    private Integer tagAuthorId;
}
