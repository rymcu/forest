package com.rymcu.forest.service;

import com.rymcu.forest.core.service.Service;
import com.rymcu.forest.dto.TransactionRecordDTO;
import com.rymcu.forest.entity.TransactionRecord;
import com.rymcu.forest.enumerate.TransactionEnum;

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
     * @param startDate
     * @param endDate
     * @return
     */
    List<TransactionRecordDTO> findTransactionRecords(String bankAccount, String startDate, String endDate);

    /**
     * 根据用户主键进行交易
     * @param toUserId
     * @param formUserId
     * @param transactionType
     * @return
     * @throws Exception
     */
    TransactionRecord userTransfer(Integer toUserId, Integer formUserId, TransactionEnum transactionType) throws Exception;

    /**
     * 社区银行转账/奖励发放
     * @param idUser
     * @param transactionType
     * @return
     * @throws Exception
     */
    TransactionRecord bankTransfer(Integer idUser, TransactionEnum transactionType) throws Exception;
}
