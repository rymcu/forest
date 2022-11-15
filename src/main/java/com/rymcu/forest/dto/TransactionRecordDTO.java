package com.rymcu.forest.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author ronger
 */
@Data
public class TransactionRecordDTO {

    private Integer idTransactionRecord;
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
     * 交易发起方
     */
    private BankAccountDTO formBankAccountInfo;
    /**
     * 交易收款方
     */
    private String toBankAccount;
    /**
     * 交易收款方
     */
    private BankAccountDTO toBankAccountInfo;
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
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date transactionTime;

}
