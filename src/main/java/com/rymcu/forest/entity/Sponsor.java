package com.rymcu.forest.entity;

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
@Table(name="vertical_sponsor")
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
    private String dataType;
    /**
     * 数据主键
     */
    private Integer dataId;
    /**
     * 赞赏人
     */
    private Integer sponsor;
    /**
     * 赞赏日期
     */
    private Date sponsorshipTime;
    /**
     * 赞赏金额
     */
    private BigDecimal sponsorshipMoney;
}
