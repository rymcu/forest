package com.rymcu.forest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.List;

/**
 * @author ronger
 */
@Data
public class PortfolioArticleDTO {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long idPortfolio;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long idArticle;

    private String headImgUrl;

    private String portfolioTitle;

    private Integer sortNo;

    private List<ArticleDTO> articles;

}
