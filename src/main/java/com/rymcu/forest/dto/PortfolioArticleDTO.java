package com.rymcu.forest.dto;


import lombok.Data;

import java.util.List;

/**
 * @author ronger
 */
@Data
public class PortfolioArticleDTO {

    private Long id;

    private Long idPortfolio;

    private Long idArticle;

    private String headImgUrl;

    private String portfolioTitle;

    private Integer sortNo;

    private List<ArticleDTO> articles;

}
