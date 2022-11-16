package com.rymcu.forest.dto;

import lombok.Data;

/**
 * @author ronger
 */
@Data
public class BankAccountSearchDTO {
    /**
     * 所属银行名称
     */
    private String bankName;
    /**
     * 银行账户
     */
    private String bankAccount;
    /**
     * 账户所有者姓名
     */
    private String accountOwnerName;

}
