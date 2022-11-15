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
 * 浏览表
 *
 * @author ronger
 */
@Data
@Table(name = "forest_visit")
public class Visit implements Serializable, Cloneable {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    @Column(name = "id")
    private Long id;
    /**
     * 浏览链接
     */
    @Column(name = "visit_url")
    private String visitUrl;
    /**
     * IP
     */
    @Column(name = "visit_ip")
    private String visitIp;
    /**
     * User-Agent
     */
    @Column(name = "visit_ua")
    private String visitUa;
    /**
     * 城市
     */
    @Column(name = "visit_city")
    private String visitCity;
    /**
     * 设备唯一标识
     */
    @Column(name = "visit_device_id")
    private String visitDeviceId;
    /**
     * 浏览者 id
     */
    @Column(name = "visit_user_id")
    private Long visitUserId;
    /**
     * 上游链接
     */
    @Column(name = "visit_referer_url")
    private String visitRefererUrl;
    /**
     * 创建时间
     */
    @Column(name = "created_time")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;
    /**
     * 过期时间
     */
    @Column(name = "expired_time")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date expiredTime;

}
