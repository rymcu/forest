package com.rymcu.forest.service;

import com.rymcu.forest.core.exception.ServiceException;
import com.rymcu.forest.core.service.Service;
import com.rymcu.forest.dto.TransactionRecordDTO;
import com.rymcu.forest.entity.TransactionRecord;

import java.util.List;

/**
 * @author ronger
 */
public interface TransactionRecordService extends Service<TransactionRecord> {
    /**
     * 交易
     * @param transactionRecord
     * @return
     */
    TransactionRecord transfer(TransactionRecord transactionRecord);

    /**
     * 查询指定账户的交易记录
     * @param bankAccount
     * @return
     */
    List<TransactionRecordDTO> findTransactionRecords(String bankAccount);
}
