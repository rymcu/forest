package com.rymcu.vertical.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Data
public class Article implements Serializable,Cloneable {
    /** 主键 */
    @Id
    @GeneratedValue(generator = "JDBC")
    @Column(name = "id")
    private Integer idArticle ;
    /** 文章标题 */
    private String articleTitle ;
    /** 文章缩略图 */
    private String articleThumbnailUrl ;
    /** 文章作者id */
    private Integer articleAuthorId ;
    /** 文章类型 */
    private String articleType ;
    /** 文章标签 */
    private String articleTags ;
    /** 浏览总数 */
    private Integer articleViewCount;
    /** 预览内容 */
    private String articlePreviewContent ;
    /** 评论总数 */
    private Integer commentCount ;
    /** 文章永久链接 */
    private String articlePermalink ;
    /** 站内链接 */
    private String articleLink ;
    /** 创建时间 */
    private Date createdTime ;
    /** 更新时间 */
    private Date updatedTime ;
}
