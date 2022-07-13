package com.rymcu.forest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * @author ronger
 */
@Data
public class ArticleTagDTO {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long idArticleTag;

    private Integer idTag;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long idArticle;

    private String tagTitle;

    private String tagUri;

    private String tagDescription;

    private String tagIconPath;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long tagAuthorId;
}
