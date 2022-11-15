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
 * 登录记录表
 *
 * @author ronger
 */
@Data
@Table(name = "forest_login_record")
public class LoginRecord implements Serializable, Cloneable {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    @Column(name = "id")
    private Long id;
    /**
     * IP
     */
    @Column(name = "login_ip")
    private String loginIp;
    /**
     * User-Agent
     */
    @Column(name = "login_ua")
    private String loginUa;
    /**
     * 城市
     */
    @Column(name = "login_city")
    private String loginCity;
    /**
     * 设备唯一标识
     */
    @Column(name = "login_device_id")
    private String loginDeviceId;
    /**
     * 设备操作系统
     */
    @Column(name = "login_os")
    private String loginOS;
    /**
     * 设备浏览器
     */
    @Column(name = "login_browser")
    private String loginBrowser;
    /**
     * 用户 id
     */
    @Column(name = "id_user")
    private Long idUser;
    /**
     * 创建时间
     */
    @Column(name = "created_time")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

}
