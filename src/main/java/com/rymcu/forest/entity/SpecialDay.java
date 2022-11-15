package com.rymcu.forest.entity;


import lombok.Data;

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
@Table(name = "forest_special_day")
public class SpecialDay implements Serializable, Cloneable {
    /**
     *
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "JDBC")
    private Long idSpecialDay;
    /**
     * 名称
     */
    private String specialDayName;
    /**
     * 权重/优先级,小数优秀
     */
    private Integer weights;
    /**
     * 开始时间
     */
    private Date startTime;
    /**
     * 过期时间
     */
    private Date expirationTime;
    /**
     * 是否重复
     */
    private Integer repeat;
    /**
     * 重复周期
     */
    private Integer repeatCycle;
    /**
     * 0:天1:周2:月3:年
     */
    private Integer repeatCycleUnit;
    /**
     * 创建时间
     */
    private Date createdTime;
    /**
     * 图片路径
     */
    private String imgUrl;
    /**
     * 执行全局样式
     */
    private String cssStyle;

}
