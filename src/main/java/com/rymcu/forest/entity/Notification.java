package com.rymcu.forest.entity;

import com.alibaba.fastjson.annotation.JSONField;
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
@Table(name = "forest_notification")
public class Notification implements Serializable, Cloneable {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    @Column(name = "id")
    private Long idNotification;
    /**
     * 用户id
     */
    @Column(name = "id_user")
    private Long idUser;
    /**
     * 数据类型
     */
    @Column(name = "data_type")
    private String dataType;
    /**
     * 数据id
     */
    @Column(name = "data_id")
    private Long dataId;
    /**
     * 数据摘要
     */
    @Column(name = "data_summary")
    private String dataSummary;
    /**
     * 是否已读
     */
    @Column(name = "has_read")
    private String hasRead;
    /**
     * 是否已读
     */
    @Column(name = "created_time")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;
}
