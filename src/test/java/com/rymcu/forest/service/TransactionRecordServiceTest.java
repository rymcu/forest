/*
 * Copyright (c) 2020 RYMCU (https://rymcu.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */


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