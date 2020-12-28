package com.rymcu.forest.service;

import com.rymcu.forest.core.service.Service;
import com.rymcu.forest.dto.TransactionRecordDTO;
import com.rymcu.forest.entity.TransactionRecord;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author ronger
 */
public interface TransactionRecordService extends Service<TransactionRecord> {
    /**
     * 交易
     * @param transactionRecord
     * @return
     * @throws Exception
     */
    TransactionRecord transfer(TransactionRecord transactionRecord) throws Exception;

    /**
     * 查询指定账户的交易记录
     * @param bankAccount
     * @return
     */
    List<TransactionRecordDTO> findTransactionRecords(String bankAccount);

    /**
     * 根据用户主键进行交易
     * @param toUserId
     * @param formUserId
     * @param money
     * @return
     * @throws Exception
     */
    TransactionRecord transferByUserId(Integer toUserId, Integer formUserId, BigDecimal money) throws Exception;
}
