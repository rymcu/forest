package com.rymcu.vertical.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Data
public class Tag {
    /** 主键 */
    @Id
    @Column(name = "id")
    private Integer idTag ;
    /** 标签名 */
    private String tagName ;
    /** 标签图标 */
    private String tagIconUrl ;
    /** 标签uri */
    private String tagUri ;
    /** 描述 */
    private String tagDescription ;
    /** 浏览量 */
    private Integer tagViewCount ;
    /** 关联文章总数 */
    private Integer tagArticleCount ;
    /** 创建时间 */
    private Date createdTime ;
    /** 更新时间 */
    private Date updatedTime ;
}
