package com.rymcu.forest.entity;


import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author ronger
 */
@Data
@Table(name = "forest_topic")
public class Topic {

    /**
     * 主键
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "JDBC")
    private Long idTopic;
    /**
     * 专题标题
     */
    private String topicTitle;
    /**
     * 专题路径
     */
    private String topicUri;
    /**
     * 专题描述
     */
    private String topicDescription;
    /**
     * 专题类型
     */
    private String topicType;
    /**
     * 专题序号;10
     */
    private Integer topicSort;
    /**
     * 专题图片路径
     */
    private String topicIconPath;
    /**
     * 0：作为导航1：不作为导航;0
     */
    private String topicNva;
    /**
     * 专题下标签总数;0
     */
    private Integer topicTagCount;
    /**
     * 0：正常1：禁用;0
     */
    private String topicStatus;
    /**
     * 创建时间
     */
    private Date createdTime;
    /**
     * 更新时间
     */
    private Date updatedTime;
    /**
     * 专题描述 Html
     */
    private String topicDescriptionHtml;
    /**
     * 专题图片类型
     */
    @Transient
    private transient String topicImageType;

}
