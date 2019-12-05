package com.rymcu.vertical.entity;

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
@Data
@Table(name = "vertical_permission")
public class Permission implements Serializable,Cloneable {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "JDBC")
    private Integer idPermission;

    /**
     * 权限标识
     * */
    @ColumnType(column = "permission_category")
    private String permissionCategory;
}