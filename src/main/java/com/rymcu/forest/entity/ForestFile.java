package com.rymcu.forest.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author caterpillar
 */
@Data
@Table(name = "forest_File")
public class ForestFile {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    @Column(name = "id")
    private Long id;

    /**
     * 访问路径
     */
    @Column(name = "web_path")
    private String webPath;

    /**
     * 上传路径
     */
    @Column(name = "upload_path")
    private String uploadPath;

    /**
     * md5
     */
    @Column(name = "md5_value")
    private String md5Value;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;


}
