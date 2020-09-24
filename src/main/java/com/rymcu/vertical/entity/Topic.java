package com.rymcu.vertical.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author ronger
 */
@Data
@Table(name = "vertical_topic")
public class Topic {

    /** 主键 */
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "JDBC")
    private Integer idTopic;
    /** 专题标题 */
    private String topicTitle;
    /** 专题路径 */
    private String topicUri;
    /** 专题描述 */
    private String topicDescription;
    /** 专题类型 */
    private String topicType;
    /** 专题序号;10 */
    private Integer topicSort;
    /** 专题图片路径 */
    private String topicIconPath;
    /** 0：作为导航1：不作为导航;0 */
    private String topicNva;
    /** 专题下标签总数;0 */
    private Integer topicTagCount;
    /** 0：正常1：禁用;0 */
    private String topicStatus;
    /** 创建时间 */
    private Date createdTime;
    /** 更新时间 */
    private Date updatedTime;
    /** 专题描述 Html */
    private String topicDescriptionHtml;

}
