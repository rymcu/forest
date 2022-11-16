package com.rymcu.forest.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author ronger
 */
@Table(name = "forest_user")
@Data
public class User implements Serializable, Cloneable {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "JDBC")
    private Long idUser;

    /**
     * 登录账号
     */
    @Column(name = "account")
    private String account;

    /**
     * 密码
     */
    @Column(name = "password")
    @JSONField(serialize = false)
    private String password;

    /**
     * 昵称
     */
    @Column(name = "nickname")
    private String nickname;

    /**
     * 真实姓名
     */
    @Column(name = "real_name")
    private String realName;

    /**
     * 性别 1:男性 2:女性
     */
    @Column(name = "sex")
    private String sex;

    /**
     * 头像文件类型
     */
    @Column(name = "avatar_type")
    private String avatarType;

    /**
     * 头像路径
     */
    @Column(name = "avatar_url")
    private String avatarUrl;

    /**
     * 邮箱地址
     */
    @ColumnType(column = "email",
            jdbcType = JdbcType.VARCHAR)
    private String email;

    /**
     * 手机号码
     */
    @ColumnType(column = "phone",
            jdbcType = JdbcType.VARCHAR)
    private String phone;

    /**
     * 签名
     */
    @ColumnType(column = "signature",
            jdbcType = JdbcType.VARCHAR)
    private String signature;

    /**
     * 状态
     */
    @Column(name = "status")
    private String status;

    /**
     * 最后登录时间
     */
    @Column(name = "last_login_time")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date lastLoginTime;

    /**
     * 创建时间
     */
    @Column(name = "created_time")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    /**
     * 更新时间
     */
    @Column(name = "updated_time")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updatedTime;

    /**
     * 最后在线时间
     */
    @Column(name = "last_online_time")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date lastOnlineTime;

    /**
     * 个人中心背景图片
     */
    @Column(name = "bg_img_url")
    private String bgImgUrl;
}