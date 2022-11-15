package com.rymcu.forest.entity;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author ronger
 */
@Table(name = "forest_currency_rule")
@Data
public class CurrencyRule implements Serializable, Cloneable {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    @Column(name = "id")
    private Long idCurrencyRule;
    /**
     * 规则名称
     */
    private String ruleName;
    /**
     * 规则标志(与枚举变量对应)
     */
    private String ruleSign;
    /**
     * 规则描述
     */
    private String ruleDescription;
    /**
     * 金额
     */
    private BigDecimal money;
    /**
     * 奖励(0)/消耗(1)状态
     */
    private String awardStatus;
    /**
     * 上限金额
     */
    private BigDecimal maximumMoney;
    /**
     * 重复(0: 不重复,单位:天)
     */
    private Integer repeatDays;
    /**
     * 状态
     */
    private String status;
}
