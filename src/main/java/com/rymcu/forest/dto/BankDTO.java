package com.rymcu.forest.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author ronger
 */
@Data
public class BankDTO {

    private Integer idBank;
    /**
     * 银行名称
     */
    private String bankName;
    /**
     * 银行负责人
     */
    private Integer bankOwner;
    /**
     * 银行负责人
     */
    private String bankOwnerName;
    /**
     * 银行账户
     */
    private String bankAccount;
    /**
     * 账户余额
     */
    private BigDecimal accountBalance;
    /**
     * 银行描述
     */
    private String bankDescription;
    /**
     * 创建人
     */
    private Integer createdBy;
    /**
     * 创建时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;
}
