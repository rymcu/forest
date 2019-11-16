package com.rymcu.vertical.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "SYS_ROLE")
public class Role {
    @Id
    @Column(name = "ID_ROLE")
    private String idRole;

    /**
     * 角色名称
     * */
    @Column(name = "NAME")
    private String name;

    /**
     * 拼音码
     * */
    @Column(name = "INPUT_CODE")
    private String inputCode;

    /**
     * 状态
     * */
    @Column(name = "STATUS")
    private Integer status;

    /**
     * 创建时间
     * */
    @Column(name = "CREATED_TIME")
    private Date createdTime;
}