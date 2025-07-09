package com.rymcu.forest.entity;


import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 货币发行记录
 *
 * @author ronger
 */
@Table(name = "forest_currency_issue")
@Data
public class CurrencyIssue {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;
    /**
     * 发行数额
     */
    private BigDecimal issueValue;
    /**
     * 发行人
     */
    private Long createdBy;
    /**
     * 发行时间
     */
    private Date createdTime;

}
