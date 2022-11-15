package com.rymcu.forest.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 银行账户
 *
 * @author ronger
 */
@Table(name = "forest_bank_account")
@Data
public class BankAccount {
    /**
     * 主键
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "JDBC")
    private Long idBankAccount;
    /**
     * 所属银行
     */
    private Long idBank;
    /**
     * 银行账户
     */
    private String bankAccount;
    /**
     * 账户余额
     */
    private BigDecimal accountBalance;
    /**
     * 账户所有者
     */
    private Long accountOwner;
    /**
     * 创建时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;
    /**
     * 账户类型
     */
    private String accountType;
}
