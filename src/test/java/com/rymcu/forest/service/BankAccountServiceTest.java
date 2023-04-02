package com.rymcu.forest.service;

import com.rymcu.forest.base.BaseServiceTest;
import com.rymcu.forest.dto.BankAccountDTO;
import com.rymcu.forest.dto.BankAccountSearchDTO;
import com.rymcu.forest.dto.TransactionRecordDTO;
import com.rymcu.forest.entity.BankAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BankAccountServiceTest extends BaseServiceTest {

    /**
     * 待查询银行账户列表
     */
    private final BankAccountSearchDTO bankAccountSearchDTO;
    private final Long idUser = 65001L;

    @Autowired
    private ArticleThumbsUpService articleThumbsUpService;
    @Autowired
    private BankAccountService bankAccountService;

    {
        bankAccountSearchDTO = new BankAccountSearchDTO();

    }

    @Test
    @BeforeEach
    @DisplayName("创建钱包账号")
    public void createBankAccount() {

        BankAccount bankAccount = bankAccountService.createBankAccount(idUser);
        assertEquals(idUser, bankAccount.getAccountOwner());
    }

    @Test
    @DisplayName("测试查询银行账户列表")
    public void findBankAccounts() {
        List<BankAccountDTO> bankAccounts = bankAccountService.findBankAccounts(bankAccountSearchDTO);
        assertEquals(1L, bankAccounts.size());
    }

    @Test
    @DisplayName("测试查询银行账户")
    public void findBankAccountByIdUser() {
        BankAccountDTO bankAccountByIdUser = bankAccountService.findBankAccountByIdUser(idUser);
        assertEquals(idUser.intValue(), bankAccountByIdUser.getAccountOwner());


    }

    @Test
    @DisplayName("根据时间查询账号交易记录")
    public void findUserTransactionRecords() {
        List<TransactionRecordDTO> userTransactionRecords = bankAccountService.findUserTransactionRecords(idUser.toString(), "", "");
        assertTrue(userTransactionRecords.isEmpty());
    }
}