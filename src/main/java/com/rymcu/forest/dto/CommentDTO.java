package com.rymcu.forest.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;

/**
 * @author ronger
 */
@Data
public class CommentDTO {
    private Integer idComment;
    /**
     * 评论内容
     */
    private String commentContent;
    /**
     * 作者 id
     */
    private Integer commentAuthorId;
    /**
     * 文章 id
     */
    private Integer commentArticleId;
    /**
     * 锚点 url
     */
    private String commentSharpUrl;
    /**
     * 父评论 id
     */
    private Integer commentOriginalCommentId;
    /**
     * 父评论作者头像
     */
    private String commentOriginalAuthorThumbnailURL;
    /**
     * 父评论作者昵称
     */
    private String commentOriginalAuthorNickname;
    /**
     * 父评论作者昵称
     */
    private String commentOriginalContent;
    /**
     * 状态
     */
    private String commentStatus;
    /**
     * 0：公开回帖，1：匿名回帖
     */
    private String commentAnonymous;
    /**
     * 回帖计数
     */
    private Integer commentReplyCount;
    /**
     * 0：所有人可见，1：仅楼主和自己可见
     */
    private String commentVisible;
    /**
     * 创建时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    private Author commenter;

    private String timeAgo;

    private String articleTitle;
}
