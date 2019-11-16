package com.rymcu.vertical.entity;

import lombok.Data;
import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "SYS_USER")
@Data
public class User {
    @Id
    @Column(name = "ID_USER")
    private Integer idUser;

    /**
     * 应用ID
     * */
    @Column(name = "APP_ID")
    private String appId;

    /**
     * 登录账号
     * */
    @Column(name = "ACCOUNT")
    private String account;

    /**
     * 密码
     * */
    @Column(name = "PASSWORD")
    private String password;

    /**
     * 昵称
     * */
    @Column(name = "NICK_NAME")
    private String nickName;

    /**
     * 真实姓名
     * */
    @Column(name = "REAL_NAME")
    private String realName;

    /**
     * 性别 1:男性 2:女性
     * */
    @Column(name = "SEX")
    private Integer sex;

    /**
     * 头像文件类型
     * */
    @Column(name = "AVATAR_TYPE")
    private Integer avatarType;

    /**
     * 头像路径
     * */
    @Column(name = "AVATAR_URL")
    private String avatarUrl;

    /**
     * 邮箱地址
     * */
    @ColumnType(column = "EMAIL",
            jdbcType = JdbcType.VARCHAR)
    private String email;

    /**
     * 手机号码
     * */
    @ColumnType(column = "PHONE",
            jdbcType = JdbcType.VARCHAR)
    private String phone;

    /**
     * 签名
     * */
    @ColumnType(column = "SIGNATURE",
            jdbcType = JdbcType.VARCHAR)
    private String signature;

    /**
     * 状态
     * */
    @Column(name = "STATUS")
    private Integer status;

    /**
     * 最后登录时间
     * */
    @Column(name = "LAST_LOGIN_TIME")
    private Date lastLoginTime;

    /**
     * 创建时间
     * */
    @Column(name = "CREATED_TIME")
    private Date createdTime;
}