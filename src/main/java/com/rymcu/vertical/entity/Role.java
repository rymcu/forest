package com.rymcu.vertical.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "sys_role")
public class Role {
    @Id
    @Column(name = "id")
    private Integer idRole;

    /**
     * 角色名称
     * */
    @Column(name = "name")
    private String name;

    /**
     * 拼音码
     * */
    @Column(name = "input_code")
    private String inputCode;

    /**
     * 状态
     * */
    @Column(name = "status")
    private String status;

    /**
     * 创建时间
     * */
    @Column(name = "created_time")
    private Date createdTime;

    /**
     * 更新时间
     * */
    @Column(name = "updated_time")
    private Date updatedTime;
}