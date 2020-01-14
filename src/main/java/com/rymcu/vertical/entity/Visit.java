package com.rymcu.vertical.entity;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 浏览表
 * @author ronger
 */
@Data
@Table(name="vertical_visit")
public class Visit implements Serializable,Cloneable {

    /** 主键 */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;
    /** 浏览链接 */
    private String visitUrl;
    /** IP */
    private String visitIp;
    /** User-Agent */
    private String visitUa;
    /** 城市 */
    private String visitCity;
    /** 设备唯一标识 */
    private String visitDeviceId;
    /** 浏览者 id */
    private Integer visitUserId;
    /** 上游链接 */
    private String visitRefererUrl;
    /** 创建时间 */
    private Date createdTime;
    /** 过期时间 */
    private Date expiredTime;

}
