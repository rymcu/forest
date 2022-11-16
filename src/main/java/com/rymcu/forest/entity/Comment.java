package com.rymcu.forest.entity;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author ronger
 */
@Data
@Table(name = "forest_comment")
public class Comment implements Serializable, Cloneable {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    @Column(name = "id")
    private Long idComment;
    /**
     * 评论内容
     */
    @Column(name = "comment_content")
    private String commentContent;
    /**
     * 作者 id
     */
    @Column(name = "comment_author_id")
    private Long commentAuthorId;
    /**
     * 文章 id
     */
    @Column(name = "comment_article_id")
    private Long commentArticleId;
    /**
     * 锚点 url
     */
    @Column(name = "comment_sharp_url")
    private String commentSharpUrl;
    /**
     * 父评论 id
     */
    @Column(name = "comment_original_comment_id")
    private Long commentOriginalCommentId;
    /**
     * 状态
     */
    @Column(name = "comment_status")
    private String commentStatus;
    /**
     * 评论 IP
     */
    @Column(name = "comment_ip")
    private String commentIP;
    /**
     * User-Agent
     */
    @Column(name = "comment_ua")
    private String commentUA;
    /**
     * 0：公开回帖，1：匿名回帖
     */
    @Column(name = "comment_anonymous")
    private String commentAnonymous;
    /**
     * 回帖计数
     */
    @Column(name = "comment_reply_count")
    private Integer commentReplyCount;
    /**
     * 0：所有人可见，1：仅楼主和自己可见
     */
    @Column(name = "comment_visible")
    private String commentVisible;
    /**
     * 创建时间
     */
    @Column(name = "created_time")
    private Date createdTime;
}
