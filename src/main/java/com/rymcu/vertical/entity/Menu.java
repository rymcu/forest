package com.rymcu.vertical.entity;

import lombok.Data;
import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "SYS_MENU")
public class Menu {

    @Id
    @Column(name = "id")
    private Integer idMenu;

    /**
     * 上级菜单ID
     * */
    @Column(name = "parent_id")
    private String parentId;

    /**
     * 菜单名称
     * */
    @Column(name = "NAME")
    private String name;

    /**
     * 排序
     * */
    @Column(name = "SORT")
    private Integer sort;

    /**
     * 路径
     * */
    @Column(name = "HREF")
    private String href;

    /**
     * 菜单类型
     * */
    @Column(name = "MENU_TYPE")
    private String menuType;

    /**
     * 权限标识
     * */
    @ColumnType(column = "PERMISSION",
    jdbcType = JdbcType.VARCHAR)
    private String permission;

    /**
     * 创建时间
     * */
    @Column(name = "created_time")
    private Date createdTime;

    /**
     * 状态
     * */
    @Column(name = "status")
    private String status;
}