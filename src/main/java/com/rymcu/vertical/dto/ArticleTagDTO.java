package com.rymcu.vertical.dto;

import lombok.Data;

/**
 * @author ronger
 */
@Data
public class ArticleTagDTO {
    private Integer idTag;

    private String tagTitle;

    private String tagUri;

    private String tagDescription;

    private String tagIconPath;

    private Integer tagAuthorId;
}
