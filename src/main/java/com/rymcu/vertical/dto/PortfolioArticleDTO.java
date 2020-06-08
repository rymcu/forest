package com.rymcu.vertical.dto;

import lombok.Data;

/**
 * @author ronger
 */
@Data
public class PortfolioArticleDTO {

    private Integer id;

    private Integer idPortfolio;

    private Integer idArticle;

    private String headImgUrl;

    private String portfolioTitle;

    private Integer sortNo;

}
