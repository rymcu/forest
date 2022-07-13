package com.rymcu.forest.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author ronger
 */
@Data
@Table(name="forest_sponsor")
public class Sponsor implements Serializable, Cloneable {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;
    /**
     * 数据类型
     */
    private Integer dataType;
    /**
     * 数据主键
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long dataId;
    /**
     * 赞赏人
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long sponsor;
    /**
     * 赞赏日期
     */
    private Date sponsorshipTime;
    /**
     * 赞赏金额
     */
    private BigDecimal sponsorshipMoney;
}
