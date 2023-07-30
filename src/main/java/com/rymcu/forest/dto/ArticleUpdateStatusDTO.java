package com.rymcu.forest.dto;

import lombok.Data;

/**
 * Created on 2023/7/26 10:20.
 *
 * @author ronger
 * @email ronger-x@outlook.com
 * @desc : com.rymcu.forest.dto
 */
@Data
public class ArticleUpdateStatusDTO {

    private Long idArticle;

    private String articleStatus;

    private String remarks;

}
