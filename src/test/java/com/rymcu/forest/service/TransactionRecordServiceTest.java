package com.rymcu.forest.service;

import com.rymcu.forest.base.BaseServiceTest;
import com.rymcu.forest.dto.TransactionRecordDTO;
import com.rymcu.forest.entity.TransactionRecord;
import com.rymcu.forest.enumerate.TransactionEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TransactionRecordServiceTest extends BaseServiceTest {

    @Autowired
    private TransactionRecordService transactionRecordService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void transfer() {
        TransactionRecord transactionRecord = new TransactionRecord();
        transactionRecord.setToBankAccount("100000061");
        transactionRecord.setFormBankAccount("100000063");
        transactionRecord.setMoney(BigDecimal.TEN);
        transactionRecordService.transfer(transactionRecord);
        assertNotNull(transactionRecord.getIdTransactionRecord());
    }

    @Test
    @DisplayName("查询指定账户的交易记录")
    void findTransactionRecords() {

        List<TransactionRecordDTO> transactionRecords = transactionRecordService.findTransactionRecords("100000001", "2020-05-05", "2025-05-05");

        assertTrue(transactionRecords.isEmpty());
    }

    @Test
    @DisplayName("根据用户主键进行交易")
    void userTransfer() {
        TransactionRecord transactionRecord = transactionRecordService.userTransfer(65001L, 65003L, TransactionEnum.ArticleSponsor);
        assertNotNull(transactionRecord);
        assertNotNull(transactionRecord.getIdTransactionRecord());
    }

    @Test
    @DisplayName("社区银行转账/奖励发放")
    void bankTransfer() {
        TransactionRecord transactionRecord = transactionRecordService.bankTransfer(65001L, TransactionEnum.Answer);
        assertNotNull(transactionRecord);
        assertNotNull(transactionRecord.getIdTransactionRecord());
    }

    @Test
    @DisplayName("发放新手奖励")
    void newbieRewards() {
        TransactionRecord transactionRecord = new TransactionRecord();
        transactionRecord.setToBankAccount("100000061");
        transactionRecord = transactionRecordService.newbieRewards(transactionRecord);
        assertNotNull(transactionRecord.getIdTransactionRecord());
    }
}