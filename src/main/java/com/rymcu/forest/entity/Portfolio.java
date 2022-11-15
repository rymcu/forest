package com.rymcu.forest.entity;


import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author ronger
 */
@Data
@Table(name = "forest_portfolio")
public class Portfolio {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    @Column(name = "id")
    private Long idPortfolio;
    /**
     * 作品集头像
     */
    @Column(name = "portfolio_head_img_url")
    private String headImgUrl;
    /**
     * 作品集名称
     */
    private String portfolioTitle;
    /**
     * 作品集作者
     */
    private Long portfolioAuthorId;
    /**
     * 作品集介绍
     */
    private String portfolioDescription;
    /**
     * 作品集介绍 Html
     */
    private String portfolioDescriptionHtml;
    /**
     * 创建时间
     */
    private Date createdTime;
    /**
     * 更新时间
     */
    private Date updatedTime;
    @Transient
    private String headImgType;
}
