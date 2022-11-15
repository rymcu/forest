package com.rymcu.forest.entity;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 交易记录
 *
 * @author ronger
 */
@Table(name = "forest_transaction_record")
@Data
public class TransactionRecord {
    /**
     * 主键
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "JDBC")
    private Long idTransactionRecord;
    /**
     * 交易流水号
     */
    private String transactionNo;
    /**
     * 款项
     */
    private String funds;
    /**
     * 交易发起方
     */
    private String formBankAccount;
    /**
     * 交易收款方
     */
    private String toBankAccount;
    /**
     * 交易金额
     */
    private BigDecimal money;
    /**
     * 交易类型
     */
    private String transactionType;
    /**
     * 交易时间
     */
    private Date transactionTime;
}
