package com.rymcu.forest.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Created on 2022/6/13 21:51.
 *
 * @author ronger
 * @email ronger-x@outlook.com
 * @desc : 产品详情表
 */
@Data
@Table(name = "forest_product_content")
public class ProductContent implements Serializable {
    /**
     * 产品表主键
     */
    private Long idProduct;
    /**
     * 产品详情原文
     */
    private String productContent;
    /**
     * 产品详情;Html
     */
    private String productContentHtml;
    /**
     * 创建时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;
    /**
     * 更新时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updatedTime;
}
