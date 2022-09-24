package com.rymcu.forest.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author ronger
 */
@Data
@Table(name = "forest_permission")
public class Permission implements Serializable,Cloneable {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "JDBC")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long idPermission;

    /**
     * 权限标识
     * */
    @ColumnType(column = "permission_category")
    private String permissionCategory;
}