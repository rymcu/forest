package com.rymcu.vertical.entity;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Data
public class Article implements Serializable,Cloneable {
    /** 主键 */
    @Id
    @GeneratedValue
    private Integer id ;
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
    private Integer articleViewCount ;
    /** 预览内容 */
    private Integer articlePreviewContent ;
    /** 评论总数 */
    private Integer commentCount ;
    /** 过去时长 */
    private String timeAgo ;
    /** 文章永久链接 */
    private String articlePermalink ;
    /** 站内链接 */
    private String articleLink ;
    /** 创建时间 */
    private Date createdTime ;
    /** 更新时间 */
    private Date updatedTime ;
}
