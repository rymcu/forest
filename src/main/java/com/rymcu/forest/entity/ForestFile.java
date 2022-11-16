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
@Table(name = "forest_file")
public class ForestFile {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    @Column(name = "id")
    private Long id;

    /**
     * 文件大小
     */
    @Column(name = "file_size")
    private Long fileSize;

    /**
     * 文件类型-文件后缀
     */
    @Column(name = "file_type")
    private String fileType;
    /**
     * 访问路径
     */
    @Column(name = "file_url")
    private String fileUrl;

    /**
     * 上传路径
     */
    @Column(name = "file_path")
    private String filePath;

    /**
     * md5
     */
    @Column(name = "md5_value")
    private String md5Value;

    /**
     * 创建时间
     */
    @Column(name = "created_time")
    private Date createdTime;

    /**
     * 更新时间
     */
    @Column(name = "updated_time")
    private Date updatedTime;

    /**
     * 创建人
     */
    @Column(name = "created_by")
    private Long createdBy;


}
