package com.rymcu.forest.mapper;

import com.rymcu.forest.core.mapper.Mapper;
import com.rymcu.forest.dto.TransactionRecordDTO;
import com.rymcu.forest.entity.TransactionRecord;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author ronger
 */
public interface TransactionRecordMapper extends Mapper<TransactionRecord> {
    /**
     * 交易
     * @param formBankAccount
     * @param toBankAccount
     * @param money
     * @return
     */
    Integer transfer(@Param("formBankAccount") String formBankAccount, @Param("toBankAccount") String toBankAccount, @Param("money") BigDecimal money);

    /**
     * 查询指定账户的交易记录
     * @param bankAccount
     * @return
     */
    List<TransactionRecordDTO> selectTransactionRecords(@Param("bankAccount") String bankAccount);
}
