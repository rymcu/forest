package com.rymcu.forest.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author ronger
 */
@Data
public class BankAccountDTO {

    private Integer idBankAccount;
    /**
     * 所属银行
     */
    private Integer idBank;
    /**
     * 所属银行名称
     */
    private String bankName;
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
    private Integer accountOwner;
    /**
     * 账户所有者姓名
     */
    private String accountOwnerName;
    /**
     * 创建时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    private List<TransactionRecordDTO> transactionRecords;

}
