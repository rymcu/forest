package com.rymcu.forest.entity;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author ronger
 */
@Data
@Table(name = "forest_follow")
public class Follow implements Serializable, Cloneable {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    @Column(name = "id")
    private Long idFollow;
    /**
     * 关注者 id
     */
    @Column(name = "follower_id")
    private Long followerId;
    /**
     * 关注数据 id
     */
    @Column(name = "following_id")
    private Long followingId;
    /**
     * 0：用户，1：标签，2：帖子收藏，3：帖子关注
     */
    @Column(name = "following_type")
    private String followingType;
}
